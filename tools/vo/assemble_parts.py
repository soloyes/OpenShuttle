"""Joins separately voiced phrases into one radio line with dramatic pauses.

Each phrase (a dry wav from render_chatterbox.py --dry --lines ...) goes through the same
radio chain as every line; between phrases goes either silence (narration) or the open channel:
a key click, crackling static, a click (the pilot calling into nothing).
Prints when each phrase starts, for Intro.PART_AT.

Usage: python3 tools/vo/assemble_parts.py <dir with <id>_dry.wav>
"""
import os
import subprocess
import sys
import tempfile
import wave

import numpy as np

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from render_radio import OUT, RADIO  # noqa: E402

SR = 44100
rng = np.random.default_rng(19)

# Line id -> phrases and what follows each one: ("static", seconds) or ("silence", seconds).
SPEC = {
    "intro12": [("intro12a", ("static", 1.5)), ("intro12b", ("static", 2.0)), ("intro12c", ("static", 1.2))],
    "intro13": [("intro13a", ("silence", 1.5)), ("intro13b", ("silence", 1.6)), ("intro13c", ("silence", 0.4))],
    "ch1_03": [("ch1_03a", ("silence", 1.4)), ("ch1_03b", ("static", 1.6)), ("ch1_03c", ("silence", 0.6))],
    "ch1_04": [("ch1_04a", ("static", 1.8)), ("ch1_04b", ("static", 2.4))],
    "ch2_03": [("ch2_03a", ("silence", 1.6)), ("ch2_03b", ("silence", 2.2))],
    "ch3_04": [("ch3_04a", ("static", 1.6)), ("ch3_04b", ("static", 1.4)), ("ch3_04c", ("silence", 2.6))],
}

if len(sys.argv) > 2:
    SPEC = {k: v for k, v in SPEC.items() if k.startswith(sys.argv[2])}


def read_wav(path):
    with wave.open(path) as w:
        x = np.frombuffer(w.readframes(w.getnframes()), dtype="<i2").astype(float) / 32768.0
        return x.reshape(-1, w.getnchannels()).mean(1)


def radio(dry):
    with tempfile.TemporaryDirectory() as tmp:
        out = os.path.join(tmp, "r.wav")
        subprocess.run(["ffmpeg", "-y", "-loglevel", "error", "-i", dry, "-filter_complex", RADIO,
                        "-map", "[out]", "-ac", "1", "-ar", str(SR), out], check=True)
        return read_wav(out)


def band(x, lo, hi):
    f = np.fft.rfft(x)
    fr = np.fft.rfftfreq(len(x), 1 / SR)
    f[(fr < lo) | (fr > hi)] = 0
    return np.fft.irfft(f, len(x))


def click(level=0.35):
    n = int(0.09 * SR)
    t = np.arange(n) / SR
    return band(rng.standard_normal(n), 900, 4500) * np.exp(-t * 45) * level


def static(seconds, level=0.07):
    n = int(seconds * SR)
    t = np.arange(n) / SR
    hiss = band(rng.standard_normal(n), 500, 4200)
    hiss /= np.max(np.abs(hiss)) + 1e-9
    # Crackle: the level wanders and spits now and then.
    wander = 0.6 + 0.4 * np.sin(2 * np.pi * 0.7 * t + rng.random() * 6) * np.sin(2 * np.pi * 3.1 * t)
    spits = (rng.random(n) < 0.0009).astype(float)
    spits = np.convolve(spits, np.exp(-np.arange(400) / 60.0), mode="same") * 2.5
    x = hiss * level * (wander + spits)
    fade = min(n // 4, int(0.15 * SR))
    x[:fade] *= np.linspace(0, 1, fade)
    x[-fade:] *= np.linspace(1, 0, fade)
    return x


def channel(seconds):
    body = static(seconds)
    c1, c2 = click(), click(0.28)
    body[: len(c1)] += c1
    body[-len(c2):] += c2
    return body


def main():
    src = sys.argv[1]
    for line, parts in SPEC.items():
        out = []
        starts = []
        pos = 0
        for pid, (kind, sec) in parts:
            v = radio(os.path.join(src, pid + "_dry.wav"))
            starts.append(pos / SR)
            out.append(v)
            pos += len(v)
            gap = channel(sec) if kind == "static" else np.zeros(int(sec * SR))
            out.append(gap)
            pos += len(gap)
        x = np.concatenate(out)
        x = x / max(1.0, np.max(np.abs(x)) / 0.95)
        with tempfile.TemporaryDirectory() as tmp:
            wav = os.path.join(tmp, "x.wav")
            with wave.open(wav, "wb") as w:
                w.setnchannels(1)
                w.setsampwidth(2)
                w.setframerate(SR)
                w.writeframes((x * 32767).astype("<i2").tobytes())
            mp3 = os.path.join(OUT, line + ".mp3")
            subprocess.run(["ffmpeg", "-y", "-loglevel", "error", "-i", wav, "-c:a", "libmp3lame", "-q:a", "5", mp3],
                           check=True)
        print(line, "starts", [round(s, 2) for s in starts], "length", round(len(x) / SR, 2))


if __name__ == "__main__":
    main()
