package com.shuttle.game.bot.anomaly;

import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;

/**
 * @author Shuttle on 9/11/18.
 */
public class ElectricalAnomalyEmitter extends AbstractAnomalyEmitter {
    public ElectricalAnomalyEmitter(GameScreen gs) {
        super.setGs(gs);
        super.setRespawnTime(Rules.ANOMALIES_ELECTRIC_RESPAWN_TIME);
        super.setAnomaliesAtStart(Rules.ANOMALIES_AT_START);
        super.setAnomaliesMaxCount(Rules.ANOMALIES_ELECTRIC_MAX_COUNT);
        emit();
    }

    @Override
    protected ElectricalAnomaly newObject() {
        return new ElectricalAnomaly(super.getGs());
    }
}
