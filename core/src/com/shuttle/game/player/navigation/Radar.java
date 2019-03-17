package com.shuttle.game.player.navigation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.anomaly.CorrosiveAnomaly;
import com.shuttle.game.bot.anomaly.ElectricalAnomaly;
import com.shuttle.game.bot.consumable.AbstractConsumable;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.effects.Oscillator;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.ActiveListJoiner;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.pool.AbstractPoolable;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 7/7/18.
 */
public class Radar {
    protected TextureRegion dot;
    private GameScreen gs;
    private TextureRegion dot1;
    private TextureRegion dot2;
    private Vector2 tmp;
    @Getter
    @Setter
    private boolean active;
    private float dotSize;
    private Shuttle player;
    private Color color;
    private BoomBox boomBox;
    private Oscillator oscillator;
    private List<List<? extends AbstractConsumable>> listsActiveConsumable;

    private Color tanColor = new Color(Color.TAN.r, Color.TAN.g, Color.TAN.b, PaintConstants.RADAR_ALPHA);
    private Color greenColor = new Color(Color.GREEN.r, Color.GREEN.g, Color.GREEN.b, PaintConstants.RADAR_ALPHA);
    private Color yellowColor = new Color(Color.YELLOW.r, Color.YELLOW.g, Color.YELLOW.b, PaintConstants.RADAR_ALPHA);
    private Color redColor = new Color(Color.RED.r, Color.RED.g, Color.RED.b, PaintConstants.RADAR_ALPHA);
    private Color purpleColor = new Color(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, PaintConstants.RADAR_ALPHA);
    private Color whiteColor = new Color(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, PaintConstants.RADAR_ALPHA);

    public Radar(GameScreen gs, Shuttle player) {
        this.gs = gs;
        this.dot1 = Assets.getInstance().getAtlas().findRegion(PaintConstants.RADAR_CONSUMABLE_DOT_REGION);
        this.dot2 = Assets.getInstance().getAtlas().findRegion(PaintConstants.RADAR_ANOMALY_DOT_REGION);
        this.player = player;
        this.tmp = new Vector2();
        this.oscillator = new Oscillator();
        this.dotSize = 0.0f;
        this.color = new Color(Color.valueOf(PaintConstants.BACKGROUND_COLOR_HEX));
        this.boomBox = new BoomBox();
        ActiveListJoiner<AbstractConsumable> joiner = new ActiveListJoiner<AbstractConsumable>();
        listsActiveConsumable = joiner.join(
                gs.getAstronautEmitter().getActiveList(),
                gs.getCrateEmitter().getActiveList(),
                gs.getFuelEmitter().getActiveList(),
                gs.getStorageEmitter().getActiveList()
        );
    }

    public void render(SpriteBatch batch) {
        if (active) {
            if (!player.isTaken()) {
                drawRadar(batch);
            }
        }
    }

    private void drawRadar(SpriteBatch batch) {
        for (int i = 0; i < gs.getCorrosiveAnomalyEmitter().getActiveList().size(); i++) {
            CorrosiveAnomaly tmpAnomaly = (CorrosiveAnomaly) gs.getCorrosiveAnomalyEmitter().getActiveList().get(i);
            dot = dot2;
            drawPoolable(batch, tmpAnomaly);
        }

        for (int i = 0; i < listsActiveConsumable.size(); i++) {
            for (int j = 0; j < listsActiveConsumable.get(i).size(); j++) {
                AbstractConsumable tmpConsumable = listsActiveConsumable.get(i).get(j);
                dot = dot1;
                drawPoolable(batch, tmpConsumable);
            }
        }

        for (int i = 0; i < gs.getElectricalAnomalyEmitter().getActiveList().size(); i++) {
            ElectricalAnomaly tmpAnomaly = (ElectricalAnomaly) gs.getElectricalAnomalyEmitter().getActiveList().get(i);
            dot = dot2;
            drawPoolable(batch, tmpAnomaly);
        }
        batch.setColor(1, 1, 1, 1);
    }

    private void drawPoolable(SpriteBatch batch, AbstractPoolable abstractPoolable) {
        float d = Vector2.dst(Shuttle.position.x, Shuttle.position.y, abstractPoolable.getPosition().x, abstractPoolable.getPosition().y);
        if (d < 2000.0f) {
            float sc = (1.0f - d / 2000.0f) * 2.0f + 2.0f + dotSize;
            tmp.set(abstractPoolable.getPosition()).sub(Shuttle.position).nor().scl(d / 2000.0f * 300.0f);
            batch.setColor(setPointColor(abstractPoolable));
            batch.draw(dot, Shuttle.position.x + tmp.x, Shuttle.position.y + tmp.y, 8f, 8f, PaintConstants.RADAR_DOT_WIDTH, PaintConstants.RADAR_DOT_HEIGHT, sc, sc, 0);
        }
    }

    private Color setPointColor(AbstractPoolable abstractPoolable) {
        float d = Vector2.dst(Shuttle.position.x, Shuttle.position.y, gs.getBlackhole().getPosition().x, gs.getBlackhole().getPosition().y);
        int c;
        if (d <= Rules.BLACKHOLE_NOISE_RANGE && !gs.getPauseGroup().isPause()) {
            c = MathUtils.random(0, 4);
            if (MathUtils.random(0, 100) <= 60) {
                return color;
            }
        } else {
            c = abstractPoolable.getLevel();
        }
        switch (c) {
            case 0:
                return tanColor;
            case 1:
                return greenColor;
            case 2:
                return yellowColor;
            case 3:
                return redColor;
            case 4:
                return purpleColor;
            default:
                return whiteColor;
        }
    }

    public void update(float dt) {
        if (active) {
            updateRadar(dt);
            float d = Vector2.dst(Shuttle.position.x, Shuttle.position.y, gs.getBlackhole().getPosition().x, gs.getBlackhole().getPosition().y);
            if (d <= Rules.BLACKHOLE_NOISE_RANGE) {
                if (!player.isTaken() && !Rules.GAME_OVER) {
                    boomBox.playMusicEffectInLoop(MSConstants.RADAR_WHITE_NOISE_MUSIC, 1.0f);
                } else {
                    boomBox.stopMusicEffect(MSConstants.RADAR_WHITE_NOISE_MUSIC);
                }
            } else {
                boomBox.stopMusicEffect(MSConstants.RADAR_WHITE_NOISE_MUSIC);
            }
        } else {
            boomBox.stopMusicEffect(MSConstants.RADAR_WHITE_NOISE_MUSIC);
        }
    }

    private void updateRadar(float dt) {
        dotSize = oscillator.oscillate(dotSize, PaintConstants.RADAR_OSCILLATION_FREQUENCY, PaintConstants.RADAR_GLOW_OSCILLATOR_MIN, PaintConstants.RADAR_GLOW_OSCILLATOR_MAX, dt);
    }
}