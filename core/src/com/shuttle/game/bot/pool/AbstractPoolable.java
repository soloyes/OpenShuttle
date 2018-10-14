package com.shuttle.game.bot.pool;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.anomaly.CorrosiveAnomaly;
import com.shuttle.game.bot.particle.ParticleEmitter;
import com.shuttle.game.utils.BoomBox;
import com.shuttle.game.utils.BoundCheck;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 8/26/18.
 */
public abstract class AbstractPoolable implements Poolable {
    protected Vector2 position;
    @Getter
    @Setter
    protected Vector2 velocity;
    protected BoundCheck boundCheck;
    protected BoomBox boomBox;
    @Getter
    @Setter
    protected int width, height;
    @Getter
    @Setter
    //showing on screen
    protected boolean active;
    @Getter
    @Setter
    //depending on AnomalyTimer
    protected boolean activated;
    @Getter
    @Setter
    protected float scaleX, scaleY;
    @Setter
    protected float frame;
    protected float angle, rotationSpeed;
    protected int clockwise;
    @Getter
    @Setter
    protected int level;
    @Getter
    @Setter
    protected float saveScaleX, saveScaleY;
    @Getter
    @Setter
    protected boolean taken;
    @Getter
    @Setter
    protected Type type;

    @Override
    public void destroy() {
        this.active = false;
        this.activated = false;
    }

    public abstract void basicRender(SpriteBatch batch);

    public abstract void basicUpdate(float dt);

    public abstract void render(SpriteBatch batch);

    public abstract void update(float dt);

    public void showSpray(ParticleEmitter particleEmitter) {
        float R = 0, G = 0.6f, B = 0;
        for (int j = 0; j < 60; j++) {
            if (this instanceof CorrosiveAnomaly) {
                R = ((CorrosiveAnomaly) (this)).getColor().r;
                G = ((CorrosiveAnomaly) (this)).getColor().g;
                B = ((CorrosiveAnomaly) (this)).getColor().b;
            }
            particleEmitter.setup(
                    this.getPosition().x + MathUtils.random(-30, 30),
                    this.getPosition().y + MathUtils.random(-30, 30),
                    MathUtils.random(-400, 400), MathUtils.random(-400, 400),
                    0.4f,
                    8,
                    1,
                    R, G, B,
                    1f,
                    R, G, B,
                    0.0f
            );
        }
    }

    public abstract Vector2 getPosition();

    public enum Type {
        CORROSIVE, ELECTRIC, MAGNETIC, RADIATION, ASTEROID, ASTRONAUT, CRATE;
    }
}