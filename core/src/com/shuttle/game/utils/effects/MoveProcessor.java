package com.shuttle.game.utils.effects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.anomaly.AbstractAnomaly;
import com.shuttle.game.bot.asteroids.Asteroid;
import com.shuttle.game.bot.consumable.Consumable;
import com.shuttle.game.bot.pool.AbstractPoolable;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;

/**
 * @author Shuttle on 10/28/18.
 */
public class MoveProcessor {
    private Vector2 tmp;
    private GameScreen gs;
    private float angle;
    private float radius;

    public MoveProcessor(GameScreen gs) {
        this.gs = gs;
        this.tmp = new Vector2();
    }

    public void update(Consumable consumable, float dt) {
        if (consumable.isTaken()) {
            //*Standard move*//
            radius = radius - dt * 60;
            consumable.getPosition().x = gs.getBlackhole().getPosition().x + radius * MathUtils.cos(angle);
            consumable.getPosition().y = gs.getBlackhole().getPosition().y + radius * MathUtils.sin(angle);
            angle += dt;
        } else {
            archimedesInit(consumable);
        }
    }

    public void archimedesInit(AbstractPoolable abstractPoolable) {
        tmp.set(abstractPoolable.getPosition()).sub(gs.getBlackhole().getPosition());
        angle = tmp.angle() * MathUtils.degreesToRadians;
        radius = tmp.len();
    }

    public void update(AbstractAnomaly abstractAnomaly, float dt) {
        abstractAnomaly.getVelocity().scl(/*FPS hack*/1.0f - dt/*FPS hack*/);
        if (!abstractAnomaly.isTaken()) {
            //*Standard move*//
            if (abstractAnomaly.isHungry() && abstractAnomaly.isActivated()) {
                abstractAnomaly.getPosition().mulAdd(abstractAnomaly.getVelocity(), dt);
                float dst = gs.getPlayer().getPosition().dst(abstractAnomaly.getPosition());
                if (dst < 2400.0f + 200 * abstractAnomaly.getLevel()) {
                    tmp.set(gs.getPlayer()
                            .getPosition())
                            .sub(abstractAnomaly.getPosition())
                            .nor()
                            .scl(abstractAnomaly.getV()
                            );
                    abstractAnomaly.getVelocity().mulAdd(tmp, dt);
                }
                if (dst < 240 * abstractAnomaly.getLevel()) {
                    tmp.set(gs.getPlayer()
                            .getPosition())
                            .sub(abstractAnomaly.getPosition())
                            .nor()
                            .scl(250.0f - 20 * abstractAnomaly.getLevel()
                            );
                    abstractAnomaly.getVelocity().mulAdd(tmp, dt);
                }
            }
        } else {
            //*Archimedes spiral*//
            radius = radius - dt * 60;
            abstractAnomaly.getPosition().x = gs.getBlackhole().getPosition().x + radius * MathUtils.cos(angle);
            abstractAnomaly.getPosition().y = gs.getBlackhole().getPosition().y + radius * MathUtils.sin(angle);
            angle += dt;
        }
    }

    public void update(Asteroid asteroid, float dt) {
        if (!asteroid.isTaken()) {
            //*Standard move*//
            asteroid.getPosition().mulAdd(asteroid.getVelocity(), dt);
            archimedesInit(asteroid);
        } else {
            //*Archimedes spiral*//
            radius = radius - dt * 60;
            asteroid.getPosition().x = gs.getBlackhole().getPosition().x + radius * MathUtils.cos(angle);
            asteroid.getPosition().y = gs.getBlackhole().getPosition().y + radius * MathUtils.sin(angle);
            angle += dt;
        }
    }
}