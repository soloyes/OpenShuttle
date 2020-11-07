package com.shuttle.game.conf;

/**
 * @author soloyes on 8/26/18.
 * Music and Sound constants (MSConstants)
 */

public final class MSConstants {
    private MSConstants() {
    }

    private static final String SOUND_ROOT_FOLDER = "sound/";
    private static final String UI_FOLDER = "ui/";
    private static final String GAME_FOLDER = "game/";
    private static final String UI = SOUND_ROOT_FOLDER + UI_FOLDER;
    private static final String GAME = SOUND_ROOT_FOLDER +GAME_FOLDER;

    private static final String MUSIC_ROOT_FOLDER = "music/";
    private static final String MUSIC_FOLDER = "music/";
    private static final String EFFECT_FOLDER = "effect/";
    private static final String MUSIC_EFFECT_FOLDER = MUSIC_ROOT_FOLDER + EFFECT_FOLDER;
    private static final String SONG_FOLDER = MUSIC_ROOT_FOLDER + MUSIC_FOLDER;

    //SOUND
    public static final String UI_PROFILE_SAVED = UI + "ui_profile_saved.ogg";
    public static final String UI_PROFILE_CHANGED = UI + "ui_profile_changed.ogg";
    public static final String UI_CHANGED = UI + "ui_changed.ogg";
    public static final String UI_INPUT = UI + "ui_input.ogg";
    public static final String UI_CLICK = UI + "ui_click.ogg";
    public static final String UI_MENU = UI + "ui_menu_button.ogg";
    public static final String UI_PAUSE = UI + "ui_pause_button.ogg";
    public static final String UI_LEFT_RIGHT = UI + "ui_left_right.ogg";
    public static final String UI_SWITCH = UI + "ui_switch_button.ogg";
    public static final String UI_EMPTY_NAME = UI + "ui_empty_name.ogg";

    public static final String GET_RADAR = GAME + "get_radar.ogg";
    public static final String GET_ROCKETS = GAME + "get_rockets.ogg";
    public static final String GET_LASER = GAME + "get_laser.ogg";
    public static final String GET_SCANNER = GAME + "get_scanner.ogg";
    public static final String GET_SHIELD = GAME + "get_shield.ogg";
    public static final String GET_FUEL = GAME + "get_fuel.ogg";
    public static final String GET_TRASH = GAME + "get_trash.ogg";
    public static final String GET_FRAGMENT = GAME + "get_fragment.ogg";
    public static final String GET_BOOST = GAME + "get_boost.ogg";
    public static final String GET_ASTRONAUT = GAME + "get_astronaut.ogg";
    public static final String GET_REPAIR = GAME + "get_repair.ogg";
    public static final String GET_SPLASH = GAME + "get_splash.ogg";

    public static final String DESTROY_CORROSIVE_ANOMALY = GAME + "destroy_corrosive_anomaly.ogg";
    public static final String DESTROY_ELECTRIC_ANOMALY = GAME + "destroy_electric_anomaly.ogg";
    public static final String DESTROY_MAGNETIC_ANOMALY = GAME + "destroy_magnetic_anomaly.ogg";
    public static final String DESTROY_RADIATION_ANOMALY = GAME + "destroy_radiation_anomaly.ogg";
    public static final String DESTROY_FUEL = GAME + "destroy_fuel.ogg";
    public static final String DESTROY_ASTRONAUT = GAME + "destroy_astronaut.ogg";
    public static final String DESTROY_CRATE = GAME + "destroy_crate.ogg";
    public static final String DESTROY_STORAGE = GAME + "destroy_storage.ogg";
    public static final String DESTROY_ASTEROID = GAME + "destroy_asteroid.ogg";
    public static final String DESTROY_ASTEROID_RAY = GAME + "destroy_asteroid_ray.ogg";

