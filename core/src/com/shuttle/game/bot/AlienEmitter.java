package com.shuttle.game.bot;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;

public class AlienEmitter extends ObjectPool<Alien> {
    private GameScreen gs;
    private float time;

    public AlienEmitter(GameScreen gs) {
        this.gs = gs;
        for (int i = 0; i < Rules.ALIENS_AT_START; i++) {
            createAlien();
        }
    }

    public void createAlien() {
        getActiveElement().init(MathUtils.random(0, Rules.SPACE_WIDTH), MathUtils.random(0, Rules.SPACE_HEIGHT));
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
        if (time > Rules.ALIENS_RESPAWN_TIME) {
            time = 0.0f;
            createAlien();
        }
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}
