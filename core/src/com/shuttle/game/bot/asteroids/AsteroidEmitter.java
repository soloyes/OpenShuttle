package com.shuttle.game.bot.asteroids;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.pool.ObjectPool;
import com.shuttle.game.conf.GlobalInteractionLevels;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.effects.BoomBox;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 6/30/18.
 */
public class AsteroidEmitter extends ObjectPool<Asteroid> {
    @Getter
    @Setter
    private float respawnTime;
    private boolean boom;
    private Vector2 position;
    private Vector2 destination;
    private float size;
    private float scale;
    private float count;
    private GlobalInteractionLevels rang;
    private GameScreen gs;
    private BoomBox boomBox;

    public AsteroidEmitter(GameScreen gs) {
        this.gs = gs;
        this.boomBox = new BoomBox();
    }

    @Override
    protected Asteroid newObject() {
        return new Asteroid(gs);
    }

    public void boom(Vector2 position, GlobalInteractionLevels rang) {
        this.rang = rang;
        this.count = rang.getFragmentAsteroidCount();
        this.position = position;
        this.boom = true;
        this.boomBox.stopMusicEffect(MSConstants.LASER);
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt) {
        respawnTime += dt;
        if (respawnTime > Rules.ASTEROID_RESPAWN_TIME) {
            respawnTime = 0.0f;
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
        destination = new Vector2();
        Side side = Side.values()[MathUtils.random(0, 3)];
        if (side == Side.NORTH) {
            position.set(
                    MathUtils.random(Rules.SPACE_WIDTH / 3, 2 * Rules.SPACE_WIDTH / 3),
                    Rules.SPACE_HEIGHT + PaintConstants.ASTEROID_HEIGHT / 2
            );
            destination.set(
                    MathUtils.random(0, Rules.SPACE_WIDTH),
                    MathUtils.random(0, Rules.SPACE_HEIGHT / 2)
            );
        }
        if (side == Side.SOUTH) {
            position.set(
                    MathUtils.random(Rules.SPACE_WIDTH / 3, 2 * Rules.SPACE_WIDTH / 3),
                    -PaintConstants.ASTEROID_HEIGHT / 2
            );
            destination.set(
                    MathUtils.random(0, Rules.SPACE_WIDTH),
                    MathUtils.random(Rules.SPACE_HEIGHT / 2, Rules.SPACE_HEIGHT)
            );
        }
        if (side == Side.WEST) {
            position.set(
                    -PaintConstants.ASTEROID_WIDTH / 2,
                    MathUtils.random(Rules.SPACE_HEIGHT / 3, 2 * Rules.SPACE_HEIGHT / 3)
            );
            destination.set(
                    MathUtils.random(Rules.SPACE_WIDTH / 2, Rules.SPACE_WIDTH),
                    MathUtils.random(0, Rules.SPACE_HEIGHT)
            );
        }
        if (side == Side.EAST) {
            position.set(
                    Rules.SPACE_WIDTH + PaintConstants.ASTEROID_WIDTH / 2,
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
        Asteroid tmpAsteroid = getActiveElement();
        tmpAsteroid.init(position, destination, scale, true);
        tmpAsteroid.setSizeForHarvest(size * Rules.ASTEROID_SIZE_FOR_HARVEST_FACTOR);
    }

    private enum Side {
        NORTH, SOUTH, WEST, EAST
    }
}
