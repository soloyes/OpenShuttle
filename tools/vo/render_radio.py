#!/usr/bin/env python3
"""
Renders the radio voice lines (sector briefings) to assets/sound/radio/<id>.mp3.

Text -> Piper TTS (open voice, commercial-safe licence) -> radio chain in ffmpeg:
band-limited to a transmitter, compressed, soft-clipped, a faint static bed under the voice.

Usage:
  pip install piper-tts            (ffmpeg must be on PATH)
  python3 tools/vo/render_radio.py --model <dir>/en_US-joe-medium.onnx [--speaker N] [--only sector01]
  Intro narration: --model en_US-norman-medium.onnx --only intro (intro12 is the pilot: joe)

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
    "adelay=80|80,apad=pad_dur=0.12[v];"
    # A faint static bed under the voice only (no squelch bursts, no hiss before or after).
    "anoisesrc=color=pink:amplitude=0.05:sample_rate=44100,highpass=f=500,lowpass=f=4200,"
    "tremolo=f=7:d=0.5[n];"
    "[v][n]amix=inputs=2:duration=first:weights=1 0.55:normalize=0,"
    "afade=t=in:d=0.06,areverse,afade=t=in:d=0.12,areverse,alimiter=limit=0.9[out]"
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


def phrases(text):
    """
    Splits a line into phrases, each with the pause that follows it. Piper reads one sentence
    with flat intonation; separate phrases get their own natural rise and fall, and the gaps
    sound like a person thinking over a radio, not a machine reading a paragraph.
    """
    import re
    out = []
    for sentence in re.split(r"(?<=[.!?])\s+", text.strip()):
        parts = sentence.split("...")
        for i, part in enumerate(parts):
            part = part.strip()
            if not part:
                continue
            last = i == len(parts) - 1
            if not last:
                out.append((part + "...", 0.55))        # hesitation
            elif len(part.split()) <= 2:
                out.append((part, 0.42))                # names, short calls
            else:
                out.append((part, 0.32))
    return out


def silence(seconds, rate, width, channels):
    return b"\x00" * int(seconds * rate) * width * channels


def main():
    import random
    import wave
    from piper import PiperVoice, SynthesisConfig
    ap = argparse.ArgumentParser()
    ap.add_argument("--model", required=True)
    ap.add_argument("--speaker", type=int, default=None)
    ap.add_argument("--length-scale", type=float, default=1.08)
    ap.add_argument("--noise-scale", type=float, default=0.75)
    ap.add_argument("--noise-w", type=float, default=0.9)
    ap.add_argument("--only", default=None)
    ap.add_argument("--out", default=OUT)
    a = ap.parse_args()
    os.makedirs(a.out, exist_ok=True)
    voice = PiperVoice.load(a.model, espeak_data_dir=short_espeak_dir())
    with open(LINES, encoding="utf-8") as f:
        rows = [l.rstrip("\n").split("\t", 1) for l in f if l.strip()]
    for line_id, text in rows:
        if a.only and not line_id.startswith(a.only):
            continue
        rnd = random.Random(line_id)
        with tempfile.TemporaryDirectory() as tmp:
            frames = b""
            params = None
            for k, (phrase, gap) in enumerate(phrases(text)):
                # Slight per-phrase variation in pace, like a tired operator.
                cfg = SynthesisConfig(speaker_id=a.speaker,
                                      length_scale=a.length_scale * rnd.uniform(0.96, 1.06),
                                      noise_scale=a.noise_scale, noise_w_scale=a.noise_w)
                part = os.path.join(tmp, "p%d.wav" % k)
                with wave.open(part, "wb") as w:
                    voice.synthesize_wav(phrase, w, syn_config=cfg)
                with wave.open(part, "rb") as r:
                    params = r.getparams()
                    frames += r.readframes(r.getnframes())
                frames += silence(gap * rnd.uniform(0.85, 1.2), params.framerate, params.sampwidth, params.nchannels)
            wav = os.path.join(tmp, "dry.wav")
            with wave.open(wav, "wb") as w:
                w.setparams(params)
                w.writeframes(frames)
            mp3 = os.path.join(a.out, line_id + ".mp3")
            subprocess.run(["ffmpeg", "-y", "-loglevel", "error", "-i", wav, "-filter_complex", RADIO,
                            "-map", "[out]", "-ac", "1", "-c:a", "libmp3lame", "-q:a", "5", mp3], check=True)
            print(mp3)


if __name__ == "__main__":
    main()
