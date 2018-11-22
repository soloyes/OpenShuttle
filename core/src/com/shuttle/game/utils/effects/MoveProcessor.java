package com.shuttle.game.utils.effects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.anomaly.AbstractAnomaly;
import com.shuttle.game.bot.asteroids.Asteroid;
import com.shuttle.game.bot.consumable.AbstractConsumable;
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
    private float length;

    public MoveProcessor(GameScreen gs) {
        this.gs = gs;
        this.tmp = new Vector2();
    }

    public void update(AbstractConsumable abstractConsumable, float dt) {
        if (abstractConsumable.isTaken()) {
            //*Archimedes spiral*//
            radius = radius - dt * abstractConsumable.getSpiralRadius();
            abstractConsumable.getPosition().x = gs.getBlackhole().getPosition().x + radius * MathUtils.cos(angle);
            abstractConsumable.getPosition().y = gs.getBlackhole().getPosition().y + radius * MathUtils.sin(angle);
            angle += dt;
            intoTheDark(abstractConsumable);
        } else {
            archimedesInit(abstractConsumable);
        }
        update(abstractConsumable);
    }

    private void intoTheDark(AbstractPoolable abstractPoolable) {
        float percentage = 1.0f - abstractPoolable.getPosition().dst(gs.getBlackhole().getPosition()) / length;
        float currentR = MathUtils.lerp(1.0f, 0.5f, 2 * percentage);
        float currentG = MathUtils.lerp(1.0f, 0.5f, 2 * percentage);
        float currentA = MathUtils.lerp(1.0f, 0.5f, 2 * percentage);
        abstractPoolable.getColor().r = currentR;
        abstractPoolable.getColor().g = currentG;
        abstractPoolable.getColor().a = currentA;
    }

    public void archimedesInit(AbstractPoolable abstractPoolable) {
        tmp.set(abstractPoolable.getPosition()).sub(gs.getBlackhole().getPosition());
        angle = tmp.angle() * MathUtils.degreesToRadians;
        radius = tmp.len();
        length = radius;
    }

    public void update(AbstractAnomaly abstractAnomaly, float dt) {
        abstractAnomaly.getVelocity().scl(/*FPS hack*/1.0f - dt/*FPS hack*/);
        if (!abstractAnomaly.isTaken()) {
            //*Standard move*//
            if (abstractAnomaly.isHungry() && abstractAnomaly.isActivated()) {
                abstractAnomaly.getPosition().mulAdd(abstractAnomaly.getVelocity(), dt);
                float dst = gs.getPlayer().getPosition().dst(abstractAnomaly.getPosition());
                if (abstractAnomaly.getLevel() + 1 != gs.getPlayer().getShieldLevel().getLevel()) {
                    if (dst < 2400.0f + 200 * abstractAnomaly.getLevel()) {
                        tmp.set(gs.getPlayer()
                                .getPosition())
                                .sub(abstractAnomaly.getPosition())
                                .nor()
                                .scl(abstractAnomaly.getV());
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
                    //Anomaly afraid related shield level
                } else {
                    tmp.set(gs.getPlayer()
                            .getPosition())
                            .sub(abstractAnomaly.getPosition())
                            .nor()
                            .scl(abstractAnomaly.getV() / Rules.ANOMALIES_CORROSIVE_AFRAID_FACTOR);
                    abstractAnomaly.getVelocity().mulAdd(tmp, dt);
                }
            }
        } else {
            //*Archimedes spiral*//
            radius = radius - dt * abstractAnomaly.getSpiralRadius();
            abstractAnomaly.getPosition().x = gs.getBlackhole().getPosition().x + radius * MathUtils.cos(angle);
            abstractAnomaly.getPosition().y = gs.getBlackhole().getPosition().y + radius * MathUtils.sin(angle);
            angle += dt;
        }
        update(abstractAnomaly);
    }

    public void update(Asteroid asteroid, float dt) {
        if (!asteroid.isTaken()) {
            //*Standard move*//
            asteroid.getPosition().mulAdd(asteroid.getVelocity(), dt);
            archimedesInit(asteroid);
        } else {
            //*Archimedes spiral*//
            radius = radius - dt * asteroid.getSpiralRadius();
            asteroid.getPosition().x = gs.getBlackhole().getPosition().x + radius * MathUtils.cos(angle);
            asteroid.getPosition().y = gs.getBlackhole().getPosition().y + radius * MathUtils.sin(angle);
            angle += dt;

            intoTheDark(asteroid);
        }
        update(asteroid);
    }

    private void update(AbstractPoolable abstractPoolable){
        if (abstractPoolable.isActive()){
            abstractPoolable.collideBlackhole();
        }
    }
}