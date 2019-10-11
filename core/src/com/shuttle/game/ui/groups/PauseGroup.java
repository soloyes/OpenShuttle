package com.shuttle.game.ui.groups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.ui.groups.configs.ConfigGroup;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.Restart;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 7/8/18.
 */
public class PauseGroup extends Group {
    private static PauseGroup instance;
    private final Button buttonMenu;
    private final Button buttonRestart;
    private final ImageButton playPauseBtn;
    private final ConfigGroup configGroup;
    private BoomBox boomBox;
    @Getter
    @Setter
    private boolean pause;

    public static PauseGroup getInstance() {
        if (instance == null) {
            instance = new PauseGroup();
        }
        return instance;
    }

    public void dispose() {
        instance = null;
    }

    private PauseGroup() {
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

        this.configGroup = ConfigGroup.getInstance();
        configGroup.setVisible(false);
        this.buttonMenu = new Button(skin, "default");
        buttonMenu.setVisible(false);
        this.buttonRestart = new Button(skin, "default");
        buttonRestart.setVisible(false);

        buttonMenu.setOrigin(Align.center);
        buttonRestart.setOrigin(Align.center);

        buttonMenu.setSize(128f, 128f);
        buttonRestart.setSize(128f, 128f);

        buttonRestart.setPosition(930, 22);
        buttonMenu.setPosition(22, 22);

        buttonMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //Workaround. If use BoomBox.playSound(), then Sound is unload while change Screen
                if (GamePreferences.isEffectsEnabled()) {
                    Sound sound = Gdx.audio.newSound(Gdx.files.internal(MSConstants.UI_MENU));
                    sound.play(GamePreferences.getOverallEffectsVolume());
                }
                //
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
                Restart.doRestart();
            }
        });

        buttonRestart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //Workaround. If use BoomBox.playSound(), then Sound is unload while change Screen
                if (GamePreferences.isEffectsEnabled()) {
                    Sound sound = Gdx.audio.newSound(Gdx.files.internal(MSConstants.UI_MENU));
                    sound.play(GamePreferences.getOverallEffectsVolume());
                }
                //
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
                Restart.doRestart();
            }
        });

        //todo: add sound here
        playPauseBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
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