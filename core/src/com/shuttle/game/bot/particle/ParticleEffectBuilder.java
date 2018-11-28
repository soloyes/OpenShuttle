package com.shuttle.game.bot.particle;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class ParticleEffectBuilder {
    private ParticleEmitter particleEmitter;

    public ParticleEffectBuilder(ParticleEmitter particleEmitter) {
        this.particleEmitter = particleEmitter;
    }

    public void buildDust(float x, float y, float dxy, int count) {
        for (int j = 0; j < count; j++) {
            particleEmitter.setup(x, y, MathUtils.random(-dxy, dxy), MathUtils.random(-dxy, dxy), 1f, 6.0f, 1.0f, 0.17f, 0.17f, 0.17f, 1, 0, 0, 0, 0);
        }
    }

    public void buildShuttleEngineFire(Vector2 position, float angle, float radius, float level, float factor) {
        particleEmitter.setup(position.x - radius * (float) Math.cos(Math.toRadians(angle)), position.y - radius * (float) Math.sin(Math.toRadians(angle)), MathUtils.random(-90, 90), MathUtils.random(-90, 90), 0.3f + level * 0.1f, 4.0f + level * 1.5f, 0.4f, 1, 0.6f - level * 0.15f, 0, 1, 1, 0, 0, 0.5f);
        if (MathUtils.random(0, 100) < /*FPS hack*/20 / factor/*FPS hack*/) {
            for (int i = 0; i < level + 1; i++) {
                particleEmitter.setup(position.x - radius * (float) Math.cos(Math.toRadians(angle)), position.y - radius * (float) Math.sin(Math.toRadians(angle)), MathUtils.random(-180, 180), MathUtils.random(-180, 180), 0.3f, 8.0f + level * 2, 0.0f, 1, 0.4f, 0, 1, 0, 0, 0, 0.2f);
            }
        }

    }

    public void buildShuttleFrontalTurboFire(Vector2 position, float angle, float radius, float randomPositionNoise) {
        particleEmitter.setup(position.x + radius * (float) Math.cos(Math.toRadians(angle)), position.y + radius * (float) Math.sin(Math.toRadians(angle)), MathUtils.random(-randomPositionNoise, randomPositionNoise), MathUtils.random(-randomPositionNoise, randomPositionNoise), 5.9f, 16.0f, 15.0f, 1, 0.6f, 0, 1, 0, 0, 0, 0.0f);
    }

    public void buildShuttleLowLevelEngineFire(Vector2 position, float angle, float radius) {
        particleEmitter.setup(position.x - radius * (float) Math.cos(Math.toRadians(angle)), position.y - radius * (float) Math.sin(Math.toRadians(angle)), MathUtils.random(-90, 90), MathUtils.random(-90, 90), 0.2f, 3.0f, 2.4f, 0.2f, 0.2f, 1.0f, 0.4f, 0, 0, 0, 0.2f);
    }

    public void buildShuttleShield(Vector2 position, float angle, float radius, int halfConeAngle, float r, float g, float b) {
        for (int i = -halfConeAngle; i <= halfConeAngle; i += 10) {
            particleEmitter.setup(position.x + radius * (float) Math.cos(Math.toRadians(angle + i)), position.y + radius * (float) Math.sin(Math.toRadians(angle + i)), MathUtils.random(-5, 5), MathUtils.random(-5, 5), 0.15f, 3.0f, 4.0f, r, g, b, 0.1f, 0, 0, 0, 0.0f);
        }
    }

    public void buildFuelDestroyFlame(Vector2 position){
        for (int j = 0; j < 20; j++) {
            particleEmitter.setup(
                    position.x, position.y,
                    MathUtils.random(-70, 70), MathUtils.random(-100, 100), 3f, 10, 0,
                    0.4f, 0.4f, 0.4f, 0.2f,
                    0.4f, 0.4f, 0.4f, 0.0f
            );
            particleEmitter.setup(
                    position.x, position.y,
                    MathUtils.random(-60, 60), MathUtils.random(-100, 100), 2f, 10, 0,
                    1.0f, 0.4f, 0.0f, 0.7f,
                    1.0f, 0.0f, 0.0f, 0.0f
            );
        }
    }
}
