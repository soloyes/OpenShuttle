package com.shuttle.game.ui.groups;

import com.badlogic.gdx.Gdx;
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
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.ui.groups.mvc.profiles.GroupController;
import com.shuttle.game.ui.groups.mvc.profiles.ProfileDAO;
import com.shuttle.game.ui.groups.mvc.profiles.Profiles;
import com.shuttle.game.ui.groups.mvc.profiles.VerticalGroupList;
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
    private VerticalGroupList verticalGroupList;
    private GroupController groupController;
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
                } else if (pointer == 0) {
                    verticalGroupList.switchVisible();
                    setNotice(PaintConstants.UI_YOUR_USERNAME);
                    boomBox.playSound(MSConstants.UI_MENU);
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
                        ProfileDAO profile = groupController.getActive();
                        if (profile == null) {
                            profile = verticalGroupList.addProfile(username.getText());
                            groupController.select(profile);
                            setNotice(PaintConstants.UI_YOUR_USERNAME);
                        } else {
                            setNotice(PaintConstants.UI_PROFILE_SAVED);
                            profile.setUsername(username.getText());
                            groupController.flush();
                            groupController.update();
                        }
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
        this.addActor(verticalGroupList);
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
        this.verticalGroupList = new VerticalGroupList();
        this.groupController = new GroupController(verticalGroupList, new Profiles());
        verticalGroupList.setController(groupController);
        groupController.update();
        //
        if (groupController.getActive() != null) {
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
        if (Gdx.input.justTouched()) {
            //todo: потестить
            //getStage().unfocusAll();
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
        if (GroupController.isEmpty()) {
            buttonProfiles.setDisabled(true);
            verticalGroupList.setVisible(false);
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
        groupController.dispose();
    }

    public static void setUsername(String username) {
        MenuGroup.username.setText(username);
    }
}