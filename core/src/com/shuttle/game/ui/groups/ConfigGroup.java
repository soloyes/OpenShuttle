package com.shuttle.game.ui.groups;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 6/23/18.
 */
public class ConfigGroup extends VerticalGroup {
    private static ConfigGroup instance;
    private static StringBuilder notice = new StringBuilder();
    private static float shadowTime;
    private final ScrollPane scrollPane;
    private final Skin skin;
    private Table joystickTable;
    private BitmapFont font64;
    private BitmapFont font96;
    private BitmapFont font256;
    private Label.LabelStyle labelStyle48;
    private Label.LabelStyle labelStyle64;
    private Label.LabelStyle noticeStyle64;
    private Label.LabelStyle labelStyle96;
    private CheckBox.CheckBoxStyle checkBoxStyle48;
    private Slider.SliderStyle sliderStyle;
    private Array<Music> musicArray;
    private BoomBox boomBox;
    private Label noticeLabel;
    private GlyphLayout glyphLayout;
    private float a;
    private float touchTime;
    private boolean settingsTouched;
    private Slider soundSlider;
    private Slider effectsSlider;
    private Slider musicSlider;
    private Slider joyStickModeSlider;
    private Line soundLine;
    private Line musicLine;
    private Line effectsLine;
    private Line shaderLine;
    private Line childLine;
    private Line levelLine;
    private Line vibrationLine;
    private Line joystickLine;
    private Line joystickPresetLine;
    private Line tmpLine;
    private CheckBox soundCheckBox;
    private CheckBox musicCheckBox;
    private CheckBox effectsCheckBox;
    private CheckBox shaderCheckBox;
    private CheckBox childCheckBox;
    private CheckBox levelCheckBox;
    private CheckBox vibrationCheckBox;
    private CheckBox joyStickCheckBox;
    private CheckBox joyStickColorCheckBox;
    private CheckBox joyStickPresetCheckBox;
    private int level;
    private Label levelLabel;
    private Label titleLabel;
    private Label space;
    private Table innerTable;

