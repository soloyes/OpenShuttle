package com.buran.game.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.buran.game.conf.PaintConstants;
import com.buran.game.ui.menu.Theme;
import com.buran.game.utils.Assets;

/**
 * A quiet readout next to an event in the world: small, one colour from the instrument
 * palette, rises a little and fades. No shaking, no giant letters (tip 61: one mood).
 *
 * @author soloyes on 03/07/18.
 */
public class FlyingText {
    private static final float LIFE = 1.6f;
    private static final float SIZE = 1.5f;
    private static final float RISE = 70f;
    private static final Color WARM = Color.valueOf("ffd28aff");

    private final BitmapFont font;
    private final GlyphLayout layout = new GlyphLayout();
    private final Vector2 position = new Vector2();
    private final Color color = new Color();
    private String text = "";
    private float age;
    private boolean active;

    public FlyingText() {
        font = Assets.getInstance().getAssetManager().get(PaintConstants.FONT32);
    }

    public void render(SpriteBatch batch) {
        if (!active) {
            return;
        }
        float t = age / LIFE;
        float a = t < 0.15f ? t / 0.15f : 1f - Interpolation.pow2In.apply((t - 0.15f) / 0.85f);
        float y = position.y + 60f + RISE * Interpolation.pow2Out.apply(t);
        boolean markup = font.getData().markupEnabled;
        float sx = font.getData().scaleX;
        float sy = font.getData().scaleY;
        font.getData().markupEnabled = false;
        font.getData().setScale(SIZE);
        layout.setText(font, text);
        Color old = font.getColor().cpy();
        // A faint dark backing keeps the line readable over nebulae.
        font.setColor(0f, 0f, 0f, a * 0.5f);
        font.draw(batch, text, position.x - layout.width / 2f + 3f, y + layout.height - 3f);
        font.setColor(color.r, color.g, color.b, a * 0.95f);
        font.draw(batch, text, position.x - layout.width / 2f, y + layout.height);
        font.setColor(old);
        font.getData().setScale(sx, sy);
        font.getData().markupEnabled = markup;
    }

    public void setup(float x, float y, String text, float scale, float dstScale, boolean colored, boolean giant) {
        String plain = strip(text).replace("MURDERER", "CREW LOST").replace("SCORE", "").replace("\n", " ").trim();
        while (plain.contains("  ")) {
            plain = plain.replace("  ", " ");
        }
        if (plain.startsWith("- ")) {
            plain = "-" + plain.substring(2);
        }
        if (plain.isEmpty()) {
            return;
        }
        if (text.contains("[RED") || text.startsWith("MURDERER")) {
            color.set(Theme.ACCENT);
        } else if (text.contains("[GREEN")) {
            color.set(Theme.OK);
        } else if (text.contains("[GOLD") || text.contains("[YELLOW")) {
            color.set(WARM);
        } else {
            color.set(Theme.DIM);
        }
        this.text = plain;
        this.position.set(x, y);
        this.age = 0f;
        this.active = true;
    }

    /** Leaves only the text of a markup string ("[GREEN] +50[]" -> " +50"). */
    private static String strip(String s) {
        StringBuilder b = new StringBuilder();
        boolean tag = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '[') {
                tag = true;
            } else if (c == ']' && tag) {
                tag = false;
            } else if (!tag) {
                b.append(c);
            }
        }
        return b.toString();
    }

    public void update(float dt) {
        if (active) {
            age += dt;
            if (age >= LIFE) {
                active = false;
            }
        }
    }

    public boolean isActive() {
        return active;
    }
}
