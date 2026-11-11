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

    @JSBody(script = "return location.search || '';")
    private static native String query();

    @JSBody(script = "var b = document.getElementById('boot'); return !b || b.classList.contains('hidden');")
    private static native boolean bootHidden();

    @JSBody(params = "p", script = "window.__introPending = p;")
    private static native void setIntroPending(boolean p);

    public static void main(String[] args) {
        com.buran.game.conf.Rules.DEBUG_START_AT_HOLE = query().contains("debug=hole");
        com.buran.game.conf.Rules.DEBUG_FAST_GOAL = query().contains("debug=clear");
        com.buran.game.conf.Rules.DEBUG_UNLOCK = query().contains("unlock");
        com.buran.game.conf.Rules.DEBUG_THIRD = query().contains("third");
        com.buran.game.conf.Rules.DEBUG_CARD = query().contains("card");
        com.buran.game.conf.Rules.DEBUG_RAM = query().contains("ram");
        com.buran.game.conf.Rules.DEBUG_GOD = query().contains("god");
        com.buran.game.conf.Rules.DEBUG_CENSUS = query().contains("census");
        com.buran.game.conf.Rules.DEBUG_BOSS = query().contains("boss");
        com.buran.game.conf.Rules.DEBUG_BOT = query().contains("bot");
        java.util.regex.Matcher sp = java.util.regex.Pattern.compile("speed=(\\d+)").matcher(query());
        com.buran.game.conf.Rules.DEBUG_SPEED = sp.find() ? Math.max(1, Math.min(8, Integer.parseInt(sp.group(1)))) : 1;
        java.util.regex.Matcher sm = java.util.regex.Pattern.compile("sector=(\\d+)").matcher(query());
        com.buran.game.conf.Rules.DEBUG_SECTOR = sm.find() ? Integer.parseInt(sm.group(1)) : 0;
        com.buran.game.conf.Rules.DEBUG_CHAPTER = query().contains("chapter=3") ? 3 : query().contains("chapter=2") ? 2
                : query().contains("chapter") ? 1 : 0;
        com.buran.game.platform.Shell.bridge = new com.buran.game.platform.Shell.Bridge() {
            @Override
            public boolean shown() {
                return bootHidden();
            }

            @Override
            public void introPending(boolean pending) {
                setIntroPending(pending);
            }
        };
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
