package com.buran.game.world;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.IdentityMap;
import com.buran.game.bot.anomaly.AbstractAnomaly;
import com.buran.game.conf.MSConstants;
import com.buran.game.conf.Rules;
import com.buran.game.effects.BoomBox;
import com.buran.game.player.Buran;
import com.buran.game.utils.ListOfListsJoiner;

/**
 * Makes anomaly intent readable.
 * <ul>
 *     <li>World pass: a dormant anomaly wears a faint slow ring; a hunting one a pulsing red ring
 *     with a chevron toward the ship. The moment it starts hunting it flashes and an alert sounds
 *     (tip 53: show what enemies think).</li>
 *     <li>Screen pass: hunting anomalies outside the view are announced by chevrons at the screen
 *     edge that grow and quicken as they close in; offline/online shelters out of view get a
 *     cyan marker (tips 69, 9).</li>
 * </ul>
 */
public final class ThreatOverlay {
    private static final Color HUNT = Color.valueOf("ff4a3dff");
    private static final Color CALM = Color.valueOf("8fa3bdff");
    private static final Color SHELTER = Color.valueOf("5fd7ffff");
    private static final float ALERT_TIME = 0.9f;

    private final ListOfListsJoiner<AbstractAnomaly> anomalies;
    private final ThreatTextures tx;
    private final IdentityMap<AbstractAnomaly, Boolean> wasHunting = new IdentityMap<AbstractAnomaly, Boolean>();
    private final IdentityMap<AbstractAnomaly, Float> alerts = new IdentityMap<AbstractAnomaly, Float>();
    private final BoomBox boomBox = new BoomBox();
    private float time;
    private float soundCooldown;

    public ThreatOverlay(ListOfListsJoiner<AbstractAnomaly> anomalies, ThreatTextures tx) {
        this.anomalies = anomalies;
        this.tx = tx;
        empUsed = false;
    }

    /** Same rule as the movement code: an awake, hungry anomaly within its chase range. */
    public static boolean hunting(AbstractAnomaly a) {
        if (!a.isActive() || !a.isBorn() || a.isTaken() || !a.isActivated() || !a.isHungry()) {
            return false;
        }
        if (Shelters.shipProtected() || Rules.GAME_OVER) {
            return false;
        }
        float range = 2400f + 200f * a.getLevel();
        return a.getPosition().dst2(Buran.position) < range * range;
    }

    public void update(float dt) {
        time += dt;
        soundCooldown -= dt;
        for (int i = 0; i < anomalies.size(); i++) {
            AbstractAnomaly a = anomalies.get(i);
            boolean h = hunting(a);
            Boolean was = wasHunting.get(a);
            if (h && (was == null || !was)) {
                alerts.put(a, ALERT_TIME);
                if (soundCooldown <= 0f) {
                    boomBox.playSound(MSConstants.UI_SWITCH);
                    soundCooldown = 0.6f;
                }
            }
            wasHunting.put(a, h);
            Float t = alerts.get(a);
            if (t != null) {
                t -= dt;
                if (t <= 0f) {
                    alerts.remove(a);
                } else {
                    alerts.put(a, t);
                }
            }
        }
    }

    private static final Color EMP = Color.valueOf("7fe8ffff");
    /** The pulse has been fired at least once in this game. */
    private static boolean empUsed;
    private float empHint;

    /** World pass, after anomalies are drawn. */
    public void renderWorld(SpriteBatch batch, com.badlogic.gdx.graphics.g2d.BitmapFont font) {
        renderEmpHint(batch, font);
        for (int i = 0; i < anomalies.size(); i++) {
            AbstractAnomaly a = anomalies.get(i);
            if (!a.isActive() || !a.isBorn() || a.isTaken()) {
                continue;
            }
            float x = a.getPosition().x;
            float y = a.getPosition().y;
            float r = Math.max(a.getWidth(), a.getHeight()) * 0.62f * (0.85f + 0.12f * a.getLevel());
            if (hunting(a)) {
                float pulse = 0.65f + 0.35f * MathUtils.sin(time * 9f + x * 0.01f);
                batch.setColor(HUNT.r, HUNT.g, HUNT.b, 0.75f * pulse);
                batch.draw(tx.ring, x - r, y - r, r * 2f, r * 2f);
                float ang = MathUtils.atan2(Buran.position.y - y, Buran.position.x - x);
                float cs = r * 0.45f;
                float cx = x + MathUtils.cos(ang) * (r + cs * 0.6f);
                float cy = y + MathUtils.sin(ang) * (r + cs * 0.6f);
                batch.setColor(HUNT.r, HUNT.g, HUNT.b, 0.9f * pulse);
                batch.draw(tx.chevron, cx - cs / 2f, cy - cs / 2f, cs / 2f, cs / 2f, cs, cs, 1f, 1f,
                        ang * MathUtils.radiansToDegrees, 0, 0, 64, 64, false, false);
            } else if (!a.isActivated() || !a.isHungry()) {
                // Asleep: dimmed under a slow dark veil, no ring. Lit = awake, dim = asleep.
                float pulse = 0.5f + 0.5f * MathUtils.sin(time * 1.2f + y * 0.01f);
                float vr = r * 1.15f;
                batch.setColor(0f, 0.01f, 0.03f, 0.38f + 0.12f * pulse);
                batch.draw(tx.dot, x - vr, y - vr, vr * 2f, vr * 2f);
            }
            Float t = alerts.get(a);
            if (t != null) {
                // Waking up: two sonar pings spread from it.
                float k = 1f - t / ALERT_TIME;
                for (int p = 0; p < 2; p++) {
                    float kk = MathUtils.clamp(k * 1.4f - p * 0.4f, 0f, 1f);
                    if (kk <= 0f || kk >= 1f) {
                        continue;
                    }
                    float rr = r * (1f + 1.6f * kk);
                    batch.setColor(HUNT.r, HUNT.g, HUNT.b, 0.8f * (1f - kk));
                    batch.draw(tx.ring, x - rr, y - rr, rr * 2f, rr * 2f);
                }
            }
        }
        batch.setColor(Color.WHITE);
    }

