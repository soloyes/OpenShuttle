package com.buran.game.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.buran.game.bot.blackhole.Blackhole;
import com.buran.game.conf.Buffs;
import com.buran.game.conf.Rules;
import com.buran.game.player.Buran;
import com.buran.game.player.Standing;

/**
 * Shelters: shielded remains of the station where anomalies stop hunting and the ship's shield
 * recharges faster (tip 15: places to breathe). Each shelter's field weakens while occupied and
 * goes offline for a while when exhausted, so hiding is a breather, not a strategy (tip 176/dominant).
 */
public final class Shelters {
    public static final float RADIUS = 420f;
    private static final float DRAIN_TIME = 20f;     // seconds of continuous use
    private static final float RECHARGE_TIME = 30f;  // empty → full when unused
    private static final float OFFLINE_TIME = 25f;
    private static final float SHIELD_REGEN = 1.6f;  // extra shield per second inside

    private static final Color FIELD = Color.valueOf("5fd7ffff");
    private static final Color OFF = Color.valueOf("4f6480ff");

    public static final class Shelter {
        public final Vector2 position = new Vector2();
        float integrity = 1f;
        float offline;
        float pulse;

        public boolean online() {
            return offline <= 0f;
        }

        public float integrity() {
            return integrity;
        }
    }

    private static final Array<Shelter> SHELTERS = new Array<Shelter>();
    private static Shelter occupied;

    private Shelters() {
    }

    /** Places shelters for a new run, away from the black hole and the map edge. */
    public static void create(int count) {
        SHELTERS.clear();
        occupied = null;
        float margin = RADIUS * 1.5f;
        float holeClear = Buffs.getBlackholeTakeRange() * 2.5f + RADIUS;
        for (int i = 0; i < count; i++) {
            Shelter s = new Shelter();
            for (int tries = 0; tries < 200; tries++) {
                s.position.set(MathUtils.random(margin, Rules.SPACE_WIDTH - margin),
                        MathUtils.random(margin, Rules.SPACE_HEIGHT - margin));
                boolean ok = s.position.dst(Blackhole.position) > holeClear;
                for (Shelter o : SHELTERS) {
                    ok &= o.position.dst(s.position) > RADIUS * 6f;
                }
                if (ok) {
                    break;
                }
            }
            s.pulse = MathUtils.random(10f);
            SHELTERS.add(s);
        }
    }

    public static Array<Shelter> all() {
        return SHELTERS;
    }

    /** The online shelter that contains the position, or null. */
    public static Shelter at(Vector2 p) {
        for (Shelter s : SHELTERS) {
            if (s.online() && s.position.dst2(p) <= RADIUS * RADIUS) {
                return s;
            }
        }
        return null;
    }

    /** True while the ship is inside an online shelter: anomalies do not hunt it. */
    public static boolean shipProtected() {
        return occupied != null;
    }

    public static Shelter occupied() {
        return occupied;
    }

    /** Pushes an anomaly that drifted into an online shelter back out. */
    public static void repel(Vector2 position, Vector2 velocity, float dt) {
        for (Shelter s : SHELTERS) {
            if (!s.online()) {
                continue;
            }
            float d = s.position.dst(position);
            if (d < RADIUS && d > 0.001f) {
                float k = (RADIUS - d) / RADIUS;
                float nx = (position.x - s.position.x) / d;
                float ny = (position.y - s.position.y) / d;
                velocity.x += nx * 900f * k * dt;
                velocity.y += ny * 900f * k * dt;
                position.x += nx * 120f * k * dt;
                position.y += ny * 120f * k * dt;
            }
        }
    }

    public static void update(float dt) {
        occupied = Rules.GAME_OVER ? null : at(Buran.position);
        for (Shelter s : SHELTERS) {
            s.pulse += dt;
            if (!s.online()) {
                s.offline -= dt;
                if (s.offline <= 0f) {
                    s.integrity = 0.3f;
                }
                continue;
            }
            if (s == occupied) {
                s.integrity -= dt / DRAIN_TIME;
                Standing st = Standing.getInstance();
                st.setLifeShield(Math.min(st.getMaxLifeShield(), st.getLifeShield() + SHIELD_REGEN * dt));
                if (s.integrity <= 0f) {
                    s.integrity = 0f;
                    s.offline = OFFLINE_TIME;
                    occupied = null;
                }
            } else {
                s.integrity = Math.min(1f, s.integrity + dt / RECHARGE_TIME);
            }
        }
    }

    /** World pass. Uses the overlay's soft textures. */
    public static void render(SpriteBatch batch, ThreatTextures tx) {
        for (Shelter s : SHELTERS) {
            float x = s.position.x;
            float y = s.position.y;
            if (s.online()) {
                float breathe = 0.85f + 0.15f * MathUtils.sin(s.pulse * 1.6f);
                float a = 0.35f + 0.65f * s.integrity;
                batch.setColor(FIELD.r, FIELD.g, FIELD.b, 0.07f * a * breathe);
                batch.draw(tx.dot, x - RADIUS, y - RADIUS, RADIUS * 2f, RADIUS * 2f);
                batch.setColor(FIELD.r, FIELD.g, FIELD.b, 0.55f * a * breathe);
                batch.draw(tx.ring, x - RADIUS, y - RADIUS, RADIUS * 2f, RADIUS * 2f);
                // Integrity shown as a second, inner ring that shrinks with use.
                float ir = RADIUS * (0.25f + 0.65f * s.integrity);
                batch.setColor(FIELD.r, FIELD.g, FIELD.b, 0.22f * a);
                batch.draw(tx.ring, x - ir, y - ir, ir * 2f, ir * 2f);
                // Warm core: something worth flying to (tip 13: light leads).
                batch.setColor(1f, 0.82f, 0.55f, 0.75f * breathe);
                batch.draw(tx.dot, x - 34f, y - 34f, 68f, 68f);
            } else {
                batch.setColor(OFF.r, OFF.g, OFF.b, 0.35f);
                batch.draw(tx.ring, x - RADIUS, y - RADIUS, RADIUS * 2f, RADIUS * 2f);
                float flick = MathUtils.random() < 0.08f ? 0.6f : 0.15f;
                batch.setColor(1f, 0.5f, 0.3f, flick);
                batch.draw(tx.dot, x - 22f, y - 22f, 44f, 44f);
            }
        }
        batch.setColor(Color.WHITE);
    }
}
