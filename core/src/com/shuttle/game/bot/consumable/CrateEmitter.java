package com.shuttle.game.bot.consumable;

import com.shuttle.game.conf.Rules;

/**
 * @author Shuttle on 23/11/18.
 */

public class CrateEmitter extends AbstractConsumableEmitter {
    public static CrateEmitter instance;

    private CrateEmitter() {
        this(Crate.class);
        super.setRespawnTime(Rules.CONSUMABLE_CRATE_RESPAWN_TIME);
        super.setConsumablesAtStart(Rules.CONSUMABLE_CRATE_AT_START);
        super.setConsumablesMaxCount(Rules.CONSUMABLE_CRATE_MAX_COUNT);
        emit();
    }

    private CrateEmitter(Class<? extends AbstractConsumable> clazz) {
        super(clazz);
    }

    @Override
    protected Crate newObject() {
        return new Crate();
    }

    public void createBonusAnomaly() {
        super.createConsumable();
    }

    public static CrateEmitter getInstance() {
        if (instance == null) {
            instance = new CrateEmitter();
        }
        return instance;
    }
}