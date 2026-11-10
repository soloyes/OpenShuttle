package com.buran.game.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.buran.game.conf.Rules;

/**
 * One look for every "it is over there" marker at the screen edge: a dark disc with a coloured
 * rim and icon, an arrow on the rim pointing at the target, and the distance under it.
 * Screen pass, UI camera (1920-high world units).
 */
public final class EdgeMarker {
    private static final GlyphLayout LAYOUT = new GlyphLayout();
    private static final float SIZE = 104f;

    private EdgeMarker() {
    }

    /**
     * @param dx,dy  target offset from the camera centre, world units
     * @param dist   distance from the ship, world units (negative = do not show)
     */
    public static void draw(SpriteBatch batch, ThreatTextures tx, BitmapFont font, Texture icon,
                            float dx, float dy, float dist, Color c, float alpha, float pulse) {
        float W = Rules.WORLD_WIDTH;
        float H = Rules.WORLD_HEIGHT;
        float scale = H / Rules.VIEW_HEIGHT;
        float hx = W / 2f - Rules.SAFE_X - 120f;
        float hy = H / 2f - Rules.SAFE_Y - 130f;
        float sx = dx * scale;
        float sy = dy * scale;
        float k = Math.min(hx / Math.max(Math.abs(sx), 0.001f), hy / Math.max(Math.abs(sy), 0.001f));
        float px = W / 2f + sx * k;
        float py = H / 2f + sy * k;
        float ang = MathUtils.atan2(sy, sx);
        float r = SIZE / 2f;

        // Soft glow that breathes with the pulse.
        batch.setColor(c.r, c.g, c.b, alpha * 0.22f * pulse);
        batch.draw(tx.dot, px - r * 2f, py - r * 2f, r * 4f, r * 4f);
        // Body and rim.
        batch.setColor(0.03f, 0.05f, 0.08f, alpha * 0.8f);
        batch.draw(tx.disc, px - r, py - r, SIZE, SIZE);
        batch.setColor(c.r, c.g, c.b, alpha);
        batch.draw(tx.ring, px - r, py - r, SIZE, SIZE);
        // Icon.
        float is = SIZE * 0.56f;
        batch.draw(icon, px - is / 2f, py - is / 2f, is, is);
        // Arrow sitting on the rim.
        float as = 34f;
        float ax = px + MathUtils.cos(ang) * (r + as * 0.55f);
        float ay = py + MathUtils.sin(ang) * (r + as * 0.55f);
        batch.draw(tx.arrow, ax - as / 2f, ay - as / 2f, as / 2f, as / 2f, as, as, 1f, 1f,
                ang * MathUtils.radiansToDegrees, 0, 0, tx.arrow.getWidth(), tx.arrow.getHeight(), false, false);
        batch.setColor(Color.WHITE);

        if (font != null && dist >= 0f) {
            String s = (int) (dist / 10f) + " M";
            Color old = font.getColor().cpy();
            font.setColor(c.r, c.g, c.b, alpha * 0.9f);
            LAYOUT.setText(font, s);
            // Text goes on the side away from the screen edge.
            float ty = py - r - 14f;
            if (py - r - LAYOUT.height - 30f < Rules.SAFE_Y) {
                ty = py + r + LAYOUT.height + 14f;
            }
            font.draw(batch, s, px - LAYOUT.width / 2f, ty);
            font.setColor(old);
        }
    }
}
