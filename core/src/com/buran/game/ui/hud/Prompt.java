package com.buran.game.ui.hud;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.buran.game.ui.menu.Theme;

/**
 * A button prompt shown where the action happens (above the ship): the key glyph and the
 * action on a small dark plate. Same key look as the menus and the console.
 */
public final class Prompt {
    private Prompt() {
    }

    /** One key and its action, centred on {@code cx}, {@code cy}. */
    public static void draw(Batch batch, BitmapFont font, String key, String action, float cx, float cy, float alpha) {
        draw(batch, font, new String[]{key}, action, cx, cy, alpha);
    }

    /** Several keys ("LB" "RB") and one action. */
    public static void draw(Batch batch, BitmapFont font, String[] keys, String action, float cx, float cy, float alpha) {
        if (alpha <= 0.01f) {
            return;
        }
        float kh = Theme.keyHeight(font);
        float gap = kh * 0.3f;
        float w = 0f;
        for (String k : keys) {
            w += Theme.keyWidth(font, k) + gap;
        }
        w += kh * 0.1f + Theme.width(font, action);
        float padX = kh * 0.5f;
        float ph = kh * 1.5f;
        float x = cx - w / 2f;
        Theme.rect(batch, x - padX - 2f, cy - ph / 2f - 2f, w + 2f * padX + 4f, ph + 4f, Theme.EDGE, alpha * 0.9f);
        Theme.rect(batch, x - padX, cy - ph / 2f, w + 2f * padX, ph, Theme.BODY_LO, alpha * 0.88f);
        for (String k : keys) {
            x += Theme.key(batch, font, k, x, cy, alpha) + gap;
        }
        Theme.text(batch, font, action, x + kh * 0.1f, cy + font.getCapHeight() / 2f, Theme.TEXT, alpha);
    }

    /** A line of text on the same plate, no key. */
    public static void text(Batch batch, BitmapFont font, String line, float cx, float cy, float alpha) {
        if (alpha <= 0.01f) {
            return;
        }
        float kh = Theme.keyHeight(font);
        float w = Theme.width(font, line);
        float padX = kh * 0.5f;
        float ph = kh * 1.5f;
        float x = cx - w / 2f;
        Theme.rect(batch, x - padX - 2f, cy - ph / 2f - 2f, w + 2f * padX + 4f, ph + 4f, Theme.EDGE, alpha * 0.9f);
        Theme.rect(batch, x - padX, cy - ph / 2f, w + 2f * padX, ph, Theme.BODY_LO, alpha * 0.88f);
        Theme.text(batch, font, line, x, cy + font.getCapHeight() / 2f, Theme.TEXT, alpha);
    }
}
