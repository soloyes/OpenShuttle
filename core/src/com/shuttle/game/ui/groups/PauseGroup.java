package com.shuttle.game.ui.groups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.utils.Assets;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 7/8/18.
 */
public class PauseGroup extends Group {
    private final Button buttonMenu;
    private final Button buttonRestart;
    private final ImageButton playPauseBtn;
    private final com.shuttle.game.ui.groups.ConfigGroup configGroup;
    private BoomBox boomBox;
    @Getter
    @Setter
    private boolean pause;

    public PauseGroup() {
        this.boomBox = new BoomBox();
        this.pause = false;
        Skin skin = Assets.getInstance().getAssetManager().get(PaintConstants.SKIN_FILE);
        final TextureRegionDrawable play = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.PLAY_BUTTON));
        final TextureRegionDrawable pause = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.PAUSE_BUTTON));
        this.playPauseBtn = new ImageButton(pause);
        playPauseBtn.setOrigin(Align.center);
        playPauseBtn.setSize(PaintConstants.PLAY_PAUSE_BUTTON_WIDTH, PaintConstants.PLAY_PAUSE_BUTTON_HEIGHT);
        playPauseBtn.setPosition(
                Rules.WORLD_WIDTH - playPauseBtn.getWidth() - 44,
                Rules.WORLD_HEIGHT - playPauseBtn.getHeight() - 44
        );

        this.configGroup = new com.shuttle.game.ui.groups.ConfigGroup(ScreenManager.ScreenType.GAME);
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
                Rules.WORLD_WIDTH - buttonRestart.getWidth(),
                0
        );

        buttonMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                //Workaround. If use BoomBox.playSound(), then Sound is unload while change Screen
                if (GamePreferences.isSoundEnabled()) {
                    Sound sound = Gdx.audio.newSound(Gdx.files.internal(MSConstants.UI_MENU));
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
                    Sound sound = Gdx.audio.newSound(Gdx.files.internal(MSConstants.UI_MENU));
                    sound.play(GamePreferences.getSoundVolume());
                }
                //
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
                Rules.GAME_OVER = false;
                Shuttle.respawn();
            }
        });

        playPauseBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                PauseGroup.this.pause = !PauseGroup.this.pause;
                if (PauseGroup.this.pause) {
                    boomBox.stopAllMusicAndPlayThese(MSConstants.MENU_SONG);
                    playPauseBtn.getStyle().imageUp = play;
                } else {
                    boomBox.stopAllMusicAndPlayThese(MSConstants.GAME_SONG, MSConstants.BLACKHOLE_ROAR_MUSIC);
                    playPauseBtn.getStyle().imageUp = pause;
                }
                configGroup.setVisible(PauseGroup.this.pause);
                buttonMenu.setVisible(PauseGroup.this.pause);
                buttonRestart.setVisible(PauseGroup.this.pause);
                boomBox.playSound(MSConstants.UI_PAUSE);
                boomBox.stopAllSound();
            }
        });

        this.addActor(playPauseBtn);
        this.addActor(configGroup);
        this.addActor(buttonMenu);
        this.addActor(buttonRestart);
    }

    public void setGameOver() {
        playPauseBtn.setVisible(false);
        configGroup.setVisible(false);
        buttonMenu.setVisible(true);
        buttonRestart.setVisible(true);
    }
}