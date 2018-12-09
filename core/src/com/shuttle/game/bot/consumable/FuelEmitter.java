package com.shuttle.game.bot.consumable;

import com.shuttle.game.bot.consumable.fuel.Fuel;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;


public class FuelEmitter extends AbstractConsumableEmitter {
    public FuelEmitter(GameScreen gs) {
        super.gs = gs;
        super.setRespawnTime(Rules.CONSUMABLE_FUEL_RESPAWN_TIME);
        super.setConsumablesAtStart(Rules.CONSUMABLE_FUEL_AT_START);
        super.setConsumablesMaxCount(Rules.CONSUMABLE_FUEL_MAX_COUNT);
        emit();
    }

    @Override
    protected Fuel newObject() {
        return new Fuel(gs);
    }
}