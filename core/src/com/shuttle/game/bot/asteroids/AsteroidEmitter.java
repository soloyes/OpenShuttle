package com.shuttle.game.bot.asteroids;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.pool.ObjectPool;
import com.shuttle.game.conf.GlobalInteractionLevels;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;

/**
 * @author Shuttle on 6/30/18.
 */
public class AsteroidEmitter extends ObjectPool<Asteroid> {
    private float time;
    private boolean boom;
    private Vector2 position;
    private Vector2 destanation;
    private float size;
    private float scale;
    private float count;
    private GlobalInteractionLevels rang;

    @Override
    protected Asteroid newObject() {
        return new Asteroid();
    }

    public void boom(Vector2 position, GlobalInteractionLevels rang) {
        this.rang = rang;
        this.count = rang.getFragmentAsteroidCount();
        this.position = position;
        this.boom = true;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt) {
        time += dt;
        if (time > Rules.ASTEROID_RESPAWN_TIME) {
            time = 0.0f;
            createAsteroid();
        }

        if (boom) {
            for (int i = 0; i < MathUtils.random(2 * count, 4 * count); i++) {
                size = scale = MathUtils.random(rang.getFragmentAsteroidSize1(), rang.getFragmentAsteroidSize2());
                createFragment();
            }
            boom = false;
        }

        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }

    private void createAsteroid() {
        position = new Vector2();
        destanation = new Vector2();
        Side side = Side.values()[MathUtils.random(0, 3)];
        if (side == Side.NORTH) {
            position.set(
                    MathUtils.random(Rules.SPACE_WIDTH / 3, 2 * Rules.SPACE_WIDTH / 3),
                    Rules.SPACE_HEIGHT + PaintConstants.ASTEROID_HEIGHT / 2
            );
            destanation.set(
                    MathUtils.random(0, Rules.SPACE_WIDTH),
                    MathUtils.random(0, Rules.SPACE_HEIGHT / 2)
            );
        }
        if (side == Side.SOUTH) {
            position.set(
                    MathUtils.random(Rules.SPACE_WIDTH / 3, 2 * Rules.SPACE_WIDTH / 3),
                    -PaintConstants.ASTEROID_HEIGHT / 2
            );
            destanation.set(
                    MathUtils.random(0, Rules.SPACE_WIDTH),
                    MathUtils.random(Rules.SPACE_HEIGHT / 2, Rules.SPACE_HEIGHT)
            );
        }
        if (side == Side.WEST) {
            position.set(
                    -PaintConstants.ASTEROID_WIDTH / 2,
                    MathUtils.random(Rules.SPACE_HEIGHT / 3, 2 * Rules.SPACE_HEIGHT / 3)
            );
            destanation.set(
                    MathUtils.random(Rules.SPACE_WIDTH / 2, Rules.SPACE_WIDTH),
                    MathUtils.random(0, Rules.SPACE_HEIGHT)
            );
        }
        if (side == Side.EAST) {
            position.set(
                    Rules.SPACE_WIDTH + PaintConstants.ASTEROID_WIDTH / 2,
                    MathUtils.random(Rules.SPACE_HEIGHT / 3, 2 * Rules.SPACE_HEIGHT / 3)
            );
            destanation.set(
                    MathUtils.random(0, Rules.SPACE_WIDTH / 2),
                    MathUtils.random(0, Rules.SPACE_HEIGHT)
            );
        }
        getActiveElement().init(position, destanation, 1.0f, false);
    }

    private void createFragment() {
        destanation.set(MathUtils.random(0, Rules.SPACE_WIDTH), MathUtils.random(0, Rules.SPACE_HEIGHT));
        Asteroid tmpAsteroid = getActiveElement();
        tmpAsteroid.init(position, destanation, scale, true);
        tmpAsteroid.setSizeForHarvest(size);
    }

    private enum Side {
        NORTH, SOUTH, WEST, EAST
    }
}
