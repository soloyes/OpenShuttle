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
import com.shuttle.game.effects.particle.ParticleEmitter;
import com.shuttle.game.player.Standing;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.ui.GameInfoSystem;
import com.shuttle.game.ui.groups.mvc.profiles.Profiles;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.Regions;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 9/12/18.
 */

public class RadiationAnomaly extends AbstractAnomaly {
    @Getter
    @Setter
    private boolean glued;
    private float liveTime;
    @Getter
    @Setter
    private boolean eat;
    @Getter
    @Setter
    private float eatTimer;
    private boolean touched;

    public RadiationAnomaly(ParticleEmitter particleEmitter) {
        super(particleEmitter);
        this.particleEmitter = particleEmitter;
        this.textureRegions = Regions.split(
                new TextureRegion(
                        Assets.getInstance()
                                .getAtlas()
                                .findRegion(PaintConstants.ANOMALY_RADIATION_REGION)),
                4, 4, PaintConstants.ANOMALY_RADIATION_FRAMES
        );
        this.width = PaintConstants.ANOMALY_RADIATION_WIDTH;
        this.height = PaintConstants.ANOMALY_RADIATION_HEIGHT;
        this.type = Type.RADIATION;
        this.moveProcessor = new MoveProcessor();
    }

    @Override
    public void init(float x, float y) {
        this.position.set(x, y);
        this.active = true;
        this.activated = true;
        this.hungry = true;
        this.eat = false;
        this.hungryTimer = 0.0f;
        this.born = false;
        this.level = 2;
        this.scaleX = this.saveScaleX = 1.0f;
        this.scaleY = this.saveScaleY = 1.0f;
        this.taken = false;
        this.color.a = 0.0f;
        this.v = MathUtils.random(
                Rules.ANOMALIES_RADIATION_VELOCITY,
                3 * Rules.ANOMALIES_RADIATION_VELOCITY
        );
        this.spiralRadius = MathUtils.random(20, 60);
        this.touched = false;
        this.spreeColor.set(Color.CYAN.r, Color.CYAN.g, Color.CYAN.b, 1.0f);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        batch.setColor(1.0f, 1.0f, 1.0f, color.a);
        basicRender(batch);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
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

        drawAtRocketPoint(batch);
    }

    @Override
    public void basicUpdate(float dt) {
        frame += 12 * dt;
        if (frame >= textureRegions.length) {
            frame = 0.0f;
        }
    }

    @Override
    public void destroyWithEffect() {
        super.destroyWithEffect();
        Standing.getInstance().addToStatistics(this.type, this.level);
        Standing.getInstance().changeScore(Rules.ANOMALIES_BASE_SCORE_ADD);
        showSprayBoom();
    }

    @Override
    public void update(float dt) {
        basicUpdate(dt);
        scaleAndBorn(dt);

        if (born) {
            moveProcessor.update(this, dt);
            if (Gdx.input.isTouched() && !Rules.GAME_OVER && Profiles.getSettings().isChild()) {
                tmp.set(Gdx.input.getX(), Gdx.input.getY());
                ScreenManager.getInstance().getViewport().unproject(tmp);
                if (tmp.dst(position) < PaintConstants.ANOMALY_MAGNETIC_WIDTH) {
                    touched = true;
                }
            }
        }
        boundCheck.checkAndHandleBoundsAndDestroy(this);
    }

    private void scaleAndBorn(float dt) {
        if (!taken) {
            moveProcessor.archimedesInit(this);
            if (!touched) {
                if (!born) {
                    color.a += dt / Rules.ANOMALIES_RADIATION_BORN_SPEED;
                    if (color.a >= 1.0) {
                        color.a = 1.0f;
                        born = true;
                    }
                } else {
                    liveTime += dt;
                    if (liveTime >= Rules.ANOMALIES_RADIATION_LIVE_TIME && !Rules.GAME_OVER) {
                        color.a -= dt;
                    }
                    if (color.a <= 0.0f) {
                        color.a = 0.0f;
                        liveTime = 0.0f;
                        born = false;
                        destroy();
                    }
                }
            } else {
                destroyWithEffect();
                boomBox.playSound(MSConstants.DESTROY_RADIATION_ANOMALY);
                GameInfoSystem.setup(
                        position.x,
                        position.y,
                        GameInfoSystem.Preset.SCORE + "[GREEN] +" + Rules.ANOMALIES_BASE_SCORE_ADD + "[]",
                        3.0f,
                        true
                );
            }
        } else {
            born = true;
        }
    }

    public void addEatTimer(float dt) {
        eatTimer += dt;
    }
}