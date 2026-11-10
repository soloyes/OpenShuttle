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
    private static final float TOP_RESERVE = 300f;

    private EdgeMarker() {
    }

    /** Camera-relative offset to an offset from the centre of the window above the console. */
    public static float windowDy(float dy) {
        return dy - Rules.hudFraction() * Rules.VIEW_HEIGHT / 2f;
    }

    /** Whether a camera-relative offset is visible in the window (pad in world units). */
    public static boolean inView(float dx, float dy, float pad) {
        float halfH = Rules.VIEW_HEIGHT * (1f - Rules.hudFraction()) / 2f;
        return Math.abs(dx) < Rules.VIEW_WIDTH / 2f + pad && Math.abs(windowDy(dy)) < halfH + pad;
    }

    /** Bottom of the window in UI units. */
    public static float bottomEdge() {
        return Math.max(Rules.SAFE_Y, Rules.HUD_BOTTOM);
    }

    /**
     * Projects a camera-relative offset onto the window's edge; returns {x, y, angle(rad)} in
     * UI units, keeping {@code inset} from the window border.
     */
    public static float[] project(float dx, float dy, float inset) {
        float W = Rules.WORLD_WIDTH;
        float H = Rules.WORLD_HEIGHT;
        float scale = H / Rules.VIEW_HEIGHT;
        // Markers stay below the lives and the task line at the top.
        float top = H - Rules.SAFE_Y - TOP_RESERVE;
        float bottom = bottomEdge();
        float cy = (top + bottom) / 2f;
        float hx = W / 2f - Rules.SAFE_X - inset;
        float hy = (top - bottom) / 2f - inset;
        float sx = dx * scale;
        float sy = windowDy(dy) * scale;
        float k = Math.min(hx / Math.max(Math.abs(sx), 0.001f), hy / Math.max(Math.abs(sy), 0.001f));
        return new float[]{W / 2f + sx * k, cy + sy * k, MathUtils.atan2(sy, sx)};
    }

    /**
     * A pin at the window edge: a dark disc drawn out into a point aimed at the target, a rim
     * in the target's colour, the icon in the disc and the distance on the side facing inward.
     *
     * @param dx,dy  target offset from the camera centre, world units
     * @param dist   distance from the ship, world units (negative = do not show)
     */
    public static void draw(SpriteBatch batch, ThreatTextures tx, BitmapFont font, Texture icon,
                            float dx, float dy, float dist, Color c, float alpha, float pulse) {
        float S = 190f;
        float r = ThreatTextures.PIN_R * S / 2f;
        float[] p = project(dx, dy, 120f);
        float ang = p[2];
        // The disc sits back from the edge so the point reaches toward it.
        float px = p[0] - MathUtils.cos(ang) * 22f;
        float py = p[1] - MathUtils.sin(ang) * 22f;
        float ox = S / 2f + ThreatTextures.PIN_CX * S / 2f;
        float oy = S / 2f;
        float deg = ang * MathUtils.radiansToDegrees;

        batch.setColor(c.r, c.g, c.b, alpha * 0.18f * pulse);
        batch.draw(tx.dot, px - r * 2.2f, py - r * 2.2f, r * 4.4f, r * 4.4f);
        batch.setColor(0.03f, 0.045f, 0.07f, alpha * 0.88f);
        batch.draw(tx.pin, px - ox, py - oy, ox, oy, S, S, 1f, 1f, deg, 0, 0, tx.pin.getWidth(), tx.pin.getHeight(), false, false);
        batch.setColor(c.r, c.g, c.b, alpha);
        batch.draw(tx.pinRim, px - ox, py - oy, ox, oy, S, S, 1f, 1f, deg, 0, 0, tx.pinRim.getWidth(), tx.pinRim.getHeight(), false, false);
        float is = r * 0.95f;
        batch.draw(icon, px - is / 2f, py - is / 2f, is, is);
        batch.setColor(Color.WHITE);

        if (font != null && dist >= 0f) {
            String s = (int) (dist / 10f) + " M";
            Color old = font.getColor().cpy();
            LAYOUT.setText(font, s);
            // Opposite the point: toward the middle of the window.
            float tx0 = px - MathUtils.cos(ang) * (r + 18f + LAYOUT.width / 2f);
            float ty0 = py - MathUtils.sin(ang) * (r + 18f + LAYOUT.height / 2f);
            float bw = LAYOUT.width + 24f;
            float bh = LAYOUT.height + 18f;
            batch.setColor(0.03f, 0.045f, 0.07f, alpha * 0.75f);
            com.buran.game.ui.menu.Theme.rect(batch, tx0 - bw / 2f, ty0 - bh / 2f, bw, bh,
                    com.buran.game.ui.menu.Theme.BODY_LO, alpha * 0.85f);
            font.setColor(c.r, c.g, c.b, alpha);
            font.draw(batch, s, tx0 - LAYOUT.width / 2f, ty0 + LAYOUT.height / 2f);
            font.setColor(old);
        }
    }
}
