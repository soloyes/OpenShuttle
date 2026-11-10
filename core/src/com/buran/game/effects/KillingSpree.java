package com.buran.game.effects;

/**
 * @author soloyes on 29/10/18.
 */

public enum KillingSpree {
    NONE("", 0, 1.0f, 0.0f),
    KILL("KILL", 1, 1.0f, 4.0f),
    DOUBLE("DOUBLE KILL", 2, 1.1f, 5.0f),
    TRIPLE("TRIPLE KILL", 3, 1.2f, 5.0f),
    MULTI("MULTI KILL", 4, 1.3f, 5.0f),
    ULTRA("ULTRA KILL!", 5, 1.5f, 6.0f),
    RAMPAGE("RAMPAGE...", 6, 2.0f, 7.0f),
    DISTRACTION("DISTRACTION!", 7, 2.5f, 10.0f),
    HOLY("WHITE FURY!", 8, 3.0f, 15.0f);

    private String text;
    private int killsCount;
    private float scoreModifier;
    private float textSizeModifier;

    KillingSpree(String text, int killsCount, float scoreModifier, float textSizeModifier) {
        this.text = text;
        this.killsCount = killsCount;
        this.scoreModifier = scoreModifier;
        this.textSizeModifier = textSizeModifier;
    }

    public static KillingSpree getKillingSpreeFromKillsCount(int killsCount) {
        KillingSpree result = NONE;
        for (KillingSpree k : values()) {
            if (killsCount >= k.killsCount) {
                result = k;
            }
        }
        return result;
    }

    public float getScoreModifier() {
        return scoreModifier;
    }

    public String getText() {
        return text;
    }

    public float getTextSizeModifier() {
        return textSizeModifier;
    }
}