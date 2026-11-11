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
            radius = radius - dt * abstractConsumable.getSpiralRadius() * 4f;
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
            com.buran.game.world.Shelters.repel(abstractAnomaly.getPosition(), abstractAnomaly.getVelocity(), dt);
            // Inside a shelter the ship is out of reach: anomalies drift instead of hunting.
            if (abstractAnomaly.isHungry() && abstractAnomaly.isActivated() && com.buran.game.world.Shelters.shipProtected()) {
                abstractAnomaly.getPosition().mulAdd(abstractAnomaly.getVelocity(), dt);
            } else if (abstractAnomaly.isHungry() && abstractAnomaly.isActivated()) {
                abstractAnomaly.getPosition().mulAdd(abstractAnomaly.getVelocity(), dt);
                float dst = Buran.position.dst(abstractAnomaly.getPosition());
                if (abstractAnomaly instanceof com.buran.game.bot.anomaly.CorrosiveAnomaly && temper(abstractAnomaly, dst, dt)) {
                    // its temperament moved it this frame
                } else if (abstractAnomaly.getLevel() + 1 != com.buran.game.player.Buran.ramLevel()) {
                    if (dst < 2400.0f + 200 * abstractAnomaly.getLevel()) {
                        target(abstractAnomaly);
                        tmp1V2.set(aim)
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
            radius = radius - dt * abstractAnomaly.getSpiralRadius() * 4f;
            abstractAnomaly.getPosition().x = Blackhole.position.x + radius * MathUtils.cos(angle);
            abstractAnomaly.getPosition().y = Blackhole.position.y + radius * MathUtils.sin(angle);
            angle += dt;
        }
        update(abstractAnomaly);
    }

    private static final int HUNTER = 0;
    private static final int FLANKER = 1;
    private static final int LURKER = 2;
    private final Vector2 aim = new Vector2();

    /** Where this one steers: the ship, or for a flanker, where the ship will be in a second. */
    private void target(AbstractAnomaly a) {
        aim.set(Buran.position);
        if (a.temper == FLANKER) {
            aim.mulAdd(Buran.velocity, 0.9f);
        }
    }

    /**
     * Corrosive temperaments (tip 71): half hunt straight, a quarter cut you off, a quarter lie
     * in wait - still, trembling when you come close (the tell, tip 53), then one hard lunge.
     * Returns true when it handled the movement itself.
     */
    private boolean temper(AbstractAnomaly a, float dst, float dt) {
        if (a.temper < 0) {
            float r = MathUtils.random();
            a.temper = r < 0.5f ? HUNTER : r < 0.75f ? FLANKER : LURKER;
        }
        if (a.temper != LURKER || a.lunged) {
            return false;
        }
        a.getVelocity().scl(Math.max(0f, 1f - 2f * dt));
        if (dst < 950f) {
            a.getPosition().add(MathUtils.random(-5f, 5f), MathUtils.random(-5f, 5f));
        }
        if (dst < 620f) {
            a.lunged = true;
            tmp1V2.set(Buran.position).sub(a.getPosition()).nor().scl(950f);
            a.getVelocity().set(tmp1V2);
        }
        return true;
    }

    public void update(AbstractAsteroid asteroid, float dt) {
        if (!asteroid.isTaken()) {
            //*Standard move*//
            asteroid.getPosition().mulAdd(asteroid.getVelocity(), dt);
            archimedesInit(asteroid);
        } else {
            //*Archimedes spiral*//
            radius = radius - dt * asteroid.getSpiralRadius() * 4f;
            asteroid.getPosition().x = Blackhole.position.x + radius * MathUtils.cos(angle);
            asteroid.getPosition().y = Blackhole.position.y + radius * MathUtils.sin(angle);
            angle += dt;

            intoTheDark(asteroid);
        }
        update(asteroid);
    }
}