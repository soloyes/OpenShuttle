package com.shuttle.game.bot.asteroids;

import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;

/**
 * @author Shuttle on 11/26/18.
 */
public class MineralAsteroidEmitter extends AbstractAsteroidEmitter {
    public MineralAsteroidEmitter(GameScreen gs) {
        super.gs = gs;
        super.setRespawnTime(Rules.ASTEROID_MINERAL_RESPAWN_TIME);
        super.setAsteroidsMaxCount(Rules.ASTEROID_MINERAL_MAX_COUNT);
    }

    @Override
    protected MineralAsteroid newObject() {
        return new MineralAsteroid(gs);
    }
}
