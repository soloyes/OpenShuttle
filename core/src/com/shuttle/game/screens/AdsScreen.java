package com.shuttle.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shuttle.game.ui.groups.ads.AdsGroup;

/**
 * Created by Shuttle on 04.11.2020.
 */

public class AdsScreen extends BaseScreen implements Screen {

    private AdsGroup adsGroup;

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
        this.adsGroup = new AdsGroup();
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