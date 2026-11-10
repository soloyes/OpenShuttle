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
 * Look of the menus and the HUD: one ship instrument. Dark panels with hairline edges and
 * screws, small section labels, amber for whatever is selected or current, red for alerts.
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
    /** Selected / current item, as the lit cargo slot of the console. */
    public static final Color AMBER = Color.valueOf("ffb547ff");
    /** Instrument body: lighter on top, darker below. */
    public static final Color BODY = new Color(0.075f, 0.098f, 0.14f, 1f);
    public static final Color BODY_LO = new Color(0.035f, 0.05f, 0.075f, 1f);
    /** Hairlines, dividers, frames. */
    public static final Color EDGE = Color.valueOf("2a3647ff");
    /** Section labels ("LIFE SUPPORT", "NAV-01"). */
    public static final Color HEAD = Color.valueOf("6b7c93ff");
    /** Empty part of a gauge. */
    public static final Color TRACK = Color.valueOf("0a0f16ff");
    private static final Color SCREW = Color.valueOf("3a4a60ff");
    private static final Color tmp = new Color();

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

    /** The instrument panel used everywhere (menus, briefing card, codex frames). */
    public static void frame(Batch batch, float x, float y, float w, float h, float alpha) {
        panel(batch, x, y, w, h, alpha);
    }

    /**
     * An instrument body: vertical gradient, hairline frame, a faint highlight under the top
     * edge and four screws in the corners.
     */
    public static void panel(Batch batch, float x, float y, float w, float h, float alpha) {
        int bands = 16;
        for (int i = 0; i < bands; i++) {
            float t = i / (float) (bands - 1);
            tmp.set(BODY_LO).lerp(BODY, t);
            rect(batch, x, y + h * i / bands, w, h / bands + 1f, tmp, alpha);
        }
        float t = 2f;
        rect(batch, x, y, w, t, EDGE, alpha);
        rect(batch, x, y + h - t, w, t, EDGE, alpha);
        rect(batch, x, y, t, h, EDGE, alpha);
        rect(batch, x + w - t, y, t, h, EDGE, alpha);
        rect(batch, x + t, y + h - t - 2f, w - 2f * t, 2f, Color.WHITE, 0.05f * alpha);
        float in = 18f;
        screw(batch, x + in, y + in, alpha);
        screw(batch, x + w - in, y + in, alpha);
        screw(batch, x + in, y + h - in, alpha);
        screw(batch, x + w - in, y + h - in, alpha);
    }

    /** A flat-head screw: a small square with a slot. */
    private static void screw(Batch batch, float cx, float cy, float alpha) {
        float s = 10f;
        rect(batch, cx - s / 2f, cy - s / 2f, s, s, SCREW, alpha);
        rect(batch, cx - s / 2f + 1f, cy - 1f, s - 2f, 2f, TRACK, alpha);
    }

    /** A horizontal divider inside a panel. */
    public static void hairline(Batch batch, float x, float y, float w, float alpha) {
        rect(batch, x, y, w, 2f, EDGE, alpha);
    }

    /** A vertical divider inside a panel. */
    public static void vline(Batch batch, float x, float y, float h, float alpha) {
        rect(batch, x, y, 2f, h, EDGE, alpha);
    }

    /** Text in a colour, alpha-multiplied; restores the font colour. */
    public static void text(Batch batch, BitmapFont font, String s, float x, float y, Color c, float alpha) {
        if (s == null) {
            return;
        }
        Color old = font.getColor().cpy();
        font.setColor(c.r, c.g, c.b, c.a * alpha);
        font.draw(batch, s, x, y);
        font.setColor(old);
    }

    /** Right-aligned text ending at x. */
    public static void textR(Batch batch, BitmapFont font, String s, float x, float y, Color c, float alpha) {
        if (s == null) {
            return;
        }
        GLYPHS.setText(font, s);
        text(batch, font, s, x - GLYPHS.width, y, c, alpha);
    }

    public static float width(BitmapFont font, String s) {
        GLYPHS.setText(font, s);
        return GLYPHS.width;
    }

    /** A status lamp: a small square, lit or dark. */
    public static void lamp(Batch batch, float x, float y, Color c, boolean lit, float alpha) {
        float s = 14f;
        if (lit) {
            rect(batch, x - 4f, y - 4f, s + 8f, s + 8f, c, 0.18f * alpha);
        }
        rect(batch, x, y, s, s, c, alpha * (lit ? 1f : 0.25f));
    }

    // ------------------------------------------------------------------ button glyphs

    private static Texture disc;
    private static Texture ring;

    private static Texture circle(boolean filled) {
        int n = 96;
        Pixmap p = new Pixmap(n, n, Pixmap.Format.RGBA8888);
        p.setBlending(Pixmap.Blending.None);
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                float sum = 0f;
                for (int j = 0; j < 4; j++) {
                    for (int i = 0; i < 4; i++) {
                        float dx = (x + (i + 0.5f) / 4f) / n * 2f - 1f;
                        float dy = (y + (j + 0.5f) / 4f) / n * 2f - 1f;
                        float d = (float) Math.sqrt(dx * dx + dy * dy);
                        sum += filled ? (d < 0.98f ? 1f : 0f) : (d < 0.98f && d > 0.84f ? 1f : 0f);
                    }
                }
                p.setColor(1f, 1f, 1f, sum / 16f);
                p.drawPixel(x, y);
            }
        }
        Texture t = new Texture(p);
        t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        p.dispose();
        return t;
    }

    /** Face-button colours of the pad, so A/B/X/Y read at a glance. */
    private static Color faceColor(String key) {
        switch (key) {
            case "A": return FACE_A;
            case "B": return FACE_B;
            case "X": return FACE_X;
            case "Y": return FACE_Y;
            default: return null;
        }
    }

    private static final Color FACE_A = Color.valueOf("6cc24aff");
    private static final Color FACE_B = Color.valueOf("e5533dff");
    private static final Color FACE_X = Color.valueOf("3f8fe8ff");
    private static final Color FACE_Y = Color.valueOf("f2c230ff");

    /** Height of a key glyph for this font. */
    public static float keyHeight(BitmapFont font) {
        return font.getCapHeight() * 1.9f;
    }

    /** Width of a key glyph. */
    public static float keyWidth(BitmapFont font, String key) {
        float h = keyHeight(font);
        if (faceColor(key) != null) {
            return h;
        }
        return Math.max(h * 1.3f, width(font, key) + h * 0.8f);
    }

    /**
     * Draws one button the same way everywhere: pad face buttons as a dark disc with a ring and
     * letter in the button's colour, every other key as a dark keycap with a hairline frame.
     * {@code cy} is the vertical centre. Returns the width.
     */
    public static float key(Batch batch, BitmapFont font, String key, float x, float cy, float alpha) {
        if (disc == null) {
            disc = circle(true);
            ring = circle(false);
        }
        float h = keyHeight(font);
        float w = keyWidth(font, key);
        Color face = faceColor(key);
        float ty = cy + font.getCapHeight() / 2f;
        if (face != null) {
            batch.setColor(BODY_LO.r, BODY_LO.g, BODY_LO.b, alpha);
            batch.draw(disc, x, cy - h / 2f, h, h);
            batch.setColor(face.r, face.g, face.b, alpha);
            batch.draw(ring, x, cy - h / 2f, h, h);
            batch.setColor(Color.WHITE);
            text(batch, font, key, x + h / 2f - width(font, key) / 2f, ty, face, alpha);
        } else {
            rect(batch, x, cy - h / 2f, w, h, EDGE, alpha);
            rect(batch, x + 2f, cy - h / 2f + 2f, w - 4f, h - 4f, BODY_LO, alpha);
            rect(batch, x + 2f, cy - h / 2f + 2f, w - 4f, 3f, Color.BLACK, 0.4f * alpha);
            text(batch, font, key, x + w / 2f - width(font, key) / 2f, ty, TEXT, alpha);
        }
        return w;
    }

    /** "[key]  ACTION": the key glyph and the action next to it. Returns the width. */
    public static float hint(Batch batch, BitmapFont font, String key, String action, float x, float cy, Color c, float alpha) {
        float w = key(batch, font, key, x, cy, alpha);
        float gap = keyHeight(font) * 0.4f;
        text(batch, font, action, x + w + gap, cy + font.getCapHeight() / 2f, c, alpha);
        return w + gap + width(font, action);
    }

    public static float hintWidth(BitmapFont font, String key, String action) {
        return keyWidth(font, key) + keyHeight(font) * 0.4f + width(font, action);
    }

    private static final com.badlogic.gdx.graphics.g2d.GlyphLayout GLYPHS = new com.badlogic.gdx.graphics.g2d.GlyphLayout();
}
