package com.shuttle.game.screens.group;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.shuttle.game.Joystick;
import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.conf.VConstants;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.effects.BoomBox;
import com.shuttle.game.utils.effects.Vibrator;

/**
 * @author Shuttle on 6/23/18.
 */
public class ConfigGroup extends Group {
    private Table table;
    private Array<Music> musicArray;
    private ScreenManager.ScreenType screenType;
    private BoomBox boomBox;
    private Joystick joystick;

    public ConfigGroup(ScreenManager.ScreenType screenType) {
        /////////
        this.boomBox = new BoomBox();
        this.joystick = new Joystick();
        this.setName(GroupType.CONFIG.name());
        this.setVisible(false);
        this.screenType = screenType;

        final String on = "ON";
        final String off = "OFF";

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


        Label.LabelStyle style48 = new Label.LabelStyle(
                Assets.getInstance().getAssetManager().get(PaintConstants.FONT48, BitmapFont.class),
                new Color(0.8f, 0.43f, 0.33f, 1f)
        );

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
                Rules.WORLD_WIDTH / 2,
                Rules.WORLD_HEIGHT / 2
        );
        Table innerTable = new Table();

        Slider.SliderStyle sliderStyle = new Slider.SliderStyle(skin.get("default-horizontal", Slider.SliderStyle.class));
        sliderStyle.knob.setMinHeight(50);
        sliderStyle.knobOver.setMinHeight(50);
        sliderStyle.knob.setMinWidth(50);
        sliderStyle.knobOver.setMinWidth(50);

        final Slider musicSlider = new Slider(0.0f, 1.0f, 0.1f, false, sliderStyle);
        musicSlider.setValue(GamePreferences.getMusicVolume());
        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                musicArray = new Array<Music>();
                Assets.getInstance().getAssetManager().getAll(Music.class, musicArray);
                for (int i = 0; i < musicArray.size; i++) {
                    musicArray.get(i).setVolume(musicSlider.getValue());
                }
                GamePreferences.setMusicVolume(musicSlider.getValue());
                boomBox.playSound(MSConstants.BUTTON_SOUND);
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

        final Label levelLabelDynamic = new Label(Rules.StartLevel.values()[GamePreferences.getLevel()].toString(), style48);
        final Label effectsLabelDynamic = new Label(GamePreferences.EffectLevel.values()[GamePreferences.getPrefEffectsLevel()].toString(), style48);


