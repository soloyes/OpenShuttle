package com.shuttle.game.ui.groups.configs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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
import com.shuttle.game.ui.groups.GroupType;
import com.shuttle.game.utils.Assets;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 6/23/18.
 */

public class ConfigGroup extends Group {
    private static ConfigGroup instance;
    private static StringBuilder notice = new StringBuilder();
    private Skin skin;
    private ScreenManager.ScreenType screenType;
    private ScrollPane scrollPane;
    private Table innerTable;
    private Table joystickTable;
    private BitmapFont font44;
    private BitmapFont font96;
    private Label.LabelStyle labelStyle54;
    private Label.LabelStyle noticeStyle44;
    private Label.LabelStyle labelStyle96;
    private SoundConfigCheckBoxStyle soundCheckBoxStyle;
    private ConfigCheckBoxStyle musicCheckBoxStyle;
    private ConfigCheckBoxStyle effectsCheckBoxStyle;
    private ConfigCheckBoxStyle shadersCheckBoxStyle;
    private ConfigCheckBoxStyle childCheckBoxStyle;
    private LevelConfigCheckBoxStyle levelCheckBoxStyle;
    private ConfigCheckBoxStyle vibrationCheckBoxStyle;
    private ConfigCheckBoxStyle joystickCheckBoxStyle;
    private ConfigCheckBoxStyle joystickPresetCheckBoxStyle;

    private Slider.SliderStyle sliderStyle;
    private int level;
    private float a;
    private float touchTime;
    private float shadowTime;
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
    private ConfigCheckBox soundCheckBox;
    private ConfigCheckBox musicCheckBox;
    private ConfigCheckBox effectsCheckBox;
    private ConfigCheckBox shadersCheckBox;
    private ConfigCheckBox childCheckBox;
    private ConfigCheckBox levelCheckBox;
    private ConfigCheckBox vibrationCheckBox;
    private ConfigCheckBox joyStickCheckBox;
    private CheckBox joyStickColorCheckBox;
    private ConfigCheckBox joyStickPresetCheckBox;
    private Label noticeLabel;
    private Label levelLabel;
    private Group noticeGroup;
    private GlyphLayout glyphLayout;
    private GlyphLayout glyphLayoutTitle;
    private Array<Music> musicArray;
    private BoomBox boomBox;
    private Label shaderLabel;
    private Label childLabel;

    public static ConfigGroup getInstance() {
        if (instance == null) {
            instance = new ConfigGroup();
        }
        return instance;
    }

