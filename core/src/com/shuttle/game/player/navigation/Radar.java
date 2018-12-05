package com.shuttle.game.player.navigation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.anomaly.CorrosiveAnomaly;
import com.shuttle.game.bot.anomaly.ElectricalAnomaly;
import com.shuttle.game.bot.consumable.AbstractConsumable;
import com.shuttle.game.bot.pool.AbstractPoolable;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.ActiveListJoiner;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.effects.BoomBox;
import com.shuttle.game.utils.effects.Oscillator;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 7/7/18.
 */
public class Radar {
    private GameScreen gs;
    private TextureRegion backgroundTexture;
    private TextureRegion pointTexture;
    @Getter
    @Setter
    private Vector2 position;
    private Vector2 tmp;
    @Getter
    @Setter
    private boolean active;
    private int width, height;
    private float scaleX, scaleY;
    private float spin;
    private float dotSize;
    private Shuttle player;
    private Color color;
    private BoomBox boomBox;
    private Oscillator oscillator;
    private List<List<? extends AbstractConsumable>> listsActiveConsumable;

    public Radar(GameScreen gs, Shuttle player) {
        this.gs = gs;
        this.backgroundTexture = Assets.getInstance().getAtlas().findRegion(PaintConstants.RADAR_REGION);
        this.pointTexture = Assets.getInstance().getAtlas().findRegion(PaintConstants.STAR16_REGION);
        this.player = player;
        this.position = new Vector2();
        this.tmp = new Vector2();
        this.width = PaintConstants.RADAR_WIDTH;
        this.height = PaintConstants.RADAR_HEIGHT;
        this.scaleX = this.scaleY = 1;
        this.generatePosition();
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

    //Prevent generation near blackhole
    public final void generatePosition() {
        float x = MathUtils.random(0, Rules.SPACE_WIDTH);
        float y = MathUtils.random(0, Rules.SPACE_HEIGHT);
        float r1 = gs.getBlackhole().getRadius().len();
        float x0 = gs.getBlackhole().getCenter().x;
        float y0 = gs.getBlackhole().getCenter().y;
        while (true) {
            float r2 = (float) Math.sqrt(Math.abs(Math.pow(x - x0, 2) + Math.pow(y - y0, 2)));
            if (Math.abs(r1 - r2) >= Rules.BLACKHOLE_TAKE_RANGE) {
                break;
            }
            x = MathUtils.random(0, Rules.SPACE_WIDTH);
            y = MathUtils.random(0, Rules.SPACE_HEIGHT);
        }
        position.set(x, y);
    }

    public void render(SpriteBatch batch) {
        if (active) {
            if (!player.isTaken()) {
                drawRadar(batch);
            }
        } else {
            batch.draw(
                    backgroundTexture,
                    position.x - width / 2, position.y - height / 2,
                    width / 2, height / 2,
                    width, height, scaleX, scaleY,
                    0.0f
            );
        }
    }

    private void drawRadar(SpriteBatch batch) {
        for (int i = 0; i < gs.getCorrosiveAnomalyEmitter().getActiveList().size(); i++) {
            CorrosiveAnomaly tmpAnomaly = (CorrosiveAnomaly) gs.getCorrosiveAnomalyEmitter().getActiveList().get(i);
            drawPoolable(batch, tmpAnomaly);
        }

        for (int i = 0; i < listsActiveConsumable.size(); i++) {
            for (int j = 0; j < listsActiveConsumable.get(i).size(); j++) {
                AbstractConsumable tmpConsumable = listsActiveConsumable.get(i).get(j);
                drawPoolable(batch, tmpConsumable);
            }
        }

        for (int i = 0; i < gs.getElectricalAnomalyEmitter().getActiveList().size(); i++) {
            ElectricalAnomaly tmpAnomaly = (ElectricalAnomaly) gs.getElectricalAnomalyEmitter().getActiveList().get(i);
            drawPoolable(batch, tmpAnomaly);
        }

        batch.setColor(1, 1, 1, 1);
    }

    private void drawPoolable(SpriteBatch batch, AbstractPoolable abstractPoolable) {
        float d = Vector2.dst(player.getPosition().x, player.getPosition().y, abstractPoolable.getPosition().x, abstractPoolable.getPosition().y);
        if (d < 2000.0f) {
            float sc = (1.0f - d / 2000.0f) * 2.0f + 2.0f + dotSize;
            tmp.set(abstractPoolable.getPosition()).sub(player.getPosition()).nor().scl(d / 2000.0f * 300.0f);
            batch.setColor(setPointColor(abstractPoolable));
            batch.draw(pointTexture, player.getPosition().x + tmp.x, player.getPosition().y + tmp.y, 8f, 8f, 16, 16, sc, sc, 0);
        }
    }

    private Color setPointColor(AbstractPoolable abstractPoolable) {
        float d = Vector2.dst(player.getPosition().x, player.getPosition().y, gs.getBlackhole().getPosition().x, gs.getBlackhole().getPosition().y);
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
                return Color.TAN;
            case 1:
                return Color.GREEN;
            case 2:
                return Color.YELLOW;
            case 3:
                return Color.RED;
            case 4:
                return Color.PURPLE;
            default:
                return Color.WHITE;
        }
    }

    public void update(float dt) {
        if (active) {
            updateRadar(dt);
            float d = Vector2.dst(player.getPosition().x, player.getPosition().y, gs.getBlackhole().getPosition().x, gs.getBlackhole().getPosition().y);
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
            spin += dt;
            scaleX = (float) Math.sin(Math.toRadians(spin * 100));
            boomBox.stopMusicEffect(MSConstants.RADAR_WHITE_NOISE_MUSIC);
        }

    }

    private void updateRadar(float dt) {
        dotSize = oscillator.oscillate(dotSize, PaintConstants.RADAR_OSCILLATION_FREQUENCY, PaintConstants.RADAR_GLOW_OSCILLATOR_MIN, PaintConstants.RADAR_GLOW_OSCILLATOR_MAX, dt);
    }
}