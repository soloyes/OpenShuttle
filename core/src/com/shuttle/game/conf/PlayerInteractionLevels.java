package com.shuttle.game.conf;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

import lombok.Getter;

/**
 * @author soloyes on 08/08/18.
 */

public enum PlayerInteractionLevels {
    NONE(0, 0, 0, 0, 0, 1.0f, 0.5f, 0.6f),
    TAN(1, Color.TAN.r, Color.TAN.g, Color.TAN.b, 1f, 1.5f, 0.35f, 0.5f),
    GREEN(2, Color.GREEN.r, Color.GREEN.g, Color.GREEN.b, 1f, 2.0f, 0.3f, 0.45f),
    YELLOW(3, Color.YELLOW.r, Color.YELLOW.g, Color.YELLOW.b, 1f, 2.5f, 0.25f, 0.4f),
    RED(4, Color.RED.r, Color.RED.g, Color.RED.b, 1f, 3.0f, 0.15f, 0.35f);
    @Getter
    private int level;
    @Getter
    private float r, g, b, a;
    @Getter
    private float fragmentAsteroidCount;
    private float fragmentAsteroidSize1, fragmentAsteroidSize2;

    PlayerInteractionLevels
            (
                    int level,
                    float r, float g, float b, float a,
                    float fragmentAsteroidCount, float fragmentAsteroidSize1, float fragmentAsteroidSize2
            ) {
        this.level = level;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.fragmentAsteroidCount = fragmentAsteroidCount;
        this.fragmentAsteroidSize1 = fragmentAsteroidSize1;
        this.fragmentAsteroidSize2 = fragmentAsteroidSize2;
    }

    public static PlayerInteractionLevels getFromLevel(int level) {
        return values()[level % (values().length)];
    }

    public float getSize() {
        return MathUtils.random(fragmentAsteroidSize1, fragmentAsteroidSize2);
    }
}