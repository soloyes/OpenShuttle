package com.buran.game.ui.groups.configs;

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
import com.badlogic.gdx.scenes.scene2d.Touchable;
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
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.conf.VConstants;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.effects.Vibrator;
import com.shuttle.game.player.Standing;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.ui.Joystick;
import com.shuttle.game.ui.groups.GroupType;
import com.shuttle.game.ui.groups.PauseGroup;
import com.shuttle.game.ui.groups.mvc.profiles.Profiles;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.TimeFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * @author soloyes on 6/23/18.
 */

public class ConfigGroup extends Group {
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
    private final Slider notifySlider;
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
    private GlyphLayout timeGlyphLayout;
    private Array<Music> musicArray;
    private BoomBox boomBox;
    private Label shaderLabel;
    private Label childLabel;
    private Profiles profiles;
    private Table table;

    public ConfigGroup() {
        this.setName(GroupType.CONFIG.name());
        this.profiles = new Profiles();
        this.setVisible(false);
        this.font44 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT44, BitmapFont.class);
        this.font96 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT96, BitmapFont.class);
        this.skin = Assets.getInstance().getAssetManager().get(PaintConstants.SKIN_FILE);
        this.setWidth(Rules.WORLD_WIDTH);
        this.setHeight(Rules.WORLD_HEIGHT);
        this.boomBox = new BoomBox();
        this.table = new Table();
        table.setTouchable(Touchable.enabled);

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
        this.timeGlyphLayout = new GlyphLayout();

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
        container.width(PaintConstants.UI_SETTINGS_WIDTH);
        container.height(PaintConstants.UI_SETTINGS_HEIGHT);
        container.setPosition(Rules.WORLD_WIDTH / 2 - PaintConstants.UI_SETTINGS_WIDTH / 2, 2 * Rules.WORLD_HEIGHT / 3);
        container.align(Align.topLeft);

        screenType = ScreenManager.getInstance().getCurrentScreen();

        Slider.SliderStyle sliderStyle = new Slider.SliderStyle(skin.get("default-horizontal", Slider.SliderStyle.class));
        notifySlider = new Slider(0.0f, 0.1f, 0.1f, false, sliderStyle);
        notifySlider.clearListeners();
        notifySlider.setValue(Profiles.getSettings().isNotification() ? notifySlider.getMaxValue() : notifySlider.getMinValue());
        notifySlider.getColor().a = Profiles.getSettings().isNotification() ? 1.0f : 0.7f;
        //todo: сделать мигание кнопки
        notifySlider.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setNotify();
            }
        });
        table.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setNotify();
            }
        });

        notifySlider.setPosition(Rules.WORLD_WIDTH - PaintConstants.PLAY_PAUSE_BUTTON_WIDTH / 2 - 25 - 42, Rules.WORLD_HEIGHT - 224);
        notifySlider.setWidth(50);

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
        this.addActor(notifySlider);
        this.addActor(table);
        this.refresh();
    }

    private void setNotify() {
        boomBox.playSound(MSConstants.UI_MENU);
        Vibrator.vibrate(VConstants.UI_CLICK);
        if (notifySlider.getValue() == notifySlider.getMinValue()) {
            notifySlider.setValue(notifySlider.getMaxValue());
            notifySlider.getColor().a = 1.0f;
        } else {
            notifySlider.getColor().a = 0.7f;
            notifySlider.setValue(notifySlider.getMinValue());
        }
        Profiles.getSettings().setNotification(!Profiles.getSettings().isNotification());
        profiles.write();
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
                Profiles.getSettings().setSoundVolume(soundSlider.getValue());
                musicArray = new Array<Music>();
                Assets.getInstance().getAssetManager().getAll(Music.class, musicArray);
                for (int i = 0; i < musicArray.size; i++) {
                    musicArray.get(i).setVolume(Profiles.getSettings().overallMusicVolume());
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
                Profiles.getSettings().setMusicVolume(musicSlider.getValue());
                musicArray = new Array<Music>();
                Assets.getInstance().getAssetManager().getAll(Music.class, musicArray);
                for (int i = 0; i < musicArray.size; i++) {
                    musicArray.get(i).setVolume(Profiles.getSettings().overallMusicVolume());
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
                Profiles.getSettings().setEffectsVolume(effectsSlider.getValue());
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

        joyStickModeSlider = new Slider(0, 3, 1, false, sliderStyle);
        joyStickModeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Profiles.getSettings().setJoystick_mode((int) joyStickModeSlider.getValue());
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
                Profiles.getSettings().setMusic(enabled);
                boomBox.setAllMusicVolume(Profiles.getSettings().getMusicVolume());
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
                Profiles.getSettings().setEffects(enabled);
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
                Profiles.getSettings().setShader(enabled);
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
                Profiles.getSettings().setChild(enabled);
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
                Profiles.getSettings().setVibration(enabled);
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
                Profiles.getSettings().setJoystick(enabled);
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
                    Profiles.getSettings().setJoystickColor(enabled);
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

        Group soundGroup = makeGroup(soundLabel, 273, PaintConstants.UI_CELL_HEIGHT);
        soundLine = new Line(
                soundCheckBox,
                soundGroup,
                soundSlider,
                new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_CONFIG_CELL_TYPE1))
        );
        soundLine.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (pointer == 0) {
                    profiles.write();
                    ConfigGroup.this.refresh();
                }
            }
        });
        soundLine.setNoticeMessage(PaintConstants.UI_SOUND_NOTICE);
        soundLine.setHoldMessage(PaintConstants.UI_SOUND_HOLD);
        soundLine.setEnabledMessage(PaintConstants.UI_SOUND_ENABLE);
        soundLine.setDisabledMessage(PaintConstants.UI_SOUND_DISABLE);
        Group musicGroup = makeGroup(musicLabel, 273, PaintConstants.UI_CELL_HEIGHT);
        musicLine = new Line(
                musicCheckBox,
                musicGroup,
                musicSlider,
                new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_CONFIG_CELL_TYPE1))
        );
        musicLine.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (pointer == 0) {
                    profiles.write();
                    ConfigGroup.this.refresh();
                }
            }
        });
        musicLine.setNoticeMessage(PaintConstants.UI_MUSIC_NOTICE);
        musicLine.setHoldMessage(PaintConstants.UI_MUSIC_HOLD);
        musicLine.setEnabledMessage(PaintConstants.UI_MUSIC_ENABLE);
        musicLine.setDisabledMessage(PaintConstants.UI_MUSIC_DISABLE);
        Group effectsGroup = makeGroup(effectsLabel, 273, PaintConstants.UI_CELL_HEIGHT);
        effectsLine = new Line(
                effectsCheckBox,
                effectsGroup,
                effectsSlider,
                new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_CONFIG_CELL_TYPE1))
        );
        effectsLine.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (pointer == 0) {
                    profiles.write();
                    ConfigGroup.this.refresh();
                }
            }
        });
        effectsLine.setNoticeMessage(PaintConstants.UI_EFFECTS_NOTICE);
        effectsLine.setHoldMessage(PaintConstants.UI_EFFECTS_HOLD);
        effectsLine.setEnabledMessage(PaintConstants.UI_EFFECTS_ENABLE);
        effectsLine.setDisabledMessage(PaintConstants.UI_EFFECTS_DISABLE);
        Group shaderGroup = makeGroup(shaderLabel, 535, PaintConstants.UI_CELL_HEIGHT);
        shaderLine = new Line(
                shadersCheckBox,
                shaderGroup,
                null,
                new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_CONFIG_CELL_TYPE2))
        );
        effectsLine.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (pointer == 0) {
                    profiles.write();
                    ConfigGroup.this.refresh();
                }
            }
        });
        shaderLine.setNoticeMessage(PaintConstants.UI_SHADERS_NOTICE);
        shaderLine.setHoldMessage(PaintConstants.UI_SHADERS_HOLD);
        shaderLine.setEnabledMessage(PaintConstants.UI_SHADERS_ENABLE);
        shaderLine.setDisabledMessage(PaintConstants.UI_SHADERS_DISABLE);
        Group childGroup = makeGroup(childLabel, 535, PaintConstants.UI_CELL_HEIGHT);
        childLine = new Line(
                childCheckBox,
                childGroup,
                null,
                new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_CONFIG_CELL_TYPE2))
        );
        childLine.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (pointer == 0) {
                    profiles.write();
                    ConfigGroup.this.refresh();
                }
            }
        });
        childLine.setNoticeMessage(PaintConstants.UI_CHILD_NOTICE);
        childLine.setHoldMessage(PaintConstants.UI_CHILD_HOLD);
        childLine.setEnabledMessage(PaintConstants.UI_CHILD_ENABLE);
        childLine.setDisabledMessage(PaintConstants.UI_CHILD_DISABLE);
        Group levelGroup = makeGroup(levelLabel, 535, PaintConstants.UI_CELL_HEIGHT);
        levelLine = new Line(
                levelCheckBox,
                levelGroup,
                null,
                new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_CONFIG_CELL_TYPE2))
        );
        levelLine.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (pointer == 0) {
                    profiles.write();
                    ConfigGroup.this.refresh();
                }
            }
        });
        levelLine.setActive(true);
        levelLine.setNoticeMessage(PaintConstants.UI_LEVEL_NOTICE);
        levelLine.setHoldMessage(PaintConstants.UI_LEVEL_HOLD);
        levelLine.setEnabledMessage(PaintConstants.UI_LEVEL_ENABLE);
        levelLine.setDisabledMessage(PaintConstants.UI_LEVEL_DISABLE);
        Group vibrationGroup = makeGroup(vibrationLabel, 535, PaintConstants.UI_CELL_HEIGHT);
        vibrationLine = new Line(
                vibrationCheckBox,
                vibrationGroup,
                null,
                new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_CONFIG_CELL_TYPE2))
        );
        vibrationLine.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (pointer == 0) {
                    profiles.write();
                    ConfigGroup.this.refresh();
                }
            }
        });
        vibrationLine.setNoticeMessage(PaintConstants.UI_VIBRATION_NOTICE);
        vibrationLine.setHoldMessage(PaintConstants.UI_VIBRATION_HOLD);
        vibrationLine.setEnabledMessage(PaintConstants.UI_VIBRATION_ENABLE);
        vibrationLine.setDisabledMessage(PaintConstants.UI_VIBRATION_DISABLE);
        Group joystickGroup = makeGroup(joyStickLabel, 535, PaintConstants.UI_CELL_HEIGHT);
        joystickLine = new Line(
                joyStickCheckBox,
                joystickGroup,
                null,
                new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_CONFIG_CELL_TYPE2))
        );
        joystickLine.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (pointer == 0) {
                    profiles.write();
                    ConfigGroup.this.refresh();
                }
            }
        });
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
        joystickPresetLine.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (pointer == 0 && joystickLine.isActive()) {
                    profiles.write();
                    ConfigGroup.this.refresh();
                }
            }
        });
    }

    public void refresh() {
        musicLine.setActive(Profiles.getSettings().isMusic());
        musicSlider.setValue(Profiles.getSettings().getMusicVolume());
        refreshMusicLine();
        effectsLine.setActive(Profiles.getSettings().isEffects());
        effectsSlider.setValue(Profiles.getSettings().getEffectsVolume());
        refreshEffectsLine();
        soundSlider.setValue(Profiles.getSettings().getSoundVolume());
        refreshSoundLine();
        shaderLine.setActive(Profiles.getSettings().isShader() && screenType == ScreenManager.ScreenType.MENU);
        refreshShaderLine();
        childLine.setActive(Profiles.getSettings().isChild() && screenType == ScreenManager.ScreenType.MENU);
        refreshChildLine();
        level = Profiles.getSettings().getLevel();
        refreshLevelLine();
        vibrationLine.setActive(Profiles.getSettings().isVibration());
        refreshVibrationLine();
        joystickLine.setActive(Profiles.getSettings().isJoystick());
        refreshJoystickPresetLine();
        joyStickModeSlider.setValue(Profiles.getSettings().getJoystick_mode());
        joyStickColorCheckBox.setChecked(Profiles.getSettings().isJoystickColor());
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
        soundLine.setActive(Profiles.getSettings().isEffects() || Profiles.getSettings().isMusic());
        soundSlider.setDisabled(!soundLine.isActive());
        soundCheckBox.setChecked(soundLine.isActive());
        if (Profiles.getSettings().isEffects() || Profiles.getSettings().isMusic()) {
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
        if (!Profiles.getSettings().isMusic() && !Profiles.getSettings().isEffects()) {
            musicLine.setSlave(true);
        }
        soundLine.setMuted(!Profiles.getSettings().isEffects() && !Profiles.getSettings().isMusic());
    }

    private void refreshEffectsLine() {
        effectsSlider.setDisabled(!effectsLine.isActive());
        effectsCheckBox.setChecked(effectsLine.isActive());
        effectsLine.setMuted(effectsSlider.getValue() == 0.0f);
        if (!Profiles.getSettings().isMusic() && !Profiles.getSettings().isEffects()) {
            effectsLine.setSlave(true);
        }
        soundLine.setMuted(!Profiles.getSettings().isEffects() && !Profiles.getSettings().isMusic());
    }

    private void refreshShaderLine() {
        if (screenType == ScreenManager.ScreenType.MENU) {
            shadersCheckBox.setChecked(shaderLine.isActive());
            shadersCheckBox.setLocked(false);
        } else {
            shadersCheckBox.setChecked(Profiles.getSettings().isShader());
            shaderLine.setSlave(true);
            shadersCheckBox.setLocked(true);
            shaderLabel.setText("SHADERS " + (Profiles.getSettings().isShader() ? "(ON)" : "(OFF)"));
        }
    }

    private void refreshChildLine() {
        if (screenType == ScreenManager.ScreenType.MENU) {
            childCheckBox.setChecked(childLine.isActive());
            childCheckBox.setLocked(false);
        } else {
            childCheckBox.setChecked(Profiles.getSettings().isChild());
            childCheckBox.setLocked(true);
            childLine.setSlave(true);
            childLabel.setText("CHILD MODE " + (Profiles.getSettings().isChild() ? "(ON)" : "(OFF)"));
        }
    }

    private void refreshLevelLine() {
        if (screenType == ScreenManager.ScreenType.MENU) {
            levelLine.setActive(true);
            level = level % (Rules.StartLevel.values().length);
            Profiles.getSettings().setLevel(level);
            levelCheckBox.setLocked(false);
        } else {
            levelLine.setActive(false);
            levelLine.setSlave(true);
            levelCheckBox.setLocked(true);
        }
        levelCheckBox.setChecked(true);
        levelLabel.setText("LEVEL " + (level + 1) + " (" + Rules.StartLevel.values()[level].name() + ")");
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
        if (PauseGroup.isPause()) {
            timeGlyphLayout.setText(font96, TimeFormat.formatYDHMS(Standing.getInstance().getPlayTime()));
            notifySlider.setVisible(true);
            table.setBounds(Rules.WORLD_WIDTH - PaintConstants.PLAY_PAUSE_BUTTON_WIDTH / 2 - PauseGroup.playPauseWidth / 2 - 43,
                    Rules.WORLD_HEIGHT - 225 - PauseGroup.playPauseHeight, PauseGroup.playPauseWidth, PauseGroup.playPauseHeight);
        } else {
            notifySlider.setVisible(false);
        }
        noticeGroup.getColor().a = a;
        glyphLayout.setText(font44, notice);
        noticeLabel.setText(notice);
        noticeGroup.setPosition(
                (Rules.WORLD_WIDTH - glyphLayout.width) / 2,
                2 * Rules.WORLD_HEIGHT / 3 + glyphLayoutTitle.height / 2
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

            table.add(leftActor).width(82).height(PaintConstants.UI_CELL_HEIGHT);

            if (rightActor != null) {
                table.add(centerActor).width(273).padLeft(23);
                table.add(rightActor).width(204).height(PaintConstants.UI_CELL_HEIGHT).padRight(29).padLeft(29);
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
                2 * Rules.WORLD_HEIGHT / 3 + 2 * glyphLayoutTitle.height
        );
        if (PauseGroup.isPause() && ScreenManager.getInstance().getCurrentScreen() == ScreenManager.ScreenType.GAME) {
            font96.draw(batch,
                    timeGlyphLayout,
                    (Rules.WORLD_WIDTH - timeGlyphLayout.width) / 2,
                    2 * Rules.WORLD_HEIGHT / 3 - PaintConstants.UI_SETTINGS_HEIGHT - timeGlyphLayout.height
            );
        }
    }
}