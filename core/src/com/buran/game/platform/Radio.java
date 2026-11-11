package com.buran.game.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.buran.game.effects.BoomBox;
import com.buran.game.ui.groups.mvc.profiles.Profiles;
import com.buran.game.ui.groups.mvc.profiles.SettingsDAO;

/**
 * Radio transmissions: pre-rendered voice lines (assets/sound/radio/&lt;id&gt;.mp3, made by
 * tools/vo/render_radio.py) with the songs ducked underneath while the channel is open.
 * The voice follows the effects switch; text on screen is controlled by the SUBTITLES setting.
 */
public final class Radio {
    private static final float DUCK = 0.35f;
    private static Music current;
    private static boolean paused;
    /** Web audio starts asynchronously: the line counts as running only once heard playing. */
    private static boolean started;
    private static long openedAt;
    private static boolean fixed;

    private Radio() {
    }

    public static void play(String id) {
        play(id, -1f);
    }

    /**
     * A line at a fixed volume (0..1), outside the settings and without ducking the songs: the
     * intro has its own mix. A negative volume means the usual radio behaviour.
     */
    public static void play(String id, float volume) {
        stop();
        SettingsDAO s = Profiles.getSettings();
        fixed = volume >= 0f;
        if (!fixed && !s.isEffects()) {
            return;
        }
        FileHandle f = Gdx.files.internal("sound/radio/" + id + ".mp3");
        if (!f.exists()) {
            return;
        }
        try {
            current = Gdx.audio.newMusic(f);
        } catch (RuntimeException e) {
            Gdx.app.error("Radio", "cannot open " + f.path(), e);
            current = null;
            return;
        }
        current.setVolume(fixed ? volume : Math.max(0.4f, Math.min(1f, s.overallEffectsVolume() * 1.8f)));
        current.play();
        paused = false;
        started = false;
        openedAt = com.badlogic.gdx.utils.TimeUtils.millis();
        duck(true);
    }

    /** True while a transmission is being heard. */
    public static boolean isPlaying() {
        return current != null && (current.isPlaying() || paused || !started);
    }

    /** Call every frame: closes the channel when the line ends. */
    public static void update() {
        if (current == null || paused) {
            return;
        }
        if (current.isPlaying()) {
            started = true;
        } else if (started || com.badlogic.gdx.utils.TimeUtils.timeSinceMillis(openedAt) > 4000) {
            // Finished, or never managed to start.
            stop();
        }
    }

    public static void setPaused(boolean p) {
        if (current == null || p == paused) {
            return;
        }
        paused = p;
        if (p) {
            current.pause();
        } else {
            current.play();
        }
    }

    public static void stop() {
        if (current != null) {
            current.stop();
            current.dispose();
            current = null;
            paused = false;
            duck(false);
        }
    }

    private static void duck(boolean on) {
        if (fixed) {
            return;
        }
        SettingsDAO s = Profiles.getSettings();
        if (s.isMusic()) {
            new BoomBox().setAllMusicVolume(s.overallMusicVolume() * (on ? DUCK : 1f));
        }
    }
}
