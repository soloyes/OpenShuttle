package com.shuttle.game.ui.groups;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.ui.groups.configs.ConfigGroup;
import com.shuttle.game.ui.groups.configs.LeftGroup;
import com.shuttle.game.ui.groups.configs.RightGroup;
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
    private final LeftGroup leftGroup;
    private final RightGroup rightGroup;
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
        this.leftGroup = LeftGroup.getInstance();
        this.rightGroup = RightGroup.getInstance();
        this.buttonMenu = new Button(new TextureRegionDrawable(new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.PAUSE_MENU_HOME))));
        buttonMenu.setVisible(false);
        this.buttonRestart = new Button(new TextureRegionDrawable(new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.PAUSE_MENU_RESTART))));
        buttonRestart.setVisible(false);

        buttonMenu.setOrigin(Align.center);
        buttonRestart.setOrigin(Align.center);

        buttonMenu.setSize(PaintConstants.MENU_BUTTON_WIDTH, PaintConstants.MENU_BUTTON_HEIGHT);
        buttonRestart.setSize(PaintConstants.MENU_BUTTON_WIDTH, PaintConstants.MENU_BUTTON_HEIGHT);

        buttonRestart.setPosition(22, 22);
        buttonMenu.setPosition(946, 22);

        buttonMenu.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    boomBox.playSound(MSConstants.UI_MENU);
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (pointer == 0) {
                    ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
                    Restart.doRestart();
                }
            }
        });

        buttonRestart.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    boomBox.playSound(MSConstants.UI_MENU);
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (pointer == 0) {
                    ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
                    Restart.doRestart();
                }
            }
        });

        playPauseBtn.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    PauseGroup.this.pause = !PauseGroup.this.pause;
                    if (PauseGroup.this.pause) {
                        boomBox.stopAllMusicAndPlayThese(MSConstants.MENU_SONG);
                        playPauseBtn.getStyle().imageUp = play;
                    } else {
                        boomBox.stopAllMusicAndPlayThese(MSConstants.GAME_SONG, MSConstants.BLACKHOLE_ROAR_MUSIC);
                        playPauseBtn.getStyle().imageUp = pause;
                    }
                    configGroup.setVisible(PauseGroup.this.pause);
                    leftGroup.setVisible(PauseGroup.this.pause);
                    rightGroup.setVisible(PauseGroup.this.pause);
                    buttonMenu.setVisible(PauseGroup.this.pause);
                    buttonRestart.setVisible(PauseGroup.this.pause);
                    boomBox.stopAllSound();
                    boomBox.playSound(MSConstants.UI_PAUSE);
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        this.addActor(configGroup);
        this.addActor(playPauseBtn);
        this.addActor(leftGroup);
        this.addActor(rightGroup);
        this.addActor(buttonMenu);
        this.addActor(buttonRestart);
    }

    public void setGameOver() {
        playPauseBtn.setVisible(false);
        configGroup.setVisible(false);
        leftGroup.setVisible(false);
        rightGroup.setVisible(false);
        buttonMenu.setVisible(true);
        buttonRestart.setVisible(true);
    }
}