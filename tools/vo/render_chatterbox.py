"""Renders voice lines with Chatterbox TTS (MIT) and the same radio chain as render_radio.py.

Chatterbox gives natural pacing and emotion; a short reference clip sets the timbre.
Usage:
  python3 tools/vo/render_chatterbox.py --ref <voice.wav> [--only sector01] [--exaggeration 0.5]
         [--cfg 0.5] [--out <dir>] [--dry]
Lines come from tools/vo/lines.tsv; output: assets/sound/radio/<id>.mp3 (or --out).
"""
import argparse
import os
import subprocess
import sys
import tempfile

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from render_radio import LINES, OUT, RADIO  # noqa: E402


def save_wav(path, tensor, rate):
    import wave
    import numpy as np
    x = tensor.detach().cpu().numpy().reshape(-1)
    x = np.clip(x, -1.0, 1.0)
    with wave.open(path, "wb") as w:
        w.setnchannels(1)
        w.setsampwidth(2)
        w.setframerate(rate)
        w.writeframes((x * 32767).astype("<i2").tobytes())


def main():
    import torch
    import perth
    if getattr(perth, "PerthImplicitWatermarker", None) is None:
        # The optional neural watermarker failed to import; the dummy one keeps the API.
        perth.PerthImplicitWatermarker = perth.DummyWatermarker
    from chatterbox.tts import ChatterboxTTS
    ap = argparse.ArgumentParser()
    ap.add_argument("--ref", required=True)
    ap.add_argument("--only", default=None)
    ap.add_argument("--exaggeration", type=float, default=0.45)
    ap.add_argument("--cfg", type=float, default=0.4)
    ap.add_argument("--out", default=OUT)
    ap.add_argument("--dry", action="store_true", help="also keep the unfiltered wav")
    a = ap.parse_args()
    device = "mps" if torch.backends.mps.is_available() else "cpu"
    model = ChatterboxTTS.from_pretrained(device=device)
    os.makedirs(a.out, exist_ok=True)
    with open(LINES, encoding="utf-8") as f:
        rows = [l.rstrip("\n").split("\t", 1) for l in f if l.strip()]
    for line_id, text in rows:
        if a.only and not line_id.startswith(a.only):
            continue
        torch.manual_seed(19)
        wav = model.generate(text, audio_prompt_path=a.ref, exaggeration=a.exaggeration, cfg_weight=a.cfg)
        with tempfile.TemporaryDirectory() as tmp:
            dry = os.path.join(tmp, "dry.wav")
            save_wav(dry, wav, model.sr)
            if a.dry:
                save_wav(os.path.join(a.out, line_id + "_dry.wav"), wav, model.sr)
            mp3 = os.path.join(a.out, line_id + ".mp3")
            subprocess.run(["ffmpeg", "-y", "-loglevel", "error", "-i", dry, "-filter_complex", RADIO,
                            "-map", "[out]", "-ac", "1", "-c:a", "libmp3lame", "-q:a", "5", mp3], check=True)
            print(mp3, flush=True)


if __name__ == "__main__":
    main()
