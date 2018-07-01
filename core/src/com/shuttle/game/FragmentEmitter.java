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
    private Shuttle shuttle;

    private float fragmentAcceleration;
    private float fragmentSize;
    private float fragmentCount;

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
            fragmentSetup();
            for (int i = 0; i < MathUtils.random(2 * fragmentCount, 4 * fragmentCount); i++) {
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
        Vector2 velocity = new Vector2(MathUtils.random(-60, 60), MathUtils.random(-60, 60));
        getActiveElement().init(position.x, position.y, velocity, fragmentAcceleration, fragmentSize);
    }

    private void fragmentSetup() {
        fragmentAcceleration = 1f;
        fragmentSize = 1f;
        fragmentCount = 1f;
        switch (shuttle.getSpeedRang()) {
            case NONE:
                fragmentAcceleration*=1f;
                fragmentSize = 1f;
                fragmentCount = 1f;
                break;
            case WHITE:
                fragmentAcceleration*=2f;
                fragmentSize = 0.8f;
                fragmentCount = 1.5f;
                break;
            case GREEN:
                fragmentAcceleration*=4f;
                fragmentSize = 0.6f;
                fragmentCount = 2f;
                break;
            case YELLOW:
                fragmentAcceleration*=8f;
                fragmentSize = 0.6f;
                fragmentCount = 2.5f;
                break;
            case RED:
                fragmentAcceleration*=16f;
                fragmentSize = 0.4f;
                fragmentCount = 3f;
                break;
        }
    }

    public void setShuttle(Shuttle shuttle) {
        this.shuttle = shuttle;
    }
}
