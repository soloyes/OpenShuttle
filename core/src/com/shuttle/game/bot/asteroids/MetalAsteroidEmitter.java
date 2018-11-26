package com.shuttle.game.bot.asteroids;

import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;

/**
 * @author Shuttle on 11/26/18.
 */
public class MetalAsteroidEmitter extends AbstractAsteroidEmitter {
    public MetalAsteroidEmitter(GameScreen gs) {
        super.gs = gs;
        super.setRespawnTime(Rules.ASTEROID_METAL_RESPAWN_TIME);
        super.setAsteroidsMaxCount(Rules.ASTEROID_METAL_MAX_COUNT);
    }

    @Override
    protected MetalAsteroid newObject() {
        return new MetalAsteroid(gs);
    }
}
