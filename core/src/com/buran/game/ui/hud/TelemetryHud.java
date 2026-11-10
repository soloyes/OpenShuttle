package com.buran.game.ui.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.buran.game.conf.PaintConstants;
import com.buran.game.conf.Rules;
import com.buran.game.player.Buran;
import com.buran.game.player.Standing;
import com.buran.game.ui.groups.selector.Selector;
import com.buran.game.ui.groups.selector.SelectorGroup;
import com.buran.game.ui.menu.Theme;
import com.buran.game.utils.Assets;

/**
 * Flight HUD in the telemetry style: step and score, segmented gauges for shield, hull,
 * fuel and laser, lives, and (on gamepad/keyboard) the cargo bay with button hints.
 * Drawn in world/UI units (1920 high) inside the TV safe area.
 */
public class TelemetryHud {
    private static final Color SHIELD = Color.valueOf("5fd7ffff");
    private static final Color HULL = Color.valueOf("ff8a6aff");
    private static final Color FUEL = Color.valueOf("ffc94dff");
    private static final Color LASER = Color.valueOf("c58bffff");
    private static final Color ALERT = Color.valueOf("ff3b30ff");

    private final BitmapFont label;
    private final BitmapFont value;
    private final BitmapFont big;
    private final GlyphLayout layout = new GlyphLayout();
    private final TextureRegion lifeIcon;
    private final boolean touch;
    private float time;
    private final Color tmp = new Color();

    public TelemetryHud() {
        boolean portrait = Theme.portrait();
        label = Theme.font(portrait ? 32 : 44);
        value = Theme.font(portrait ? 44 : 54);
        big = Theme.font(portrait ? 72 : 96);
        lifeIcon = Assets.getInstance().getAtlas().findRegion(PaintConstants.BURAN_ICON);
        touch = Gdx.input.isPeripheralAvailable(Input.Peripheral.MultitouchScreen);
    }

    /** The touch carousel stays on phones; gamepad/keyboard get the cargo bay. */
    public boolean usesCargoBay() {
        return !touch;
    }

    /** Contextual visibility (tips 90-91): a gauge shows while it changes, is not full, or alerts. */
    private static final float LINGER = 3f;
    private final float[] lastValue = {-1f, -1f, -1f, -1f};
    private final float[] lastChange = {0f, 0f, 0f, 0f};
    private final float[] visible = {1f, 1f, 1f, 1f};

    private void track(int i, float value, float max, boolean alert, float dt) {
        if (Math.abs(value - lastValue[i]) > 0.05f) {
            lastValue[i] = value;
            lastChange[i] = time;
        }
        boolean show = alert || value < max - 0.5f || time - lastChange[i] < LINGER || Rules.GAME_OVER;
        visible[i] += ((show ? 1f : 0f) - visible[i]) * Math.min(1f, dt * (show ? 10f : 2.5f));
    }

    public void update(float dt) {
        time += dt;
        Standing st = Standing.getInstance();
        track(0, st.getLifeShield(), st.getMaxLifeShield(), false, dt);
        track(1, st.getHp(), st.getMaxHp(), st.isHpAlert(), dt);
        track(2, st.getFuel(), st.getMaxFuel(), st.isFuelAlertOutOfBounds() || st.isFuelAlertMagneticAnomaly(), dt);
        // Laser: shown while it has charge or just changed.
        float laser = st.getLaserChargeLevel();
        track(3, laser, laser > 0f ? Float.MAX_VALUE : 0f, false, dt);
    }

