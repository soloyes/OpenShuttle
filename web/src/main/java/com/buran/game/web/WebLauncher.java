package com.buran.game.web;

import com.buran.game.AdsController;
import com.buran.game.BuranGame;
import com.buran.game.Share;
import com.github.xpenatan.gdx.teavm.backends.web.WebApplication;
import com.github.xpenatan.gdx.teavm.backends.web.WebApplicationConfiguration;

/**
 * Browser entry point (Edge on Xbox, or any desktop browser).
 */
public class WebLauncher {

    public static void main(String[] args) {
        WebApplicationConfiguration config = new WebApplicationConfiguration("canvas");
        // 0 x 0 = follow the browser window size.
        config.width = 0;
        config.height = 0;
        config.showDownloadLogs = false;
        config.antialiasing = false;

        AdsController noAds = new AdsController() {
            @Override
            public void loadBannerAd() {
            }

            @Override
            public void showBannerAd(boolean isTop) {
            }

            @Override
            public void hideBannerAd() {
            }
        };
        Share share = new Share() {
            @Override
            public void share(String text) {
                System.out.println(text);
            }
        };
        // "mobile" here means: derive the world size from the real canvas size.
        new WebApplication(new BuranGame(noAds, share, true, 0, 0), config);
    }
}
