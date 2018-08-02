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

        //todo: this is for easy operate during test needed Screen, just run game with key from enum ScreenManager.ScreenType
        ScreenManager.ScreenType screenType = ScreenManager.ScreenType.MENU;;
        if (arg.length > 0){
            for (ScreenManager.ScreenType t: ScreenManager.ScreenType.values()) {
                if (arg[0].equals(t.name())){
                    screenType = t;
                }
            }
        }
        ShuttleGame game = new ShuttleGame();
        game.setRunScreen(screenType);
        //

        new LwjglApplication(game, config);
    }
}