    public void render(SpriteBatch batch) {
        Standing st = Standing.getInstance();
        boolean portrait = Theme.portrait();
        float W = Rules.WORLD_WIDTH;
        float H = Rules.WORLD_HEIGHT;
        float sx = Rules.SAFE_X;
        float top = H - Rules.SAFE_Y;
        boolean blink = ((int) (time * 4f)) % 2 == 0;

        float colW = portrait ? (W - 2 * sx - 40f) / 2f : 640f;

        // ---- step and score (top left)
        int step = st.getPlayingLevel();
        String stepText = step >= 19 ? "COLLAPSE" : "STEP " + pad2(step) + " / 19";
        text(batch, label, stepText, sx, top, step >= 19 ? ALERT : Theme.DIM, 1f);
        float progress = step >= 19 ? 1f : stepProgress(st);
        float py = top - label.getLineHeight() - 6f;
        Theme.rect(batch, sx, py, colW, 5f, Theme.FAINT, 0.6f);
        Theme.rect(batch, sx, py, colW * progress, 5f, step >= 19 ? ALERT : Theme.LINE, 1f);
        Color scoreColor = st.isScoreAlertRadiationAnomaly() && blink ? ALERT : Theme.TEXT;
        text(batch, big, formatScore(st.getScoreView()), sx, py - 16f, scoreColor, 1f);

        // ---- lives (top centre, or top right on phones)
        int lives = Math.max(0, st.getLives());
        float icon = portrait ? 46f : 64f;
        float lx = portrait ? W - sx - lives * (icon + 8f) : W / 2f - (lives * (icon + 12f)) / 2f;
        for (int i = 0; i < lives; i++) {
            batch.setColor(1f, 1f, 1f, 0.9f);
            batch.draw(lifeIcon, lx + i * (icon + (portrait ? 8f : 12f)), top - icon, icon, icon);
        }
        batch.setColor(Color.WHITE);

        // ---- gauges
        float gaugeTop = portrait ? py - 16f - big.getLineHeight() - 10f : top;
        float leftX = sx;
        float rightX = W - sx - colW;
        if (portrait) {
            ga = visible[0];
            gauge(batch, "SHIELD", st.getLifeShield(), st.getMaxLifeShield(), SHIELD, leftX, gaugeTop, colW, false);
            ga = visible[1];
            gauge(batch, "HULL", st.getHp(), st.getMaxHp(), HULL, leftX, gaugeTop - gaugeHeight(), colW,
                    st.isHpAlert() && blink);
            ga = visible[2];
            gauge(batch, "FUEL", st.getFuel(), st.getMaxFuel(), FUEL, rightX, gaugeTop, colW,
                    (st.isFuelAlertOutOfBounds() || st.isFuelAlertMagneticAnomaly() || st.getFuel() < st.getMaxFuel() * 0.2f) && blink);
            ga = visible[3];
            gaugeLaser(batch, st, rightX, gaugeTop - gaugeHeight(), colW);
            ga = 1f;
        } else {
            float g2 = py - 16f - big.getLineHeight() - 20f;
            // Gauges stack in their column without gaps when some are hidden.
            float ly = g2;
            ga = visible[0];
            gauge(batch, "SHIELD", st.getLifeShield(), st.getMaxLifeShield(), SHIELD, leftX, ly, colW, false);
            ly -= gaugeHeight() * visible[0];
            ga = visible[1];
            gauge(batch, "HULL", st.getHp(), st.getMaxHp(), HULL, leftX, ly, colW, st.isHpAlert() && blink);
            float ry = top;
            ga = visible[2];
            gauge(batch, "FUEL", st.getFuel(), st.getMaxFuel(), FUEL, rightX, ry, colW,
                    (st.isFuelAlertOutOfBounds() || st.isFuelAlertMagneticAnomaly() || st.getFuel() < st.getMaxFuel() * 0.2f) && blink);
            ry -= gaugeHeight() * visible[2];
            ga = visible[3];
            gaugeLaser(batch, st, rightX, ry, colW);
            ga = 1f;
        }

        if (usesCargoBay()) {
            cargoBay(batch);
        }

        // Shelter status, centred above the bottom edge while the ship is inside one.
        com.buran.game.world.Shelters.Shelter sh = com.buran.game.world.Shelters.occupied();
        if (sh != null) {
            String t = "SHELTER  " + Math.round(sh.integrity() * 100f) + "%";
            layout.setText(value, t);
            float bw = Math.min(W * 0.3f, 640f);
            float bx = (W - bw) / 2f;
            float by = Rules.SAFE_Y + 40f;
            text(batch, value, t, (W - layout.width) / 2f, by + 70f, SHIELD, 1f);
            Theme.rect(batch, bx, by, bw, 8f, Theme.FAINT, 0.5f);
            Theme.rect(batch, bx, by, bw * sh.integrity(), 8f, SHIELD, sh.integrity() < 0.25f && blink ? 0.4f : 1f);
        }
    }

    private float gaugeHeight() {
        return label.getLineHeight() + 50f;
    }

    private float ga = 1f;

    private void gaugeLaser(SpriteBatch batch, Standing st, float x, float y, float w) {
        if (ga < 0.01f) {
            return;
        }
        if (!Buran.isLaser && st.getLaserChargeLevel() <= 0f) {
            text(batch, label, "LASER", x, y, Theme.FAINT, 1f);
            layout.setText(label, "OFFLINE");
            text(batch, label, "OFFLINE", x + w - layout.width, y, Theme.FAINT, 1f);
            segments(batch, x, y - label.getLineHeight() - 8f, w, 0f, Theme.FAINT);
            return;
        }
        gauge(batch, "LASER", st.getLaserChargeLevel(), st.getMaxLaserChargeLevel(), LASER, x, y, w, false);
    }

