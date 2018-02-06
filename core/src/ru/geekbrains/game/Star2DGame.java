package ru.geekbrains.game;

import com.badlogic.gdx.Game;

import ru.geekbrains.game.screen.GameScreen;
import ru.geekbrains.game.screen.MenuScreen;
import ru.geekbrains.game.star.StarsHandler;

/**
 * Created by sol on 1/29/18.
 */

public class Star2DGame extends Game {
    @Override
    public void create() {
        setScreen(new MenuScreen(this));
    }

    @Override
    public void dispose() {
        StarsHandler.dispose();
        super.dispose();
    }
}
