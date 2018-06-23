package com.shuttle.game.screens.group_screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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

    public MenuBar() {
        this.setName(GroupType.MENU_BAR.name());
        Skin skin = Assets.getInstance().getAssetManager().get("uiskin/gameSkin.json");

        buttonPlay = new Button(skin, "default");
        buttonExit = new Button(skin, "default");
        buttonConfig = new Button(skin, "default");
        buttonPlay.setOrigin(Align.center);
        buttonExit.setOrigin(Align.center);
        buttonConfig.setOrigin(Align.center);
        buttonPlay.setSize(128, 128);
        buttonExit.setSize(128, 128);
        buttonConfig.setSize(128, 128);

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
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
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
}