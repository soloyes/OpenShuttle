package com.shuttle.game.conf;

/**
 * @author Shuttle on 8/5/18.
 */
public class GameLevel {
    public void commitLevel(GamePreferences preferences) {
        switch (Levels.values()[(int) preferences.getLevel()]) {
            case LOW:
                break;
            case MEDIUM:
                break;
            case HIGH:
                break;
        }
    }
}
