package com.shuttle.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * @author Shuttle on 4/6/18.
 */
public abstract class BaseScreen implements Screen {
    protected Stage stage;

    public BaseScreen(SpriteBatch batch) {
        stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        createGUI();
    }

    abstract void createGUI();

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0.05f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        ScreenManager.getInstance().resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
