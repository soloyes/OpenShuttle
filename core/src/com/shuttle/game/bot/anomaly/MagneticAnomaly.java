package com.shuttle.game.bot.anomaly;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.Regions;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 9/12/18.
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

    public MagneticAnomaly(GameScreen gs) {
        super(gs);
        this.textureRegions = Regions.split(
                new TextureRegion(
                        Assets.getInstance()
                                .getAtlas()
                                .findRegion(PaintConstants.ANOMALY_MAGNETIC_REGION)
                ),
                4,
                4,
                16
        );
        this.width = PaintConstants.ANOMALY_MAGNETIC_WIDTH;
        this.height = PaintConstants.ANOMALY_MAGNETIC_HEIGHT;
        this.type = Type.MAGNETIC;
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
        this.level = 1;
        this.scaleX = this.saveScaleX = 1 + level * 0.15f;
        this.scaleY = this.saveScaleY = 1 + level * 0.15f;
        this.taken = true;
        this.color.a = 0.0f;
        this.angle = MathUtils.random(365);
        this.clockwise = MathUtils.randomSign();
        this.v = MathUtils.random(
                Rules.ANOMALIES_MAGNETIC_VELOCITY,
                3 * Rules.ANOMALIES_MAGNETIC_VELOCITY
        );
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
        scaleAndBorn(dt);
        basicUpdate(dt);

        if (born) {
            boundCheck.checkAndHandleBoundsAndDestroy(this);

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
        if (!born) {
            color.a += dt;
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
    }

    public void addEatTimer(float dt) {
        eatTimer += dt;
    }
}
