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
    private static final String EFFECTS_LEVEL = "effects";
    private static Preferences preferences;

    static {
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
        if (!getPrefs().contains(EFFECTS_LEVEL)) {
            setLevel(1);
        }
    }

    public static int getEffectsLevel() {
        return getPrefs().getInteger(EFFECTS_LEVEL);
    }

    public static void setEffectsLevel(int level) {
        getPrefs().putInteger(EFFECTS_LEVEL, level);
        getPrefs().flush();
    }

    private static Preferences getPrefs() {
        if (preferences == null)
            preferences = Gdx.app.getPreferences(PREFS_NAME);
        return preferences;
    }

    public static int getLevel() {
        return getPrefs().getInteger(PREF_LEVEL);
    }

    public static void setLevel(int level) {
        getPrefs().putInteger(PREF_LEVEL, level);
        getPrefs().flush();
    }

    public static float getMusicVolume() {
        return getPrefs().getFloat(PREF_MUSIC_VOLUME);
    }

    public static void setMusicVolume(float volume) {
        getPrefs().putFloat(PREF_MUSIC_VOLUME, volume);
        getPrefs().flush();
    }

    public static float getSoundVolume() {
        return getPrefs().getFloat(PREF_SOUND_VOLUME);
    }

    public static void setSoundVolume(float volume) {
        getPrefs().putFloat(PREF_SOUND_VOLUME, volume);
        getPrefs().flush();
    }

    public static boolean isJoyStickEnabled() {
        return getPrefs().getBoolean(PREF_JOYSTICK_ENABLED);
    }

    public static void setJoyStickEnabled(boolean joyStickEnabled) {
        getPrefs().putBoolean(PREF_JOYSTICK_ENABLED, joyStickEnabled);
        getPrefs().flush();
    }

    public static boolean isMusicEnabled() {
        return getPrefs().getBoolean(PREF_MUSIC_ENABLED);
    }

    public static void setMusicEnabled(boolean musicEnabled) {
        getPrefs().putBoolean(PREF_MUSIC_ENABLED, musicEnabled);
        getPrefs().flush();
    }

    public static boolean isSoundEnabled() {
        return getPrefs().getBoolean(PREF_SOUND_ENABLED);
    }

    public static void setSoundEnabled(boolean soundEffectsEnabled) {
        getPrefs().putBoolean(PREF_SOUND_ENABLED, soundEffectsEnabled);
        getPrefs().flush();
    }

    public enum EffectLevel {
        LOW(0), MEDIUM(1), HIGH(2);

        private int value;

        EffectLevel(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
