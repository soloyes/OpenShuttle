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
        small = Theme.font(32);
        Rules.HUD_BOTTOM = touch ? 0f : Rules.SAFE_Y + CONSOLE_H + 24f;
    }

    // ------------------------------------------------------------------ console (TV / PC)

    /** Height of the instrument console, UI units. */
    private static final float CONSOLE_H = 300f;
    private static final Color BODY = new Color(0.075f, 0.098f, 0.14f, 0.96f);
    private static final Color BODY_LO = new Color(0.04f, 0.055f, 0.08f, 0.96f);
    private static final Color EDGE = Color.valueOf("2a3647ff");
    private static final Color TRACK = Color.valueOf("0a0f16ff");
    private static final Color HEAD = Color.valueOf("6b7c93ff");
    private static final Color CARGO = Color.valueOf("ffb547ff");
    private final BitmapFont small;

    /**
     * One physical instrument panel along the bottom: life support | cargo drum | propulsion.
     * The step progress runs along its top edge. Always present, like a real console;
     * only the values and alerts move.
     */
    private void console(SpriteBatch batch, Standing st, boolean blink) {
        float W = Rules.WORLD_WIDTH;
        float x0 = Rules.SAFE_X;
        float y0 = Rules.SAFE_Y;
        float w = W - 2f * Rules.SAFE_X;
        float h = CONSOLE_H;
        // Body: darker lower half, a highlight line on top, a hairline frame.
        int bands = 12;
        for (int i = 0; i < bands; i++) {
            float t = i / (float) (bands - 1);
            tmp.set(BODY_LO).lerp(BODY, t);
            Theme.rect(batch, x0, y0 + h * i / bands, w, h / bands + 1f, tmp, 1f);
        }
        Theme.rect(batch, x0, y0 + h - 2f, w, 2f, EDGE, 1f);
        Theme.rect(batch, x0, y0, w, 2f, EDGE, 1f);
        Theme.rect(batch, x0, y0, 2f, h, EDGE, 1f);
        Theme.rect(batch, x0 + w - 2f, y0, 2f, h, EDGE, 1f);
        // Step progress along the top edge.
        int step = st.getPlayingLevel();
        float progress = step >= 19 ? 1f : stepProgress(st);
        Theme.rect(batch, x0, y0 + h, w * progress, 4f, step >= 19 ? ALERT : CARGO, 0.9f);
        float sec = w / 3f;
        Theme.rect(batch, x0 + sec, y0 + 18f, 2f, h - 36f, EDGE, 1f);
        Theme.rect(batch, x0 + 2f * sec, y0 + 18f, 2f, h - 36f, EDGE, 1f);
        float pad = 44f;
        float headY = y0 + h - 30f;

        // ---- life support
        float lx = x0 + pad;
        float lw = sec - 2f * pad;
        textL(batch, small, "LIFE SUPPORT", lx, headY, HEAD);
        Color scoreColor = st.isScoreAlertRadiationAnomaly() && blink ? ALERT : HEAD;
        textR(batch, small, "SCORE  " + formatScore(st.getScoreView()), lx + lw, headY, scoreColor);
        bar(batch, "SHIELD", st.getLifeShield(), st.getMaxLifeShield(), SHIELD, lx, headY - 62f, lw, false);
        bar(batch, "HULL", st.getHp(), st.getMaxHp(), HULL, lx, headY - 160f, lw, st.isHpAlert() && blink);

        // ---- propulsion (mirror)
        float rx = x0 + 2f * sec + pad;
        String pause = com.buran.game.input.Gamepad.get().hasController() ? "B  -  PAUSE" : "ESC  -  PAUSE";
        textL(batch, small, pause, rx, headY, HEAD);
        textR(batch, small, "PROPULSION", rx + lw, headY, HEAD);
        boolean fuelAlert = (st.isFuelAlertOutOfBounds() || st.isFuelAlertMagneticAnomaly() || st.getFuel() < st.getMaxFuel() * 0.2f) && blink;
        bar(batch, "FUEL", st.getFuel(), st.getMaxFuel(), FUEL, rx, headY - 62f, lw, fuelAlert);
        if (!Buran.isLaser && st.getLaserChargeLevel() <= 0f) {
            textL(batch, label, "LASER", rx, headY - 160f, LASER);
            textR(batch, label, "OFFLINE", rx + lw, headY - 160f, HEAD);
            track(batch, rx, headY - 160f - label.getLineHeight() - 6f, lw, 0f, LASER);
        } else {
            bar(batch, "LASER", st.getLaserChargeLevel(), st.getMaxLaserChargeLevel(), LASER, rx, headY - 160f, lw, false);
        }

        // ---- cargo drum
        float cx = x0 + 1.5f * sec;
        String stepText = step >= 19 ? "COLLAPSE" : "STEP " + pad2(step) + " / 19";
        layout.setText(small, "CARGO");
        textL(batch, small, "CARGO", cx - sec / 2f + pad, headY, HEAD);
        textR(batch, small, stepText, cx + sec / 2f - pad, headY, step >= 19 ? ALERT : HEAD);
        SelectorGroup sg = SelectorGroup.getInstance();
        Selector cur = sg.getCenter();
        float big = 176f;
        float side = 112f;
        float by = y0 + 36f;
        float gap = 70f;
        slot(batch, sg.getPrevious(), cx - big / 2f - gap - side, by + (big - side) / 2f + 14f, side, 0.45f);
        slot(batch, sg.getNext(), cx + big / 2f + gap, by + (big - side) / 2f + 14f, side, 0.45f);
        if (sg.getPrevious() != null) {
            textC(batch, small, "< LB", cx - big / 2f - gap - side / 2f, by + 22f, HEAD);
        }
        if (sg.getNext() != null) {
            textC(batch, small, "RB >", cx + big / 2f + gap + side / 2f, by + 22f, HEAD);
        }
        // Current: a lit frame.
        float bx = cx - big / 2f;
        float byy = by + 14f;
        Theme.rect(batch, bx, byy, big, big, TRACK, 1f);
        Color frame = cur == null ? EDGE : CARGO;
        Theme.rect(batch, bx, byy, big, 3f, frame, 1f);
        Theme.rect(batch, bx, byy + big - 3f, big, 3f, frame, 1f);
        Theme.rect(batch, bx, byy, 3f, big, frame, 1f);
        Theme.rect(batch, bx + big - 3f, byy, 3f, big, frame, 1f);
        if (cur == null) {
            textC(batch, small, "EMPTY", cx, byy + big / 2f + small.getCapHeight() / 2f, HEAD);
        } else {
            float a = cur.isActive() ? 1f : 0.4f;
            TextureRegion r = cur.getTextureRegion();
            if (r != null) {
                batch.setColor(1f, 1f, 1f, a);
                batch.draw(r, bx + 26f, byy + 26f, big - 52f, big - 52f);
                batch.setColor(Color.WHITE);
            }
            textR(batch, small, "x" + cur.getCount(), bx + big - 12f, byy + small.getLineHeight() - 4f, Theme.TEXT);
            textC(batch, small, cur.isActive() ? "A  -  " + cur.getSticker().name() : "RECHARGING",
                    cx, byy + big + small.getLineHeight() - 2f, cur.isActive() ? CARGO : HEAD);
        }
    }

    private void slot(SpriteBatch batch, Selector s, float x, float y, float size, float a) {
        if (s == null) {
            return;
        }
        Theme.rect(batch, x, y, size, size, TRACK, 1f);
        Theme.rect(batch, x, y, size, 2f, EDGE, 1f);
        Theme.rect(batch, x, y + size - 2f, size, 2f, EDGE, 1f);
        Theme.rect(batch, x, y, 2f, size, EDGE, 1f);
        Theme.rect(batch, x + size - 2f, y, 2f, size, EDGE, 1f);
        TextureRegion r = s.getTextureRegion();
        if (r != null) {
            batch.setColor(1f, 1f, 1f, a);
            batch.draw(r, x + 16f, y + 16f, size - 32f, size - 32f);
            batch.setColor(Color.WHITE);
        }
    }

    /** Label and value on one line, a solid bar in a dark track under it. */
    private void bar(SpriteBatch batch, String name, float v, float max, Color c, float x, float y, float w, boolean alert) {
        float p = max <= 0 ? 0f : MathUtils.clamp(v / max, 0f, 1f);
        textL(batch, label, name, x, y, alert ? ALERT : c);
        textR(batch, label, Math.round(v) + " / " + Math.round(max), x + w, y, alert ? ALERT : Theme.TEXT);
        track(batch, x, y - label.getLineHeight() - 6f, w, p, alert ? ALERT : c);
    }

    private void track(SpriteBatch batch, float x, float y, float w, float p, Color c) {
        float h = 22f;
        Theme.rect(batch, x, y - h, w, h, TRACK, 1f);
        Theme.rect(batch, x, y - h, w, 2f, EDGE, 1f);
        Theme.rect(batch, x, y - 2f, w, 2f, EDGE, 1f);
        if (p > 0f) {
            Theme.rect(batch, x + 3f, y - h + 3f, (w - 6f) * p, h - 6f, c, 0.95f);
            // Light along the top of the fill, a glassy instrument look.
            Theme.rect(batch, x + 3f, y - 7f, (w - 6f) * p, 3f, Color.WHITE, 0.25f);
        }
    }

    private void textL(SpriteBatch batch, BitmapFont f, String s, float x, float y, Color c) {
        text(batch, f, s, x, y, c, 1f);
    }

    private void textR(SpriteBatch batch, BitmapFont f, String s, float x, float y, Color c) {
        layout.setText(f, s);
        text(batch, f, s, x - layout.width, y, c, 1f);
    }

    private void textC(SpriteBatch batch, BitmapFont f, String s, float x, float y, Color c) {
        layout.setText(f, s);
        text(batch, f, s, x - layout.width / 2f, y, c, 1f);
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
        if (!touch) {
            ga = 1f;
            boolean blinkC = ((int) (time * 4f)) % 2 == 0;
            console(batch, st, blinkC);
            lives(batch, st);
            shelterStatus(batch, blinkC);
            return;
        }
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
            // Where the way out is: bottom right, quiet.
            String hint = com.buran.game.input.Gamepad.get().hasController() ? "B  -  PAUSE" : "ESC  -  PAUSE";
            layout.setText(label, hint);
            text(batch, label, hint, W - Rules.SAFE_X - layout.width, Rules.SAFE_Y + label.getCapHeight(), Theme.FAINT, 1f);
        }

        shelterStatus(batch, blink);
    }

    private void lives(SpriteBatch batch, Standing st) {
        float W = Rules.WORLD_WIDTH;
        float top = Rules.WORLD_HEIGHT - Rules.SAFE_Y;
        int lives = Math.max(0, st.getLives());
        float icon = 64f;
        float lx = W / 2f - (lives * (icon + 12f)) / 2f;
        for (int i = 0; i < lives; i++) {
            batch.setColor(1f, 1f, 1f, 0.9f);
            batch.draw(lifeIcon, lx + i * (icon + 12f), top - icon, icon, icon);
        }
        batch.setColor(Color.WHITE);
    }

    /** Shelter status, centred just above the console (or the bottom edge) while inside one. */
    private void shelterStatus(SpriteBatch batch, boolean blink) {
        float W = Rules.WORLD_WIDTH;
        com.buran.game.world.Shelters.Shelter sh = com.buran.game.world.Shelters.occupied();
        if (sh != null) {
            String t = "SHELTER  " + Math.round(sh.integrity() * 100f) + "%";
            layout.setText(value, t);
            float bw = Math.min(W * 0.3f, 640f);
            float bx = (W - bw) / 2f;
            float by = Math.max(Rules.SAFE_Y, Rules.HUD_BOTTOM) + 40f;
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
