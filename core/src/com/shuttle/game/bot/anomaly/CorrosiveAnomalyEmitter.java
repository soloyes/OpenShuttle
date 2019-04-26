package com.shuttle.game.bot.anomaly;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
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
        return new CorrosiveAnomaly();
    }

    public void createBonusAnomaly(Vector2 position, int level) {
        AbstractAnomaly anomaly = getActiveElement();
        anomaly.init(position.x, position.y);
        anomaly.setLevel(level);
    }

    public void createBonusAnomaly(int level) {
        AbstractAnomaly anomaly = getActiveElement();
        anomaly.init(
                MathUtils.random(0, Rules.SPACE_WIDTH),
                MathUtils.random(0, Rules.SPACE_HEIGHT)
        );
        anomaly.setLevel(level);
    }
}