    /**
     * When an electric anomaly closes in, show the EMP reach around the ship and the button that
     * fires it: the counter is taught exactly when it is needed (tips 9, 28).
     */
    private void renderEmpHint(SpriteBatch batch, com.badlogic.gdx.graphics.g2d.BitmapFont font) {
        boolean near = false;
        float reach = Rules.PULSE_RANGE * 1.5f;
        for (int i = 0; i < anomalies.size(); i++) {
            AbstractAnomaly a = anomalies.get(i);
            if (a instanceof com.buran.game.bot.anomaly.ElectricalAnomaly && a.isActive() && a.isBorn() && !a.isTaken()
                    && a.getPosition().dst2(Buran.position) < reach * reach) {
                near = true;
                break;
            }
        }
        empHint = MathUtils.clamp(empHint + (near ? 1f : -1f) * 0.05f, 0f, 1f);
        if (empHint <= 0f || Rules.GAME_OVER) {
            return;
        }
        float charge = com.buran.game.input.Gamepad.get().pulseCharge();
        float r = Rules.PULSE_RANGE;
        float x = Buran.position.x;
        float y = Buran.position.y;
        float pulse = 0.75f + 0.25f * MathUtils.sin(time * 6f);
        batch.setColor(EMP.r, EMP.g, EMP.b, empHint * (charge >= 1f ? 0.35f * pulse : 0.12f));
        batch.draw(tx.ring, x - r, y - r, r * 2f, r * 2f);
        if (charge < 1f) {
            empUsed = true;
        }
        // Until the pulse is fired once, the button is shown big above the ship.
        if (font != null && !empUsed) {
            com.buran.game.ui.hud.TutorialPrompts.empRequest = empHint;
        }
        batch.setColor(Color.WHITE);
    }

    /**
     * Screen pass (UI camera, 1920-high world units): edge markers for threats and shelters that
     * are outside the view.
     */
    public void renderEdges(SpriteBatch batch, Camera worldCamera, com.badlogic.gdx.graphics.g2d.BitmapFont font) {
        float vw = Rules.VIEW_WIDTH;
        float vh = Rules.VIEW_HEIGHT;
        float cx = worldCamera.position.x;
        float cy = worldCamera.position.y;
        float scale = Rules.WORLD_HEIGHT / vh;
        // Anomalies off screen are the radar's job; only shelters get edge pins.
        for (Shelters.Shelter s : Shelters.all()) {
            float dx = s.position.x - cx;
            float dy = s.position.y - cy;
            if (EdgeMarker.inView(dx, dy, Shelters.RADIUS)) {
                continue;
            }
            EdgeMarker.draw(batch, tx, font, tx.dome, dx, dy, s.position.dst(Buran.position),
                    s.online() ? SHELTER : CALM, s.online() ? 0.85f : 0.4f, 0.7f + 0.3f * MathUtils.sin(time * 2f));
        }
        batch.setColor(Color.WHITE);
    }

    private void edgeMarker(SpriteBatch batch, float dx, float dy, float scale, float size, Color c, float alpha,
                            boolean chevron) {
        float[] p = EdgeMarker.project(dx, dy, 90f);
        float px = p[0];
        float py = p[1];
        float ang = p[2] * MathUtils.radiansToDegrees;
        batch.setColor(c.r, c.g, c.b, alpha);
        if (chevron) {
            batch.draw(tx.chevron, px - size / 2f, py - size / 2f, size / 2f, size / 2f, size, size, 1f, 1f, ang,
                    0, 0, 64, 64, false, false);
        } else {
            batch.draw(tx.ring, px - size / 2f, py - size / 2f, size, size);
            batch.draw(tx.dot, px - size * 0.2f, py - size * 0.2f, size * 0.4f, size * 0.4f);
        }
    }

    public void clear() {
        wasHunting.clear();
        alerts.clear();
    }
}
