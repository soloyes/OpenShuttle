"""Synthesises the intro score: a slow, dark D-minor piece that follows the comic.

  0-28 s   1988: low drone, a lone pad, distant (Baikonur, the launch, the hangar)
 28-50 s   1991: strings widen, a sad high line (the station, the cosmonaut, the secret launch)
 50-75 s   Object-19: the pulse quickens, choir, a heavy hit on the explosion, the rift
 75-95 s   silence: everything falls away to a drone and one fragile high note

Usage: python3 tools/music/intro_score.py  -> assets/music/music/intro.mp3
Needs numpy and scipy; ffmpeg for the mp3.
"""
import os
import subprocess
import tempfile
import wave

import numpy as np
from scipy.signal import butter, fftconvolve, sosfilt

SR = 44100
LEN = 96.0
ROOT = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
OUT = os.path.join(ROOT, "assets", "music", "music", "intro.mp3")
rng = np.random.default_rng(19)


def hz(midi):
    return 440.0 * 2 ** ((midi - 69) / 12.0)


def t_axis(dur):
    return np.arange(int(dur * SR)) / SR


def env(n, attack, release):
    e = np.ones(n)
    a = min(n, int(attack * SR))
    r = min(n, int(release * SR))
    e[:a] = np.linspace(0, 1, a) ** 2
    e[n - r:] *= np.linspace(1, 0, r) ** 2
    return e


def saw(f, t, phase=0.0):
    x = (f * t + phase) % 1.0
    return 2 * x - 1


def lowpass(x, cutoff):
    sos = butter(2, min(cutoff, SR / 2 - 100), btype="low", fs=SR, output="sos")
    return sosfilt(sos, x)


def bandpass(x, lo, hi):
    sos = butter(2, [lo, hi], btype="band", fs=SR, output="sos")
    return sosfilt(sos, x)


def pad(notes, dur, cutoff, attack=2.5, release=3.5, detune=0.006, width=0.6):
    """String-like pad: three detuned saws per note, filtered, slow swell. Stereo."""
    t = t_axis(dur)
    L = np.zeros_like(t)
    R = np.zeros_like(t)
    for m in notes:
        f = hz(m)
        vib = 1 + 0.0025 * np.sin(2 * np.pi * (4.6 + rng.random()) * t + rng.random() * 6)
        for k, d in enumerate((-detune, 0.0, detune)):
            s = saw(f * (1 + d) * vib, t, rng.random())
            pan = 0.5 + width * 0.5 * (k - 1)
            L += s * (1 - pan)
            R += s * pan
    L = lowpass(L, cutoff) * env(len(t), attack, release)
    R = lowpass(R, cutoff) * env(len(t), attack, release)
    n = max(1, len(notes))
    return np.stack([L, R]) / (3 * n)


def choir(notes, dur, attack=3.0, release=4.0):
    """'Ah' voices: saws through two formant bands."""
    t = t_axis(dur)
    x = np.zeros_like(t)
    for m in notes:
        f = hz(m)
        vib = 1 + 0.004 * np.sin(2 * np.pi * 5.2 * t + rng.random() * 6)
        x += saw(f * vib, t) + saw(f * 1.004 * vib, t, 0.3)
    x = bandpass(x, 600, 900) + 0.6 * bandpass(x, 1000, 1300)
    x *= env(len(t), attack, release) / (2 * len(notes))
    return np.stack([x * 0.8, x])


def drone(midi, dur, level=1.0):
    t = t_axis(dur)
    f = hz(midi)
    x = np.sin(2 * np.pi * f * t) + 0.35 * lowpass(saw(f, t), 300)
    x *= (0.85 + 0.15 * np.sin(2 * np.pi * 0.07 * t)) * env(len(t), 4, 5) * level
    return np.stack([x, x])


def hit(level=1.0, low=55.0):
    """A timpani / sub boom with a noise thud."""
    t = t_axis(5.0)
    f = low * (1 + 0.8 * np.exp(-t * 9))
    body = np.sin(2 * np.pi * np.cumsum(f) / SR) * np.exp(-t * 1.2)
    thud = lowpass(rng.standard_normal(len(t)), 400) * np.exp(-t * 6) * 0.6
    x = (body + thud) * level
    return np.stack([x, x])


def bell(midi, dur, level=0.5):
    """A thin, fragile high tone (the last note)."""
    t = t_axis(dur)
    f = hz(midi)
    x = (np.sin(2 * np.pi * f * t) + 0.3 * np.sin(2 * np.pi * f * 2.01 * t)) * np.exp(-t * 0.35)
    x *= env(len(t), 0.02, 2.0) * level
    return np.stack([x * 0.7, x])


