package ru.geekbrains.stargame.engine.math;

import java.security.SecureRandom;
import java.util.Random;

public class Rnd {
    private static final Random random = new SecureRandom();

    public static float nextFloat(float min, float max) {
        return random.nextFloat() * (max - min) + min;
    }

    public static int nextInt(int val) {
        return random.nextInt(val);
    }
}