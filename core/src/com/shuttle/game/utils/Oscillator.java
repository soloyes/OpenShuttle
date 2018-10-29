package com.shuttle.game.utils;

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
}