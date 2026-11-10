package com.buran.game.world;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;

/** Soft procedural shapes shared by shelters and the threat overlay. */
public final class ThreatTextures implements Disposable {
    public final Texture dot;
    public final Texture ring;
    public final Texture chevron;
    /** Solid anti-aliased disc. */
    public final Texture disc;
    /** Solid triangle pointing right (+x). */
    public final Texture arrow;
    /** Solid diamond: the beacon icon. */
    public final Texture diamond;
    /** A dome over a base line: the shelter icon. */
    public final Texture dome;
    /** Marker body: a disc drawn out into a point at +x (fill and rim). */
    public final Texture pin;
    public final Texture pinRim;
    /** Pin geometry in texture space ([-1, 1]): disc centre x and radius. */
    public static final float PIN_CX = -0.3f;
    public static final float PIN_R = 0.55f;

    public ThreatTextures() {
        dot = make(64, (dx, dy) -> {
            float d = (float) Math.sqrt(dx * dx + dy * dy);
            float a = MathUtils.clamp(1f - d, 0f, 1f);
            return a * a;
        });
        ring = make(256, (dx, dy) -> {
            float d = (float) Math.sqrt(dx * dx + dy * dy);
            return MathUtils.clamp(1f - Math.abs(d - 0.95f) * 40f, 0f, 1f);
        });
        // A chevron pointing right (+x).
        chevron = make(64, (dx, dy) -> {
            float ax = Math.abs(dy);
            float edge = 0.55f - ax * 0.9f;
            float inner = edge - 0.32f;
            boolean on = dx < edge && dx > inner && ax < 0.62f;
            return on ? 1f : 0f;
        });
        disc = make(128, (dx, dy) -> dx * dx + dy * dy < 0.97f ? 1f : 0f);
        arrow = make(64, (dx, dy) -> dx > -0.7f && Math.abs(dy) < (0.8f - dx) * 0.55f ? 1f : 0f);
        diamond = make(64, (dx, dy) -> Math.abs(dx) + Math.abs(dy) < 0.9f ? 1f : 0f);
        dome = make(64, (dx, dy) -> {
            float d = (float) Math.sqrt(dx * dx + (dy + 0.35f) * (dy + 0.35f));
            boolean arc = dy > -0.35f && d < 0.85f && d > 0.6f;
            boolean base = dy < -0.4f && dy > -0.6f && Math.abs(dx) < 0.9f;
            boolean core = d < 0.3f && dy > -0.35f;
            return arc || base || core ? 1f : 0f;
        });
        pin = make(128, (dx, dy) -> pinSd(dx, dy) < 0f ? 1f : 0f);
        pinRim = make(128, (dx, dy) -> {
            float sd = pinSd(dx, dy);
            return sd < 0f && sd > -0.07f ? 1f : 0f;
        });
    }

    /** Signed distance to the pin outline (negative inside). */
    private static float pinSd(float dx, float dy) {
        float dc = (float) Math.sqrt((dx - PIN_CX) * (dx - PIN_CX) + dy * dy) - PIN_R;
        float tip = 0.95f;
        float d = tip - PIN_CX;
        float half = (float) Math.asin(PIN_R / d);
        float sn = MathUtils.sin(half);
        float cs = MathUtils.cos(half);
        float line = (dx - tip) * sn + Math.abs(dy) * cs;
        float tangentX = PIN_CX + PIN_R * sn;
        float cone = Math.max(line, tangentX - dx);
        return Math.min(dc, cone);
    }

    private interface Shape {
        float alpha(float dx, float dy);
    }

    private static Texture make(int s, Shape shape) {
        Pixmap p = new Pixmap(s, s, Pixmap.Format.RGBA8888);
        p.setBlending(Pixmap.Blending.None);
        // 4x4 supersampling: clean edges for hard shapes, unchanged soft ones.
        final int n = 4;
        for (int y = 0; y < s; y++) {
            for (int x = 0; x < s; x++) {
                float sum = 0f;
                for (int j = 0; j < n; j++) {
                    for (int i = 0; i < n; i++) {
                        float dx = (x + (i + 0.5f) / n) / s * 2f - 1f;
                        // Pixmap rows run top-down; flip so +y is up like the world.
                        float dy = 1f - (y + (j + 0.5f) / n) / s * 2f;
                        sum += shape.alpha(dx, dy);
                    }
                }
                p.setColor(1f, 1f, 1f, sum / (n * n));
                p.drawPixel(x, y);
            }
        }
        Texture t = new Texture(p);
        t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        p.dispose();
        return t;
    }

    @Override
    public void dispose() {
        dot.dispose();
        ring.dispose();
        chevron.dispose();
        disc.dispose();
        arrow.dispose();
        diamond.dispose();
        dome.dispose();
        pin.dispose();
        pinRim.dispose();
    }
}
