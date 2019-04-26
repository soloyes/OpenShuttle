package com.shuttle.game.bot.asteroids;

import com.shuttle.game.conf.Rules;
import com.shuttle.game.utils.pool.AbstractPoolable;

/**
 * @author Shuttle on 11/26/18.
 */
public class MetalAsteroidEmitter extends AbstractAsteroidEmitter {
    public static MetalAsteroidEmitter instance;

    public MetalAsteroidEmitter() {
        super.setRespawnTime(Rules.ASTEROID_METAL_RESPAWN_TIME);
        super.setAsteroidsMaxCount(Rules.ASTEROID_METAL_MAX_COUNT);
        //todo: review emitterMap - what is it, maybe singletone already solved this.
        AbstractAsteroidEmitter.emitterMap.put(AbstractPoolable.Type.ASTEROID_METAL, this);
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
