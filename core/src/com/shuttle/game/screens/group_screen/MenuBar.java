package com.shuttle.game.screens.group_screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.shuttle.game.screens.BaseScreen;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.screens.ScreenManager;

/**
 * @author Shuttle on 3/24/18.
 */
public class MenuBar extends Group {
    private Button buttonExit;
    private Button buttonPlay;
    private Button buttonConfig;
    private Skin skin;
    private MenuGroup group;

    public MenuBar() {
        this.setName(GroupType.MENU_BAR.name());
        this.skin = Assets.getInstance().getAssetManager().get("uiskin/gameSkin.json");
        this.buttonPlay = new Button(skin, "default");
        this.buttonExit = new Button(skin, "default");
        this.buttonConfig = new Button(skin, "default");
        this.buttonPlay.setOrigin(Align.center);
        this.buttonExit.setOrigin(Align.center);
        this.buttonConfig.setOrigin(Align.center);
        this.buttonPlay.setSize(128, 128);
        this.buttonExit.setSize(128, 128);
        this.buttonConfig.setSize(128, 128);

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
                if (!group.isEmptyUserName()) {
                    ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
                    group.getFile().writeString(group.getUserName().getText(), false);
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
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        buttonPlay.draw(batch, parentAlpha);
        buttonExit.draw(batch, parentAlpha);
        buttonConfig.draw(batch, parentAlpha);
    }

    public void setGroup(MenuGroup group) {
        this.group = group;
    }
}