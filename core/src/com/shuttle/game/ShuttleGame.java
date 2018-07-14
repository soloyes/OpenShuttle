package com.shuttle.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shuttle.game.screens.ScreenManager;

public class ShuttleGame extends Game {
    private SpriteBatch batch;
    private ScreenManager.ScreenType screenType;
    private FileHandle usernameFile;

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        ScreenManager.getInstance().init(this, batch);
        if (screenType == null) {
            screenType = ScreenManager.ScreenType.MENU;
        }
        ScreenManager.getInstance().changeScreen(screenType);
        this.usernameFile = Gdx.files.local("username");
        usernameFile.writeString("", false);
    }

    @Override
    public void dispose() {
        batch.dispose();
        usernameFile.delete();
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
