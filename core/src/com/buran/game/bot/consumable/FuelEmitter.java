package com.buran.game.bot.consumable;

import com.buran.game.bot.consumable.fuel.Fuel;
import com.buran.game.conf.Rules;
import com.buran.game.effects.particle.ParticleEmitter;

import lombok.Getter;

/**
 * @author soloyes on 24/11/18.
 */

public class FuelEmitter extends AbstractConsumableEmitter {
    @Getter
    private static int emitted;
    @Getter
    private static int emittedGolden;

    public FuelEmitter(ParticleEmitter particleEmitter) {
        this(Fuel.class, particleEmitter);
        super.setRespawnTime(Rules.CONSUMABLE_FUEL_RESPAWN_TIME);
        super.setConsumablesAtStart(Rules.CONSUMABLE_FUEL_AT_START);
        super.setConsumablesMaxCount(Rules.CONSUMABLE_FUEL_MAX_COUNT);
        emit();
    }

    private FuelEmitter(Class<? extends AbstractConsumable> clazz, ParticleEmitter particleEmitter) {
        super(clazz, particleEmitter);
    }

    @Override
    protected Fuel newObject() {
        return new Fuel(particleEmitter);
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