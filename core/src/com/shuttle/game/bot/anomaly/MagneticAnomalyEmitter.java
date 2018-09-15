package com.shuttle.game.bot.anomaly;

import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;

/**
 * @author Shuttle on 9/12/18.
 */
public class MagneticAnomalyEmitter extends AbstractAnomalyEmitter {
    public MagneticAnomalyEmitter(GameScreen gs) {
        super.setGs(gs);
        super.setRespawnTime(Rules.ANOMALIES_MAGNETIC_RESPAWN_TIME);
        super.setAnomaliesAtStart(Rules.ANOMALIES_AT_START);
        super.setAnomaliesMaxCount(Rules.ANOMALIES_MAGNETIC_MAX_COUNT);
        emit();
    }

    @Override
    protected MagneticAnomaly newObject() {
        return new MagneticAnomaly(super.getGs());
    }
}
