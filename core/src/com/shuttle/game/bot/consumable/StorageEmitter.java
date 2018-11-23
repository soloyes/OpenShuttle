package com.shuttle.game.bot.consumable;

import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;

public class StorageEmitter extends AbstractConsumableEmitter {
    public StorageEmitter(GameScreen gs) {
        super.gs = gs;
        super.setRespawnTime(Rules.CONSUMABLE_STORAGE_RESPAWN_TIME);
        super.setConsumablesAtStart(Rules.CONSUMABLE_STORAGE_AT_START);
        super.setConsumablesMaxCount(Rules.CONSUMABLE_STORAGE_MAX_COUNT);
        emit();
    }

    @Override
    protected Storage newObject() {
            return new Storage(gs);
    }
}
