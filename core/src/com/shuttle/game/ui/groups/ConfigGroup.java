package com.shuttle.game.ui.groups;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.ui.Joystick;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 6/23/18.
 */
public class ConfigGroup extends VerticalGroup {
    private static ConfigGroup instance;

    private Label.LabelStyle labelStyle48;
    private Label.LabelStyle labelStyle64;
    private Label.LabelStyle labelStyle96;
    private CheckBox.CheckBoxStyle checkBoxStyle48;
    private Slider.SliderStyle sliderStyle;
    private Label levelLabelDynamic;
    private Label effectsLabelDynamic;
    private final Skin skin;
    private Array<Music> musicArray;
    private BoomBox boomBox;

    private Table joystickTable;
    private Slider musicSlider;
    private Slider soundSlider;
    private Slider levelSlider;
    private Slider effectsSlider;
    private Slider joyStickModeSlider;
    private CheckBox musicCheckBox;
    private CheckBox soundCheckBox;
    private CheckBox joyStickCheckBox;
    private CheckBox joyStickColorCheckBox;
    private CheckBox childCheckBox;
    private CheckBox vibrationCheckBox;
    private final String cyan = "CYAN";
    private final String white = "WHITE";
    private CheckBox effectsCheckBox;
    private CheckBox shadersCheckBox;
    private CheckBox levelCheckBox;
    private CheckBox joyStickPresetCheckBox;

    public static ConfigGroup getInstance() {
        if (instance == null) {
            instance = new ConfigGroup();
        }
        return instance;
    }

    public void dispose() {
        instance = null;
    }

    private class Line extends Group {
        private Table table;
//        private Actor label;
//        private Actor icon;
//        private Actor slider;

        public Line(Actor icon, Actor label, Actor slider) {
            this.table = new Table();
//            this.label = label;
//            this.icon = icon;
//            this.slider = slider;
            table.row().padBottom(22).expandX();
            table.add(icon);
            table.add(label);
            if (slider != null) {
                table.add(slider);
            }
            table.align(Align.bottomLeft);
            table.setFillParent(true);
            this.setWidth(Rules.WORLD_WIDTH / 2);
            this.setHeight(90);
            this.addActor(table);
        }
    }

    public void refresh() {
        //todo тут поправить все дефолты. А также перехуярить Prefs
        soundSlider.setValue(GamePreferences.getSoundVolume());
        musicSlider.setValue(GamePreferences.getMusicVolume());
        //levelSlider.setValue(GamePreferences.getLevel());
        effectsSlider.setValue(GamePreferences.getPrefEffectsLevel());
        joyStickModeSlider.setValue(GamePreferences.getJoyStickMode());
        //musicCheckBox.setText(GamePreferences.isMusicEnabled() ? on : off);
        //musicCheckBox.setChecked(GamePreferences.isMusicEnabled());
        //soundCheckBox.setText(GamePreferences.isSoundEnabled() ? on : off);
        soundCheckBox.setChecked(GamePreferences.isSoundEnabled());
        //joyStickCheckBox.setText(GamePreferences.isJoyStickEnabled() ? on : off);
        joyStickCheckBox.setChecked(GamePreferences.isJoyStickEnabled());
        //joystickTable.setVisible(joyStickCheckBox.isChecked());
        joyStickColorCheckBox.setText(GamePreferences.isJoyStickColorEnabled() ? cyan : white);
        joyStickColorCheckBox.setChecked(GamePreferences.isJoyStickColorEnabled());
        //childCheckBox.setText(GamePreferences.isChildEnabled() ? on : off);
        childCheckBox.setChecked(GamePreferences.isChildEnabled());
        //vibrationCheckBox.setText(GamePreferences.isVibrationEnabled() ? on : off);
        vibrationCheckBox.setChecked(GamePreferences.isVibrationEnabled());
    }

