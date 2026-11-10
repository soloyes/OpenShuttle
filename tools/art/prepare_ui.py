"""Prepares UI art from art/ui/raw for the game (assets/ui):
menu backdrop (wide, 1920 px) and crew portraits (square crops, 384 px).

Usage: python3 tools/art/prepare_ui.py
"""
import glob
import os
import sys

from PIL import Image

sys.path.insert(0, os.path.join(os.path.dirname(os.path.dirname(os.path.abspath(__file__))), "intro"))
from prepare import trim  # noqa: E402

ROOT = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
SRC = os.path.join(ROOT, "art", "ui", "raw")
DST = os.path.join(ROOT, "assets", "ui")


def save(im, name, q=84):
    out = os.path.join(DST, name + ".jpg")
    im.convert("RGB").save(out, "JPEG", quality=q, optimize=True)
    print(name, im.size, os.path.getsize(out) // 1024, "KB")


def main():
    os.makedirs(DST, exist_ok=True)
    for f in sorted(glob.glob(os.path.join(SRC, "*.*"))):
        name = os.path.splitext(os.path.basename(f))[0]
        im = trim(Image.open(f).convert("RGB"))
        if name == "menu" or name.startswith("sector") or name in ("clear", "lost"):
            w = 1920 if name == "menu" else 1280
            save(im.resize((w, round(im.height * w / im.width)), Image.LANCZOS), name)
        elif name.startswith("crew_"):
            s = im.height
            x = (im.width - s) // 2
            save(im.crop((x, 0, x + s, s)).resize((384, 384), Image.LANCZOS), name, 86)
        elif name.startswith("codex_"):
            s = min(im.width, im.height)
            x = (im.width - s) // 2
            y = (im.height - s) // 2
            save(im.crop((x, y, x + s, y + s)).resize((512, 512), Image.LANCZOS), name)


if __name__ == "__main__":
    main()
