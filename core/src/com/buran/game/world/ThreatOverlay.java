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

    /** World pass, after anomalies are drawn. */
    public void renderWorld(SpriteBatch batch) {
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
                float pulse = 0.5f + 0.5f * MathUtils.sin(time * 1.5f + y * 0.01f);
                batch.setColor(CALM.r, CALM.g, CALM.b, 0.14f + 0.08f * pulse);
                batch.draw(tx.ring, x - r, y - r, r * 2f, r * 2f);
            }
            Float t = alerts.get(a);
            if (t != null) {
                float k = 1f - t / ALERT_TIME;
                float rr = r * (1f + 1.2f * k);
                batch.setColor(HUNT.r, HUNT.g, HUNT.b, 1f - k);
                batch.draw(tx.ring, x - rr, y - rr, rr * 2f, rr * 2f);
                // "!" made of a bar and a dot above the anomaly.
                float h = r * 0.55f;
                float w = r * 0.12f;
                float bx = x - w / 2f;
                float by = y + r + 20f;
                batch.setColor(HUNT.r, HUNT.g, HUNT.b, 1f - k * 0.6f);
                batch.draw(tx.dot, bx - w * 0.3f, by, w * 1.6f, w * 1.6f);
                batch.draw(tx.dot, bx - w * 0.3f, by + w * 2.2f, w * 1.6f, h);
            }
        }
        batch.setColor(Color.WHITE);
    }

    /**
     * Screen pass (UI camera, 1920-high world units): edge markers for threats and shelters that
     * are outside the view.
     */
    public void renderEdges(SpriteBatch batch, Camera worldCamera) {
        float vw = Rules.VIEW_WIDTH;
        float vh = Rules.VIEW_HEIGHT;
        float cx = worldCamera.position.x;
        float cy = worldCamera.position.y;
        float scale = Rules.WORLD_HEIGHT / vh;
        for (int i = 0; i < anomalies.size(); i++) {
            AbstractAnomaly a = anomalies.get(i);
            if (!hunting(a)) {
                continue;
            }
            float dx = a.getPosition().x - cx;
            float dy = a.getPosition().y - cy;
            if (Math.abs(dx) < vw / 2f && Math.abs(dy) < vh / 2f) {
                continue;
            }
            float dist = a.getPosition().dst(Buran.position);
            float near = 1f - MathUtils.clamp((dist - vh * 0.5f) / 2400f, 0f, 1f);
            float pulse = 0.6f + 0.4f * MathUtils.sin(time * (4f + 8f * near));
            edgeMarker(batch, dx, dy, scale, 46f + 40f * near, HUNT, (0.35f + 0.6f * near) * pulse, true);
        }
        for (Shelters.Shelter s : Shelters.all()) {
            float dx = s.position.x - cx;
            float dy = s.position.y - cy;
            if (Math.abs(dx) < vw / 2f + Shelters.RADIUS && Math.abs(dy) < vh / 2f + Shelters.RADIUS) {
                continue;
            }
            edgeMarker(batch, dx, dy, scale, 40f, s.online() ? SHELTER : CALM, s.online() ? 0.7f : 0.3f, false);
        }
        batch.setColor(Color.WHITE);
    }

    private void edgeMarker(SpriteBatch batch, float dx, float dy, float scale, float size, Color c, float alpha,
                            boolean chevron) {
        float W = Rules.WORLD_WIDTH;
        float H = Rules.WORLD_HEIGHT;
        float mx = Rules.SAFE_X + 90f;
        float my = Rules.SAFE_Y + 90f;
        float hx = W / 2f - mx;
        float hy = H / 2f - my;
        float sx = dx * scale;
        float sy = dy * scale;
        float k = Math.min(hx / Math.max(Math.abs(sx), 0.001f), hy / Math.max(Math.abs(sy), 0.001f));
        float px = W / 2f + sx * k;
        float py = H / 2f + sy * k;
        float ang = MathUtils.atan2(sy, sx) * MathUtils.radiansToDegrees;
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
