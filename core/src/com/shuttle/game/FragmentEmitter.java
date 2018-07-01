package com.shuttle.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Shuttle on 7/1/18.
 */
public class FragmentEmitter extends ObjectPool<Fragment> {
    private boolean boom = false;
    private Vector2 position;

    @Override
    protected Fragment newObject() {
        return new Fragment();
    }

    public void boom(Vector2 position) {
        boom = true;
        this.position = position;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt) {
        if (boom) {
            for (int i = 0; i < MathUtils.random(3, 7); i++) {
                createFragment();
            }
            boom = false;
        }
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }

    public void createFragment() {
        Vector2 velocity = new Vector2(MathUtils.random(-70, 70), MathUtils.random(-70, 70));
        getActiveElement().init(position.x, position.y, velocity);
    }
}
