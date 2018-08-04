package com.shuttle.game.conf;

/**
 * @author Shuttle on 8/5/18.
 */
public enum Levels {
    LOW(0), MEDIUM(1), HIGH(2);
    private int level;

    Levels(int level) {
        this.level = level;
    }

    public static String getLabel(int i) {
        return Levels.values()[i].toString();
    }
}

