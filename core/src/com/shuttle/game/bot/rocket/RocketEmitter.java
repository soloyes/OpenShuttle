package com.shuttle.game.bot.rocket;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.anomaly.AbstractAnomaly;
import com.shuttle.game.utils.BoundCheck;
import com.shuttle.game.utils.pool.ObjectPool;

/**
 * @author Shuttle on 19/01/19.
 */
public class RocketEmitter extends ObjectPool<Rocket> {
    private BoundCheck boundCheck;

    @Override
    protected Rocket newObject() {
        return new Rocket();
    }

    public RocketEmitter() {
        this.boundCheck = new BoundCheck();
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

    public void createRocket(Vector2 position, AbstractAnomaly anomaly) {
        Rocket rocket = getActiveElement();
        rocket.setTargetAnomaly(anomaly);
        rocket.init(position.x, position.y);
    }

    public void checkCollision() {
        for (int i = 0; i < activeList.size(); i++) {
            Rocket rocket = activeList.get(i);
            rocket.checkCollision();
        }
    }
}
