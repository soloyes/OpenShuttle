package com.shuttle.game.bot;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.shuttle.game.conf.Rules;

public class ConsumableEmitter extends ObjectPool<Consumable> {
    private float time;

    public ConsumableEmitter() {
        if (Rules.CONSUMABLES_AT_START != 0) {
            for (int i = 0; i <= Rules.CONSUMABLES_AT_START; i++) {
                createBot();
            }
        }
    }

    public void createBot() {
        getActiveElement().init(MathUtils.random(0, Rules.SPACE_WIDTH), MathUtils.random(0, Rules.SPACE_HEIGHT));
    }

    @Override
    protected Consumable newObject() {
        return new Consumable();
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt) {
        time += dt;
        if (time > Rules.CONSUMABLES_RESPAWN_TIME) {
            time = 0.0f;
            createBot();
        }
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}