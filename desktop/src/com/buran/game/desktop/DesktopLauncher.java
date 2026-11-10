package com.buran.game.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.buran.game.BuranGame;
import com.buran.game.screens.ScreenManager;

/**
 * @author soloyes on 03/06/18.
 */

public class DesktopLauncher {
    private static ScreenResolutionTester tester = ScreenResolutionTester.SIXTEEN_TO_NINE;

    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        try {
            if (arg.length > 1) {
                tester = ScreenResolutionTester.valueOf(arg[1]);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        config.setTitle("Buran-19");
        config.setWindowedMode(tester.getHalfWidth(), tester.getHalfHeight());
        config.useVsync(true);
        config.setPreferencesConfig(".prefs/", com.badlogic.gdx.Files.FileType.External);
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

        new Lwjgl3Application(game, config);
    }
}