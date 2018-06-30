package com.shuttle.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Shuttle on 6/30/18.
 */
public class AsteroidEmitter extends ObjectPool<Asteroid> {
    private float time;

    public void createAsteroid(int width, int height) {
        float x = 0;
        float y = 0;
        Vector2 destanation = new Vector2();
        Side side = Side.values()[MathUtils.random(0, 3)];
        if (side == Side.NORTH) {
            y = Rules.SPACE_HEIGHT;
            x = MathUtils.random(Rules.SPACE_HEIGHT / 3, 2 * Rules.SPACE_HEIGHT / 3);
            destanation.set(
                    MathUtils.random(0, Rules.SPACE_WIDTH),
                    MathUtils.random(0, Rules.SPACE_HEIGHT / 2)
            );
        }
        if (side == Side.SOUTH) {
            y = -height;
            x = MathUtils.random(Rules.SPACE_WIDTH / 3, 2 * Rules.SPACE_WIDTH / 3);
            destanation.set(
                    MathUtils.random(0, Rules.SPACE_WIDTH),
                    MathUtils.random(Rules.SPACE_HEIGHT / 2, Rules.SPACE_HEIGHT)
            );
        }
        if (side == Side.WEST) {
            x = -width;
            y = MathUtils.random(Rules.SPACE_HEIGHT / 3, 2 * Rules.SPACE_HEIGHT / 3);
            destanation.set(
                    MathUtils.random(Rules.SPACE_WIDTH / 2, Rules.SPACE_WIDTH),
                    MathUtils.random(0, Rules.SPACE_HEIGHT)
            );
        }
        if (side == Side.EAST) {
            x = Rules.SPACE_WIDTH;
            y = MathUtils.random(Rules.SPACE_HEIGHT / 3, 2 * Rules.SPACE_HEIGHT / 3);
            destanation.set(
                    MathUtils.random(0, Rules.SPACE_WIDTH / 2),
                    MathUtils.random(0, Rules.SPACE_HEIGHT)
            );
        }
        getActiveElement().init(x, y, destanation);
    }

    @Override
    protected Asteroid newObject() {
        return new Asteroid();
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
            createAsteroid(128, 128);
        }
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }

    private enum Side {
        NORTH, SOUTH, WEST, EAST
    }
}
