package com.buran.game.ui.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.buran.game.conf.PaintConstants;
import com.buran.game.conf.Rules;
import com.buran.game.utils.Assets;

/**
 * Look of the menus: cold telemetry palette, thin frames, one accent colour.
 * Layout helpers cover both TV landscape and phone portrait.
 */
public final class Theme {
    public static final Color TEXT = Color.valueOf("e8eef7ff");
    public static final Color DIM = Color.valueOf("8fa3bdff");
    public static final Color FAINT = Color.valueOf("4f6480ff");
    public static final Color ACCENT = Color.valueOf("ff6a45ff");
    public static final Color OK = Color.valueOf("7fd3a8ff");
    public static final Color LINE = Color.valueOf("9fd0ffff");
    public static final Color PANEL = new Color(0.02f, 0.03f, 0.07f, 0.78f);

    private static Texture pixel;

    private Theme() {
    }

    public static Texture pixel() {
        if (pixel == null) {
            Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            p.setColor(Color.WHITE);
            p.fill();
            pixel = new Texture(p);
            p.dispose();
        }
        return pixel;
    }

    public static BitmapFont font(int size) {
        String name;
        switch (size) {
            case 32: name = PaintConstants.FONT32; break;
            case 44: name = PaintConstants.FONT44; break;
            case 48: name = PaintConstants.FONT48; break;
            case 54: name = PaintConstants.FONT54; break;
            case 64: name = PaintConstants.FONT64; break;
            case 72: name = PaintConstants.FONT72; break;
            case 96: name = PaintConstants.FONT96; break;
            case 128: name = PaintConstants.FONT128; break;
            default: name = PaintConstants.FONT175; break;
        }
        BitmapFont f = Assets.getInstance().getAssetManager().get(name, BitmapFont.class);
        f.getData().markupEnabled = true;
        return f;
    }

    public static boolean portrait() {
        return Rules.WORLD_HEIGHT > Rules.WORLD_WIDTH;
    }

    /** Left edge of the content column. */
    public static float marginX() {
        return portrait() ? Rules.WORLD_WIDTH * 0.07f : Rules.WORLD_WIDTH * 0.085f;
    }

    public static void rect(Batch batch, float x, float y, float w, float h, Color c, float alpha) {
        batch.setColor(c.r, c.g, c.b, c.a * alpha);
        batch.draw(pixel(), x, y, w, h);
        batch.setColor(Color.WHITE);
    }

    /** A dark translucent panel with a hairline frame and accent corner ticks. */
    public static void frame(Batch batch, float x, float y, float w, float h, float alpha) {
        rect(batch, x, y, w, h, PANEL, alpha);
        float t = 3f;
        Color c = FAINT;
        rect(batch, x, y, w, t, c, alpha);
        rect(batch, x, y + h - t, w, t, c, alpha);
        rect(batch, x, y, t, h, c, alpha);
        rect(batch, x + w - t, y, t, h, c, alpha);
        float k = 46f;
        float kt = 6f;
        rect(batch, x, y + h - kt, k, kt, ACCENT, alpha);
        rect(batch, x, y + h - k, kt, k, ACCENT, alpha);
        rect(batch, x + w - k, y, k, kt, ACCENT, alpha);
        rect(batch, x + w - kt, y, kt, k, ACCENT, alpha);
    }
}
