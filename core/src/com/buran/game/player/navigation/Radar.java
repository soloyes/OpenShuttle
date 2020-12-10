package com.buran.game.player.navigation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.buran.game.bot.anomaly.AbstractAnomaly;
import com.buran.game.bot.anomaly.CorrosiveAnomaly;
import com.buran.game.bot.anomaly.ElectricalAnomaly;
import com.buran.game.bot.blackhole.Blackhole;
import com.buran.game.bot.blackhole.CircleRenderer;
import com.buran.game.bot.consumable.AbstractConsumable;
import com.buran.game.bot.consumable.Astronaut;
import com.buran.game.bot.consumable.Crate;
import com.buran.game.bot.consumable.Storage;
import com.buran.game.bot.consumable.fuel.Fuel;
import com.buran.game.conf.Buffs;
import com.buran.game.conf.MSConstants;
import com.buran.game.conf.PaintConstants;
import com.buran.game.conf.Rules;
import com.buran.game.effects.BoomBox;
import com.buran.game.effects.Oscillator;
import com.buran.game.player.Buran;
import com.buran.game.ui.groups.PauseGroup;
import com.buran.game.utils.Assets;
import com.buran.game.utils.ListOfListsJoiner;
import com.buran.game.utils.pool.AbstractPoolable;
import com.buran.game.utils.pool.ObjectPool;

/**
 * @author soloyes on 7/7/18.
 */

public class Radar {
    private TextureRegion dot;
    private TextureRegion dot1;
    private TextureRegion dot2;
    private Vector2 tmp1V2;
    private float dotSize;
    private Color color;
    private BoomBox boomBox;
    private Oscillator oscillator;
    private ListOfListsJoiner<AbstractPoolable> poolableJoiner;
    private Color tanColor = new Color(Color.TAN.r, Color.TAN.g, Color.TAN.b, PaintConstants.RADAR_ALPHA);
    private Color greenColor = new Color(Color.GREEN.r, Color.GREEN.g, Color.GREEN.b, PaintConstants.RADAR_ALPHA);
    private Color yellowColor = new Color(Color.YELLOW.r, Color.YELLOW.g, Color.YELLOW.b, PaintConstants.RADAR_ALPHA);
    private Color redColor = new Color(Color.RED.r, Color.RED.g, Color.RED.b, PaintConstants.RADAR_ALPHA);
    private Color purpleColor = new Color(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, PaintConstants.RADAR_ALPHA);
    private Color whiteColor = new Color(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, PaintConstants.RADAR_ALPHA);
    public static boolean isActive;

    public Radar() {
        this.dot1 = Assets.getInstance().getAtlas().findRegion(PaintConstants.RADAR_CONSUMABLE_DOT_REGION);
        this.dot2 = Assets.getInstance().getAtlas().findRegion(PaintConstants.RADAR_ANOMALY_DOT_REGION);
        this.tmp1V2 = new Vector2();
        this.oscillator = new Oscillator();
        this.dotSize = 0.0f;
        this.color = new Color(Color.valueOf(PaintConstants.BACKGROUND_COLOR_HEX));
        this.boomBox = new BoomBox();
        poolableJoiner = new ListOfListsJoiner<AbstractPoolable>(
                ObjectPool.EMITTERS.get(Astronaut.class).getActiveList(),
                ObjectPool.EMITTERS.get(Crate.class).getActiveList(),
                ObjectPool.EMITTERS.get(Fuel.class).getActiveList(),
                ObjectPool.EMITTERS.get(Storage.class).getActiveList(),
                ObjectPool.EMITTERS.get(CorrosiveAnomaly.class).getActiveList(),
                ObjectPool.EMITTERS.get(ElectricalAnomaly.class).getActiveList()
        );
    }

    public void render(SpriteBatch batch) {
        if (isActive || CircleRenderer.scan) {
            if (!Buran.isTaken) {
                drawRadar(batch);
            }
        }
    }

    private void drawRadar(SpriteBatch batch) {
        for (int i = 0; i < poolableJoiner.size(); i++) {
            AbstractPoolable poolable = poolableJoiner.get(i);
            if (poolable instanceof AbstractAnomaly) {
                dot = dot2;
            } else if (poolable instanceof AbstractConsumable) {
                dot = dot1;
            }
            drawPoolable(batch, poolable);
        }
        batch.setColor(1, 1, 1, 1);
    }

    private void drawPoolable(SpriteBatch batch, AbstractPoolable abstractPoolable) {
        float d = Vector2.dst(Buran.position.x, Buran.position.y, abstractPoolable.getPosition().x, abstractPoolable.getPosition().y);
        if (d < 2000.0f) {
            float sc = (1.0f - d / 2000.0f) * 2.0f + 2.0f + dotSize;
            tmp1V2.set(abstractPoolable.getPosition()).sub(Buran.position).nor().scl(d / 2000.0f * 300.0f);
            batch.setColor(setPointColor(abstractPoolable));
            batch.draw(dot, Buran.position.x + tmp1V2.x, Buran.position.y + tmp1V2.y, 8f, 8f, PaintConstants.RADAR_DOT_WIDTH, PaintConstants.RADAR_DOT_HEIGHT, sc, sc, 0);
        }
    }

    private Color setPointColor(AbstractPoolable abstractPoolable) {
        float d = Vector2.dst(Buran.position.x, Buran.position.y, Blackhole.position.x, Blackhole.position.y);
        int c;
        if (d <= Buffs.getBlackholeNoiseRange() && !PauseGroup.isPause()) {
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
        if (isActive || CircleRenderer.scan) {
            updateRadar(dt);
            float d = Vector2.dst(Buran.position.x, Buran.position.y, Blackhole.position.x, Blackhole.position.y);
            if (d <= Buffs.getBlackholeNoiseRange()) {
                if (!Buran.isTaken && !Rules.GAME_OVER) {
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