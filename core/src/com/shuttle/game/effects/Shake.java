package com.shuttle.game.effects;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * @author Shuttle on 8/29/18.
 */

public class Shake {
    private static Shake instance;
    private float[] samples, vectorSamples;
    private float internalTimer = 0, vectorInternalTimer = 0;
    private float shakeDuration;
    private float vectorShakeDuration;
    private int duration = 10; // In seconds, make longer if you want more variation
    private int frequency, vectorFrequency; // hertz
    private float amplitude, vectorAmplitude; // how much you want to shakeCamera
    private boolean falloff = true; // if the shakeCamera should decay as it expires
    private int sampleCount, vectorSampleCount;
    private Array<Vector2> array;

    public static Shake getInstance() {
        if (instance == null) {
            instance = new Shake();
        }
        return instance;
    }

    private Shake() {
        this.array = new Array<Vector2>();
    }

    /**
     * Will make the camera shakeCamera for the duration passed in in seconds
     *
     * @param d duration of the shakeCamera in seconds
     */
    public void shakeCamera(float d, int frequency, int amplitude) {
        this.shakeDuration = d;
        this.amplitude = amplitude;
        this.frequency = frequency;

        sampleCount = duration * frequency;
        samples = new float[sampleCount];
        for (int i = 0; i < sampleCount; i++) {
            samples[i] = MathUtils.random(-1.0f, 1.0f);
        }
    }

    public void shakeVector(float d, int frequency, int amplitude) {
        this.vectorShakeDuration = d;
        this.vectorAmplitude = amplitude;
        this.vectorFrequency = frequency;

        vectorSampleCount = duration * frequency;
        vectorSamples = new float[vectorSampleCount];
        for (int i = 0; i < vectorSampleCount; i++) {
            vectorSamples[i] = MathUtils.random(-1.0f, 1.0f);
        }
    }

    /**
     * Called every frame will shakeCamera the camera if it has a shakeCamera duration
     *
     * @param dt     Gdx.graphics.getDeltaTime() or your dt in seconds
     * @param camera your camera
     */
    public void update(float dt, Camera camera) {
        update(dt);
        internalTimer += dt;
        if (internalTimer > duration) {
            internalTimer -= duration;
        }
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
     * Called every frame will shakeCamera the Vector if it has a shakeCamera duration
     *
     * @param dt Gdx.graphics.getDeltaTime() or your dt in seconds
     */
    private void update(float dt) {
        if (vectorShakeDuration <= 0.0f) {
            shakeVector(2, 30, 20);
        }
        vectorInternalTimer += dt;
        if (vectorInternalTimer > duration) {
            vectorInternalTimer -= duration;
        }
        if (vectorShakeDuration > 0) {
            vectorShakeDuration -= dt;
            float shakeTime = (vectorInternalTimer * vectorFrequency);
            int first = (int) shakeTime;
            int second = (first + 1) % vectorSampleCount;
            int third = (first + 2) % vectorSampleCount;
            float deltaT = shakeTime - (int) shakeTime;
            float deltaX = vectorSamples[first] * deltaT + vectorSamples[second] * (1f - deltaT);
            float deltaY = vectorSamples[second] * deltaT + vectorSamples[third] * (1f - deltaT);

            for (int i = 0; i < array.size; i++) {
                array.get(i).x = array.get(i).x + deltaX * vectorAmplitude * (falloff ? Math.min(vectorShakeDuration, 1f) : 1f);
                array.get(i).y = array.get(i).y + deltaY * vectorAmplitude * (falloff ? Math.min(vectorShakeDuration, 1f) : 1f);
            }
        }
    }

    public void subscribe(Vector2 position) {
        array.add(position);
    }
}