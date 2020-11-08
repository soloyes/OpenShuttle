package com.buran.game.bot.asteroids;

import com.buran.game.conf.Rules;
import com.buran.game.effects.particle.ParticleEmitter;

/**
 * @author soloyes on 11/26/18.
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