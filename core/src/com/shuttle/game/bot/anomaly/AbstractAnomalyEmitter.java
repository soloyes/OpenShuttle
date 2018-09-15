package com.shuttle.game.bot.anomaly;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.shuttle.game.bot.pool.ObjectPool;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;

import lombok.Data;

/**
 * @author Shuttle on 9/15/18.
 */
@Data
public abstract class AbstractAnomalyEmitter extends ObjectPool<AbstractAnomaly> {
    private GameScreen gs;
    private float time;
    private float respawnTime;
    private int anomaliesAtStart;
    private int anomaliesMaxCount;

    protected abstract AbstractAnomaly newObject();

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt) {
        time += dt;
        if (time > respawnTime) {
            time = 0.0f;
            if (activeList.size() != anomaliesMaxCount)
                createAnomaly();
        }
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }

    protected void createAnomaly() {
        getActiveElement().init(
                MathUtils.random(0, Rules.SPACE_WIDTH),
                MathUtils.random(0, Rules.SPACE_HEIGHT)
        );
    }

    protected void emit() {
        if (anomaliesAtStart != 0) {
            for (int i = 0; i <= anomaliesAtStart; i++) {
                createAnomaly();
            }
        }
    }
}
