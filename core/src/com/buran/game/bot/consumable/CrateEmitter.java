package com.buran.game.bot.consumable;

import com.buran.game.conf.Rules;
import com.buran.game.effects.particle.ParticleEmitter;

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
        this(Crate.class, particleEmitter);
        super.setRespawnTime(Rules.CONSUMABLE_CRATE_RESPAWN_TIME);
        super.setConsumablesAtStart(Rules.CONSUMABLE_CRATE_AT_START);
        super.setConsumablesMaxCount(Rules.CONSUMABLE_CRATE_MAX_COUNT);
        emit();
    }

    private CrateEmitter(Class<? extends AbstractConsumable> clazz, ParticleEmitter particleEmitter) {
        super(clazz, particleEmitter);
    }

    @Override
    protected Crate newObject() {
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