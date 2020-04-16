package com.shuttle.game.bot.consumable;

import com.shuttle.game.bot.consumable.fuel.Fuel;
import com.shuttle.game.conf.Rules;

/**
 * @author Shuttle on 24/11/18.
 */

public class FuelEmitter extends AbstractConsumableEmitter {
    private static FuelEmitter instance;

    public static FuelEmitter getInstance() {
        if (instance == null) {
            instance = new FuelEmitter();
        }
        return instance;
    }

    private FuelEmitter() {
        this(Fuel.class);
        super.setRespawnTime(Rules.CONSUMABLE_FUEL_RESPAWN_TIME);
        super.setConsumablesAtStart(Rules.CONSUMABLE_FUEL_AT_START);
        super.setConsumablesMaxCount(Rules.CONSUMABLE_FUEL_MAX_COUNT);
        emit();
    }

    private FuelEmitter(Class<? extends AbstractConsumable> clazz) {
        super(clazz);
    }

    @Override
    protected Fuel newObject() {
        return new Fuel();
    }
}