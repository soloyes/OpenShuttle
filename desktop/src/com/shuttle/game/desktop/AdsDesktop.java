package com.shuttle.game.desktop;

import com.shuttle.game.AdsController;

public class AdsDesktop implements AdsController {
    @Override
    public boolean isInterstitialLoaded() {
        return false;
    }

    @Override
    public void showInterstitialAd() {

    }
}
