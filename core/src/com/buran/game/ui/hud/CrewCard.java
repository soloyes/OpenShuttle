package com.buran.game.ui.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.buran.game.conf.Rules;
import com.buran.game.labelsticker.Sticker;
import com.buran.game.ui.menu.Theme;

import java.util.HashMap;
import java.util.Map;

/**
 * When someone comes aboard, their portrait slides in at the left above the console with who
 * they are and what they brought (tips 25, 96): a face, not just a number.
 */
public final class CrewCard {
    private static final float IN = 0.35f;
    private static final float HOLD = 3.2f;
    private static final float OUT = 0.45f;
    private static final Map<Sticker, Texture> PORTRAITS = new HashMap<Sticker, Texture>();

    private static Sticker who;
    private static String role;
    private static String gift;
    private static float age = -1f;

    private CrewCard() {
    }

    /** gift: "HULL REPAIRED" etc. */
    public static void show(Sticker crew, String roleName, String giftText) {
        who = crew;
        role = roleName;
        gift = giftText;
        age = 0f;
    }

    private static Texture portrait(Sticker s) {
        if (!PORTRAITS.containsKey(s)) {
            Texture t = null;
            FileHandle f = Gdx.files.internal("ui/crew_" + s.name().toLowerCase() + ".jpg");
            if (f.exists()) {
                t = new Texture(f);
                t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            }
            PORTRAITS.put(s, t);
        }
        return PORTRAITS.get(s);
    }

    /** UI pass (1920-high units). */
    public static void render(Batch batch, BitmapFont big, BitmapFont small) {
        if (age < 0f || who == null) {
            return;
        }
        age += Math.min(0.05f, Gdx.graphics.getDeltaTime());
        float total = IN + HOLD + OUT;
        if (age > total || Rules.GAME_OVER) {
            age = -1f;
            return;
        }
        float k;
        if (age < IN) {
            k = Interpolation.pow2Out.apply(age / IN);
        } else if (age > IN + HOLD) {
            k = 1f - Interpolation.pow2In.apply((age - IN - HOLD) / OUT);
        } else {
            k = 1f;
        }
        Texture t = portrait(who);
        float size = 300f;
        float pad = 26f;
        float textW = Math.max(Theme.width(big, role), Theme.width(small, gift)) + 60f;
        float w = (t != null ? size + pad : 0f) + textW + pad;
        float h = size + 2f * pad;
        float x = Rules.SAFE_X + 30f - (1f - k) * (w + 80f);
        float y = Math.max(Rules.SAFE_Y, Rules.HUD_BOTTOM) + 30f;
        Theme.panel(batch, x, y, w, h, k);
        float px = x + pad;
        if (t != null) {
            batch.setColor(1f, 1f, 1f, k);
            batch.draw(t, px, y + pad, size, size);
            batch.setColor(Color.WHITE);
            // A brief warm flash on arrival.
            float flash = MathUtils.clamp(1f - age / 0.6f, 0f, 1f);
            Theme.rect(batch, px, y + pad, size, size, Theme.AMBER, 0.35f * flash * k);
            Theme.rect(batch, px, y + pad, size, 3f, Theme.AMBER, k);
            px += size + pad;
        }
        float ty = y + h - pad - 30f;
        Theme.text(batch, small, "CREW ABOARD", px + 10f, ty, Theme.HEAD, k);
        Theme.text(batch, big, role, px + 10f, ty - 54f, Theme.TEXT, k);
        Theme.text(batch, small, gift, px + 10f, ty - 54f - big.getLineHeight() - 6f, Theme.OK, k);
    }
}
