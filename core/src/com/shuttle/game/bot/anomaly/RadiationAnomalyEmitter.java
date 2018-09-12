package com.shuttle.game.bot.anomaly;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.shuttle.game.bot.ObjectPool;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;

/**
 * @author Shuttle on 9/12/18.
 */
public class RadiationAnomalyEmitter extends ObjectPool<RadiationAnomaly> {
    private GameScreen gs;
    private float time;

    public RadiationAnomalyEmitter(GameScreen gs) {
        this.gs = gs;
        if (Rules.ANOMALIES_AT_START != 0) {
            for (int i = 0; i <= Rules.ANOMALIES_AT_START; i++) {
                createAnomaly();
            }
        }
    }

    private void createAnomaly() {
        getActiveElement().init(MathUtils.random(0, Rules.SPACE_WIDTH), MathUtils.random(0, Rules.SPACE_HEIGHT));
    }

    @Override
    protected RadiationAnomaly newObject() {
        return new RadiationAnomaly(gs);
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt) {
        time += dt;
        if (time > Rules.ANOMALIES_RADIATION_RESPAWN_TIME) {
            time = 0.0f;
            if (activeList.size() != Rules.ANOMALIES_RADIATION_MAX_COUNT)
            createAnomaly();
        }
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}
