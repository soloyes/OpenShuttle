package com.shuttle.game.bot.consumable;

import com.shuttle.game.conf.Rules;

/**
 * @author Shuttle on 12/11/18.
 */

public class AstronautEmitter extends AbstractConsumableEmitter {
    private static AstronautEmitter instance;

    public static AstronautEmitter getInstance() {
        if (instance == null) {
            instance = new AstronautEmitter();
        }
        return instance;
    }

    private AstronautEmitter() {
        this(Astronaut.class);
        super.setRespawnTime(Rules.CONSUMABLE_ASTRONAUT_RESPAWN_TIME);
        super.setConsumablesAtStart(Rules.CONSUMABLE_ASTRONAUT_AT_START);
        super.setConsumablesMaxCount(Rules.CONSUMABLE_ASTRONAUT_MAX_COUNT);
        emit();
    }

    private AstronautEmitter(Class<? extends AbstractConsumable> clazz) {
        super(clazz);
    }

    @Override
    protected Astronaut newObject() {
        return new Astronaut();
    }
}