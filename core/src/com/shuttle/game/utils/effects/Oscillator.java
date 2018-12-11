package com.shuttle.game.utils.effects;

import lombok.Getter;

public class Oscillator {
    @Getter
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

    public float prettyOscillate(float value, float frequency, float asc, float from, float to, float dt) {
        if (oscillator) {
            if (value < (to - from) / 2) {
                value -= (dt * frequency + asc * dt * dt/2);
                if (value <= from || dt * frequency + asc * dt * dt/2 < 0) {
                    value = from;
                    oscillator = false;
                }
            }
            else {
                value -= dt * frequency + asc * (-1) * dt * dt/2;
            }
        } else {
            if ( value > (to - from) / 2) {
                value += dt * frequency + asc * (-1) * dt * dt/2;
                if (value >= to || dt * frequency + asc * (-1) * dt * dt/2 < 0 ) {
                    value = to;
                    oscillator = true;
                }
            }
            else {
                value += dt * frequency + asc * dt * dt / 2;
            }
        }
        return value;
    }
}