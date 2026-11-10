package com.buran.game.ui.groups;

import com.badlogic.gdx.graphics.Color;
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
import com.buran.game.conf.MSConstants;
import com.buran.game.conf.PaintConstants;
import com.buran.game.conf.Rules;
import com.buran.game.conf.VConstants;
import com.buran.game.effects.BoomBox;
import com.buran.game.effects.Oscillator;
import com.buran.game.effects.Vibrator;
import com.buran.game.screens.ScreenManager;
import com.buran.game.ui.UIButton;
import com.buran.game.ui.groups.configs.ConfigGroup;
import com.buran.game.ui.groups.configs.LeftGroup;
import com.buran.game.ui.groups.configs.RightGroup;
import com.buran.game.ui.groups.mvc.profiles.Profiles;
import com.buran.game.utils.Assets;
import com.buran.game.utils.Restart;

import lombok.Getter;
import lombok.Setter;

/**
 * @author soloyes on 7/8/18.
 */

public class PauseGroup extends Group {
    private final Button buttonMenu;
    private final Button buttonRestart;
    private final ImageButton playPauseBtn;
    private final TextureRegionDrawable playTexture;
    private final TextureRegionDrawable pauseTexture;
    private final ConfigGroup configGroup;
    private CommitGroup commitGroup;
    private final LeftGroup leftGroup;
    private final RightGroup rightGroup;
    private final BitmapFont font60;
    private BoomBox boomBox;
    @Getter
    @Setter
    private static boolean pause;
    private GlyphLayout glyphLayout;
    public static float playPauseWidth;
    public static float playPauseHeight;
    private float a;
    private Oscillator oscillatorA;
    private Color color;
    @Setter
    private static boolean watched = true;
    /** The pause group of the running game screen, for gamepad/keyboard shortcuts. */
    private static PauseGroup current;

