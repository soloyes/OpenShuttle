"""Builds the living-menu layers from art/ui/raw (run after prepare_ui.py):

  assets/ui/menu_shuttle.png  the Buran keyed out of art/ui/raw/layers/menu_shuttle.jpg
                              (drawn on flat green), green spill removed, soft edge
  assets/ui/menu_core.png     the anomaly sphere cut out of assets/ui/menu.jpg with a soft
                              round edge, so the game can turn it

Prints the measured points (sphere, station windows, stars) for MenuArtLife.

Usage: python3 tools/art/menu_layers.py
"""
import os

import numpy as np
from PIL import Image
from scipy import ndimage as nd

ROOT = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
UI = os.path.join(ROOT, "assets", "ui")


def shuttle():
    im = np.asarray(Image.open(os.path.join(ROOT, "art", "ui", "raw", "layers", "menu_shuttle.jpg"))
                    .convert("RGB")).astype(float)
    r, g, b = im[..., 0], im[..., 1], im[..., 2]
    # Greenness: how far green rises above the other two channels.
    spill = g - np.maximum(r, b)
    fg = spill < 60
    lab, n = nd.label(fg)
    h, w = fg.shape
    # The largest piece that does not touch the image edge (the white frame does).
    best, size = 0, 0
    for i, sl in enumerate(nd.find_objects(lab), 1):
        if sl[0].start == 0 or sl[1].start == 0 or sl[0].stop == h or sl[1].stop == w:
            continue
        s = int((lab[sl] == i).sum())
        if s > size:
            best, size = i, s
    mask = nd.binary_fill_holes(lab == best)
    # Soft alpha from the green distance at the edge.
    soft = np.clip((90 - spill) / 60.0, 0, 1)
    edge = nd.binary_dilation(mask, iterations=2) & ~nd.binary_erosion(mask, iterations=2)
    alpha = np.where(edge, soft, mask.astype(float))
    alpha = nd.gaussian_filter(alpha, 0.6)
    # Despill: green no brighter than the brighter of red and blue.
    g2 = np.minimum(g, np.maximum(r, b) + 6)
    rgba = np.dstack([r, g2, b, alpha * 255]).clip(0, 255).astype(np.uint8)
    ys, xs = np.nonzero(mask)
    x0, x1, y0, y1 = xs.min() - 4, xs.max() + 5, ys.min() - 4, ys.max() + 5
    out = Image.fromarray(rgba[y0:y1, x0:x1], "RGBA")
    out.save(os.path.join(UI, "menu_shuttle.png"), optimize=True)
    print("shuttle", out.size)


def core_and_points():
    im = Image.open(os.path.join(UI, "menu.jpg")).convert("RGB")
    a = np.asarray(im).astype(float)
    lum = a.mean(2)
    s = nd.uniform_filter(lum, 61)
    cy, cx = np.unravel_index(s.argmax(), s.shape)
    # Sphere radius: where the brightness along the row falls to half.
    rad = next(r for r in range(20, 400, 2) if lum[cy, min(lum.shape[1] - 1, cx + r)] < 0.5 * lum[cy, cx])
    print("core", int(cx), int(cy), "radius", rad)
    r_out = int(rad * 1.55)
    r_in = int(rad * 0.95)
    crop = np.asarray(im.crop((cx - r_out, cy - r_out, cx + r_out, cy + r_out))).astype(np.uint8)
    sz = 2 * r_out
    yy, xx = np.mgrid[0:sz, 0:sz] + 0.5
    t = np.clip((np.hypot(xx - r_out, yy - r_out) - r_in) / (r_out - r_in), 0, 1)
    alpha = ((1 - t * t * (3 - 2 * t)) * 255).astype(np.uint8)
    Image.fromarray(np.dstack([crop, alpha]), "RGBA").save(os.path.join(UI, "menu_core.png"), optimize=True)
    print("core png", sz)
    # Station windows: small warm specks brighter than their surroundings, on the ring.
    R, B = a[..., 0], a[..., 2]
    loc = lum - nd.uniform_filter(lum, 15)
    H, W = lum.shape
    yy, xx = np.mgrid[0:H, 0:W]
    dist = np.hypot(xx - cx, yy - cy)
    win = (R - B > 15) & (loc > 10) & (dist > rad * 2.1) & (dist < rad * 4.2)
    lab, n = nd.label(win)
    pts = []
    for y, x in nd.center_of_mass(win, lab, range(1, n + 1)):
        if all((x - p[0]) ** 2 + (y - p[1]) ** 2 > 18 ** 2 for p in pts):
            pts.append((int(x), int(y)))
    print("windows", pts)
    st = (loc > 30) & (lum > 110) & (dist > rad * 4.5) & (nd.maximum_filter(lum, 9) == lum)
    ys, xs = np.nonzero(st)
    print("stars", list(zip(xs.tolist(), ys.tolist()))[:30])


if __name__ == "__main__":
    shuttle()
    core_and_points()
