package com.shuttle.game.bot.anomaly;

import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;

/**
 * @author Shuttle on 9/12/18.
 */
public class MagneticAnomalyEmitter extends AbstractAnomalyEmitter {
    public MagneticAnomalyEmitter(GameScreen gs) {
        super.gs = gs;
        super.setRespawnTime(Rules.ANOMALIES_MAGNETIC_RESPAWN_TIME);
        super.setAnomaliesAtStart(Rules.ANOMALIES_MAGNETIC_AT_START);
        super.setAnomaliesMaxCount(Rules.ANOMALIES_MAGNETIC_MAX_COUNT);
        emit();
    }

    @Override
    protected MagneticAnomaly newObject() {
        return new MagneticAnomaly(gs);
    }

    public void createBonusAnomaly(Vector2 position) {
        AbstractAnomaly anomaly = getActiveElement();
        anomaly.init(position.x, position.y);
    }
}
