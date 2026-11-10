package com.buran.game.web;

import com.buran.game.AdsController;
import com.buran.game.BuranGame;
import com.buran.game.Share;
import com.buran.game.effects.BoomBox;
import com.buran.game.input.Gamepad;
import com.buran.game.screens.LoadingScreen;
import com.github.xpenatan.gdx.teavm.backends.web.WebApplication;
import com.github.xpenatan.gdx.teavm.backends.web.WebApplicationConfiguration;

import org.teavm.jso.JSBody;

/**
 * Browser entry point (Edge on Xbox, or any desktop browser).
 * The page (web/webapp/index.html) shows its own boot screen; the game tells it when it is
 * ready and waits for the player to dismiss it before taking gamepad input.
 */
public class WebLauncher {

    @JSBody(script = "if (window.onGameReady) window.onGameReady();")
    private static native void notifyReady();

    @JSBody(script = "return !!window.__gameStarted;")
    private static native boolean pageStarted();

    @JSBody(script = "return !!(window.Howler && Howler.ctx && Howler.ctx.state === 'running');")
    private static native boolean audioRunning();

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
        BuranGame game = new BuranGame(noAds, share, true, 0, 0) {
            private boolean announced;
            private boolean started;
            private boolean musicReplayed;

            @Override
            public void render() {
                boolean nowStarted = pageStarted();
                Gamepad.get().setEnabled(nowStarted);
                started = nowStarted;
                // Music started before the first user gesture was swallowed by the browser.
                // Replay it once the audio context is actually running (resume is async).
                if (started && !musicReplayed && audioRunning()) {
                    musicReplayed = true;
                    BoomBox.replayLast();
                }
                super.render();
                if (!announced && getScreen() != null && !(getScreen() instanceof LoadingScreen)) {
                    announced = true;
                    notifyReady();
                }
            }
        };
        new WebApplication(game, config);
    }
}
