package com.shuttle.game.screens.group_screen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.shuttle.game.Assets;
import com.shuttle.game.screens.ScreenManager;

/**
 * @author Shuttle on 7/8/18.
 */
public class PauseGroup extends Group {
    private boolean pause;

    public PauseGroup() {
        pause = false;
        Skin skin = Assets.getInstance().getAssetManager().get("uiskin/gameSkin.json");
        Button pauseBtn = new Button(skin, "default");
        pauseBtn.setOrigin(Align.center);
        pauseBtn.setSize(128f, 128f);
        pauseBtn.setPosition(
                ScreenManager.getInstance().getViewport().getWorldWidth() - pauseBtn.getWidth() - 20,
                ScreenManager.getInstance().getViewport().getWorldHeight() - pauseBtn.getHeight() - 20
        );

        final ConfigGroup configGroup = new ConfigGroup();
        configGroup.setVisible(false);
        final Button buttonMenu = new Button(skin, "default");
        buttonMenu.setVisible(false);
        final Button buttonRestart = new Button(skin, "default");
        buttonRestart.setVisible(false);

        buttonMenu.setOrigin(Align.center);
        buttonRestart.setOrigin(Align.center);

        buttonMenu.setSize(128f, 128f);
        buttonRestart.setSize(128f, 128f);

        buttonRestart.setPosition(
                ScreenManager.getInstance().getViewport().getWorldWidth() - buttonRestart.getWidth(),
                0
        );

        buttonMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
            }
        });

        buttonRestart.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
            }
        });

        pauseBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                pause = !pause;
                configGroup.setVisible(pause);
                buttonMenu.setVisible(pause);
                buttonRestart.setVisible(pause);
            }
        });

        this.addActor(pauseBtn);
        this.addActor(configGroup);
        this.addActor(buttonMenu);
        this.addActor(buttonRestart);
    }

    public boolean isPause() {
        return pause;
    }
}
