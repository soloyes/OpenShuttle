package com.buran.game.player.navigation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.buran.game.player.Buran;

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
    private com.buran.game.effects.BoomBox boomBox;
    private Oscillator oscillator;
    private com.buran.game.utils.ListOfListsJoiner<AbstractPoolable> poolableJoiner;
    private Color tanColor = new Color(Color.TAN.r, Color.TAN.g, Color.TAN.b, com.buran.game.conf.PaintConstants.RADAR_ALPHA);
    private Color greenColor = new Color(Color.GREEN.r, Color.GREEN.g, Color.GREEN.b, com.buran.game.conf.PaintConstants.RADAR_ALPHA);
    private Color yellowColor = new Color(Color.YELLOW.r, Color.YELLOW.g, Color.YELLOW.b, com.buran.game.conf.PaintConstants.RADAR_ALPHA);
    private Color redColor = new Color(Color.RED.r, Color.RED.g, Color.RED.b, com.buran.game.conf.PaintConstants.RADAR_ALPHA);
    private Color purpleColor = new Color(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, com.buran.game.conf.PaintConstants.RADAR_ALPHA);
    private Color whiteColor = new Color(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, com.buran.game.conf.PaintConstants.RADAR_ALPHA);
    public static boolean isActive;

    public Radar() {
        this.dot1 = com.buran.game.utils.Assets.getInstance().getAtlas().findRegion(com.buran.game.conf.PaintConstants.RADAR_CONSUMABLE_DOT_REGION);
        this.dot2 = com.buran.game.utils.Assets.getInstance().getAtlas().findRegion(com.buran.game.conf.PaintConstants.RADAR_ANOMALY_DOT_REGION);
        this.tmp1V2 = new Vector2();
        this.oscillator = new Oscillator();
        this.dotSize = 0.0f;
        this.color = new Color(Color.valueOf(com.buran.game.conf.PaintConstants.BACKGROUND_COLOR_HEX));
        this.boomBox = new com.buran.game.effects.BoomBox();
        poolableJoiner = new com.buran.game.utils.ListOfListsJoiner<AbstractPoolable>(
                com.buran.game.utils.pool.ObjectPool.EMITTERS.get(com.buran.game.bot.consumable.Astronaut.class).getActiveList(),
                com.buran.game.utils.pool.ObjectPool.EMITTERS.get(com.buran.game.bot.consumable.Crate.class).getActiveList(),
                com.buran.game.utils.pool.ObjectPool.EMITTERS.get(com.buran.game.bot.consumable.fuel.Fuel.class).getActiveList(),
                com.buran.game.utils.pool.ObjectPool.EMITTERS.get(com.buran.game.bot.consumable.Storage.class).getActiveList(),
                com.buran.game.utils.pool.ObjectPool.EMITTERS.get(com.buran.game.bot.anomaly.CorrosiveAnomaly.class).getActiveList(),
                com.buran.game.utils.pool.ObjectPool.EMITTERS.get(com.buran.game.bot.anomaly.ElectricalAnomaly.class).getActiveList()
        );
    }

    public void render(SpriteBatch batch) {
        if (isActive) {
            if (!Buran.isTaken) {
                drawRadar(batch);
            }
        }
    }

    private void drawRadar(SpriteBatch batch) {
        for (int i = 0; i < poolableJoiner.size(); i++) {
            com.buran.game.utils.pool.AbstractPoolable poolable = poolableJoiner.get(i);
            if (poolable instanceof com.buran.game.bot.anomaly.AbstractAnomaly) {
                dot = dot2;
            } else if (poolable instanceof com.buran.game.bot.consumable.AbstractConsumable) {
                dot = dot1;
            }
            drawPoolable(batch, poolable);
        }
        batch.setColor(1, 1, 1, 1);
    }

    private void drawPoolable(SpriteBatch batch, com.buran.game.utils.pool.AbstractPoolable abstractPoolable) {
        float d = Vector2.dst(Buran.position.x, Buran.position.y, abstractPoolable.getPosition().x, abstractPoolable.getPosition().y);
        if (d < 2000.0f) {
            float sc = (1.0f - d / 2000.0f) * 2.0f + 2.0f + dotSize;
            tmp1V2.set(abstractPoolable.getPosition()).sub(Buran.position).nor().scl(d / 2000.0f * 300.0f);
            batch.setColor(setPointColor(abstractPoolable));
            batch.draw(dot, Buran.position.x + tmp1V2.x, Buran.position.y + tmp1V2.y, 8f, 8f, com.buran.game.conf.PaintConstants.RADAR_DOT_WIDTH, com.buran.game.conf.PaintConstants.RADAR_DOT_HEIGHT, sc, sc, 0);
        }
    }

    private Color setPointColor(com.buran.game.utils.pool.AbstractPoolable abstractPoolable) {
        float d = Vector2.dst(Buran.position.x, Buran.position.y, com.buran.game.bot.blackhole.Blackhole.position.x, com.buran.game.bot.blackhole.Blackhole.position.y);
        int c;
        if (d <= com.buran.game.conf.Rules.BLACKHOLE_NOISE_RANGE && !com.buran.game.ui.groups.PauseGroup.isPause()) {
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
        if (isActive) {
            updateRadar(dt);
            float d = Vector2.dst(Buran.position.x, Buran.position.y, com.buran.game.bot.blackhole.Blackhole.position.x, com.buran.game.bot.blackhole.Blackhole.position.y);
            if (d <= com.buran.game.conf.Rules.BLACKHOLE_NOISE_RANGE) {
                if (!Buran.isTaken && !com.buran.game.conf.Rules.GAME_OVER) {
                    boomBox.playMusicEffectInLoop(com.buran.game.conf.MSConstants.RADAR_WHITE_NOISE_MUSIC, 1.0f);
                } else {
                    boomBox.stopMusicEffect(com.buran.game.conf.MSConstants.RADAR_WHITE_NOISE_MUSIC);
                }
            } else {
                boomBox.stopMusicEffect(com.buran.game.conf.MSConstants.RADAR_WHITE_NOISE_MUSIC);
            }
        } else {
            boomBox.stopMusicEffect(com.buran.game.conf.MSConstants.RADAR_WHITE_NOISE_MUSIC);
        }
    }

    private void updateRadar(float dt) {
        dotSize = oscillator.oscillate(dotSize, com.buran.game.conf.PaintConstants.RADAR_OSCILLATION_FREQUENCY, com.buran.game.conf.PaintConstants.RADAR_GLOW_OSCILLATOR_MIN, com.buran.game.conf.PaintConstants.RADAR_GLOW_OSCILLATOR_MAX, dt);
    }
}