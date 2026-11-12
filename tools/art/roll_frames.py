"""Builds the ship's roll animation from the level sprite, as real 3D would (the image models
cannot): the fuselage is a cylinder whose painted surface turns, the wings are one plane through
the axis that foreshortens (the far wing darker, the near one brighter), the tail fin leans out.

Input: assets/sprites/sprite_buran.png (nose right). Output: assets/sprites/anim_buran_roll.png,
12 square frames from level to MAX_DEG, read by the game (mirrored for the other side).

Usage: python3 tools/art/roll_frames.py
"""
import math
import os

import numpy as np
from PIL import Image, ImageDraw

ROOT = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
SRC = os.path.join(ROOT, "assets", "sprites", "sprite_buran.png")
OUT = os.path.join(ROOT, "assets", "sprites", "anim_buran_roll.png")
FRAMES = 12
MAX_DEG = 30.0
SS = 3          # supersampling: computed at 3x, scaled down (smooth edges)
R_MAX = 26 * SS  # fuselage half-width in supersampled pixels


def bilinear_rows(img, src_rows, x):
    """Samples column x of img at fractional rows."""
    h = img.shape[0]
    r0 = np.clip(np.floor(src_rows).astype(int), 0, h - 1)
    r1 = np.clip(r0 + 1, 0, h - 1)
    t = (src_rows - np.floor(src_rows))[:, None]
    return img[r0, x] * (1 - t) + img[r1, x] * t


def frame(src, theta, c):
    h, w, _ = src.shape
    alpha = src[..., 3]
    out = np.zeros_like(src, dtype=float)
    ys = np.arange(h)
    # Fuselage half-width per column: the solid run around the axis, capped.
    half = np.zeros(w)
    for x in range(w):
        col = alpha[:, x] > 128
        if not col[c]:
            continue
        up = c
        while up > 0 and col[up - 1] and c - up < R_MAX:
            up -= 1
        dn = c
        while dn < h - 1 and col[dn + 1] and dn - c < R_MAX:
            dn += 1
        half[x] = min(c - up, dn - c)
    cos_t, sin_t = math.cos(theta), math.sin(theta)
    # Wings: one plane through the axis, foreshortened by cos(theta). Each output pixel takes the
    # source pixel it came from (no smearing); the far wing (top) darker, the near one brighter.
    k = sin_t / math.sin(math.radians(MAX_DEG))
    for y in range(h):
        d = (y - c) / cos_t
        sy = c + d
        if sy < 0 or sy > h - 1:
            continue
        y0 = int(math.floor(sy))
        y1 = min(h - 1, y0 + 1)
        f = sy - y0
        row = src[y0].astype(float) * (1 - f) + src[y1].astype(float) * f
        shade = 1.0 - 0.22 * k if d < 0 else 1.0 + 0.08 * k
        for x in range(w):
            if half[x] > 0 and abs(d) <= half[x]:
                continue
            if row[x, 3] < 8:
                continue
            px = row[x].copy()
            px[:3] *= shade
            out[y, x] = px
    # Fuselage: a cylinder, its surface turned by theta (cockpit and panels slide to the far side).
    for x in range(w):
        r = half[x]
        if r <= 1:
            continue
        v = np.arange(-int(r), int(r) + 1, dtype=float)
        psi = np.arcsin(np.clip(v / r, -1, 1))
        phi = psi + theta
        src_rows = c + r * np.sin(np.clip(phi, -math.pi / 2, math.pi / 2))
        col = bilinear_rows(src.astype(float), src_rows, x)
        shade = 0.78 + 0.22 * np.cos(psi + 0.35)
        col[:, :3] *= shade[:, None]
        # Beyond the edge of the painted top we see the cylinder's side: plain grey-white.
        side = np.abs(phi) > math.pi / 2 * 0.98
        col[side, :3] = np.array([196, 200, 206]) * shade[side, None]
        col[:, 3] = 255
        rows = (c + v).astype(int)
        ok = (rows >= 0) & (rows < h)
        out[rows[ok], x] = col[ok]
    img = Image.fromarray(out.clip(0, 255).astype(np.uint8), "RGBA")
    # Tail fin: seen edge-on when level (the spike at the back); as the ship rolls its top edge
    # swings out towards the far wing, so the fin shows as a swept trapezoid.
    if sin_t > 0.01:
        d = ImageDraw.Draw(img)
        lean = 40 * SS * sin_t
        poly = [(int(w * 0.30), c), (int(w * 0.12), c), (int(w * 0.02), c - lean), (int(w * 0.16), c - lean)]
        d.polygon(poly, fill=(206, 210, 218, 255))
        d.line(poly + [poly[0]], fill=(28, 30, 36, 255), width=2 * SS)
        d.line([(int(w * 0.02), c - lean), (int(w * 0.16), c - lean)], fill=(60, 64, 72, 255), width=4 * SS)
    return img


def main():
    base = Image.open(SRC).convert("RGBA")
    bw, bh = base.size
    src = np.asarray(base.resize((bw * SS, bh * SS), Image.LANCZOS))
    h, w, _ = src.shape
    a = src[..., 3] > 128
    c = int(round(np.nonzero(a[:, int(w * 0.85)])[0].mean()))
    side = max(bw, bh) + 8
    strip = Image.new("RGBA", (side * FRAMES, side))
    for i in range(FRAMES):
        theta = math.radians(MAX_DEG * i / (FRAMES - 1))
        f = frame(src, theta, c).resize((bw, bh), Image.LANCZOS)
        strip.paste(f, (i * side + (side - bw) // 2, (side - bh) // 2))
    strip.save(OUT, optimize=True)
    print(OUT, strip.size, "axis row", c)


if __name__ == "__main__":
    main()
