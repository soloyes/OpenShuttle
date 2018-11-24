package com.shuttle.game.bot.consumable;

import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;

public class AstronautEmitter extends AbstractConsumableEmitter {
    public AstronautEmitter(GameScreen gs) {
        super.gs = gs;
        super.setRespawnTime(Rules.CONSUMABLE_ASTRONAUT_RESPAWN_TIME);
        super.setConsumablesAtStart(Rules.CONSUMABLE_ASTRONAUT_AT_START);
        super.setConsumablesMaxCount(Rules.CONSUMABLE_ASTRONAUT_MAX_COUNT);
        emit();
    }

    @Override
    protected Astronaut newObject() {
            return new Astronaut(gs);
    }
}
