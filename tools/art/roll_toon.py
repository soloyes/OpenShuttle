"""The ship's roll drawn from the 3D model in the comic style (no texture to slide): flat tone
bands, ink on folds and the silhouette, decals placed on the model itself - red stars on the
wings, cockpit windows, dark leading edges and nose cap, panel lines on the hull - and the black
heat-shield belly that comes into view as it rolls. Reuses the model loading of roll_from_stl.

Output: assets/sprites/anim_buran_roll.png (and sprite_buran.png = frame 0, the level ship).
Usage: python3 tools/art/roll_toon.py
"""
import math
import os
import sys

import numpy as np
from PIL import Image
from scipy import ndimage as nd

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from roll_from_stl import FRAMES, MAX_DEG, ROOT, load_points, roll, zbuffer  # noqa: E402

OUT = os.environ.get("ROLL_OUT") or os.path.join(ROOT, "assets", "sprites", "anim_buran_roll.png")
LEVEL = os.environ.get("LEVEL_OUT") or os.path.join(ROOT, "assets", "sprites", "sprite_buran.png")
SS = 3
OUT_W = 256

WHITE = np.array([232, 234, 238.0])
GREY = np.array([150, 156, 166.0])
DARK = np.array([52, 56, 64.0])
BLACK = np.array([30, 32, 38.0])
RED = np.array([206, 52, 40.0])
GLASS = np.array([70, 92, 110.0])
INK = np.array([22, 24, 30.0])


def star(u, v, r):
    """True inside a five-pointed star of outer radius r centred at 0, a point towards +u."""
    pts = []
    for i in range(10):
        a = math.pi / 2 - i * math.pi / 5
        rr = r if i % 2 == 0 else r * 0.4
        pts.append((rr * math.sin(a), rr * math.cos(a)))
    inside = np.zeros(len(u), bool)
    j = len(pts) - 1
    for i in range(len(pts)):
        xi, yi = pts[i]
        xj, yj = pts[j]
        cross = ((yi > v) != (yj > v)) & (u < (xj - xi) * (v - yi) / (yj - yi + 1e-12) + xi)
        inside ^= cross
        j = i
    return inside


def decorate(P, N):
    X, Y, Z = P[:, 0], P[:, 1], P[:, 2]
    L = np.ptp(X)
    x0 = X.min()
    t = (X - x0) / L                     # 0 tail .. 1 nose
    span = max(abs(Y.min()), Y.max())
    # Hull half-width: the narrow part of the body ahead of the wings.
    body = (t > 0.72) & (t < 0.8)
    R = np.percentile(np.abs(Y[body]), 98) if body.any() else span * 0.25
    col = np.tile(WHITE, (len(P), 1))
    up = N[:, 2]
    # Belly: black heat-shield tiles, seen as the ship rolls.
    col[up < -0.15] = BLACK
    wing = np.abs(Y) > R * 1.05
    # Dark leading edges of the wings and the nose cap.
    lead = wing & (N[:, 0] > 0.45)
    col[lead] = DARK
    col[t > 0.965] = DARK
    # Engines at the very back.
    col[(t < 0.03)] = GREY
    # Panel lines on the hull top.
    hull_top = (~wing) & (up > 0.3)
    seam = ((t * 18) % 1.0 < 0.06) & (t > 0.18) & (t < 0.8)
    col[hull_top & seam] = GREY
    col[hull_top & (np.abs(Y) < R * 0.06) & (t > 0.18) & (t < 0.8)] = GREY
    # Cockpit windows: a dark arc of panes near the nose.
    w = hull_top & (t > 0.83) & (t < 0.87) & (np.abs(Y) < R * 0.75)
    col[w & (((Y / (R * 0.75) + 1) * 3) % 1.0 > 0.18)] = GLASS
    # Red stars on the wings.
    wx = x0 + 0.33 * L
    for sgn in (1, -1):
        wy = sgn * (R + (span - R) * 0.45)
        inside = wing & (up > 0.2) & star(X - wx, (Y - wy) * sgn, span * 0.13)
        col[inside] = RED
    return col


def render(P, N, col, scale, off, W, H):
    buf, _, _ = zbuffer(P, scale, off, W, H)
    hit = buf >= 0
    pts = buf[hit]
    light = np.array([-0.3, 0.45, 0.84])
    light /= np.linalg.norm(light)
    lam = np.clip(N[pts] @ light, 0, 1)
    band = np.where(lam > 0.75, 1.0, np.where(lam > 0.45, 0.86, 0.70))   # comic tone bands
    img = np.zeros((H, W, 3))
    img[hit] = col[pts] * band[:, None]
    # Ink: silhouette, folds (normals turn sharply) and steps in depth.
    nimg = np.zeros((H, W, 3))
    nimg[hit] = N[pts]
    zimg = np.full((H, W), np.nan)
    zimg[hit] = P[pts, 2]
    mask = nd.binary_fill_holes(nd.binary_closing(hit, iterations=2))
    for ch in range(3):
        img[..., ch] = np.where(mask & ~hit, nd.grey_dilation(img[..., ch], size=3), img[..., ch])
    for ch in range(3):
        nimg[..., ch] = nd.gaussian_filter(nd.median_filter(nimg[..., ch], size=5), 1.2)
    dn = np.zeros((H, W))
    for ax in (0, 1):
        dn += np.abs(np.diff(nimg, axis=ax, append=0)).sum(axis=2)
    zf = nd.median_filter(np.nan_to_num(zimg, nan=-1e3), size=5)
    dz = np.abs(np.diff(zf, axis=0, append=-1e3)) + np.abs(np.diff(zf, axis=1, append=-1e3))
    ink = (dn > 0.55) | (dz > 3.0)
    ink = nd.binary_opening(ink, iterations=1)
    ink &= mask
    ink |= mask & ~nd.binary_erosion(mask, iterations=SS * 2)
    ink = nd.binary_dilation(ink, iterations=1) & mask
    for ch in range(3):
        img[..., ch] = nd.median_filter(img[..., ch], size=5)
    img[ink] = INK
    rgba = np.dstack([img, mask * 255.0]).clip(0, 255).astype(np.uint8)
    return Image.fromarray(rgba, "RGBA")


def main():
    P0, N0 = load_points()
    col = decorate(P0, N0)
    L = np.ptp(P0[:, 0])
    span = np.ptp(P0[:, 1])
    W = OUT_W * SS
    scale = (W * 0.96) / L
    H = int(span * scale * 1.08)
    off = (P0[:, 0].min() - W * 0.02 / scale, -span * 0.54)
    side = max(OUT_W, int(H / SS)) + 8
    strip = Image.new("RGBA", (side * FRAMES, side))
    for f in range(FRAMES):
        deg = MAX_DEG * f / (FRAMES - 1)
        P, N = roll(P0, N0, deg)
        im = render(P, N, col, scale, off, W, H).resize((OUT_W, int(H / SS)), Image.LANCZOS)
        strip.paste(im, (f * side + (side - im.width) // 2, (side - im.height) // 2))
        if f == 0:
            im.save(LEVEL)
    strip.save(OUT, optimize=True)
    print(OUT, strip.size)


if __name__ == "__main__":
    main()
