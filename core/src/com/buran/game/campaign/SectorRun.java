package com.buran.game.campaign;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.buran.game.bot.anomaly.CorrosiveAnomaly;
import com.buran.game.bot.anomaly.CorrosiveAnomalyEmitter;
import com.buran.game.bot.anomaly.ElectricalAnomaly;
import com.buran.game.bot.anomaly.ElectricalAnomalyEmitter;
import com.buran.game.bot.anomaly.MagneticAnomaly;
import com.buran.game.bot.anomaly.MagneticAnomalyEmitter;
import com.buran.game.bot.anomaly.RadiationAnomaly;
import com.buran.game.bot.anomaly.RadiationAnomalyEmitter;
import com.buran.game.bot.asteroids.AbstractAsteroidEmitter;
import com.buran.game.bot.asteroids.MetalAsteroid;
import com.buran.game.bot.asteroids.MineralAsteroid;
import com.buran.game.bot.blackhole.Blackhole;
import com.buran.game.bot.consumable.Astronaut;
import com.buran.game.bot.consumable.AstronautEmitter;
import com.buran.game.conf.Buffs;
import com.buran.game.conf.MSConstants;
import com.buran.game.conf.Rules;
import com.buran.game.effects.BoomBox;
import com.buran.game.effects.Juice;
import com.buran.game.player.Buran;
import com.buran.game.player.Standing;
import com.buran.game.ui.menu.Theme;
import com.buran.game.utils.pool.ObjectPool;
import com.buran.game.world.ThreatTextures;

/**
 * A sector being played: briefing, goal tracking, the extraction beacon and the clear state.
 * Phases follow the sector rhythm: calm briefing, the task, then the flight to the beacon.
 */
public final class SectorRun {
    public enum Phase { BRIEFING, ACTIVE, EXTRACT, CLEAR }

    private static final float CARD_FADE = 0.4f;
    /** Typing keeps pace with the voice (about 10 characters a second, with pauses) and starts with it. */
    private static final float TYPE_SPEED = 10.5f;
    private static final float VOICE_AT = 0.5f;
    private static final float TYPE_DELAY = VOICE_AT + 0.45f;
    private static final float BEACON_RADIUS = 260f;
    private static final Color WARM = Color.valueOf("ffc06aff");

    private final Sector sector;
    private final BoomBox boomBox = new BoomBox();
    private Phase phase = Phase.BRIEFING;
    private float phaseTime;
    private int baseline;
    private int progress;
    private final Vector2 beacon = new Vector2();
    private float time;
    private final GlyphLayout layout = new GlyphLayout();

    public SectorRun(Sector sector) {
        this.sector = sector;
    }

    public Sector sector() {
        return sector;
    }

    public Phase phase() {
        return phase;
    }

    public boolean isClear() {
        return phase == Phase.CLEAR;
    }

    /** Applies the sector's threat table. Call after the emitters exist. */
    public void configure() {
        Rules.corrosiveHuntTime = sector.corrosiveHuntTime;
        CorrosiveAnomalyEmitter c = (CorrosiveAnomalyEmitter) ObjectPool.EMITTERS.get(CorrosiveAnomaly.class);
        c.setAnomaliesMaxCount(corrosiveCap());
        c.setRespawnTime(sector.corrosiveRespawn * (1f + 0.3f * Campaign.fails(sector.number)));
        trim(c, corrosiveCap());
        ((ElectricalAnomalyEmitter) ObjectPool.EMITTERS.get(ElectricalAnomaly.class)).setAnomaliesMaxCount(sector.electricMax);
        trim(ObjectPool.EMITTERS.get(ElectricalAnomaly.class), sector.electricMax);
        ((RadiationAnomalyEmitter) ObjectPool.EMITTERS.get(RadiationAnomaly.class)).setAnomaliesMaxCount(sector.radiationMax);
        trim(ObjectPool.EMITTERS.get(RadiationAnomaly.class), sector.radiationMax);
        ((MagneticAnomalyEmitter) ObjectPool.EMITTERS.get(MagneticAnomaly.class)).setAnomaliesMaxCount(sector.magneticMax);
        trim(ObjectPool.EMITTERS.get(MagneticAnomaly.class), sector.magneticMax);
        for (Class<?> k : new Class<?>[]{MetalAsteroid.class, MineralAsteroid.class}) {
            AbstractAsteroidEmitter e = (AbstractAsteroidEmitter) ObjectPool.EMITTERS.get(k);
            e.setAsteroidsMaxCount((sector.asteroidMax + 1) / 2);
            e.setRespawnTime(sector.asteroidRespawn);
            trim(e, (sector.asteroidMax + 1) / 2);
        }
        limit(Astronaut.class, sector.astronautMax);
        limit(com.buran.game.bot.consumable.Crate.class, sector.crateMax);
        limit(com.buran.game.bot.consumable.Storage.class, sector.storageMax);
        limit(com.buran.game.bot.consumable.fuel.Fuel.class, sector.fuelMax);
        if (sector.startWithLaser) {
            Buran.isLaser = true;
            Standing.getInstance().setLaserChargeLevel(Standing.getInstance().getMaxLaserChargeLevel());
        }
        Rules.CONSUMABLE_CRATE_DANGER_PROBABILITY = sector.dangerOdds;
        Rules.CONSUMABLE_STORAGE_DANGER_PROBABILITY = sector.dangerOdds;
        if (sector.noRadar) {
            com.buran.game.player.navigation.Radar.isActive = false;
        }
        for (java.util.Map.Entry<com.buran.game.labelsticker.Sticker, Integer> it : sector.startItems.entrySet()) {
            com.buran.game.ui.groups.selector.SelectorGroup.getInstance().addSelector(it.getKey(), it.getValue());
        }
        if (sector.goal == Sector.Goal.RESCUE) {
            placeCrew();
        }
        placeCache();
        layout();
        baseline = counter();
        if (Rules.DEBUG_CARD) {
            com.buran.game.ui.hud.CrewCard.show(com.buran.game.labelsticker.Sticker.MEDIC, "MEDIC", "HULL REPAIRED");
        }
        if (Rules.DEBUG_CENSUS) {
            com.badlogic.gdx.Gdx.app.log("CENSUS", "CAPS sector=" + sector.number + " corrosive=" + sector.corrosiveMax
                    + " electric=" + sector.electricMax + " radiation=" + sector.radiationMax + " magnetic=" + sector.magneticMax
                    + " rocks=" + sector.asteroidMax + " crew=" + sector.astronautMax + " crates=" + sector.crateMax
                    + " storages=" + sector.storageMax + " fuel=" + sector.fuelMax + " hole=" + sector.blackhole
                    + " shelters=" + sector.shelters + " noradar=" + sector.noRadar + " laser=" + sector.startWithLaser
                    + " escalate=" + sector.escalate + " items=" + sector.startItems.size()
                    + " loot=" + sector.loot());
        }
        if (Rules.DEBUG_RAM) {
            Standing.getInstance().changeRamPower(3.2f - Standing.getInstance().getRamPower());
        }
        if (Rules.DEBUG_FAST_GOAL) {
            baseline -= sector.target;
        } else if (Rules.DEBUG_THIRD) {
            baseline -= sector.target / 3;
        }
    }

