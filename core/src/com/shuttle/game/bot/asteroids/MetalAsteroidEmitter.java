package com.shuttle.game.bot.asteroids;

import com.shuttle.game.conf.Rules;

/**
 * @author Shuttle on 11/26/18.
 */

public class MetalAsteroidEmitter extends AbstractAsteroidEmitter {
    private static MetalAsteroidEmitter instance;

    private MetalAsteroidEmitter() {
        this(MetalAsteroid.class);
        super.setRespawnTime(Rules.ASTEROID_METAL_RESPAWN_TIME);
        super.setAsteroidsMaxCount(Rules.ASTEROID_METAL_MAX_COUNT);
    }

    private MetalAsteroidEmitter(Class<? extends AbstractAsteroid> clazz) {
        super(clazz);
    }

    @Override
    protected MetalAsteroid newObject() {
        return new MetalAsteroid();
    }

    public static MetalAsteroidEmitter getInstance() {
        if (instance == null) {
            instance = new MetalAsteroidEmitter();
        }
        return instance;
    }
}