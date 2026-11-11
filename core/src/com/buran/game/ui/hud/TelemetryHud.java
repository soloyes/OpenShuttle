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

    private static final Color EMP = Color.valueOf("5fd7ffff");

    /**
     * The EMP: its button, a recharge bar and a lamp. Dim when the shield cannot pay for a pulse.
     */
    private void empGauge(SpriteBatch batch, Standing st, float x, float cy, boolean blink) {
        float charge = com.buran.game.input.Gamepad.get().pulseCharge();
        boolean affordable = st.getLifeShield() >= st.getMaxLifeShield() * Rules.PULSE_SHIELD_COST;
        boolean ready = charge >= 1f && affordable;
        x += Theme.rich(batch, small, "{Y}", x, cy, HEAD, 1f);
        textL(batch, small, "EMP", x, cy + small.getCapHeight() / 2f, ready ? Theme.TEXT : HEAD);
        x += Theme.width(small, "EMP") + 22f;
        float bw = 150f;
        float bh = 18f;
        Theme.rect(batch, x, cy - bh / 2f, bw, bh, Theme.TRACK, 1f);
        Theme.rect(batch, x + 2f, cy - bh / 2f + 2f, (bw - 4f) * charge, bh - 4f, affordable ? EMP : HEAD, ready ? 0.95f : 0.45f);
        x += bw + 20f;
        Theme.lamp(batch, x, cy - 7f, EMP, ready, 1f);
    }

    private static final Color[] RAM_COLORS = {
            Color.valueOf("c8a46eff"), Color.valueOf("6cc24aff"), Color.valueOf("f2c230ff"), Color.valueOf("e5533dff"),
    };

    /**
     * The ram shield: its button, four charge segments in the level colours (tan, green,
     * yellow, red) and a lamp that says whether it is on.
     */
    private void ramGauge(SpriteBatch batch, Standing st, float x, float cy, boolean blink) {
        float ram = st.getRamPower();
        boolean on = Buran.ramOn;
        float age = Buran.ramSwitchAge;
        boolean denied = Buran.ramDenied && age < 0.6f;
        x += Theme.rich(batch, small, "{A}", x, cy, HEAD, 1f);
        Color lc = on ? Theme.TEXT : HEAD;
        textL(batch, small, "RAM", x, cy + small.getCapHeight() / 2f, denied && blink ? ALERT : lc);
        x += Theme.width(small, "RAM") + 22f;
        float sw = 44f;
        float sh = 18f;
        for (int i = 0; i < 4; i++) {
            float fill = com.badlogic.gdx.math.MathUtils.clamp(ram - i, 0f, 1f);
            Color c = RAM_COLORS[i];
            Theme.rect(batch, x, cy - sh / 2f, sw, sh, Theme.TRACK, 1f);
            if (fill > 0f) {
                float a = on ? 0.95f : 0.45f;
                if (on && age < 0.4f) {
                    a = 1f;
                }
                Theme.rect(batch, x + 2f, cy - sh / 2f + 2f, (sw - 4f) * fill, sh - 4f, c, a);
            }
            x += sw + 6f;
        }
        x += 14f;
        Color lamp = on ? RAM_COLORS[Math.max(0, Math.min(3, (int) ram - 1))] : HEAD;
        Theme.lamp(batch, x, cy - 7f, lamp, on, 1f);
        textL(batch, small, on ? "ON" : "OFF", x + 30f, cy + small.getCapHeight() / 2f, on ? Theme.TEXT : HEAD);
    }

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
        Theme.panel(batch, x0, y0, w, h, 1f);
        float sec = w / 3f;
        Theme.vline(batch, x0 + sec, y0 + 24f, h - 48f, 1f);
        Theme.vline(batch, x0 + 2f * sec, y0 + 24f, h - 48f, 1f);
        float pad = 50f;
        float headY = y0 + h - 30f;
        float lw = sec - 2f * pad;

        // ---- life support
        float lx = x0 + pad;
        textL(batch, small, "LIFE SUPPORT", lx, headY, HEAD);
        Color scoreColor = st.isScoreAlertRadiationAnomaly() && blink ? ALERT : HEAD;
        textR(batch, small, "SCORE  " + formatScore(st.getScoreView()), lx + lw, headY, scoreColor);
        ramGauge(batch, st, lx + Theme.width(small, "LIFE SUPPORT") + 70f, headY - small.getCapHeight() / 2f, blink);
        bar(batch, "SHIELD", st.getLifeShield(), st.getMaxLifeShield(), SHIELD, lx, headY - 58f, lw, false, false);
        bar(batch, "HULL", st.getHp(), st.getMaxHp(), HULL, lx, headY - 158f, lw, st.isHpAlert() && blink, false);

        // ---- propulsion (mirror)
        float rx = x0 + 2f * sec + pad;
        Theme.hint(batch, small, com.buran.game.ui.menu.Hints.button("B", "ESC"), "PAUSE", rx, headY - small.getCapHeight() / 2f, HEAD, 1f);
        empGauge(batch, st, rx + Theme.hintWidth(small, com.buran.game.ui.menu.Hints.button("B", "ESC"), "PAUSE") + 70f,
                headY - small.getCapHeight() / 2f, blink);
        textR(batch, small, "PROPULSION", rx + lw, headY, HEAD);
        boolean fuelAlert = (st.isFuelAlertOutOfBounds() || st.isFuelAlertMagneticAnomaly() || st.getFuel() < st.getMaxFuel() * 0.2f) && blink;
        bar(batch, "FUEL", st.getFuel(), st.getMaxFuel(), FUEL, rx, headY - 58f, lw, fuelAlert, false);
        boolean offline = !Buran.isLaser;
        bar(batch, "LASER", st.getLaserChargeLevel(), st.getMaxLaserChargeLevel(), LASER, rx, headY - 158f, lw, false, offline);

        // ---- cargo drum, with the task progress as a row of pips along its top
        float cx = x0 + 1.5f * sec;
        float cl = x0 + sec + pad;
        float cw = sec - 2f * pad;
        com.buran.game.campaign.SectorRun run = com.buran.game.campaign.Campaign.run();
        int step = st.getPlayingLevel();
        String right;
        int pips;
        float done;
        Color pipColor = CARGO;
        if (run != null) {
            right = "SECTOR " + run.sector().code() + "  -  " + run.progress() + " / " + run.sector().target;
            pips = run.sector().target;
            done = run.progress();
        } else {
            right = step >= 19 ? "COLLAPSE" : "STEP " + pad2(step) + " / 19";
            pips = 19;
            done = step >= 19 ? 19f : step + stepProgress(st);
            if (step >= 19) {
                pipColor = ALERT;
            }
        }
        textL(batch, small, "CARGO", cl, headY, HEAD);
        textR(batch, small, right, cl + cw, headY, step >= 19 && run == null ? ALERT : HEAD);
        pipRow(batch, cl, headY - 44f, cw, pips, done, pipColor);

        SelectorGroup sg = SelectorGroup.getInstance();
        Selector cur = sg.getCenter();
        float big = 140f;
        float side = 96f;
        float by = y0 + 58f;
        float gap = 60f;
        float sideY = by + (big - side) / 2f;
        float textY = y0 + 46f;
        socket(batch, sg.getPrevious(), cx - big / 2f - gap - side, sideY, side, false, 0.45f);
        socket(batch, sg.getNext(), cx + big / 2f + gap, sideY, side, false, 0.45f);
        float keyCy = textY - small.getCapHeight() / 2f;
        if (sg.getPrevious() != null) {
            String k = com.buran.game.ui.menu.Hints.button("LB", "Q");
            Theme.key(batch, small, k, cx - big / 2f - gap - side / 2f - Theme.keyWidth(small, k) / 2f, keyCy, 1f);
        }
        if (sg.getNext() != null) {
            String k = com.buran.game.ui.menu.Hints.button("RB", "E");
            Theme.key(batch, small, k, cx + big / 2f + gap + side / 2f - Theme.keyWidth(small, k) / 2f, keyCy, 1f);
        }
        socket(batch, cur, cx - big / 2f, by, big, true, cur == null || cur.isActive() ? 1f : 0.4f);
        if (cur == null) {
            textC(batch, small, "BAY EMPTY", cx, textY, HEAD);
        } else {
            textR(batch, small, "x" + cur.getCount(), cx + big / 2f - 10f, by + small.getLineHeight() - 2f, Theme.TEXT);
            if (cur.isActive()) {
                String k = com.buran.game.ui.menu.Hints.button("X", "X");
                String n = cur.getSticker().name();
                Theme.hint(batch, small, k, n, cx - Theme.hintWidth(small, k, n) / 2f, keyCy, CARGO, 1f);
            } else {
                textC(batch, small, "RECHARGING", cx, textY, HEAD);
            }
        }
    }

    /** A row of task pips: lit when done, the current one filling. */
    private void pipRow(SpriteBatch batch, float x, float y, float w, int n, float done, Color c) {
        if (n <= 0) {
            return;
        }
        float gap = n > 12 ? 6f : 10f;
        float pw = (w - gap * (n - 1)) / n;
        float ph = 14f;
        for (int i = 0; i < n; i++) {
            float px = x + i * (pw + gap);
            Theme.rect(batch, px, y, pw, ph, EDGE, 1f);
            Theme.rect(batch, px + 2f, y + 2f, pw - 4f, ph - 4f, TRACK, 1f);
            float f = MathUtils.clamp(done - i, 0f, 1f);
            if (f > 0f) {
                Theme.rect(batch, px, y, pw * f, ph, c, 0.95f);
            }
        }
    }

    /** A recessed cargo socket; the current one is lit amber. */
    private void socket(SpriteBatch batch, Selector s, float x, float y, float size, boolean current, float a) {
        Color frame = current && s != null ? CARGO : EDGE;
        float t = current && s != null ? 3f : 2f;
        if (current && s != null) {
            Theme.rect(batch, x - 8f, y - 8f, size + 16f, size + 16f, CARGO, 0.08f);
        }
        Theme.rect(batch, x, y, size, size, TRACK, 1f);
        Theme.rect(batch, x + t, y + size - t - 6f, size - 2f * t, 6f, Color.BLACK, 0.35f);
        Theme.rect(batch, x, y, size, t, frame, 1f);
        Theme.rect(batch, x, y + size - t, size, t, frame, 1f);
        Theme.rect(batch, x, y, t, size, frame, 1f);
        Theme.rect(batch, x + size - t, y, t, size, frame, 1f);
        if (s == null) {
            return;
        }
        TextureRegion r = s.getTextureRegion();
        if (r != null) {
            float in = size * 0.16f;
            batch.setColor(1f, 1f, 1f, a);
            batch.draw(r, x + in, y + in, size - 2f * in, size - 2f * in);
            batch.setColor(Color.WHITE);
        }
    }

    /** Label and value on one line, a segmented gauge under it (dark when offline). */
    /** Per gauge: last value, flash (+1 gain, -1 loss, fading) and the change shown. */
    private final java.util.HashMap<String, float[]> gaugeState = new java.util.HashMap<String, float[]>();

    private void bar(SpriteBatch batch, String name, float v, float max, Color c, float x, float y, float w,
                     boolean alert, boolean offline) {
        float p = max <= 0 ? 0f : MathUtils.clamp(v / max, 0f, 1f);
        float[] g = gaugeState.get(name);
        if (g == null) {
            g = new float[]{v, 0f, 0f};
            gaugeState.put(name, g);
        }
        float dt = Math.min(0.05f, Gdx.graphics.getDeltaTime());
        float change = v - g[0];
        // Fuel and laser charge drift every frame; only real jumps flash.
        float step = "FUEL".equals(name) || "LASER".equals(name) ? Math.max(2f, max * 0.06f) : 0.5f;
        if (Math.abs(change) >= step) {
            g[1] = change < 0 ? -1f : 1f;
            g[2] = (Math.signum(g[2]) == Math.signum(change) && Math.abs(g[1]) > 0.2f ? g[2] : 0f) + change;
            g[0] = v;
        } else if (Math.abs(g[1]) < 0.01f) {
            g[0] = v;
        }
        g[1] = Math.signum(g[1]) * Math.max(0f, Math.abs(g[1]) - dt / 1.4f);
        if (Math.abs(g[1]) < 0.01f) {
            g[0] = v;
            g[2] = 0f;
        }
        float f = Math.abs(g[1]);
        boolean loss = g[1] < 0f;
        Color flash = loss ? ALERT : Theme.OK;
        boolean lit = f > 0f && ((int) (f * 14f)) % 2 == 0;
        Color nameC = offline ? HEAD : (alert || (lit && loss)) ? ALERT : c;
        textL(batch, label, name, x, y, nameC);
        String val = offline ? "OFFLINE" : Math.round(v) + " / " + Math.round(max);
        textR(batch, label, val, x + w, y, offline ? HEAD : alert ? ALERT : Theme.TEXT);
        if (f > 0f && Math.round(g[2]) != 0) {
            layout.setText(label, val);
            String d = (g[2] > 0 ? "+" : "") + Math.round(g[2]);
            text(batch, label, d, x + w - layout.width - 30f - widthOf(label, d), y, flash, Math.min(1f, f * 2f));
        }
        float sy = y - label.getLineHeight() - 8f;
        ledStrip(batch, x, sy, w, offline ? 0f : p, alert ? ALERT : c);
        if (f > 0f) {
            Theme.rect(batch, x - 6f, sy - 40f, w + 12f, 46f, flash, 0.35f * f);
        }
    }

    private float widthOf(BitmapFont f, String s) {
        layout.setText(f, s);
        return layout.width;
    }

    /** An LED strip in a recessed track: lit segments in colour, the rest dark. */
    private void ledStrip(SpriteBatch batch, float x, float top, float w, float p, Color c) {
        int n = 30;
        float h = 34f;
        float y = top - h;
        Theme.rect(batch, x, y, w, h, EDGE, 1f);
        Theme.rect(batch, x + 2f, y + 2f, w - 4f, h - 4f, TRACK, 1f);
        float in = 6f;
        float gap = 5f;
        float sw = (float) Math.floor((w - 2f * in - gap * (n - 1)) / n);
        float x1 = Math.round(x + (w - (sw * n + gap * (n - 1))) / 2f);
        float lit = p * n;
        for (int i = 0; i < n; i++) {
            float sx = x1 + i * (sw + gap);
            float f = MathUtils.clamp(lit - i, 0f, 1f);
            if (f > 0.02f) {
                Theme.rect(batch, sx, y + in, sw, h - 2f * in, c, 0.35f + 0.65f * f);
                Theme.rect(batch, sx, y + h - in - 4f, sw, 4f, Color.WHITE, 0.22f * f);
            } else {
                Theme.rect(batch, sx, y + in, sw, h - 2f * in, EDGE, 0.55f);
            }
        }
    }

    private void track(SpriteBatch batch, float x, float y, float w, float p, Color c) {
        ledStrip(batch, x, y, w, p, c);
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
            ActiveEffects.render(batch, small, Rules.WORLD_WIDTH / 2f, Rules.SAFE_Y + CONSOLE_H + 24f);
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
            String hint = "{B} PAUSE";
            Theme.rich(batch, label, hint, W - Rules.SAFE_X - Theme.richWidth(label, hint), Rules.SAFE_Y + label.getCapHeight() / 2f, Theme.FAINT, 1f);
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
        float cy0 = y + size - 20f - label.getCapHeight() / 2f;
        if (cur.isActive()) {
            Theme.rich(batch, label, "{X} USE", hx, cy0, Theme.OK, 1f);
        } else {
            text(batch, label, "RECHARGING", hx, y + size - 20f, Theme.FAINT, 1f);
        }
        if (sg.getSelectorCount() > 1) {
            Theme.rich(batch, label, "{LB} {RB} SWITCH", hx, cy0 - label.getLineHeight() - 30f, Theme.DIM, 1f);
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
