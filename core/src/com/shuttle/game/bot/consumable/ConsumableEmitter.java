package com.shuttle.game.bot.consumable;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.shuttle.game.bot.pool.ObjectPool;
import com.shuttle.game.conf.Rules;

import lombok.Getter;
import lombok.Setter;

public class ConsumableEmitter extends ObjectPool<Consumable> {
    @Getter
    @Setter
    private float respawnTime;

    public ConsumableEmitter() {
        if (Rules.CONSUMABLE_AT_START != 0) {
            for (int i = 0; i <= Rules.CONSUMABLE_AT_START; i++) {
                createConsumable();
            }
        }
    }

    public void createConsumable() {
        getActiveElement().init(MathUtils.random(0, Rules.SPACE_WIDTH), MathUtils.random(0, Rules.SPACE_HEIGHT));
    }

    @Override
    protected Consumable newObject() {
        int i = MathUtils.random(0, 1);
        switch (i) {
            case 1:
                return new Astronaut();
            default:
                return new Crate();
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt) {
        respawnTime += dt;
        if (respawnTime > Rules.CONSUMABLE_RESPAWN_TIME && !Rules.GAME_OVER) {
            respawnTime = 0.0f;
            createConsumable();
        }
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}