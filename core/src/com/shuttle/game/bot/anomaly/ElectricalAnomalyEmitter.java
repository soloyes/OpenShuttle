package com.shuttle.game.bot.anomaly;

import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.effects.particle.ParticleEmitter;

/**
 * @author Shuttle on 9/11/18.
 */

public class ElectricalAnomalyEmitter extends AbstractAnomalyEmitter {
    public ElectricalAnomalyEmitter(ParticleEmitter particleEmitter) {
        this(ElectricalAnomaly.class, particleEmitter);
        super.setRespawnTime(Rules.ANOMALIES_ELECTRIC_RESPAWN_TIME);
        super.setAnomaliesAtStart(Rules.ANOMALIES_ELECTRIC_AT_START);
        super.setAnomaliesMaxCount(Rules.ANOMALIES_ELECTRIC_MAX_COUNT);
        emit();
    }

    private ElectricalAnomalyEmitter(Class<? extends AbstractAnomaly> clazz, ParticleEmitter particleEmitter) {
        super(clazz, particleEmitter);
    }

    @Override
    protected ElectricalAnomaly newObject() {
        return new ElectricalAnomaly(particleEmitter);
    }

    public void createBonusAnomaly(Vector2 position) {
        AbstractAnomaly anomaly = getActiveElement();
        anomaly.init(position.x, position.y);
    }
}