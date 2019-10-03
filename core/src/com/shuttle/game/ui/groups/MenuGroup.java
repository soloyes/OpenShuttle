package com.shuttle.game.ui.groups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
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
    private BitmapFont font64;
    private Button buttonProfile;
    private GlyphLayout glyphLayout;
    private VerticalGroupList verticalGroupList;
    private GroupController groupController;
    private float a;
    private float blinkTime;
    private Label noticeLabel;
    private BoomBox boomBox;

    public MenuGroup() {
        this(GroupType.MENU.name());
        username.setAlignment(Align.center);
        username.setMaxLength(14);
        glyphLayout.setText(font64, notice);

        username.setPosition(Rules.WORLD_WIDTH / 4, username.getHeight() + verticalGroupList.getY() + verticalGroupList.getHeight());
        username.setWidth(Rules.WORLD_WIDTH / 2);
        buttonProfile.setSize(128, 128);
        buttonProfile.setPosition(Rules.WORLD_WIDTH / 2 - buttonProfile.getWidth() / 2, buttonProfile.getHeight() + verticalGroupList.getY() + verticalGroupList.getHeight() + username.getHeight() - 22);

        buttonProfile.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!buttonProfile.isDisabled()) {
                    verticalGroupList.switchVisible();
                    setNotice(PaintConstants.UI_YOUR_USERNAME);
                    boomBox.playSound(MSConstants.UI_MENU);
                }
            }
        });

        username.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setNotice(PaintConstants.UI_CAN_SAVE);
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

        this.addActor(buttonProfile);
        this.addActor(username);
        this.addActor(verticalGroupList);
    }

    private MenuGroup(String name) {
        this.setName(name);
        this.setVisible(true);
        Skin skin = Assets.getInstance().getAssetManager().get(PaintConstants.SKIN_FILE);
        this.font64 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT64, BitmapFont.class);
        Label.LabelStyle labelStyle64 = new Label.LabelStyle();
        labelStyle64.font = font64;
        noticeLabel = new Label(notice, labelStyle64);
        this.buttonProfile = new Button(skin, "default");
        TextField.TextFieldStyle tfs = skin.get("default", TextField.TextFieldStyle.class);
        tfs.font = font64;
        tfs.fontColor = Color.valueOf("ffffffff");
        tfs.cursor.setMinWidth(10);
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
            buttonProfile.setDisabled(true);
            verticalGroupList.setVisible(false);
        } else {
            buttonProfile.setDisabled(false);
        }
        glyphLayout.setText(font64, notice);
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

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        noticeLabel.setText(notice);
        noticeLabel.setPosition(Rules.WORLD_WIDTH / 2 - glyphLayout.width / 2,
                verticalGroupList.getY() + verticalGroupList.getHeight() + glyphLayout.height / 2 + 22);
        noticeLabel.draw(batch, a);
    }

    public void dispose() {
        groupController.dispose();
    }

    public static void setUsername(String username) {
        MenuGroup.username.setText(username);
    }
}