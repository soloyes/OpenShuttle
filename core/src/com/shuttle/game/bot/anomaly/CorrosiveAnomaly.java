package com.shuttle.game.bot.anomaly;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.conf.VConstants;
import com.shuttle.game.effects.MoveProcessor;
import com.shuttle.game.effects.Oscillator;
import com.shuttle.game.effects.Vibrator;
import com.shuttle.game.player.Standing;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.ui.GameInfoSystem;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.Regions;

import lombok.Getter;
import lombok.Setter;

public class CorrosiveAnomaly extends AbstractAnomaly {
    private float[] probabilities = {1.0f, 0.8f, 0.5f, 0.3f};
    private TextureRegion glowRegion;
    @Getter
    @Setter
    private float slimTimer;
    private float slim;
    private int glowWidth, glowHeight;
    private Oscillator oscillator;
    private boolean touched;

    public CorrosiveAnomaly(GameScreen gs) {
        super(gs);
        this.textureRegions = Regions.split(
                new TextureRegion(
                        Assets.getInstance()
                                .getAtlas()
                                .findRegion(PaintConstants.ANOMALY_CORROSIVE_REGION)
                ),
                3, 4, PaintConstants.ANOMALY_CORROSIVE_FRAMES
        );
        this.glowRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.ANOMALY_GLOW);
        this.width = PaintConstants.ANOMALY_CORROSIVE_WIDTH;
        this.height = PaintConstants.ANOMALY_CORROSIVE_HEIGHT;
        this.glowWidth = PaintConstants.ANOMALY_GLOW_WIDTH;
        this.glowHeight = PaintConstants.ANOMALY_GLOW_HEIGHT;
        this.type = Type.CORROSIVE;
        this.moveProcessor = new MoveProcessor(gs);
        this.oscillator = new Oscillator();
    }

    @Override
    public void init(float x, float y) {
        this.touched = false;
        this.hungryTimer = 0;
        this.slimTimer = 0;
        this.slim = Rules.ANOMALIES_CORROSIVE_SLIM_TIME + MathUtils.random(0, 5);
        this.boomTimer = 0;
        this.boomDelay = MathUtils.random(Rules.ANOMALIES_CORROSIVE_BOOM_MIN_DELAY, Rules.ANOMALIES_CORROSIVE_BOOM_MAX_DELAY);
        this.hungry = true;
        this.angle = MathUtils.random(365);
        this.clockwise = MathUtils.randomSign();
        this.position.set(x, y);
        this.active = true;
        this.born = false;
        this.color.a = PaintConstants.ANOMALY_CORROSIVE_GLOW_OSCILLATOR_MAX;
        float rnd = MathUtils.random(0.0f, 1.0f);
        for (int i = probabilities.length - 1; i > 0; i--) {
            if (rnd < probabilities[i]) {
                this.level = i;
                break;
            }
        }
        this.taken = false;
        this.v = MathUtils.random(
                Rules.ANOMALIES_CORROSIVE_VELOCITY,
                3 * Rules.ANOMALIES_CORROSIVE_VELOCITY
        );
        this.moveProcessor = new MoveProcessor(gs);
        this.spiralRadius = MathUtils.random(20, 60);
        this.scaleX = this.scaleY = 0;
        this.saveScaleX = this.saveScaleY = 0;
        this.markedToBoom = false;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        batch.setColor(color);
        basicRender(batch);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public void update(float dt) {
        basicUpdate(dt);
        scaleAndBorn(dt);

        if (born) {
            if (Gdx.input.isTouched() && !Rules.GAME_OVER && GamePreferences.isChildEnabled()) {
                tmp.set(Gdx.input.getX(), Gdx.input.getY());
                ScreenManager.getInstance().getViewport().unproject(tmp);
                if (tmp.dst(position) < PaintConstants.ANOMALY_CORROSIVE_WIDTH) {
                    hungry = false;
                    touched = true;
                }
            }

            color.a = oscillator.oscillate(
                    color.a, PaintConstants.ANOMALY_CORROSIVE_OSCILLATION_FREQUENCY,
                    PaintConstants.ANOMALY_CORROSIVE_GLOW_OSCILLATOR_MIN, PaintConstants.ANOMALY_CORROSIVE_GLOW_OSCILLATOR_MAX, dt
            );
            if (activated) {
                slimTimer += dt;
            }
            if (slimTimer > slim) {
                slimTimer = 0;
                if (level > 0) {
                    takeDamage(1);
                }
            }

            if (!hungry) {
                hungryTimer += dt;
            }

            if (hungryTimer > Rules.ANOMALIES_CORROSIVE_HUNGRY_TIME) {
                hungryTimer = 0;
                hungry = true;
            }

            if (markedToBoom) {
                boomTimer += dt;
                if (boomTimer >= boomDelay) {
                    destroy();
                    showSprayBoom(gs.getParticleEmitter());
                    boomBox.playSound(MSConstants.SPLASH_ANOMALY);
                    Vibrator.vibrate(VConstants.PLAYER_ANOMALY_CORROSIVE_DESTROY);
                    GameInfoSystem.setup(
                            position.x,
                            position.y,
                            GameInfoSystem.Preset.SCORE + "[GREEN] +" + Rules.ANOMALIES_BASE_SCORE_ADD + "[]",
                            3.0f,
                            true
                    );
                }
            }
            moveProcessor.update(this, dt);
        }
        boundCheck.checkAndHandleBoundsAndDestroy(this);
    }

    private void scaleAndBorn(float dt) {
        if (!taken) {
            moveProcessor.archimedesInit(this);
            if (!touched) {
                scaleX = scaleY += dt;
                saveScaleX = saveScaleY += dt;
                if (scaleX >= 1 + level * 0.15f) {
                    scaleX = saveScaleX = 1 + level * 0.15f;
                    scaleY = saveScaleY = 1 + level * 0.15f;
                    if (!born) {
                        born = true;
                    }
                }
            } else {
                destroy();
                boomBox.playSound(MSConstants.DESTROY_CORROSIVE_ANOMALY);
                Standing.getInstance().addToStatistics(this.type, this.level);
                Standing.getInstance().changeScore(Rules.ANOMALIES_BASE_SCORE_ADD);
                showSprayBoom(gs.getParticleEmitter());
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

    public void takeDamage(int dmg) {
        level -= dmg;
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void basicRender(SpriteBatch batch) {
        batch.draw(
                glowRegion,
                position.x - glowWidth / 2,
                position.y - glowHeight / 2,
                glowWidth / 2, glowHeight / 2,
                glowWidth, glowHeight,
                scaleX, scaleY,
                clockwise * angle
        );

        batch.draw(
                textureRegions[(int) (frame % PaintConstants.ANOMALY_CORROSIVE_FRAMES)],
                position.x - width / 2, position.y - height / 2,
                width / 2, height / 2,
                width, height,
                scaleX, scaleY,
                clockwise * angle
        );

        drawAtRocketPoint(batch);
    }

    public void basicUpdate(float dt) {
        frame += 24 * dt;
        if (frame >= PaintConstants.ANOMALY_CORROSIVE_FRAMES) {
            frame = 0.0f;
        }

        angle += 48 * dt;

        switch (level) {
            case 0:
                color.set(Color.TAN.r, Color.TAN.g, Color.TAN.b, color.a);
                break;
            case 1:
                color.set(Color.GREEN.r, Color.GREEN.g, Color.GREEN.b, color.a);
                break;
            case 2:
                color.set(Color.YELLOW.r, Color.YELLOW.g, Color.YELLOW.b, color.a);
                break;
            case 3:
                color.set(Color.RED.r, Color.RED.g, Color.RED.b, color.a);
                break;
        }
        spreeColor.set(color);
    }

    @Override
    public void destroyWithEffect() {
        super.destroyWithEffect();
        showSprayCollision(gs.getParticleEmitter());
    }
}