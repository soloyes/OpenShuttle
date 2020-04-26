package com.shuttle.game.bot.asteroids;

import com.shuttle.game.conf.Rules;
import com.shuttle.game.effects.particle.ParticleEmitter;

/**
 * @author Shuttle on 11/26/18.
 */

public class MineralAsteroidEmitter extends AbstractAsteroidEmitter {
    public MineralAsteroidEmitter(ParticleEmitter particleEmitter) {
        this(MineralAsteroid.class, particleEmitter);
        super.setRespawnTime(Rules.ASTEROID_MINERAL_RESPAWN_TIME);
        super.setAsteroidsMaxCount(Rules.ASTEROID_MINERAL_MAX_COUNT);
    }

    private MineralAsteroidEmitter(Class<? extends AbstractAsteroid> clazz, ParticleEmitter particleEmitter) {
        super(clazz, particleEmitter);
    }

    @Override
    protected MineralAsteroid newObject() {
        return new MineralAsteroid(particleEmitter);
    }
}