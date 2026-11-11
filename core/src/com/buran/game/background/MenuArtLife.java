package com.buran.game.background;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;

/**
 * Brings the menu key art (ui/menu.jpg) to life: Object-19 slowly turns and breathes, dust
 * spirals into it, a faint wave rolls out now and then, the station windows flicker, the stars
 * twinkle and the Buran blinks its navigation lights. Points are in image pixels (1920 wide,
 * y down), measured on the picture.
 */
class MenuArtLife implements Disposable {
    private static final float IW = 1920f;
    private static final float IH = 1037f;
    private static final float CX = 1335f;
    private static final float CY = 442f;
    private static final float CORE_R = 161f;   // half the size of ui/menu_core.png

    private static final float[][] WINDOWS = {
            {1467, 173}, {1486, 183}, {1535, 224}, {1112, 248}, {1566, 259}, {1617, 366},
            {1622, 390}, {1624, 495}, {1056, 521}, {1121, 632}, {1145, 656}, {1201, 694},
    };
    private static final float[][] STARS = {
            {1787, 69}, {203, 92}, {1805, 148}, {261, 251}, {283, 302}, {92, 474}, {573, 490},
            {233, 597}, {956, 629}, {862, 723}, {822, 745}, {242, 882}, {149, 914}, {1374, 926},
            {1106, 979}, {728, 991},
    };
    /**
     * The Buran sprite (ui/menu_shuttle.png, 603 x 402): where its centre sits on the art and how
     * wide it is there, in image pixels. Lights are in sprite pixels: near wingtip (red), far
     * wingtip (green), top of the fin (white strobe), the engine bells.
     */
    private static final float SHIP_CX = 1615f, SHIP_CY = 705f, SHIP_W = 405f;
    private static final float SPRITE_W = 603f, SPRITE_H = 402f;
    private static final float[] PORT = {118, 350};
    private static final float[] STARBOARD = {592, 180};
    private static final float[] FIN = {500, 16};
    private static final float[][] NOZZLES = {{395, 212}, {440, 222}, {475, 260}, {420, 290}};

    private static final int DUST = 46;

    private final TextureRegion core;
    private final Texture shuttle;
    private final com.badlogic.gdx.math.Vector2 tmp = new com.badlogic.gdx.math.Vector2();
    private float bobX, bobY, bobRot;
    private final Texture dot;
    private final Texture ring;
    private final float[] phase = new float[64];
    private final float[] speed = new float[64];
    private final float[] dustR = new float[DUST];
    private final float[] dustA = new float[DUST];
    private final float[] dustS = new float[DUST];
    private float time;

    MenuArtLife() {
        FileHandle f = Gdx.files.internal("ui/menu_core.png");
        if (f.exists()) {
            Texture t = new Texture(f);
            t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            core = new TextureRegion(t);
        } else {
            core = null;
        }
        shuttle = load("ui/menu_shuttle.png");
        dot = radial(64, false);
        ring = radial(256, true);
        for (int i = 0; i < phase.length; i++) {
            phase[i] = MathUtils.random(MathUtils.PI2);
            speed[i] = MathUtils.random(0.6f, 1.8f);
        }
        for (int i = 0; i < DUST; i++) {
            spawnDust(i, MathUtils.random(140f, 560f));
        }
    }

    private static Texture load(String path) {
        FileHandle f = Gdx.files.internal(path);
        if (!f.exists()) {
            return null;
        }
        Texture t = new Texture(f);
        t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        return t;
    }

    /** A soft round spot, or a soft thin ring (for the wave). */
    private static Texture radial(int s, boolean asRing) {
        Pixmap p = new Pixmap(s, s, Pixmap.Format.RGBA8888);
        p.setBlending(Pixmap.Blending.None);
        for (int y = 0; y < s; y++) {
            for (int x = 0; x < s; x++) {
                float dx = (x + 0.5f) / s * 2f - 1f;
                float dy = (y + 0.5f) / s * 2f - 1f;
                float d = (float) Math.sqrt(dx * dx + dy * dy);
                float a;
                if (asRing) {
                    float k = 1f - Math.abs(d - 0.9f) / 0.1f;
                    a = MathUtils.clamp(k, 0f, 1f);
                    a = a * a;
                } else {
                    float k = MathUtils.clamp(1f - d, 0f, 1f);
                    a = k * k * k;
                }
                p.setColor(1f, 1f, 1f, a);
                p.drawPixel(x, y);
            }
        }
        Texture t = new Texture(p);
        t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        p.dispose();
        return t;
    }

