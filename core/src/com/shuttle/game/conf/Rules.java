package com.shuttle.game.conf;

public class Rules {
    //3200
    public static final int STARS_COUNT = 3200;
    //TEST
    //public static final int STARS_COUNT = 200;
    //1080
    public static final int WORLD_WIDTH = 1080;
    //1920
    public static final int WORLD_HEIGHT = 1920;
    //*7
    public static final int SPACE_WIDTH = WORLD_WIDTH * 7;
    //*7
    public static final int SPACE_HEIGHT = WORLD_WIDTH * 7;

    //TEST
    //public static final int SPACE_WIDTH = 1080;
    //public static final int SPACE_HEIGHT = 1920;
    //
    //10
    public static final int CONSUMABLES_AT_START = 10;
    //6.0f
    public static final float CONSUMABLES_RESPAWN_TIME = 6.0f;
    //0
    public static final int ALIENS_AT_START = 0;
    //5.0f
    public static final float ALIENS_RESPAWN_TIME = 5.0f;
    //20.0f
    public static final float ALIENS_HUNGRY_TIME = 20.0f;
    //60.0f
    public static final float ALIENS_SLIM_TIME = 60.0f;
    //4.0f
    public static final float ASTEROIDS_RESPAWN_TIME = 4.0f;

    //250.0f
    public static final float PLAYER_MAX_FUEL = 250.0f;
    //25
    public static final int PLAYER_MAX_HP = 1;
    //3
    public static final int PLAYER_INIT_LIVES = 1;
    //500
    public static final int PLAYER_TAKE_RANGE = 500;
    //10000
    public static final int PLAYER_SCORE_ADD = 10000;
    //135.0f
    public static final float PLAYER_RADIAL_SPEED = 135.0f;
    //400.0f
    public static final float PLAYER_ENGINE_POWER = 400.0f;
    //5.0f
    public static final float PLAYER_MAX_POWER = 5.0f;

    //30
    public static final int CRATE_FUEL_ADD = 30;
    //1
    public static final int CRATE_REPAIR_ADD = 1;

    //false
    public static boolean GAME_OVER = false;
}
