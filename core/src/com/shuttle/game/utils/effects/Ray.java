package com.shuttle.game.utils.effects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.asteroids.Asteroid;
import com.shuttle.game.bot.particle.ParticleEmitter;
import com.shuttle.game.bot.pool.AbstractPoolable;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.screens.GameScreen;

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
    private BoomBox boomBox;
    private GameScreen gs;

    public Ray(GameScreen gs) {
        this.gs = gs;
        this.tmp1V2 = new Vector2();
        this.count = 24;
        this.preset = Preset.GREEN;
        this.size = 4;
        this.boomBox = new BoomBox();
    }

    public void harvest(Shuttle player, AbstractPoolable abstractPoolable, ParticleEmitter particleEmitter, float dt) {
        if (!player.isTaken()) {
            float takeRange = preset.name().equals(Preset.GREEN.name()) ?
                    Rules.PLAYER_CONSUMABLE_TAKE_RANGE : Rules.PLAYER_FRAGMENT_TAKE_RANGE;
            float harvestVelocity = preset.name().equals(Preset.GREEN.name()) ?
                    Rules.PLAYER_RAY_CONSUMABLE_HARVEST_VELOCITY : Rules.PLAYER_RAY_FRAGMENT_HARVEST_VELOCITY;

            if (player.getPosition().dst(abstractPoolable.getPosition()) <= takeRange) {
                tmp1V2.set(
                        player.getPosition())
                        .sub(abstractPoolable.getPosition())
                        .nor()
                        .scl(harvestVelocity);
                abstractPoolable.getPosition().mulAdd(tmp1V2, dt);
                float dst = player.getPosition().dst(abstractPoolable.getPosition());
                float scaleX = (dst / takeRange) * abstractPoolable.getSaveScaleX();
                float scaleY = (dst / takeRange) * abstractPoolable.getSaveScaleY();
                if (scaleX >= abstractPoolable.getSaveScaleX()) {
                    scaleX = abstractPoolable.getSaveScaleX();
                }
                if (scaleY >= abstractPoolable.getSaveScaleY()) {
                    scaleY = abstractPoolable.getSaveScaleY();
                }

                abstractPoolable.setScaleX(scaleX);
                abstractPoolable.setScaleY(scaleY);

                particleEmitter.setupFromPointToPoint(
                        abstractPoolable.getPosition().x, abstractPoolable.getPosition().y,
                        player.getPosition().x, player.getPosition().y,
                        1, 2,
                        preset.startR, preset.startG, preset.startB, 0.3f,
                        preset.endA, preset.endG, preset.endB, 0.3f
                );
                for (int j = 0; j < count; j++) {
                    float ang = 6.28f / count * j;
                    float radX = abstractPoolable.getWidth() / 2 * scaleX;
                    float radY = abstractPoolable.getHeight() / 2 * scaleY;
                    particleEmitter.setup(
                            abstractPoolable.getPosition().x + radX * (float) Math.cos(ang) + MathUtils.random(-10, 10),
                            abstractPoolable.getPosition().y + radY * (float) Math.sin(ang) + MathUtils.random(-10, 10),
                            0, 0, 0.1f, size, size,
                            preset.startR, preset.startG, preset.startB, 0.2f,
                            preset.endA, preset.endG, preset.endB, 0.0f
                    );
                }

                abstractPoolable.setScaleX(scaleX);
                abstractPoolable.setScaleY(scaleY);
            }
        } else {
            abstractPoolable.setScaleX(abstractPoolable.getScaleX() + dt);
            abstractPoolable.setScaleY(abstractPoolable.getScaleY() + dt);
            if (abstractPoolable.getScaleX() >= abstractPoolable.getSaveScaleX()) {
                abstractPoolable.setScaleX(abstractPoolable.getSaveScaleX());
            }
            if (abstractPoolable.getScaleY() >= abstractPoolable.getSaveScaleY()) {
                abstractPoolable.setScaleY(abstractPoolable.getSaveScaleY());
            }
        }
    }

    public void laser(Shuttle player, Asteroid asteroid, ParticleEmitter particleEmitter, float dt) {
        if (player.getPosition().dst(asteroid.getPosition()) <= Rules.PLAYER_LASER_TAKE_RANGE) {
            float dst = player.getPosition().dst(asteroid.getPosition());
            float scaleX = (dst / Rules.PLAYER_LASER_TAKE_RANGE) * asteroid.getSaveScaleX();
            float scaleY = (dst / Rules.PLAYER_LASER_TAKE_RANGE) * asteroid.getSaveScaleY();

            particleEmitter.setupFromPointToPoint(
                    asteroid.getPosition().x, asteroid.getPosition().y,
                    player.getPosition().x, player.getPosition().y,
                    1, 2,
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
            if (asteroid.getTimeToBurn() >= 0.5f + Rules.ASTEROID_BURN_TIME * player.getShieldLevel().getLevel()) {
                boomBox.playSound(MSConstants.ASTEROID_DESTROY);
                gs.getAsteroidEmitter().boom(asteroid.getPosition(), player.getShieldLevel());
                asteroid.destroy();
                asteroid.setTimeToBurn(0.0f);
            }
        } else asteroid.setTimeToBurn(0.0f);
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
