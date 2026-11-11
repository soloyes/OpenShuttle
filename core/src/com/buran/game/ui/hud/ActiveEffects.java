package com.buran.game.ui.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.buran.game.conf.Rules;
import com.buran.game.ui.menu.Theme;

import java.util.ArrayList;
import java.util.List;

/**
 * What is working right now and for how long (tip 56): FREEZE, SHIELD, SCAN. Small plates just
 * above the console, each with a draining bar and the seconds left; they go when it ends.
 */
public final class ActiveEffects {
    private static final class Effect {
        final String name;
        final float total;
        final Color color;
        float left;

        Effect(String name, float total, Color color) {
            this.name = name;
            this.total = total;
            this.left = total;
            this.color = color;
        }
    }

    private static final List<Effect> ACTIVE = new ArrayList<>();
    public static final Color ICE = Color.valueOf("5fd7ffff");
    public static final Color WARM = Color.valueOf("ffb547ff");
    public static final Color SCAN = Color.valueOf("6cc24aff");

    private ActiveEffects() {
    }

    /** Starts (or refreshes) an effect. */
    public static void start(String name, float seconds, Color color) {
        for (Effect e : ACTIVE) {
            if (e.name.equals(name)) {
                e.left = Math.max(e.left, seconds);
                return;
            }
        }
        ACTIVE.add(new Effect(name, seconds, color));
    }

    public static void clear() {
        ACTIVE.clear();
    }

    public static void update(float dt) {
        for (int i = ACTIVE.size() - 1; i >= 0; i--) {
            ACTIVE.get(i).left -= dt;
            if (ACTIVE.get(i).left <= 0f) {
                ACTIVE.remove(i);
            }
        }
    }

    /** Plates centred on cx, sitting on y (their bottom). */
    public static void render(Batch batch, BitmapFont font, float cx, float y) {
        if (ACTIVE.isEmpty()) {
            return;
        }
        float pw = 300f;
        float ph = font.getCapHeight() * 3.2f;
        float gap = 24f;
        float x = cx - (ACTIVE.size() * pw + (ACTIVE.size() - 1) * gap) / 2f;
        for (Effect e : ACTIVE) {
            float a = Math.min(1f, e.left * 2f);
            boolean ending = e.left < 3f && ((int) (e.left * 4f)) % 2 == 0;
            Theme.rect(batch, x - 2f, y - 2f, pw + 4f, ph + 4f, Theme.EDGE, 0.9f * a);
            Theme.rect(batch, x, y, pw, ph, Theme.BODY_LO, 0.92f * a);
            float ty = y + ph - font.getCapHeight() * 0.7f;
            Theme.text(batch, font, e.name, x + 18f, ty, ending ? Theme.TEXT : e.color, a);
            String s = String.valueOf((int) Math.ceil(e.left));
            Theme.textR(batch, font, s, x + pw - 18f, ty, Theme.TEXT, a);
            float bw = pw - 36f;
            Theme.rect(batch, x + 18f, y + 12f, bw, 8f, Theme.TRACK, a);
            Theme.rect(batch, x + 18f, y + 12f, bw * Math.max(0f, e.left / e.total), 8f, e.color, a);
            x += pw + gap;
        }
    }

    static float consoleTop() {
        return Rules.SAFE_Y;
    }
}
