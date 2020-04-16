package com.shuttle.game.bot.consumable;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.utils.pool.ObjectPool;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 02/11/18.
 */

public abstract class AbstractConsumableEmitter extends ObjectPool<AbstractConsumable> {
    private float time;
    @Getter
    @Setter
    private float respawnTime;
    @Getter
    @Setter
    private int consumablesAtStart;
    @Getter
    @Setter
    private int consumablesMaxCount;

    AbstractConsumableEmitter(Class<? extends AbstractConsumable> clazz) {
        super(clazz);
    }

    protected abstract AbstractConsumable newObject();

    public void emit() {
        if (consumablesAtStart != 0) {
            for (int i = 0; i < consumablesAtStart; i++) {
                createConsumable();
            }
        }
    }

    protected void createConsumable() {
        getActiveElement().init(
                MathUtils.random(0, Rules.SPACE_WIDTH),
                MathUtils.random(0, Rules.SPACE_HEIGHT)
        );
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt) {
        time += dt;
        if (time > respawnTime && !Rules.GAME_OVER) {
            time = 0.0f;
            if (activeList.size() < consumablesMaxCount)
                createConsumable();
        }
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}