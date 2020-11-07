package com.buran.game.bot.consumable;

import com.buran.game.conf.Rules;
import com.buran.game.effects.particle.ParticleEmitter;

import lombok.Getter;

/**
 * @author soloyes on 22/11/18.
 */

public class StorageEmitter extends AbstractConsumableEmitter {
    @Getter
    private static int emitted;
    @Getter
    private static int emittedGolden;

    public StorageEmitter(ParticleEmitter particleEmitter) {
        this(Storage.class, particleEmitter);
        super.setRespawnTime(Rules.CONSUMABLE_STORAGE_RESPAWN_TIME);
        super.setConsumablesAtStart(Rules.CONSUMABLE_STORAGE_AT_START);
        super.setConsumablesMaxCount(Rules.CONSUMABLE_STORAGE_MAX_COUNT);
    }

    private StorageEmitter(Class<? extends AbstractConsumable> clazz, ParticleEmitter particleEmitter) {
        super(clazz, particleEmitter);
    }

    @Override
    protected Storage newObject() {
        return new Storage(particleEmitter);
    }

    public void createBonusStorage() {
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