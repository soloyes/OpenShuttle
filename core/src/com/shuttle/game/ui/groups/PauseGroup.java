package com.shuttle.game.ui.groups;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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
import com.shuttle.game.conf.VConstants;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.effects.Vibrator;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.ui.UIButton;
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
    private final Button buttonMenu;
    private final Button buttonRestart;
    private final ImageButton playPauseBtn;
    private final ConfigGroup configGroup;
    private final LeftGroup leftGroup;
    private final RightGroup rightGroup;
    private final BitmapFont font60;
    private BoomBox boomBox;
    @Getter
    @Setter
    private static boolean pause;
    private GlyphLayout glyphLayout;

    public PauseGroup() {
        this.boomBox = new BoomBox();
        pause = false;
        this.font60 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT60, BitmapFont.class);
        this.glyphLayout = new GlyphLayout();
        final TextureRegionDrawable playTexture = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.PLAY_BUTTON));
        final TextureRegionDrawable pauseTexture = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.PAUSE_BUTTON));
        this.playPauseBtn = new ImageButton(pauseTexture);
        playPauseBtn.setOrigin(Align.center);
        playPauseBtn.setSize(PaintConstants.PLAY_PAUSE_BUTTON_WIDTH, PaintConstants.PLAY_PAUSE_BUTTON_HEIGHT);
        playPauseBtn.setPosition(Rules.WORLD_WIDTH - playPauseBtn.getWidth() - 43, Rules.WORLD_HEIGHT - playPauseBtn.getHeight() - 43);

        this.configGroup = new ConfigGroup();
        this.leftGroup = LeftGroup.getInstance();
        this.rightGroup = RightGroup.getInstance();
        this.buttonMenu = new UIButton(new TextureRegionDrawable(new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.PAUSE_MENU_HOME))));
        buttonMenu.setVisible(false);
        this.buttonRestart = new UIButton(new TextureRegionDrawable(new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.PAUSE_MENU_RESTART))));
        buttonRestart.setVisible(false);

        buttonMenu.setOrigin(Align.center);
        buttonRestart.setOrigin(Align.center);

        buttonMenu.setSize(PaintConstants.MENU_BUTTON_WIDTH, PaintConstants.MENU_BUTTON_HEIGHT);
        buttonRestart.setSize(PaintConstants.MENU_BUTTON_WIDTH, PaintConstants.MENU_BUTTON_HEIGHT);

        buttonRestart.setPosition(22, 22);
        buttonMenu.setPosition(Rules.WORLD_WIDTH - buttonMenu.getWidth() - 22, 22);

        buttonMenu.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    boomBox.playSound(MSConstants.UI_MENU);
                    Vibrator.vibrate(VConstants.UI_CLICK);
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (pointer == 0) {
                    if (Rules.ADS_ENABLED) {
                        ScreenManager.getInstance().getAdsController().hideBannerAd();
                    }
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
                    Vibrator.vibrate(VConstants.UI_CLICK);
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (pointer == 0) {
                    if (Rules.ADS_ENABLED) {
                        ScreenManager.getInstance().getAdsController().hideBannerAd();
                        ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.ADS);
                    } else {
                        ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
                    }
                    Restart.doRestart();
                }
            }
        });

        playPauseBtn.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    pause = !pause;
                    if (pause) {
                        boomBox.stopAllMusicAndPlayThese(MSConstants.MENU_SONG);
                        playPauseBtn.getStyle().imageUp = playTexture;
                    } else {
                        boomBox.stopAllMusicAndPlayThese(MSConstants.GAME_SONG, MSConstants.BLACKHOLE_ROAR_MUSIC);
                        playPauseBtn.getStyle().imageUp = pauseTexture;
                    }
                    configGroup.setVisible(pause);
                    leftGroup.setVisible(pause);
                    rightGroup.setVisible(pause);
                    buttonMenu.setVisible(pause);
                    buttonRestart.setVisible(pause);
                    boomBox.stopAllSound();
                    boomBox.playSound(MSConstants.UI_PAUSE);
                    Vibrator.vibrate(VConstants.UI_CLICK);
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

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (!Rules.GAME_OVER) {
            font60.draw(
                    batch,
                    glyphLayout, Rules.WORLD_WIDTH - playPauseBtn.getWidth() / 2 - glyphLayout.width / 2 - 43,
                    Rules.WORLD_HEIGHT - 225
            );
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!Rules.GAME_OVER) {
            if (pause) {
                glyphLayout.setText(font60, "PLAY");
            } else {
                glyphLayout.setText(font60, "PAUSE");
            }
        }
    }
}