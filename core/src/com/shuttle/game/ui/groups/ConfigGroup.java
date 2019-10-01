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
import com.shuttle.game.effects.BoomBox;
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
    private Table joystickTable;

    private final Label titleLabel;

    private BitmapFont font64;
    private BitmapFont font96;
    private BitmapFont font256;

    private final ScrollPane scrollPane;

    private Label.LabelStyle labelStyle48;
    private Label.LabelStyle labelStyle64;
    private Label.LabelStyle noticelStyle64;
    private Label.LabelStyle labelStyle96;
    private CheckBox.CheckBoxStyle checkBoxStyle48;
    private Slider.SliderStyle sliderStyle;
    private Label levelLabelDynamic;
    private Label effectsLabelDynamic;
    private final Skin skin;
    private Array<Music> musicArray;
    private BoomBox boomBox;
    private Label noticeLabel;
    private static StringBuilder notice = new StringBuilder();
    private static float shadowTime;
    private GlyphLayout glyphLayout;
    private float a;
    private float touchTime;
    private boolean settingsTouched;
    private String touchedSettingsMessage;
    private String holdSettingsMessage;
    private String disableSettingsMessage;
    private String enableSettingsMessage;

    private Slider soundSlider;
    private Slider effectsSlider;
    private Slider musicSlider;
    private Slider joyStickModeSlider;

    private Line soundLine;
    private Line musicLine;
    private Line effectsLine;
    private Line shadersLine;
    private Line childLine;
    private Line levelLine;
    private Line vibrationLine;
    private Line joystickLine;
    private Line tmpLine;

    private CheckBox soundCheckBox;
    private CheckBox musicCheckBox;
    private CheckBox effectsCheckBox;
    private CheckBox shadersCheckBox;
    private CheckBox childCheckBox;
    private CheckBox levelCheckBox;
    private CheckBox vibrationCheckBox;
    private CheckBox joyStickCheckBox;

    private CheckBox joyStickColorCheckBox;
    private CheckBox joyStickPresetCheckBox;

    public static ConfigGroup getInstance() {
        if (instance == null) {
            instance = new ConfigGroup();
        }
        return instance;
    }

    public void setNotice(String text) {
        notice.setLength(0);
        notice.append(text);
        shadowTime = Rules.NOTICE_WAIT;
    }

    public void setNoticeAndAlpha(String text) {
        setNotice(text);
        a = 1.0f;
    }

    public void dispose() {
        instance = null;
    }

    private class Line extends Group {
        @Getter
        @Setter
        private boolean active;
        private Table table;
        private CheckBox checkBox;
        private float a;

        private Line(CheckBox checkBox, Actor label, int align, Actor slider) {
            this.table = new Table();
            this.checkBox = checkBox;
            table.row().padBottom(22);
            //todo: to PaintCost
            table.add(checkBox).width(44).padRight(44).padLeft(44);
            table.add(label).expandX().align(align);

            if (slider != null) {
                table.add(slider).width(250).padRight(22).padLeft(22);
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
            } else {
                a = 0.5f;
            }
            super.draw(batch, a);
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            checkBox.setChecked(active);
            levelCheckBox.setChecked(active);
        }
    }

    public void refresh() {
        //todo тут поправить все дефолты. А также перехуярить Prefs
        soundSlider.setValue(GamePreferences.getSoundVolume());
        effectsSlider.setValue(GamePreferences.getEffectsVolume());
        musicSlider.setValue(GamePreferences.getMusicVolume());
        //todo:// add Level read from config
        joyStickModeSlider.setValue(GamePreferences.getJoyStickMode());

        soundLine.setActive(GamePreferences.isSoundEnabled());
        effectsLine.setActive(GamePreferences.isEffectsEnabled());
        musicLine.setActive(GamePreferences.isMusicEnabled());
        shadersLine.setActive(GamePreferences.isShaderEnabled());
        childLine.setActive(GamePreferences.isChildEnabled());

        vibrationLine.setActive(GamePreferences.isVibrationEnabled());
        joystickLine.setActive(GamePreferences.isJoyStickEnabled());
        joyStickModeSlider.setValue(GamePreferences.getJoyStickMode());
        joyStickColorCheckBox.setChecked(GamePreferences.isJoyStickColorEnabled());
    }

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
        this.noticeLabel = new Label(notice, noticelStyle64);

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
        Joystick joystick = new Joystick();
        joystick.setWidth(PaintConstants.JOYSTICK_WIDTH + PaintConstants.JOYSTICK_STICK_WIDTH);
        joystick.setHeight(PaintConstants.JOYSTICK_HEIGHT);
        ScreenManager.ScreenType screenType = ScreenManager.getInstance().getCurrentScreen();

        titleLabel = new Label("SETTINGS", labelStyle96);
        Label space = new Label("", labelStyle96);
        Label soundLabel = new Label("SOUND", labelStyle64);
        Label musicLabel = new Label("MUSIC", labelStyle64);
        Label effectsLabel = new Label("EFFECTS", labelStyle64);
        Label shadersLabel = new Label("SHADERS", labelStyle64);
        Label childLabel = new Label("CHILD MODE", labelStyle64);
        Label levelLabel = new Label("LEVEL", labelStyle64);
        Label vibrationLabel = new Label("VIBRATION", labelStyle64);
        Label joyStickLabel = new Label("JOYSTICK", labelStyle64);

        this.soundLine = new Line(soundCheckBox, soundLabel, Align.left, soundSlider);
        this.musicLine = new Line(musicCheckBox, musicLabel, Align.left, musicSlider);
        this.effectsLine = new Line(effectsCheckBox, effectsLabel, Align.left, effectsSlider);
        this.shadersLine = new Line(shadersCheckBox, shadersLabel, Align.left, null);
        this.childLine = new Line(childCheckBox, childLabel, Align.left, null);
        this.levelLine = new Line(levelCheckBox, levelLabel, Align.left, null);
        this.vibrationLine = new Line(vibrationCheckBox, vibrationLabel, Align.left, null);
        this.joystickLine = new Line(joyStickCheckBox, joyStickLabel, Align.left, null);

        Table innerTable = new Table();
        Table joystickTable = new Table();
        joystickTable.add(joystick)
                .height(PaintConstants.JOYSTICK_HEIGHT);

        innerTable.add(joyStickModeSlider).width(250).height(100);
        innerTable.row();
        innerTable.add(joyStickColorCheckBox).left();

        Line joystickPresetLine = new Line(joyStickPresetCheckBox, joystickTable, Align.center, innerTable);
        joystickPresetLine.setHeight(PaintConstants.JOYSTICK_HEIGHT);

        verticalGroup.addActor(soundLine);
        verticalGroup.addActor(musicLine);
        verticalGroup.addActor(effectsLine);
        verticalGroup.addActor(shadersLine);
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

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        noticeLabel.setText(notice);
        noticeLabel.setPosition(Rules.WORLD_WIDTH / 2 - glyphLayout.width / 2,
                this.getY() + this.getHeight() - 3 * titleLabel.getHeight() / 2);
        noticeLabel.draw(batch, a);
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
            setNoticeAndAlpha(touchedSettingsMessage);
            touchTime += delta;
        }

        if (touchTime >= Rules.SETTINGS_WAIT_TOUCH) {
            setNoticeAndAlpha(holdSettingsMessage);
        }

        if (touchTime >= Rules.SETTINGS_WAIT_DISABLE) {
            setNoticeAndAlpha(disableSettingsMessage);
            tmpLine.setActive(!tmpLine.isActive());
            settingsTouched = false;
            touchTime = 0.0f;
        }

        glyphLayout.setText(font64, notice);
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

        noticelStyle64 = new Label.LabelStyle(
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

        effectsSlider = new Slider(0.0f, 1.0f, 0.1f, false, sliderStyle);
        effectsSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GamePreferences.setEffectsVolume(effectsSlider.getValue());
                if (effectsSlider.isDragging()) {
                    boomBox.playSound(MSConstants.UI_MENU);
                }
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

//        levelLabelDynamic = new Label(Rules.StartLevel.values()[GamePreferences.getLevel()].toString(), labelStyle48);
//        effectsLabelDynamic = new Label(GamePreferences.EffectLevel.values()[GamePreferences.isShaderEnabled()].toString(), labelStyle48);

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
        //todo: затестить мультитач. Надо чтобы несколько пальцев не мгли одновременно нажимать на опции
        soundCheckBox.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    settingsTouched = true;
                    scrollPane.setFlickScroll(false);
                    touchedSettingsMessage = PaintConstants.UI_SOUND_NOTICE;
                    holdSettingsMessage = PaintConstants.UI_SOUND_HOLD;
                    tmpLine = soundLine;
                    if (GamePreferences.isSoundEnabled()) {
                        disableSettingsMessage = PaintConstants.UI_SOUND_DISABLE;
                    } else {
                        enableSettingsMessage = PaintConstants.UI_SOUND_ENABLE;
                    }
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
        musicCheckBox.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    settingsTouched = true;
                    scrollPane.setFlickScroll(false);
                    touchedSettingsMessage = PaintConstants.UI_MUSIC_NOTICE;
                    holdSettingsMessage = PaintConstants.UI_MUSIC_HOLD;
                    tmpLine = musicLine;
                    if (GamePreferences.isSoundEnabled()) {
                        disableSettingsMessage = PaintConstants.UI_MUSIC_DISABLE;
                    } else {
                        enableSettingsMessage = PaintConstants.UI_MUSIC_ENABLE;
                    }
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
        effectsCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            }
        });

        effectsCheckBox.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    settingsTouched = true;
                    scrollPane.setFlickScroll(false);
                    touchedSettingsMessage = PaintConstants.UI_EFFECTS_NOTICE;
                    holdSettingsMessage = PaintConstants.UI_EFFECTS_HOLD;
                    tmpLine = effectsLine;
                    if (GamePreferences.isSoundEnabled()) {
                        disableSettingsMessage = PaintConstants.UI_EFFECTS_DISABLE;
                    } else {
                        enableSettingsMessage = PaintConstants.UI_EFFECTS_ENABLE;
                    }
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

        shadersCheckBox = new CheckBox("", checkBoxStyle48);
        shadersCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            }
        });

        shadersCheckBox.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    settingsTouched = true;
                    scrollPane.setFlickScroll(false);
                    touchedSettingsMessage = PaintConstants.UI_SHADERS_NOTICE;
                    holdSettingsMessage = PaintConstants.UI_SHADERS_HOLD;
                    tmpLine = shadersLine;
                    if (GamePreferences.isSoundEnabled()) {
                        disableSettingsMessage = PaintConstants.UI_EFFECTS_DISABLE;
                    } else {
                        enableSettingsMessage = PaintConstants.UI_EFFECTS_ENABLE;
                    }
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

        childCheckBox.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    settingsTouched = true;
                    scrollPane.setFlickScroll(false);
                    touchedSettingsMessage = PaintConstants.UI_CHILD_NOTICE;
                    holdSettingsMessage = PaintConstants.UI_CHILD_HOLD;
                    tmpLine = childLine;
                    if (GamePreferences.isChildEnabled()) {
                        disableSettingsMessage = PaintConstants.UI_CHILD_DISABLE;
                    } else {
                        enableSettingsMessage = PaintConstants.UI_CHILD_ENABLE;
                    }
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
        levelCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            }
        });

        levelCheckBox.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    settingsTouched = true;
                    scrollPane.setFlickScroll(false);
                    touchedSettingsMessage = PaintConstants.UI_LEVEL_NOTICE;
                    holdSettingsMessage = PaintConstants.UI_LEVEL_HOLD;
                    disableSettingsMessage = PaintConstants.UI_LEVEL_DISABLE;
                    enableSettingsMessage = PaintConstants.UI_LEVEL_ENABLE;
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

        vibrationCheckBox.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    settingsTouched = true;
                    scrollPane.setFlickScroll(false);
                    touchedSettingsMessage = PaintConstants.UI_VIBRATION_NOTICE;
                    holdSettingsMessage = PaintConstants.UI_VIBRATION_HOLD;
                    tmpLine = vibrationLine;
                    if (GamePreferences.isVibrationEnabled()) {
                        disableSettingsMessage = PaintConstants.UI_VIBRATION_DISABLE;
                    } else {
                        enableSettingsMessage = PaintConstants.UI_VIBRATION_ENABLE;
                    }
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

        joyStickCheckBox.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    settingsTouched = true;
                    scrollPane.setFlickScroll(false);
                    touchedSettingsMessage = PaintConstants.UI_JOYSTICK_NOTICE;
                    holdSettingsMessage = PaintConstants.UI_JOYSTICK_HOLD;
                    tmpLine = joystickLine;
                    if (GamePreferences.isJoyStickEnabled()) {
                        disableSettingsMessage = PaintConstants.UI_JOYSTICK_DISABLE;
                    } else {
                        enableSettingsMessage = PaintConstants.UI_JOYSTICK_ENABLE;
                    }
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

        joyStickPresetCheckBox.addListener(new ClickListener() {
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

        joyStickColorCheckBox = new CheckBox("COLOR", checkBoxStyle48);
        joyStickColorCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
//                boolean enabled = joyStickColorCheckBox.isChecked();
//                joyStickColorCheckBox.setText(enabled ? cyan : white);
//                GamePreferences.setJoyStickColorEnabled(enabled);
//                if (joyStickColorCheckBox.isPressed()) {
//                    boomBox.playSound(MSConstants.UI_CHECKBOX);
//                }
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
}