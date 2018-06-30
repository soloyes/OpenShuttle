package com.shuttle.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class AlienEmitter extends ObjectPool<Alien> {
    private float time;

    public AlienEmitter() {
        for (int i = 0; i < 20; i++) {
            createAlien();
        }
    }

    public void createAlien() {
        getActiveElement().init(MathUtils.random(0, Rules.SPACE_WIDTH), MathUtils.random(0, Rules.SPACE_HEIGHT));
    }

    @Override
    protected Alien newObject() {
        return new Alien(/*gs*/);
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
            createAlien();
        }
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}
