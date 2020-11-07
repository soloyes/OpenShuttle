package com.buran.game.effects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.buran.game.bot.anomaly.AbstractAnomaly;
import com.buran.game.bot.asteroids.AbstractAsteroid;
import com.buran.game.bot.blackhole.Blackhole;
import com.buran.game.bot.consumable.AbstractConsumable;
import com.buran.game.conf.Rules;
import com.buran.game.player.Buran;
import com.buran.game.player.Standing;
import com.buran.game.utils.pool.AbstractPoolable;

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

    public void update(AbstractConsumable abstractConsumable, float dt) {
        if (abstractConsumable.isTaken()) {
            //*Archimedes spiral*//
            radius = radius - dt * abstractConsumable.getSpiralRadius();
            abstractConsumable.getPosition().x = Blackhole.position.x + radius * MathUtils.cos(angle);
            abstractConsumable.getPosition().y = Blackhole.position.y + radius * MathUtils.sin(angle);
            angle += dt;
            intoTheDark(abstractConsumable);
        } else {
            archimedesInit(abstractConsumable);
        }
        update(abstractConsumable);
    }

    private void intoTheDark(AbstractPoolable abstractPoolable) {
        float percentage = 1.0f - abstractPoolable.getPosition().dst(Blackhole.position) / length;
        float currentR = MathUtils.lerp(1.0f, 0.5f, 2 * percentage);
        float currentG = MathUtils.lerp(1.0f, 0.5f, 2 * percentage);
        float currentA = MathUtils.lerp(1.0f, 0.5f, 2 * percentage);
        abstractPoolable.getColor().r = currentR;
        abstractPoolable.getColor().g = currentG;
        abstractPoolable.getColor().a = currentA;
    }

    public void archimedesInit(AbstractPoolable abstractPoolable) {
        tmp1V2.set(abstractPoolable.getPosition()).sub(Blackhole.position);
        angle = tmp1V2.angle() * MathUtils.degreesToRadians;
        radius = tmp1V2.len();
        length = radius;
    }

    private void update(AbstractPoolable abstractPoolable) {
        if (abstractPoolable.isActive()) {
            abstractPoolable.collideBlackhole();
        }
    }

    public void update(AbstractAnomaly abstractAnomaly, float dt) {
        abstractAnomaly.getVelocity().scl(/*FPS hack*/1.0f - dt/*FPS hack*/);
        if (!abstractAnomaly.isTaken()) {
            //*Standard move*//
            if (abstractAnomaly.isHungry() && abstractAnomaly.isActivated()) {
                abstractAnomaly.getPosition().mulAdd(abstractAnomaly.getVelocity(), dt);
                float dst = Buran.position.dst(abstractAnomaly.getPosition());
                if (abstractAnomaly.getLevel() + 1 != (int) Standing.getInstance().getRamPower()) {
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
                            .scl(abstractAnomaly.getV() / Rules.ANOMALIES_CORROSIVE_AFRAID_FACTOR);
                    abstractAnomaly.getVelocity().mulAdd(tmp1V2, dt);
                }
            }
        } else {
            //*Archimedes spiral*//
            radius = radius - dt * abstractAnomaly.getSpiralRadius();
            abstractAnomaly.getPosition().x = Blackhole.position.x + radius * MathUtils.cos(angle);
            abstractAnomaly.getPosition().y = Blackhole.position.y + radius * MathUtils.sin(angle);
            angle += dt;
        }
        update(abstractAnomaly);
    }

    public void update(AbstractAsteroid asteroid, float dt) {
        if (!asteroid.isTaken()) {
            //*Standard move*//
            asteroid.getPosition().mulAdd(asteroid.getVelocity(), dt);
            archimedesInit(asteroid);
        } else {
            //*Archimedes spiral*//
            radius = radius - dt * asteroid.getSpiralRadius();
            asteroid.getPosition().x = Blackhole.position.x + radius * MathUtils.cos(angle);
            asteroid.getPosition().y = Blackhole.position.y + radius * MathUtils.sin(angle);
            angle += dt;

            intoTheDark(asteroid);
        }
        update(asteroid);
    }
}