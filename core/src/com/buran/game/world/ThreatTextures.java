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
    }

    private interface Shape {
        float alpha(float dx, float dy);
    }

    private static Texture make(int s, Shape shape) {
        Pixmap p = new Pixmap(s, s, Pixmap.Format.RGBA8888);
        p.setBlending(Pixmap.Blending.None);
        for (int y = 0; y < s; y++) {
            for (int x = 0; x < s; x++) {
                float dx = (x + 0.5f) / s * 2f - 1f;
                float dy = (y + 0.5f) / s * 2f - 1f;
                p.setColor(1f, 1f, 1f, shape.alpha(dx, dy));
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
    }
}
