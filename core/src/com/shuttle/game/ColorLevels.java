package com.shuttle.game;

public enum ColorLevels {
    NONE(0, 0, 0, 0, 0),
    WHITE(1, 1, 1, 1, 1f),
    GREEN(2, 0, 1, 0, 1f),
    YELLOW(3, 1, 1, 0, 1f),
    RED(4, 1, 0, 0, 1f);

    private int level;
    private float r, g, b, a;

    public int getLevel() {
        return level;
    }

    public float getR() {
        return r;
    }

    public float getG() {
        return g;
    }

    public float getB() {
        return b;
    }

    public float getA() {
        return a;
    }

    ColorLevels(int level, float r, float g, float b, float a) {
        this.level = level;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public static ColorLevels getFromLevel(int level) {
        if (level > 4) {
            level = 4;
        }
        return values()[level];
    }
}
