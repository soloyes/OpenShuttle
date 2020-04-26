package com.shuttle.game.bot.anomaly;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.rocket.RocketLauncher;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.effects.particle.ParticleEmitter;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.BoundCheck;
import com.shuttle.game.utils.pool.AbstractPoolable;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 9/15/18.
 */

public abstract class AbstractAnomaly extends AbstractPoolable {
    @Getter
    @Setter
    protected boolean hungry;
    @Getter
    @Setter
    protected float hungryTimer;
    protected Vector2 tmp;
    @Getter
    @Setter
    protected TextureRegion[] textureRegions;
    @Getter
    @Setter
    protected float v;
    @Getter
    protected TextureRegion atRocketRegion;
    protected ParticleEmitter particleEmitter;

    @Override
    public Vector2 getPosition() {
        return position;
    }

    public abstract void render(SpriteBatch batch);

    public abstract void update(float dt);

    public AbstractAnomaly() {
    }

    protected AbstractAnomaly(ParticleEmitter particleEmitter) {
        this.boomBox = new BoomBox();
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.tmp = new Vector2();
        this.color = new Color();
        this.spreeColor = new Color();
        this.boundCheck = new BoundCheck();
        this.atRocketRegion = new TextureRegion(
                Assets.getInstance()
                        .getAtlas()
                        .findRegion(PaintConstants.AT_ROCKET_POINT_REGION));
        this.particleEmitter = particleEmitter;
    }

    protected void drawAtRocketPoint(SpriteBatch batch) {
        if (atRocketPoint && RocketLauncher.isLaunched()) {
            Color color = batch.getColor();
            batch.setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, PaintConstants.AT_ROCKET_POINT_ALPHA);
            batch.draw(atRocketRegion,
                    position.x - width / 2, position.y - height / 2,
                    width / 2, height / 2,
                    width, height,
                    scaleX, scaleY,
                    clockwise * angle);
            batch.setColor(color);
        }
    }

    public void showSprayBoom() {
        float R, G, B;
        for (int j = 0; j < 8; j++) {
            R = this.getSpreeColor().r;
            G = this.getSpreeColor().g;
            B = this.getSpreeColor().b;
            particleEmitter.setup(
                    this.getPosition().x,
                    this.getPosition().y,
                    MathUtils.random(-200, 200), MathUtils.random(-200, 200),
                    0.4f, 24, 0,
                    R, G, B, 1f,
                    R, G, B, 0.0f
            );
        }
    }

    public void showSprayCollision() {
        float R, G, B;
        for (int j = 0; j < 60; j++) {
            R = this.getSpreeColor().r;
            G = this.getSpreeColor().g;
            B = this.getSpreeColor().b;
            particleEmitter.setup(
                    this.getPosition().x + MathUtils.random(-30, 30),
                    this.getPosition().y + MathUtils.random(-30, 30),
                    MathUtils.random(-600, 600), MathUtils.random(-600, 600),
                    0.4f, 8, 0,
                    R, G, B, 1f,
                    R, G, B, 0.0f
            );
        }
    }
}