    private ConfigGroup() {
        this.setName(GroupType.CONFIG.name());
        this.setVisible(false);
        this.font64 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT64, BitmapFont.class);
        this.font96 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT96, BitmapFont.class);
        this.font256 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT256, BitmapFont.class);
        this.skin = Assets.getInstance().getAssetManager().get(PaintConstants.SKIN_FILE);
        this.setPosition(Rules.WORLD_WIDTH / 8, Rules.WORLD_HEIGHT / 4);
        this.setWidth(3 * Rules.WORLD_WIDTH / 4);
        this.setHeight(Rules.WORLD_HEIGHT / 2);
        this.boomBox = new BoomBox();
        this.glyphLayout = new GlyphLayout();
        glyphLayout.setText(font64, notice);

        initStyles();
        initSliders();
        initCheckBoxes();

        Joystick joystick = new Joystick();
        joystick.setWidth(PaintConstants.JOYSTICK_WIDTH + PaintConstants.JOYSTICK_STICK_WIDTH);
        joystick.setHeight(PaintConstants.JOYSTICK_HEIGHT);
        innerTable = new Table();
        joystickTable = new Table();
        joystickTable.add(joystick)
                .height(PaintConstants.JOYSTICK_HEIGHT);

        innerTable.add(joyStickModeSlider).width(250).height(100);
        innerTable.row();
        innerTable.add(joyStickColorCheckBox).left();

        initLines();

        this.noticeLabel = new Label(notice, noticeStyle64);

        VerticalGroup verticalGroup = new VerticalGroup();
        scrollPane = new ScrollPane(verticalGroup, skin);
        scrollPane.setScrollbarsOnTop(false);
        scrollPane.setForceScroll(false, true);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setVariableSizeKnobs(false);
        Container<ScrollPane> container = new Container<ScrollPane>(scrollPane);
        container.setHeight(Rules.WORLD_HEIGHT / 2);
        container.align(Align.top);

        ScreenManager.ScreenType screenType = ScreenManager.getInstance().getCurrentScreen();

        verticalGroup.addActor(soundLine);
        verticalGroup.addActor(musicLine);
        verticalGroup.addActor(effectsLine);
        verticalGroup.addActor(shaderLine);
        verticalGroup.addActor(childLine);
        verticalGroup.addActor(levelLine);
        verticalGroup.addActor(vibrationLine);
        verticalGroup.addActor(joystickLine);
        verticalGroup.addActor(joystickPresetLine);
        this.addActor(titleLabel);
        this.addActor(space);
        this.addActor(container);
        this.refresh();
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

        noticeStyle64 = new Label.LabelStyle(
                Assets.getInstance().getAssetManager().get(PaintConstants.FONT64, BitmapFont.class),
                new Color(1.0f, 1.0f, 1.0f, 1f)
        );

        labelStyle96 = new Label.LabelStyle();
        labelStyle96.font = font96;
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
                GamePreferences.setSoundVolume(soundSlider.getValue());
                musicArray = new Array<Music>();
                Assets.getInstance().getAssetManager().getAll(Music.class, musicArray);
                for (int i = 0; i < musicArray.size; i++) {
                    musicArray.get(i).setVolume(GamePreferences.getOverallMusicVolume());
                }
                if (soundSlider.isDragging()) {
                    boomBox.playSound(MSConstants.UI_MENU);
                }
                soundLine.setMuted(soundSlider.getValue() == 0.0f);
            }
        });
        soundSlider.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                scrollPane.setFlickScroll(false);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                scrollPane.setFlickScroll(true);
                super.touchUp(event, x, y, pointer, button);
            }
        });

        musicSlider = new Slider(0.0f, 1.0f, 0.1f, false, sliderStyle);
        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GamePreferences.setMusicVolume(musicSlider.getValue());
                musicArray = new Array<Music>();
                Assets.getInstance().getAssetManager().getAll(Music.class, musicArray);
                for (int i = 0; i < musicArray.size; i++) {
                    musicArray.get(i).setVolume(GamePreferences.getOverallMusicVolume());
                }
                if (musicSlider.isDragging()) {
                    boomBox.playSound(MSConstants.UI_MENU);
                }
                musicLine.setMuted(musicSlider.getValue() == 0.0f);
            }
        });
        musicSlider.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                scrollPane.setFlickScroll(false);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                scrollPane.setFlickScroll(true);
                super.touchUp(event, x, y, pointer, button);
            }
        });

        effectsSlider = new Slider(0.0f, 1.0f, 0.1f, false, sliderStyle);
        effectsSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GamePreferences.setEffectsVolume(effectsSlider.getValue());
                if (effectsSlider.isDragging()) {
                    boomBox.playSound(MSConstants.UI_MENU);
                }
                effectsLine.setMuted(effectsSlider.getValue() == 0.0f);
            }
        });
        effectsSlider.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                scrollPane.setFlickScroll(false);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                scrollPane.setFlickScroll(true);
                super.touchUp(event, x, y, pointer, button);
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
        joyStickModeSlider.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                scrollPane.setFlickScroll(false);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                scrollPane.setFlickScroll(true);
                super.touchUp(event, x, y, pointer, button);
            }
        });
    }

    //todo: затестить мультитач. Надо чтобы несколько пальцев не мгли одновременно нажимать на опции
    private void initCheckBoxes() {
        soundCheckBox = new CheckBox("", checkBoxStyle48);
        soundCheckBox.setDisabled(true);
        soundCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            }
        });
        soundCheckBox.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    settingsTouched = true;
                    scrollPane.setFlickScroll(false);
                    tmpLine = soundLine;
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    settingsTouched = false;
                    touchTime = 0.0f;
                    scrollPane.setFlickScroll(true);
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });

        musicCheckBox = new CheckBox("", checkBoxStyle48);
        musicCheckBox.setDisabled(true);

        musicCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean enabled = musicCheckBox.isChecked();
                GamePreferences.setMusicEnabled(enabled);
                boomBox.setAllMusicVolume(GamePreferences.getMusicVolume());
                boomBox.stopAllMusicAndPlayThese(MSConstants.MENU_SONG);
            }
        });

        musicCheckBox.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    settingsTouched = true;
                    scrollPane.setFlickScroll(false);
                    tmpLine = musicLine;
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    settingsTouched = false;
                    touchTime = 0.0f;
                    scrollPane.setFlickScroll(true);
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });

        effectsCheckBox = new CheckBox("", checkBoxStyle48);
        effectsCheckBox.setDisabled(true);
        effectsCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean enabled = effectsCheckBox.isChecked();
                GamePreferences.setEffectsEnabled(enabled);
            }
        });

        effectsCheckBox.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    settingsTouched = true;
                    scrollPane.setFlickScroll(false);
                    tmpLine = effectsLine;
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    settingsTouched = false;
                    touchTime = 0.0f;
                    scrollPane.setFlickScroll(true);
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });

        shaderCheckBox = new CheckBox("", checkBoxStyle48);
        shaderCheckBox.setDisabled(true);
        shaderCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean enabled = shaderCheckBox.isChecked();
                GamePreferences.setShaderEnabled(enabled);
            }
        });

        shaderCheckBox.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    settingsTouched = true;
                    scrollPane.setFlickScroll(false);
                    tmpLine = shaderLine;
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    settingsTouched = false;
                    touchTime = 0.0f;
                    scrollPane.setFlickScroll(true);
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });

        childCheckBox = new CheckBox("", checkBoxStyle48);
        childCheckBox.setDisabled(true);
        childCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean enabled = childCheckBox.isChecked();
                GamePreferences.setChildEnabled(enabled);
            }
        });

        childCheckBox.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    settingsTouched = true;
                    scrollPane.setFlickScroll(false);
                    tmpLine = childLine;
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    settingsTouched = false;
                    touchTime = 0.0f;
                    scrollPane.setFlickScroll(true);
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });

        levelCheckBox = new CheckBox("", checkBoxStyle48);
        levelCheckBox.setDisabled(true);

        levelCheckBox.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    settingsTouched = true;
                    scrollPane.setFlickScroll(false);
                    tmpLine = levelLine;
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    settingsTouched = false;
                    touchTime = 0.0f;
                    scrollPane.setFlickScroll(true);
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });

        vibrationCheckBox = new CheckBox("", checkBoxStyle48);
        vibrationCheckBox.setDisabled(true);
        vibrationCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean enabled = vibrationCheckBox.isChecked();
                GamePreferences.setVibrationEnabled(enabled);
                if (enabled) {
                    Vibrator.vibrate(VConstants.VIBRATION_ENABLED);
                }
            }
        });

        vibrationCheckBox.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    settingsTouched = true;
                    scrollPane.setFlickScroll(false);
                    tmpLine = vibrationLine;
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    settingsTouched = false;
                    touchTime = 0.0f;
                    scrollPane.setFlickScroll(true);
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });

        joyStickCheckBox = new CheckBox("", checkBoxStyle48);
        joyStickCheckBox.setDisabled(true);
        joyStickCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean enabled = joyStickCheckBox.isChecked();
                GamePreferences.setJoyStickEnabled(enabled);
            }
        });
        joyStickCheckBox.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    settingsTouched = true;
                    scrollPane.setFlickScroll(false);
                    tmpLine = joystickLine;
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    settingsTouched = false;
                    touchTime = 0.0f;
                    scrollPane.setFlickScroll(true);
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });

        joyStickPresetCheckBox = new CheckBox("", checkBoxStyle48);
        joyStickPresetCheckBox.setDisabled(true);

        joyStickColorCheckBox = new CheckBox("COLOR", checkBoxStyle48);
        joyStickColorCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean enabled = joyStickColorCheckBox.isChecked();
                GamePreferences.setJoyStickColorEnabled(enabled);
                if (joyStickColorCheckBox.isPressed()) {
                    boomBox.playSound(MSConstants.UI_CHECKBOX);
                }
            }
        });

        joyStickColorCheckBox.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                scrollPane.setFlickScroll(false);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                scrollPane.setFlickScroll(true);
                super.touchUp(event, x, y, pointer, button);
            }
        });
    }

    private Label initLines() {
        titleLabel = new Label("SETTINGS", labelStyle96);
        space = new Label("", labelStyle96);
        Label soundLabel = new Label("SOUND", labelStyle64);
        Label musicLabel = new Label("MUSIC", labelStyle64);
        Label effectsLabel = new Label("EFFECTS", labelStyle64);
        Label shaderLabel = new Label("SHADERS", labelStyle64);
        Label childLabel = new Label("CHILD MODE", labelStyle64);
        levelLabel = new Label("LEVEL", labelStyle64);
        Label vibrationLabel = new Label("VIBRATION", labelStyle64);
        Label joyStickLabel = new Label("JOYSTICK", labelStyle64);

        soundLine = new Line(soundCheckBox, soundLabel, Align.left, soundSlider);
        soundLine.setNoticeMessage(PaintConstants.UI_SOUND_NOTICE);
        soundLine.setHoldMessage(PaintConstants.UI_SOUND_HOLD);
        soundLine.setEnabledMessage(PaintConstants.UI_SOUND_ENABLE);
        soundLine.setDisabledMessage(PaintConstants.UI_SOUND_DISABLE);
        musicLine = new Line(musicCheckBox, musicLabel, Align.left, musicSlider);
        musicLine.setNoticeMessage(PaintConstants.UI_MUSIC_NOTICE);
        musicLine.setHoldMessage(PaintConstants.UI_MUSIC_HOLD);
        musicLine.setEnabledMessage(PaintConstants.UI_MUSIC_ENABLE);
        musicLine.setDisabledMessage(PaintConstants.UI_MUSIC_DISABLE);
        effectsLine = new Line(effectsCheckBox, effectsLabel, Align.left, effectsSlider);
        effectsLine.setNoticeMessage(PaintConstants.UI_EFFECTS_NOTICE);
        effectsLine.setHoldMessage(PaintConstants.UI_EFFECTS_HOLD);
        effectsLine.setEnabledMessage(PaintConstants.UI_EFFECTS_ENABLE);
        effectsLine.setDisabledMessage(PaintConstants.UI_EFFECTS_DISABLE);
        shaderLine = new Line(shaderCheckBox, shaderLabel, Align.left, null);
        shaderLine.setNoticeMessage(PaintConstants.UI_SHADERS_NOTICE);
        shaderLine.setHoldMessage(PaintConstants.UI_SHADERS_HOLD);
        shaderLine.setEnabledMessage(PaintConstants.UI_SHADERS_ENABLE);
        shaderLine.setDisabledMessage(PaintConstants.UI_SHADERS_DISABLE);
        childLine = new Line(childCheckBox, childLabel, Align.left, null);
        childLine.setNoticeMessage(PaintConstants.UI_CHILD_NOTICE);
        childLine.setHoldMessage(PaintConstants.UI_CHILD_HOLD);
        childLine.setEnabledMessage(PaintConstants.UI_CHILD_ENABLE);
        childLine.setDisabledMessage(PaintConstants.UI_CHILD_DISABLE);
        levelLine = new Line(levelCheckBox, levelLabel, Align.left, null);
        levelLine.setActive(true);
        levelLine.setNoticeMessage(PaintConstants.UI_LEVEL_NOTICE);
        levelLine.setHoldMessage(PaintConstants.UI_LEVEL_HOLD);
        levelLine.setEnabledMessage(PaintConstants.UI_LEVEL_ENABLE);
        levelLine.setDisabledMessage(PaintConstants.UI_LEVEL_DISABLE);
        vibrationLine = new Line(vibrationCheckBox, vibrationLabel, Align.left, null);
        vibrationLine.setNoticeMessage(PaintConstants.UI_VIBRATION_NOTICE);
        vibrationLine.setHoldMessage(PaintConstants.UI_VIBRATION_HOLD);
        vibrationLine.setEnabledMessage(PaintConstants.UI_VIBRATION_ENABLE);
        vibrationLine.setDisabledMessage(PaintConstants.UI_VIBRATION_DISABLE);
        joystickLine = new Line(joyStickCheckBox, joyStickLabel, Align.left, null);
        joystickLine.setNoticeMessage(PaintConstants.UI_JOYSTICK_NOTICE);
        joystickLine.setHoldMessage(PaintConstants.UI_JOYSTICK_HOLD);
        joystickLine.setEnabledMessage(PaintConstants.UI_JOYSTICK_ENABLE);
        joystickLine.setDisabledMessage(PaintConstants.UI_JOYSTICK_DISABLE);

        joystickPresetLine = new Line(joyStickPresetCheckBox, joystickTable, Align.center, innerTable);
        joystickPresetLine.setHeight(PaintConstants.JOYSTICK_HEIGHT);

        return space;
    }

    public void refresh() {
        soundLine.setActive(GamePreferences.isEffectsEnabled() || GamePreferences.isMusicEnabled());
        soundSlider.setValue(GamePreferences.getSoundVolume());
        refreshSoundLine();
        musicLine.setActive(GamePreferences.isMusicEnabled());
        musicSlider.setValue(GamePreferences.getMusicVolume());
        refreshMusicLine();
        effectsLine.setActive(GamePreferences.isEffectsEnabled());
        effectsSlider.setValue(GamePreferences.getEffectsVolume());
        refreshEffectsLine();
        shaderLine.setActive(GamePreferences.isShaderEnabled());
        refreshShaderLine();
        childLine.setActive(GamePreferences.isChildEnabled());
        refreshChildLine();
        level = GamePreferences.getLevel();
        refreshLevelLine();
        vibrationLine.setActive(GamePreferences.isVibrationEnabled());
        refreshVibrationLine();
        joystickLine.setActive(GamePreferences.isJoyStickEnabled());
        refreshJoystickPresetLine();
        joyStickModeSlider.setValue(GamePreferences.getJoyStickMode());
        joyStickColorCheckBox.setChecked(GamePreferences.isJoyStickColorEnabled());
    }

    private void refreshSoundLine() {
        soundCheckBox.setChecked(soundLine.isActive());

        musicLine.setActive(soundLine.isActive());
        musicLine.setSlave(!soundLine.isActive());
        musicCheckBox.setChecked(soundLine.isActive());
        effectsLine.setActive(soundLine.isActive());
        effectsLine.setSlave(!soundLine.isActive());
        effectsCheckBox.setChecked(soundLine.isActive());

        soundSlider.setDisabled(!soundLine.isActive());
        musicSlider.setDisabled(!soundLine.isActive());
        effectsSlider.setDisabled(!soundLine.isActive());
        soundLine.setMuted(soundSlider.getValue() == 0.0f);
    }

    private void refreshMusicLine() {
        musicSlider.setDisabled(!musicLine.isActive());
        musicCheckBox.setChecked(musicLine.isActive());
        musicLine.setMuted(musicSlider.getValue() == 0.0f);

    }

    private void refreshEffectsLine() {
        effectsSlider.setDisabled(!effectsLine.isActive());
        effectsCheckBox.setChecked(effectsLine.isActive());
        effectsLine.setMuted(effectsSlider.getValue() == 0.0f);
    }

    private void refreshShaderLine() {
        shaderCheckBox.setChecked(shaderLine.isActive());
    }

    private void refreshChildLine() {
        childCheckBox.setChecked(childLine.isActive());
    }

    private void refreshLevelLine() {
        levelLine.setActive(true);
        levelCheckBox.setChecked(true);
        level = level % (Rules.StartLevel.values().length);
        levelLabel.setText("LEVEL " + level + " (" + Rules.StartLevel.values()[level].name() + ")");
        GamePreferences.setLevel(level);
    }

    private void refreshVibrationLine() {
        vibrationCheckBox.setChecked(vibrationLine.isActive());
    }

    private void refreshJoystickPresetLine() {
        joystickPresetLine.setActive(joystickLine.isActive());
        joystickPresetLine.setSlave(!joystickLine.isActive());
        joyStickModeSlider.setDisabled(!joystickLine.isActive());
        joyStickColorCheckBox.setDisabled(!joystickLine.isActive());
        joyStickCheckBox.setChecked(joystickPresetLine.isActive());
        joyStickPresetCheckBox.setChecked(joystickPresetLine.isActive());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        shadowTime -= delta;
        if (shadowTime <= 0.0f) {
            shadowTime = 0.0f;
            a = 0.0f;
        }

        if (settingsTouched) {
            setNoticeAndAlpha(tmpLine.getNoticeMessage());
            touchTime += delta;
        }

        if (touchTime >= Rules.SETTINGS_WAIT_TOUCH) {
            setNoticeAndAlpha(tmpLine.getHoldMessage());
        }

        if (touchTime >= Rules.SETTINGS_WAIT_DISABLE) {
            boomBox.playSound(MSConstants.UI_CHECKBOX);
            if (tmpLine.isActive()) {
                setNoticeAndAlpha(tmpLine.getDisabledMessage());
            } else {
                setNoticeAndAlpha(tmpLine.getEnabledMessage());
            }

            tmpLine.setActive(!tmpLine.isActive());
            settingsTouched = false;
            touchTime = 0.0f;

            if (tmpLine == soundLine) {
                refreshSoundLine();
            } else if (tmpLine == musicLine) {
                refreshMusicLine();
            } else if (tmpLine == effectsLine) {
                refreshEffectsLine();
            } else if (tmpLine == shaderLine) {
                refreshShaderLine();
            } else if (tmpLine == childLine) {
                refreshChildLine();
            } else if (tmpLine == levelLine) {
                level++;
                refreshLevelLine();
            } else if (tmpLine == vibrationLine) {
                refreshVibrationLine();
            } else if (tmpLine == joystickLine) {
                refreshJoystickPresetLine();
            }

            if (!effectsLine.isActive() && !musicLine.isActive()) {
                soundLine.setActive(false);
                musicLine.setSlave(true);
                effectsLine.setSlave(true);
                soundCheckBox.setChecked(false);
                soundSlider.setDisabled(true);
            } else if (effectsLine.isActive() || musicLine.isActive()) {
                soundLine.setActive(true);
                musicLine.setSlave(false);
                effectsLine.setSlave(false);
                soundCheckBox.setChecked(true);
                soundSlider.setDisabled(false);
            }
        }
        glyphLayout.setText(font64, notice);
    }

    public void setNoticeAndAlpha(String text) {
        setNotice(text);
        a = 1.0f;
    }

    public void setNotice(String text) {
        notice.setLength(0);
        notice.append(text);
        shadowTime = Rules.NOTICE_WAIT;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        noticeLabel.setText(notice);
        noticeLabel.setPosition(Rules.WORLD_WIDTH / 2 - glyphLayout.width / 2,
                this.getY() + this.getHeight() - 3 * titleLabel.getHeight() / 2);
        noticeLabel.draw(batch, a);
    }

    public void dispose() {
        instance = null;
    }

    public static ConfigGroup getInstance() {
        if (instance == null) {
            instance = new ConfigGroup();
        }
        return instance;
    }

    private class Line extends Group {
        @Getter
        @Setter
        private String noticeMessage;
        @Getter
        @Setter
        private String holdMessage;
        @Getter
        @Setter
        private String disabledMessage;
        @Getter
        @Setter
        private String enabledMessage;
        @Getter
        @Setter
        private boolean active;
        @Getter
        @Setter
        private boolean muted;
        private Table table;
        private float a;
        @Getter
        @Setter
        private boolean slave;

        private Line(CheckBox checkBox, Actor label, int align, Actor actor) {
            this.table = new Table();
            table.row().padBottom(22);
            //todo: to PaintCost
            table.add(checkBox).width(44).padRight(44).padLeft(44);
            table.add(label).expandX().align(align);

            if (actor != null) {
                table.add(actor).width(250).padRight(22).padLeft(22);
            }
            table.align(Align.top);
            table.setFillParent(true);
            this.setWidth(3 * Rules.WORLD_WIDTH / 4);
            this.setHeight(90);
            this.addActor(table);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            //todo: to Const
            if (active) {
                a = 1.0f;
            } else if (slave) {
                a = 0.5f;
            } else {
                a = 0.75f;
            }

            float tmp = a;
            if (muted) {
                a = 0.75f;
            } else {
                a = tmp;
            }

            super.draw(batch, a);
        }
    }
}