    private void spawnDust(int i, float r) {
        dustR[i] = r;
        dustA[i] = MathUtils.random(MathUtils.PI2);
        dustS[i] = MathUtils.random(3f, 7f);
    }

    void update(float dt) {
        time += dt;
        // The Buran floats: a slow rise and fall, a little drift and roll (image pixels, degrees).
        // Without a separate Buran layer it stays where it is painted.
        if (shuttle != null) {
            bobY = 4.5f * MathUtils.sin(time * MathUtils.PI2 / 6f);
            bobX = 1.5f * MathUtils.sin(time * MathUtils.PI2 / 9f + 0.7f);
            bobRot = 0.7f * MathUtils.sin(time * MathUtils.PI2 / 7.5f + 1.3f);
        }
        for (int i = 0; i < DUST; i++) {
            // Spiral in: faster closer to the core, gone at its edge.
            float r = dustR[i];
            dustA[i] += dt * 18f / Math.max(60f, r) * 3f;
            dustR[i] -= dt * (6f + 1800f / Math.max(80f, r));
            if (dustR[i] < 110f) {
                spawnDust(i, MathUtils.random(420f, 600f));
            }
        }
    }

    /** Draws over the art placed at (x, y, w, h). Leaves the batch in normal blending. */
    void draw(SpriteBatch batch, float x, float y, float w, float h) {
        float k = w / IW;
        // The sphere turns slowly and breathes.
        if (core != null) {
            float size = CORE_R * 2f * k;
            float s = 1f + 0.015f * MathUtils.sin(time * 0.7f);
            batch.setColor(1f, 1f, 1f, 1f);
            batch.draw(core, px(x, w, CX) - size / 2f, py(y, h, CY) - size / 2f, size / 2f, size / 2f,
                    size, size, s, s, time * 4f);
        }
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        // Core glow, pulsing.
        float glow = 0.16f + 0.07f * MathUtils.sin(time * 0.8f) + 0.03f * MathUtils.sin(time * 2.3f);
        spot(batch, dot, x, y, w, h, CX, CY, 560f * k, 0.85f, 0.92f, 1f, glow);
        // A wave rolls out of it every few seconds.
        float cycle = 7f;
        float p = (time % cycle) / cycle;
        if (p < 0.6f) {
            float q = p / 0.6f;
            float r = (230f + 420f * q) * k;
            spot(batch, ring, x, y, w, h, CX, CY, r * 2f, 0.8f, 0.9f, 1f, 0.12f * (1f - q) * Math.min(1f, q * 6f));
        }
        // Dust drawn into it.
        for (int i = 0; i < DUST; i++) {
            float r = dustR[i];
            float fade = MathUtils.clamp((r - 110f) / 80f, 0f, 1f) * MathUtils.clamp((600f - r) / 120f, 0f, 1f);
            float dx = CX + MathUtils.cos(dustA[i]) * r;
            float dy = CY + MathUtils.sin(dustA[i]) * r * 0.92f;
            spot(batch, dot, x, y, w, h, dx, dy, dustS[i] * 2.2f * k, 0.9f, 0.94f, 1f, 0.45f * fade);
        }
        // Station windows: mostly steady, each one dips and comes back on its own beat.
        for (int i = 0; i < WINDOWS.length; i++) {
            float f = 0.55f + 0.3f * MathUtils.sin(time * speed[i] + phase[i]);
            if (MathUtils.sin(time * 0.37f * speed[i] + phase[i] * 3f) > 0.93f) {
                f *= 0.2f;   // a brief blackout
            }
            spot(batch, dot, x, y, w, h, WINDOWS[i][0], WINDOWS[i][1], 30f * k, 1f, 0.68f, 0.32f, 0.55f * f);
        }
        // Stars twinkle.
        for (int i = 0; i < STARS.length; i++) {
            float t = MathUtils.sin(time * speed[20 + i] * 1.4f + phase[20 + i]);
            float a = 0.1f + 0.5f * Math.max(0f, t) * Math.max(0f, t);
            spot(batch, dot, x, y, w, h, STARS[i][0], STARS[i][1], 26f * k, 0.85f, 0.9f, 1f, a);
        }
        // The Buran, floating, in front of the dust and the wave.
        if (shuttle != null) {
            batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            batch.setColor(1f, 1f, 1f, 1f);
            float sw = SHIP_W * k;
            float sh = sw * SPRITE_H / SPRITE_W;
            float bx = px(x, w, SHIP_CX) - sw / 2f + bobX * k;
            float by = py(y, h, SHIP_CY) - sh / 2f + bobY * k;
            batch.setColor(0.86f, 0.88f, 0.92f, 1f);   // sit in the scene's muted light
            batch.draw(shuttle, bx, by, sw / 2f, sh / 2f, sw, sh, 1f, 1f, bobRot,
                    0, 0, shuttle.getWidth(), shuttle.getHeight(), false, false);
            batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        }
        // Buran: nav lights (a sharp point and a small halo), a fin strobe, the engines idling warm.
        float blink = time % 1.6f;
        if (blink < 0.18f) {
            light(batch, x, y, w, h, k, PORT, 1f, 0.25f, 0.2f);
        }
        float blink2 = (time + 0.8f) % 1.6f;
        if (blink2 < 0.18f) {
            light(batch, x, y, w, h, k, STARBOARD, 0.35f, 1f, 0.5f);
        }
        float strobe = time % 2.4f;
        if (strobe < 0.07f || (strobe > 0.19f && strobe < 0.26f)) {
            light(batch, x, y, w, h, k, FIN, 1f, 1f, 1f);
        }
        for (int i = 0; i < NOZZLES.length; i++) {
            float a = 0.2f + 0.06f * MathUtils.sin(time * 9f + phase[40 + i]) + 0.04f * MathUtils.sin(time * 23f + i);
            ship(NOZZLES[i], x, y, w, h, k);
            spot(batch, dot, tmp.x, tmp.y, 44f * k, 1f, 0.6f, 0.3f, a);
        }
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setColor(1f, 1f, 1f, 1f);
    }

