package com.buran.game.effects;

import com.badlogic.gdx.Gdx;
import com.buran.game.conf.VConstants;
import com.buran.game.ui.groups.mvc.profiles.Profiles;

/**
 * @author soloyes on 10/23/18.
 */

public class Vibrator {
    public static void vibrateGame(int ms, int mul) {
        if (Profiles.getSettings().getVibration() == 2) {
            int t = ms * mul;
            if (t <= 0) {
                t = VConstants.PLAYER_ANOMALY_CORROSIVE_DESTROY;
            }
            Gdx.input.vibrate(t);
        }
    }

    public static void vibrateGame(int ms) {
        if (Profiles.getSettings().getVibration() == 2) {
            Gdx.input.vibrate(ms);
        }
    }

    public static void vibrateUI(int ms) {
        if (Profiles.getSettings().getVibration() >= 1) {
            Gdx.input.vibrate(ms);
        }
    }
}