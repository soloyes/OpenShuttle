package com.shuttle.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class AstronautEmitter extends ObjectPool<Astronaut> {
    private GameScreen gs;
    private float time;

    public AstronautEmitter(GameScreen gs) {
        this.gs = gs;

    }

    @Override
    protected Astronaut newObject() {
        return new Astronaut(gs);
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
