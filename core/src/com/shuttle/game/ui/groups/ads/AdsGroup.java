package com.shuttle.game.ui.groups.ads;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.conf.VConstants;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.effects.Oscillator;
import com.shuttle.game.effects.Vibrator;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.ui.UIButton;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.RadialSprite;

public class AdsGroup extends Group {
    private Button buttonContinue;
    private Button buttonGetFull;
    private TextureRegion shuttle;
    private TextureRegion shield;
    private TextureRegion astronaut;
    private float shuttleX, shuttleY;
    private float shieldX, shieldY;
    private float shieldScale;
    private Oscillator shieldScaleOscillator;
    private Oscillator shuttleYOscillator;
    private BoomBox boomBox;
    private RadialSprite timerBox;
    private float counter;
    private float angle;
    private int r;

    public AdsGroup() {
        this.timerBox = new RadialSprite(Assets.getInstance().getAtlas().findRegion(PaintConstants.TIMER_BOX));
        this.counter = Rules.ADS_TIME;
        this.boomBox = new BoomBox();
        this.shieldScaleOscillator = new Oscillator();
        this.shieldScale = 1.0f;
        this.shuttleYOscillator = new Oscillator();
        this.shuttleX = Rules.WORLD_WIDTH / 2 - PaintConstants.UI_MENU_BACKGROUND_SHUTTLE_WIDTH / 2;
        this.shuttleY = Rules.WORLD_HEIGHT / 2 - PaintConstants.UI_MENU_BACKGROUND_SHUTTLE_HEIGHT / 2;
        this.shieldX = Rules.WORLD_WIDTH / 2 - PaintConstants.UI_MENU_BACKGROUND_SHIELD_WIDTH / 2;
        this.shieldY = Rules.WORLD_HEIGHT / 2 - PaintConstants.UI_MENU_BACKGROUND_SHIELD_HEIGHT / 2;

        this.shuttle = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_MENU_BACKGROUND_SHUTTLE));
        this.shield = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_MENU_BACKGROUND_SHIELD));
        this.astronaut = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_MENU_BACKGROUND_ASTRONAUT));

        this.buttonContinue = new UIButton(new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.CONTINUE_BUTTON)));
        this.buttonContinue.setSize(PaintConstants.MENU_BUTTON_WIDTH, PaintConstants.MENU_BUTTON_HEIGHT);
        buttonContinue.setPosition(Rules.WORLD_WIDTH / 2 - PaintConstants.MENU_BUTTON_WIDTH / 2, 24);
        buttonContinue.addListener(new ClickListener() {
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
                    ScreenManager.getInstance().getAdsController().hideBannerAd();
                    ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
                }
            }
        });

        this.buttonGetFull = new UIButton(new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.GET_FULL_BUTTON)));
        this.buttonGetFull.setSize(PaintConstants.GET_FULL_BUTTON_WIDTH, PaintConstants.GET_FULL_BUTTON_HEIGHT);
        buttonGetFull.setPosition(Rules.WORLD_WIDTH / 2 - PaintConstants.GET_FULL_BUTTON_WIDTH / 2, Rules.WORLD_HEIGHT / 4 - PaintConstants.GET_FULL_BUTTON_HEIGHT / 2);

        buttonGetFull.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (pointer == 0) {
                    Vibrator.vibrate(VConstants.UI_CLICK);
                    Gdx.net.openURI(Rules.PAY_US);
                }
            }
        });

        this.r = MathUtils.random(0, 1);

        this.addActor(buttonContinue);
        this.addActor(buttonGetFull);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        shieldScale = shieldScaleOscillator.oscillate(shieldScale, 0.09f, 0.95f, 1.0f, delta);
        shuttleY = shuttleYOscillator.oscillate(
                shuttleY, 12.0f,
                Rules.WORLD_HEIGHT / 2 - PaintConstants.UI_MENU_BACKGROUND_SHUTTLE_HEIGHT / 2,
                Rules.WORLD_HEIGHT / 2 - PaintConstants.UI_MENU_BACKGROUND_SHUTTLE_HEIGHT / 2 + 15,
                delta
        );

        counter -= delta;

        if (counter <= 0.0f) {
            buttonContinue.setVisible(true);
        } else {
            buttonContinue.setVisible(false);
        }

        angle = -360.0f * counter / Rules.ADS_TIME;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.setColor(Color.WHITE);
        if (r == 0) {
            batch.draw(shuttle,
                    shuttleX, shuttleY,
                    PaintConstants.UI_MENU_BACKGROUND_SHUTTLE_WIDTH, PaintConstants.UI_MENU_BACKGROUND_SHUTTLE_HEIGHT
            );
            batch.draw(shield,
                    shieldX, shieldY,
                    PaintConstants.UI_MENU_BACKGROUND_SHIELD_WIDTH / 2, PaintConstants.UI_MENU_BACKGROUND_SHIELD_HEIGHT / 2,
                    PaintConstants.UI_MENU_BACKGROUND_SHIELD_WIDTH, PaintConstants.UI_MENU_BACKGROUND_SHIELD_HEIGHT,
                    shieldScale, shieldScale,
                    0.0f
            );
        } else if (r == 1) {
            batch.draw(astronaut,
                    shieldX, shieldY,
                    PaintConstants.UI_MENU_BACKGROUND_SHIELD_WIDTH / 2, PaintConstants.UI_MENU_BACKGROUND_SHIELD_HEIGHT / 2,
                    PaintConstants.UI_MENU_BACKGROUND_SHIELD_WIDTH, PaintConstants.UI_MENU_BACKGROUND_SHIELD_HEIGHT,
                    shieldScale, shieldScale,
                    0.0f
            );
        }
        if (counter >= 0.0f) {
            timerBox.draw((SpriteBatch) batch, buttonContinue.getX() + PaintConstants.MENU_BUTTON_WIDTH / 2 - PaintConstants.TIMER_WIDTH, buttonContinue.getY() + PaintConstants.MENU_BUTTON_HEIGHT / 2 - PaintConstants.TIMER_HEIGHT,
                    PaintConstants.TIMER_WIDTH * 2, PaintConstants.TIMER_HEIGHT * 2,
                    angle
            );
        }
    }
}