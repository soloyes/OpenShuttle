package com.shuttle.game.bot.anomaly;

import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;

/**
 * @author Shuttle on 9/12/18.
 */
public class RadiationAnomalyEmitter extends AbstractAnomalyEmitter {
    public RadiationAnomalyEmitter(GameScreen gs) {
        super.setGs(gs);
        super.setRespawnTime(Rules.ANOMALIES_RADIATION_RESPAWN_TIME);
        super.setAnomaliesAtStart(Rules.ANOMALIES_AT_START);
        super.setAnomaliesMaxCount(Rules.ANOMALIES_RADIATION_MAX_COUNT);
        emit();
    }

    @Override
    protected RadiationAnomaly newObject() {
        return new RadiationAnomaly(super.getGs());
    }
}
