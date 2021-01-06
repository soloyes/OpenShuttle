package com.buran.game.ui.groups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.buran.game.conf.MSConstants;
import com.buran.game.conf.PaintConstants;
import com.buran.game.conf.Rules;
import com.buran.game.conf.VConstants;
import com.buran.game.effects.BoomBox;
import com.buran.game.effects.Oscillator;
import com.buran.game.effects.Vibrator;
import com.buran.game.screens.ScreenManager;
import com.buran.game.ui.UIButton;
import com.buran.game.ui.groups.configs.ConfigGroup;
import com.buran.game.ui.groups.mvc.global.Global;
import com.buran.game.ui.groups.mvc.profiles.ProfileDAO;
import com.buran.game.ui.groups.mvc.profiles.Profiles;
import com.buran.game.ui.groups.mvc.profiles.ProfilesController;
import com.buran.game.ui.groups.mvc.profiles.ProfilesView;
import com.buran.game.ui.groups.mvc.results.ResultGroup;
import com.buran.game.utils.Assets;

/**
 * @author soloyes on 6/23/18.
 */

public class MenuGroup extends Group {
    private static boolean validUserName;
    private static TextField username;
    private Rectangle rectangle;
    private Vector2 tmp1V2;
    private Vector2 tmp2V2;
    private static float shadowTime;
    private static StringBuilder notice = new StringBuilder();
    private BitmapFont font44;
    private BitmapFont font72;
    private Button buttonProfiles;
    private GlyphLayout glyphLayout;
    private ProfilesView profilesView;
    private ProfilesController profilesController;
    private float a;
    private float blinkTime;
    private Label noticeLabel;
    private BoomBox boomBox;
    private Group noticeGroup;
    private float a1;
    private Oscillator oscillatorA1;
    private Global global;
    private static Button buttonOK;

