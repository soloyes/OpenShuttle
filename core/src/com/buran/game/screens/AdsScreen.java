package com.buran.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by soloyes on 04/11/20.
 */

public class AdsScreen extends BaseScreen implements Screen {

    private com.buran.game.ui.groups.ads.AdsGroup adsGroup;

    public AdsScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void show() {
        super.show();
        ScreenManager.getInstance().getAdsController().showBannerAd();
    }

    @Override
    public void createGUI() {
        this.adsGroup = new com.buran.game.ui.groups.ads.AdsGroup();
        this.stage.addActor(adsGroup);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        stage.draw();
    }

    private void update(float delta) {
        stage.act();
    }
}