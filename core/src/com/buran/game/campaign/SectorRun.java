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
    private static final float TYPE_SPEED = 45f;
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
        ((ElectricalAnomalyEmitter) ObjectPool.EMITTERS.get(ElectricalAnomaly.class)).setAnomaliesMaxCount(sector.electricMax);
        ((RadiationAnomalyEmitter) ObjectPool.EMITTERS.get(RadiationAnomaly.class)).setAnomaliesMaxCount(sector.radiationMax);
        ((MagneticAnomalyEmitter) ObjectPool.EMITTERS.get(MagneticAnomaly.class)).setAnomaliesMaxCount(sector.magneticMax);
        for (Class<?> k : new Class<?>[]{MetalAsteroid.class, MineralAsteroid.class}) {
            AbstractAsteroidEmitter e = (AbstractAsteroidEmitter) ObjectPool.EMITTERS.get(k);
            e.setAsteroidsMaxCount(sector.asteroidMax / 2);
            e.setRespawnTime(sector.asteroidRespawn);
        }
        limit(Astronaut.class, sector.astronautMax);
        limit(com.buran.game.bot.consumable.Crate.class, sector.crateMax);
        limit(com.buran.game.bot.consumable.Storage.class, sector.storageMax);
        limit(com.buran.game.bot.consumable.fuel.Fuel.class, sector.fuelMax);
        if (sector.startWithLaser) {
            Buran.isLaser = true;
            Standing.getInstance().setLaserChargeLevel(Standing.getInstance().getMaxLaserChargeLevel());
        }
        baseline = counter();
        if (Rules.DEBUG_FAST_GOAL) {
            baseline -= sector.target;
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
        return sector.goal == Sector.Goal.RESCUE ? st.getAstronautsTotal() : st.getMinedTotal();
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
                if (!voiced && phaseTime > 0.5f) {
                    voiced = true;
                    com.buran.game.platform.Radio.say(sector.briefing);
                }
                if (com.badlogic.gdx.Gdx.input.justTouched()) {
                    advance();
                }
                break;
            case ACTIVE:
                progress = counter() - baseline;
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

    private boolean voiced;
    private boolean typedAll;

    public boolean isBriefing() {
        return phase == Phase.BRIEFING;
    }

    /** A on the briefing: first press finishes the typing, the next one starts the sector. */
    public void advance() {
        if (phase != Phase.BRIEFING || phaseTime < 0.4f) {
            return;
        }
        if (!typedAll && typed() < sector.briefing.length()) {
            typedAll = true;
            return;
        }
        com.buran.game.platform.Radio.stop();
        boomBox.playSound(MSConstants.UI_MENU);
        setPhase(Phase.ACTIVE);
    }

    private int typed() {
        return typedAll ? sector.briefing.length() : (int) Math.min(sector.briefing.length(), phaseTime * TYPE_SPEED);
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
        float w = Math.min(W * 0.62f, 1700f);
        float x = (W - w) / 2f;
        float y = H * 0.30f;
        int n = phase == Phase.BRIEFING ? typed() : sector.briefing.length();
        String shown = sector.briefing.substring(0, n) + ((((int) (time * 2.5f)) % 2 == 0 || n < sector.briefing.length()) ? "_" : "");
        layout.setText(font, sector.briefing, Theme.DIM, w - 80f, Align.left, true);
        float h = layout.height + 240f;
        Theme.frame(batch, x, y - h, w, h, a);
        text(batch, font, "RADIO  -  SECTOR " + sector.code(), x + 40f, y - 30f, Theme.ACCENT, a, false);
        Color old = font.getColor().cpy();
        font.setColor(Theme.TEXT.r, Theme.TEXT.g, Theme.TEXT.b, a);
        font.draw(batch, shown, x + 40f, y - 30f - font.getLineHeight() * 1.5f, w - 80f, Align.left, true);
        font.setColor(old);
        if (sector.lesson != null) {
            text(batch, font, sector.lesson, W / 2f, y - h - 50f, Theme.OK, a, true);
        }
        // Continue prompt once the message is fully on screen.
        if (n >= sector.briefing.length()) {
            float blink = 0.55f + 0.45f * MathUtils.sin(time * 4f);
            layout.setText(font, "A  -  CONTINUE");
            text(batch, font, "A  -  CONTINUE", x + w - 40f - layout.width, y - h + 40f + layout.height, Theme.ACCENT, a * blink, false);
        }
    }

    private void edgeMarker(SpriteBatch batch, Camera cam, ThreatTextures tx, BitmapFont font) {
        float dx = beacon.x - cam.position.x;
        float dy = beacon.y - cam.position.y;
        if (Math.abs(dx) < Rules.VIEW_WIDTH / 2f && Math.abs(dy) < Rules.VIEW_HEIGHT / 2f) {
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
