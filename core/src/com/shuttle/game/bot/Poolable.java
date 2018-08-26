package com.shuttle.game.bot;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by FlameXander on 23.09.2017.
 */
public interface Poolable {

    void destroy();

    Vector2 getPosition();

    boolean isActive();
}
