package com.buran.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.buran.game.BuranGame;
import com.buran.game.screens.ScreenManager;

/**
 * @author soloyes on 03/06/18.
 */

public class DesktopLauncher {
    private static ScreenResolutionTester tester = ScreenResolutionTester.SIXTEEN_TO_NINE;

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        try {
            if (arg.length > 1) {
                tester = ScreenResolutionTester.valueOf(arg[1]);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        config.width = tester.getHalfWidth();
        config.height = tester.getHalfHeight();
        config.vSyncEnabled = true;
        //config.foregroundFPS = 10;

        //This is for easy operate during test. Just run game with key from enum ScreenManager.ScreenType
        ScreenManager.ScreenType screenType = ScreenManager.ScreenType.MENU;
        if (arg.length > 0) {
            for (ScreenManager.ScreenType t : ScreenManager.ScreenType.values()) {
                if (arg[0].equals(t.name())) {
                    screenType = t;
                }
            }
        }
        BuranGame game = new BuranGame(new AdsDesktop(), new ShareDesktop(), false, tester.getWidth(), tester.getHeight());
        game.setRunScreen(screenType);
        //

        new LwjglApplication(game, config);
    }
}