    private ConfigGroup() {
        this.setName(GroupType.CONFIG.name());
        this.setVisible(false);
        this.skin = Assets.getInstance().getAssetManager().get(PaintConstants.SKIN_FILE);
        this.setPosition(Rules.WORLD_WIDTH / 8, Rules.WORLD_HEIGHT / 4);
        this.setWidth(3 * Rules.WORLD_WIDTH / 4);
        this.setHeight(Rules.WORLD_HEIGHT / 4);
        this.boomBox = new BoomBox();

        VerticalGroup verticalGroup = new VerticalGroup();
        ScrollPane scrollPane = new ScrollPane(verticalGroup, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        //scrollPane.setVariableSizeKnobs(false);
        Container<ScrollPane> container = new Container<ScrollPane>(scrollPane);
        container.setPosition(Rules.WORLD_WIDTH / 8, Rules.WORLD_HEIGHT / 2);
        container.width(3 * Rules.WORLD_WIDTH / 4);
        container.height(Rules.WORLD_HEIGHT / 4);
        container.align(Align.top);
        Joystick joystick = new Joystick();
        ScreenManager.ScreenType screenType = ScreenManager.getInstance().getCurrentScreen();

        initStyles();
        initSliders();
        initCheckBoxes();


//        Table table = new Table();
//        table.setPosition(
//                Rules.WORLD_WIDTH / 2,
//                Rules.WORLD_HEIGHT / 2
//        );
//
//        joystickTable = new Table();
//        final Table innerTable = new Table();
//
        Label titleLabel = new Label("Preferences", labelStyle96);
        Label soundLabel = new Label("SOUND", labelStyle64);
        Label musicLabel = new Label("MUSIC", labelStyle64);
        Label effectsLabel = new Label("EFFECTS", labelStyle64);
        Label shadersLabel = new Label("SHADERS", labelStyle64);
        Label childLabel = new Label("CHILD MODE", labelStyle64);
        Label levelLabel = new Label("LEVEL", labelStyle64);
        Label vibrationLabel = new Label("VIBRATION", labelStyle64);
        Label joyStickLabel = new Label("JOYSTICK", labelStyle64);

        Line soundLine = new Line(soundCheckBox, soundLabel, soundSlider);
        Line musicLine = new Line(musicCheckBox, musicLabel, musicSlider);
        Line effectsLine = new Line(effectsCheckBox, effectsLabel, effectsSlider);
        Line shadersLine = new Line(shadersCheckBox, shadersLabel, null);
        Line childLine = new Line(childCheckBox, childLabel, null);
        Line levelLine = new Line(levelCheckBox, levelLabel, null);
        Line vibrationLine = new Line(vibrationCheckBox, vibrationLabel, null);
        Line joystickLine = new Line(joyStickCheckBox, joyStickLabel, null);
        Table innerTable = new Table();
        Table joystickTable = new Table();
        Table table = new Table();
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

        Line p = new Line(joyStickPresetCheckBox, table, null);
//        Label effectsLabel = new Label("Effects ", labelStyle64);
//
//        table.add(titleLabel).colspan(3);
//        table.row();
//        table.add(volumeMusicLabel).left();
//        table.add().padRight(10).width(300);
//        table.add(musicSlider).left().fillX().width(200);
//        table.row();
//        table.add(musicOnOffLabel).left();
//        table.add();
//        table.add(musicCheckBox).left();
//        table.row();
//        table.add(volumeSoundLabel).left();
//        table.add();
//        table.add(soundSlider).left().fillX();
//        table.row();
//        table.add(soundOnOffLabel).left();
//        table.add();
//        table.add(soundCheckBox).left();
//
//        switch (screenType) {
//            case MENU:
//                table.row();
//                table.add(childLabel).left();
//                table.add();
//                table.add(childCheckBox).left();
//
//                table.row();
//                table.add(levelLabel).left();
//                table.add(levelSlider).padRight(10).width(300);
//                table.add(levelLabelDynamic).left().width(200);
//
//                table.row();
//                table.add(effectsLabel).left();
//                table.add(effectsSlider).padRight(10).width(300);
//                table.add(effectsLabelDynamic).left().width(200);
//                break;
//        }
//
//        table.row();
//        table.add(vibrationLabel).left();
//        table.add();
//        table.add(vibrationCheckBox).left();
//        table.row();
//        table.add(joyStickOnOffLabel).left();
//        table.add();
//        table.add(joyStickCheckBox).left();
//        table.row();
//        table.add(joystickTable).colspan(3);
//
//        joystickTable.row();
//        joystickTable.add(innerTable);
//        joystickTable.add(joystick)
//                .width(PaintConstants.JOYSTICK_WIDTH)
//                .height(PaintConstants.JOYSTICK_HEIGHT)
//                .align(Align.center);
//        joystickTable.add();
//
//        innerTable.row().width(200).padRight(45);
//        innerTable.add(joyStickModeSlider);
//        innerTable.row();
//        innerTable.add(joyStickColorCheckBox).left();
//
//        table.setDebug(true);
//        innerTable.setDebug(true);
//        joystickTable.setDebug(true);
        //this.refresh();
        //verticalGroup.addActor(table);
        verticalGroup.addActor(soundLine);
        verticalGroup.addActor(musicLine);
        verticalGroup.addActor(effectsLine);
        verticalGroup.addActor(shadersLine);
        verticalGroup.addActor(childLine);
        verticalGroup.addActor(levelLine);
        verticalGroup.addActor(vibrationLine);
        verticalGroup.addActor(joystickLine);
        verticalGroup.addActor(p);
        this.addActor(titleLabel);
        this.addActor(container);
        //this.addActor(table);
    }

    private void initStyles() {
        checkBoxStyle48 = skin.get("default", CheckBox.CheckBoxStyle.class);
        checkBoxStyle48.checkboxOn.setMinHeight(50);
        checkBoxStyle48.checkboxOn.setMinWidth(50);
        checkBoxStyle48.checkboxOff.setMinWidth(50);
        checkBoxStyle48.checkboxOff.setMinHeight(50);

        labelStyle48 = new Label.LabelStyle(
                Assets.getInstance().getAssetManager().get(PaintConstants.FONT48, BitmapFont.class),
                new Color(0.8f, 0.43f, 0.33f, 1f)
        );

        labelStyle64 = new Label.LabelStyle(
                Assets.getInstance().getAssetManager().get(PaintConstants.FONT64, BitmapFont.class),
                new Color(0.8f, 0.43f, 0.33f, 1f)
        );

        labelStyle96 = new Label.LabelStyle(
                Assets.getInstance().getAssetManager().get(PaintConstants.FONT96, BitmapFont.class),
                new Color(0.8f, 0.43f, 0.33f, 1f)
        );
        /////////

        sliderStyle = new Slider.SliderStyle(skin.get("default-horizontal", Slider.SliderStyle.class));
        sliderStyle.knob.setMinHeight(50);
        //sliderStyle.knobOver.setMinHeight(50);
        sliderStyle.knob.setMinWidth(50);
        //sliderStyle.knobOver.setMinWidth(50);
    }

    private void initSliders() {
        soundSlider = new Slider(0.0f, 1.0f, 0.1f, false, sliderStyle);
        soundSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                GamePreferences.setSoundVolume(soundSlider.getValue());
//                if (soundSlider.isDragging()) {
//                    boomBox.playSound(MSConstants.UI_MENU);
//                }
            }
        });

        musicSlider = new Slider(0.0f, 1.0f, 0.1f, false, sliderStyle);
        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                musicArray = new Array<Music>();
