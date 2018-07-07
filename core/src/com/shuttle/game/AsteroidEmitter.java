package com.shuttle.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Shuttle on 6/30/18.
 */
public class AsteroidEmitter extends ObjectPool<Asteroid> {
    private float time;
    private boolean boom;
    private Vector2 position;
    private Vector2 destanation;
    private float acceleration;
    private float size;
    private float count;

    @Override
    protected Asteroid newObject() {
        return new Asteroid();
    }

    public void boom(Vector2 position, Shuttle.SpeedRang rang) {
        fragmentSetup(rang);
        this.position = position;
        this.boom = true;
    }

    private void fragmentSetup(Shuttle.SpeedRang rang) {
        acceleration = 1f;
        size = 1f;
        count = 1f;
        switch (rang) {
            case NONE:
                acceleration = 1f;
                size = 1f;
                count = 1f;
                break;
            case WHITE:
                acceleration = 1.5f;
                size = 0.8f;
                count = 1.5f;
                break;
            case GREEN:
                acceleration = 2f;
                size = 0.6f;
                count = 2f;
                break;
            case YELLOW:
                acceleration = 2.5f;
                size = 0.6f;
                count = 2.5f;
                break;
            case RED:
                acceleration = 3f;
                size = 0.4f;
                count = 3f;
                break;
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt) {
        time += dt;
        if (time > Rules.ASTEROIDS_RESPAWN_TIME) {
            time = 0.0f;
            createAsteroid(128, 128);
        }

        if (boom) {
            for (int i = 0; i < MathUtils.random(2 * count, 4 * count); i++) {
                createFragment();
            }
            boom = false;
        }

        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }

    public void createAsteroid(int width, int height) {
        position = new Vector2();
        destanation = new Vector2();
        Side side = Side.values()[MathUtils.random(0, 3)];
        if (side == Side.NORTH) {
            position.set(
                    MathUtils.random(Rules.SPACE_WIDTH / 3, 2 * Rules.SPACE_WIDTH / 3),
                    Rules.SPACE_HEIGHT + height / 2
            );
            destanation.set(
                    MathUtils.random(0, Rules.SPACE_WIDTH),
                    MathUtils.random(0, Rules.SPACE_HEIGHT / 2)
            );
        }
        if (side == Side.SOUTH) {
            position.set(
                    MathUtils.random(Rules.SPACE_WIDTH / 3, 2 * Rules.SPACE_WIDTH / 3),
                    -height / 2
            );
            destanation.set(
                    MathUtils.random(0, Rules.SPACE_WIDTH),
                    MathUtils.random(Rules.SPACE_HEIGHT / 2, Rules.SPACE_HEIGHT)
            );
        }
        if (side == Side.WEST) {
            position.set(
                    -width / 2,
                    MathUtils.random(Rules.SPACE_HEIGHT / 3, 2 * Rules.SPACE_HEIGHT / 3)
            );
            destanation.set(
                    MathUtils.random(Rules.SPACE_WIDTH / 2, Rules.SPACE_WIDTH),
                    MathUtils.random(0, Rules.SPACE_HEIGHT)
            );
        }
        if (side == Side.EAST) {
            position.set(
                    Rules.SPACE_WIDTH + width / 2,
                    MathUtils.random(Rules.SPACE_HEIGHT / 3, 2 * Rules.SPACE_HEIGHT / 3)
            );
            destanation.set(
                    MathUtils.random(0, Rules.SPACE_WIDTH / 2),
                    MathUtils.random(0, Rules.SPACE_HEIGHT)
            );
        }
        getActiveElement().init(position, destanation, 1, 1, false);
    }

    public void createFragment() {
        destanation = new Vector2(MathUtils.random(0, Rules.SPACE_WIDTH), MathUtils.random(0, Rules.SPACE_HEIGHT));
        getActiveElement().init(position, destanation, acceleration, size, true);
    }

    private enum Side {
        NORTH, SOUTH, WEST, EAST
    }
}
