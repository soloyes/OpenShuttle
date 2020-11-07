package com.shuttle.game.bot.anomaly;

import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.effects.particle.ParticleEmitter;

/**
 * @author soloyes on 9/12/18.
 */

public class RadiationAnomalyEmitter extends AbstractAnomalyEmitter {
    public RadiationAnomalyEmitter(ParticleEmitter particleEmitter) {
        this(RadiationAnomaly.class, particleEmitter);
        super.setRespawnTime(Rules.ANOMALIES_RADIATION_RESPAWN_TIME);
        super.setAnomaliesAtStart(Rules.ANOMALIES_RADIATION_AT_START);
        super.setAnomaliesMaxCount(Rules.ANOMALIES_RADIATION_MAX_COUNT);
        emit();
    }

    private RadiationAnomalyEmitter(Class<? extends AbstractAnomaly> clazz, ParticleEmitter particleEmitter) {
        super(clazz, particleEmitter);
    }

    @Override
    protected RadiationAnomaly newObject() {
        return new RadiationAnomaly(particleEmitter);
    }

    public void createBonusAnomaly(Vector2 position) {
        AbstractAnomaly anomaly = getActiveElement();
        anomaly.init(position.x, position.y);
    }
}