package com.shuttle.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.shuttle.game.Assets;
import com.shuttle.game.conf.GamePreferences;

/**
 * @author Shuttle on 6/19/18.
 */
public class ConfigScreen extends BaseScreen {
    private GamePreferences preferences;

    public ConfigScreen(SpriteBatch batch) {
        super(batch);
        preferences = new GamePreferences();
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    void createGUI() {
        Table table = new Table();
        table.setFillParent(true);

        Skin skin = new Skin(Gdx.files.internal("uiskin/gameSkin.json"));
        final Slider musicSlider = new Slider(0.0f, 1.0f, 0.1f, false, skin, "default-horizontal");
        musicSlider.setValue(preferences.getMusicVolume());
        musicSlider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                preferences.setMusicVolume(musicSlider.getValue());
                return false;
            }
        });

        final Slider soundSlider = new Slider(0.0f, 1.0f, 0.1f, false, skin, "default-horizontal");
        soundSlider.setValue(preferences.getSoundVolume());
        soundSlider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                preferences.setSoundVolume(soundSlider.getValue());
                return false;
            }
        });

        CheckBox.CheckBoxStyle checkBoxStyle32 = new CheckBox.CheckBoxStyle(
                skin.getDrawable("checkbox-c1"),
                skin.getDrawable("checkbox-pressed-c1"),
                Assets.getInstance().getAssetManager().get("fonts/gomarice32.ttf", BitmapFont.class),
                new Color(0.8f, 0.43f, 0.33f, 1f)
        );

        final CheckBox musicCheckBox = new CheckBox("Music", checkBoxStyle32);
        musicCheckBox.setChecked(preferences.isMusicEnabled());
        musicCheckBox.addListener(new ClickListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = musicCheckBox.isChecked();
                preferences.setMusicEnabled(enabled);
                return false;
            }
        });

        final CheckBox soundCheckBox = new CheckBox("Sound", checkBoxStyle32);
        soundCheckBox.setChecked(preferences.isSoundEffectsEnabled());
        soundCheckBox.addListener(new ClickListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = soundCheckBox.isChecked();
                preferences.setSoundEffectsEnabled(enabled);
                return false;
            }
        });

        Label.LabelStyle style64 = new Label.LabelStyle(
                Assets.getInstance().getAssetManager().get("fonts/gomarice64.ttf", BitmapFont.class),
                new Color(0.8f, 0.43f, 0.33f, 1f)
        );

        Label.LabelStyle style96 = new Label.LabelStyle(
                Assets.getInstance().getAssetManager().get("fonts/gomarice64.ttf", BitmapFont.class),
                new Color(0.8f, 0.43f, 0.33f, 1f)
        );

        Label titleLabel = new Label("Preferences", style96);

        Label volumeMusicLabel = new Label("Music Volume ", style64);

        Label volumeSoundLabel = new Label("Sound Volume ", style64);
        Label musicOnOffLabel = new Label("Music ", style64);
        Label soundOnOffLabel = new Label("Sound Effect ", style64);

        table.add(titleLabel).colspan(2);
        table.row();
        table.add(volumeMusicLabel).left();
        table.add(musicSlider);
        table.row();
        table.add(musicOnOffLabel).left();
        table.add(musicCheckBox);
        table.row();
        table.add(volumeSoundLabel).left();
        table.add(soundSlider);
        table.row();
        table.add(soundOnOffLabel).left();
        table.add(soundCheckBox);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.draw();
    }
}
