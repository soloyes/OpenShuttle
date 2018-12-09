package com.shuttle.game.utils.effects;

import java.util.LinkedList;

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

    public float prettyOscillate(float value, float frequency, float from, float to, float dt) {
        LinkedList<Integer> fib = Fibonacci.generate(15);
        for (int i = fib.size() - 2; i >= 0; i--) {
            fib.add(fib.get(i));

        }
        float step = (to - from) / fib.size();

        if (oscillator) {
            value -= dt * frequency * fib.get((int) (value / step)) / 10;
            if (value <= from) {
                value = from;
                oscillator = false;
            }
        } else {
            value += dt * frequency * fib.get((int) (value / step)) / 10;
            if (value >= to) {
                value = to;
                oscillator = true;
            }
        }
        return value;
    }
}