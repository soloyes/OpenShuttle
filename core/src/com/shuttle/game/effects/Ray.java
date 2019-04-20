package com.shuttle.game.effects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.asteroids.AbstractAsteroid;
import com.shuttle.game.conf.GlobalInteractionLevels;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.effects.particle.ParticleEmitter;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.player.Standing;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.ListOfListsJoiner;
import com.shuttle.game.utils.pool.AbstractPoolable;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 10/29/18.
 */
public class Ray {
    private Vector2 tmp1V2;
    @Setter
    private int count;
    @Setter
    private Preset preset;
    @Setter
    private float size;
    @Setter
    private float harvestVelocity;
    @Setter
    private float takeRange;
    private BoomBox boomBox;
    private GameScreen gs;
    private ListOfListsJoiner<AbstractAsteroid> asteroidsJoiner;

    public Ray(GameScreen gs) {
        this.gs = gs;
        this.tmp1V2 = new Vector2();
        this.count = 24;
        this.preset = Preset.GREEN;
        this.size = 4;
        this.harvestVelocity = Rules.PLAYER_RAY_CONSUMABLE_HARVEST_VELOCITY;
        this.takeRange = Rules.PLAYER_CONSUMABLE_TAKE_RANGE;
        this.boomBox = new BoomBox();
        this.asteroidsJoiner = new ListOfListsJoiner<AbstractAsteroid>(
                gs.getMetalAsteroidEmitter().getActiveList(),
                gs.getMineralAsteroidEmitter().getActiveList()
        );
    }

    public void harvest(Shuttle player, AbstractPoolable poolable, ParticleEmitter particleEmitter, float dt) {
        if (!player.isTaken()) {
            if (Shuttle.position.dst(poolable.getPosition()) <= takeRange) {
                poolable.setAtRayPoint(true);
                tmp1V2.set(
                        Shuttle.position)
                        .sub(poolable.getPosition())
                        .nor()
                        .scl(harvestVelocity);
                poolable.getPosition().mulAdd(tmp1V2, dt);
                float dst = Shuttle.position.dst(poolable.getPosition());
                float scaleX = (dst / takeRange) * poolable.getSaveScaleX();
                float scaleY = (dst / takeRange) * poolable.getSaveScaleY();
                if (scaleX >= poolable.getSaveScaleX()) {
                    scaleX = poolable.getSaveScaleX();
                }
                if (scaleY >= poolable.getSaveScaleY()) {
                    scaleY = poolable.getSaveScaleY();
                }

                poolable.setScaleX(scaleX);
                poolable.setScaleY(scaleY);

                particleEmitter.setupFromPointToPoint(
                        poolable.getPosition().x, poolable.getPosition().y,
                        Shuttle.position.x, Shuttle.position.y,
                        1, 2,
                        preset.startR, preset.startG, preset.startB, 0.3f,
                        preset.endA, preset.endG, preset.endB, 0.3f
                );
                for (int j = 0; j < count; j++) {
                    float ang = 6.28f / count * j;
                    float radX = poolable.getWidth() / 2 * scaleX;
                    float radY = poolable.getHeight() / 2 * scaleY;
                    particleEmitter.setup(
                            poolable.getPosition().x + radX * (float) Math.cos(ang) + MathUtils.random(-10, 10),
                            poolable.getPosition().y + radY * (float) Math.sin(ang) + MathUtils.random(-10, 10),
                            0, 0, 0.1f, size, size,
                            preset.startR, preset.startG, preset.startB, 0.2f,
                            preset.endA, preset.endG, preset.endB, 0.0f
                    );
                }

                poolable.setScaleX(scaleX);
                poolable.setScaleY(scaleY);
            } else {
                poolable.setAtRayPoint(false);
            }
        } else {

            poolable.setScaleX(poolable.getScaleX() + dt);
            poolable.setScaleY(poolable.getScaleY() + dt);
            if (poolable.getScaleX() >= poolable.getSaveScaleX()) {
                poolable.setScaleX(poolable.getSaveScaleX());
            }
            if (poolable.getScaleY() >= poolable.getSaveScaleY()) {
                poolable.setScaleY(poolable.getSaveScaleY());
            }
        }
    }

