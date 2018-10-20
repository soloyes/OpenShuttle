package com.shuttle.game.bot.consumable;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.shuttle.game.bot.pool.ObjectPool;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;

import lombok.Getter;
import lombok.Setter;

public class ConsumableEmitter extends ObjectPool<Consumable> {
    @Getter
    @Setter
    private float respawnTime;
    private GameScreen gs;

    public ConsumableEmitter(GameScreen gs) {
        this.gs = gs;
        if (Rules.CONSUMABLE_AT_START != 0) {
            for (int i = 0; i <= Rules.CONSUMABLE_AT_START; i++) {
                createConsumable();
            }
        }
    }

    public void createConsumable() {
        getActiveElement().init(
                MathUtils.random(0, Rules.SPACE_WIDTH),
                MathUtils.random(0, Rules.SPACE_HEIGHT)
        );
    }

    @Override
    protected Consumable newObject() {
        int i = 0;//MathUtils.random(0, 1);
        switch (i) {
            case 0:
                return new Astronaut(gs);
            default:
                return new Crate(gs);
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