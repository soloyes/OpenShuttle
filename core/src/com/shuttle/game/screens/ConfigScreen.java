package com.shuttle.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;

/**
 * @author Shuttle on 6/19/18.
 */
public class ConfigScreen extends BaseScreen {
    private Slider musicSlider;
    private Slider soundSlider;
    private Skin skin;

    public ConfigScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    void createGUI() {
        skin = new Skin(Gdx.files.internal("uiskin/uiskin.json"));
        musicSlider = new Slider(0.0f, 1.0f, 0.1f, false, skin, "default-horizontal");
        musicSlider.setWidth(ScreenManager.getInstance().getViewport().getWorldWidth() / 2);
        musicSlider.setPosition(ScreenManager.getInstance().getViewport().getWorldWidth() / 4, ScreenManager.getInstance().getViewport().getWorldHeight() / 2);

        soundSlider = new Slider(0.0f, 1.0f, 0.1f, false, skin, "default-horizontal");
        soundSlider.setWidth(ScreenManager.getInstance().getViewport().getWorldWidth() / 2);
        soundSlider.setPosition(ScreenManager.getInstance().getViewport().getWorldWidth() / 4, ScreenManager.getInstance().getViewport().getWorldHeight() / 4);

        stage.addActor(musicSlider);
        stage.addActor(soundSlider);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.draw();

    }
}
