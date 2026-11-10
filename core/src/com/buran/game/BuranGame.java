package com.buran.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.buran.game.input.Gamepad;
import com.buran.game.screens.ScreenManager;
import com.buran.game.ui.groups.mvc.profiles.Profiles;

import lombok.Getter;

/**
 * @author soloyes on 03/06/18.
 */

public class BuranGame extends Game {
    private SpriteBatch batch;
    private ScreenManager.ScreenType screenType;
    @Getter
    private static Share share;
    private boolean mobile;
    private int width;
    private int height;

    public BuranGame(AdsController adsController, Share share, boolean mobile, int width, int height) {
        BuranGame.share = share;
        this.mobile = mobile;
        this.width = width;
        this.height = height;
        ScreenManager.getInstance().setAdsController(adsController);
    }

    @Override
    public void create() {
        this.batch = new SpriteBatch();
        ScreenManager.getInstance().init(this, batch, mobile, width, height);
        if (screenType == null) {
            screenType = ScreenManager.ScreenType.MENU;
        }
        Profiles profiles = new Profiles();
        if (profiles.getActive() == null) {
            screenType = ScreenManager.ScreenType.MENU;
        }
        ScreenManager.getInstance().changeScreen(screenType);
    }

    @Override
    public void dispose() {
        batch.dispose();
        getScreen().dispose();
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        Gamepad.get().update(dt);
        getScreen().render(dt);
        Gamepad.get().render(batch);
    }

    public void setRunScreen(ScreenManager.ScreenType screenType) {
        this.screenType = screenType;
    }
}