package com.buran.game.ui.groups;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.buran.game.conf.MSConstants;
import com.buran.game.conf.PaintConstants;
import com.buran.game.conf.Rules;
import com.buran.game.conf.VConstants;
import com.buran.game.effects.BoomBox;
import com.buran.game.effects.Vibrator;
import com.buran.game.screens.ScreenManager;
import com.buran.game.ui.UIButton;
import com.buran.game.utils.Assets;
import com.buran.game.utils.Restart;

import lombok.Setter;

/**
 * @author soloyes on 07/12/20.
 */

public class CommitGroup extends Group {

    private final UIButton buttonRestart;
    private final UIButton buttonMenu;
    private final UIButton buttonBack;
    private final TextureRegion textureRegion;
    private final BitmapFont font72;
    private Label sureLabel;
    private ScreenManager.ScreenType dstScreen;
    private BoomBox boomBox;
    @Setter
    private PauseGroup pauseGroup;

    public CommitGroup() {
        this.setName(GroupType.COMMIT.name());
        setVisible(false);
        this.dstScreen = ScreenManager.ScreenType.MENU;
        this.boomBox = new BoomBox();
        this.font72 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT72, BitmapFont.class);
        Label.LabelStyle noticeStyle72 = new Label.LabelStyle(
                Assets.getInstance().getAssetManager().get(PaintConstants.FONT72, BitmapFont.class),
                new Color(0.8f, 0.43f, 0.33f, 1f)
        );
        sureLabel = new Label("Are u sure?", noticeStyle72);
        sureLabel.setPosition(Rules.WORLD_WIDTH / 2 - sureLabel.getWidth() / 2, Rules.WORLD_HEIGHT / 3 + 5);
        this.textureRegion = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_INFO_CELL));

        this.buttonRestart = new UIButton(new TextureRegionDrawable(new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.PAUSE_MENU_RESTART))));
        buttonRestart.setPosition(
                Rules.WORLD_WIDTH / 2 - PaintConstants.UI_MENU_CELL_WIDTH / 3,
                Rules.WORLD_HEIGHT / 2 - PaintConstants.UI_CELL_HEIGHT / 2 - PaintConstants.MENU_BUTTON_HEIGHT - 48
        );
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
        this.buttonMenu = new UIButton(new TextureRegionDrawable(new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.PAUSE_MENU_HOME))));
        buttonMenu.setPosition(
                Rules.WORLD_WIDTH / 2 - PaintConstants.UI_MENU_CELL_WIDTH / 3,
                Rules.WORLD_HEIGHT / 2 - PaintConstants.UI_CELL_HEIGHT / 2 - PaintConstants.MENU_BUTTON_HEIGHT - 48
        );
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
                    if (Rules.ADS_ENABLED) {
                        ScreenManager.getInstance().getAdsController().hideBannerAd();
                    }
                    ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
                    Restart.doRestart();
                }
            }
        });
        this.buttonBack = new UIButton(new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.BACK_BUTTON)));
        buttonBack.setPosition(
                Rules.WORLD_WIDTH / 2 + PaintConstants.UI_MENU_CELL_WIDTH / 3 - PaintConstants.MENU_BUTTON_WIDTH,
                Rules.WORLD_HEIGHT / 2 - PaintConstants.UI_CELL_HEIGHT / 2 - PaintConstants.MENU_BUTTON_HEIGHT - 48
        );
        buttonBack.addListener(new ClickListener() {
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
                    pauseGroup.commitVisible(false);
                }
            }
        });
        this.addActor(buttonRestart);
        this.addActor(buttonMenu);
        this.addActor(buttonBack);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(textureRegion, Rules.WORLD_WIDTH / 2 - PaintConstants.UI_MENU_CELL_WIDTH / 2, Rules.WORLD_HEIGHT / 3);
        sureLabel.draw(batch, parentAlpha);
        switch (dstScreen) {
            case MENU:
                buttonRestart.setVisible(false);
                buttonMenu.setVisible(true);
                break;
            default:
                buttonRestart.setVisible(true);
                buttonMenu.setVisible(false);
        }
        super.draw(batch, parentAlpha);
    }

    public void setDstScreen(ScreenManager.ScreenType dstScreen) {
        this.dstScreen = dstScreen;
    }
}