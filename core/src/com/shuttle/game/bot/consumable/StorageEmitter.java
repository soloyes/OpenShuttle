package com.shuttle.game.bot.consumable;

import com.shuttle.game.conf.Rules;

public class StorageEmitter extends AbstractConsumableEmitter {
    public static StorageEmitter instance;

    public StorageEmitter() {
        super.setRespawnTime(Rules.CONSUMABLE_STORAGE_RESPAWN_TIME);
        super.setConsumablesAtStart(Rules.CONSUMABLE_STORAGE_AT_START);
        super.setConsumablesMaxCount(Rules.CONSUMABLE_STORAGE_MAX_COUNT);
    }

    @Override
    protected Storage newObject() {
        return new Storage();
    }

    public void createBonusAnomaly() {
        super.createConsumable();
    }

    public static StorageEmitter getInstance() {
        if (instance == null) {
            instance = new StorageEmitter();
        }
        return instance;
    }
}
