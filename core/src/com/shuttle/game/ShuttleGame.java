package com.shuttle.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shuttle.game.screens.ScreenManager;

public class ShuttleGame extends Game {
    private SpriteBatch batch;
    private ScreenManager.ScreenType screenType;

    @Override
    public void create() {
        batch = new SpriteBatch();
        ScreenManager.getInstance().init(this, batch);
        ScreenManager.getInstance().changeScreen(screenType);
    }

    @Override
    public void dispose() {
        batch.dispose();
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
