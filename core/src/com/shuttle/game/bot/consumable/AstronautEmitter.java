package com.shuttle.game.bot.consumable;

import com.shuttle.game.conf.Rules;

public class AstronautEmitter extends AbstractConsumableEmitter {
    private static AstronautEmitter instance;

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

    public static AstronautEmitter getInstance() {
        if (instance == null) {
            instance = new AstronautEmitter();
        }
        return instance;
    }
}