    /**
     * The crew to rescue is already out there when the sector opens: spread around the ship,
     * a screen or so away, in different directions, so each one is a small trip (tips 9, 85).
     */
    private void placeCrew() {
        AstronautEmitter e = (AstronautEmitter) ObjectPool.EMITTERS.get(Astronaut.class);
        trim(e, 0);
        crewEmittedAtStart = AstronautEmitter.getEmitted();
        int n = Math.min(sector.target, sector.astronautMax);
        float base = MathUtils.random(MathUtils.PI2);
        for (int i = 0; i < n; i++) {
            float ang = base + i * MathUtils.PI2 / n + MathUtils.random(-0.4f, 0.4f);
            crewSpot(ang, MathUtils.random(0.35f, 0.9f));
            e.emitAt(spot.x, spot.y);
        }
        // A field full of people: more keep drifting in until the goal is covered.
        e.setConsumablesMaxCount(sector.astronautMax);
        e.setRespawnTime(sector.crewRespawn);
    }

    /**
     * Each sector hides one golden crate in the corner farthest from the start, with no marker
     * (tips 68, 88). Inside: a repair kit and a page of the station's log for the codex.
     */
    private void placeCache() {
        Campaign.cache = null;
        if (Campaign.logFound(sector.number)) {
            return;
        }
        // The farthest corner; in a small sector that may still be on screen at the start, so
        // tuck it to the very edge and never inside the opening view.
        float m = 260f;
        float x = Buran.position.x < Rules.SPACE_WIDTH / 2f ? Rules.SPACE_WIDTH - m : m;
        float y = Buran.position.y < Rules.SPACE_HEIGHT / 2f ? Rules.SPACE_HEIGHT - m : m;
        if (Math.abs(x - Buran.position.x) < Rules.VIEW_WIDTH * 0.6f && Math.abs(y - Buran.position.y) < Rules.VIEW_HEIGHT * 0.6f) {
            // Still in view: put it straight behind the start, past the screen edge.
            y = Buran.position.y < Rules.SPACE_HEIGHT / 2f ? Math.min(Rules.SPACE_HEIGHT - m, Buran.position.y + Rules.VIEW_HEIGHT * 0.65f)
                    : Math.max(m, Buran.position.y - Rules.VIEW_HEIGHT * 0.65f);
        }
        com.buran.game.bot.consumable.CrateEmitter ce =
                (com.buran.game.bot.consumable.CrateEmitter) ObjectPool.EMITTERS.get(com.buran.game.bot.consumable.Crate.class);
        com.buran.game.bot.consumable.Crate c = (com.buran.game.bot.consumable.Crate) ce.emitAt(x, y);
        c.makeCache();
        Campaign.cache = c;
        cacheHint = 25f;
    }

    private float cacheHint;

    /**
     * Designed pieces instead of raw scatter at the start (tip 85): rocks form a belt across the
     * way in salvage sectors; thick hunter sectors open with two nests far from the ship.
     */
    private void layout() {
        float m = 400f;
        if (sector.goal == Sector.Goal.SALVAGE) {
            float base = MathUtils.random(MathUtils.PI2);
            float cx = Buran.position.x + MathUtils.cos(base) * Rules.VIEW_HEIGHT * 1.4f;
            float cy = Buran.position.y + MathUtils.sin(base) * Rules.VIEW_HEIGHT * 1.4f;
            float radius = Rules.VIEW_HEIGHT * 1.1f;
            for (Class<?> k : new Class<?>[]{MetalAsteroid.class, MineralAsteroid.class}) {
                java.util.List<?> list = ObjectPool.EMITTERS.get(k).getActiveList();
                for (int i = 0; i < list.size(); i++) {
                    com.buran.game.bot.asteroids.AbstractAsteroid a = (com.buran.game.bot.asteroids.AbstractAsteroid) list.get(i);
                    float t = base + MathUtils.PI + MathUtils.random(-1.1f, 1.1f);
                    float r = radius + MathUtils.random(-160f, 160f);
                    a.getPosition().set(MathUtils.clamp(cx + MathUtils.cos(t) * r, m, Rules.SPACE_WIDTH - m),
                            MathUtils.clamp(cy + MathUtils.sin(t) * r, m, Rules.SPACE_HEIGHT - m));
                    a.getVelocity().set(-MathUtils.sin(t), MathUtils.cos(t)).scl(MathUtils.random(15f, 40f));
                }
            }
        }
        if (sector.corrosiveMax >= 8) {
            java.util.List<?> list = ObjectPool.EMITTERS.get(CorrosiveAnomaly.class).getActiveList();
            float base = MathUtils.random(MathUtils.PI2);
            for (int i = 0; i < list.size(); i++) {
                com.buran.game.bot.anomaly.AbstractAnomaly a = (com.buran.game.bot.anomaly.AbstractAnomaly) list.get(i);
                float t = base + (i % 2) * MathUtils.PI * 0.9f;
                float d = Rules.VIEW_HEIGHT * 1.3f;
                a.getPosition().set(
                        MathUtils.clamp(Buran.position.x + MathUtils.cos(t) * d + MathUtils.random(-300f, 300f), m, Rules.SPACE_WIDTH - m),
                        MathUtils.clamp(Buran.position.y + MathUtils.sin(t) * d + MathUtils.random(-300f, 300f), m, Rules.SPACE_HEIGHT - m));
            }
        }
    }

