package com.shuttle.game.bot.asteroids;

import com.shuttle.game.conf.Rules;

/**
 * @author Shuttle on 11/26/18.
 */

public class MineralAsteroidEmitter extends AbstractAsteroidEmitter {
    private static MineralAsteroidEmitter instance;

    private MineralAsteroidEmitter() {
        this(MineralAsteroid.class);
        super.setRespawnTime(Rules.ASTEROID_MINERAL_RESPAWN_TIME);
        super.setAsteroidsMaxCount(Rules.ASTEROID_MINERAL_MAX_COUNT);
    }

    private MineralAsteroidEmitter(Class<? extends AbstractAsteroid> clazz) {
        super(clazz);
    }

    @Override
    protected MineralAsteroid newObject() {
        return new MineralAsteroid();
    }

    public static MineralAsteroidEmitter getInstance() {
        if (instance == null) {
            instance = new MineralAsteroidEmitter();
        }
        return instance;
    }
}