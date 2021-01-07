package com.buran.game.conf;

import com.buran.game.player.Standing;

/**
 * @author soloyes on 10/16/18.
 * Game rules constants (Rules)
 */

public final class Rules {
    public static final boolean DEBUG = false;

    public static final boolean ADS_ENABLED = true;
    public static final float ADS_DELAY = 30.0f;
    //4
    public static final int ADS_RUNTIME_GAME = 4;
    public static final int ADS_RUNTIME_DEATH_MODE = ADS_RUNTIME_GAME / 2;
    //6
    public static final int ADS_TIME = 6;
    //0.5f
    public static final float ADS_TIME_DELTA = 0.5f;

    public static final String RATE_US =
            ADS_ENABLED ?
                    "https://play.google.com/store/apps/details?id=com.buran.game.free.pack"
                    :
                    "https://play.google.com/store/apps/details?id=com.buran.game.pack";
    public static final String ABOUT_US =
            ADS_ENABLED ?
                    "https://play.google.com/store/apps/details?id=com.buran.game.free.pack"
                    :
                    "https://play.google.com/store/apps/details?id=com.buran.game.pack";
    public static final String PAY_US = "https://play.google.com/store/apps/details?id=com.buran.game.pack";

    public static String DEFAULT_NAME = "Player";

    private Rules() {
    }

