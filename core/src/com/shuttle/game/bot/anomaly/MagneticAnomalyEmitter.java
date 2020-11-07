package com.shuttle.game.bot.anomaly;

import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.effects.particle.ParticleEmitter;

/**
 * @author soloyes on 9/12/18.
 */

public class MagneticAnomalyEmitter extends AbstractAnomalyEmitter {
    public MagneticAnomalyEmitter(ParticleEmitter particleEmitter) {
        this(MagneticAnomaly.class, particleEmitter);
        super.setRespawnTime(Rules.ANOMALIES_MAGNETIC_RESPAWN_TIME);
        super.setAnomaliesAtStart(Rules.ANOMALIES_MAGNETIC_AT_START);
        super.setAnomaliesMaxCount(Rules.ANOMALIES_MAGNETIC_MAX_COUNT);
        emit();
    }

    private MagneticAnomalyEmitter(Class<? extends AbstractAnomaly> clazz, ParticleEmitter particleEmitter) {
        super(clazz, particleEmitter);
    }

    @Override
    protected MagneticAnomaly newObject() {
        return new MagneticAnomaly(particleEmitter);
    }

    public void createBonusAnomaly(Vector2 position) {
        AbstractAnomaly anomaly = getActiveElement();
        anomaly.init(position.x, position.y);
    }
}