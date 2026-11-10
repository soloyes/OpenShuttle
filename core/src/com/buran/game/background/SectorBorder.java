package com.buran.game.background;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import com.buran.game.conf.Rules;
import com.buran.game.player.Buran;

/**
 * The edge of the playable sector, drawn as a dashed glowing line that fades in as the
 * shuttle approaches it. Leaving the sector costs hull and fuel and eventually a life, so the
 * player has to see the edge before it hurts.
 */
public class SectorBorder implements Disposable {
    private static final float DASH = 120f;
    private static final float GAP = 60f;
    private static final float THICKNESS = 22f;
    private static final float GLOW = 220f;

    private final Texture line;
    private final Texture glow;
    private final Color color = new Color(1f, 0.38f, 0.26f, 1f);
    private float time;

    public SectorBorder() {
        Pixmap p = new Pixmap(4, 4, Pixmap.Format.RGBA8888);
        p.setColor(Color.WHITE);
        p.fill();
        line = new Texture(p);
        p.dispose();

        // Soft falloff strip (vertical gradient) for the glow around the line.
        Pixmap g = new Pixmap(4, 64, Pixmap.Format.RGBA8888);
        g.setBlending(Pixmap.Blending.None);
        for (int y = 0; y < 64; y++) {
            float d = Math.abs(y - 31.5f) / 31.5f;
            float a = (1f - d) * (1f - d);
            g.setColor(1f, 1f, 1f, a);
            g.drawLine(0, y, 3, y);
        }
        glow = new Texture(g);
        glow.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        g.dispose();
    }

    public void update(float dt) {
        time += dt;
    }

    public void render(SpriteBatch batch) {
        float w = Rules.SPACE_WIDTH;
        float h = Rules.SPACE_HEIGHT;
        float px = Buran.position.x;
        float py = Buran.position.y;
        // Distance from the ship to the nearest edge (negative = outside).
        float edge = Math.min(Math.min(px, w - px), Math.min(py, h - py));
        float fadeStart = Math.min(Rules.VIEW_WIDTH, Rules.VIEW_HEIGHT) * 0.75f;
        float alpha = MathUtils.clamp(1f - edge / fadeStart, 0f, 1f);
        if (alpha <= 0f) {
            return;
        }
        float pulse = 0.85f + 0.15f * MathUtils.sin(time * 5f);
        float a = alpha * pulse;

        Color old = batch.getColor().cpy();
        // March the dashes so the edge feels alive.
        float offset = (time * 120f) % (DASH + GAP);

        batch.setColor(color.r, color.g, color.b, a * 0.55f);
        batch.draw(glow, 0, -GLOW / 2, w, GLOW);
        batch.draw(glow, 0, h - GLOW / 2, w, GLOW);
        drawVerticalGlow(batch, -GLOW / 2, h);
        drawVerticalGlow(batch, w - GLOW / 2, h);

        batch.setColor(color.r, color.g, color.b, a);
        dashedHorizontal(batch, 0, w, 0, offset);
        dashedHorizontal(batch, 0, w, h, offset);
        dashedVertical(batch, 0, h, 0, offset);
        dashedVertical(batch, 0, h, w, offset);
        batch.setColor(old);
    }

    private void drawVerticalGlow(SpriteBatch batch, float x, float h) {
        // The gradient strip is vertical; rotate it 90 degrees for the left/right edges.
        batch.draw(glow, x + GLOW / 2 - h / 2, h / 2 - GLOW / 2, h / 2, GLOW / 2, h, GLOW, 1f, 1f, 90f,
                0, 0, 4, 64, false, false);
    }

    private void dashedHorizontal(SpriteBatch batch, float x0, float x1, float y, float offset) {
        for (float x = x0 - offset; x < x1; x += DASH + GAP) {
            float s = Math.max(x, x0);
            float e = Math.min(x + DASH, x1);
            if (e > s) {
                batch.draw(line, s, y - THICKNESS / 2, e - s, THICKNESS);
            }
        }
    }

    private void dashedVertical(SpriteBatch batch, float y0, float y1, float x, float offset) {
        for (float y = y0 - offset; y < y1; y += DASH + GAP) {
            float s = Math.max(y, y0);
            float e = Math.min(y + DASH, y1);
            if (e > s) {
                batch.draw(line, x - THICKNESS / 2, s, THICKNESS, e - s);
            }
        }
    }

    @Override
    public void dispose() {
        line.dispose();
        glow.dispose();
    }
}
