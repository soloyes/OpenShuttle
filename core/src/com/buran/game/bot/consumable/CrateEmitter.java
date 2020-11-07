package com.buran.game.bot.consumable;

import lombok.Getter;

/**
 * @author soloyes on 23/11/18.
 */

public class CrateEmitter extends AbstractConsumableEmitter {
    @Getter
    private static int emitted;
    @Getter
    private static int emittedGolden;

    public CrateEmitter(ParticleEmitter particleEmitter) {
        this(com.shuttle.game.bot.consumable.Crate.class, particleEmitter);
        super.setRespawnTime(Rules.CONSUMABLE_CRATE_RESPAWN_TIME);
        super.setConsumablesAtStart(Rules.CONSUMABLE_CRATE_AT_START);
        super.setConsumablesMaxCount(Rules.CONSUMABLE_CRATE_MAX_COUNT);
        emit();
    }

    private CrateEmitter(Class<? extends com.shuttle.game.bot.consumable.AbstractConsumable> clazz, ParticleEmitter particleEmitter) {
        super(clazz, particleEmitter);
    }

    @Override
    protected com.shuttle.game.bot.consumable.Crate newObject() {
        return new Crate(particleEmitter);
    }

    public void createBonusCrate() {
        super.createConsumable();
    }

    @Override
    protected void calculateStat(AbstractConsumable consumable) {
        if (consumable.isGolden()) {
            emittedGolden++;
        } else {
            emitted++;
        }
    }
}