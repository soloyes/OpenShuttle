"""Prepares the intro comic panels: trims the white comic frame the generator draws around
each image, resizes for the web build and writes assets/intro/pNN.jpg.

Usage: python3 tools/intro/prepare.py   (reads art/intro/raw/pNN.*)
"""
import glob
import os

from PIL import Image

ROOT = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
SRC = os.path.join(ROOT, "art", "intro", "raw")
DST = os.path.join(ROOT, "assets", "intro")
WIDTH = 1280


def trim(im):
    """Cuts light borders (the drawn panel frame) and a few pixels more."""
    g = im.convert("L")
    w, h = g.size
    px = g.load()

    def light_row(y):
        return sum(px[x, y] for x in range(0, w, 4)) / len(range(0, w, 4)) > 170

    def light_col(x):
        return sum(px[x, y] for y in range(0, h, 4)) / len(range(0, h, 4)) > 170

    top, bottom, left, right = 0, h - 1, 0, w - 1
    while top < h // 4 and light_row(top):
        top += 1
    while bottom > 3 * h // 4 and light_row(bottom):
        bottom -= 1
    while left < w // 4 and light_col(left):
        left += 1
    while right > 3 * w // 4 and light_col(right):
        right -= 1
    m = 6  # the dark ink line inside the frame
    return im.crop((left + m, top + m, right - m, bottom - m))


def main():
    os.makedirs(DST, exist_ok=True)
    for f in sorted(glob.glob(os.path.join(SRC, "p*.*"))):
        name = os.path.splitext(os.path.basename(f))[0]
        im = trim(Image.open(f).convert("RGB"))
        h = round(im.height * WIDTH / im.width)
        im = im.resize((WIDTH, h), Image.LANCZOS)
        out = os.path.join(DST, name + ".jpg")
        im.save(out, "JPEG", quality=82, optimize=True, progressive=False)
        print(name, im.size, os.path.getsize(out) // 1024, "KB")


if __name__ == "__main__":
    main()