    //SPACE
    public static int STARS_COUNT;
    //300
    private static final int STARS = 300;
    //1080
    public static int WORLD_WIDTH;
    //1920
    public static int WORLD_HEIGHT;
    //
    public static int SPACE_WIDTH;
    //
    public static int SPACE_HEIGHT;
    //
    public static String DATE_FORMAT = "dd/MM/yyyy";
    //CONSUMABLE
    //2
    public static final int CONSUMABLE_ASTRONAUT_AT_START = 0;
    //1.5f
    public static final float CONSUMABLE_ASTRONAUT_RESPAWN_TIME = 1.5f;
    //3
    public static final int CONSUMABLE_ASTRONAUT_MAX_COUNT = 3;
    //2
    public static final int CONSUMABLE_CRATE_AT_START = 0;
    //5.0f
    public static final float CONSUMABLE_CRATE_RESPAWN_TIME = 5.0f;
    //4
    public static final int CONSUMABLE_CRATE_MAX_COUNT = 4;
    //0
    public static final int CONSUMABLE_FUEL_AT_START = 0;
    //15.0f
    public static final float CONSUMABLE_FUEL_RESPAWN_TIME = 15.0f;
    //3
    public static final int CONSUMABLE_FUEL_MAX_COUNT = 3;
    //1
    public static final int CONSUMABLE_FUEL_ADD_FACTOR = 1;
    //8
    public static final int CONSUMABLE_FUEL_MIN_ADD = 8;
    //15
    public static final int CONSUMABLE_FUEL_MAX_ADD = 15;
    //7
    public static final int CONSUMABLE_FUEL_HP_DAMAGE = 7;
    //0
    public static final int CONSUMABLE_STORAGE_AT_START = 0;
    //2.5f
    public static final float CONSUMABLE_STORAGE_RESPAWN_TIME = 2.5f;
    //2
    public static final int CONSUMABLE_STORAGE_MAX_COUNT = 3;
    //5
    public static final int CONSUMABLE_REPAIR_ADD = 5;
    //ANOMALY
    //0
    public static final int ANOMALIES_ELECTRIC_AT_START = 0;
    //30.0f
    public static final float ANOMALIES_ELECTRIC_RESPAWN_TIME = 30.0f;
    //1
    public static final int ANOMALIES_ELECTRIC_MAX_COUNT = 1;
    //600.0f
    public static final float ANOMALY_ELECTRIC_VELOCITY = 600.0f;
    //0.5
    public static final float ANOMALY_ELECTRIC_DECREASE_FACTOR = 0.5f;
    //10.0f
    public static final float ANOMALIES_ELECTRIC_HUNGRY_TIME = 10.0f;
    //
    //0
    public static final int ANOMALIES_RADIATION_AT_START = 0;
    //12
    public static final float ANOMALIES_RADIATION_RESPAWN_TIME = 12.0f;
    //2
    public static final int ANOMALIES_RADIATION_MAX_COUNT = 2;
    //50.0f
    public static final float ANOMALIES_RADIATION_VELOCITY = 50.0f;
    //10000
    public static final int ANOMALIES_RADIATION_SCORE_DECREASE = 100000;
    //120.0f
    public static final float ANOMALIES_RADIATION_LIVE_TIME = 120.0f;
    //0.4f
    public static final float ANOMALIES_RADIATION_EAT_TIME = 0.4f;
    //2.0f
    public static final float ANOMALIES_RADIATION_BORN_SPEED = 2.0f;
    //10.0f
    public static final float ANOMALIES_RADIATION_HUNGRY_TIME = 10.0f;
    //
    //0
    public static final int ANOMALIES_MAGNETIC_AT_START = 0;
    //15.0f
    public static final float ANOMALIES_MAGNETIC_RESPAWN_TIME = 15.0f;
    //2
    public static final int ANOMALIES_MAGNETIC_MAX_COUNT = 2;
    //50.0f
    public static final float ANOMALIES_MAGNETIC_VELOCITY = 50.0f;
    //1.5f
    public static final float ANOMALIES_MAGNETIC_FUEL_DECREASE = 1.5f;
    //120.0f
    public static final float ANOMALIES_MAGNETIC_LIVE_TIME = 120.0f;
    //0.4f
    public static final float ANOMALIES_MAGNETIC_EAT_TIME = 0.4f;
    //2.0f
    public static final float ANOMALIES_MAGNETIC_BORN_SPEED = 2.0f;
    //10.0f
    public static final float ANOMALIES_MAGNETIC_HUNGRY_TIME = 10.0f;
    //
    //2
    public static final int ANOMALIES_CORROSIVE_AT_START = 0;
    //2.0f
    public static final float ANOMALIES_CORROSIVE_RESPAWN_TIME = 2.0f;
    //40
    public static final int ANOMALIES_CORROSIVE_MAX_COUNT = 40;
    //60.0f
    public static final float ANOMALIES_CORROSIVE_VELOCITY = 60.0f;
    //0.15f
    public static final float ANOMALIES_CORROSIVE_BOOM_MIN_DELAY = 0.15f;
    //0.3f
    public static final float ANOMALIES_CORROSIVE_BOOM_MAX_DELAY = 0.3f;
    //10.0f
    public static final float ANOMALIES_CORROSIVE_HUNGRY_TIME = 10.0f;
    //15.0f
    public static final float ANOMALIES_CORROSIVE_SLIM_TIME = 15.0f;
    //2
    public static final int ANOMALIES_CORROSIVE_AFRAID_FACTOR = 2;
    //45
    public static final int ANOMALIES_CORROSIVE_LIVE_TIME = 45;
    //ASTEROID
    //4.0f
    public static final float ASTEROID_METAL_RESPAWN_TIME = 4.0f;
    //50
    public static final int ASTEROID_METAL_MAX_COUNT = 50;
    //4.0f
    public static final float ASTEROID_MINERAL_RESPAWN_TIME = 4.0f;
    //50
    public static final int ASTEROID_MINERAL_MAX_COUNT = 50;
    //0.5f
    public static final float ASTEROID_SIZE_FOR_HARVEST_FACTOR = 0.5f;
    //0.7f
    public static final float ASTEROID_BURN_TIME = 0.7f;
    //PLAYER
    //400
    public static final int PLAYER_CONSUMABLE_TAKE_RANGE = 400;
    //300
    public static final int PLAYER_FRAGMENT_TAKE_RANGE = 300;
    //500
    public static final int PLAYER_LASER_TAKE_RANGE = 500;
    //135.0f
    public static final float PLAYER_RADIAL_SPEED = 135.0f;
    //600.0f
    public static final float PLAYER_ENGINE_POWER_COMPONENT1 = 600.0f;
    //100.0f
    public static final float PLAYER_ENGINE_POWER_COMPONENT2 = 100.0f;
    //
    public static final float PLAYER_ENGINE_POWER = PLAYER_ENGINE_POWER_COMPONENT1 + PLAYER_ENGINE_POWER_COMPONENT2;
    //1500.0f
    public static final float PLAYER_TURBO_THRESHOLD_VELOCITY = 1500.0f;
    //4.0f
    private static final float PLAYER_MAX_POWER = 4.0f;
    //12
    public static final int PLAYER_RAM_POWER_DECREASE_FACTOR = 12;
    //5.0f
    public static final float PLAYER_TIMER_HP_OUT_OF_MAP = 5.0f;
    //5
    public static final int PLAYER_HP_OUT_OF_MAP = 5;
    //0.2f
    public static final float PLAYER_LOW_LIFE_HP_PERCENT = 0.2f;
    //2.5f
    public static final float PLAYER_TIMER_FUEL_OUT_OF_MAP = 2.5f;
    //3.0f
    public static final float PLAYER_FUEL_OUT_OF_MAP = 3.0f;
    //10.0f
    public static final float PLAYER_KILL_TIMER = 10.0f;
    //3.0f
    public static final float PLAYER_ENGINE_POWER_FACTOR = 3.0f;
    //
    public static final float PLAYER_BOOST_ENGINE_POWER = PLAYER_ENGINE_POWER * PLAYER_ENGINE_POWER_FACTOR;
    //200
    public static final int PLAYER_RAY_CONSUMABLE_HARVEST_VELOCITY = 200;
    //150
    public static final int PLAYER_RAY_FRAGMENT_HARVEST_VELOCITY = 150;
    //1.0f
    public static final float PLAYER_FUEL_CONSUMPTION = 1.0f;
    //2.0f
    public static final float PLAYER_SHIELD_DELAY_TIME = 2.0f;
    //2.0f
    public static final float PLAYER_SHIELD_REPAIR_FACTOR = 2.0f;
    //1.5f
    public static final float PLAYER_SHIELD_DAMAGE_FACTOR = 1.5f;
    //2
    public static final int PLAYER_DAMAGE_DONE_GOLDEN_FACTOR = 2;
    //2
    public static final int PLAYER_DAMAGE_DONE_CRATE = 2;
    //3
    public static final int PLAYER_DAMAGE_DONE_FUEL = 3;
    //4
    public static final int PLAYER_DAMAGE_DONE_STORAGE = 4;
    //10
    public static final int PLAYER_DAMAGE_DONE_ASTRONAUT = 10;
    //5
    public static final int PLAYER_BLACKHOLE_ROTATION = 5;
    //6.0f Check Buffs.getRechargeTime()
    public static final float PLAYER_RECHARGE_TIME = 6.0f;
    //
    //BLACKHOLE
    //1260
    private static final int BLACKHOLE_ROAR_FACTOR = 1260;
    //
    public static final float BLACKHOLE_TAKE_RANGE = BLACKHOLE_ROAR_FACTOR / 3;
    //
    public static int BLACKHOLE_ROAR_RANGE;
    //20
    public static final int BLACKHOLE_EVENT_HORIZON = 20;
    //200
    public static final int BLACKHOLE_VELOCITY_CONSUME_PLAYER = 200;
    //12
    public static final int BLACKHOLE_GET_DAMAGE = 12;
    //0.02f
    public static final float BLACKHOLE_FLOW_VELOCITY = 0.02f;
    //SOUND
    //0.1f
    public static final float FRAGMENT_KICK_PRETTY_SOUND_DELAY = 0.1f;
    //0.5f
    public static final float ANOMALY_KICK_PRETTY_SOUND_DELAY = 0.5f;
    //MUSIC
    //2
    public static final int MUSIC_EFFECT_PROBABILITY = 2;
    //CAMERA
    //1500.0f
    public static final float CAMERA_SMOOTH_VELOCITY = 1500.0f;
    //GAME
    //false
    public static boolean GAME_OVER = false;
    //false
    public static boolean DEATH_MODE = false;
    //0.5f
    public static float DEATH_MODE_RESPAWN_TIME_FACTOR = 0.45f;
    //50.0f
    public static float DEATH_MODE_MAX_COUNT_TIME_PERIOD = 50.0f;
    //1.2f
    public static float DEATH_MODE_MAX_COUNT_TIME_FACTOR = 1.25f;
    //1.5
    public static float DEATH_MODE_VELOCITY_FACTOR = 1.6f;
    //1.0f
    public static float STANDARD_COEFFICIENT = 1.0f;
    //1.3f
    public static float MAX_COUNT_COEFFICIENT = 1.3f;
    //1200
    public static int GAME_SESSION_TIME = 1200;
    //400
    //todo: Balance
    public static final int INDEX_SCORE_TO_LEVEL = 400;
    //todo: Balance
    //50000
    public static final int SCORE_BASE_BONUS = 50000;
    //3
    public static final int ANOMALY_GENERATE_BASE = 3;
    //4
    public static final int CONSUMABLE_GENERATE_BASE_BONUS = 4;
    //2000
    public static final int CONSUMABLE_TRASH_BASE_SCORE = 2000;
    //2
    public static final int CONSUMABLE_TRASH_GOLDEN_SCORE_MIN = 2;
    //4
    public static final int CONSUMABLE_TRASH_GOLDEN_SCORE_MAX = 4;
    //5
    public static final int CONSUMABLE_TRASH_GOLDEN_SCORE_FACTOR = 5;
    //10
    public static final int ANOMALY_GENERATE_BASE_BONUS = 10;
    //2000
    public static final int ANOMALIES_BASE_SCORE_ADD = 2000;
    //10000
    public static final int ANOMALIES_BURST_SCORE_ADD = 10000;
    //todo: Balance
    //18000
    public static final int CONSUMABLE_ASTRONAUT_BASE_SCORE_ADD = 18000;
    //2000
    public static final int CONSUMABLE_ASTRONAUT_BONUS_SCORE_ADD = 2000;
    //100
    public static final int CONSUMABLE_ASTRONAUT_RANGE_SCORE_ADD = 1000;
    //70000
    public static final int CONSUMABLE_MURDER_SCORE_DECREASE = 70000;
    //30.0f
    public static final float CONSUMABLE_SHIELD_TIME = 30.0f;
    //30.0f
    public static final float CONSUMABLE_SCAN_TIME = 30.0f;
    //60
    public static final int ANOMALIES_CORROSIVE_HUNT_TIME = 60;
    //3
    public static final int LAUNCH_ROCKETS_TIME = 3;
    //1500
    public static final int ROCKETS_TAKE_RANGE = 1500;
    //200
    public static final int ROCKETS_VELOCITY = 200;
    //3 means 1/3. 0 means 100%
    public static int CONSUMABLE_GOLDEN_PROBABILITY = 3;
    //3 means 1/3. 0 means 100%
    public static int CONSUMABLE_STORAGE_DANGER_PROBABILITY = 4;
    //3 means 1/3. 0 means 100%
    public static int CONSUMABLE_CRATE_DANGER_PROBABILITY = 4;
    //6
    public static final int SELECTOR_MAX_COUNT = 6;
    //3
    public static final int SELECTOR_GOLDEN_COUNT = 3;
    //3
    public static final int SELECTOR_SHABBY_BASE_BONUS = 3;
    //0.7f
    public static final float LINE_PRESSED_WAIT = 0.7f;
    //3.0f
    public static final float LINE_DELETE_WAIT = 3.0f;
    //2.5f better less than LINE_DELETE_WAIT
    public static final float NOTICE_WAIT = 2.5f;
    //0.4f
    public static final float SETTINGS_WAIT_TOUCH = 0.4f;
    //0.9f
    public static final float SETTINGS_WAIT_DISABLE = 0.5f + SETTINGS_WAIT_TOUCH;
    //3
    public static final int LEVELS_TO_LIVE = 6;
    //0.1f
    public static final float INCREMENT_OF_SINGLE_BAR = 0.1f;
    //5
    public static final int CIRCLE_RENDER = 5;

