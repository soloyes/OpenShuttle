package com.shuttle.game.bot.consumable;

import com.shuttle.game.conf.Rules;

/**
 * @author Shuttle on 22/11/18.
 */

public class StorageEmitter extends AbstractConsumableEmitter {
    private static StorageEmitter instance;

    private StorageEmitter() {
        this(Storage.class);
        super.setRespawnTime(Rules.CONSUMABLE_STORAGE_RESPAWN_TIME);
        super.setConsumablesAtStart(Rules.CONSUMABLE_STORAGE_AT_START);
        super.setConsumablesMaxCount(Rules.CONSUMABLE_STORAGE_MAX_COUNT);
    }

    private StorageEmitter(Class<? extends AbstractConsumable> clazz) {
        super(clazz);
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