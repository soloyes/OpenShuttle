package com.shuttle.game.conf;

public enum GlobalInteractionLevels {
    NONE(0, 0, 0, 0, 0, 1.0f, 0.5f, 0.6f),
    WHITE(1, 1, 1, 1, 1f, 1.5f, 0.35f, 0.5f),
    GREEN(2, 0, 1, 0, 1f, 2.0f, 0.3f, 0.45f),
    YELLOW(3, 1, 1, 0, 1f, 2.5f, 0.25f, 0.4f),
    RED(4, 1, 0, 0, 1f, 3.0f, 0.15f, 0.35f);
    private int level;
    private float r, g, b, a;
    private float fragmentAsteroidCount;
    private float fragmentAsteroidSize1, fragmentAsteroidSize2;

    GlobalInteractionLevels(int level, float r, float g, float b, float a, float fragmentAsteroidCount
            , float fragmentAsteroidSize1, float fragmentAsteroidSize2) {
        this.level = level;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.fragmentAsteroidCount = fragmentAsteroidCount;
        this.fragmentAsteroidSize1 = fragmentAsteroidSize1;
        this.fragmentAsteroidSize2 = fragmentAsteroidSize2;
    }

    public static GlobalInteractionLevels getFromLevel(int level) {
        if (level > 4) {
            level = 4;
        }
        return values()[level];
    }

    public float getA() {
        return a;
    }

    public float getB() {
        return b;
    }

    public float getFragmentAsteroidCount() {
        return fragmentAsteroidCount;
    }

    public float getFragmentAsteroidSize1() {
        return fragmentAsteroidSize1;
    }

    public float getFragmentAsteroidSize2() {
        return fragmentAsteroidSize2;
    }

    public float getG() {
        return g;
    }

    public int getLevel() {
        return level;
    }

    public float getR() {
        return r;
    }
}