    private final com.badlogic.gdx.math.Vector2 spot = new com.badlogic.gdx.math.Vector2();

    /**
     * Where a crew member drifts: around the ship at a fraction of a screen, or, in a sector
     * with the rift, just outside its capture ring (close enough to be urgent).
     */
    private void crewSpot(float ang, float k) {
        float m = 260f;
        boolean rift = sector.blackhole && com.buran.game.bot.blackhole.Blackhole.enabled;
        com.badlogic.gdx.math.Vector2 c = rift ? com.buran.game.bot.blackhole.Blackhole.position : Buran.position;
        float r = rift ? com.buran.game.conf.Buffs.getBlackholeTakeRange() * MathUtils.random(1.35f, 1.8f)
                : Rules.VIEW_HEIGHT * k;
        spot.set(MathUtils.clamp(c.x + MathUtils.cos(ang) * r, m, Rules.SPACE_WIDTH - m),
                MathUtils.clamp(c.y + MathUtils.sin(ang) * r, m, Rules.SPACE_HEIGHT - m));
    }

    /**
     * Hunters in this sector: fewer after failures here (a fifth less per failure, up to three),
     * never said out loud (tip 21). The lesson's own threats stay as they are.
     */
    private int corrosiveCap() {
        if (sector.corrosiveMax == 0) {
            return 0;
        }
        return Math.max(1, Math.round(sector.corrosiveMax * (1f - 0.2f * Campaign.fails(sector.number))));
    }

    /** Removes whatever an emitter spawned at construction beyond the sector's cap. */
    private static void trim(ObjectPool<?> pool, int max) {
        if (pool == null) {
            return;
        }
        while (pool.getActiveList().size() > Math.max(0, max)) {
            pool.free(pool.getActiveList().size() - 1);
        }
    }

    /** Caps a consumable emitter and removes anything spawned at start beyond the cap. */
    private static void limit(Class<?> k, int max) {
        com.buran.game.bot.consumable.AbstractConsumableEmitter e =
                (com.buran.game.bot.consumable.AbstractConsumableEmitter) ObjectPool.EMITTERS.get(k);
        if (e == null) {
            return;
        }
        e.setConsumablesMaxCount(max);
        e.setConsumablesAtStart(Math.min(e.getConsumablesAtStart(), max));
        while (e.getActiveList().size() > max) {
            e.free(e.getActiveList().size() - 1);
        }
    }

    private int counter() {
        Standing st = Standing.getInstance();
        switch (sector.goal) {
            case RESCUE:
                return st.getAstronautsTotal();
            case CARGO:
                return Campaign.cargoTaken;
            default:
                return st.getMinedTotal();
        }
    }

    public int progress() {
        return Math.min(progress, sector.target);
    }

    public void update(float dt) {
        time += dt;
        if (sector.noRadar) {
            com.buran.game.player.navigation.Radar.isActive = false;   // nothing brings it back here
        }
        phaseTime += dt;
        switch (phase) {
            case BRIEFING:
                // The world holds still until the player has heard the radio and pressed A.
                if (!voiced && phaseTime > VOICE_AT) {
                    voiced = true;
                    com.buran.game.platform.Radio.play("sector" + sector.code());
                }
                com.buran.game.platform.Radio.update();
                if (com.badlogic.gdx.Gdx.input.justTouched()) {
                    advance();
                }
                break;
            case ACTIVE:
                track(dt);
                progress = counter() - baseline;
                script(dt);
                if (progress >= sector.target) {
                    startExtraction();
                }
                break;
            case EXTRACT:
                track(dt);
                if (!Rules.GAME_OVER && Buran.position.dst(beacon) < BEACON_RADIUS) {
                    setPhase(Phase.CLEAR);
                    medals = (crewLost == 0 && sector.goal == Sector.Goal.RESCUE ? Campaign.MEDAL_SAFE : 0)
                            | (!hullHit ? Campaign.MEDAL_UNTOUCHED : 0)
                            | (flightTime <= Campaign.par(sector) ? Campaign.MEDAL_SWIFT : 0);
                    int before = Upgrades.unlocked();
                    newBest = Campaign.record(sector, flightTime, medals);
                    Campaign.recordLost(sector, crewLost);
                    int after = Upgrades.unlocked();
                    newUpgrade = after > before ? Upgrades.NAMES[after - 1] : null;
                    Campaign.complete(sector);
                    boomBox.stopAllSound();
                    boomBox.stopLoopingEffects();
                    boomBox.playSound(MSConstants.LEVEL_UP);
                    Juice.flash(0.35f, 1f, 0.85f, 0.6f);
                }
                break;
            default:
                break;
        }
    }

    // ------------------------------------------------------------------ scripted lessons