    public static final String LOSE_RADAR = GAME + "lose_radar.ogg";
    public static final String LOSE_FUEL = GAME + "lose_fuel.ogg";
    public static final String LOSE_SCORE = GAME + "lose_score.ogg";
    public static final String KICKED_BY_CORROSIVE = GAME + "kicked_by_corrosive.ogg";
    public static final String KICKED_BY_ELECTRIC = GAME + "kicked_by_electric.ogg";

    public static final String TELEPORT = GAME + "teleport.ogg";
    public static final String NEW_ASTRONAUT = GAME + "new_astronaut.ogg";
    public static final String LOW_LIFE_WARNING = GAME + "low_life_warning.ogg";
    public static final String UP = GAME + "up.ogg";
    public static final String LEVEL_UP = GAME + "level_up.ogg";
    public static final String DOWN = GAME + "down.ogg";
    public static final String OUT_OF_SCREEN = GAME + "out_of_screen.ogg";
    public static final String START_ENGINE = GAME + "start_engine.ogg";
    public static final String SPLASH_ANOMALY = GAME + "splash_anomaly.ogg";
    public static final String LAUNCH_ROCKETS = GAME + "launch_rockets.ogg";
    public static final String BURST_CORROSIVE_ANOMALY = GAME + "burst_corrosive_anomaly.ogg";
    public static final String TRASH_ASTEROIDS = GAME + "trash_asteroids.ogg";

    public static final String KILL_MAGNETIC_RADIATION = GAME + "kill_magnetic_radiation.ogg";
    public static final String GENERATE_CRATE_STORAGE = GAME + "generate_crate_storage.ogg";
    public static final String ADD_SELECTABLE = GAME + "add_selectable.ogg";
    public static final String ADD_SCORE = GAME + "add_score.ogg";
    public static final String ANOMALY_FLUSH = GAME + "anomaly_flush.ogg";
    public static final String DEL_HP_FUEL = GAME + "del_hp_fuel.ogg";
    public static final String ANOMALIES_BORN = GAME + "anomalies_born.ogg";
    public static final String DEL_CRATE_STORAGE = GAME + "del_crate_storage.ogg";
    public static final String DISABLE_LASER_RADAR = GAME + "disable_laser_radar.ogg";
    public static final String DEL_SCORE = GAME + "del_score.ogg";
    public static final String ADD_CORROSIVE = GAME + "add_corrosive.ogg";
    public static final String ADD_HP = GAME + "add_hp.ogg";
    //

    //SONG/MUSIC
    public static final String MENU_SONG = SONG_FOLDER + "menu.ogg";
    public static final String GAME_SONG = SONG_FOLDER + "game.ogg";
    public static final String GAME_OVER = SONG_FOLDER + "gameover.ogg";
    //

    //MUSIC EFFECT
    public static final String BLACKHOLE_ROAR_MUSIC = MUSIC_EFFECT_FOLDER + "black_hole_roar.ogg";
    public static final String RADAR_WHITE_NOISE_MUSIC = MUSIC_EFFECT_FOLDER + "white_noise.ogg";
    public static final String LASER = MUSIC_EFFECT_FOLDER + "laser.ogg";
    public static final String HUM_MUSIC_EFFECT_1 = MUSIC_EFFECT_FOLDER + "hum_1.ogg";
    public static final String HUM_MUSIC_EFFECT_2 = MUSIC_EFFECT_FOLDER + "hum_2.ogg";
    public static final String HUM_MUSIC_EFFECT_3 = MUSIC_EFFECT_FOLDER + "hum_3.ogg";
    public static final String HUM_MUSIC_EFFECT_4 = MUSIC_EFFECT_FOLDER + "hum_4.ogg";
    public static final String HUM_MUSIC_EFFECT_5 = MUSIC_EFFECT_FOLDER + "hum_5.ogg";
    public static final String CHARGING_ROCKETS = MUSIC_EFFECT_FOLDER + "charging_rockets.ogg";
    //
}