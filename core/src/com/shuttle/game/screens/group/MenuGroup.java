package com.shuttle.game.screens.group;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.screens.ScreenManager;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 6/23/18.
 */
public class MenuGroup extends Group {
    private BitmapFont font96;
    @Getter
    @Setter
    private boolean isEmptyUserName;
    @Getter
    @Setter
    private TextField username;
    private Skin skin;
    @Getter
    @Setter
    private FileHandle file;

    public MenuGroup() {
        this.setName(GroupType.MENU.name());
        this.setVisible(true);
        this.skin = Assets.getInstance().getAssetManager().get(PaintConstants.SKIN_FILE);
        this.font96 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT96, BitmapFont.class);
        this.file = Gdx.files.local("username");
        this.username = new TextField(
                "",
                skin,
                "login");

        username.setPosition(
                ScreenManager.getInstance().getViewport().getWorldWidth() / 4,
                ScreenManager.getInstance().getViewport().getWorldHeight() / 2
        );
        username.setMaxLength(10);
        username.setWidth(ScreenManager.getInstance().getViewport().getWorldWidth() / 2);

        username.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isEmptyUserName = username.getText().isEmpty();
            }
        });

        if (file.exists()) {
            username.setText(file.readString());
        }

        isEmptyUserName = username.getText().isEmpty();

        this.addActor(username);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        font96.draw(batch, "Shuttle", 0, 1800, 1080, 1, false);
        username.draw(batch, parentAlpha);
        if (isEmptyUserName) {
            font96.draw(batch, "Empty username", 0, 900, 1080, 1, false);
        }
    }
}