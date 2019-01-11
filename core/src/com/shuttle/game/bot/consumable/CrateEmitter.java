package com.shuttle.game.bot.consumable;

import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;

public class CrateEmitter extends AbstractConsumableEmitter {
    public CrateEmitter(GameScreen gs) {
        super.gs = gs;
        super.setRespawnTime(Rules.CONSUMABLE_CRATE_RESPAWN_TIME);
        super.setConsumablesAtStart(Rules.CONSUMABLE_CRATE_AT_START);
        super.setConsumablesMaxCount(Rules.CONSUMABLE_CRATE_MAX_COUNT);
        emit();
    }

    @Override
    protected Crate newObject() {
        return new Crate(gs);
    }

    public void createBonusAnomaly() {
        super.createConsumable();
    }
}
