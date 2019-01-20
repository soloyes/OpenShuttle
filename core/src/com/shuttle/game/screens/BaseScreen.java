package com.shuttle.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.effects.BoomBox;

/**
 * @author Shuttle on 4/6/18.
 */
public abstract class BaseScreen implements Screen {
    protected Stage stage;
    protected SpriteBatch batch;
    protected BoomBox boomBox;
    private Color color;

    public BaseScreen() {
    }

    public BaseScreen(SpriteBatch batch) {
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.batch = batch;
        this.color = new Color(Color.valueOf(PaintConstants.BACKGROUND_COLOR_HEX));
    }

    @Override
    public void show() {
        boomBox = new BoomBox();
        Gdx.input.setInputProcessor(stage);
        createGUI();
    }

    public abstract void createGUI();

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
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
        stage.dispose();
    }
}
