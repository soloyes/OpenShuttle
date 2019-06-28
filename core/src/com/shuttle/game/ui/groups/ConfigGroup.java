package com.shuttle.game.ui.groups;

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
import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.conf.VConstants;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.effects.Vibrator;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.ui.Joystick;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 6/23/18.
 */
public class ConfigGroup extends Group {
    private static ConfigGroup instance;
    private final Table table;
    private Array<Music> musicArray;
    private BoomBox boomBox;

    private final Table joystickTable;
    private final Slider musicSlider;
    private final Slider soundSlider;
    private final Slider levelSlider;
    private final Slider effectsSlider;
    private final Slider joyStickModeSlider;
    private final CheckBox musicCheckBox;
    private final CheckBox soundCheckBox;
    private final CheckBox joyStickCheckBox;
    private final CheckBox joyStickColorCheckBox;
    private final CheckBox childCheckBox;
    private final CheckBox vibrationCheckBox;
    private final String on = "ON";
    private final String off = "OFF";
    private final String cyan = "CYAN";
    private final String white = "WHITE";

    public static ConfigGroup getInstance() {
        if (instance == null) {
            instance = new ConfigGroup();
        }
        return instance;
    }

    public void dispose() {
        instance = null;
    }

    public void refresh() {
        musicSlider.setValue(GamePreferences.getMusicVolume());
        soundSlider.setValue(GamePreferences.getSoundVolume());
        levelSlider.setValue(GamePreferences.getLevel());
        effectsSlider.setValue(GamePreferences.getPrefEffectsLevel());
        joyStickModeSlider.setValue(GamePreferences.getJoyStickMode());
        musicCheckBox.setText(GamePreferences.isMusicEnabled() ? on : off);
        musicCheckBox.setChecked(GamePreferences.isMusicEnabled());
        soundCheckBox.setText(GamePreferences.isSoundEnabled() ? on : off);
        soundCheckBox.setChecked(GamePreferences.isSoundEnabled());
        joyStickCheckBox.setText(GamePreferences.isJoyStickEnabled() ? on : off);
        joyStickCheckBox.setChecked(GamePreferences.isJoyStickEnabled());
        joystickTable.setVisible(joyStickCheckBox.isChecked());
        joyStickColorCheckBox.setText(GamePreferences.isJoyStickColorEnabled() ? cyan : white);
        joyStickColorCheckBox.setChecked(GamePreferences.isJoyStickColorEnabled());
        childCheckBox.setText(GamePreferences.isChildEnabled() ? on : off);
        childCheckBox.setChecked(GamePreferences.isChildEnabled());
        vibrationCheckBox.setText(GamePreferences.isVibrationEnabled() ? on : off);
        vibrationCheckBox.setChecked(GamePreferences.isVibrationEnabled());
    }

