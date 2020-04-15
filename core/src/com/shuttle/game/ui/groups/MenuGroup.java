package com.shuttle.game.ui.groups;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.conf.VConstants;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.effects.Vibrator;
import com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO;
import com.shuttle.game.ui.groups.mvc.profiles.Profiles;
import com.shuttle.game.ui.groups.mvc.profiles.ProfilesController;
import com.shuttle.game.ui.groups.mvc.profiles.ProfilesView;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 6/23/18.
 */

public class MenuGroup extends Group {
    private static boolean validUserName;
    private static TextField username;
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

    public MenuGroup() {
        this(GroupType.MENU.name());
        username.setAlignment(Align.center);
        username.setMaxLength(8);
        username.setPosition(270, 960);
        username.setWidth(540);
        username.setHeight(108);
        buttonProfiles.setSize(PaintConstants.PROFILES_BUTTON_WIDTH, PaintConstants.PROFILES_BUTTON_HEIGHT);
        buttonProfiles.setPosition(487, 1096);

        buttonProfiles.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (buttonProfiles.isDisabled()) {
                    boomBox.playSound(MSConstants.UI_CLICK);
                    Vibrator.vibrate(VConstants.UI_CLICK);
                } else if (pointer == 0) {
                    boomBox.playSound(MSConstants.UI_MENU);
                    Vibrator.vibrate(VConstants.UI_CLICK);
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
                    Vibrator.vibrate(VConstants.UI_CLICK);
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        username.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setNotice(PaintConstants.UI_CAN_SAVE);
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
                        ProfileDAO profile = profilesController.getActive();
                        if (profile == null) {
                            profile = profilesView.addProfile(username.getText());
                            profilesController.select(profile);
                            setNotice(PaintConstants.UI_YOUR_USERNAME);
                        } else {
                            setNotice(PaintConstants.UI_PROFILE_SAVED);
                            profile.setUsername(username.getText());
                            profilesController.flush();
                            profilesController.update();
                        }
                        boomBox.playSound(MSConstants.UI_CHANGED);
                        Vibrator.vibrate(VConstants.UI_CHANGED);
                    } else {
                        boomBox.playSound(MSConstants.UI_EMPTY_NAME);
                        Vibrator.vibrate(VConstants.UI_EMPTY_NAME);
                    }
                }
                return super.keyDown(event, keycode);
            }
        });

        noticeGroup = new Group();
        noticeGroup.addActor(noticeLabel);
        noticeLabel.setFillParent(true);
        this.addActor(buttonProfiles);
        this.addActor(noticeGroup);
        this.addActor(username);
        this.addActor(profilesView);
    }

    private MenuGroup(String name) {
        this.setName(name);
        this.setVisible(true);
        Skin skin = Assets.getInstance().getAssetManager().get(PaintConstants.SKIN_FILE);
        this.font44 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT44, BitmapFont.class);
        this.font72 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT72, BitmapFont.class);
        Label.LabelStyle noticeStyle64 = new Label.LabelStyle(
                font44,
                new Color(1.0f, 1.0f, 1.0f, 1.0f)
        );

        noticeLabel = new Label(notice, noticeStyle64);
        this.buttonProfiles = new Button(new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.PROFILES_BUTTON_MENU)));
        buttonProfiles.getStyle().disabled = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.LOCK_BUTTON));

        TextField.TextFieldStyle tfs = skin.get("default", TextField.TextFieldStyle.class);
        tfs.font = font72;
        tfs.fontColor = Color.valueOf("ffffffff");
        tfs.cursor.setMinWidth(10);
        tfs.background = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.PROFILES_BORDER));
        username = new TextField("", skin, "default");
        this.glyphLayout = new GlyphLayout();
        //MVC
        this.profilesView = new ProfilesView();
        this.profilesController = new ProfilesController(profilesView, new Profiles());
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
    public void act(float delta) {
        super.act(delta);
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
                904 + glyphLayout.height / 2 + 22);
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
    }
}