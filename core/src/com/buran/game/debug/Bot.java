package com.buran.game.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.buran.game.bot.anomaly.AbstractAnomaly;
import com.buran.game.bot.anomaly.CorrosiveAnomaly;
import com.buran.game.bot.anomaly.ElectricalAnomaly;
import com.buran.game.bot.anomaly.MagneticAnomaly;
import com.buran.game.bot.anomaly.RadiationAnomaly;
import com.buran.game.bot.asteroids.AbstractAsteroid;
import com.buran.game.bot.asteroids.MetalAsteroid;
import com.buran.game.bot.asteroids.MineralAsteroid;
import com.buran.game.bot.blackhole.Blackhole;
import com.buran.game.bot.consumable.Astronaut;
import com.buran.game.bot.consumable.Crate;
import com.buran.game.bot.consumable.Storage;
import com.buran.game.campaign.Campaign;
import com.buran.game.campaign.Sector;
import com.buran.game.campaign.SectorRun;
import com.buran.game.conf.Buffs;
import com.buran.game.conf.Rules;
import com.buran.game.player.Buran;
import com.buran.game.player.Standing;
import com.buran.game.utils.pool.AbstractPoolable;
import com.buran.game.utils.pool.ObjectPool;

/**
 * An autopilot for measuring sectors (?bot): flies to the nearest thing the goal needs, brakes
 * before it, steers around anomalies, the rift and the edge, fires the EMP at close discharges
 * and items when hunters close in. Logs progress, hull lost and deaths, so the difficulty curve
 * comes from play, not from guesses. A decent but not great pilot: a floor, not a human.
 */
public final class Bot {
    private static final Vector2 desired = new Vector2();
    private static final Vector2 away = new Vector2();
    private static final Vector2 tmp = new Vector2();
    private static float time;
    private static float logTime;
    private static float itemCooldown;
    private static float lastHp = -1f;
    private static float hullLost;
    private static int lastLives = -1;
    private static int deaths;
    private static boolean reported;
    private static String lastGoal = "";

    private Bot() {
    }

    public static void reset() {
        time = 0f;
        logTime = 0f;
        itemCooldown = 0f;
        lastHp = -1f;
        hullLost = 0f;
        lastLives = -1;
        deaths = 0;
        reported = false;
    }

    /** Fills out with the stick: direction and thrust (length 0..1). */
    public static void drive(float dt, Vector2 out) {
        dt *= Rules.DEBUG_SPEED;   // game time, not frame time
        time += dt;
        com.buran.game.ui.help.HandPointer.done = true;   // spawning waits for a first move; the bot has made it
        track(dt);
        SectorRun run = Campaign.run();
        AbstractPoolable target = null;
        Vector2 goal = null;
        float stopAt = 0f;
        if (run != null && run.phase() == SectorRun.Phase.EXTRACT) {
            goal = run.beacon();
        } else if (run != null && run.sector().goal == Sector.Goal.SALVAGE) {
            target = nearest(true, MetalAsteroid.class, MineralAsteroid.class);
            if (target == null) {
                target = nearest(false, MetalAsteroid.class, MineralAsteroid.class);
                stopAt = Rules.PLAYER_LASER_TAKE_RANGE * 0.6f;
            }
        } else if (run != null && run.sector().goal == Sector.Goal.CARGO) {
            target = nearest(false, Crate.class, Storage.class);
        } else {
            target = nearest(false, Astronaut.class);
        }
        if (target != null) {
            goal = target.getPosition();
        }
        lastGoal = target == null ? "none" : target.getClass().getSimpleName() + "@" + Math.round(target.getPosition().x) + ","
                + Math.round(target.getPosition().y) + " d=" + Math.round(target.getPosition().dst(Buran.position));
        if (goal == null) {
            goal = tmp.set(Rules.SPACE_WIDTH / 2f, Rules.SPACE_HEIGHT / 2f);
        }
        float dist = goal.dst(Buran.position);
        float speed = MathUtils.clamp((dist - stopAt) * 1.4f, 0f, 1100f);
        desired.set(goal).sub(Buran.position).nor().scl(speed);
        avoid();
        desired.add(away);
        // Steer the velocity towards the desired one: the stick points at the correction.
        tmp.set(desired).sub(Buran.velocity);
        float power = MathUtils.clamp(tmp.len() / 500f, 0f, 1f);
        if (power < 0.08f) {
            out.setZero();
        } else {
            out.set(tmp).nor().scl(power);
        }
        act(dt);
    }

