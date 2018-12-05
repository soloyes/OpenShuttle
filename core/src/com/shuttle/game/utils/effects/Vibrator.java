package com.shuttle.game.utils.effects;

import com.badlogic.gdx.Gdx;
import com.shuttle.game.conf.GamePreferences;

/**
 * @author Shuttle on 10/23/18.
 */
public class Vibrator {
    public static void vibrate(int ms, int mul) {
        if (GamePreferences.isVibrationEnabled()) {
            Gdx.input.vibrate(ms * mul);
        }
    }

    public static void vibrate(int ms) {
        if (GamePreferences.isVibrationEnabled()) {
            Gdx.input.vibrate(ms);
        }
    }
}
