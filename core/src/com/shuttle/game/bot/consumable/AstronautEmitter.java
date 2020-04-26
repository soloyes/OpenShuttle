package com.shuttle.game.bot.consumable;

import com.shuttle.game.conf.Rules;
import com.shuttle.game.effects.particle.ParticleEmitter;

import lombok.Getter;

/**
 * @author Shuttle on 12/11/18.
 */

public class AstronautEmitter extends AbstractConsumableEmitter {
    @Getter
    private static int emitted;

    public AstronautEmitter(ParticleEmitter particleEmitter) {
        this(Astronaut.class, particleEmitter);
        super.setRespawnTime(Rules.CONSUMABLE_ASTRONAUT_RESPAWN_TIME);
        super.setConsumablesAtStart(Rules.CONSUMABLE_ASTRONAUT_AT_START);
        super.setConsumablesMaxCount(Rules.CONSUMABLE_ASTRONAUT_MAX_COUNT);
        this.particleEmitter = particleEmitter;
        emit();
    }

    private AstronautEmitter(Class<? extends AbstractConsumable> clazz, ParticleEmitter particleEmitter) {
        super(clazz, particleEmitter);
    }

    @Override
    protected Astronaut newObject() {
        return new Astronaut(particleEmitter);
    }

    @Override
    protected void calculateStat(AbstractConsumable consumable) {
        emitted++;
    }
}