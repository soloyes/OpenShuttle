package com.shuttle.game.bot.rocket;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.anomaly.AbstractAnomaly;
import com.shuttle.game.bot.blackhole.Blackhole;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.player.Standing;
import com.shuttle.game.ui.GameInfoSystem;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.Regions;
import com.shuttle.game.utils.pool.AbstractPoolable;

import lombok.Setter;

/**
 * @author soloyes on 19/01/19.
 */

public class Rocket extends AbstractPoolable {
    private Vector2 position;
    private Vector2 velocity;
    private Vector2 destination;
    private TextureRegion[] textureRegions;
    private Vector2 tmp1V2, tmp2V2;
    @Setter
    private AbstractAnomaly targetAnomaly;
    private boolean aimed;
    private float angle;

    public Rocket() {
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.destination = new Vector2();
        this.width = PaintConstants.ROCKET_WIDTH;
        this.height = PaintConstants.ROCKET_HEIGHT;
        this.textureRegions = Regions.split(
                Assets.getInstance().getAtlas().findRegion(PaintConstants.ROCKET_REGION),
                1, 3, PaintConstants.ROCKET_FRAMES
        );
        this.tmp1V2 = new Vector2();
        this.tmp2V2 = new Vector2();
    }

    @Override
    public void init(float x, float y) {
        this.position.set(x, y);
        this.active = true;
        this.velocity.set(0, 0);
        this.scaleX = this.scaleY = 1.0f;
        this.aimed = true;
    }

    @Override
    public void basicRender(SpriteBatch batch) {
        batch.draw(
                textureRegions[(int) (frame % PaintConstants.ROCKET_FRAMES)],
                position.x - width / 2, position.y - height / 2,
                width / 2, height / 2,
                width, height,
                scaleX, scaleY,
                angle
        );
    }

    public void checkCollision() {
        if (position.dst(targetAnomaly.getPosition()) <= PaintConstants.ROCKET_WIDTH && targetAnomaly.isActive()) {
            destroy();
            targetAnomaly.destroyWithEffect();
            GameInfoSystem.setup(
                    position.x,
                    position.y,
                    GameInfoSystem.Preset.SCORE + "[GREEN] +" + Rules.ANOMALIES_BASE_SCORE_ADD + "[]",
                    3.0f,
                    true
            );
            Standing.getInstance().addToStatistics(targetAnomaly.getType(), targetAnomaly.getLevel());
        }
    }

    @Override
    public void collideBlackhole() {
        float dst = Blackhole.position.dst(position);
        if (dst <= Rules.BLACKHOLE_TAKE_RANGE) {
            taken = true;
            velocity.scl(0.5f);
        }
    }

    @Override
    public void basicUpdate(float dt) {
        frame += 24 * dt;
        if (frame >= PaintConstants.ROCKET_FRAMES) {
            frame = 0.0f;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        basicRender(batch);
    }

    @Override
    public void update(float dt) {
        basicUpdate(dt);
        position.mulAdd(velocity, dt);
        if (targetAnomaly.isActive() && !targetAnomaly.isTaken() && aimed && !Rules.GAME_OVER) {
            destination.set(targetAnomaly.getPosition());
            tmp2V2.set(destination);
        } else {
            if (aimed) {
                tmp2V2.scl(Integer.MAX_VALUE);
                aimed = false;
            }
        }
        float v = Rules.ROCKETS_VELOCITY + (targetAnomaly.isActivated() ? targetAnomaly.getV() : 0);
        tmp1V2.set(tmp2V2
                .sub(position)
                .nor()
                .scl(v)
        );
        velocity.mulAdd(tmp1V2, dt);
        angle = velocity.angle();
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }
}