    /** Push away from anything that hunts, from the rift and from the edge. */
    private static void avoid() {
        away.setZero();
        for (Class<?> k : new Class<?>[]{CorrosiveAnomaly.class, ElectricalAnomaly.class, RadiationAnomaly.class, MagneticAnomaly.class}) {
            ObjectPool<?> pool = ObjectPool.EMITTERS.get(k);
            if (pool == null) {
                continue;
            }
            for (Object o : pool.getActiveList()) {
                AbstractAnomaly a = (AbstractAnomaly) o;
                if (!a.isActive() || a.isTaken()) {
                    continue;
                }
                float r = a.bossHp > 0 ? 1300f : 650f;
                float d = a.getPosition().dst(Buran.position);
                if (d < r) {
                    tmp.set(Buran.position).sub(a.getPosition()).nor().scl(900f * (1f - d / r));
                    away.add(tmp);
                }
            }
        }
        if (Blackhole.enabled) {
            float r = Buffs.getBlackholeTakeRange() * 1.7f;
            float d = Blackhole.position.dst(Buran.position);
            if (d < r) {
                tmp.set(Buran.position).sub(Blackhole.position).nor().scl(1400f * (1f - d / r));
                away.add(tmp);
            }
        }
        float m = 350f;
        if (Buran.position.x < m) away.x += 600f;
        if (Buran.position.x > Rules.SPACE_WIDTH - m) away.x -= 600f;
        if (Buran.position.y < m) away.y += 600f;
        if (Buran.position.y > Rules.SPACE_HEIGHT - m) away.y -= 600f;
    }

    /** EMP at close discharges; items when hunters are close. */
    private static void act(float dt) {
        itemCooldown -= dt;
        boolean electricNear = false;
        boolean huntersNear = false;
        ObjectPool<?> e = ObjectPool.EMITTERS.get(ElectricalAnomaly.class);
        if (e != null) {
            for (Object o : e.getActiveList()) {
                AbstractAnomaly a = (AbstractAnomaly) o;
                if (a.isActive() && a.getPosition().dst(Buran.position) < 550f) {
                    electricNear = true;
                }
            }
        }
        ObjectPool<?> c = ObjectPool.EMITTERS.get(CorrosiveAnomaly.class);
        if (c != null) {
            int n = 0;
            for (Object o : c.getActiveList()) {
                AbstractAnomaly a = (AbstractAnomaly) o;
                if (a.isActive() && a.isHungry() && a.getPosition().dst(Buran.position) < 700f) {
                    n++;
                }
            }
            huntersNear = n >= 2;
        }
        if (electricNear) {
            com.buran.game.input.Gamepad.get().botPulse();
        }
        if (huntersNear && itemCooldown <= 0f) {
            itemCooldown = 3f;
            com.buran.game.ui.groups.selector.SelectorGroup.getInstance().useSelected();
        }
    }

    private static AbstractPoolable nearest(boolean fragmentsOnly, Class<?>... kinds) {
        AbstractPoolable best = null;
        float bd = Float.MAX_VALUE;
        for (Class<?> k : kinds) {
            ObjectPool<?> pool = ObjectPool.EMITTERS.get(k);
            if (pool == null) {
                continue;
            }
            for (Object o : pool.getActiveList()) {
                AbstractPoolable p = (AbstractPoolable) o;
                if (!p.isActive() || p.isTaken()) {
                    continue;
                }
                if (p instanceof AbstractAsteroid && ((AbstractAsteroid) p).isFragmented() != fragmentsOnly) {
                    continue;
                }
                if (Blackhole.enabled && p.getPosition().dst(Blackhole.position) < Buffs.getBlackholeTakeRange() * 1.2f) {
                    continue;   // not worth dying for
                }
                float d = p.getPosition().dst2(Buran.position);
                if (d < bd) {
                    bd = d;
                    best = p;
                }
            }
        }
        return best;
    }

    private static void track(float dt) {
        Standing st = Standing.getInstance();
        if (lastHp >= 0f && st.getHp() < lastHp) {
            hullLost += lastHp - st.getHp();
        }
        lastHp = st.getHp();
        if (lastLives >= 0 && st.getLives() < lastLives) {
            deaths++;
        }
        lastLives = st.getLives();
        logTime += dt;
        SectorRun run = Campaign.run();
        if (run == null) {
            return;
        }
        if (logTime > 10f) {
            logTime = 0f;
            Gdx.app.log("BOT", "TICK sector=" + run.sector().number + " t=" + Math.round(time) + " progress="
                    + run.progress() + "/" + run.sector().target + " hullLost=" + Math.round(hullLost) + " deaths=" + deaths + " goal=" + lastGoal
                    + " ship=" + Math.round(Buran.position.x) + "," + Math.round(Buran.position.y)
                    + " field=" + Rules.SPACE_WIDTH + "x" + Rules.SPACE_HEIGHT + " laser=" + Buran.isLaser);
        }
    }

    /** Called every frame by the game: reports the end of the run once. */
    public static void report(boolean clear, boolean over) {
        SectorRun run = Campaign.run();
        if (reported || run == null || (!clear && !over && time < 420f)) {
            return;
        }
        reported = true;
        String result = clear ? "CLEAR" : over ? "FAILED" : "TIMEOUT";
        Gdx.app.log("BOT", "RESULT sector=" + run.sector().number + " result=" + result + " time=" + Math.round(time)
                + " progress=" + run.progress() + "/" + run.sector().target + " hullLost=" + Math.round(hullLost)
                + " deaths=" + deaths + " maxHull=" + Math.round(Standing.getInstance().getMaxHp()));
    }
}
