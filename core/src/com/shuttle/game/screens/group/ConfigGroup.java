package com.shuttle.game.screens.group;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.screens.ScreenManager;

/**
 * @author Shuttle on 6/23/18.
 */
public class ConfigGroup extends Group {
    private Table table;
    private Array<Music> musicArray;
    private GamePreferences preferences = new GamePreferences();

    public ConfigGroup() {
        /////////
        this.setName(GroupType.CONFIG.name());
        this.setVisible(false);

        Skin skin = Assets.getInstance().getAssetManager().get("uiskin/gameSkin.json");

        CheckBox.CheckBoxStyle checkBoxStyle32 = new CheckBox.CheckBoxStyle(
                skin.getDrawable("checkbox-c1"),
                skin.getDrawable("checkbox-pressed-c1"),
                Assets.getInstance().getAssetManager().get("fonts/gomarice32.ttf", BitmapFont.class),
                new Color(0.8f, 0.43f, 0.33f, 1f)
        );

        Label.LabelStyle style64 = new Label.LabelStyle(
                Assets.getInstance().getAssetManager().get("fonts/gomarice64.ttf", BitmapFont.class),
                new Color(0.8f, 0.43f, 0.33f, 1f)
        );

        Label.LabelStyle style96 = new Label.LabelStyle(
                Assets.getInstance().getAssetManager().get("fonts/gomarice96.ttf", BitmapFont.class),
                new Color(0.8f, 0.43f, 0.33f, 1f)
        );
        /////////

        table = new Table();
        table.setFillParent(true);
        table.setPosition(
                ScreenManager.getInstance().getViewport().getWorldWidth() / 2,
                ScreenManager.getInstance().getViewport().getWorldHeight() / 2
        );
        //table.setDebug(true);

        final Slider musicSlider = new Slider(0.0f, 1.0f, 0.1f, false, skin, "default-horizontal");
        musicSlider.setValue(preferences.getMusicVolume());
        musicSlider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                musicArray = new Array<Music>();
                Assets.getInstance().getAssetManager().getAll(Music.class, musicArray);
                for (int i = 0; i < musicArray.size; i++) {
                    musicArray.get(i).setVolume(musicSlider.getValue());
                }
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

        final CheckBox musicCheckBox = new CheckBox("Music", checkBoxStyle32);
        musicCheckBox.setChecked(preferences.isMusicEnabled());
        musicCheckBox.addListener(new ClickListener() {
            @Override
            public boolean handle(Event event) {
                musicArray = new Array<Music>();
                Assets.getInstance().getAssetManager().getAll(Music.class, musicArray);
                boolean enabled = musicCheckBox.isChecked();
                for (int i = 0; i < musicArray.size; i++) {
                    if (enabled) {
                        musicArray.get(i).setVolume(preferences.getMusicVolume());
                        musicArray.get(i).play();
                    } else musicArray.get(i).stop();
                }
                preferences.setMusicEnabled(enabled);
                return false;
            }
        });

        final CheckBox soundCheckBox = new CheckBox("Sound", checkBoxStyle32);
        soundCheckBox.setChecked(preferences.isSoundEnabled());
        soundCheckBox.addListener(new ClickListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = soundCheckBox.isChecked();
                preferences.setSoundEnabled(enabled);
                return false;
            }
        });

        final CheckBox joyStickCheckBox = new CheckBox("JoyStick", checkBoxStyle32);
        joyStickCheckBox.setChecked(preferences.isJoyStickEnabled());
        joyStickCheckBox.addListener(new ClickListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = joyStickCheckBox.isChecked();
                preferences.setJoyStickEnabled(enabled);
                return false;
            }
        });

        Label titleLabel = new Label("Preferences", style96);
        Label volumeMusicLabel = new Label("Music Volume ", style64);
        Label volumeSoundLabel = new Label("Sound Volume ", style64);
        Label musicOnOffLabel = new Label("Music ", style64);
        Label soundOnOffLabel = new Label("Sound Effect ", style64);
        Label joyStickOnOffLabel = new Label("JoyStick ", style64);

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
        table.row();
        table.add(joyStickOnOffLabel).left();
        table.add(joyStickCheckBox);

        this.addActor(table);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        table.draw(batch, parentAlpha);
    }
}