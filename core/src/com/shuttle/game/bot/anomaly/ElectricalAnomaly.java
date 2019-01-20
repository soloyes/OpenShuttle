package com.shuttle.game.bot.anomaly;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.effects.MoveProcessor;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.ui.GameInfoSystem;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.Regions;

/**
 * @author Shuttle on 9/11/18.
 */
public class ElectricalAnomaly extends AbstractAnomaly {
    private boolean touched;

    public ElectricalAnomaly(GameScreen gs) {
        super(gs);
        this.textureRegions = Regions.split(
                new TextureRegion(
                        Assets.getInstance()
                                .getAtlas()
                                .findRegion(PaintConstants.ANOMALY_ELECTRIC_REGION)),
                4, 4, 16
        );
        this.width = PaintConstants.ANOMALY_ELECTRIC_WIDTH;
        this.height = PaintConstants.ANOMALY_ELECTRIC_HEIGHT;
        this.type = Type.ELECTRIC;
        this.moveProcessor = new MoveProcessor(gs);
    }

    @Override
    public void init(float x, float y) {
        this.touched = false;
        this.hungry = true;
        this.hungryTimer = 0;
        this.angle = MathUtils.random(365);
        this.clockwise = MathUtils.randomSign();
        this.position.set(x, y);
        this.active = true;
        this.activated = true;
        this.born = false;
        this.level = 4;
        this.color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        this.spreeColor.set(Color.PURPLE.r, Color.PURPLE.g, Color.PURPLE.b, 1.0f);
        this.color.a = 0.0f;
        this.scaleX = this.saveScaleX = 1.0f;
        this.scaleY = this.saveScaleY = 1.0f;
        this.taken = false;
        this.v = Rules.ANOMALY_ELECTRIC_VELOCITY;
        this.spiralRadius = MathUtils.random(20, 60);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        batch.setColor(1.0f, 1.0f, 1.0f, color.a);
        basicRender(batch);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public void basicRender(SpriteBatch batch) {
        batch.draw(
                textureRegions[(int) (frame % textureRegions.length)],
                position.x - width / 2, position.y - height / 2,
                width / 2, height / 2,
                width, height,
                scaleX, scaleY,
                clockwise * angle
        );
    }

    @Override
    public void basicUpdate(float dt) {
        frame += 24 * dt;
        angle += 48 * dt;
        if (frame >= textureRegions.length)
            frame = 0.0f;
        if (angle >= 365)
            angle = 0;
    }

    @Override
    public void update(float dt) {
        basicUpdate(dt);
        scaleAndBorn(dt);

        if (born) {
            if (Gdx.input.isTouched() && !Rules.GAME_OVER) {
                tmp.set(Gdx.input.getX(), Gdx.input.getY());
                ScreenManager.getInstance().getViewport().unproject(tmp);
                if (tmp.dst(position) < PaintConstants.ANOMALY_ELECTRIC_WIDTH) {
                    hungry = false;
                    touched = true;
                }
            }

            if (!hungry) {
                hungryTimer += dt;
            }

            if (hungryTimer > Rules.ANOMALIES_ELECTRIC_HUNGRY_TIME) {
                hungryTimer = 0;
                hungry = true;
            }

            moveProcessor.update(this, dt);
        }
    }

    private void scaleAndBorn(float dt) {
        if (!taken) {
            moveProcessor.archimedesInit(this);
            if (!touched) {
                color.a += dt / 5;
                if (color.a >= 1.0) {
                    color.a = 1.0f;
                    if (!born) {
                        born = true;
                    }
                }
            } else {
                destroy();
                boomBox.playSound(MSConstants.DESTROY_ELECTRIC_ANOMALY);
                gs.getPlayer().getStanding().addToStatistics(this.type, this.level);
                showSprayBoom(gs.getParticleEmitter());
                //score amount decrease when kicked
                int score = (int) (level * Rules.ANOMALIES_BASE_SCORE_ADD * (v / Rules.ANOMALY_ELECTRIC_VELOCITY));
                gs.getGameInfoSystem().setup(
                        position.x,
                        position.y,
                        GameInfoSystem.GameInfoSystemPreset.SCORE + "[GREEN] +" + score + "[]",
                        3.0f,
                        true
                );
                gs.getPlayer().getStanding().changeScore(score);
            }
        } else {
            born = true;
        }
    }
}