    /** A short line from the ship under the goal (seconds left). */
    private String notice;
    private float noticeTime;
    private int crewStage;
    private int laserStage;
    private float laserTimer;
    private com.buran.game.bot.anomaly.AbstractAnomaly discharge;
    private com.buran.game.bot.consumable.AbstractConsumable laserBox;

    private void say(String line, float seconds) {
        com.buran.game.ui.hud.Comms.say(line, seconds, com.buran.game.ui.hud.Comms.STORY);
    }

    private void alert(String line, float seconds) {
        com.buran.game.ui.hud.Comms.say(line, seconds, com.buran.game.ui.hud.Comms.ALERT);
    }

    private void script(float dt) {
        noticeTime -= dt;
        if (sector.goal == Sector.Goal.RESCUE) {
            crewScript();
        }
        if (sector.number == 2) {
            laserScript(dt);
        }
        if (sector.escalate) {
            escalate(dt);
        }
        if (cacheHint > 0f && Campaign.cache != null) {
            cacheHint -= dt;
            if (cacheHint <= 0f) {
                say("SOMETHING GLINTS AT THE EDGE OF THE SECTOR", 6f);
            }
        }
        if (sector.boss > 0) {
            bossScript(dt);
        }
        if (sector.choice) {
            choiceScript(dt);
        }
    }

    private com.buran.game.bot.consumable.AbstractConsumable choiceCrew;
    private com.buran.game.bot.consumable.AbstractConsumable choiceFuel;
    private int choiceStage;
    private float choiceTime;

    /**
     * A choice with a cost (tip 41): halfway through, a crew member and the station's fuel depot
     * drift on opposite sides of the rift. Whichever you take first is yours; the rift takes the
     * other. Wait too long and it takes both. Fuel fills the tanks; the crew counts toward home.
     */
    private void choiceScript(float dt) {
        if (choiceStage == 0) {
            if (progress * 2 < sector.target || !Blackhole.enabled) {
                return;
            }
            choiceStage = 1;
            float ang = MathUtils.random(MathUtils.PI2);
            float r = Buffs.getBlackholeTakeRange() * 1.5f;
            choiceCrew = ((AstronautEmitter) ObjectPool.EMITTERS.get(Astronaut.class)).emitAt(
                    Blackhole.position.x + MathUtils.cos(ang) * r, Blackhole.position.y + MathUtils.sin(ang) * r);
            choiceFuel = ((com.buran.game.bot.consumable.AbstractConsumableEmitter) ObjectPool.EMITTERS.get(
                    com.buran.game.bot.consumable.fuel.Fuel.class)).emitAt(
                    Blackhole.position.x - MathUtils.cos(ang) * r, Blackhole.position.y - MathUtils.sin(ang) * r);
            alert("TWO SIGNALS: ONE OF OURS, AND THE STATION'S FUEL DEPOT.  THE RIFT TAKES THE OTHER", 10f);
            return;
        }
        if (choiceStage != 1) {
            return;
        }
        choiceTime += dt;
        boolean crewGone = !choiceCrew.isActive() || choiceCrew.isTaken();
        boolean fuelGone = !choiceFuel.isActive() || choiceFuel.isTaken();
        boolean crewTaken = crewGone && choiceCrew.getPosition().dst(Buran.position) < 500f;
        boolean fuelTaken = fuelGone && choiceFuel.getPosition().dst(Buran.position) < 500f;
        if (crewTaken || fuelTaken || choiceTime > 40f || (crewGone && fuelGone)) {
            choiceStage = 2;
            if (fuelTaken && !crewTaken) {
                Standing st = Standing.getInstance();
                st.setFuel(st.getMaxFuel());
                if (choiceCrew.isActive()) {
                    choiceCrew.destroyWithEffect();
                }
                say("TANKS FULL.  WE LEFT HIM BEHIND", 8f);
                Campaign.remember("choice16", "fuel");
            } else if (crewTaken) {
                if (choiceFuel.isActive()) {
                    choiceFuel.destroyWithEffect();
                }
                say("HE IS ABOARD.  THE DEPOT IS GONE", 8f);
                Campaign.remember("choice16", "crew");
            } else {
                if (choiceCrew.isActive()) {
                    choiceCrew.destroyWithEffect();
                }
                if (choiceFuel.isActive()) {
                    choiceFuel.destroyWithEffect();
                }
                say("THE RIFT TOOK BOTH", 8f);
                Campaign.remember("choice16", "none");
            }
        }
    }

    private com.buran.game.bot.anomaly.AbstractAnomaly boss;
    private boolean bossDone;
    private float bossChild;

    /**
     * The chapter's boss wakes halfway through the goal, announced on the radio first (tips 8,
     * 69). The Mother throws out small hunters now and then; three rockets or three boosted rams
     * break her. The Storm Heart takes three EMP pulses. Beating it is not the goal - surviving is.
     */
    private void bossScript(float dt) {
        if (bossDone) {
            return;
        }
        if (boss == null) {
            if (progress * 2 >= sector.target || Rules.DEBUG_BOSS) {
                spawnBoss();
            }
            return;
        }
        if (!boss.isActive()) {
            bossDone = true;
            boss = null;
            say("IT'S GONE.  KEEP GOING", 6f);
            com.buran.game.effects.Juice.flash(0.4f, 1f, 0.9f, 0.7f);
            boomBox.playSound(MSConstants.LEVEL_UP);
            return;
        }
        boss.bossCooldown = Math.max(0f, boss.bossCooldown - dt);
        if (sector.boss == 1) {
            bossChild += dt;
            CorrosiveAnomalyEmitter c = (CorrosiveAnomalyEmitter) ObjectPool.EMITTERS.get(CorrosiveAnomaly.class);
            if (bossChild > 9f && c.getActiveList().size() < sector.corrosiveMax + 3) {
                bossChild = 0f;
                com.buran.game.bot.anomaly.AbstractAnomaly a = c.emitAt(boss.getPosition().x + MathUtils.random(-200f, 200f),
                        boss.getPosition().y + MathUtils.random(-200f, 200f));
                a.makeBoss(0, 0);
            }
        }
    }

