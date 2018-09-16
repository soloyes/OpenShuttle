package com.shuttle.game.bot.pool;

import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.utils.BoomBox;
import com.shuttle.game.utils.BoundCheck;

import lombok.Data;

/**
 * @author Shuttle on 8/26/18.
 */
@Data
public abstract class AbstractPoolable implements Poolable {
    protected Vector2 position;
    protected Vector2 velocity;
    protected BoundCheck boundCheck;
    protected BoomBox boomBox;
    protected int width, height;
    protected boolean active;
    protected float scaleX, scaleY;
    protected float frame;
    protected float angle, rotationSpeed;
    protected int clockwise;
    protected int level;
    protected float saveScaleX, saveScaleY;
    protected boolean interacting;

    @Override
    public void destroy() {
        this.active = false;
    }

    public abstract Vector2 getPosition();
}