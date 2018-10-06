package com.shuttle.game.bot.pool;

import com.badlogic.gdx.math.Vector2;
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

    @Override
    public void destroy() {
        this.active = false;
        this.activated = false;
    }

    public abstract Vector2 getPosition();
}