    private void spawnBoss() {
        float ang = MathUtils.random(MathUtils.PI2);
        float r = Rules.VIEW_HEIGHT * 1.1f;
        float m = 600f;
        float x = MathUtils.clamp(Buran.position.x + MathUtils.cos(ang) * r, m, Rules.SPACE_WIDTH - m);
        float y = MathUtils.clamp(Buran.position.y + MathUtils.sin(ang) * r, m, Rules.SPACE_HEIGHT - m);
        if (sector.boss == 1) {
            boss = ((CorrosiveAnomalyEmitter) ObjectPool.EMITTERS.get(CorrosiveAnomaly.class)).emitAt(x, y);
            boss.makeBoss(3, 3);
            com.buran.game.ui.groups.selector.SelectorGroup.getInstance()
                    .addSelector(com.buran.game.labelsticker.Sticker.BOOST, 3);
            alert("SOMETHING BIG IS WAKING UP  -  ROCKETS, OR HIT IT AT BOOST SPEED", 9f);
        } else {
            boss = ((ElectricalAnomalyEmitter) ObjectPool.EMITTERS.get(ElectricalAnomaly.class)).emitAt(x, y);
            boss.makeBoss(3, boss.getLevel());
            boss.setScaleX(2.6f);
            boss.setScaleY(2.6f);
            alert("THE STORM HAS A HEART  -  {Y} PULSE IT THREE TIMES", 9f);
        }
        com.buran.game.effects.Shake.getInstance().shakeCamera(0.6f, 40, 14);
    }

    /** The extraction beacon (valid once the goal is met). */
    public com.badlogic.gdx.math.Vector2 beacon() {
        return beacon;
    }

    /** The boss, for its name plate (null when there is none). */
    public com.buran.game.bot.anomaly.AbstractAnomaly boss() {
        return boss;
    }

    private float flightTime;
    private int crewLost;
    private boolean hullHit;
    private int medals;
    private boolean newBest;
    private String newUpgrade;

    /** The upgrade this clear unlocked, or null. */
    public String newUpgrade() {
        return newUpgrade;
    }

    /** Time in flight, and whether the hull was ever hit (for the records). */
    private void track(float dt) {
        flightTime += dt;
        Standing st = Standing.getInstance();
        if (st.getHp() < st.getMaxHp() - 0.01f) {
            hullHit = true;
        }
    }

    public float flightTime() {
        return flightTime;
    }

    public int medals() {
        return medals;
    }

    public boolean isNewBest() {
        return newBest;
    }

    private float escalateTimer;
    private int escalateLevel;

    /** The collapsing field: every 40 s more hunters, faster respawn, one more discharge. */
    private void escalate(float dt) {
        escalateTimer += dt;
        if (escalateTimer < 40f || escalateLevel >= 5) {
            return;
        }
        escalateTimer = 0f;
        escalateLevel++;
        CorrosiveAnomalyEmitter c = (CorrosiveAnomalyEmitter) ObjectPool.EMITTERS.get(CorrosiveAnomaly.class);
        c.setAnomaliesMaxCount(corrosiveCap() + escalateLevel * 2);
        c.setRespawnTime(Math.max(1.5f, sector.corrosiveRespawn * (1f - 0.15f * escalateLevel)));
        if (escalateLevel % 2 == 0) {
            ((ElectricalAnomalyEmitter) ObjectPool.EMITTERS.get(ElectricalAnomaly.class))
                    .setAnomaliesMaxCount(Math.min(3, sector.electricMax + escalateLevel / 2));
        }
        alert("THE FIELD IS COLLAPSING  -  IT GROWS STRONGER", 5f);
    }

    /**
     * Sector 01: the first one comes aboard freely. Then the hull shield charges, and from now on
     * the nose and tail hurt people: they must come in over the wings. Anyone lost is replaced.
     */
    private void crewScript() {
        if (sector.number == 1 && crewStage == 0 && progress >= 1) {
            crewStage = 1;
            Standing st = Standing.getInstance();
            st.changeRamPower(2.5f - st.getRamPower());
            com.buran.game.player.Buran.ramOn = true;
            com.buran.game.player.Buran.ramSwitchAge = 0f;
            say("RAM SHIELD ON  -  NOSE AND TAIL KILL NOW. TAKE THEM OVER THE WINGS, OR {A} SWITCH IT OFF", 10f);
        }
        AstronautEmitter e = (AstronautEmitter) ObjectPool.EMITTERS.get(Astronaut.class);
        int out = 0;
        for (Object o : e.getActiveList()) {
            com.buran.game.utils.pool.AbstractPoolable ap = (com.buran.game.utils.pool.AbstractPoolable) o;
            if (ap.isActive() && !ap.isTaken()) {
                out++;
            }
        }
        // Keep the stream going until those out there cover what is still missing.
        e.setConsumablesMaxCount(progress + out >= sector.target ? 0 : sector.astronautMax);
        int lost = Math.max(0, AstronautEmitter.getEmitted() - crewEmittedAtStart - progress - out);
        if (lost > crewLost) {
            crewLost = lost;
            if (crewLost == 1) {
                alert("WE LOST ONE", 4f);
            }
        }
        if (progress + out < sector.target && out == 0 && !Rules.GAME_OVER) {
            crewSpot(MathUtils.random(MathUtils.PI2), 0.8f);
            e.emitAt(spot.x, spot.y);   // never leave the field empty
        }
    }

    private int crewEmittedAtStart;