    private ConfigGroup() {
        /////////
        this.boomBox = new BoomBox();
        Joystick joystick = new Joystick();
        this.setName(GroupType.CONFIG.name());
        this.setVisible(false);
        ScreenManager.ScreenType screenType = ScreenManager.getInstance().getCurrentScreen();

        Skin skin = Assets.getInstance().getAssetManager().get(PaintConstants.SKIN_FILE);

        CheckBox.CheckBoxStyle checkBoxStyle48 = skin.get("default", CheckBox.CheckBoxStyle.class);
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

        joystickTable = new Table();
        final Table innerTable = new Table();

        Slider.SliderStyle sliderStyle = new Slider.SliderStyle(skin.get("default-horizontal", Slider.SliderStyle.class));
        sliderStyle.knob.setMinHeight(50);
        //sliderStyle.knobOver.setMinHeight(50);
        sliderStyle.knob.setMinWidth(50);
        //sliderStyle.knobOver.setMinWidth(50);

        musicSlider = new Slider(0.0f, 1.0f, 0.1f, false, sliderStyle);
        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                musicArray = new Array<Music>();
                Assets.getInstance().getAssetManager().getAll(Music.class, musicArray);
                for (int i = 0; i < musicArray.size; i++) {
                    musicArray.get(i).setVolume(musicSlider.getValue());
                }
                GamePreferences.setMusicVolume(musicSlider.getValue());
                if (musicSlider.isDragging()) {
                    boomBox.playSound(MSConstants.UI_MENU);
                }
            }
        });

        soundSlider = new Slider(0.0f, 1.0f, 0.1f, false, sliderStyle);
        soundSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GamePreferences.setSoundVolume(soundSlider.getValue());
                if (soundSlider.isDragging()) {
                    boomBox.playSound(MSConstants.UI_MENU);
                }
            }
        });

        final Label levelLabelDynamic = new Label(Rules.StartLevel.values()[GamePreferences.getLevel()].toString(), style48);
        final Label effectsLabelDynamic = new Label(GamePreferences.EffectLevel.values()[GamePreferences.getPrefEffectsLevel()].toString(), style48);

        levelSlider = new Slider(0.0f, Rules.StartLevel.values().length - 1, 1.0f, false, sliderStyle);
        levelSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GamePreferences.setLevel((int) levelSlider.getValue());
                levelLabelDynamic.setText(Rules.StartLevel.values()[(int) levelSlider.getValue()].toString());
                if (levelSlider.isDragging()) {
                    boomBox.playSound(MSConstants.UI_MENU);
                }
            }
        });

        effectsSlider = new Slider(0.0f, GamePreferences.EffectLevel.values().length - 1, 1.0f, false, sliderStyle);
        effectsSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GamePreferences.setPrefEffectsLevel((int) effectsSlider.getValue());
                effectsLabelDynamic.setText(GamePreferences.EffectLevel.values()[(int) effectsSlider.getValue()].toString());
                if (effectsSlider.isDragging()) {
                    boomBox.playSound(MSConstants.UI_MENU);
                }
            }
        });

        joyStickModeSlider = new Slider(0, 4, 1, false, sliderStyle);
        joyStickModeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GamePreferences.setJoyStickMode((int) joyStickModeSlider.getValue());
                if (joyStickModeSlider.isDragging()) {
                    boomBox.playSound(MSConstants.UI_MENU);
                }
            }
        });

        musicCheckBox = new CheckBox("", checkBoxStyle48);
        musicCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean enabled = musicCheckBox.isChecked();
                GamePreferences.setMusicEnabled(enabled);
                musicCheckBox.setText(enabled ? on : off);
                boomBox.setAllMusicVolume(GamePreferences.getMusicVolume());
                boomBox.stopAllMusicAndPlayThese(MSConstants.MENU_SONG);
                if (musicCheckBox.isPressed()) {
                    boomBox.playSound(MSConstants.UI_CHECKBOX);
                }
            }
        });

        soundCheckBox = new CheckBox("", checkBoxStyle48);
        soundCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean enabled = soundCheckBox.isChecked();
                soundCheckBox.setText(enabled ? on : off);
                GamePreferences.setSoundEnabled(enabled);
                if (soundCheckBox.isPressed()) {
                    boomBox.playSound(MSConstants.UI_CHECKBOX);
                }
            }
        });

        joyStickCheckBox = new CheckBox("", checkBoxStyle48);
        joyStickCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean enabled = joyStickCheckBox.isChecked();
                joyStickCheckBox.setText(enabled ? on : off);
                GamePreferences.setJoyStickEnabled(enabled);
                joystickTable.setVisible(enabled);
                if (joyStickCheckBox.isPressed()) {
                    boomBox.playSound(MSConstants.UI_CHECKBOX);
                }
            }
        });

        joyStickColorCheckBox = new CheckBox("", checkBoxStyle48);
        joyStickColorCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean enabled = joyStickColorCheckBox.isChecked();
                joyStickColorCheckBox.setText(enabled ? cyan : white);
                GamePreferences.setJoyStickColor(enabled);
                if (joyStickColorCheckBox.isPressed()) {
                    boomBox.playSound(MSConstants.UI_CHECKBOX);
                }
            }
        });

        childCheckBox = new CheckBox("", checkBoxStyle48);
        childCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean enabled = childCheckBox.isChecked();
                childCheckBox.setText(enabled ? on : off);
                GamePreferences.setChildEnabled(enabled);
                if (childCheckBox.isPressed()) {
                    boomBox.playSound(MSConstants.UI_CHECKBOX);
                }
            }
        });

        vibrationCheckBox = new CheckBox("", checkBoxStyle48);
        vibrationCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean enabled = vibrationCheckBox.isChecked();
                vibrationCheckBox.setText(enabled ? on : off);
                GamePreferences.setVibrationEnabled(enabled);
                if (enabled) {
                    Vibrator.vibrate(VConstants.VIBRATION_ENABLED);
                }
                if (vibrationCheckBox.isPressed()) {
                    boomBox.playSound(MSConstants.UI_CHECKBOX);
                }
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
        table.add().padRight(10).width(300);
        table.add(musicSlider).left().fillX().width(200);
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

        switch (screenType) {
            case MENU:
                table.row();
                table.add(childLabel).left();
                table.add();
                table.add(childCheckBox).left();

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
        table.row();
        table.add(joyStickOnOffLabel).left();
        table.add();
        table.add(joyStickCheckBox).left();
        table.row();
        table.add(joystickTable).colspan(3);

        joystickTable.row();
        joystickTable.add(innerTable);
        joystickTable.add(joystick)
                .width(PaintConstants.JOYSTICK_WIDTH)
                .height(PaintConstants.JOYSTICK_HEIGHT)
                .align(Align.center);
        joystickTable.add();

        innerTable.row().width(200).padRight(45);
        innerTable.add(joyStickModeSlider);
        innerTable.row();
        innerTable.add(joyStickColorCheckBox).left();

        //table.setDebug(true);
        //innerTable.setDebug(true);
        //joystickTable.setDebug(true);
        this.refresh();
        this.addActor(table);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        table.draw(batch, parentAlpha);
    }
}