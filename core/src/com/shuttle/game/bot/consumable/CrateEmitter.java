package com.shuttle.game.bot.consumable;

import com.shuttle.game.conf.Rules;

public class CrateEmitter extends AbstractConsumableEmitter {
    public static CrateEmitter instance;

    private CrateEmitter() {
        super.setRespawnTime(Rules.CONSUMABLE_CRATE_RESPAWN_TIME);
        super.setConsumablesAtStart(Rules.CONSUMABLE_CRATE_AT_START);
        super.setConsumablesMaxCount(Rules.CONSUMABLE_CRATE_MAX_COUNT);
        emit();
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
