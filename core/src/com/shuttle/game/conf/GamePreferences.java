package com.shuttle.game.conf;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Shuttle on 6/19/18.
 */
public class GamePreferences {
    private static final String PREFS_NAME = "shuttleGame_preferences";

    private static final String PREF_SOUND_ENABLED = "sound.enabled";
    private static final String PREF_SOUND_VOLUME = "sound";
    private static final String PREF_EFFECTS_ENABLED = "effects.enabled";
    private static final String PREF_EFFECTS_VOLUME = "effects";
    private static final String PREF_MUSIC_ENABLED = "music.enabled";
    private static final String PREF_MUSIC_VOLUME = "music";

    private static final String PREF_SHADER_ENABLED = "shader.enabled";
    private static final String PREF_CHILD_ENABLED = "child.enabled";
    private static final String PREF_LEVEL = "level";
    private static final String PREF_VIBRATION_ENABLED = "vibration.enabled";

    private static final String PREF_JOYSTICK_ENABLED = "joystick.enabled";
    private static final String PREF_JOYSTICK_MODE = "joystick.mode";
    private static final String PREF_JOYSTICK_COLOR_ENABLED = "joystick_color.enabled";

    private static Preferences preferences;

    public static Map<String, Object> defaults = new HashMap<String, Object>() {{
        put(PREF_SOUND_ENABLED, true);
        put(PREF_SOUND_VOLUME, 0.6f);
        put(PREF_EFFECTS_ENABLED, true);
        put(PREF_EFFECTS_VOLUME, 0.7f);
        put(PREF_MUSIC_ENABLED, true);
        put(PREF_MUSIC_VOLUME, 0.8f);
        put(PREF_SHADER_ENABLED, true);
        put(PREF_CHILD_ENABLED, false);
        put(PREF_LEVEL, 0);
        put(PREF_VIBRATION_ENABLED, true);
        put(PREF_JOYSTICK_ENABLED, false);
        put(PREF_JOYSTICK_COLOR_ENABLED, true);
        put(PREF_JOYSTICK_MODE, 0);
    }};

    public static void apply(Map<String, Object> preferences) {
        getInstance().put(preferences);
        getInstance().flush();
    }

    public static Map<String, Object> getCurrent() {
        return new HashMap<String, Object>(getInstance().get());
    }

    private GamePreferences() {
    }

    private static Preferences getInstance() {
        if (preferences == null)
            preferences = Gdx.app.getPreferences(PREFS_NAME);
        return preferences;
    }

    public static boolean isSoundEnabled() {
        return getInstance().getBoolean(PREF_SOUND_ENABLED);
    }

    public static void setSoundEnabled(boolean enabled) {
        getInstance().putBoolean(PREF_SOUND_ENABLED, enabled);
        getInstance().flush();
    }

    public static float getSoundVolume() {
        return getInstance().getFloat(PREF_SOUND_VOLUME);
    }

    public static void setSoundVolume(float volume) {
        getInstance().putFloat(PREF_SOUND_VOLUME, volume);
        getInstance().flush();
    }

    public static boolean isEffectsEnabled() {
        return getInstance().getBoolean(PREF_EFFECTS_ENABLED);
    }

    public static void setEffectsEnabled(boolean enabled) {
        getInstance().putBoolean(PREF_EFFECTS_ENABLED, enabled);
        getInstance().flush();
    }

    public static float getEffectsVolume() {
        return getInstance().getFloat(PREF_EFFECTS_VOLUME);
    }

    public static void setEffectsVolume(float volume) {
        getInstance().putFloat(PREF_EFFECTS_VOLUME, volume);
        getInstance().flush();
    }

    public static boolean isMusicEnabled() {
        return getInstance().getBoolean(PREF_MUSIC_ENABLED);
    }

    public static void setMusicEnabled(boolean enabled) {
        getInstance().putBoolean(PREF_MUSIC_ENABLED, enabled);
        getInstance().flush();
    }

    public static float getMusicVolume() {
        return getInstance().getFloat(PREF_MUSIC_VOLUME);
    }

    public static void setMusicVolume(float volume) {
        getInstance().putFloat(PREF_MUSIC_VOLUME, volume);
        getInstance().flush();
    }

    public static boolean isShaderEnabled() {
        return getInstance().getBoolean(PREF_SHADER_ENABLED);
    }

    public static void setShaderEnabled(boolean enabled) {
        getInstance().putBoolean(PREF_SHADER_ENABLED, enabled);
        getInstance().flush();
    }

    public static boolean isChildEnabled() {
        return getInstance().getBoolean(PREF_CHILD_ENABLED);
    }

    public static void setChildEnabled(boolean enabled) {
        getInstance().putBoolean(PREF_CHILD_ENABLED, enabled);
        getInstance().flush();
    }

    public static int getLevel() {
        return getInstance().getInteger(PREF_LEVEL);
    }

    public static void setLevel(int level) {
        getInstance().putInteger(PREF_LEVEL, level);
        getInstance().flush();
    }

    public static boolean isVibrationEnabled() {
        return getInstance().getBoolean(PREF_VIBRATION_ENABLED);
    }

    public static void setVibrationEnabled(boolean enabled) {
        getInstance().putBoolean(PREF_VIBRATION_ENABLED, enabled);
        getInstance().flush();
    }

    public static boolean isJoyStickEnabled() {
        return getInstance().getBoolean(PREF_JOYSTICK_ENABLED);
    }

    public static void setJoyStickEnabled(boolean enabled) {
        getInstance().putBoolean(PREF_JOYSTICK_ENABLED, enabled);
        getInstance().flush();
    }

    public static int getJoyStickMode() {
        return getInstance().getInteger(PREF_JOYSTICK_MODE);
    }

    public static void setJoyStickMode(int mode) {
        getInstance().putInteger(PREF_JOYSTICK_MODE, mode);
        getInstance().flush();
    }

    public static boolean isJoyStickColorEnabled() {
        return getInstance().getBoolean(PREF_JOYSTICK_COLOR_ENABLED);
    }

    public static void setJoyStickColorEnabled(boolean enabled) {
        getInstance().putBoolean(PREF_JOYSTICK_COLOR_ENABLED, enabled);
        getInstance().flush();
    }
}