    /**
     * Sector 02: a third of the way in, a discharge burns the laser out. A storage drifts close by
     * with a new one; without it no rock can be burned.
     */
    private void laserScript(float dt) {
        switch (laserStage) {
            case 0:
                if (progress >= sector.target / 3 && Buran.isLaser) {
                    float ang = MathUtils.random(MathUtils.PI2);
                    float r = Rules.VIEW_HEIGHT * 0.9f;
                    float m = 300f;
                    discharge = ((com.buran.game.bot.anomaly.AbstractAnomalyEmitter) ObjectPool.EMITTERS.get(ElectricalAnomaly.class))
                            .emitAt(MathUtils.clamp(Buran.position.x + MathUtils.cos(ang) * r, m, Rules.SPACE_WIDTH - m),
                                    MathUtils.clamp(Buran.position.y + MathUtils.sin(ang) * r, m, Rules.SPACE_HEIGHT - m));
                    laserStage = 1;
                    laserTimer = 0f;
                    alert("DISCHARGE ON THE SCOPE  -  IT IS COMING FOR US", 5f);
                }
                break;
            case 1:
                laserTimer += dt;
                if (!Buran.isLaser) {
                    removeDischarge();
                    placeLaserBox();
                    com.buran.game.player.navigation.Radar.isActive = true;
                    laserStage = 2;
                    alert("THE DISCHARGE BURNED OUR LASER.  A STORAGE IS DRIFTING NEARBY  -  A NEW ONE IS INSIDE", 9f);
                } else if (laserTimer > 30f) {
                    removeDischarge();
                    laserStage = 3;
                }
                break;
            case 2:
                if (Buran.isLaser) {
                    laserStage = 3;
                    say("LASER ONLINE", 3f);
                } else if (laserBox == null || !laserBox.isActive() || laserBox.isTaken() && !Buran.isLaser && laserBox.getPosition().dst(Buran.position) > 400f) {
                    placeLaserBox();
                }
                break;
            default:
                break;
        }
    }

    private void removeDischarge() {
        if (discharge != null && discharge.isActive()) {
            discharge.destroyWithEffect();
        }
        discharge = null;
    }

    private void placeLaserBox() {
        float ang = MathUtils.random(MathUtils.PI2);
        float r = Rules.VIEW_HEIGHT * 0.7f;
        float m = 300f;
        com.buran.game.bot.consumable.AbstractConsumableEmitter se =
                (com.buran.game.bot.consumable.AbstractConsumableEmitter) ObjectPool.EMITTERS.get(com.buran.game.bot.consumable.Storage.class);
        laserBox = se.emitAt(MathUtils.clamp(Buran.position.x + MathUtils.cos(ang) * r, m, Rules.SPACE_WIDTH - m),
                MathUtils.clamp(Buran.position.y + MathUtils.sin(ang) * r, m, Rules.SPACE_HEIGHT - m));
        ((com.buran.game.bot.consumable.Storage) laserBox).force(com.buran.game.labelsticker.Sticker.LASER);
    }

    private boolean voiced;
    private boolean typedAll;
    private final boolean subtitles = com.buran.game.ui.groups.mvc.profiles.Profiles.getSettings().isSubtitles();

    public boolean isBriefing() {
        return phase == Phase.BRIEFING;
    }

    /** A on the briefing: first press finishes the typing, the next one starts the sector. */
    public void advance() {
        if (phase != Phase.BRIEFING || phaseTime < 0.4f) {
            return;
        }
        if (subtitles && !typedAll && typed() < sector.briefing.length()) {
            typedAll = true;
            return;
        }
        com.buran.game.platform.Radio.stop();
        boomBox.playSound(MSConstants.UI_MENU);
        setPhase(Phase.ACTIVE);
    }

    private int typed() {
        return typedAll ? sector.briefing.length() : (int) MathUtils.clamp((phaseTime - TYPE_DELAY) * TYPE_SPEED, 0f, sector.briefing.length());
    }

    private void setPhase(Phase p) {
        phase = p;
        phaseTime = 0f;
    }

    private void startExtraction() {
        // The beacon appears far from the ship and clear of the black hole.
        float minDist = Rules.DEBUG_FAST_GOAL ? 0f : Math.min(Rules.SPACE_WIDTH, Rules.SPACE_HEIGHT) * 0.45f;
        if (Rules.DEBUG_FAST_GOAL) {
            beacon.set(Buran.position.x + 700f, Buran.position.y);
            boomBox.playSound(MSConstants.UP);
            setPhase(Phase.EXTRACT);
            return;
        }
        if (sector.beaconAtRift && Blackhole.enabled) {
            // The way home: just outside the rift's ring, on the side facing the ship.
            beacon.set(Buran.position).sub(Blackhole.position).nor().scl(Buffs.getBlackholeTakeRange() * 1.2f).add(Blackhole.position);
            boomBox.playSound(MSConstants.UP);
            setPhase(Phase.EXTRACT);
            return;
        }
        float margin = 500f;
        for (int i = 0; i < 300; i++) {
            beacon.set(MathUtils.random(margin, Rules.SPACE_WIDTH - margin), MathUtils.random(margin, Rules.SPACE_HEIGHT - margin));
            if (beacon.dst(Buran.position) > minDist && beacon.dst(Blackhole.position) > Buffs.getBlackholeTakeRange() * 3f) {
                break;
            }
        }
        boomBox.playSound(MSConstants.UP);
        setPhase(Phase.EXTRACT);
    }

    // ------------------------------------------------------------------ world pass

