package com.buran.game.bot.anomaly;

import com.buran.game.input.Gamepad;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.buran.game.conf.DeathMode;
import com.buran.game.conf.MSConstants;
import com.buran.game.conf.PaintConstants;
import com.buran.game.conf.Rules;
import com.buran.game.conf.VConstants;
import com.buran.game.effects.MoveProcessor;
import com.buran.game.effects.Vibrator;
import com.buran.game.effects.particle.ParticleEmitter;
import com.buran.game.labelsticker.Sticker;
import com.buran.game.player.Standing;
import com.buran.game.screens.ScreenManager;
import com.buran.game.ui.GameInfoSystem;
import com.buran.game.ui.groups.mvc.profiles.Profiles;
import com.buran.game.utils.Assets;
import com.buran.game.utils.Regions;

import lombok.Getter;
import lombok.Setter;

/**
 * @author soloyes on 9/12/18.
 */

public class MagneticAnomaly extends AbstractAnomaly {
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

    public MagneticAnomaly(ParticleEmitter particleEmitter) {
        super(particleEmitter);
        this.particleEmitter = particleEmitter;
        this.textureRegions = Regions.split(
                new TextureRegion(
                        Assets.getInstance()
                                .getAtlas()
                                .findRegion(PaintConstants.ANOMALY_MAGNETIC_REGION)),
                4, 4, PaintConstants.ANOMALY_MAGNETIC_FRAMES
        );
        this.width = PaintConstants.ANOMALY_MAGNETIC_WIDTH;
        this.height = PaintConstants.ANOMALY_MAGNETIC_HEIGHT;
        this.type = Type.MAGNETIC;
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
        this.liveTime = 0.0f;
        this.born = false;
        this.level = 2;
        this.scaleX = this.saveScaleX = 1.0f;
        this.scaleY = this.saveScaleY = 1.0f;
        this.taken = false;
        this.color.a = 0.0f;
        this.angle = MathUtils.random(365);
        this.clockwise = MathUtils.randomSign();
        this.v = MathUtils.random(
                DeathMode.magneticAnomalyVelocity(),
                3 * DeathMode.magneticAnomalyVelocity()
        );
        this.spiralRadius = MathUtils.random(20, 60);
        this.touched = false;
        this.spreeColor.set(Color.GOLDENROD.r, Color.GOLDENROD.g, Color.GOLDENROD.b, 1.0f);
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

        drawAtRocketPoint(batch);
    }

    @Override
    public void basicUpdate(float dt) {
        frame += 24 * dt;
        if (!eat) {
            angle += 48 * dt;
        }
        if (frame >= textureRegions.length)
            frame = 0.0f;
    }

    @Override
    public void update(float dt) {
        basicUpdate(dt);
        scaleAndBorn(dt);

        if (born) {
            moveProcessor.update(this, dt);
            if (!Rules.GAME_OVER && Profiles.getSettings().isChild() && (Gdx.input.isTouched() || Gamepad.pulseHits(position))) {
                tmp.set(Gdx.input.getX(), Gdx.input.getY());
                ScreenManager.getInstance().getViewport().unproject(tmp);
                if (Gamepad.pulseHits(position) || (Gdx.input.isTouched() && tmp.dst(position) < PaintConstants.ANOMALY_MAGNETIC_WIDTH)) {
                    touched = true;
                    if (!taken) {
                        Vibrator.vibrateGame(VConstants.PLAYER_ANOMALY_MAGNETIC_DESTROY);
                    }
                }
            }

            if (!hungry) {
                hungryTimer += dt;
            }

            if (hungryTimer > Rules.ANOMALIES_MAGNETIC_HUNGRY_TIME) {
                hungryTimer = 0;
                hungry = true;
            }
        }
        boundCheck.checkAndHandleBoundsAndDestroy(this);
    }

    private void scaleAndBorn(float dt) {
        if (!taken) {
            moveProcessor.archimedesInit(this);
            if (!touched) {
                if (!born) {
                    color.a += dt / Rules.ANOMALIES_MAGNETIC_BORN_SPEED;
                    if (color.a >= 1.0) {
                        color.a = 1.0f;
                        born = true;
                    }
                } else {
                    liveTime += dt;
                    if (liveTime >= Rules.ANOMALIES_MAGNETIC_LIVE_TIME && !Rules.GAME_OVER) {
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
                boomBox.playSound(MSConstants.DESTROY_MAGNETIC_ANOMALY);
            }
        } else {
            born = true;
        }
    }

    public void addEatTimer(float dt) {
        eatTimer += dt;
    }

    @Override
    public void destroyWithEffect() {
        super.destroyWithEffect();
        Standing.getInstance().addToStatistics(this.type, this.level);
        int score = (int) (Rules.ANOMALIES_BASE_SCORE_ADD * Sticker.COMMANDER.getCoefficient());
        Standing.getInstance().changeScore(score, position, GameInfoSystem.Preset.SCORE + "[GREEN] +" + score + "[]", 3.0f, true);
        showSprayBoom();
    }
}