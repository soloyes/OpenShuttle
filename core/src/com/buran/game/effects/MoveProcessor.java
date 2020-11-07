package com.buran.game.effects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.buran.game.player.Buran;

/**
 * @author soloyes on 10/28/18.
 */

public class MoveProcessor {
    private float angle;
    private float radius;
    private float length;
    private Vector2 tmp1V2;

    public MoveProcessor() {
        this.tmp1V2 = new Vector2();
    }

    public void update(com.buran.game.bot.consumable.AbstractConsumable abstractConsumable, float dt) {
        if (abstractConsumable.isTaken()) {
            //*Archimedes spiral*//
            radius = radius - dt * abstractConsumable.getSpiralRadius();
            abstractConsumable.getPosition().x = com.buran.game.bot.blackhole.Blackhole.position.x + radius * MathUtils.cos(angle);
            abstractConsumable.getPosition().y = com.buran.game.bot.blackhole.Blackhole.position.y + radius * MathUtils.sin(angle);
            angle += dt;
            intoTheDark(abstractConsumable);
        } else {
            archimedesInit(abstractConsumable);
        }
        update(abstractConsumable);
    }

    private void intoTheDark(com.buran.game.utils.pool.AbstractPoolable abstractPoolable) {
        float percentage = 1.0f - abstractPoolable.getPosition().dst(com.buran.game.bot.blackhole.Blackhole.position) / length;
        float currentR = MathUtils.lerp(1.0f, 0.5f, 2 * percentage);
        float currentG = MathUtils.lerp(1.0f, 0.5f, 2 * percentage);
        float currentA = MathUtils.lerp(1.0f, 0.5f, 2 * percentage);
        abstractPoolable.getColor().r = currentR;
        abstractPoolable.getColor().g = currentG;
        abstractPoolable.getColor().a = currentA;
    }

    public void archimedesInit(com.buran.game.utils.pool.AbstractPoolable abstractPoolable) {
        tmp1V2.set(abstractPoolable.getPosition()).sub(com.buran.game.bot.blackhole.Blackhole.position);
        angle = tmp1V2.angle() * MathUtils.degreesToRadians;
        radius = tmp1V2.len();
        length = radius;
    }

    private void update(com.buran.game.utils.pool.AbstractPoolable abstractPoolable) {
        if (abstractPoolable.isActive()) {
            abstractPoolable.collideBlackhole();
        }
    }

    public void update(com.buran.game.bot.anomaly.AbstractAnomaly abstractAnomaly, float dt) {
        abstractAnomaly.getVelocity().scl(/*FPS hack*/1.0f - dt/*FPS hack*/);
        if (!abstractAnomaly.isTaken()) {
            //*Standard move*//
            if (abstractAnomaly.isHungry() && abstractAnomaly.isActivated()) {
                abstractAnomaly.getPosition().mulAdd(abstractAnomaly.getVelocity(), dt);
                float dst = Buran.position.dst(abstractAnomaly.getPosition());
                if (abstractAnomaly.getLevel() + 1 != (int) com.buran.game.player.Standing.getInstance().getRamPower()) {
                    if (dst < 2400.0f + 200 * abstractAnomaly.getLevel()) {
                        tmp1V2.set(Buran.position)
                                .sub(abstractAnomaly.getPosition())
                                .nor()
                                .scl(abstractAnomaly.getV());
                        abstractAnomaly.getVelocity().mulAdd(tmp1V2, dt);
                    }
                    if (dst < 240 * abstractAnomaly.getLevel()) {
                        tmp1V2.set(Buran.position)
                                .sub(abstractAnomaly.getPosition())
                                .nor()
                                .scl(250.0f - 20 * abstractAnomaly.getLevel()
                                );
                        abstractAnomaly.getVelocity().mulAdd(tmp1V2, dt);
                    }
                    //Anomaly afraid related shield level
                } else {
                    tmp1V2.set(Buran.position)
                            .sub(abstractAnomaly.getPosition())
                            .nor()
                            .scl(abstractAnomaly.getV() / com.buran.game.conf.Rules.ANOMALIES_CORROSIVE_AFRAID_FACTOR);
                    abstractAnomaly.getVelocity().mulAdd(tmp1V2, dt);
                }
            }
        } else {
            //*Archimedes spiral*//
            radius = radius - dt * abstractAnomaly.getSpiralRadius();
            abstractAnomaly.getPosition().x = com.buran.game.bot.blackhole.Blackhole.position.x + radius * MathUtils.cos(angle);
            abstractAnomaly.getPosition().y = com.buran.game.bot.blackhole.Blackhole.position.y + radius * MathUtils.sin(angle);
            angle += dt;
        }
        update(abstractAnomaly);
    }

    public void update(com.buran.game.bot.asteroids.AbstractAsteroid asteroid, float dt) {
        if (!asteroid.isTaken()) {
            //*Standard move*//
            asteroid.getPosition().mulAdd(asteroid.getVelocity(), dt);
            archimedesInit(asteroid);
        } else {
            //*Archimedes spiral*//
            radius = radius - dt * asteroid.getSpiralRadius();
            asteroid.getPosition().x = com.buran.game.bot.blackhole.Blackhole.position.x + radius * MathUtils.cos(angle);
            asteroid.getPosition().y = com.buran.game.bot.blackhole.Blackhole.position.y + radius * MathUtils.sin(angle);
            angle += dt;

            intoTheDark(asteroid);
        }
        update(asteroid);
    }
}