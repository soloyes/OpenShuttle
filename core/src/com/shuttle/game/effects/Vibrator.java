package com.shuttle.game.effects;

import com.badlogic.gdx.Gdx;
import com.shuttle.game.conf.VConstants;
import com.shuttle.game.ui.groups.mvc.profiles.Profiles;

/**
 * @author Shuttle on 10/23/18.
 */

public class Vibrator {
    public static void vibrate(int ms, int mul) {
        if (Profiles.getSettings().isVibration()) {
            int t = ms * mul;
            if (t <= 0) {
                t = VConstants.PLAYER_ANOMALY_CORROSIVE_DESTROY;
            }
            Gdx.input.vibrate(t);
        }
    }

    public static void vibrate(int ms) {
        if (Profiles.getSettings().isVibration()) {
            Gdx.input.vibrate(ms);
        }
    }
}