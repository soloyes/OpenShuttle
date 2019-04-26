package com.shuttle.game.bot.asteroids;

import com.shuttle.game.conf.Rules;
import com.shuttle.game.utils.pool.AbstractPoolable;

/**
 * @author Shuttle on 11/26/18.
 */
public class MineralAsteroidEmitter extends AbstractAsteroidEmitter {
    public static MineralAsteroidEmitter instance;

    public MineralAsteroidEmitter() {
        super.setRespawnTime(Rules.ASTEROID_MINERAL_RESPAWN_TIME);
        super.setAsteroidsMaxCount(Rules.ASTEROID_MINERAL_MAX_COUNT);
        AbstractAsteroidEmitter.emitterMap.put(AbstractPoolable.Type.ASTEROID_MINERAL, this);
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
