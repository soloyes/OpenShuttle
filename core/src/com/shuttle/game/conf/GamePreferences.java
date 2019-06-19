package com.shuttle.game.conf;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Shuttle on 6/19/18.
 */
public class GamePreferences {
    private static final String PREF_MUSIC_VOLUME = "volume";
    private static final String PREF_MUSIC_ENABLED = "music.enabled";
    private static final String PREF_SOUND_ENABLED = "sound.enabled";
    private static final String PREF_JOYSTICK_ENABLED = "joystick.enabled";
    private static final String PREF_JOYSTICK_MODE = "joystick.mode";
    private static final String PREF_JOYSTICK_COLOR = "joystick_color.enabled";
    private static final String PREF_CHILD_ENABLED = "child.enabled";
    private static final String PREF_SOUND_VOLUME = "sound";
    private static final String PREFS_NAME = "shuttleGame_preferences";
    private static final String PREF_LEVEL = "level";
    private static final String PREF_EFFECTS_LEVEL = "effects";
    private static final String PREF_VIBRATION_ENABLED = "vibration.enabled";
    private static Preferences preferences;

    public static Map<String, Object> defaults = new HashMap<String, Object>() {{
        put(PREF_MUSIC_ENABLED, true);
        put(PREF_MUSIC_VOLUME, 0.5f);
        put(PREF_SOUND_ENABLED, true);
        put(PREF_SOUND_VOLUME, 0.5f);
        put(PREF_JOYSTICK_ENABLED, false);
        put(PREF_JOYSTICK_COLOR, true);
        put(PREF_JOYSTICK_MODE, 0);
        put(PREF_VIBRATION_ENABLED, true);
        put(PREF_LEVEL, 0);
        put(PREF_CHILD_ENABLED, false);
        put(PREF_EFFECTS_LEVEL, 1);
    }};

    public static void apply(Map<String, Object> preferences) {
        getInstance().put(preferences);
        getInstance().flush();
    }

    private GamePreferences() {
    }

    public static int getJoyStickMode() {
        return getInstance().getInteger(PREF_JOYSTICK_MODE);
    }

    public static void setJoyStickMode(int joystickMode) {
        getInstance().putInteger(PREF_JOYSTICK_MODE, joystickMode);
        getInstance().flush();
    }

    public static int getLevel() {
        return getInstance().getInteger(PREF_LEVEL);
    }

    public static void setLevel(int level) {
        getInstance().putInteger(PREF_LEVEL, level);
        getInstance().flush();
    }

    public static float getMusicVolume() {
        return getInstance().getFloat(PREF_MUSIC_VOLUME);
    }

    public static void setMusicVolume(float volume) {
        getInstance().putFloat(PREF_MUSIC_VOLUME, volume);
        getInstance().flush();
    }

    public static int getPrefEffectsLevel() {
        return getInstance().getInteger(PREF_EFFECTS_LEVEL);
    }

    public static void setPrefEffectsLevel(int level) {
        getInstance().putInteger(PREF_EFFECTS_LEVEL, level);
        getInstance().flush();
    }

    private static Preferences getInstance() {
        if (preferences == null)
            preferences = Gdx.app.getPreferences(PREFS_NAME);
        return preferences;
    }

    public static float getSoundVolume() {
        return getInstance().getFloat(PREF_SOUND_VOLUME);
    }

    public static void setSoundVolume(float volume) {
        getInstance().putFloat(PREF_SOUND_VOLUME, volume);
        getInstance().flush();
    }

    public static boolean isChildEnabled() {
        return getInstance().getBoolean(PREF_CHILD_ENABLED);
    }

    public static void setChildEnabled(boolean childEnabled) {
        getInstance().putBoolean(PREF_CHILD_ENABLED, childEnabled);
        getInstance().flush();
    }

    public static boolean isJoyStickColorEnabled() {
        return getInstance().getBoolean(PREF_JOYSTICK_COLOR);
    }

    public static boolean isJoyStickEnabled() {
        return getInstance().getBoolean(PREF_JOYSTICK_ENABLED);
    }

    public static void setJoyStickEnabled(boolean joyStickEnabled) {
        getInstance().putBoolean(PREF_JOYSTICK_ENABLED, joyStickEnabled);
        getInstance().flush();
    }

    public static boolean isMusicEnabled() {
        return getInstance().getBoolean(PREF_MUSIC_ENABLED);
    }

    public static void setMusicEnabled(boolean musicEnabled) {
        getInstance().putBoolean(PREF_MUSIC_ENABLED, musicEnabled);
        getInstance().flush();
    }

    public static boolean isSoundEnabled() {
        return getInstance().getBoolean(PREF_SOUND_ENABLED);
    }

    public static void setSoundEnabled(boolean soundEffectsEnabled) {
        getInstance().putBoolean(PREF_SOUND_ENABLED, soundEffectsEnabled);
        getInstance().flush();
    }

    public static boolean isVibrationEnabled() {
        return getInstance().getBoolean(PREF_VIBRATION_ENABLED);
    }

    public static void setVibrationEnabled(boolean vibrationEnabled) {
        getInstance().putBoolean(PREF_VIBRATION_ENABLED, vibrationEnabled);
        getInstance().flush();
    }

    public static void setJoyStickColor(boolean joystickColor) {
        getInstance().putBoolean(PREF_JOYSTICK_COLOR, joystickColor);
        getInstance().flush();
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
