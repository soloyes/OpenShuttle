package com.shuttle.game.bot.asteroids;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.conf.GlobalInteractionLevels;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.pool.ObjectPool;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 11/26/18.
 */
public abstract class AbstractAsteroidEmitter extends ObjectPool<AbstractAsteroid> {
    protected GameScreen gs;
    private float time;
    @Getter
    @Setter
    private float respawnTime;
    @Getter
    @Setter
    private int asteroidsMaxCount;
    private boolean boom;
    private  Vector2 tmp1V2;
    private Vector2 position;
    private Vector2 destination;
    private float size;
    private float scale;
    private float count;
    private GlobalInteractionLevels range;

    @Override
    protected abstract AbstractAsteroid newObject();

    public AbstractAsteroidEmitter(){
        this.position = new Vector2();
        this.destination = new Vector2();
        this.tmp1V2 = new Vector2();
    }

    public void boom(Vector2 position, GlobalInteractionLevels range) {
        this.range = range;
        this.count = range.getFragmentAsteroidCount();
        this.position.set(position);
        this.boom = true;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt) {
        time += dt;
        if (time > respawnTime) {
            time = 0.0f;
            if (activeList.size() < asteroidsMaxCount) {
                createAsteroid();
            }
        }

        if (boom) {
            for (int i = 0; i < MathUtils.random(2 * count, 4 * count); i++) {
                size = scale = MathUtils.random(range.getFragmentAsteroidSize1(), range.getFragmentAsteroidSize2());
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
        position.set(tmp1V2);
        destination.set(tmp1V2);

        AbstractAsteroidEmitter.Side side = AbstractAsteroidEmitter.Side.values()[MathUtils.random(0, 3)];
        if (side == AbstractAsteroidEmitter.Side.NORTH) {
            position.set(
                    MathUtils.random(Rules.SPACE_WIDTH / 3, 2 * Rules.SPACE_WIDTH / 3),
                    Rules.SPACE_HEIGHT + PaintConstants.ASTEROID_METAL_HEIGHT / 2
            );
            destination.set(
                    MathUtils.random(0, Rules.SPACE_WIDTH),
                    MathUtils.random(0, Rules.SPACE_HEIGHT / 2)
            );
        }
        if (side == AbstractAsteroidEmitter.Side.SOUTH) {
            position.set(
                    MathUtils.random(Rules.SPACE_WIDTH / 3, 2 * Rules.SPACE_WIDTH / 3),
                    -PaintConstants.ASTEROID_METAL_HEIGHT / 2
            );
            destination.set(
                    MathUtils.random(0, Rules.SPACE_WIDTH),
                    MathUtils.random(Rules.SPACE_HEIGHT / 2, Rules.SPACE_HEIGHT)
            );
        }
        if (side == AbstractAsteroidEmitter.Side.WEST) {
            position.set(
                    -PaintConstants.ASTEROID_METAL_WIDTH / 2,
                    MathUtils.random(Rules.SPACE_HEIGHT / 3, 2 * Rules.SPACE_HEIGHT / 3)
            );
            destination.set(
                    MathUtils.random(Rules.SPACE_WIDTH / 2, Rules.SPACE_WIDTH),
                    MathUtils.random(0, Rules.SPACE_HEIGHT)
            );
        }
        if (side == AbstractAsteroidEmitter.Side.EAST) {
            position.set(
                    Rules.SPACE_WIDTH + PaintConstants.ASTEROID_METAL_WIDTH / 2,
                    MathUtils.random(Rules.SPACE_HEIGHT / 3, 2 * Rules.SPACE_HEIGHT / 3)
            );
            destination.set(
                    MathUtils.random(0, Rules.SPACE_WIDTH / 2),
                    MathUtils.random(0, Rules.SPACE_HEIGHT)
            );
        }
        getActiveElement().init(position, destination, 1.0f, false);
    }

    private void createFragment() {
        destination.set(MathUtils.random(0, Rules.SPACE_WIDTH), MathUtils.random(0, Rules.SPACE_HEIGHT));
        AbstractAsteroid tmpAsteroid = getActiveElement();
        tmpAsteroid.init(position, destination, scale, true);
        tmpAsteroid.setSizeForHarvest(size * Rules.ASTEROID_SIZE_FOR_HARVEST_FACTOR);
    }

    private enum Side {
        NORTH, SOUTH, WEST, EAST
    }
}
