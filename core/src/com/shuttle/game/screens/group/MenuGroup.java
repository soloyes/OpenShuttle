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
import com.shuttle.game.utils.Assets;
import com.shuttle.game.screens.ScreenManager;

import lombok.Data;

/**
 * @author Shuttle on 6/23/18.
 */
@Data
public class MenuGroup extends Group {
    private BitmapFont font96;
    private boolean isEmptyUserName;
    private TextField userName;
    private Skin skin;
    private FileHandle file;

    public MenuGroup() {
        this.setName(GroupType.MENU.name());
        this.setVisible(true);
        this.skin = Assets.getInstance().getAssetManager().get("uiskin/gameSkin.json");
        this.font96 = Assets.getInstance().getAssetManager().get("fonts/gomarice96.ttf", BitmapFont.class);
        this.file = Gdx.files.local("username");
        this.userName = new TextField(
                "",
                skin,
                "login");

        userName.setPosition(
                ScreenManager.getInstance().getViewport().getWorldWidth() / 4,
                ScreenManager.getInstance().getViewport().getWorldHeight() / 2
        );
        userName.setMaxLength(10);
        userName.setWidth(ScreenManager.getInstance().getViewport().getWorldWidth() / 2);

        userName.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isEmptyUserName = userName.getText().isEmpty();
            }
        });

        if (file.exists()) {
            userName.setText(file.readString());
        }

        //todo: delete soon
        this.userName.setText("Player");
        //
        isEmptyUserName = userName.getText().isEmpty();

        this.addActor(userName);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        font96.draw(batch, "Shuttle", 0, 1800, 1080, 1, false);
        userName.draw(batch, parentAlpha);
        if (isEmptyUserName) {
            font96.draw(batch, "Empty username", 0, 900, 1080, 1, false);
        }
    }
}
