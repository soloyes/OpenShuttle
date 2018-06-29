package com.shuttle.game.screens.group_screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.shuttle.game.Assets;
import com.shuttle.game.screens.ScreenManager;

/**
 * @author Shuttle on 3/24/18.
 */
public class MenuBar extends Group {
    private Button buttonExit;
    private Button buttonPlay;
    private Button buttonConfig;
    private TextField userName;
    private BitmapFont font96;
    private FileHandle file;
    private Skin skin;
    private boolean isEmptyUserName;

    public MenuBar() {
        this.setName(GroupType.MENU_BAR.name());
        this.skin = Assets.getInstance().getAssetManager().get("uiskin/gameSkin.json");
        this.file = Gdx.files.local("username");
        this.font96 = Assets.getInstance().getAssetManager().get("fonts/gomarice96.ttf", BitmapFont.class);
        this.buttonPlay = new Button(skin, "default");
        this.buttonExit = new Button(skin, "default");
        this.buttonConfig = new Button(skin, "default");
        this.buttonPlay.setOrigin(Align.center);
        this.buttonExit.setOrigin(Align.center);
        this.buttonConfig.setOrigin(Align.center);
        this.buttonPlay.setSize(128, 128);
        this.buttonExit.setSize(128, 128);
        this.buttonConfig.setSize(128, 128);
        this.userName = new TextField(
                "",
                skin,
                "login");

        userName.setPosition(
                ScreenManager.getInstance().getViewport().getWorldWidth() / 4,
                ScreenManager.getInstance().getViewport().getWorldHeight() / 2
        );
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

        this.userName.setText("Player");
        isEmptyUserName = userName.getText().isEmpty();


        buttonExit.setPosition(
                ScreenManager.getInstance().getViewport().getWorldWidth() - buttonExit.getWidth(),
                0
        );

        buttonConfig.setPosition(
                ScreenManager.getInstance().getViewport().getWorldWidth() / 2 - buttonExit.getWidth() / 2,
                0
        );

        buttonExit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        buttonPlay.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!isEmptyUserName) {
                    ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
                    file.writeString(userName.getText(), false);
                }
            }
        });

        buttonConfig.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                for (Actor a : getStage().getActors().toArray()) {
                    if (!a.getName().equals(GroupType.MENU_BAR.name())) {
                        a.setVisible(!a.isVisible());
                    }
                }
            }
        });

        this.addActor(buttonExit);
        this.addActor(buttonPlay);
        this.addActor(buttonConfig);
        this.addActor(userName);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        buttonPlay.draw(batch, parentAlpha);
        buttonExit.draw(batch, parentAlpha);
        buttonConfig.draw(batch, parentAlpha);
        userName.draw(batch, parentAlpha);
        if (isEmptyUserName) {
            font96.draw(batch, "Empty username", 0, 900, 1080, 1, false);
        }
    }
}