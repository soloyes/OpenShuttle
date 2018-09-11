package com.shuttle.game.bot.anomaly;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.shuttle.game.bot.ObjectPool;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;

public class CorrosiveAnomalyEmitter extends ObjectPool<CorrosiveAnomaly> {
    private GameScreen gs;
    private float time;

    public CorrosiveAnomalyEmitter(GameScreen gs) {
        this.gs = gs;
        for (int i = 0; i <= Rules.ANOMALIES_AT_START; i++) {
            createAnomaly();
        }
    }

    public void createAnomaly() {
        getActiveElement().init(MathUtils.random(0, Rules.SPACE_WIDTH), MathUtils.random(0, Rules.SPACE_HEIGHT));
    }

    @Override
    protected CorrosiveAnomaly newObject() {
        return new CorrosiveAnomaly(gs);
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt) {
        time += dt;
        if (time > Rules.ANOMALIES_CORROSIVE_RESPAWN_TIME) {
            time = 0.0f;
            createAnomaly();
        }
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}