//                Assets.getInstance().getAssetManager().getAll(Music.class, musicArray);
//                for (int i = 0; i < musicArray.size; i++) {
//                    musicArray.get(i).setVolume(musicSlider.getValue());
//                }
//                GamePreferences.setMusicVolume(musicSlider.getValue());
//                if (musicSlider.isDragging()) {
//                    boomBox.playSound(MSConstants.UI_MENU);
//                }
            }
        });

        effectsSlider = new Slider(0.0f, GamePreferences.EffectLevel.values().length - 1, 1.0f, false, sliderStyle);
        effectsSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                GamePreferences.setPrefEffectsLevel((int) effectsSlider.getValue());
//                effectsLabelDynamic.setText(GamePreferences.EffectLevel.values()[(int) effectsSlider.getValue()].toString());
//                if (effectsSlider.isDragging()) {
//                    boomBox.playSound(MSConstants.UI_MENU);
//                }
            }
        });

//        levelLabelDynamic = new Label(Rules.StartLevel.values()[GamePreferences.getLevel()].toString(), labelStyle48);
//        effectsLabelDynamic = new Label(GamePreferences.EffectLevel.values()[GamePreferences.getPrefEffectsLevel()].toString(), labelStyle48);

//        levelSlider = new Slider(0.0f, Rules.StartLevel.values().length - 1, 1.0f, false, sliderStyle);
//        levelSlider.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
////                GamePreferences.setLevel((int) levelSlider.getValue());
////                levelLabelDynamic.setText(Rules.StartLevel.values()[(int) levelSlider.getValue()].toString());
////                if (levelSlider.isDragging()) {
////                    boomBox.playSound(MSConstants.UI_MENU);
////                }
//            }
//        });

        joyStickModeSlider = new Slider(0, 4, 1, false, sliderStyle);
        joyStickModeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                GamePreferences.setJoyStickMode((int) joyStickModeSlider.getValue());
