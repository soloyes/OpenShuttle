package com.shuttle.game.bot.anomaly;

import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;

/**
 * @author Shuttle on 9/11/18.
 */
public class ElectricalAnomalyEmitter extends AbstractAnomalyEmitter {
    public ElectricalAnomalyEmitter(GameScreen gs) {
        super.gs = gs;
        super.setRespawnTime(Rules.ANOMALIES_ELECTRIC_RESPAWN_TIME);
        super.setAnomaliesAtStart(Rules.ANOMALIES_ELECTRIC_AT_START);
        super.setAnomaliesMaxCount(Rules.ANOMALIES_ELECTRIC_MAX_COUNT);
        emit();
    }

    @Override
    protected ElectricalAnomaly newObject() {
        return new ElectricalAnomaly();
    }

    public void createBonusAnomaly(Vector2 position) {
        AbstractAnomaly anomaly = getActiveElement();
        anomaly.init(position.x, position.y);
    }
}
