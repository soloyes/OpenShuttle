package com.shuttle.game.bot.asteroids;

import com.shuttle.game.conf.Rules;
import com.shuttle.game.effects.particle.ParticleEmitter;

/**
 * @author Shuttle on 11/26/18.
 */

public class MetalAsteroidEmitter extends AbstractAsteroidEmitter {
    public MetalAsteroidEmitter(ParticleEmitter particleEmitter) {
        this(MetalAsteroid.class, particleEmitter);
        super.setRespawnTime(Rules.ASTEROID_METAL_RESPAWN_TIME);
        super.setAsteroidsMaxCount(Rules.ASTEROID_METAL_MAX_COUNT);
    }

    private MetalAsteroidEmitter(Class<? extends AbstractAsteroid> clazz, ParticleEmitter particleEmitter) {
        super(clazz, particleEmitter);
    }

    @Override
    protected MetalAsteroid newObject() {
        return new MetalAsteroid(particleEmitter);
    }
}