package com.shuttle.game.ui.groups;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.ui.groups.profilesMVC.VerticalGroupController;
import com.shuttle.game.ui.groups.profilesMVC.VerticalGroupView;
import com.shuttle.game.utils.Assets;


/**
 * @author Shuttle on 6/23/18.
 */
public class MenuGroup extends Group {
    private BitmapFont font64;
    private BitmapFont font256;
    private static boolean isValidUserName;
    private static TextField username;
    private Button buttonProfile;
    private GlyphLayout glyphLayout;
    private String notice;
    private VerticalGroupView verticalGroupView;

    public static void setUsername(String username) {
        MenuGroup.username.setText(username);
    }

    private MenuGroup(String name) {
        this.setName(name);
        this.setVisible(true);
        Skin skin = Assets.getInstance().getAssetManager().get(PaintConstants.SKIN_FILE);
        this.font256 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT256, BitmapFont.class);
        this.font64 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT64, BitmapFont.class);
        this.buttonProfile = new Button(skin, "default");
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font64;
        TextField.TextFieldStyle tfs = skin.get("default", TextField.TextFieldStyle.class);
        tfs.font = font64;
        tfs.fontColor = Color.valueOf("ffffffff");
        tfs.cursor.setMinWidth(10);
        username = new TextField("", skin, "default");
        this.glyphLayout = new GlyphLayout();
        //MVC
        this.verticalGroupView = new VerticalGroupView();
        VerticalGroupController verticalGroupController = new VerticalGroupController(verticalGroupView);
        verticalGroupView.setController(verticalGroupController);
        //
    }

    public MenuGroup() {
        this(GroupType.MENU.name());
        username.setAlignment(Align.center);
        username.setMaxLength(14);
        notice = "";
        glyphLayout.setText(font64, "");

        username.setPosition(Rules.WORLD_WIDTH / 4, username.getHeight() + verticalGroupView.getY() + verticalGroupView.getHeight());
        username.setWidth(Rules.WORLD_WIDTH / 2);
        buttonProfile.setSize(128, 128);
        buttonProfile.setPosition(Rules.WORLD_WIDTH / 2 - buttonProfile.getWidth() / 2, buttonProfile.getHeight() + verticalGroupView.getY() + verticalGroupView.getHeight() + username.getHeight() - 22);

        buttonProfile.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                verticalGroupView.switchVisible();
            }
        });
        username.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isValidUserName = username.getText().isEmpty();
            }
        });

        isValidUserName = username.getText().isEmpty();

        this.addActor(buttonProfile);
        this.addActor(username);
        this.addActor(verticalGroupView);
    }

    public static boolean isValidProfile() {
        return isValidUserName;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        font256.draw(batch, "SHUTTLE", 0, 1800, 1080, 1, false);
        font64.draw(batch, glyphLayout,
                Rules.WORLD_WIDTH / 2 - glyphLayout.width / 2,
                verticalGroupView.getY() + verticalGroupView.getHeight() + glyphLayout.height + 22
        );
    }
}