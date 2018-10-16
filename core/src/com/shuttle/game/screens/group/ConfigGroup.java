package com.shuttle.game.screens.group;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.utils.BoomBox;

/**
 * @author Shuttle on 6/23/18.
 */
public class ConfigGroup extends Group {
    private Table table;
    private Array<Music> musicArray;
    //private GamePreferences preferences = new GamePreferences();
    private ScreenManager.ScreenType screenType;
    private BoomBox boomBox;

    public ConfigGroup(ScreenManager.ScreenType screenType) {
        /////////
        this.boomBox = new BoomBox();
        this.setName(GroupType.CONFIG.name());
        this.setVisible(false);
        this.screenType = screenType;

        Skin skin = Assets.getInstance().getAssetManager().get(PaintConstants.SKIN_FILE);

        CheckBox.CheckBoxStyle checkBoxStyle48 = new CheckBox.CheckBoxStyle(
                skin.getDrawable("checkbox-c1"),
                skin.getDrawable("checkbox-pressed-c1"),
                Assets.getInstance().getAssetManager().get(PaintConstants.FONT48, BitmapFont.class),
                new Color(0.8f, 0.43f, 0.33f, 1f)
        );
        checkBoxStyle48.checkboxOn.setMinHeight(50);
        checkBoxStyle48.checkboxOn.setMinWidth(50);
        checkBoxStyle48.checkboxOff.setMinWidth(50);
        checkBoxStyle48.checkboxOff.setMinHeight(50);

        Label.LabelStyle style64 = new Label.LabelStyle(
                Assets.getInstance().getAssetManager().get(PaintConstants.FONT64, BitmapFont.class),
                new Color(0.8f, 0.43f, 0.33f, 1f)
        );

        Label.LabelStyle style96 = new Label.LabelStyle(
                Assets.getInstance().getAssetManager().get(PaintConstants.FONT96, BitmapFont.class),
                new Color(0.8f, 0.43f, 0.33f, 1f)
        );
        /////////

        table = new Table();
        table.setPosition(
                ScreenManager.getInstance().getViewport().getWorldWidth() / 2,
                ScreenManager.getInstance().getViewport().getWorldHeight() / 2
        );

        Slider.SliderStyle sliderStyle = new Slider.SliderStyle(skin.get("default-horizontal", Slider.SliderStyle.class));
        sliderStyle.knob.setMinHeight(25);
        sliderStyle.knobOver.setMinHeight(25);
        sliderStyle.knob.setMinWidth(25);
        sliderStyle.knobOver.setMinWidth(25);

        final Slider musicSlider = new Slider(0.0f, 1.0f, 0.1f, false, sliderStyle);
        musicSlider.setValue(GamePreferences.getMusicVolume());
        musicSlider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                musicArray = new Array<Music>();
                Assets.getInstance().getAssetManager().getAll(Music.class, musicArray);
                for (int i = 0; i < musicArray.size; i++) {
                    musicArray.get(i).setVolume(musicSlider.getValue());
                }
                GamePreferences.setMusicVolume(musicSlider.getValue());
                return false;
            }
        });

        final Slider soundSlider = new Slider(0.0f, 1.0f, 0.1f, false, sliderStyle);
        soundSlider.setValue(GamePreferences.getSoundVolume());
        soundSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GamePreferences.setSoundVolume(soundSlider.getValue());
                boomBox.playSound(MSConstants.BUTTON_SOUND);
            }
        });

        final Label levelLabelDynamic = new Label(Rules.StartLevel.values()[GamePreferences.getLevel()].toString(), style64);
        final Label effectsLabelDynamic = new Label(GamePreferences.EffectLevel.values()[GamePreferences.getEffectsLevel()].toString(), style64);


        final Slider levelSlider = new Slider(0.0f, 2.0f, 1.0f, false, sliderStyle);
        levelSlider.setValue(GamePreferences.getLevel());
        levelSlider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                GamePreferences.setLevel((int) levelSlider.getValue());
                levelLabelDynamic.setText(Rules.StartLevel.values()[(int) levelSlider.getValue()].toString());
                return false;
            }
        });

        final Slider effectsSlider = new Slider(0.0f, GamePreferences.EffectLevel.values().length - 1, 1.0f, false, sliderStyle);
        effectsSlider.setValue(GamePreferences.getEffectsLevel());
        effectsSlider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                GamePreferences.setEffectsLevel((int) effectsSlider.getValue());
                effectsLabelDynamic.setText(GamePreferences.EffectLevel.values()[(int) effectsSlider.getValue()].toString());
                return false;
            }
        });

        final CheckBox musicCheckBox = new CheckBox("Music", checkBoxStyle48);
        musicCheckBox.setChecked(GamePreferences.isMusicEnabled());
        musicCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean enabled = musicCheckBox.isChecked();
                GamePreferences.setMusicEnabled(enabled);
                boomBox.setAllMusicVolume(GamePreferences.getMusicVolume());
                boomBox.stopAllMusicAndPlay(MSConstants.MENU_MUSIC);
            }
        });

        final CheckBox soundCheckBox = new CheckBox("Sound", checkBoxStyle48);
        soundCheckBox.setChecked(GamePreferences.isSoundEnabled());
        soundCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean enabled = soundCheckBox.isChecked();
                GamePreferences.setSoundEnabled(enabled);
            }
        });

        final CheckBox joyStickCheckBox = new CheckBox("JoyStick", checkBoxStyle48);
        joyStickCheckBox.setChecked(GamePreferences.isJoyStickEnabled());
        joyStickCheckBox.addListener(new ClickListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = joyStickCheckBox.isChecked();
                GamePreferences.setJoyStickEnabled(enabled);
                return false;
            }
        });

        Label titleLabel = new Label("Preferences", style96);
        Label volumeMusicLabel = new Label("Music Volume ", style64);
        Label volumeSoundLabel = new Label("Sound Volume ", style64);
        Label musicOnOffLabel = new Label("Music ", style64);
        Label soundOnOffLabel = new Label("Sound Effect ", style64);
        Label joyStickOnOffLabel = new Label("JoyStick ", style64);
        Label levelLabel = new Label("Level ", style64);

        Label effectsLabel = new Label("Effects ", style64);

        table.add(titleLabel).colspan(3);
        table.row();
        table.add(volumeMusicLabel).left();
        table.add();
        table.add(musicSlider).left().fillX();
        table.row();
        table.add(musicOnOffLabel).left();
        table.add();
        table.add(musicCheckBox).left();
        table.row();
        table.add(volumeSoundLabel).left();
        table.add();
        table.add(soundSlider).left().fillX();
        table.row();
        table.add(soundOnOffLabel).left();
        table.add();
        table.add(soundCheckBox).left();
        table.row();
        table.add(joyStickOnOffLabel).left();
        table.add();
        table.add(joyStickCheckBox).left();

        switch (screenType) {
            case MENU:
                table.row();
                table.add(levelLabel).left();
                table.add(levelSlider).padRight(10);
                table.add(levelLabelDynamic).left().width(200);

                table.row();
                table.add(effectsLabel).left();
                table.add(effectsSlider).padRight(10);
                table.add(effectsLabelDynamic).left().width(200);
                break;
        }
        //table.setDebug(true);
        this.addActor(table);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        table.draw(batch, parentAlpha);
    }
}