    public PauseGroup() {
        current = this;
        this.commitGroup = new CommitGroup();
        commitGroup.setPauseGroup(this);
        this.boomBox = new BoomBox();
        this.oscillatorA = new Oscillator();
        pause = false;
        this.font60 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT60, BitmapFont.class);
        this.glyphLayout = new GlyphLayout();
        this.playTexture = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.PLAY_BUTTON));
        this.pauseTexture = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.PAUSE_BUTTON));
        this.playPauseBtn = new ImageButton(pauseTexture);
        playPauseBtn.setOrigin(Align.center);
        playPauseBtn.setSize(PaintConstants.PLAY_PAUSE_BUTTON_WIDTH, PaintConstants.PLAY_PAUSE_BUTTON_HEIGHT);
        playPauseBtn.setPosition(Rules.WORLD_WIDTH - playPauseBtn.getWidth() - Rules.SAFE_X, Rules.WORLD_HEIGHT - playPauseBtn.getHeight() - Rules.SAFE_Y);
        this.color = new Color();
        this.configGroup = new ConfigGroup();
        this.leftGroup = LeftGroup.getInstance();
        leftGroup.setConfigGroup(configGroup);
        this.rightGroup = RightGroup.getInstance();
        rightGroup.setConfigGroup(configGroup);
        this.buttonMenu = new UIButton(new TextureRegionDrawable(new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.PAUSE_MENU_HOME))));
        buttonMenu.setVisible(false);
        this.buttonRestart = new UIButton(new TextureRegionDrawable(new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.PAUSE_MENU_RESTART))));
        buttonRestart.setVisible(false);

        buttonMenu.setOrigin(Align.center);
        buttonRestart.setOrigin(Align.center);

        buttonMenu.setSize(PaintConstants.MENU_BUTTON_WIDTH, PaintConstants.MENU_BUTTON_HEIGHT);
        buttonRestart.setSize(PaintConstants.MENU_BUTTON_WIDTH, PaintConstants.MENU_BUTTON_HEIGHT);

        buttonRestart.setPosition(Rules.SAFE_X, Rules.SAFE_Y);
        buttonMenu.setPosition(Rules.WORLD_WIDTH - buttonMenu.getWidth() - Rules.SAFE_X, Rules.SAFE_Y);

        buttonMenu.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    boomBox.playSound(MSConstants.UI_MENU);
                    Vibrator.vibrateUI(VConstants.UI_CLICK);
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (pointer == 0) {
                    if (!Rules.GAME_OVER) {
                        commitVisible(true);
                        commitGroup.setDstScreen(ScreenManager.ScreenType.MENU);
                    } else {
                        if (Rules.ADS_ENABLED) {
                            ScreenManager.getInstance().getAdsController().hideBannerAd();
                        }
                        ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
                        Restart.doRestart();
                    }
                }
            }
        });

        buttonRestart.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    boomBox.playSound(MSConstants.UI_MENU);
                    Vibrator.vibrateUI(VConstants.UI_CLICK);
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (pointer == 0) {
                    if (!Rules.GAME_OVER) {
                        commitVisible(true);
                        commitGroup.setDstScreen(ScreenManager.ScreenType.GAME);
                    } else {
                        if (Rules.ADS_ENABLED) {
                            ScreenManager.getInstance().getAdsController().hideBannerAd();
                            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.ADS);
                        } else {
                            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
                        }
                        Restart.doRestart();
                    }
                }
            }
        });

        playPauseBtn.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0) {
                    doTogglePause();
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        // On TV/PC the pause is the Menu button; the on-screen button is for touch screens.
        playPauseBtn.setVisible(com.badlogic.gdx.Gdx.input.isPeripheralAvailable(com.badlogic.gdx.Input.Peripheral.MultitouchScreen));
        this.addActor(configGroup);
        this.addActor(playPauseBtn);
        this.addActor(leftGroup);
        this.addActor(rightGroup);
        this.addActor(buttonMenu);
        this.addActor(buttonRestart);
        this.addActor(commitGroup);
    }

    private void doTogglePause() {
        pause = !pause;
        if (pause) {
            boomBox.stopAllMusicAndPlayThese(MSConstants.PAUSE);
            playPauseBtn.getStyle().imageUp = playTexture;
        } else {
            if (Rules.DEATH_MODE){
                boomBox.stopAllMusicAndPlayThese(MSConstants.DEATH_MODE, MSConstants.BLACKHOLE_ROAR_MUSIC);
            } else {
                boomBox.stopAllMusicAndPlayThese(MSConstants.GAME_SONG, MSConstants.BLACKHOLE_ROAR_MUSIC);
            }
            playPauseBtn.getStyle().imageUp = pauseTexture;
            watched = true;
        }
        // The pause screen itself is drawn by ui.menu.GameMenu now.
        configGroup.setVisible(false);
        leftGroup.setVisible(false);
        rightGroup.setVisible(false);
        buttonMenu.setVisible(false);
        buttonRestart.setVisible(false);
        commitGroup.setVisible(false);
        boomBox.stopAllSound();
        boomBox.playSound(MSConstants.UI_PAUSE);
        Vibrator.vibrateUI(VConstants.UI_CLICK);
    }

    /** Same as tapping the play/pause button. No-op outside a running game. */
    public static void togglePause() {
        if (current != null && current.playPauseBtn.isVisible() && !Rules.GAME_OVER) {
            current.doTogglePause();
        }
    }

    /** True while a game screen with a pause button is alive. */
    public static boolean isActive() {
        return current != null;
    }

    /** Forget the pause group when its screen goes away. */
    public static void clearCurrent() {
        current = null;
    }

    public void commitVisible(boolean visible) {
        commitGroup.setVisible(visible);
        leftGroup.setVisible(!visible);
        rightGroup.setVisible(!visible);
        configGroup.setVisible(!visible);
        buttonMenu.setVisible(!visible);
        buttonRestart.setVisible(!visible);
    }

    public void setGameOver() {
        playPauseBtn.setVisible(false);
        configGroup.setVisible(false);
        leftGroup.setVisible(false);
        rightGroup.setVisible(false);
        buttonMenu.setVisible(false);
        buttonRestart.setVisible(false);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color tmpColor = playPauseBtn.getColor();
        color.set(tmpColor);
        color.a = a;
        super.draw(batch, parentAlpha);
        playPauseBtn.setColor(color);
        if (!Rules.GAME_OVER && playPauseBtn.isVisible()) {
            font60.draw(
                    batch,
                    glyphLayout, Rules.WORLD_WIDTH - playPauseBtn.getWidth() / 2 - glyphLayout.width / 2 - Rules.SAFE_X,
                    Rules.WORLD_HEIGHT - Rules.SAFE_Y + 43 - 225
            );
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!Rules.GAME_OVER) {
            if (Profiles.getSettings().isNotification() && !pause && !watched) {
                a = oscillatorA.oscillate(a, 1.0f, 0.3f, 1.0f, delta);
            } else {
                a = 1.0f;
            }
            if (pause) {
                glyphLayout.setText(font60, "PLAY");
                playPauseWidth = glyphLayout.width;
                playPauseHeight = glyphLayout.height;
            } else {
                glyphLayout.setText(font60, "PAUSE");
            }
        }
    }
}