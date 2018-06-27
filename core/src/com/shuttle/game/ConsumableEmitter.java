package com.shuttle.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.shuttle.game.screens.GameScreen;

public class ConsumableEmitter extends ObjectPool<Consumable> {
    private GameScreen gs;
    private float time;

    public ConsumableEmitter(GameScreen gs) {
        this.gs = gs;
        for (int i = 0; i < 20; i++) {
            createBot();
        }
    }

    @Override
    protected Consumable newObject() {
        return new Consumable(gs);
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt) {
        time += dt;
        if (time > 5.0f) {
            time = 0.0f;
            createBot();
        }
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }

    public void createBot() {
        getActiveElement().init(MathUtils.random(0, Rules.SPACE_WIDTH), MathUtils.random(0, Rules.SPACE_HEIGHT));
    }
}
