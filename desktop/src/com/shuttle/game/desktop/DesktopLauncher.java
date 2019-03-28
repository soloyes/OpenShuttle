package com.shuttle.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.shuttle.game.ShuttleGame;
import com.shuttle.game.screens.ScreenManager;

public class DesktopLauncher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 540;
        config.height = 960;
        config.vSyncEnabled = true;
        //config.foregroundFPS = 10;

        //This is for easy operate during test. Just run game with key from enum ScreenManager.ScreenType
        ScreenManager.ScreenType screenType = ScreenManager.ScreenType.MENU;
        if (arg.length > 0){
            for (ScreenManager.ScreenType t: ScreenManager.ScreenType.values()) {
                if (arg[0].equals(t.name())){
                    screenType = t;
                }
            }
        }
        ShuttleGame game = new ShuttleGame(new ShareDesktop());
        game.setRunScreen(screenType);
        //

        new LwjglApplication(game, config);
    }
}
