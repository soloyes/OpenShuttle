package com.buran.game.effects;

import com.badlogic.gdx.Gdx;

/**
 * @author soloyes on 10/23/18.
 */

public class Vibrator {
    public static void vibrate(int ms, int mul) {
        if (com.buran.game.ui.groups.mvc.profiles.Profiles.getSettings().isVibration()) {
            int t = ms * mul;
            if (t <= 0) {
                t = com.buran.game.conf.VConstants.PLAYER_ANOMALY_CORROSIVE_DESTROY;
            }
            Gdx.input.vibrate(t);
        }
    }

    public static void vibrate(int ms) {
        if (com.buran.game.ui.groups.mvc.profiles.Profiles.getSettings().isVibration()) {
            Gdx.input.vibrate(ms);
        }
    }
}