    public static void setSpace(int n) {
        STARS_COUNT = STARS * n;
        SPACE_WIDTH = WORLD_WIDTH * n;
        SPACE_HEIGHT = WORLD_HEIGHT * n;
        BLACKHOLE_ROAR_RANGE = n * BLACKHOLE_ROAR_FACTOR;
    }

    public enum StartLevel {
        LOW {
            @Override
            public void setGameLevel() {
                Standing.getInstance().setMaxFuel(135);
                Standing.getInstance().setFuel(135);
                Standing.getInstance().setHp(27);
                Standing.getInstance().setMaxHp(27);
                Standing.getInstance().setMaxLaserChargeLevel(27);
                Standing.getInstance().setLifeShield(27);
                Standing.getInstance().setMaxLifeShield(27);
                Standing.getInstance().setLives(1);
                Standing.getInstance().setRamMaxPower(PLAYER_MAX_POWER);
                Standing.getInstance().setStartLevel(0);
                Standing.getInstance().setScore(0);
                Standing.getInstance().setScoreView(0);
            }
        },
        MEDIUM {
            @Override
            public void setGameLevel() {
                Standing.getInstance().setMaxFuel(120);
                Standing.getInstance().setFuel(120);
                Standing.getInstance().setHp(24);
                Standing.getInstance().setMaxHp(24);
                Standing.getInstance().setMaxLaserChargeLevel(24);
                Standing.getInstance().setLifeShield(24);
                Standing.getInstance().setMaxLifeShield(24);
                Standing.getInstance().setLives(2);
                Standing.getInstance().setRamMaxPower(PLAYER_MAX_POWER);
                Standing.getInstance().setStartLevel(0);
                Standing.getInstance().setScore(0);
                Standing.getInstance().setScoreView(0);
            }
        },
        HIGH {
            @Override
            public void setGameLevel() {
                Standing.getInstance().setMaxFuel(105);
                Standing.getInstance().setFuel(105);
                Standing.getInstance().setHp(21);
                Standing.getInstance().setMaxHp(21);
                Standing.getInstance().setMaxLaserChargeLevel(21);
                Standing.getInstance().setLifeShield(21);
                Standing.getInstance().setMaxLifeShield(21);
                Standing.getInstance().setLives(3);
                Standing.getInstance().setRamMaxPower(PLAYER_MAX_POWER);
                Standing.getInstance().setStartLevel(0);
                Standing.getInstance().setScore(0);
                Standing.getInstance().setScoreView(0);
            }
        },
        DEBUG {
            @Override
            public void setGameLevel() {
                Standing.getInstance().setMaxFuel(180);
                Standing.getInstance().setFuel(180);
                Standing.getInstance().setHp(18);
                Standing.getInstance().setMaxHp(18);
                Standing.getInstance().setMaxLaserChargeLevel(18);
                Standing.getInstance().setLifeShield(18);
                Standing.getInstance().setMaxLifeShield(18);
                Standing.getInstance().setLives(2);
                Standing.getInstance().setRamMaxPower(PLAYER_MAX_POWER);
                Standing.getInstance().setStartLevel(0);
                Standing.getInstance().setScore(0);
                Standing.getInstance().setScoreView(0);
            }
        };

        public abstract void setGameLevel();
    }
}