    private void gauge(SpriteBatch batch, String name, float v, float max, Color c, float x, float y, float w, boolean alert) {
        if (ga < 0.01f) {
            return;
        }
        float p = max <= 0 ? 0f : MathUtils.clamp(v / max, 0f, 1f);
        Color col = alert ? ALERT : c;
        text(batch, label, name, x, y, Theme.DIM, 1f);
        String val = Math.round(v) + " / " + Math.round(max);
        layout.setText(label, val);
        text(batch, label, val, x + w - layout.width, y, alert ? ALERT : Theme.TEXT, 1f);
        segments(batch, x, y - label.getLineHeight() - 8f, w, p, col);
    }

    private void segments(SpriteBatch batch, float x, float y, float w, float p, Color c) {
        int n = 24;
        float gap = 5f;
        float sw = (w - gap * (n - 1)) / n;
        float h = Theme.portrait() ? 12f : 16f;
        float filled = p * n;
        for (int i = 0; i < n; i++) {
            float f = MathUtils.clamp(filled - i, 0f, 1f);
            Theme.rect(batch, x + i * (sw + gap), y - h, sw, h, Theme.FAINT, 0.45f * ga);
            if (f > 0f) {
                Theme.rect(batch, x + i * (sw + gap), y - h, sw * f, h, c, ga);
            }
        }
    }

    private void cargoBay(SpriteBatch batch) {
        SelectorGroup sg = SelectorGroup.getInstance();
        Selector cur = sg.getCenter();
        float size = 190f;
        float x = Rules.SAFE_X;
        float y = Rules.SAFE_Y;
        Theme.frame(batch, x, y, size, size, 0.9f);
        if (cur == null) {
            text(batch, label, "CARGO", x, y + size + label.getLineHeight() + 10f, Theme.DIM, 1f);
            layout.setText(label, "EMPTY");
            text(batch, label, "EMPTY", x + (size - layout.width) / 2f, y + size / 2f + label.getCapHeight() / 2f, Theme.FAINT, 1f);
            return;
        }
        float a = cur.isActive() ? 1f : 0.45f;
        TextureRegion r = cur.getTextureRegion();
        if (r != null) {
            batch.setColor(1f, 1f, 1f, a);
            batch.draw(r, x + 25f, y + 25f, size - 50f, size - 50f);
            batch.setColor(Color.WHITE);
        }
        String count = "x" + cur.getCount();
        layout.setText(value, count);
        text(batch, value, count, x + size - layout.width - 14f, y + value.getLineHeight() - 6f, Theme.TEXT, 1f);
        String name = cur.getSticker().name();
        text(batch, label, name, x, y + size + label.getLineHeight() + 10f, Theme.TEXT, 1f);
        float hx = x + size + 30f;
        text(batch, label, cur.isActive() ? "A  USE" : "RECHARGING", hx, y + size - 20f, cur.isActive() ? Theme.OK : Theme.FAINT, 1f);
        if (sg.getSelectorCount() > 1) {
            text(batch, label, "LB / RB  SWITCH", hx, y + size - 20f - label.getLineHeight() - 10f, Theme.DIM, 1f);
        }
    }

    private static float stepProgress(Standing st) {
        if (st.getScore() <= 0 || st.getScoreToLevel() <= 0) {
            return 0f;
        }
        return MathUtils.clamp(1f - (float) (st.getScoreToLevel() - st.getScore()) / st.getScoreToLevel(), 0f, 1f);
    }

    private void text(SpriteBatch batch, BitmapFont font, String s, float x, float y, Color c, float a) {
        a *= ga;
        tmp.set(font.getColor());
        font.setColor(c.r, c.g, c.b, c.a * a);
        font.draw(batch, s, x, y);
        font.setColor(tmp);
    }

    private static String pad2(int v) {
        return v < 10 ? "0" + v : String.valueOf(v);
    }

    private static String formatScore(long score) {
        String s = String.valueOf(Math.abs(score));
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (i > 0 && (s.length() - i) % 3 == 0) {
                b.append(' ');
            }
            b.append(s.charAt(i));
        }
        return (score < 0 ? "-" : "") + b;
    }
}