    /** Where an image point of the Buran is on screen now (it bobs and rolls). Result in tmp. */
    private void ship(float[] p, float x, float y, float w, float h, float k) {
        float cx = px(x, w, SHIP_CX);
        float cy = py(y, h, SHIP_CY);
        float s = SHIP_W / SPRITE_W;
        float ix = SHIP_CX + (p[0] - SPRITE_W / 2f) * s;
        float iy = SHIP_CY + (p[1] - SPRITE_H / 2f) * s;
        tmp.set(px(x, w, ix) - cx, py(y, h, iy) - cy).rotateDeg(bobRot);
        tmp.add(cx + bobX * k, cy + bobY * k);
    }

    private void light(SpriteBatch batch, float x, float y, float w, float h, float k, float[] p,
                       float r, float g, float b) {
        ship(p, x, y, w, h, k);
        spot(batch, dot, tmp.x, tmp.y, 64f * k, r, g, b, 0.5f);
        spot(batch, dot, tmp.x, tmp.y, 18f * k, 1f, 1f, 1f, 0.95f);
        spot(batch, dot, tmp.x, tmp.y, 26f * k, r, g, b, 0.9f);
    }

    /** A spot at a screen point. */
    private static void spot(SpriteBatch batch, Texture t, float sx, float sy, float size,
                             float r, float g, float b, float a) {
        if (a <= 0.005f) {
            return;
        }
        batch.setColor(r, g, b, a);
        batch.draw(t, sx - size / 2f, sy - size / 2f, size, size);
    }

    private static float px(float x, float w, float ix) {
        return x + ix / IW * w;
    }

    private static float py(float y, float h, float iy) {
        return y + (1f - iy / IH) * h;
    }

    private static void spot(SpriteBatch batch, Texture t, float x, float y, float w, float h,
                             float ix, float iy, float size, float r, float g, float b, float a) {
        if (a <= 0.005f) {
            return;
        }
        batch.setColor(r, g, b, a);
        batch.draw(t, px(x, w, ix) - size / 2f, py(y, h, iy) - size / 2f, size, size);
    }

    @Override
    public void dispose() {
        if (core != null) {
            core.getTexture().dispose();
        }
        dot.dispose();
        ring.dispose();
        if (shuttle != null) {
            shuttle.dispose();
        }
    }
}
