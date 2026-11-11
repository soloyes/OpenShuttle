package com.buran.game.ui.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;

import java.util.HashMap;
import java.util.Map;

/** Painted UI art (assets/ui): loaded once on first use, drawn cropped to fill a frame. */
public final class Art {
    private static final Map<String, Texture> CACHE = new HashMap<>();

    private Art() {
    }

    /** The texture, or null when the file is not there (art not made yet). */
    public static Texture get(String path) {
        if (!CACHE.containsKey(path)) {
            FileHandle f = Gdx.files.internal(path);
            Texture t = null;
            if (f.exists()) {
                t = new Texture(f);
                t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            }
            CACHE.put(path, t);
        }
        return CACHE.get(path);
    }

    /** The sector card (ui/sectorNN.jpg), or null. */
    public static Texture sector(int number) {
        return get(String.format("ui/sector%02d.jpg", number));
    }

    /**
     * Draws t cropped to fill (x, y, w, h) like CSS cover, centred on focusX of the width, with
     * a zoom (1 = no push-in) and a tint.
     */
    public static void cover(Batch batch, Texture t, float x, float y, float w, float h, float zoom,
                             float focusX, float r, float g, float b, float a) {
        float sw = t.getWidth();
        float sh = sw * h / w;
        if (sh > t.getHeight()) {
            sh = t.getHeight();
            sw = sh * w / h;
        }
        sw /= zoom;
        sh /= zoom;
        float sx = MathUtils.clamp(t.getWidth() * focusX - sw / 2f, 0f, t.getWidth() - sw);
        float sy = (t.getHeight() - sh) / 2f;
        batch.setColor(r, g, b, a);
        batch.draw(t, x, y, w, h, (int) sx, (int) sy, (int) sw, (int) sh, false, false);
        batch.setColor(1f, 1f, 1f, 1f);
    }

    /** cover() in a thin dark frame. */
    public static void framed(Batch batch, Texture t, float x, float y, float w, float h, float zoom, float a) {
        Theme.rect(batch, x - 3f, y - 3f, w + 6f, h + 6f, Theme.EDGE, a);
        cover(batch, t, x, y, w, h, zoom, 0.5f, 1f, 1f, 1f, a);
    }
}