    public void renderWorld(SpriteBatch batch, ThreatTextures tx) {
        if (boss != null && boss.isActive()) {
            // The boss's name and its three hit points, above it.
            BitmapFont f = Theme.font(54);
            String name = sector.boss == 1 ? "THE MOTHER" : "THE STORM HEART";
            float bx = boss.getPosition().x;
            float by = boss.getPosition().y + 330f;
            text(batch, f, name, bx, by, Theme.ACCENT, 0.9f, true);
            for (int i = 0; i < 3; i++) {
                Theme.rect(batch, bx - 105f + i * 75f, by - 80f, 60f, 16f, i < boss.bossHp ? Theme.ACCENT : Theme.TRACK, 0.9f);
            }
        }
        if (phase != Phase.EXTRACT && phase != Phase.CLEAR) {
            return;
        }
        float pulse = 0.75f + 0.25f * MathUtils.sin(time * 3f);
        float r = BEACON_RADIUS;
        batch.setColor(WARM.r, WARM.g, WARM.b, 0.12f * pulse);
        batch.draw(tx.dot, beacon.x - r * 2f, beacon.y - r * 2f, r * 4f, r * 4f);
        batch.setColor(WARM.r, WARM.g, WARM.b, 0.8f * pulse);
        batch.draw(tx.ring, beacon.x - r, beacon.y - r, r * 2f, r * 2f);
        for (int i = 0; i < 3; i++) {
            float k = ((time * 0.5f + i / 3f) % 1f);
            float rr = r * (0.3f + 1.4f * k);
            batch.setColor(WARM.r, WARM.g, WARM.b, 0.5f * (1f - k));
            batch.draw(tx.ring, beacon.x - rr, beacon.y - rr, rr * 2f, rr * 2f);
        }
        batch.setColor(1f, 0.95f, 0.85f, pulse);
        batch.draw(tx.dot, beacon.x - 50f, beacon.y - 50f, 100f, 100f);
        batch.setColor(Color.WHITE);
    }

    // ------------------------------------------------------------------ screen pass

    /** Goal line, beacon marker and the briefing card (UI camera, 1920-high world units). */
    public void renderHud(SpriteBatch batch, Camera worldCamera, ThreatTextures tx, BitmapFont font, BitmapFont small) {
        float W = Rules.WORLD_WIDTH;
        float H = Rules.WORLD_HEIGHT;
        float top = H - Rules.SAFE_Y;

        // Objective, top centre under the lives.
        String line;
        Color c = Theme.TEXT;
        if (phase == Phase.EXTRACT) {
            line = "REACH THE BEACON";
            c = WARM;
        } else if (phase == Phase.CLEAR) {
            line = "SECTOR CLEAR";
            c = Theme.OK;
        } else {
            line = sector.goalText() + "   " + progress() + " / " + sector.target;
        }
        text(batch, small, "SECTOR " + sector.code() + "  " + sector.name, W / 2f, top - 100f, Theme.DIM, 1f, true);
        text(batch, font, line, W / 2f, top - 100f - small.getLineHeight() - 6f, c, 1f, true);
        if (phase == Phase.ACTIVE || phase == Phase.EXTRACT) {
            com.buran.game.ui.hud.Comms.render(batch, small, W / 2f, top - 100f - small.getLineHeight() - font.getLineHeight() - 70f);
        }
        if (laserStage == 2 && laserBox != null && laserBox.isActive() && !laserBox.isTaken()) {
            float dx = laserBox.getPosition().x - worldCamera.position.x;
            float dy = laserBox.getPosition().y - worldCamera.position.y;
            if (!com.buran.game.world.EdgeMarker.inView(dx, dy, 0f)) {
                com.buran.game.world.EdgeMarker.draw(batch, tx, small, tx.diamond, dx, dy,
                        laserBox.getPosition().dst(Buran.position), WARM, 1f, 0.7f + 0.3f * MathUtils.sin(time * 4f));
            }
        }

        if (phase == Phase.ACTIVE && sector.goal == Sector.Goal.RESCUE) {
            crewMarkers(batch, worldCamera, tx, Theme.font(32));
        }
        if (phase == Phase.EXTRACT) {
            edgeMarker(batch, worldCamera, tx, Theme.font(32));
        }
        if (boss != null && boss.isActive()) {
            // Where the boss is when it is off screen: a red pin, always (tips 53, 69).
            float dx = boss.getPosition().x - worldCamera.position.x;
            float dy = boss.getPosition().y - worldCamera.position.y;
            if (!com.buran.game.world.EdgeMarker.inView(dx, dy, 0f)) {
                float pulse = 0.7f + 0.3f * MathUtils.sin(time * 6f);
                com.buran.game.world.EdgeMarker.draw(batch, tx, Theme.font(32), tx.diamond, dx, dy,
                        boss.getPosition().dst(Buran.position), Theme.ACCENT, 1f, pulse);
            }
        }
        if (phase == Phase.BRIEFING) {
            renderBriefing(batch, small, MathUtils.clamp(phaseTime / 0.5f, 0f, 1f));
        } else if (phase == Phase.ACTIVE && phaseTime < CARD_FADE) {
            renderBriefing(batch, small, 1f - phaseTime / CARD_FADE);
        }
    }