def pulse(midi, dur, bpm, level=0.5):
    """Low ostinato: soft plucked eighths that make the middle feel urgent."""
    t = t_axis(dur)
    step = 60.0 / bpm / 2
    x = np.zeros_like(t)
    one = t_axis(step * 1.6)
    note = lowpass(saw(hz(midi), one), 500) * np.exp(-one * 5)
    i = 0
    while i * step < dur:
        s = int(i * step * SR)
        e = min(len(x), s + len(note))
        x[s:e] += note[: e - s] * (1.0 if i % 4 == 0 else 0.6)
        i += 1
    x *= env(len(t), 1.0, 2.0) * level
    return np.stack([x, x])


def place(mix, part, at):
    s = int(at * SR)
    e = min(mix.shape[1], s + part.shape[1])
    mix[:, s:e] += part[:, : e - s]


def reverb(x, seconds=3.8, wet=0.35):
    n = int(seconds * SR)
    t = np.arange(n) / SR
    out = []
    for ch in range(2):
        ir = rng.standard_normal(n) * np.exp(-t * 6.9 / seconds)
        ir = lowpass(ir, 5000)
        ir /= np.sqrt(np.sum(ir ** 2))
        out.append(fftconvolve(x[ch], ir)[: x.shape[1]])
    return x * (1 - wet) + np.stack(out) * wet * 2.2


def main():
    mix = np.zeros((2, int(LEN * SR)))
    D2, F2, G2, A2, Bb2, C3, D3 = 38, 41, 43, 45, 46, 48, 50
    # Harmony: Dm - Bb - Gm - A, eight seconds each, voiced low then higher.
    chords = [
        (0, [D3, 53, 57], 500), (8, [Bb2, 50, 53], 600), (16, [G2, 50, 55], 700), (24, [A2, 49, 52], 800),
        (30, [D3, 53, 57, 62], 1100), (38, [Bb2, 53, 58, 62], 1300), (46, [G2, 55, 58, 62], 1500),
        (52, [D3, 57, 62, 65], 2200), (58, [Bb2, 58, 62, 65], 2600), (64, [G2, 58, 62, 67], 2800),
        (70, [A2, 61, 64, 69], 2400),
    ]
    for at, notes, cut in chords:
        place(mix, pad(notes, 10.5, cut) * (0.55 if at < 30 else 0.75 if at < 52 else 0.9), at)
    # Drone under everything, swelling into the climax and holding through the silence.
    place(mix, drone(D2 - 12, 52) * 0.5, 0)
    place(mix, drone(D2 - 12, 46) * 0.7, 50)
    # A sad high line in 1991.
    line = [(31, 69, 4), (35, 67, 3), (38, 65, 5), (43, 64, 3), (46, 62, 5)]
    for at, m, d in line:
        place(mix, pad([m], d + 2, 3000, attack=0.8, release=2.0, detune=0.003) * 0.35, at)
    # Object-19: the ostinato and the choir.
    place(mix, pulse(D2, 24, 84, 0.35), 50)
    place(mix, choir([62, 65, 69], 12) * 0.6, 52)
    place(mix, choir([62, 67, 70], 12) * 0.7, 62)
    # Hits: the launch, the closing, the explosion (big), the rift.
    place(mix, hit(0.5), 5.5)
    place(mix, hit(0.45, 50), 21.5)
    place(mix, hit(1.2, 45), 57.5)
    place(mix, hit(0.8, 40), 66.0)
    # Silence: drone and one note left alone, then a last soft chord (hope).
    place(mix, bell(74, 9, 0.35), 78)
    place(mix, pad([D3, 57, 62], 12, 900, attack=4, release=6) * 0.45, 83)

    mix = reverb(mix)
    fade = int(3 * SR)
    mix[:, :fade] *= np.linspace(0, 1, fade)
    mix[:, -fade:] *= np.linspace(1, 0, fade)
    mix /= np.max(np.abs(mix)) / 0.85

    with tempfile.TemporaryDirectory() as tmp:
        wav = os.path.join(tmp, "intro.wav")
        with wave.open(wav, "wb") as w:
            w.setnchannels(2)
            w.setsampwidth(2)
            w.setframerate(SR)
            w.writeframes((mix.T * 32767).astype(np.int16).tobytes())
        subprocess.run(["ffmpeg", "-y", "-loglevel", "error", "-i", wav, "-af", "loudnorm=I=-18:TP=-1.5",
                        "-c:a", "libmp3lame", "-q:a", "4", OUT], check=True)
    print(OUT)


if __name__ == "__main__":
    main()
