"""Turns animations drawn on flat green into game frame strips.

Input: art/sprites/raw/anim_<name>.gif (any frame count) or anim_<name>.png (a 4x2 grid sheet).
Output: assets/sprites/anim_<name>.png - all frames side by side, equal cells, one common crop
and scale for every frame (so nothing jumps), green keyed out with a soft edge.

Usage: python3 tools/art/anim_frames.py
"""
import glob
import os
import re

import numpy as np
from PIL import Image, ImageSequence
from scipy import ndimage as nd

ROOT = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
SRC = os.path.join(ROOT, "art", "sprites", "raw")
DST = os.path.join(ROOT, "assets", "sprites")


def side(name):
    if "boss" in name:
        return 384
    if "crew" in name:
        return 200
    if "buran" in name:
        return 256
    return 256


def grid(name):
    m = re.search(r"_(\d+)x(\d+)$", name)
    return (int(m.group(1)), int(m.group(2))) if m else (4, 2)


def frames_of(path):
    if path.endswith(".gif"):
        return [f.convert("RGB") for f in ImageSequence.Iterator(Image.open(path))]
    im = Image.open(path).convert("RGB")
    cols, rows = grid(os.path.splitext(os.path.basename(path))[0])
    w, h = im.width // cols, im.height // rows
    m = 6   # skip the white grid lines
    return [im.crop((c * w + m, r * h + m, (c + 1) * w - m, (r + 1) * h - m)) for r in range(rows) for c in range(cols)]


def key(img, by_distance=False):
    a = np.asarray(img).astype(float)
    r, g, b = a[..., 0], a[..., 1], a[..., 2]
    spill = g - np.maximum(r, b)
    if by_distance:
        # A green subject on green (radiation): key by distance from the flat background colour.
        corners = np.concatenate([a[:8, :8].reshape(-1, 3), a[:8, -8:].reshape(-1, 3),
                                  a[-8:, :8].reshape(-1, 3), a[-8:, -8:].reshape(-1, 3)])
        bg = np.median(corners, axis=0)
        d = np.sqrt(((a - bg) ** 2).sum(axis=2))
        alpha = np.clip((d - 35) / 45.0, 0, 1)
    else:
        alpha = np.clip((70 - spill) / 50.0, 0, 1)
    solid = alpha > 0.5
    lab, n = nd.label(solid)
    if n:
        sizes = nd.sum(solid, lab, range(1, n + 1))
        keep = np.isin(lab, [i + 1 for i, s in enumerate(sizes) if s >= 12])
        alpha = np.where(nd.binary_dilation(keep, iterations=3), alpha, 0)
    g2 = g if by_distance else np.minimum(g, np.maximum(r, b) + 6)
    return np.dstack([r, g2, b, alpha * 255]).clip(0, 255).astype(np.uint8)


def main():
    os.makedirs(DST, exist_ok=True)
    for path in sorted(glob.glob(os.path.join(SRC, "anim_*.*"))):
        raw = os.path.splitext(os.path.basename(path))[0]
        name = re.sub(r"_\d+x\d+$", "", raw)
        frames = [key(f, "radiation" in name) for f in frames_of(path)]
        if "buran" in name:
            frames = [f for f in frames]
        h = min(f.shape[0] for f in frames)
        w = min(f.shape[1] for f in frames)
        frames = [f[:h, :w] for f in frames]
        union = np.zeros((h, w), bool)
        for f in frames:
            union |= f[..., 3] > 20
        ys, xs = np.nonzero(union)
        y0, y1, x0, x1 = max(0, ys.min() - 4), min(h, ys.max() + 5), max(0, xs.min() - 4), min(w, xs.max() + 5)
        # Square cells (the game reads the frame count as width / height), content centred.
        k = side(name) / max(y1 - y0, x1 - x0)
        cw, ch = round((x1 - x0) * k), round((y1 - y0) * k)
        sq = max(cw, ch)
        strip = Image.new("RGBA", (sq * len(frames), sq))
        for i, f in enumerate(frames):
            cell = Image.fromarray(f[y0:y1, x0:x1], "RGBA").resize((cw, ch), Image.LANCZOS)
            strip.paste(cell, (i * sq + (sq - cw) // 2, (sq - ch) // 2))
        strip.save(os.path.join(DST, name + ".png"), optimize=True)
        print(name, len(frames), "frames", (cw, ch))


if __name__ == "__main__":
    main()
