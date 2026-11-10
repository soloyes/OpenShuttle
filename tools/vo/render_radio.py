#!/usr/bin/env python3
"""
Renders the radio voice lines (sector briefings) to assets/sound/radio/<id>.mp3.

Text -> Piper TTS (open voice, commercial-safe licence) -> radio chain in ffmpeg:
band-limited to a transmitter, compressed, soft-clipped, a static bed under the voice and
squelch bursts at key-up and key-down.

Usage:
  pip install piper-tts            (ffmpeg must be on PATH)
  python3 tools/vo/render_radio.py --model <dir>/en_US-joe-medium.onnx [--speaker N] [--only sector01]

Voice models: https://huggingface.co/rhasspy/piper-voices (check MODEL_CARD licence;
joe = CC0, norman = public domain, libritts_r = CC BY 4.0 -> credit "LibriTTS-R").
"""
import argparse
import os
import subprocess
import sys
import tempfile

ROOT = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
LINES = os.path.join(os.path.dirname(os.path.abspath(__file__)), "lines.tsv")
OUT = os.path.join(ROOT, "assets", "sound", "radio")

RADIO = (
    "[0:a]aresample=44100,highpass=f=320,lowpass=f=3300,"
    "acompressor=threshold=-22dB:ratio=6:attack=4:release=90:makeup=4,"
    "asoftclip=type=tanh:threshold=0.6,volume=0.9,"
    "adelay=420|420,apad=pad_dur=0.5[v];"
    # Static bed for the whole transmission, with slow flutter.
    "anoisesrc=color=pink:amplitude=0.05:sample_rate=44100,highpass=f=500,lowpass=f=4200,"
    "tremolo=f=7:d=0.5[n];"
    # Squelch: short bright noise bursts at key-up and key-down.
    "anoisesrc=color=white:amplitude=0.35:sample_rate=44100:duration=0.14,highpass=f=1200,"
    "afade=t=out:st=0.02:d=0.12[s1];"
    "[v][n]amix=inputs=2:duration=first:weights=1 0.55:normalize=0[vn];"
    "[s1][vn]concat=n=2:v=0:a=1[pre];"
    "anoisesrc=color=white:amplitude=0.3:sample_rate=44100:duration=0.18,highpass=f=1000,"
    "afade=t=out:st=0.0:d=0.18[s2];"
    "[pre][s2]concat=n=2:v=0:a=1,afade=t=in:d=0.01,alimiter=limit=0.9[out]"
)


def short_espeak_dir():
    """espeak-ng silently ignores long data paths (fixed buffer), so link it from a short one."""
    import piper
    real = os.path.join(os.path.dirname(piper.__file__), "espeak-ng-data")
    if len(real) < 80:
        return real
    link = os.path.join(tempfile.gettempdir(), "espk")
    if os.path.realpath(link) != os.path.realpath(real):
        if os.path.islink(link):
            os.remove(link)
        os.symlink(real, link)
    return link


def main():
    import wave
    from piper import PiperVoice, SynthesisConfig
    ap = argparse.ArgumentParser()
    ap.add_argument("--model", required=True)
    ap.add_argument("--speaker", type=int, default=None)
    ap.add_argument("--length-scale", type=float, default=1.0)
    ap.add_argument("--only", default=None)
    ap.add_argument("--out", default=OUT)
    a = ap.parse_args()
    os.makedirs(a.out, exist_ok=True)
    voice = PiperVoice.load(a.model, espeak_data_dir=short_espeak_dir())
    cfg = SynthesisConfig(speaker_id=a.speaker, length_scale=a.length_scale)
    with open(LINES, encoding="utf-8") as f:
        rows = [l.rstrip("\n").split("\t", 1) for l in f if l.strip()]
    for line_id, text in rows:
        if a.only and line_id != a.only:
            continue
        with tempfile.TemporaryDirectory() as tmp:
            wav = os.path.join(tmp, "dry.wav")
            with wave.open(wav, "wb") as w:
                voice.synthesize_wav(text, w, syn_config=cfg)
            mp3 = os.path.join(a.out, line_id + ".mp3")
            subprocess.run(["ffmpeg", "-y", "-loglevel", "error", "-i", wav, "-filter_complex", RADIO,
                            "-map", "[out]", "-ac", "1", "-c:a", "libmp3lame", "-q:a", "5", mp3], check=True)
            print(mp3)


if __name__ == "__main__":
    main()
