package com.buran.game.effects;

/**
 * @author soloyes on 29/10/18.
 */

public class Oscillator {
    private boolean oscillator;

    public float oscillate(float value, float frequency, float from, float to, float dt) {
        if (oscillator) {
            value -= dt * frequency;
            if (value <= from) {
                value = from;
                oscillator = false;
            }
        } else {
            value += dt * frequency;
            if (value >= to) {
                value = to;
                oscillator = true;
            }
        }
        return value;
    }

    public float prettyOscillate(float value, float frequency, float acceleration, float from, float to, float dt) {
        if (value > (to - from) / 2) {
            value += dt * frequency + acceleration * (-1) * dt * dt / 2;
        } else
            value += dt * frequency + acceleration * dt * dt / 2;
        return value;
    }
}