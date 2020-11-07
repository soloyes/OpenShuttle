package com.shuttle.game.bot.rocket;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.anomaly.AbstractAnomaly;
import com.shuttle.game.utils.BoundCheck;
import com.shuttle.game.utils.pool.ObjectPool;

/**
 * @author soloyes on 19/01/19.
 */

public class RocketEmitter extends ObjectPool<Rocket> {
    private BoundCheck boundCheck;

    public RocketEmitter() {
        this(Rocket.class);
        this.boundCheck = new BoundCheck();
    }

    private RocketEmitter(Class<? extends Rocket> clazz) {
        super(clazz);
    }

    @Override
    protected Rocket newObject() {
        return new Rocket();
    }

    public void checkCollision() {
        for (int i = 0; i < activeList.size(); i++) {
            Rocket rocket = activeList.get(i);
            rocket.checkCollision();
        }
    }

    public void createRocket(Vector2 position, AbstractAnomaly anomaly) {
        Rocket rocket = getActiveElement();
        rocket.setTargetAnomaly(anomaly);
        rocket.init(position.x, position.y);
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            Rocket rocket = activeList.get(i);
            rocket.update(dt);
            if (boundCheck.checkAndHandleBounds(rocket)) {
                rocket.destroy();
            }
        }
        checkPool();
    }
}