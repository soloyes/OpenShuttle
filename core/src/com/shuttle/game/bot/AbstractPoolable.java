package com.shuttle.game.bot;

import com.badlogic.gdx.math.Vector2;
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
    protected int width, height;
    protected boolean active;
    protected float scaleX;
    protected float scaleY;
    protected float frame;
    protected float angle, rotationSpeed;
    protected int clockwise;
    protected int level;

    @Override
    public void destroy() {
        this.active = false;
    }
}