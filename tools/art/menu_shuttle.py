"""Cuts the Buran out of the menu key art so the game can let it float (bob and sway).

Writes two files next to menu.jpg:
  assets/ui/menu_shuttle.png  the shuttle with a soft alpha edge (crop of BOX)
  assets/ui/menu_patch.png    the same crop with the shuttle painted out (dark space filled in),
                              drawn under the moving shuttle so no second outline shows.
The outline is a hand-traced polygon (image pixels, 1920 wide). Run after prepare_ui.py.

Usage: python3 tools/art/menu_shuttle.py
"""
import os

import numpy as np
from PIL import Image, ImageDraw, ImageFilter
from scipy.ndimage import gaussian_filter

ROOT = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
BOX = (1440, 550, 1860, 830)   # left, top, right, bottom

# Traced on a 2x crop starting at (1430, 540): image = 1430 + x / 2, 540 + y / 2.
OUTLINE_2X = [
    (48, 150), (70, 112), (125, 92), (210, 98), (470, 212), (598, 58), (692, 50), (696, 78),
    (600, 236), (650, 244), (772, 250), (806, 268), (790, 290), (700, 334), (650, 350),
    (676, 382), (656, 436), (560, 456), (500, 452), (480, 444), (238, 530), (196, 526),
    (182, 470), (226, 350), (118, 232), (56, 198),
]


def main():
    src = Image.open(os.path.join(ROOT, "assets", "ui", "menu.jpg")).convert("RGB")
    crop = src.crop(BOX)
    w, h = crop.size
    pts = [(1430 + x / 2 - BOX[0], 540 + y / 2 - BOX[1]) for x, y in OUTLINE_2X]
    mask = Image.new("L", (w, h), 0)
    ImageDraw.Draw(mask).polygon(pts, fill=255)
    mask = mask.filter(ImageFilter.MaxFilter(5)).filter(ImageFilter.GaussianBlur(1.2))

    shuttle = crop.copy()
    shuttle.putalpha(mask)
    shuttle.save(os.path.join(ROOT, "assets", "ui", "menu_shuttle.png"), optimize=True)

    # Paint the shuttle out: normalised convolution from the surrounding pixels, plus grain.
    hole = np.asarray(mask.filter(ImageFilter.MaxFilter(21))).astype(float) / 255.0
    img = np.asarray(crop).astype(float)
    known = 1.0 - hole
    out = img.copy()
    for ch in range(3):
        num = gaussian_filter(img[..., ch] * known, 18)
        den = gaussian_filter(known, 18) + 1e-4
        fill = num / den
        out[..., ch] = img[..., ch] * known + fill * hole
    rng = np.random.default_rng(19)
    out += rng.normal(0, 2.0, out.shape) * hole[..., None]
    Image.fromarray(np.clip(out, 0, 255).astype(np.uint8)).save(
        os.path.join(ROOT, "assets", "ui", "menu_patch.png"), optimize=True)
    for n in ("menu_shuttle.png", "menu_patch.png"):
        p = os.path.join(ROOT, "assets", "ui", n)
        print(n, os.path.getsize(p) // 1024, "KB")


if __name__ == "__main__":
    main()
