package com.shuttle.game;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by FlameXander on 23.09.2017.
 */
public interface Poolable {
    boolean isActive();

    Vector2 getPosition();
}
