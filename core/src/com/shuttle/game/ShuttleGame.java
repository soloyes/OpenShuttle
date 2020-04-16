package com.shuttle.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shuttle.game.screens.ScreenManager;

import lombok.Getter;

/**
 * @author Shuttle on 03/06/18.
 */

public class ShuttleGame extends Game {
    private SpriteBatch batch;
    private ScreenManager.ScreenType screenType;
    @Getter
    private static Share share;

    public ShuttleGame(Share share) {
        ShuttleGame.share = share;
    }

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        ScreenManager.getInstance().init(this, batch);
        if (screenType == null) {
            screenType = ScreenManager.ScreenType.MENU;
        }
        ScreenManager.getInstance().changeScreen(screenType);
    }

    @Override
    public void dispose() {
        batch.dispose();
        getScreen().dispose();
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        getScreen().render(dt);
    }

    public void setRunScreen(ScreenManager.ScreenType screenType) {
        this.screenType = screenType;
    }
}