    private ConfigGroup() {
        this.setName(GroupType.CONFIG.name());
        this.setVisible(false);
        this.font44 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT44, BitmapFont.class);
        this.font96 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT96, BitmapFont.class);
        this.skin = Assets.getInstance().getAssetManager().get(PaintConstants.SKIN_FILE);
        this.setWidth(Rules.WORLD_WIDTH);
        this.setHeight(Rules.WORLD_HEIGHT);
        this.boomBox = new BoomBox();

        initStyles();
        initSliders();
        initCheckBoxes();

        Joystick joystick = new Joystick();
        joystick.setWidth(PaintConstants.JOYSTICK_WIDTH + PaintConstants.JOYSTICK_STICK_WIDTH);
        joystick.setHeight(PaintConstants.JOYSTICK_HEIGHT);
        innerTable = new Table();
        joystickTable = new Table();
        joystickTable.setHeight(PaintConstants.JOYSTICK_HEIGHT);
        joystickTable.add(joystick)
                .height(PaintConstants.JOYSTICK_HEIGHT);

        innerTable.add(joyStickModeSlider).width(204).height(PaintConstants.JOYSTICK_HEIGHT / 2);
        innerTable.row();
        innerTable.add(joyStickColorCheckBox).height(PaintConstants.JOYSTICK_HEIGHT / 2).left();

        this.glyphLayoutTitle = new GlyphLayout();
        initLines();
        this.glyphLayout = new GlyphLayout();

        this.noticeLabel = new Label(notice, noticeStyle44);
        this.noticeGroup = new Group();
        noticeGroup.addActor(noticeLabel);
        noticeLabel.setFillParent(true);

        VerticalGroup verticalGroup = new VerticalGroup();
        scrollPane = new ScrollPane(verticalGroup, skin);
        scrollPane.setScrollbarsOnTop(false);
        scrollPane.setForceScroll(false, true);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        //scrollPane.setVariableSizeKnobs(false);
        //scrollPane.getStyle().vScrollKnob.setMinHeight(100);
        Container<ScrollPane> container = new Container<ScrollPane>(scrollPane);
        container.setPosition(PaintConstants.UI_SETTINGS_X, PaintConstants.UI_SETTINGS_Y);
        container.width(PaintConstants.UI_SETTINGS_WIDTH);
        container.height(PaintConstants.UI_SETTINGS_HEIGHT);
        container.align(Align.bottomLeft);

        screenType = ScreenManager.getInstance().getCurrentScreen();

        verticalGroup.addActor(soundLine);
        verticalGroup.addActor(musicLine);
        verticalGroup.addActor(effectsLine);
        verticalGroup.addActor(shaderLine);
        verticalGroup.addActor(childLine);
        verticalGroup.addActor(levelLine);
        verticalGroup.addActor(vibrationLine);
        verticalGroup.addActor(joystickLine);
        verticalGroup.addActor(joystickPresetLine);
        this.addActor(noticeGroup);
        this.addActor(container);
        this.refresh();
    }

    private void initStyles() {
        soundCheckBoxStyle = new SoundConfigCheckBoxStyle(
                new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_SOUND1)),
                new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_SOUND1))
        );

        musicCheckBoxStyle = new ConfigCheckBoxStyle(
                new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_MUSIC)),
                new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_MUSIC))
        );

        effectsCheckBoxStyle = new ConfigCheckBoxStyle(
                new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_EFFECTS)),
                new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_EFFECTS))
        );

        shadersCheckBoxStyle = new ConfigCheckBoxStyle(
                new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_SHADERS)),
                new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_SHADERS))
        );

        childCheckBoxStyle = new ConfigCheckBoxStyle(
                new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_CHILD)),
                new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_CHILD))
        );

        vibrationCheckBoxStyle = new ConfigCheckBoxStyle(
                new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_VIBRATION)),
                new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_VIBRATION))
        );

        levelCheckBoxStyle = new LevelConfigCheckBoxStyle(
                new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_LEVEL1)),
                new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_LEVEL1))
        );

        joystickCheckBoxStyle = new ConfigCheckBoxStyle(
                new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_JOYSTICK)),
                new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_JOYSTICK))
        );

        joystickPresetCheckBoxStyle = new ConfigCheckBoxStyle(
                new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_JOYSTICK_OFF)),
                new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_JOYSTICK_ON))
        );

        labelStyle54 = new Label.LabelStyle(
                Assets.getInstance().getAssetManager().get(PaintConstants.FONT54, BitmapFont.class),
                new Color(0.8f, 0.43f, 0.33f, 1f)
        );


        noticeStyle44 = new Label.LabelStyle(
                Assets.getInstance().getAssetManager().get(PaintConstants.FONT44, BitmapFont.class),
                new Color(1.0f, 1.0f, 1.0f, 1f)
        );

        labelStyle96 = new Label.LabelStyle();
        labelStyle96.font = font96;
        /////////

        sliderStyle = new Slider.SliderStyle(skin.get("default-horizontal", Slider.SliderStyle.class));
        sliderStyle.knob.setMinHeight(36);
        //sliderStyle.knobOver.setMinHeight(50);
        sliderStyle.knob.setMinWidth(36);
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
                    Vibrator.vibrate(VConstants.UI_CLICK);
                }
                soundLine.setMuted(soundSlider.getValue() == 0.0f);
                soundCheckBoxStyle.setFront(soundSlider.getPercent());
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
                    Vibrator.vibrate(VConstants.UI_CLICK);
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
                    Vibrator.vibrate(VConstants.UI_CLICK);
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
                    Vibrator.vibrate(VConstants.UI_CLICK);
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

    private void initCheckBoxes() {
        soundCheckBox = new ConfigCheckBox("", soundCheckBoxStyle);
        soundCheckBox.setDisabled(true);
        soundCheckBox.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ConfigGroup.this.touchDown(pointer, soundLine);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ConfigGroup.this.touchUp(pointer);
                super.touchUp(event, x, y, pointer, button);
            }
        });
        soundCheckBoxStyle.setFront(soundSlider.getPercent());

        musicCheckBox = new ConfigCheckBox("", musicCheckBoxStyle);
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
                ConfigGroup.this.touchDown(pointer, musicLine);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ConfigGroup.this.touchUp(pointer);
                super.touchUp(event, x, y, pointer, button);
            }
        });

        effectsCheckBox = new ConfigCheckBox("", effectsCheckBoxStyle);
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
                ConfigGroup.this.touchDown(pointer, effectsLine);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ConfigGroup.this.touchUp(pointer);
                super.touchUp(event, x, y, pointer, button);
            }
        });

        shadersCheckBox = new ConfigCheckBox("", shadersCheckBoxStyle);
        shadersCheckBox.setDisabled(true);
        shadersCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean enabled = shadersCheckBox.isChecked();
                GamePreferences.setShaderEnabled(enabled);
            }
        });
        shadersCheckBox.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (screenType == ScreenManager.ScreenType.MENU) {
                    ConfigGroup.this.touchDown(pointer, shaderLine);
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (screenType == ScreenManager.ScreenType.MENU) {
                    ConfigGroup.this.touchUp(pointer);
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });

        childCheckBox = new ConfigCheckBox("", childCheckBoxStyle);
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
                if (screenType == ScreenManager.ScreenType.MENU) {
                    ConfigGroup.this.touchDown(pointer, childLine);
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (screenType == ScreenManager.ScreenType.MENU) {
                    ConfigGroup.this.touchUp(pointer);
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });

        levelCheckBox = new ConfigCheckBox("", levelCheckBoxStyle);
        levelCheckBox.setDisabled(true);
        levelCheckBox.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (screenType == ScreenManager.ScreenType.MENU) {
                    ConfigGroup.this.touchDown(pointer, levelLine);
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (screenType == ScreenManager.ScreenType.MENU) {
                    ConfigGroup.this.touchUp(pointer);
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });

        vibrationCheckBox = new ConfigCheckBox("", vibrationCheckBoxStyle);
        vibrationCheckBox.setDisabled(true);
        vibrationCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean enabled = vibrationCheckBox.isChecked();
                GamePreferences.setVibrationEnabled(enabled);
                if (enabled && Gdx.input.isTouched() && isVisible()) {
                    Vibrator.vibrate(VConstants.VIBRATION_ENABLED);
                }
            }
        });
        vibrationCheckBox.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ConfigGroup.this.touchDown(pointer, vibrationLine);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ConfigGroup.this.touchUp(pointer);
                super.touchUp(event, x, y, pointer, button);
            }
        });

        joyStickCheckBox = new ConfigCheckBox("", joystickCheckBoxStyle);
        joyStickCheckBox.setDisabled(true);
        joyStickCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean enabled = joyStickCheckBox.isChecked();
                GamePreferences.setJoyStickEnabled(enabled);
                if (joyStickCheckBox.isPressed()) {
                    boomBox.playSound(MSConstants.UI_CHANGED);
                    Vibrator.vibrate(VConstants.UI_CHANGED);
                }
            }
        });
        joyStickCheckBox.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ConfigGroup.this.touchDown(pointer, joystickLine);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ConfigGroup.this.touchUp(pointer);
                super.touchUp(event, x, y, pointer, button);
            }
        });

        joyStickPresetCheckBox = new ConfigCheckBox("", joystickCheckBoxStyle);
        joyStickPresetCheckBox.setDisabled(true);

        joyStickColorCheckBox = new CheckBox(" COLOR", joystickPresetCheckBoxStyle);

        joyStickColorCheckBox.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                scrollPane.setFlickScroll(false);
                if (pointer == 0 && joystickPresetLine.isActive()) {
                    boomBox.playSound(MSConstants.UI_CHANGED);
                    Vibrator.vibrate(VConstants.UI_CHANGED);
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                scrollPane.setFlickScroll(true);
                if (pointer == 0 && joystickPresetLine.isActive()) {
                    boolean enabled = joyStickColorCheckBox.isChecked();
                    GamePreferences.setJoyStickColorEnabled(enabled);
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });
    }

    private void initLines() {
        glyphLayoutTitle.setText(font96, "SETTINGS");

        Label soundLabel = new Label("SOUND", labelStyle54);
        soundLabel.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ConfigGroup.this.touchDown(pointer, soundLine);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ConfigGroup.this.touchUp(pointer);
                super.touchUp(event, x, y, pointer, button);
            }
        });

        Label musicLabel = new Label("MUSIC", labelStyle54);
        musicLabel.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ConfigGroup.this.touchDown(pointer, musicLine);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ConfigGroup.this.touchUp(pointer);
                super.touchUp(event, x, y, pointer, button);
            }
        });

        Label effectsLabel = new Label("EFFECTS", labelStyle54);
        effectsLabel.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ConfigGroup.this.touchDown(pointer, effectsLine);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ConfigGroup.this.touchUp(pointer);
                super.touchUp(event, x, y, pointer, button);
            }
        });

        shaderLabel = new Label("SHADERS", labelStyle54);
        shaderLabel.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (screenType == ScreenManager.ScreenType.MENU) {
                    ConfigGroup.this.touchDown(pointer, shaderLine);
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (screenType == ScreenManager.ScreenType.MENU) {
                    ConfigGroup.this.touchUp(pointer);
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });

        childLabel = new Label("CHILD MODE", labelStyle54);
        childLabel.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (screenType == ScreenManager.ScreenType.MENU) {
                    ConfigGroup.this.touchDown(pointer, childLine);
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (screenType == ScreenManager.ScreenType.MENU) {
                    ConfigGroup.this.touchUp(pointer);
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });

        levelLabel = new Label("LEVEL", labelStyle54);
        levelLabel.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (screenType == ScreenManager.ScreenType.MENU) {
                    ConfigGroup.this.touchDown(pointer, levelLine);
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (screenType == ScreenManager.ScreenType.MENU) {
                    ConfigGroup.this.touchUp(pointer);
                }
                super.touchUp(event, x, y, pointer, button);
            }
        });

        Label vibrationLabel = new Label("VIBRATION", labelStyle54);
        vibrationLabel.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ConfigGroup.this.touchDown(pointer, vibrationLine);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ConfigGroup.this.touchUp(pointer);
                super.touchUp(event, x, y, pointer, button);
            }
        });

        Label joyStickLabel = new Label("JOYSTICK", labelStyle54);
        joyStickLabel.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ConfigGroup.this.touchDown(pointer, joystickLine);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ConfigGroup.this.touchUp(pointer);
                super.touchUp(event, x, y, pointer, button);
            }
        });

        Group soundGroup = makeGroup(soundLabel, 273, 82);
        soundLine = new Line(
                soundCheckBox,
                soundGroup,
                soundSlider,
                new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_CONFIG_CELL_TYPE1))
        );
        soundLine.setNoticeMessage(PaintConstants.UI_SOUND_NOTICE);
        soundLine.setHoldMessage(PaintConstants.UI_SOUND_HOLD);
        soundLine.setEnabledMessage(PaintConstants.UI_SOUND_ENABLE);
        soundLine.setDisabledMessage(PaintConstants.UI_SOUND_DISABLE);
        Group musicGroup = makeGroup(musicLabel, 273, 82);
        musicLine = new Line(
                musicCheckBox,
                musicGroup,
                musicSlider,
                new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_CONFIG_CELL_TYPE1))
        );
        musicLine.setNoticeMessage(PaintConstants.UI_MUSIC_NOTICE);
        musicLine.setHoldMessage(PaintConstants.UI_MUSIC_HOLD);
        musicLine.setEnabledMessage(PaintConstants.UI_MUSIC_ENABLE);
        musicLine.setDisabledMessage(PaintConstants.UI_MUSIC_DISABLE);
        Group effectsGroup = makeGroup(effectsLabel, 273, 82);
        effectsLine = new Line(
                effectsCheckBox,
                effectsGroup,
                effectsSlider,
                new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_CONFIG_CELL_TYPE1))
        );
        effectsLine.setNoticeMessage(PaintConstants.UI_EFFECTS_NOTICE);
        effectsLine.setHoldMessage(PaintConstants.UI_EFFECTS_HOLD);
        effectsLine.setEnabledMessage(PaintConstants.UI_EFFECTS_ENABLE);
        effectsLine.setDisabledMessage(PaintConstants.UI_EFFECTS_DISABLE);
        Group shaderGroup = makeGroup(shaderLabel, 535, 82);
        shaderLine = new Line(
                shadersCheckBox,
                shaderGroup,
                null,
                new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_CONFIG_CELL_TYPE2))
        );
        shaderLine.setNoticeMessage(PaintConstants.UI_SHADERS_NOTICE);
        shaderLine.setHoldMessage(PaintConstants.UI_SHADERS_HOLD);
        shaderLine.setEnabledMessage(PaintConstants.UI_SHADERS_ENABLE);
        shaderLine.setDisabledMessage(PaintConstants.UI_SHADERS_DISABLE);
        Group childGroup = makeGroup(childLabel, 535, 82);
        childLine = new Line(
                childCheckBox,
                childGroup,
                null,
                new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_CONFIG_CELL_TYPE2))
        );
        childLine.setNoticeMessage(PaintConstants.UI_CHILD_NOTICE);
        childLine.setHoldMessage(PaintConstants.UI_CHILD_HOLD);
        childLine.setEnabledMessage(PaintConstants.UI_CHILD_ENABLE);
        childLine.setDisabledMessage(PaintConstants.UI_CHILD_DISABLE);
        Group levelGroup = makeGroup(levelLabel, 535, 82);
        levelLine = new Line(
                levelCheckBox,
                levelGroup,
                null,
                new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_CONFIG_CELL_TYPE2))
        );
        levelLine.setActive(true);
        levelLine.setNoticeMessage(PaintConstants.UI_LEVEL_NOTICE);
        levelLine.setHoldMessage(PaintConstants.UI_LEVEL_HOLD);
        levelLine.setEnabledMessage(PaintConstants.UI_LEVEL_ENABLE);
        levelLine.setDisabledMessage(PaintConstants.UI_LEVEL_DISABLE);
        Group vibrationGroup = makeGroup(vibrationLabel, 535, 82);
        vibrationLine = new Line(
                vibrationCheckBox,
                vibrationGroup,
                null,
                new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_CONFIG_CELL_TYPE2))
        );
        vibrationLine.setNoticeMessage(PaintConstants.UI_VIBRATION_NOTICE);
        vibrationLine.setHoldMessage(PaintConstants.UI_VIBRATION_HOLD);
        vibrationLine.setEnabledMessage(PaintConstants.UI_VIBRATION_ENABLE);
        vibrationLine.setDisabledMessage(PaintConstants.UI_VIBRATION_DISABLE);
        Group joystickGroup = makeGroup(joyStickLabel, 535, 82);
        joystickLine = new Line(
                joyStickCheckBox,
                joystickGroup,
                null,
                new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_CONFIG_CELL_TYPE2))
        );
        joystickLine.setNoticeMessage(PaintConstants.UI_JOYSTICK_NOTICE);
        joystickLine.setHoldMessage(PaintConstants.UI_JOYSTICK_HOLD);
        joystickLine.setEnabledMessage(PaintConstants.UI_JOYSTICK_ENABLE);
        joystickLine.setDisabledMessage(PaintConstants.UI_JOYSTICK_DISABLE);

        joystickPresetLine = new Line(
                joyStickPresetCheckBox,
                joystickTable,
                innerTable,
                new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_CONFIG_CELL_TYPE3))
        );
    }

    public void refresh() {
        musicLine.setActive(GamePreferences.isMusicEnabled());
        musicSlider.setValue(GamePreferences.getMusicVolume());
        refreshMusicLine();
        effectsLine.setActive(GamePreferences.isEffectsEnabled());
        effectsSlider.setValue(GamePreferences.getEffectsVolume());
        refreshEffectsLine();
        soundSlider.setValue(GamePreferences.getSoundVolume());
        refreshSoundLine();
        shaderLine.setActive(GamePreferences.isShaderEnabled() && screenType == ScreenManager.ScreenType.MENU);
        refreshShaderLine();
        childLine.setActive(GamePreferences.isChildEnabled() && screenType == ScreenManager.ScreenType.MENU);
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

    private void touchDown(int pointer, Line line) {
        if (pointer == 0) {
            settingsTouched = true;
            scrollPane.setFlickScroll(false);
            tmpLine = line;
            boomBox.playSound(MSConstants.UI_CLICK);
            Vibrator.vibrate(VConstants.UI_CLICK);
        }
    }

    private void touchUp(int pointer) {
        if (pointer == 0) {
            settingsTouched = false;
            touchTime = 0.0f;
            scrollPane.setFlickScroll(true);
            tmpLine.setTouchTime(touchTime);
        }
    }

    private Group makeGroup(Widget widget, int width, int height) {
        widget.setFillParent(true);
        Group group = new Group();
        group.addActor(widget);
        group.setSize(width, height);
        return group;
    }

    private void refreshSoundLine() {
        //soundSlider.setDisabled(!soundLine.isActive());
        soundLine.setActive(GamePreferences.isEffectsEnabled() || GamePreferences.isMusicEnabled());
        soundCheckBox.setChecked(soundLine.isActive());
        if (GamePreferences.isEffectsEnabled() || GamePreferences.isMusicEnabled()) {
            soundLine.setMuted(false);
        } else {
            soundLine.setMuted(true);
        }
        if (soundSlider.getValue() == 0) {
            soundLine.setMuted(true);
        }
    }

    private void refreshMusicLine() {
        musicSlider.setDisabled(!musicLine.isActive());
        musicCheckBox.setChecked(musicLine.isActive());
        musicLine.setMuted(musicSlider.getValue() == 0.0f);
        if (!GamePreferences.isMusicEnabled() && !GamePreferences.isEffectsEnabled()) {
            musicLine.setSlave(true);
        }
        soundLine.setMuted(!GamePreferences.isEffectsEnabled() && !GamePreferences.isMusicEnabled());
    }

    private void refreshEffectsLine() {
        effectsSlider.setDisabled(!effectsLine.isActive());
        effectsCheckBox.setChecked(effectsLine.isActive());
        effectsLine.setMuted(effectsSlider.getValue() == 0.0f);
        if (!GamePreferences.isMusicEnabled() && !GamePreferences.isEffectsEnabled()) {
            effectsLine.setSlave(true);
        }
        soundLine.setMuted(!GamePreferences.isEffectsEnabled() && !GamePreferences.isMusicEnabled());
    }

    private void refreshShaderLine() {
        if (screenType == ScreenManager.ScreenType.MENU) {
            shadersCheckBox.setChecked(shaderLine.isActive());
            shadersCheckBox.setLocked(false);
        } else {
            shadersCheckBox.setChecked(GamePreferences.isShaderEnabled());
            shaderLine.setSlave(true);
            shadersCheckBox.setLocked(true);
            shaderLabel.setText("SHADERS " + (GamePreferences.isShaderEnabled() ? "(ON)" : "(OFF)"));
        }
    }

    private void refreshChildLine() {
        if (screenType == ScreenManager.ScreenType.MENU) {
            childCheckBox.setChecked(childLine.isActive());
            childCheckBox.setLocked(false);
        } else {
            childCheckBox.setChecked(GamePreferences.isChildEnabled());
            childCheckBox.setLocked(true);
            childLine.setSlave(true);
            childLabel.setText("CHILD MODE " + (GamePreferences.isChildEnabled() ? "(ON)" : "(OFF)"));
        }
    }

    private void refreshLevelLine() {
        if (screenType == ScreenManager.ScreenType.MENU) {
            levelLine.setActive(true);
            level = level % (Rules.StartLevel.values().length);
            GamePreferences.setLevel(level);
            levelCheckBox.setLocked(false);
        } else {
            levelLine.setActive(false);
            levelLine.setSlave(true);
            levelCheckBox.setLocked(true);
        }
        levelCheckBox.setChecked(true);
        levelLabel.setText("LEVEL " + level + " (" + Rules.StartLevel.values()[level].name() + ")");
        levelCheckBoxStyle.setFront(level);
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
        noticeGroup.getColor().a = a;
        glyphLayout.setText(font44, notice);
        noticeLabel.setText(notice);
        noticeGroup.setPosition(
                (Rules.WORLD_WIDTH - glyphLayout.width) / 2,
                1313
        );

        shadowTime -= delta;
        if (shadowTime <= 0.0f) {
            shadowTime = 0.0f;
            a = 0.0f;
        }

        if (settingsTouched) {
            setNoticeAndAlpha(tmpLine.getNoticeMessage());
            touchTime += delta;
            tmpLine.setTouchTime(touchTime);
        }

        if (touchTime >= Rules.SETTINGS_WAIT_TOUCH) {
            setNoticeAndAlpha(tmpLine.getHoldMessage());
        }

        if (touchTime >= Rules.SETTINGS_WAIT_DISABLE) {
            boomBox.playSound(MSConstants.UI_CHANGED);
            Vibrator.vibrate(VConstants.UI_CHANGED);
            if (tmpLine.isActive()) {
                setNoticeAndAlpha(tmpLine.getDisabledMessage());
            } else {
                setNoticeAndAlpha(tmpLine.getEnabledMessage());
            }

            tmpLine.setActive(!tmpLine.isActive());
            settingsTouched = false;
            touchTime = 0.0f;

            if (tmpLine == soundLine) {
                refreshOverallSound();
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
            tmpLine.setTouchTime(touchTime);
        }
    }

    public void setNoticeAndAlpha(String text) {
        notice.setLength(0);
        notice.append(text);
        shadowTime = Rules.NOTICE_WAIT;
        a = 1.0f;
    }

    private void refreshOverallSound() {
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

    private static class Line extends Group {
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
        private TextureRegion textureRegion;

        private ConfigCheckBox configCheckBox;
        private Actor centerActor;

        private Line(ConfigCheckBox leftActor, Actor centerActor, Actor rightActor, TextureRegion textureRegion) {

            this.table = new Table();
            this.configCheckBox = leftActor;
            this.centerActor = centerActor;
            this.textureRegion = textureRegion;
            if (centerActor.getHeight() == PaintConstants.JOYSTICK_HEIGHT) {
                table.row().padBottom(12);
            } else {
                table.row();
            }

            table.add(leftActor).width(82).height(82);

            if (rightActor != null) {
                table.add(centerActor).width(273).padLeft(23);
                table.add(rightActor).width(204).height(82).padRight(29).padLeft(29);
            } else {
                table.add(centerActor).width(535).padLeft(23);
            }
            table.align(Align.top);
            table.setFillParent(true);
            this.setWidth(PaintConstants.UI_CONFIG_CELL_WIDTH);

            if (centerActor.getHeight() == PaintConstants.JOYSTICK_HEIGHT) {
                this.setHeight(centerActor.getHeight());
            } else {
                this.setHeight(centerActor.getHeight() + 12);
            }

            this.addActor(table);
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            if (active) {
                a = 1.0f;
            } else if (slave) {
                a = 0.5f;
            } else {
                a = 0.75f;
            }

            float tmp = a;
            if (muted && !slave) {
                a = 0.75f;
            } else {
                a = tmp;
            }
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            Color color = batch.getColor();
            color.a = a;
            batch.setColor(color);
            if (centerActor.getHeight() == PaintConstants.JOYSTICK_HEIGHT) {
                batch.draw(textureRegion, getX(), getY());
            } else {
                batch.draw(textureRegion, getX(), getY() + 12);
            }

            color.a = parentAlpha;
            batch.setColor(color);
            super.draw(batch, a);
        }

        public void setTouchTime(float touchTime) {
            configCheckBox.setTouchTime(touchTime);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        font96.draw(batch,
                glyphLayoutTitle,
                (Rules.WORLD_WIDTH - glyphLayoutTitle.width) / 2,
                PaintConstants.UI_TITLE_LABEL_Y + glyphLayoutTitle.height
        );
    }

    public void dispose() {
        instance = null;
    }

    public static void doStart() {
        instance = null;
    }
}