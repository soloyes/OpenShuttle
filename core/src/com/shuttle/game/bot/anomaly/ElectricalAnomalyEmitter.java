package com.shuttle.game.bot.anomaly;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.shuttle.game.bot.ObjectPool;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;

/**
 * @author Shuttle on 9/11/18.
 */
public class ElectricalAnomalyEmitter extends ObjectPool<ElectricalAnomaly> {
    private GameScreen gs;
    private float time;

    public ElectricalAnomalyEmitter(GameScreen gs) {
        this.gs = gs;
        for (int i = 0; i <= Rules.ANOMALIES_AT_START; i++) {
            createAnomaly();
        }
    }

    public void createAnomaly() {
        getActiveElement().init(MathUtils.random(0, Rules.SPACE_WIDTH), MathUtils.random(0, Rules.SPACE_HEIGHT));
    }

    @Override
    protected ElectricalAnomaly newObject() {
        return new ElectricalAnomaly(gs);
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt) {
        time += dt;
        if (time > Rules.ANOMALIES_ELECTRIC_RESPAWN_TIME) {
            time = 0.0f;
            createAnomaly();
        }
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}