//                if (joyStickModeSlider.isDragging()) {
//                    boomBox.playSound(MSConstants.UI_MENU);
//                }
            }
        });
    }

    private void initCheckBoxes() {
        soundCheckBox = new CheckBox("", checkBoxStyle48);
        soundCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                GamePreferences.setSoundEnabled(soundCheckBox.isChecked());
//                if (soundCheckBox.isPressed()) {
//                    boomBox.playSound(MSConstants.UI_CHECKBOX);
//                }
            }
        });

        musicCheckBox = new CheckBox("", checkBoxStyle48);
        musicCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                GamePreferences.setMusicEnabled(musicCheckBox.isChecked());
//                boomBox.setAllMusicVolume(GamePreferences.getMusicVolume());
//                boomBox.stopAllMusicAndPlayThese(MSConstants.MENU_SONG);
//                if (musicCheckBox.isPressed()) {
//                    boomBox.playSound(MSConstants.UI_CHECKBOX);
//                }
            }
        });

        effectsCheckBox = new CheckBox("", checkBoxStyle48);
        effectsCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            }
        });

        shadersCheckBox = new CheckBox("", checkBoxStyle48);
        shadersCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            }
        });

        childCheckBox = new CheckBox("", checkBoxStyle48);
        childCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                boolean enabled = childCheckBox.isChecked();
//                childCheckBox.setText(enabled ? on : off);
//                GamePreferences.setChildEnabled(enabled);
//                if (childCheckBox.isPressed()) {
//                    boomBox.playSound(MSConstants.UI_CHECKBOX);
//                }
            }
        });

        levelCheckBox = new CheckBox("", checkBoxStyle48);
        levelCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            }
        });

        vibrationCheckBox = new CheckBox("", checkBoxStyle48);
        vibrationCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                boolean enabled = vibrationCheckBox.isChecked();
//                vibrationCheckBox.setText(enabled ? on : off);
//                GamePreferences.setVibrationEnabled(enabled);
//                if (enabled) {
//                    Vibrator.vibrate(VConstants.VIBRATION_ENABLED);
//                }
//                if (vibrationCheckBox.isPressed()) {
//                    boomBox.playSound(MSConstants.UI_CHECKBOX);
//                }
            }
        });

        joyStickCheckBox = new CheckBox("", checkBoxStyle48);
        joyStickCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                boolean enabled = joyStickCheckBox.isChecked();
//                joyStickCheckBox.setText(enabled ? on : off);
//                GamePreferences.setJoyStickEnabled(enabled);
//                joystickTable.setVisible(enabled);
//                if (joyStickCheckBox.isPressed()) {
//                    boomBox.playSound(MSConstants.UI_CHECKBOX);
//                }
            }
        });

        joyStickPresetCheckBox = new CheckBox("", checkBoxStyle48);
        joyStickPresetCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                boolean enabled = joyStickCheckBox.isChecked();
//                joyStickCheckBox.setText(enabled ? on : off);
//                GamePreferences.setJoyStickEnabled(enabled);
//                joystickTable.setVisible(enabled);
//                if (joyStickCheckBox.isPressed()) {
//                    boomBox.playSound(MSConstants.UI_CHECKBOX);
//                }
            }
        });

        joyStickColorCheckBox = new CheckBox("", checkBoxStyle48);
        joyStickColorCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                boolean enabled = joyStickColorCheckBox.isChecked();
//                joyStickColorCheckBox.setText(enabled ? cyan : white);
//                GamePreferences.setJoyStickColor(enabled);
//                if (joyStickColorCheckBox.isPressed()) {
//                    boomBox.playSound(MSConstants.UI_CHECKBOX);
//                }
            }
        });
    }
}