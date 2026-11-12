"""Renders the ship's roll from the real 3D model (art/sprites/raw/*.stl) and paints it with the
level sprite: the model gives the exact silhouette and shading in every frame, the sprite gives
the drawing (stars, panels, windows, ink lines). Points hidden in the level view (the hull's side
coming into sight) get a plain hull grey.

Output: assets/sprites/anim_buran_roll.png, FRAMES square frames, level to MAX_DEG.
Usage: python3 tools/art/roll_from_stl.py
"""
import glob
import math
import os

import numpy as np
import trimesh
from PIL import Image, ImageFilter
from scipy import ndimage as nd

ROOT = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
SPRITE = os.path.join(ROOT, "assets", "sprites", "sprite_buran.png")
OUT = os.environ.get("ROLL_OUT") or os.path.join(ROOT, "assets", "sprites", "anim_buran_roll.png")
FRAMES = 12
MAX_DEG = 30.0
SS = 3


def load_points():
    path = glob.glob(os.path.join(ROOT, "art", "sprites", "raw", "*.stl"))[0]
    m = trimesh.load(path)
    p = m.triangles_center.astype(np.float64)
    n = m.face_normals.astype(np.float64)
    # The model may lie at any angle: its own axes from PCA (long = along the ship, middle = span,
    # smallest = up), weighted by face area.
    w = m.area_faces
    c = np.average(p, axis=0, weights=w)
    _, vec = np.linalg.eigh(np.cov((p - c).T, aweights=w))
    ex, ey, ez = vec[:, 2], vec[:, 1], vec[:, 0]
    if ez[2] < 0:
        ez = -ez
    P = (p - c) @ np.stack([ex, ey, ez], 1)
    N = n @ np.stack([ex, ey, ez], 1)
    # Nose to +X: the tall fin is at the tail.
    if P[np.argmax(P[:, 2]), 0] > 0:
        P[:, 0] *= -1
        N[:, 0] *= -1
        P[:, 1] *= -1
        N[:, 1] *= -1
    # PCA leaves the ship slightly skewed: turn it about the vertical until the top view is
    # mirror-symmetric (left wing onto right wing), then put the axis on the plane of symmetry.
    def asym(P2):
        y0 = (P2[:, 1].min() + P2[:, 1].max()) / 2
        g = 160
        xs = np.clip(((P2[:, 0] - P2[:, 0].min()) / np.ptp(P2[:, 0]) * (g - 1)).astype(int), 0, g - 1)
        yr = max(P2[:, 1].max() - y0, y0 - P2[:, 1].min())
        ys = np.clip(((P2[:, 1] - y0) / yr * (g // 2 - 1) + g // 2).astype(int), 0, g - 1)
        occ = np.zeros((g, g), bool)
        occ[ys, xs] = True
        return (occ ^ occ[::-1]).sum()

    best = (None, 0.0)
    for deg in np.arange(-12, 12.01, 0.25):
        t = math.radians(deg)
        c2, s2 = math.cos(t), math.sin(t)
        Q = P.copy()
        Q[:, 0], Q[:, 1] = c2 * P[:, 0] - s2 * P[:, 1], s2 * P[:, 0] + c2 * P[:, 1]
        v = asym(Q)
        if best[0] is None or v < best[0]:
            best = (v, deg)
    t = math.radians(best[1])
    c2, s2 = math.cos(t), math.sin(t)
    P[:, 0], P[:, 1] = c2 * P[:, 0] - s2 * P[:, 1], s2 * P[:, 0] + c2 * P[:, 1]
    N[:, 0], N[:, 1] = c2 * N[:, 0] - s2 * N[:, 1], s2 * N[:, 0] + c2 * N[:, 1]
    print("symmetry turn", best[1], "deg")
    P[:, 1] -= (P[:, 1].min() + P[:, 1].max()) / 2
    P[:, 2] -= np.percentile(P[:, 2], 40)
    return P, N


def roll(P, N, deg):
    t = math.radians(deg)
    c, s = math.cos(t), math.sin(t)
    R = np.array([[1, 0, 0], [0, c, -s], [0, s, c]])
    return P @ R.T, N @ R.T


def zbuffer(P, scale, off, W, H):
    """Top-down orthographic: image x = X, image row = -Y (span up), keeps the highest point."""
    ix = ((P[:, 0] - off[0]) * scale).astype(int)
    iy = ((-P[:, 1] - off[1]) * scale).astype(int)
    ok = (ix >= 0) & (ix < W) & (iy >= 0) & (iy < H)
    idx = np.nonzero(ok)[0]
    key = iy[idx] * W + ix[idx]
    order = np.lexsort((P[idx, 2], key))          # by pixel, then height
    key_s = key[order]
    last = np.r_[key_s[1:] != key_s[:-1], True]   # highest point per pixel
    winner = idx[order][last]
    buf = -np.ones(W * H, dtype=np.int64)
    buf[key_s[last]] = winner
    return buf.reshape(H, W), ix, iy


def main():
    P0, N0 = load_points()
    sprite = Image.open(SPRITE).convert("RGBA")
    sw, sh = sprite.size
    W, H = sw * SS, sh * SS
    # Fit the level model's silhouette onto the sprite's bounding box.
    a = np.asarray(sprite)[..., 3] > 128
    ys, xs = np.nonzero(a)
    bx0, bx1, by0, by1 = xs.min() * SS, xs.max() * SS, ys.min() * SS, ys.max() * SS
    mx0, mx1 = P0[:, 0].min(), P0[:, 0].max()
    my0, my1 = (-P0[:, 1]).min(), (-P0[:, 1]).max()
    scale = (bx1 - bx0) / (mx1 - mx0)
    yscale = (by1 - by0) / (my1 - my0)
    off = (mx0 - bx0 / scale, my0 - (by0 + (by1 - by0 - (my1 - my0) * scale) / 2) / scale)
    big = np.asarray(sprite.resize((W, H), Image.LANCZOS)).astype(float)
    # Level view: which points the camera sees, and the sprite colour each of them carries.
    buf0, ix0, iy0 = zbuffer(P0, scale, off, W, H)
    visible0 = np.zeros(len(P0), bool)
    visible0[buf0[buf0 >= 0]] = True
    # A point counts as painted if it is within a hair of the top surface at level.
    topz = np.full(W * H, -1e9)
    flat = buf0.ravel()
    topz[flat >= 0] = P0[flat[flat >= 0], 2]
    ok = (ix0 >= 0) & (ix0 < W) & (iy0 >= 0) & (iy0 < H)
    near_top = np.zeros(len(P0), bool)
    kk = iy0[ok] * W + ix0[ok]
    near_top[np.nonzero(ok)[0]] = P0[ok, 2] >= topz[kk] - 0.6
    # Lay the drawing onto the model column by column: at every x the sprite's top and bottom
    # edges are stretched onto the model's, so wings and hull line up exactly.
    mask0 = nd.binary_fill_holes(nd.binary_closing(buf0 >= 0, iterations=2))
    alpha_big = big[..., 3] > 128
    M0 = np.full(W, -1)
    M1 = np.full(W, -1)
    S0 = np.full(W, -1)
    S1 = np.full(W, -1)
    for x in range(W):
        r = np.nonzero(mask0[:, x])[0]
        q = np.nonzero(alpha_big[:, x])[0]
        if len(r) and len(q):
            M0[x], M1[x], S0[x], S1[x] = r.min(), r.max(), q.min(), q.max()
    # Two pieces per column: the hull band keeps one width (no waves in the panel lines), the
    # wings stretch from the hull edge to the wing edge.
    cm = (M0 + M1) / 2.0
    cs = (S0 + S1) / 2.0
    Hm = (M1 - M0) / 2.0
    Hs = (S1 - S0) / 2.0
    xr = int(W * 0.78)
    Rm = Hm[xr] if Hm[xr] > 0 else 26 * SS
    Rs = Hs[xr] if Hs[xr] > 0 else 26 * SS
    colour = np.zeros((len(P0), 4))
    idx = np.nonzero(ok)[0]
    xs_ = ix0[idx]
    good = M0[xs_] >= 0
    idx, xs_ = idx[good], xs_[good]
    d = iy0[idx] - cm[xs_]
    hm, hs = Hm[xs_], Hs[xs_]
    narrow = hm <= Rm * 1.05
    ad = np.abs(d)
    inner = ad <= Rm
    mapped = np.where(narrow, d * hs / np.maximum(1, hm),
                      np.where(inner, d * Rs / Rm,
                               np.sign(d) * (Rs + (ad - Rm) * (hs - Rs) / np.maximum(1, hm - Rm))))
    sy = np.clip(np.round(cs[xs_] + mapped).astype(int), 0, H - 1)
    colour[idx] = big[sy, xs_]
    painted = near_top & (colour[:, 3] > 128)
    hull = np.array([200, 204, 210, 255.0])
    colour[~painted] = hull
    light = np.array([-0.35, 0.45, 0.82])
    light /= np.linalg.norm(light)
    side = max(sw, sh) + 8
    strip = Image.new("RGBA", (side * FRAMES, side))
    for f in range(FRAMES):
        deg = MAX_DEG * f / (FRAMES - 1)
        P, N = roll(P0, N0, deg)
        buf, _, _ = zbuffer(P, scale, off, W, H)
        img = np.zeros((H, W, 4))
        hit = buf >= 0
        pts = buf[hit]
        lam = np.clip(N[pts] @ light, 0, 1)
        lam0 = np.clip(N0[pts] @ light, 0, 1)
        # Keep the sprite's own lighting; add only the change the roll brings.
        shade = np.clip(1.0 + 0.9 * (lam - lam0), 0.55, 1.25)
        col = colour[pts].copy()
        col[:, :3] *= shade[:, None]
        img[hit] = col
        img[hit, 3] = 255
        # Close pinholes between splatted points, then ink the silhouette like the drawing.
        rgba = Image.fromarray(img.clip(0, 255).astype(np.uint8), "RGBA")
        m = nd.binary_closing(hit, iterations=2)
        m = nd.binary_fill_holes(m)
        filled = np.asarray(rgba.filter(ImageFilter.MaxFilter(3))).astype(float)
        base = np.asarray(rgba).astype(float)
        base[m & ~hit] = filled[m & ~hit]
        edge = m & ~nd.binary_erosion(m, iterations=SS * 2)
        base[edge, :3] = [26, 28, 34]
        base[..., 3] = np.where(m, 255, 0)
        frame = Image.fromarray(base.clip(0, 255).astype(np.uint8), "RGBA").resize((sw, sh), Image.LANCZOS)
        strip.paste(frame, (f * side + (side - sw) // 2, (side - sh) // 2))
        print("frame", f, round(deg, 1))
    strip.save(OUT, optimize=True)
    print(OUT, strip.size)


if __name__ == "__main__":
    main()
