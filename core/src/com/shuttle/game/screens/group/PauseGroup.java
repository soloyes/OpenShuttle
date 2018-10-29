package com.shuttle.game.screens.group;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.utils.BoomBox;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 7/8/18.
 */
public class PauseGroup extends Group {
    private final Button buttonMenu;
    private final Button buttonRestart;
    private final Button pauseBtn;
    private final ConfigGroup configGroup;
    private BoomBox boomBox;
    @Getter
    @Setter
    private boolean pause;

    public PauseGroup() {
        this.boomBox = new BoomBox();
        this.pause = false;
        Skin skin = Assets.getInstance().getAssetManager().get(PaintConstants.SKIN_FILE);
        this.pauseBtn = new Button(skin, "default");
        pauseBtn.setOrigin(Align.center);
        pauseBtn.setSize(128f, 128f);
        pauseBtn.setPosition(
                ScreenManager.getInstance().getViewport().getWorldWidth() - pauseBtn.getWidth() - 20,
                ScreenManager.getInstance().getViewport().getWorldHeight() - pauseBtn.getHeight() - 20
        );

        this.configGroup = new ConfigGroup(ScreenManager.ScreenType.GAME);
        configGroup.setVisible(false);
        this.buttonMenu = new Button(skin, "default");
        buttonMenu.setVisible(false);
        this.buttonRestart = new Button(skin, "default");
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
                //Workaround. If use BoomBox.playSound(), then Sound is unload while change Screen
                if (GamePreferences.isSoundEnabled()) {
                    Sound sound = Gdx.audio.newSound(Gdx.files.internal(MSConstants.BUTTON_SOUND));
                    sound.play(GamePreferences.getSoundVolume());
                }
                //
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
                Rules.GAME_OVER = false;
            }
        });

        buttonRestart.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                //Workaround. If use BoomBox.playSound(), then Sound is unload while change Screen
                if (GamePreferences.isSoundEnabled()) {
                    Sound sound = Gdx.audio.newSound(Gdx.files.internal(MSConstants.BUTTON_SOUND));
                    sound.play(GamePreferences.getSoundVolume());
                }
                //
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
                Rules.GAME_OVER = false;
            }
        });

        pauseBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                pause = !pause;
                if (pause) {
                    boomBox.stopAllMusicAndPlayThese(MSConstants.MENU_MUSIC);
                } else {
                    boomBox.stopAllMusicAndPlayThese(MSConstants.GAME_MUSIC, MSConstants.BLACKHOLE_ROAR_MUSIC);
                }
                configGroup.setVisible(pause);
                buttonMenu.setVisible(pause);
                buttonRestart.setVisible(pause);
                boomBox.playSound(MSConstants.PAUSE_SOUND);
                boomBox.stopAllSound();
            }
        });

        this.addActor(pauseBtn);
        this.addActor(configGroup);
        this.addActor(buttonMenu);
        this.addActor(buttonRestart);
    }

    public void setGameOver() {
        pauseBtn.setVisible(false);
        configGroup.setVisible(false);
        buttonMenu.setVisible(true);
        buttonRestart.setVisible(true);
    }
}