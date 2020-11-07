package com.shuttle.game.ui.groups.mvc.profiles;

import lombok.Getter;
import lombok.Setter;

public class SettingsDAO {
    @Getter
    @Setter
    private boolean notification;
    @Getter
    @Setter
    private float soundVolume;
    @Getter
    @Setter
    private boolean effects;
    @Getter
    @Setter
    private float effectsVolume;
    @Getter
    @Setter
    private boolean music;
    @Getter
    @Setter
    private float musicVolume;

    @Getter
    @Setter
    private boolean shader;
    @Getter
    @Setter
    private boolean child;
    @Getter
    @Setter
    private int level;
    @Getter
    @Setter
    private boolean vibration;

    @Getter
    @Setter
    private boolean joystick;
    @Getter
    @Setter
    private int joystick_mode;
    @Getter
    @Setter
    private boolean joystickColor;

    public SettingsDAO() {
        notification = true;
        soundVolume = 1.0f;
        effects = true;
        effectsVolume = 0.5f;
        music = true;
        musicVolume = 0.5f;
        shader = true;
        child = false;
        level = 0;
        vibration = true;
        joystick = false;
        joystickColor = true;
        joystick_mode = 0;
    }

    public SettingsDAO(SettingsDAO settingsDAO) {
        this.notification = settingsDAO.notification;
        this.soundVolume = settingsDAO.soundVolume;
        this.effects = settingsDAO.effects;
        this.effectsVolume = settingsDAO.effectsVolume;
        this.music = settingsDAO.music;
        this.musicVolume = settingsDAO.musicVolume;
        this.shader = settingsDAO.shader;
        this.child = settingsDAO.child;
        this.level = settingsDAO.level;
        this.vibration = settingsDAO.vibration;
        this.joystick = settingsDAO.joystick;
        this.joystickColor = settingsDAO.joystickColor;
        this.joystick_mode = settingsDAO.joystick_mode;
    }

    public float overallMusicVolume() {
        return musicVolume * soundVolume;
    }

    public float overallEffectsVolume() {
        return effectsVolume * soundVolume;
    }
}