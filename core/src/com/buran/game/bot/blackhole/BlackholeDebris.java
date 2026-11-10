package com.buran.game.bot.blackhole;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import com.buran.game.conf.Buffs;

/**
 * Matter falling into the black hole. Everything the hole captures sheds glowing "sand"
 * that orbits on Keplerian paths in the plane of the accretion disk (same tilt as the
 * composite shader), spirals inward, stretches along its motion and heats up before it
 * crosses the horizon. Drawn in the world pass, so the screen-space lens smears it.
 */
public class BlackholeDebris implements Disposable {
    /** Must match TILT in composite.frag (cosine of the disk inclination). */
    private static final float TILT = 0.30f;
    private static final int MAX = 1400;

    private static BlackholeDebris instance;

    private final float[] r = new float[MAX];
    private final float[] phi = new float[MAX];
    private final float[] life = new float[MAX];
    private final float[] cr = new float[MAX];
    private final float[] cg = new float[MAX];
    private final float[] cb = new float[MAX];
    private int count;
    private final Texture dot;
    private float ambient;

    public BlackholeDebris() {
        instance = this;
        int s = 32;
        Pixmap p = new Pixmap(s, s, Pixmap.Format.RGBA8888);
        p.setBlending(Pixmap.Blending.None);
        for (int y = 0; y < s; y++) {
            for (int x = 0; x < s; x++) {
                float dx = (x + 0.5f) / s * 2f - 1f;
                float dy = (y + 0.5f) / s * 2f - 1f;
                float d = (float) Math.sqrt(dx * dx + dy * dy);
                float a = MathUtils.clamp(1f - d, 0f, 1f);
                p.setColor(1f, 1f, 1f, a * a);
                p.drawPixel(x, y);
            }
        }
        dot = new Texture(p);
        dot.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        p.dispose();
    }

    public static BlackholeDebris get() {
        return instance;
    }

    private static float shadowRadius() {
        return Buffs.getBlackholeTakeRange() * 0.17f;
    }

    /** Sheds {@code n} grains from a world position, tinted by the object's colour. */
    public void emit(float x, float y, int n, float red, float green, float blue) {
        float dx = x - Blackhole.position.x;
        float dy = (y - Blackhole.position.y) / TILT;
        float rr = Math.min((float) Math.sqrt(dx * dx + dy * dy), Buffs.getBlackholeTakeRange());
        float pp = MathUtils.atan2(dy, dx);
        for (int i = 0; i < n && count < MAX; i++) {
            r[count] = Math.max(shadowRadius() * 1.3f, rr + MathUtils.random(-25f, 25f));
            phi[count] = pp + MathUtils.random(-0.15f, 0.15f);
            life[count] = 1f;
            cr[count] = red;
            cg[count] = green;
            cb[count] = blue;
            count++;
        }
    }

    public static void shed(float x, float y, Color c) {
        shed(x, y, c, 3);
    }

    public static void shed(float x, float y, Color c, int n) {
        if (instance != null) {
            float w = 0.55f;
            instance.emit(x, y, n, MathUtils.lerp(c.r, 1f, w), MathUtils.lerp(c.g, 0.7f, w), MathUtils.lerp(c.b, 0.45f, w));
        }
    }

    /** The whole object breaks into sand spread over its own footprint. */
    public static void burst(float x, float y, float size, Color c) {
        if (instance == null) {
            return;
        }
        int n = MathUtils.clamp((int) (size * 0.5f), 30, 120);
        for (int i = 0; i < n; i++) {
            float a = MathUtils.random(MathUtils.PI2);
            float r = MathUtils.random(size * 0.45f);
            shed(x + MathUtils.cos(a) * r, y + MathUtils.sin(a) * r, c, 1);
        }
    }

    public void update(float dt) {
        // A faint constant inflow keeps the disk alive even when nothing is caught.
        ambient += dt * 45f;
        float range = Buffs.getBlackholeTakeRange();
        while (ambient >= 1f && count < MAX) {
            ambient -= 1f;
            r[count] = range * MathUtils.random(0.55f, 0.95f);
            phi[count] = MathUtils.random(MathUtils.PI2);
            life[count] = 0.6f;
            cr[count] = 1f;
            cg[count] = 0.55f;
            cb[count] = 0.25f;
            count++;
        }
        float horizon = shadowRadius();
        float k = 2.2f * (float) Math.pow(150f, 1.5f);
        for (int i = 0; i < count; i++) {
            float rr = r[i];
            phi[i] += k / (float) Math.pow(rr, 1.5f) * dt;
            r[i] = rr - (18f + 7000f / rr) * dt;
            if (r[i] < horizon) {
                // Crossed the horizon: swap-remove.
                count--;
                r[i] = r[count];
                phi[i] = phi[count];
                life[i] = life[count];
                cr[i] = cr[count];
                cg[i] = cg[count];
                cb[i] = cb[count];
                i--;
            }
        }
    }

    public void render(SpriteBatch batch) {
        if (count == 0) {
            return;
        }
        float cx = Blackhole.position.x;
        float cy = Blackhole.position.y;
        float horizon = shadowRadius();
        float range = Buffs.getBlackholeTakeRange();
        float k = 2.2f * (float) Math.pow(150f, 1.5f);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        for (int i = 0; i < count; i++) {
            float rr = r[i];
            float c = MathUtils.cos(phi[i]);
            float s = MathUtils.sin(phi[i]);
            float x = cx + rr * c;
            float y = cy + rr * s * TILT;
            // Tangent of the projected orbit, and speed for the streak length.
            float tx = -s;
            float ty = c * TILT;
            float angle = MathUtils.atan2(ty, tx) * MathUtils.radiansToDegrees;
            float speed = k / (float) Math.sqrt(rr);
            float len = MathUtils.clamp(speed * 0.05f, 8f, 70f);
            // Heats up as it falls: towards white-yellow near the horizon.
            float heat = 1f - MathUtils.clamp((rr - horizon) / (range - horizon), 0f, 1f);
            float red = MathUtils.lerp(cr[i], 1f, heat);
            float green = MathUtils.lerp(cg[i], 0.92f, heat * heat);
            float blue = MathUtils.lerp(cb[i], 0.75f, heat * heat * heat);
            float fadeIn = MathUtils.clamp((range - rr) / 80f + 0.3f, 0f, 1f);
            float fadeOut = MathUtils.clamp((rr - horizon) / 40f, 0f, 1f);
            batch.setColor(red, green, blue, 0.75f * life[i] * fadeIn * fadeOut);
            float h = 7f + 5f * heat;
            batch.draw(dot, x - len / 2f, y - h / 2f, len / 2f, h / 2f, len, h, 1f, 1f, angle, 0, 0, 32, 32, false, false);
        }
        batch.setColor(Color.WHITE);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void clear() {
        count = 0;
    }

    @Override
    public void dispose() {
        dot.dispose();
        if (instance == this) {
            instance = null;
        }
    }
}