        final Slider levelSlider = new Slider(0.0f, Rules.StartLevel.values().length - 1, 1.0f, false, sliderStyle);
        levelSlider.setValue(GamePreferences.getLevel());
        levelSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GamePreferences.setLevel((int) levelSlider.getValue());
                levelLabelDynamic.setText(Rules.StartLevel.values()[(int) levelSlider.getValue()].toString());
                boomBox.playSound(MSConstants.BUTTON_SOUND);

            }
        });

        final Slider effectsSlider = new Slider(0.0f, GamePreferences.EffectLevel.values().length - 1, 1.0f, false, sliderStyle);
        effectsSlider.setValue(GamePreferences.getPrefEffectsLevel());
        effectsSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GamePreferences.setPrefEffectsLevel((int) effectsSlider.getValue());
                effectsLabelDynamic.setText(GamePreferences.EffectLevel.values()[(int) effectsSlider.getValue()].toString());
                boomBox.playSound(MSConstants.BUTTON_SOUND);
            }
        });

        final Slider joyStickModeSlider = new Slider(0, 4, 1, false, sliderStyle);
        joyStickModeSlider.setValue(GamePreferences.getJoyStickMode());
        joyStickModeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GamePreferences.setJoyStickMode((int) joyStickModeSlider.getValue());
                boomBox.playSound(MSConstants.BUTTON_SOUND);
            }
        });


        final CheckBox musicCheckBox = new CheckBox("", checkBoxStyle48);
        musicCheckBox.setText(GamePreferences.isMusicEnabled() ? on : off);
        musicCheckBox.setChecked(GamePreferences.isMusicEnabled());
        musicCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean enabled = musicCheckBox.isChecked();
                GamePreferences.setMusicEnabled(enabled);
                musicCheckBox.setText(enabled ? on : off);
                boomBox.setAllMusicVolume(GamePreferences.getMusicVolume());
                boomBox.stopAllMusicAndPlayThese(MSConstants.MENU_MUSIC);
                boomBox.playSound(MSConstants.CHECKBOX);
            }
        });

        final CheckBox soundCheckBox = new CheckBox("", checkBoxStyle48);
        soundCheckBox.setText(GamePreferences.isSoundEnabled() ? on : off);
        soundCheckBox.setChecked(GamePreferences.isSoundEnabled());
        soundCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boomBox.playSound(MSConstants.CHECKBOX);
                boolean enabled = soundCheckBox.isChecked();
                soundCheckBox.setText(enabled ? on : off);
                GamePreferences.setSoundEnabled(enabled);
            }
        });

        final CheckBox joyStickCheckBox = new CheckBox("", checkBoxStyle48);
        joyStickCheckBox.setText(GamePreferences.isJoyStickEnabled() ? on : off);
        joyStickCheckBox.setChecked(GamePreferences.isJoyStickEnabled());
        joyStickCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean enabled = joyStickCheckBox.isChecked();
                joyStickCheckBox.setText(enabled ? on : off);
                GamePreferences.setJoyStickEnabled(enabled);
                boomBox.playSound(MSConstants.CHECKBOX);
            }
        });

        final CheckBox joyStickColorCheckBox = new CheckBox("", checkBoxStyle48);
        joyStickColorCheckBox.setText(GamePreferences.isJoyStickColorEnabled() ? "CYAN" : "WHITE");
        joyStickColorCheckBox.setChecked(GamePreferences.isJoyStickColorEnabled());
        joyStickColorCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean enabled = joyStickColorCheckBox.isChecked();
                joyStickColorCheckBox.setText(enabled ? "CYAN" : "WHITE");
                GamePreferences.setJoyStickColor(enabled);
                boomBox.playSound(MSConstants.CHECKBOX);
            }
        });

        final CheckBox childCheckBox = new CheckBox("", checkBoxStyle48);
        childCheckBox.setText(GamePreferences.isChildEnabled() ? on : off);
        childCheckBox.setChecked(GamePreferences.isChildEnabled());
        childCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean enabled = childCheckBox.isChecked();
                childCheckBox.setText(enabled ? on : off);
                GamePreferences.setChildEnabled(enabled);
                boomBox.playSound(MSConstants.CHECKBOX);
            }
        });

        final CheckBox vibrationCheckBox = new CheckBox("", checkBoxStyle48);
        vibrationCheckBox.setText(GamePreferences.isVibrationEnabled() ? on : off);
        vibrationCheckBox.setChecked(GamePreferences.isVibrationEnabled());
        vibrationCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean enabled = vibrationCheckBox.isChecked();
                vibrationCheckBox.setText(enabled ? on : off);
                GamePreferences.setVibrationEnabled(enabled);
                if (enabled) {
                    Vibrator.vibrate(VConstants.VIBRATION_ENABLED);
                }
                boomBox.playSound(MSConstants.CHECKBOX);
            }
        });

        Label titleLabel = new Label("Preferences", style96);
        Label volumeMusicLabel = new Label("Music Volume ", style64);
        Label volumeSoundLabel = new Label("Sound Volume ", style64);
        Label musicOnOffLabel = new Label("Music ", style64);
        Label soundOnOffLabel = new Label("Sound Effect ", style64);
        Label joyStickOnOffLabel = new Label("JoyStick ", style64);
        Label levelLabel = new Label("Level ", style64);
        Label vibrationLabel = new Label("Vibration ", style64);
        Label effectsLabel = new Label("Effects ", style64);
        Label childLabel = new Label("Child ", style64);

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
        table.row();
        table.add();
        table.add(joystick)
                .width(PaintConstants.JOYSTICK_WIDTH)
                .height(PaintConstants.JOYSTICK_HEIGHT)
                .align(Align.center);
        innerTable.row().expand();
        innerTable.add(joyStickModeSlider).left().fillX();
        innerTable.row().expand();
        innerTable.add(joyStickColorCheckBox).left();

        table.add(innerTable).left().fillX();
        table.row();
        table.add(childLabel).left();
        table.add();
        table.add(childCheckBox).left();


        switch (screenType) {
            case MENU:
                table.row();
                table.add(levelLabel).left();
                table.add(levelSlider).padRight(10).width(300);
                table.add(levelLabelDynamic).left().width(200);

                table.row();
                table.add(effectsLabel).left();
                table.add(effectsSlider).padRight(10).width(300);
                table.add(effectsLabelDynamic).left().width(200);
                break;
        }
        table.row();
        table.add(vibrationLabel).left();
        table.add();
        table.add(vibrationCheckBox).left();
        //table.setDebug(true);
        //innerTable.setDebug(true);
        this.addActor(table);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        table.draw(batch, parentAlpha);
    }
}