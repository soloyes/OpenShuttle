package com.shuttle.game.bot.anomaly;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.Regions;

import lombok.Getter;
import lombok.Setter;

public class CorrosiveAnomaly extends AbstractAnomaly {
    private float[] probabilities = {1.0f, 0.8f, 0.5f, 0.3f};
    private TextureRegion glowRegion;
    @Getter
    @Setter
    private float slimTimer;
    private int glowWidth, glowHeight;
    private boolean oscillator;

    public CorrosiveAnomaly(GameScreen gs) {
        super(gs);
        this.textureRegions = Regions.split(
                new TextureRegion(
                        Assets.getInstance()
                                .getAtlas()
                                .findRegion(PaintConstants.ANOMALY_CORROSIVE_REGION)
                ),
                3,
                4,
                PaintConstants.ANOMALY_CORROSIVE_FRAMES
        );
        this.glowRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.ANOMALY_GLOW);
        this.width = PaintConstants.ANOMALY_CORROSIVE_WIDTH;
        this.height = PaintConstants.ANOMALY_CORROSIVE_HEIGHT;
        this.glowWidth = PaintConstants.ANOMALY_GLOW_WIDTH;
        this.glowHeight = PaintConstants.ANOMALY_GLOW_HEIGHT;
        this.type = Type.CORROSIVE;
    }

    @Override
    public void init(float x, float y) {
        this.hungryTimer = 0;
        this.slimTimer = 0;
        this.boomTimer = 0;
        this.boomDelay = MathUtils.random(Rules.ANOMALIES_BOOM_MIN_DELAY, Rules.ANOMALIES_BOOM_MAX_DELAY);
        this.hungry = true;
        this.angle = MathUtils.random(365);
        this.clockwise = MathUtils.randomSign();
        this.position.set(x, y);
        this.active = true;
        this.born = false;
        this.oscillator = true;
        this.color.a = PaintConstants.ANOMALY_GLOW_OSCILLATOR_MAX;
        float rnd = MathUtils.random(0.0f, 1.0f);
        for (int i = probabilities.length - 1; i > 0; i--) {
            if (rnd < probabilities[i]) {
                level = i;
                break;
            }
        }
        this.taken = true;
        this.v = MathUtils.random(
                Rules.ANOMALIES_CORROSIVE_VELOCITY,
                3 * Rules.ANOMALIES_CORROSIVE_VELOCITY
        );
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
        scaleAndBorn(dt);
        basicUpdate(dt);

        if (born) {
            if (oscillator) {
                color.a -= dt / 2;
                if (color.a <= 0.0f) {
                    color.a = 0.0f;
                    oscillator = false;
                }
            } else {
                color.a += dt / 2;
                if (color.a >= PaintConstants.ANOMALY_GLOW_OSCILLATOR_MAX) {
                    color.a = PaintConstants.ANOMALY_GLOW_OSCILLATOR_MAX;
                    oscillator = true;
                }
            }

            boundCheck.checkAndHandleBoundsAndDestroy(this);

            if (activated) {
                slimTimer += dt;
            }
            if (slimTimer > Rules.ANOMALIES_CORROSIVE_SLIM_TIME) {
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
                }
            }

            velocity.scl(0.99f);

            if (hungry && activated) {
                position.mulAdd(velocity, dt);
                if (gs.getPlayer().getPosition().dst(position) < 2400.0f + 200 * level) {
                    tmp.set(gs.getPlayer()
                            .getPosition())
                            .sub(position)
                            .nor()
                            .scl(v);
                    velocity.mulAdd(tmp, dt);
                }
                if (gs.getPlayer().getPosition().dst(position) < 240 * level) {
                    tmp.set(gs.getPlayer().getPosition()).sub(position).nor().scl(250.0f - 20 * level);
                    velocity.mulAdd(tmp, dt);
                }
            }
        }
    }

    private void scaleAndBorn(float dt) {
        scaleX = scaleY += dt;
        if (scaleX > 1 + level * 0.15f) {
            scaleX = saveScaleX = 1 + level * 0.15f;
            scaleY = saveScaleY = 1 + level * 0.15f;
            born = true;
        }
    }

    public void takeDamage(int dmg) {
        level -= dmg;
    }

    @Override
    public void destroy() {
        super.destroy();
        scaleX = scaleY = 0;
        born = false;
        markedToBoom = false;
    }

    @Override
    public void basicRender(SpriteBatch batch) {
        batch.draw(
                glowRegion,
                position.x - glowWidth / 2,
                position.y - glowHeight / 2,
                glowWidth / 2,
                glowHeight / 2,
                glowWidth,
                glowHeight,
                scaleX,
                scaleY,
                clockwise * angle
        );

        batch.draw(
                textureRegions[(int) (frame % PaintConstants.ANOMALY_CORROSIVE_FRAMES)],
                position.x - width / 2,
                position.y - height / 2,
                width / 2,
                height / 2,
                width,
                height,
                scaleX,
                scaleY,
                clockwise * angle
        );
    }

    public void basicUpdate(float dt) {
        frame += 24 * dt;
        if (frame >= PaintConstants.ANOMALY_CORROSIVE_FRAMES) {
            frame = 0.0f;
        }

        angle += 48 * dt;
        if (angle >= 365) {
            angle = 0;
        }

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

    }
}