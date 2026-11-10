package com.buran.game.utils.pool;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.buran.game.bot.blackhole.Blackhole;
import com.buran.game.bot.consumable.AbstractConsumable;
import com.buran.game.conf.Buffs;
import com.buran.game.conf.Rules;
import com.buran.game.effects.BoomBox;
import com.buran.game.effects.MoveProcessor;
import com.buran.game.utils.BoundCheck;

import lombok.Getter;
import lombok.Setter;

/**
 * @author soloyes on 8/26/18.
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
    /** Distance to the black hole when it caught this object (0 = not caught). */
    private float captureDst;
    /** 0..1 how far this object has crumbled into the black hole (drawn with the Dissolve shader). */
    protected float dissolve;

    public float getDissolve() {
        return dissolve;
    }
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

    public abstract Vector2 getPosition();

    public void collideBlackhole() {
        float dst = Blackhole.position.dst(position);
        if (dst <= Buffs.getBlackholeTakeRange()) {
            taken = true;
            com.badlogic.gdx.graphics.Color tint = color != null ? color : com.badlogic.gdx.graphics.Color.WHITE;
            // The object crumbles into the accretion flow: it sheds sand as it falls and, half-way in,
            // disintegrates completely instead of shrinking to a dot.
            com.buran.game.bot.blackhole.BlackholeDebris.shed(position.x, position.y, tint, 4);
            atRayPoint = false;
            velocity.scl(0.5f);
            // Crumble right after capture: fade over the first stretch of the fall, then burst into sand.
            if (captureDst <= 0f) {
                captureDst = dst;
            }
            float fall = MathUtils.clamp((captureDst - dst) / (Buffs.getBlackholeTakeRange() * 0.35f), 0f, 1f);
            dissolve = fall;
            com.buran.game.bot.blackhole.BlackholeDebris.shed(position.x, position.y, tint, 3 + (int) (fall * 6));
            if (fall >= 1f) {
                com.buran.game.bot.blackhole.BlackholeDebris.burst(position.x, position.y, Math.max(width, height) * saveScaleX, tint);
                captureDst = 0f;
                dissolve = 0f;
                destroy();
                return;
            }
            float shrink = 1f - 0.15f * fall;
            float scaleX = shrink * saveScaleX;
            float scaleY = shrink * saveScaleY;
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
            captureDst = 0f;
            dissolve = 0f;
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