package com.shuttle.game.effects;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import lombok.Getter;

/**
 * @author Shuttle on 8/29/18.
 */
public class Shake {
    private float[] samples;
    private float internalTimer = 0;
    @Getter
    private float shakeDuration;
    private int duration = 10; // In seconds, make longer if you want more variation
    private int frequency; // hertz
    private float amplitude; // how much you want to shake
    private boolean falloff = true; // if the shake should decay as it expires
    private int sampleCount;

    /**
     * Will make the camera shake for the duration passed in in seconds
     *
     * @param d duration of the shake in seconds
     */
    public void shake(float d, int frequency, int amplitude) {
        this.shakeDuration = d;
        this.amplitude = amplitude;
        this.frequency = frequency;

        sampleCount = duration * frequency;
        samples = new float[sampleCount];
        for (int i = 0; i < sampleCount; i++) {
            samples[i] = MathUtils.random(-1.0f, 1.0f);
        }
    }

    /**
     * Called every frame will shake the camera if it has a shake duration
     *
     * @param dt     Gdx.graphics.getDeltaTime() or your dt in seconds
     * @param camera your camera
     */
    public void update(float dt, Camera camera) {
        internalTimer += dt;
        if (internalTimer > duration) internalTimer -= duration;
        if (shakeDuration > 0) {
            shakeDuration -= dt;
            float shakeTime = (internalTimer * frequency);
            int first = (int) shakeTime;
            int second = (first + 1) % sampleCount;
            int third = (first + 2) % sampleCount;
            float deltaT = shakeTime - (int) shakeTime;
            float deltaX = samples[first] * deltaT + samples[second] * (1f - deltaT);
            float deltaY = samples[second] * deltaT + samples[third] * (1f - deltaT);

            camera.position.x = camera.position.x + deltaX * amplitude * (falloff ? Math.min(shakeDuration, 1f) : 1f);
            camera.position.y = camera.position.y + deltaY * amplitude * (falloff ? Math.min(shakeDuration, 1f) : 1f);
            camera.update();
        }
    }

    /**
     * Called every frame will shake the Vector if it has a shake duration
     *
     * @param dt      Gdx.graphics.getDeltaTime() or your dt in seconds
     * @param vector2 your vector to shake
     */
    public void update(float dt, Vector2 vector2) {
        internalTimer += dt;
        if (internalTimer > duration) internalTimer -= duration;
        if (shakeDuration > 0) {
            shakeDuration -= dt;
            float shakeTime = (internalTimer * frequency);
            int first = (int) shakeTime;
            int second = (first + 1) % sampleCount;
            int third = (first + 2) % sampleCount;
            float deltaT = shakeTime - (int) shakeTime;
            float deltaX = samples[first] * deltaT + samples[second] * (1f - deltaT);
            float deltaY = samples[second] * deltaT + samples[third] * (1f - deltaT);

            vector2.x = vector2.x + deltaX * amplitude * (falloff ? Math.min(shakeDuration, 1f) : 1f);
            vector2.y = vector2.y + deltaY * amplitude * (falloff ? Math.min(shakeDuration, 1f) : 1f);
        }
    }
}
