"""Cuts the anomaly sphere out of the menu key art with a soft round edge, so the game can
spin it slowly on its own (assets/ui/menu_core.png). Run after prepare_ui.py.

Usage: python3 tools/art/menu_core.py
"""
import os

import numpy as np
from PIL import Image

ROOT = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
CX, CY = 1335, 442      # sphere centre in assets/ui/menu.jpg (1920 wide)
R_OUT = 160             # cut-out radius; fully opaque inside R_IN
R_IN = 100


def main():
    im = Image.open(os.path.join(ROOT, "assets", "ui", "menu.jpg")).convert("RGB")
    crop = np.asarray(im.crop((CX - R_OUT, CY - R_OUT, CX + R_OUT, CY + R_OUT))).astype(np.uint8)
    s = 2 * R_OUT
    yy, xx = np.mgrid[0:s, 0:s] + 0.5
    r = np.hypot(xx - R_OUT, yy - R_OUT)
    t = np.clip((r - R_IN) / (R_OUT - R_IN), 0, 1)
    alpha = (1 - t * t * (3 - 2 * t)) * 255
    out = np.dstack([crop, alpha.astype(np.uint8)])
    path = os.path.join(ROOT, "assets", "ui", "menu_core.png")
    Image.fromarray(out, "RGBA").save(path, optimize=True)
    print(path, os.path.getsize(path) // 1024, "KB")


if __name__ == "__main__":
    main()
