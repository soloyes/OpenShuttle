package com.buran.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

/**
 * Painted looping animations (assets/sprites/anim_<name>.png: frames side by side, square-ish
 * equal cells, made by tools/art/anim_frames.py). Null when the strip is not there.
 */
public final class Anim {
    /** Frames per second of the painted loops (drawn at 150 ms a frame). */
    public static final float FPS = 7f;
    private static final Map<String, TextureRegion[]> CACHE = new HashMap<>();

    private Anim() {
    }

    public static TextureRegion[] frames(String name, int count) {
        if (CACHE.containsKey(name)) {
            return CACHE.get(name);
        }
        TextureRegion[] r = null;
        FileHandle f = Gdx.files.internal("sprites/anim_" + name + ".png");
        if (f.exists()) {
            Texture t = new Texture(f);
            t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            int w = t.getWidth() / count;
            r = new TextureRegion[count];
            for (int i = 0; i < count; i++) {
                r[i] = new TextureRegion(t, i * w, 0, w, t.getHeight());
            }
        }
        CACHE.put(name, r);
        return r;
    }
}
