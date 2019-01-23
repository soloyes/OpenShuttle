package com.shuttle.game.conf;

/**
 * @author Shuttle on 8/26/18.
 * Music and Sound constants (MSConstants)
 */
public final class MSConstants {
    private final static String SOUND_ROOT_FOLDER = "sound/";
    private final static String UI_FOLDER = "ui/";
    private final static String GAME_FOLDER = "game/";
    private final static String UI = SOUND_ROOT_FOLDER + UI_FOLDER;
    private final static String GAME = SOUND_ROOT_FOLDER +GAME_FOLDER;

    private final static String MUSIC_ROOT_FOLDER = "music/";
    private final static String MUSIC_FOLDER = "music/";
    private final static String EFFECT_FOLDER = "effect/";
    private final static String MUSIC_EFFECT_FOLDER = MUSIC_ROOT_FOLDER + EFFECT_FOLDER;
    private final static String SONG_FOLDER = MUSIC_ROOT_FOLDER + MUSIC_FOLDER;

    //SOUND
    public final static String UI_CHECKBOX = UI + "ui_checkbox.ogg";
    public final static String UI_MENU = UI + "ui_menu_button.ogg";
    public final static String UI_PAUSE = UI + "ui_pause_button.ogg";

    public final static String GET_RADAR = GAME + "get_radar.ogg";
    public final static String GET_BOMBS = GAME + "get_bombs.ogg";
    public final static String GET_LASER = GAME + "get_laser.ogg";
    public final static String GET_SCANNER = GAME + "get_scanner.ogg";
    public final static String GET_SHIELD = GAME + "get_shield.ogg";
    public final static String GET_FUEL = GAME + "get_fuel.ogg";
    public final static String GET_TRASH = GAME + "get_trash.ogg";
    public final static String GET_FRAGMENT = GAME + "get_fragment.ogg";
    public final static String GET_BOOST = GAME + "get_boost.ogg";
    public final static String GET_ASTRONAUT = GAME + "get_astronaut.ogg";
    public final static String GET_REPAIR = GAME + "get_repair.ogg";
    public final static String GET_SPLASH = GAME + "get_splash.ogg";

    public final static String DESTROY_CORROSIVE_ANOMALY = GAME + "destroy_corrosive_anomaly.ogg";
    public final static String DESTROY_ELECTRIC_ANOMALY = GAME + "destroy_electric_anomaly.ogg";
    public final static String DESTROY_MAGNETIC_ANOMALY = GAME + "destroy_magnetic_anomaly.ogg";
    public final static String DESTROY_RADIATION_ANOMALY = GAME + "destroy_radiation_anomaly.ogg";
    public final static String DESTROY_FUEL = GAME + "destroy_fuel.ogg";
    public final static String DESTROY_ASTRONAUT = GAME + "destroy_astronaut.ogg";
    public final static String DESTROY_CRATE = GAME + "destroy_crate.ogg";
    public final static String DESTROY_STORAGE = GAME + "destroy_storage.ogg";
    public final static String DESTROY_ASTEROID = GAME + "destroy_asteroid.ogg";
    public final static String DESTROY_ASTEROID_RAY = GAME + "destroy_asteroid_ray.ogg";

    public final static String LOSE_RADAR = GAME + "lose_radar.ogg";
    public final static String LOSE_FUEL = GAME + "lose_fuel.ogg";
    public final static String LOSE_SCORE = GAME + "lose_score.ogg";
    public final static String KICKED_BY_CORROSIVE = GAME + "kicked_by_corrosive.ogg";
    public final static String KICKED_BY_ELECTRIC = GAME + "kicked_by_electric.ogg";

    public final static String TELEPORT = GAME + "teleport.ogg";
    public final static String NEW_ASTRONAUT = GAME + "new_astronaut.ogg";
    public final static String LOW_LIFE_WARNING = GAME + "low_life_warning.ogg";
    public final static String UP = GAME + "up.ogg";
    public final static String DOWN = GAME + "down.ogg";
    public final static String OUT_OF_SCREEN = GAME + "out_of_screen.ogg";
    public final static String START_ENGINE = GAME + "start_engine.ogg";
    public final static String SPLASH_ANOMALY = GAME + "splash_anomaly.ogg";
    public final static String LAUNCH_BOMBS = GAME + "launch_bombs.ogg";
    public final static String BURST_CORROSIVE_ANOMALY = GAME + "burst_corrosive_anomaly.ogg";
    //

    //SONG/MUSIC
    public final static String MENU_SONG = SONG_FOLDER + "menu.ogg";
    public final static String GAME_SONG = SONG_FOLDER + "game.ogg";
    //

    //MUSIC EFFECT
    public final static String BLACKHOLE_ROAR_MUSIC = MUSIC_EFFECT_FOLDER + "black_hole_roar.ogg";
    public final static String RADAR_WHITE_NOISE_MUSIC = MUSIC_EFFECT_FOLDER + "white_noise.ogg";
    public final static String LASER = MUSIC_EFFECT_FOLDER + "laser.ogg";
    public final static String HUM_MUSIC_EFFECT_1 = MUSIC_EFFECT_FOLDER + "hum_1.ogg";
    public final static String HUM_MUSIC_EFFECT_2 = MUSIC_EFFECT_FOLDER + "hum_2.ogg";
    public final static String HUM_MUSIC_EFFECT_3 = MUSIC_EFFECT_FOLDER + "hum_3.ogg";
    public final static String HUM_MUSIC_EFFECT_4 = MUSIC_EFFECT_FOLDER + "hum_4.ogg";
    public final static String HUM_MUSIC_EFFECT_5 = MUSIC_EFFECT_FOLDER + "hum_5.ogg";
    public final static String CHARGING_BOMBS = MUSIC_EFFECT_FOLDER + "charging_bombs.ogg";
    //
}