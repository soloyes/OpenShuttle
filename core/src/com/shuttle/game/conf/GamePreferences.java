package com.shuttle.game.conf;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * @author Shuttle on 6/19/18.
 */
public class GamePreferences {
    private static final String PREF_MUSIC_VOLUME = "volume";
    private static final String PREF_MUSIC_ENABLED = "music.enabled";
    private static final String PREF_SOUND_ENABLED = "sound.enabled";
    private static final String PREF_JOYSTICK_ENABLED = "joystick.enabled";
    private static final String PREF_SOUND_VOLUME = "sound";
    private static final String PREFS_NAME = "shuttleGame_preferences";
    private static final String PREF_LEVEL = "level";
    private Preferences preferences;

    public GamePreferences() {
        if (!getPrefs().contains(PREF_MUSIC_VOLUME)) {
            setMusicVolume(0.5f);
        }
        if (!getPrefs().contains(PREF_SOUND_VOLUME)) {
            setSoundVolume(0.5f);
        }
        if (!getPrefs().contains(PREF_MUSIC_ENABLED)) {
            setMusicEnabled(true);
        }
        if (!getPrefs().contains(PREF_SOUND_ENABLED)) {
            setSoundEnabled(true);
        }
        if (!getPrefs().contains(PREF_JOYSTICK_ENABLED)) {
            setSoundEnabled(true);
        }
        if (!getPrefs().contains(PREF_LEVEL)) {
            setLevel(1);
        }
    }

    private Preferences getPrefs() {
        if (preferences == null)
            preferences = Gdx.app.getPreferences(PREFS_NAME);
        return preferences;
    }

    public int getLevel() {
        return getPrefs().getInteger(PREF_LEVEL);
    }

    public void setLevel(int level) {
        getPrefs().putInteger(PREF_LEVEL, level);
        getPrefs().flush();
    }

    public float getMusicVolume() {
        return getPrefs().getFloat(PREF_MUSIC_VOLUME);
    }

    public void setMusicVolume(float volume) {
        getPrefs().putFloat(PREF_MUSIC_VOLUME, volume);
        getPrefs().flush();
    }

    public float getSoundVolume() {
        return getPrefs().getFloat(PREF_SOUND_VOLUME);
    }

    public void setSoundVolume(float volume) {
        getPrefs().putFloat(PREF_SOUND_VOLUME, volume);
        getPrefs().flush();
    }

    public boolean isJoyStickEnabled() {
        return getPrefs().getBoolean(PREF_JOYSTICK_ENABLED);
    }

    public void setJoyStickEnabled(boolean joyStickEnabled) {
        getPrefs().putBoolean(PREF_JOYSTICK_ENABLED, joyStickEnabled);
        getPrefs().flush();
    }

    public boolean isMusicEnabled() {
        return getPrefs().getBoolean(PREF_MUSIC_ENABLED);
    }

    public void setMusicEnabled(boolean musicEnabled) {
        getPrefs().putBoolean(PREF_MUSIC_ENABLED, musicEnabled);
        getPrefs().flush();
    }

    public boolean isSoundEnabled() {
        return getPrefs().getBoolean(PREF_SOUND_ENABLED);
    }

    public void setSoundEnabled(boolean soundEffectsEnabled) {
        getPrefs().putBoolean(PREF_SOUND_ENABLED, soundEffectsEnabled);
        getPrefs().flush();
    }
}