    public void laser(Shuttle player, AbstractAsteroid asteroid, ParticleEmitter particleEmitter, float dt) {
        float dst = Shuttle.position.dst(asteroid.getPosition());
        if (dst <= Rules.PLAYER_LASER_TAKE_RANGE && !player.isTaken() && !asteroid.isTaken() && asteroid.isActive()) {
            asteroid.setAtRayPoint(true);
            boomBox.playMusicEffectInLoop(MSConstants.LASER, 1.0f);
            float scaleX = (dst / Rules.PLAYER_LASER_TAKE_RANGE) * asteroid.getSaveScaleX();
            float scaleY = (dst / Rules.PLAYER_LASER_TAKE_RANGE) * asteroid.getSaveScaleY();
            particleEmitter.setupFromPointToPoint(
                    asteroid.getPosition().x, asteroid.getPosition().y,
                    Shuttle.position.x, Shuttle.position.y,
                    1.0f, 1.0f,
                    preset.startR, preset.startG, preset.startB, 0.3f,
                    preset.endA, preset.endG, preset.endB, 0.3f
            );
            for (int j = 0; j < count; j++) {
                float ang = 6.28f / count * j;
                float radX = asteroid.getWidth() / 4 * scaleX;
                float radY = asteroid.getHeight() / 4 * scaleY;
                particleEmitter.setup(
                        asteroid.getPosition().x + radX * (float) Math.cos(ang) + MathUtils.random(-10, 10),
                        asteroid.getPosition().y + radY * (float) Math.sin(ang) + MathUtils.random(-10, 10),
                        0, 0, 0.1f, size, size,
                        preset.startR, preset.startG, preset.startB, 0.2f,
                        preset.endA, preset.endG, preset.endB, 0.0f
                );
            }
            asteroid.setTimeToBurn(asteroid.getTimeToBurn() + dt);
            if (asteroid.getTimeToBurn() >= 0.5f + Rules.ASTEROID_BURN_TIME / Standing.getInstance().getLaserChargeLevel()) {
                boomBox.playSound(MSConstants.DESTROY_ASTEROID_RAY);
                switch (asteroid.getType()) {
                    case ASTEROID_METAL:
                        gs.getMetalAsteroidEmitter().boom(asteroid.getPosition(), GlobalInteractionLevels.NONE);
                        break;
                    case ASTEROID_MINERAL:
                        gs.getMineralAsteroidEmitter().boom(asteroid.getPosition(), GlobalInteractionLevels.NONE);
                        break;
                }
                asteroid.destroyWithEffect();
                asteroid.showSprayCollision(particleEmitter);
                Standing.getInstance().addToStatistics(asteroid.getType(), asteroid.getLevel());
                asteroid.setTimeToBurn(0.0f);
                asteroid.setAtRayPoint(false);
                Standing.getInstance().changeLaserChargeLevel(-1);
                if (Standing.getInstance().getLaserChargeLevel() <= 1) {
                    boomBox.stopMusicEffect(MSConstants.LASER);
                    player.setLaser(false);
                    Standing.getInstance().setLaserChargeLevel(0);
                }
            }
        } else {
            asteroid.setAtRayPoint(false);
            asteroid.setTimeToBurn(0.0f);
        }

        boolean anyAtLaserPoint = false;
        for (int i = 0; i < asteroidsJoiner.size(); i++) {
            if (!asteroidsJoiner.get(i).isFragmented()) {
                anyAtLaserPoint |= asteroidsJoiner.get(i).isAtRayPoint();
            }
        }
        if (!anyAtLaserPoint) {
            boomBox.stopMusicEffect(MSConstants.LASER);
        }
    }

    public enum Preset {
        GREEN(0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f),
        GREY(0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f),
        RED(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f);
        @Getter
        private float startR, startG, startB;
        @Getter
        private float endA, endG, endB;

        Preset(float startR, float startG, float startB, float endA, float endG, float endB) {
            this.startR = startR;
            this.startG = startG;
            this.startB = startB;
            this.endA = endA;
            this.endG = endG;
            this.endB = endB;
        }
    }
}
