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
    private static final String PREF_JOYSTICK_MODE = "0";
    private static final String PREF_JOYSTICK_COLOR = "joystick_color.enabled";
    private static final String PREF_CHILD_ENABLED = "child.enabled";
    private static final String PREF_SOUND_VOLUME = "sound";
    private static final String PREFS_NAME = "shuttleGame_preferences";
    private static final String PREF_LEVEL = "level";
    private static final String PREF_EFFECTS_LEVEL = "effects";
    private static final String PREF_VIBRATION_ENABLED = "vibration.enabled";
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
        if (!getPrefs().contains(PREF_JOYSTICK_MODE)) {
            setJoyStickMode(0);
        }
        if (!getPrefs().contains(PREF_JOYSTICK_COLOR)) {
            setJoyStickColor(false);
        }
        if (!getPrefs().contains(PREF_CHILD_ENABLED)) {
            setSoundEnabled(true);
        }
        if (!getPrefs().contains(PREF_LEVEL)) {
            setLevel(1);
        }
        if (!getPrefs().contains(PREF_EFFECTS_LEVEL)) {
            setLevel(1);
        }
        if (!getPrefs().contains(PREF_VIBRATION_ENABLED)) {
            setVibrationEnabled(true);
        }
    }

    private GamePreferences() {
    }

    public static int getJoyStickMode() {
        return getPrefs().getInteger(PREF_JOYSTICK_MODE);
    }

    public static void setJoyStickMode(int joystickMode) {
        getPrefs().putInteger(PREF_JOYSTICK_MODE, joystickMode);
        getPrefs().flush();
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

    public static int getPrefEffectsLevel() {
        return getPrefs().getInteger(PREF_EFFECTS_LEVEL);
    }

    public static void setPrefEffectsLevel(int level) {
        getPrefs().putInteger(PREF_EFFECTS_LEVEL, level);
        getPrefs().flush();
    }

    private static Preferences getPrefs() {
        if (preferences == null)
            preferences = Gdx.app.getPreferences(PREFS_NAME);
        return preferences;
    }

    public static float getSoundVolume() {
        return getPrefs().getFloat(PREF_SOUND_VOLUME);
    }

    public static void setSoundVolume(float volume) {
        getPrefs().putFloat(PREF_SOUND_VOLUME, volume);
        getPrefs().flush();
    }

    public static boolean isChildEnabled() {
        return getPrefs().getBoolean(PREF_CHILD_ENABLED);
    }

    public static void setChildEnabled(boolean childEnabled) {
        getPrefs().putBoolean(PREF_CHILD_ENABLED, childEnabled);
        getPrefs().flush();
    }

    public static boolean isJoyStickColorEnabled() {
        return getPrefs().getBoolean(PREF_JOYSTICK_COLOR);
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

    public static boolean isVibrationEnabled() {
        return getPrefs().getBoolean(PREF_VIBRATION_ENABLED);
    }

    public static void setVibrationEnabled(boolean vibrationEnabled) {
        getPrefs().putBoolean(PREF_VIBRATION_ENABLED, vibrationEnabled);
        getPrefs().flush();
    }

    public static void setJoyStickColor(boolean joystickColor) {
        getPrefs().putBoolean(PREF_JOYSTICK_COLOR, joystickColor);
        getPrefs().flush();
    }

    public enum EffectLevel {
        LOW(0), HIGH(1);
        private int value;

        EffectLevel(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
