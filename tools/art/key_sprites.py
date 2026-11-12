"""Prepares game sprites drawn on flat green (art/sprites/raw, prompts in docs/art/ui-prompts.md
section 10): keys the green out with a soft edge, removes green spill, trims to the object and
scales to the in-game size. Fragment sheets are split into one PNG per piece. Chapter
backgrounds are only resized.

Output: assets/sprites/<name>.png (fragments: <name>_1.png ...), assets/sprites/<bg>.jpg

Usage: python3 tools/art/key_sprites.py
"""
import glob
import os

import numpy as np
from PIL import Image
from scipy import ndimage as nd

ROOT = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
SRC = os.path.join(ROOT, "art", "sprites", "raw")
DST = os.path.join(ROOT, "assets", "sprites")

# Longest side in pixels, by name prefix (first match wins).
SIZES = [
    ("sprite_boss", 512), ("sprite_buran", 256), ("sprite_crew", 128), ("sprite_rock", 192),
    ("sprite_fragments", 64), ("sprite_corrosive", 256), ("sprite_electric", 256), ("sprite_radiation", 256),
    ("sprite_magnetic", 256), ("sprite_crate", 160), ("sprite_storage", 160), ("sprite_fuel", 140),
    ("sprite_rocket", 96), ("sprite_shelter", 256), ("sprite_beacon", 160), ("icon_", 128), ("mark_", 128),
    ("badge_", 128),
]


def size_for(name):
    for prefix, px in SIZES:
        if name.startswith(prefix):
            return px
    return 192


def key(img):
    a = np.asarray(img.convert("RGB")).astype(float)
    r, g, b = a[..., 0], a[..., 1], a[..., 2]
    spill = g - np.maximum(r, b)
    fg = spill < 60
    # Drop the white comic frame some images come with: anything touching the border.
    lab, n = nd.label(fg)
    h, w = fg.shape
    keep = np.zeros_like(fg)
    for i, sl in enumerate(nd.find_objects(lab), 1):
        if sl is None or sl[0].start == 0 or sl[1].start == 0 or sl[0].stop == h or sl[1].stop == w:
            continue
        keep |= lab == i
    soft = np.clip((90 - spill) / 60.0, 0, 1)
    edge = nd.binary_dilation(keep, iterations=2) & ~nd.binary_erosion(keep, iterations=2)
    alpha = nd.gaussian_filter(np.where(edge, soft, keep.astype(float)), 0.6)
    g2 = np.minimum(g, np.maximum(r, b) + 6)
    rgba = np.dstack([r, g2, b, alpha * 255]).clip(0, 255).astype(np.uint8)
    return rgba, keep


def save(rgba, mask, name, px):
    ys, xs = np.nonzero(mask)
    if len(xs) == 0:
        print("EMPTY", name)
        return
    crop = Image.fromarray(rgba[max(0, ys.min() - 3):ys.max() + 4, max(0, xs.min() - 3):xs.max() + 4], "RGBA")
    k = px / max(crop.size)
    crop = crop.resize((max(1, round(crop.width * k)), max(1, round(crop.height * k))), Image.LANCZOS)
    out = os.path.join(DST, name + ".png")
    crop.save(out, optimize=True)
    print(name, crop.size)


def main():
    os.makedirs(DST, exist_ok=True)
    for f in sorted(glob.glob(os.path.join(SRC, "*.*"))):
        name = os.path.splitext(os.path.basename(f))[0]
        img = Image.open(f)
        if name.startswith("bg_"):
            im = img.convert("RGB")
            im = im.resize((1920, round(im.height * 1920 / im.width)), Image.LANCZOS)
            im.save(os.path.join(DST, name + ".jpg"), "JPEG", quality=85, optimize=True)
            print(name, im.size)
            continue
        rgba, keep = key(img)
        px = size_for(name)
        if name == "sprite_buran_roll_sheet":
            # Four ships left to right: level, then rolled more and more. One scale for all four,
            # so the frames match in size.
            lab, n = nd.label(keep)
            sizes = nd.sum(keep, lab, range(1, n + 1))
            big = sorted([i + 1 for i, s_ in enumerate(sizes) if s_ > 2000],
                         key=lambda i: nd.center_of_mass(keep, lab, i)[1])[:4]
            names = ["sprite_buran", "sprite_buran_roll20", "sprite_buran_roll40", "sprite_buran_roll60"]
            ys, xs = np.nonzero(lab == big[0])
            k = 256 / (xs.max() - xs.min() + 7)
            for nm, i in zip(names, big):
                m = lab == i
                ys, xs = np.nonzero(m)
                crop = Image.fromarray(np.where(m[..., None], rgba, 0).astype(np.uint8)[
                    max(0, ys.min() - 3):ys.max() + 4, max(0, xs.min() - 3):xs.max() + 4], "RGBA")
                crop = crop.resize((max(1, round(crop.width * k)), max(1, round(crop.height * k))), Image.LANCZOS)
                crop.save(os.path.join(DST, nm + ".png"), optimize=True)
                print(nm, crop.size)
            continue
        if name.startswith("sprite_fragments"):
            lab, n = nd.label(keep)
            sizes = nd.sum(keep, lab, range(1, n + 1))
            pieces = [i + 1 for i, s in enumerate(sizes) if s > 200]
            for j, i in enumerate(pieces, 1):
                save(rgba, lab == i, f"{name}_{j}", px)
        else:
            save(rgba, keep, name, px)


if __name__ == "__main__":
    main()
