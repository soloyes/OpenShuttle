"""Synthesises the sounds of the newer mechanics into assets/sound/game:

  ram_on.mp3      the ram shield coming up: a rising electric hum that settles
  ram_off.mp3     the field letting go: a falling sigh
  ram_denied.mp3  no charge: a dull double click with a short buzz
  emp.mp3         the EMP: a sharp crack, a low thump and a fizzing tail

Usage: python3 tools/sfx/make_sfx.py   (numpy; ffmpeg for mp3)
"""
import os
import subprocess
import tempfile
import wave

import numpy as np

SR = 44100
ROOT = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
OUT = os.path.join(ROOT, "assets", "sound", "game")
rng = np.random.default_rng(19)


def t_axis(d):
    return np.arange(int(d * SR)) / SR


def band(x, lo, hi):
    f = np.fft.rfft(x)
    fr = np.fft.rfftfreq(len(x), 1 / SR)
    f[(fr < lo) | (fr > hi)] = 0
    return np.fft.irfft(f, len(x))


def sweep(f0, f1, d, shape=2.0):
    t = t_axis(d)
    k = (t / d) ** (1 / shape)
    f = f0 + (f1 - f0) * k
    return np.sin(2 * np.pi * np.cumsum(f) / SR)


def env(n, a, r):
    e = np.ones(n)
    ai, ri = int(a * SR), int(r * SR)
    e[:ai] = np.linspace(0, 1, ai)
    e[n - ri:] *= np.linspace(1, 0, ri)
    return e


def ram_on():
    d = 0.55
    t = t_axis(d)
    base = sweep(90, 220, d, 1.5)
    buzz = np.sign(sweep(180, 440, d, 1.5)) * 0.25
    shimmer = band(rng.standard_normal(len(t)), 2500, 7000) * 0.08 * np.exp(-t * 4)
    x = (base * 0.7 + buzz) * (0.6 + 0.4 * np.sin(2 * np.pi * 30 * t)) + shimmer
    return x * env(len(t), 0.02, 0.25)


def ram_off():
    d = 0.45
    t = t_axis(d)
    x = sweep(240, 70, d, 0.7) * 0.7 + band(rng.standard_normal(len(t)), 800, 3000) * 0.1 * np.exp(-t * 6)
    return x * env(len(t), 0.01, 0.3)


def ram_denied():
    out = np.zeros(int(0.32 * SR))
    for at in (0.0, 0.11):
        n = int(0.07 * SR)
        t = t_axis(0.07)
        c = (np.sin(2 * np.pi * 140 * t) + 0.5 * np.sign(np.sin(2 * np.pi * 70 * t))) * np.exp(-t * 40)
        s = int(at * SR)
        out[s:s + n] += c
    return out * 0.8


def emp():
    d = 1.2
    t = t_axis(d)
    crack = band(rng.standard_normal(len(t)), 1500, 9000) * np.exp(-t * 30)
    thump = np.sin(2 * np.pi * np.cumsum(60 * (1 + 2 * np.exp(-t * 12))) / SR) * np.exp(-t * 5)
    fizz = band(rng.standard_normal(len(t)), 3000, 8000) * 0.25 * np.exp(-t * 3) * (0.5 + 0.5 * np.sin(2 * np.pi * 23 * t))
    whoosh = sweep(900, 200, d, 0.6) * 0.15 * np.exp(-t * 2.5)
    return crack * 0.9 + thump * 1.0 + fizz + whoosh


def save(name, x):
    x = x / (np.max(np.abs(x)) + 1e-9) * 0.9
    with tempfile.TemporaryDirectory() as tmp:
        w = os.path.join(tmp, "x.wav")
        with wave.open(w, "wb") as f:
            f.setnchannels(1)
            f.setsampwidth(2)
            f.setframerate(SR)
            f.writeframes((x * 32767).astype("<i2").tobytes())
        out = os.path.join(OUT, name + ".mp3")
        subprocess.run(["ffmpeg", "-y", "-loglevel", "error", "-i", w, "-af", "loudnorm=I=-20:TP=-2",
                        "-c:a", "libmp3lame", "-q:a", "4", out], check=True)
        print(out)


if __name__ == "__main__":
    save("ram_on", ram_on())
    save("ram_off", ram_off())
    save("ram_denied", ram_denied())
    save("emp", emp())