    public MenuGroup(ConfigGroup configGroup, ResultGroup resultGroup) {
        this(GroupType.MENU.name(), configGroup, resultGroup);
        this.global = new Global();
        global.init();
        this.oscillatorA1 = new Oscillator();
        username.setAlignment(Align.center);
        username.setMaxLength(8);
        username.setWidth(540);
        username.setHeight(108);
        username.setPosition(Rules.WORLD_WIDTH / 2 - username.getWidth() / 2, 960);
        this.rectangle = new Rectangle(Rules.WORLD_WIDTH / 2 - username.getWidth() / 2, 960, 540, 108);
        this.tmp1V2 = new Vector2();
        this.tmp2V2 = new Vector2();
        buttonProfiles.setSize(PaintConstants.PROFILES_BUTTON_WIDTH, PaintConstants.PROFILES_BUTTON_HEIGHT);
        buttonProfiles.setPosition(Rules.WORLD_WIDTH / 2 - PaintConstants.PROFILES_BUTTON_WIDTH / 2, 1096);

        buttonProfiles.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (buttonProfiles.isDisabled()) {
                    boomBox.playSound(MSConstants.UI_CLICK);
                    Vibrator.vibrateUI(VConstants.UI_CLICK);
                } else if (pointer == 0) {
                    boomBox.playSound(MSConstants.UI_MENU);
                    Vibrator.vibrateUI(VConstants.UI_CLICK);
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (pointer == 0) {
                    profilesView.switchVisible();
                    setNotice(PaintConstants.UI_YOUR_USERNAME);
                }
            }
        });

        username.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    boomBox.playSound(MSConstants.UI_CLICK);
                    Vibrator.vibrateUI(VConstants.UI_CLICK);
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        username.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setNotice(PaintConstants.UI_CAN_SAVE);
                buttonOK.setVisible(true);
                boomBox.playSound(MSConstants.UI_INPUT);
            }
        });
        username.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (validUserName) {
                    setNotice(PaintConstants.UI_PROFILE_EDIT);
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    if (validUserName) {
                        getStage().unfocusAll();
                        ProfileDAO profile = profilesController.getActive();
                        if (profile == null) {
                            profile = profilesView.addProfile(username.getText());
                            profilesController.select(profile);
                            setNotice(PaintConstants.UI_YOUR_USERNAME);
                            boomBox.playSound(MSConstants.UI_PROFILE_SAVED);
                        } else {
                            setNotice(PaintConstants.UI_PROFILE_SAVED);
                            boomBox.playSound(MSConstants.UI_PROFILE_CHANGED);
                            profile.setUsername(username.getText());
                            profilesController.flush();
                            profilesController.update();
                            profilesView.getResultGroup().init();
                        }
                        Vibrator.vibrateUI(VConstants.UI_CHANGED);
                        Gdx.input.setOnscreenKeyboardVisible(false);
                        buttonOK.setVisible(false);
                    } else {
                        boomBox.playSound(MSConstants.UI_EMPTY_NAME);
                        Vibrator.vibrateUI(VConstants.UI_EMPTY_NAME);
                    }
                }
                return super.keyDown(event, keycode);
            }
        });

        buttonOK.setPosition(username.getX() + username.getWidth() + 24, username.getY());
        buttonOK.setVisible(false);
        buttonOK.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    boomBox.playSound(MSConstants.UI_MENU);
                    if (!validUserName) {
                        Vibrator.vibrateUI(VConstants.UI_CLICK);
                    }
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (validUserName) {
                    getStage().unfocusAll();
                    ProfileDAO profile = profilesController.getActive();
                    if (profile == null) {
                        profile = profilesView.addProfile(username.getText());
                        profilesController.select(profile);
                        setNotice(PaintConstants.UI_YOUR_USERNAME);
                        boomBox.playSound(MSConstants.UI_PROFILE_SAVED);
                    } else {
                        setNotice(PaintConstants.UI_PROFILE_SAVED);
                        boomBox.playSound(MSConstants.UI_PROFILE_CHANGED);
                        profile.setUsername(username.getText());
                        profilesController.flush();
                        profilesController.update();
                        profilesView.getResultGroup().init();
                    }
                    Vibrator.vibrateUI(VConstants.UI_CHANGED);
                    Gdx.input.setOnscreenKeyboardVisible(false);
                    buttonOK.setVisible(false);
                } else {
                    boomBox.playSound(MSConstants.UI_EMPTY_NAME);
                    Vibrator.vibrateUI(VConstants.UI_EMPTY_NAME);
                }
            }
        });

        noticeGroup = new Group();
        noticeGroup.addActor(noticeLabel);
        noticeLabel.setFillParent(true);
        this.addActor(buttonProfiles);
        this.addActor(noticeGroup);
        this.addActor(username);
        this.addActor(buttonOK);
        this.addActor(profilesView);
    }

    private MenuGroup(String name, ConfigGroup configGroup, ResultGroup resultGroup) {
        this.setName(name);
        this.setVisible(true);
        Skin skin = Assets.getInstance().getAssetManager().get(PaintConstants.SKIN_FILE);
        this.font44 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT44, BitmapFont.class);
        this.font72 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT72, BitmapFont.class);
        Label.LabelStyle noticeStyle44 = new Label.LabelStyle(
                font44,
                new Color(1.0f, 1.0f, 1.0f, 1.0f)
        );

        noticeLabel = new Label(notice, noticeStyle44);
        this.buttonProfiles = new UIButton(new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.PROFILES_BUTTON_MENU)));
        buttonProfiles.getStyle().disabled = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.LOCK_BUTTON));

        TextField.TextFieldStyle tfs = skin.get("default", TextField.TextFieldStyle.class);
        tfs.font = font72;
        tfs.fontColor = Color.valueOf("ffffffff");
        tfs.cursor.setMinWidth(10);
        tfs.background = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.PROFILES_BORDER));
        username = new TextField("", skin, "default");
        buttonOK = new UIButton(new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.OK_BUTTON_MENU)));
        this.glyphLayout = new GlyphLayout();
        //MVC
        Profiles profiles = new Profiles();
        this.profilesView = new ProfilesView(configGroup, resultGroup);
        this.profilesController = new ProfilesController(profilesView, profiles);
        profilesView.setController(profilesController);
        profilesController.update();
        //
        if (profilesController.getActive() != null) {
            setNotice(PaintConstants.UI_YOUR_USERNAME);
        }
        this.boomBox = new BoomBox();
    }

    public static void setNotice(String text) {
        notice.setLength(0);
        notice.append(text);
        shadowTime = Rules.NOTICE_WAIT;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!global.isGlobalStart()) {
            this.setVisible(false);
        } else {
            buttonProfiles.getColor().a = a1;
            super.draw(batch, parentAlpha);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (profilesController.getActive() != null) {
            a1 = oscillatorA1.oscillate(a1, 1.0f, 0.3f, 1.0f, delta);
        } else {
            a1 = 1.0f;
        }
        tmp1V2.set(Gdx.input.getX(), Gdx.input.getY());
        tmp2V2 = ScreenManager.getInstance().getViewport().unproject(tmp1V2);
        if (Gdx.input.justTouched() && !rectangle.contains(tmp2V2.x, tmp2V2.y)) {
            getStage().unfocus(username);
            Gdx.input.setOnscreenKeyboardVisible(false);
        }
        validUserName = !username.getText().isEmpty();
        if (!isValidUserName()) {
            setNotice(PaintConstants.UI_EMPTY_USERNAME);
            blink(delta);
        } else {
            shadowTime -= delta;
            if (shadowTime <= 0.0f) {
                shadowTime = 0.0f;
                a = 0.0f;
            } else {
                a = 1.0f;
            }
        }
        if (ProfilesController.isEmpty()) {
            buttonProfiles.setDisabled(true);
            profilesView.setVisible(false);
        } else {
            buttonProfiles.setDisabled(false);
        }

        glyphLayout.setText(font44, notice);
        noticeLabel.setText(notice);
        noticeGroup.setPosition(Rules.WORLD_WIDTH / 2 - glyphLayout.width / 2,
                900 + glyphLayout.height / 2 + 20);
        noticeGroup.getColor().a = a;
    }

    public static boolean isValidUserName() {
        return validUserName;
    }

    private void blink(float delta) {
        blinkTime += delta;
        if (blinkTime >= 0.0f) {
            a = 1.0f;
        }
        if (blinkTime >= PaintConstants.NOTICE_BLINK_TIME) {
            a = 0.0f;
        }
        if (blinkTime >= PaintConstants.NOTICE_BLINK_TIME * 2) {
            blinkTime = 0.0f;
        }
    }

    public void dispose() {
        profilesController.dispose();
    }

    public static void setUsername(String username) {
        MenuGroup.username.setText(username);
        MenuGroup.buttonOK.setVisible(false);
    }
}