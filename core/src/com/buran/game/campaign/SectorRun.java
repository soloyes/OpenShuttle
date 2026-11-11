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
        c.setAnomaliesMaxCount(sector.corrosiveMax);
        c.setRespawnTime(sector.corrosiveRespawn);
        trim(c, sector.corrosiveMax);
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
        if (sector.startScans > 0) {
            com.buran.game.ui.groups.selector.SelectorGroup.getInstance()
                    .addSelector(com.buran.game.labelsticker.Sticker.SCAN, sector.startScans);
        }
        if (sector.goal == Sector.Goal.RESCUE) {
            placeCrew();
        }
        baseline = counter();
        if (Rules.DEBUG_CARD) {
            com.buran.game.ui.hud.CrewCard.show(com.buran.game.labelsticker.Sticker.MEDIC, "MEDIC", "HULL REPAIRED");
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
        int n = Math.min(sector.target, sector.astronautMax);
        float base = MathUtils.random(MathUtils.PI2);
        float margin = 260f;
        for (int i = 0; i < n; i++) {
            float ang = base + i * MathUtils.PI2 / n + MathUtils.random(-0.4f, 0.4f);
            crewSpot(ang, MathUtils.random(0.6f, 0.85f));
            e.emitAt(spot.x, spot.y);
        }
        // Only these people are out there: no one new drifts in after they are aboard.
        e.setConsumablesMaxCount(0);
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
                progress = counter() - baseline;
                script(dt);
                if (progress >= sector.target) {
                    startExtraction();
                }
                break;
            case EXTRACT:
                if (!Rules.GAME_OVER && Buran.position.dst(beacon) < BEACON_RADIUS) {
                    setPhase(Phase.CLEAR);
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
        notice = line;
        noticeTime = seconds;
    }

    private void script(float dt) {
        noticeTime -= dt;
        if (sector.goal == Sector.Goal.RESCUE) {
            crewScript();
        }
        if (sector.number == 2) {
            laserScript(dt);
        }
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
        if (progress + out < sector.target && !Rules.GAME_OVER) {
            crewSpot(MathUtils.random(MathUtils.PI2), 0.8f);
            e.emitAt(spot.x, spot.y);
            say("WE LOST ONE.  ANOTHER BEACON IS CALLING", 6f);
        }
    }

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
                    say("DISCHARGE ON THE SCOPE  -  IT IS COMING FOR US", 5f);
                }
                break;
            case 1:
                laserTimer += dt;
                if (!Buran.isLaser) {
                    removeDischarge();
                    placeLaserBox();
                    com.buran.game.player.navigation.Radar.isActive = true;
                    laserStage = 2;
                    say("THE DISCHARGE BURNED OUR LASER.  A STORAGE IS DRIFTING NEARBY  -  A NEW ONE IS INSIDE", 9f);
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
        if (notice != null && noticeTime > 0f && phase == Phase.ACTIVE) {
            float a = Math.min(1f, noticeTime) * Math.min(1f, (phaseTime) * 2f);
            com.buran.game.ui.hud.Prompt.text(batch, small, notice, W / 2f,
                    top - 100f - small.getLineHeight() - font.getLineHeight() - 70f, a);
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
    private void crewMarkers(SpriteBatch batch, Camera cam, ThreatTextures tx, BitmapFont font) {
        java.util.List<?> crew = ((AstronautEmitter) ObjectPool.EMITTERS.get(Astronaut.class)).getActiveList();
        float pulse = 0.75f + 0.25f * MathUtils.sin(time * 3f);
        for (int i = 0; i < crew.size(); i++) {
            Astronaut a = (Astronaut) crew.get(i);
            if (!a.isActive() || a.isTaken()) {
                continue;
            }
            float dx = a.getPosition().x - cam.position.x;
            float dy = a.getPosition().y - cam.position.y;
            if (com.buran.game.world.EdgeMarker.inView(dx, dy, 0f)) {
                continue;
            }
            com.buran.game.world.EdgeMarker.draw(batch, tx, font, tx.disc, dx, dy,
                    a.getPosition().dst(Buran.position), WARM, 0.9f, pulse);
        }
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
