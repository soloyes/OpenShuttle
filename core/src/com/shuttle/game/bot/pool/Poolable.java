package com.shuttle.game.bot.pool;

/**
 * Created by FlameXander on 23.09.2017.
 */
public interface Poolable {
    void init(float x, float y);

    void destroy();

    boolean isActive();
}
