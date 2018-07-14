package com.shuttle.game.screens.group_screen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.screens.ScreenManager;

import lombok.Data;

/**
 * @author Shuttle on 7/8/18.
 */
@Data
public class PauseGroup extends Group {
    private boolean pause;
    private Button pauseBtn;
    private final Button buttonMenu;
    private final Button buttonRestart;
    private final ConfigGroup configGroup;

    public PauseGroup() {
        pause = false;
        Skin skin = Assets.getInstance().getAssetManager().get("uiskin/gameSkin.json");
        pauseBtn = new Button(skin, "default");
        pauseBtn.setOrigin(Align.center);
        pauseBtn.setSize(128f, 128f);
        pauseBtn.setPosition(
                ScreenManager.getInstance().getViewport().getWorldWidth() - pauseBtn.getWidth() - 20,
                ScreenManager.getInstance().getViewport().getWorldHeight() - pauseBtn.getHeight() - 20
        );

        configGroup = new ConfigGroup();
        configGroup.setVisible(false);
        buttonMenu = new Button(skin, "default");
        buttonMenu.setVisible(false);
        buttonRestart = new Button(skin, "default");
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
                Rules.GAME_OVER = false;
            }
        });

        buttonRestart.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
                Rules.GAME_OVER = false;
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

    public void setGameOver(){
        pauseBtn.setVisible(false);
        configGroup.setVisible(false);
        buttonMenu.setVisible(true);
        buttonRestart.setVisible(true);
    }
}