    private void renderBriefing(SpriteBatch batch, BitmapFont font, float a) {
        float W = Rules.WORLD_WIDTH;
        float H = Rules.WORLD_HEIGHT;
        com.badlogic.gdx.graphics.Texture card = com.buran.game.ui.menu.Art.sector(sector.number);
        float w = Math.min(W * (card != null ? 0.72f : 0.62f), card != null ? 2100f : 1700f);
        float x = (W - w) / 2f;
        float y = H * 0.30f;
        // The sector's painted card on the left of the radio panel, the message to its right.
        float cardW = card != null ? 560f : 0f;
        float cardH = cardW * 9f / 16f;
        float tx = x + 40f + (card != null ? cardW + 40f : 0f);
        float tw = w - (tx - x) - 40f;
        int n = phase == Phase.BRIEFING ? typed() : sector.briefing.length();
        boolean ready;
        float h;
        if (subtitles) {
            String shown = sector.briefing.substring(0, n) + ((((int) (time * 2.5f)) % 2 == 0 || n < sector.briefing.length()) ? "_" : "");
            layout.setText(font, sector.briefing, Theme.DIM, tw, Align.left, true);
            h = Math.max(layout.height + 240f, cardH + 80f);
            y = Math.max(y, com.buran.game.world.EdgeMarker.bottomEdge() + h + 130f);
            Theme.frame(batch, x, y - h, w, h, a);
            Color old = font.getColor().cpy();
            font.setColor(Theme.TEXT.r, Theme.TEXT.g, Theme.TEXT.b, a);
            font.draw(batch, shown, tx, y - 30f - font.getLineHeight() * 1.5f, tw, Align.left, true);
            font.setColor(old);
            ready = n >= sector.briefing.length();
        } else {
            // No text: a live signal meter while the voice is on the air.
            h = Math.max(300f, cardH + 80f);
            y = Math.max(y, com.buran.game.world.EdgeMarker.bottomEdge() + h + 130f);
            Theme.frame(batch, x, y - h, w, h, a);
            boolean onAir = com.buran.game.platform.Radio.isPlaying();
            int bars = 48;
            float bw = tw / bars;
            float mid = y - h / 2f - 20f;
            for (int i = 0; i < bars; i++) {
                float level = onAir
                        ? 0.15f + 0.85f * Math.abs(MathUtils.sin(time * 11f + i * 0.7f) * MathUtils.sin(time * 3.3f + i * 0.23f))
                        : 0.04f;
                float bh = 8f + 110f * level;
                Theme.rect(batch, tx + i * bw + bw * 0.2f, mid - bh / 2f, bw * 0.6f, bh, Theme.ACCENT, a * (onAir ? 0.8f : 0.3f));
            }
            ready = phaseTime > 1.2f;
        }
        if (card != null) {
            float zoom = 1.02f + 0.05f * MathUtils.clamp(time / 20f, 0f, 1f);
            Theme.rect(batch, x + 37f, y - 40f - cardH - 3f, cardW + 6f, cardH + 6f, Theme.EDGE, a);
            com.buran.game.ui.menu.Art.cover(batch, card, x + 40f, y - 40f - cardH, cardW, cardH, zoom, 0.5f, 1f, 1f, 1f, a);
        }
        text(batch, font, "RADIO  -  SECTOR " + sector.code(), tx, y - 30f, Theme.ACCENT, a, false);
        if (sector.lesson != null) {
            float lw = Theme.richWidth(font, sector.lesson);
            Theme.rich(batch, font, sector.lesson, W / 2f - lw / 2f, y - h - 50f - font.getCapHeight() / 2f, Theme.OK, a);
        }
        // Continue prompt once the message is fully on screen.
        if (ready) {
            float blink = 0.55f + 0.45f * MathUtils.sin(time * 4f);
            String k = com.buran.game.ui.menu.Hints.button("A", "ENTER");
            float hw = Theme.hintWidth(font, k, "CONTINUE");
            Theme.hint(batch, font, k, "CONTINUE", x + w - 40f - hw, y - h + 40f + Theme.keyHeight(font) / 2f, Theme.TEXT, a * blink);
        }
    }

    /** Crew drifting out of view: warm markers, the goal pulls the eye (tips 9, 13). */
    /**
     * A field full of people needs no pins. Only when none is in view, one pin points to the
     * nearest, so the ship is never lost.
     */
    private void crewMarkers(SpriteBatch batch, Camera cam, ThreatTextures tx, BitmapFont font) {
        java.util.List<?> crew = ((AstronautEmitter) ObjectPool.EMITTERS.get(Astronaut.class)).getActiveList();
        Astronaut nearest = null;
        float best = Float.MAX_VALUE;
        for (int i = 0; i < crew.size(); i++) {
            Astronaut a = (Astronaut) crew.get(i);
            if (!a.isActive() || a.isTaken()) {
                continue;
            }
            if (com.buran.game.world.EdgeMarker.inView(a.getPosition().x - cam.position.x, a.getPosition().y - cam.position.y, 0f)) {
                return;
            }
            float d = a.getPosition().dst2(Buran.position);
            if (d < best) {
                best = d;
                nearest = a;
            }
        }
        if (nearest == null) {
            return;
        }
        float pulse = 0.75f + 0.25f * MathUtils.sin(time * 3f);
        com.buran.game.world.EdgeMarker.draw(batch, tx, font, tx.disc, nearest.getPosition().x - cam.position.x,
                nearest.getPosition().y - cam.position.y, (float) Math.sqrt(best), WARM, 0.9f, pulse);
    }

    private void edgeMarker(SpriteBatch batch, Camera cam, ThreatTextures tx, BitmapFont font) {
        float dx = beacon.x - cam.position.x;
        float dy = beacon.y - cam.position.y;
        if (com.buran.game.world.EdgeMarker.inView(dx, dy, 0f)) {
            return;
        }
        float pulse = 0.7f + 0.3f * MathUtils.sin(time * 4f);
        com.buran.game.world.EdgeMarker.draw(batch, tx, font, tx.diamond, dx, dy,
                Math.max(0f, beacon.dst(Buran.position) - BEACON_RADIUS), WARM, 1f, pulse);
    }

    private void text(SpriteBatch batch, BitmapFont font, String s, float x, float y, Color c, float a, boolean center) {
        Color old = font.getColor().cpy();
        font.setColor(c.r, c.g, c.b, c.a * a);
        if (center) {
            layout.setText(font, s);
            font.draw(batch, s, x - layout.width / 2f, y);
        } else {
            font.draw(batch, s, x, y);
        }
        font.setColor(old);
    }
}
