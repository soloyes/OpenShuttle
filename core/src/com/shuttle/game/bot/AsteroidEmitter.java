package com.shuttle.game.bot;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.ColorLevels;
import com.shuttle.game.Shuttle;
import com.shuttle.game.conf.Rules;

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

    public void boom(Vector2 position, ColorLevels rang) {
        fragmentSetup(rang);
        this.position = position;
        this.boom = true;
    }

    private void fragmentSetup(ColorLevels rang) {
        acceleration = 1.0f;
        switch (rang) {
            case NONE:
                count = 1f;
                size = MathUtils.random(0.25f, 0.5f);
                break;
            case WHITE:
                count = 1.5f;
                size = MathUtils.random(0.16f, 0.3f);
                break;
            case GREEN:
                count = 2f;
                size = MathUtils.random(0.125f, 0.25f);
                break;
            case YELLOW:
                count = 2.5f;
                size = MathUtils.random(0.1f, 0.2f);
                break;
            case RED:
                count = 3f;
                size = MathUtils.random(0.08f, 0.16f);
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
        Asteroid tmpAsteroid = getActiveElement();
        tmpAsteroid.init(position, destanation, acceleration, size, true);
        tmpAsteroid.setSizeForHarvest(size);

    }

    private enum Side {
        NORTH, SOUTH, WEST, EAST
    }
}
