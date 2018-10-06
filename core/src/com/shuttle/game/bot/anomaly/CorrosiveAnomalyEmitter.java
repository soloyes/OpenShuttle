package com.shuttle.game.bot.anomaly;

import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;

/**
 * @author Shuttle on 9/11/18.
 */
public class CorrosiveAnomalyEmitter extends AbstractAnomalyEmitter {
    public CorrosiveAnomalyEmitter(GameScreen gs) {
        super.gs = gs;
        super.setRespawnTime(Rules.ANOMALIES_CORROSIVE_RESPAWN_TIME);
        super.setAnomaliesAtStart(Rules.ANOMALIES_CORROSIVE_AT_START);
        super.setAnomaliesMaxCount(Rules.ANOMALIES_CORROSIVE_MAX_COUNT);
        emit();
    }

    @Override
    protected CorrosiveAnomaly newObject() {
        return new CorrosiveAnomaly(gs);
    }
}
