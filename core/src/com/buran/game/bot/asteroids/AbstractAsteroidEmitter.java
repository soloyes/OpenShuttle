package com.buran.game.bot.asteroids;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.buran.game.conf.PaintConstants;
import com.buran.game.conf.PlayerInteractionLevels;
import com.buran.game.conf.Rules;
import com.buran.game.effects.particle.ParticleEmitter;
import com.buran.game.ui.help.HandPointer;
import com.buran.game.utils.pool.ObjectPool;

import lombok.Getter;
import lombok.Setter;

/**
 * @author soloyes on 11/26/18.
 */

public abstract class AbstractAsteroidEmitter extends ObjectPool<AbstractAsteroid> {
    private float time;
    @Getter
    @Setter
    private float respawnTime;
    @Getter
    @Setter
    private int asteroidsMaxCount;
    private boolean boom;
    private final com.badlogic.gdx.utils.Array<Vector2> pendingBooms = new com.badlogic.gdx.utils.Array<Vector2>();
    private final com.badlogic.gdx.utils.Array<PlayerInteractionLevels> pendingRanges = new com.badlogic.gdx.utils.Array<PlayerInteractionLevels>();
    private Vector2 tmp1V2;
    private Vector2 position;
    private Vector2 destination;
    private float size;
    private float scale;
    private float count;
    private PlayerInteractionLevels range;
    @Getter
    private static int emitted;
    protected ParticleEmitter particleEmitter;

    AbstractAsteroidEmitter(Class<? extends AbstractAsteroid> clazz, ParticleEmitter particleEmitter) {
        super(clazz);
        this.position = new Vector2();
        this.destination = new Vector2();
        this.tmp1V2 = new Vector2();
        this.particleEmitter = particleEmitter;
        emitted = 0;
    }

    @Override
    protected abstract AbstractAsteroid newObject();

    @Override
    public void boom(Vector2 position, PlayerInteractionLevels range) {
        // Several asteroids can burst in one frame; queue them all instead of keeping the last.
        pendingBooms.add(new Vector2(position));
        pendingRanges.add(range);
        this.boom = true;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            float d = activeList.get(i).getDissolve();
            boolean dis = d > 0f && com.buran.game.effects.Dissolve.begin(batch, d);
            activeList.get(i).render(batch);
            if (dis) {
                com.buran.game.effects.Dissolve.end(batch);
            }
        }
    }

    public void update(float dt) {
        if (HandPointer.done) {
            time += dt;
        }
        if (time > respawnTime) {
            time = 0.0f;
            if (activeList.size() < asteroidsMaxCount) {
                createAsteroid();
            }
        }

        if (boom) {
            for (int b = 0; b < pendingBooms.size; b++) {
                this.range = pendingRanges.get(b);
                this.count = range.getFragmentAsteroidCount();
                this.position.set(pendingBooms.get(b));
                int fragments = MathUtils.random((int) (2 * count), (int) (4 * count));
                for (int i = 0; i < fragments; i++) {
                    scale = size = range.getSize();
                    createFragment();
                }
            }
            pendingBooms.clear();
            pendingRanges.clear();
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
        emitted++;
    }

    private enum Side {
        NORTH, SOUTH, WEST, EAST
    }
}