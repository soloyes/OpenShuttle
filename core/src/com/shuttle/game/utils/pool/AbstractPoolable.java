package com.shuttle.game.utils.pool;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.blackhole.Blackhole;
import com.shuttle.game.bot.consumable.AbstractConsumable;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.effects.MoveProcessor;
import com.shuttle.game.effects.particle.ParticleEmitter;
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
    @Getter
    protected BoundCheck boundCheck;
    @Getter
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
    @Getter
    @Setter
    protected float saveScaleX, saveScaleY;
    @Setter
    protected float frame;
    @Getter
    protected float angle, rotationSpeed;
    protected int clockwise;
    @Getter
    @Setter
    protected int level;
    @Getter
    @Setter
    protected boolean taken;
    @Getter
    @Setter
    protected Type type;
    @Getter
    @Setter
    protected Color color;
    @Getter
    @Setter
    protected Color spreeColor;
    @Getter
    @Setter
    protected boolean markedToBoom;
    @Getter
    @Setter
    protected float boomTimer;
    @Getter
    @Setter
    protected float boomDelay;
    @Getter
    @Setter
    //Should mean: is in pool?
    protected boolean fake;
    protected MoveProcessor moveProcessor;
    @Getter
    @Setter
    protected boolean born;
    @Getter
    @Setter
    protected int spiralRadius;
    @Getter
    @Setter
    protected boolean atRayPoint;
    @Getter
    @Setter
    protected boolean atRocketPoint;

    @Override
    public void destroy() {
        this.active = false;
        this.activated = false;
    }

    public abstract void basicRender(SpriteBatch batch);

    public abstract void basicUpdate(float dt);

    public abstract void render(SpriteBatch batch);

    public abstract void update(float dt);

    public void showSprayBoom() {
        float R, G, B;
        for (int j = 0; j < 8; j++) {
            R = this.getSpreeColor().r;
            G = this.getSpreeColor().g;
            B = this.getSpreeColor().b;
            ParticleEmitter.getInstance().setup(
                    this.getPosition().x,
                    this.getPosition().y,
                    MathUtils.random(-200, 200), MathUtils.random(-200, 200),
                    0.4f, 24, 0,
                    R, G, B, 1f,
                    R, G, B, 0.0f
            );
        }
    }

    public abstract Vector2 getPosition();

    public void showSprayCollision() {
        float R, G, B;
        for (int j = 0; j < 60; j++) {
            R = this.getSpreeColor().r;
            G = this.getSpreeColor().g;
            B = this.getSpreeColor().b;
            ParticleEmitter.getInstance().setup(
                    this.getPosition().x + MathUtils.random(-30, 30),
                    this.getPosition().y + MathUtils.random(-30, 30),
                    MathUtils.random(-600, 600), MathUtils.random(-600, 600),
                    0.4f, 8, 0,
                    R, G, B, 1f,
                    R, G, B, 0.0f
            );
        }
    }

    public void collideBlackhole() {
        float dst = Blackhole.position.dst(position);
        if (dst <= Rules.BLACKHOLE_TAKE_RANGE) {
            taken = true;
            atRayPoint = false;
            velocity.scl(0.5f);
            float scaleX = (dst / Rules.BLACKHOLE_TAKE_RANGE) * saveScaleX;
            float scaleY = (dst / Rules.BLACKHOLE_TAKE_RANGE) * saveScaleY;
            if (scaleX >= saveScaleX) {
                scaleX = saveScaleX;
            }
            if (scaleY >= saveScaleY) {
                scaleY = saveScaleY;
            }
            this.scaleX = scaleX;
            this.scaleY = scaleY;
            if (dst <= Rules.BLACKHOLE_EVENT_HORIZON) {
                destroy();
            }
        } else {
            if (!(this instanceof AbstractConsumable)) {
                this.scaleX = this.saveScaleX;
                this.scaleY = this.saveScaleY;
            }
            taken = false;
        }
    }

    public enum Type {
        CORROSIVE, ELECTRIC, MAGNETIC, RADIATION, ASTEROID_METAL, ASTEROID_MINERAL, ASTRONAUT, CRATE, FUEL, STORAGE
    }

    @Override
    public void destroyWithEffect() {
        destroy();
    }
}