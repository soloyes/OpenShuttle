package com.shuttle.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.shuttle.game.screens.GameScreen;

public class AlienEmitter extends ObjectPool<Alien> {
    private GameScreen gs;
    private float time;

    public AlienEmitter(GameScreen gs) {
        super();
        this.gs = gs;
        for (int i = 0; i < 20; i++) {
            createAlien();
        }
    }

    @Override
    protected Alien newObject() {
        return new Alien(gs);
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

    public void createAlien() {
        getActiveElement().init(MathUtils.random(0, Rules.SPACE_WIDTH), MathUtils.random(0, Rules.SPACE_HEIGHT));
    }
}
