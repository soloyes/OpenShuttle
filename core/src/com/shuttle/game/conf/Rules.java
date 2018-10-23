package com.shuttle.game.conf;

import com.shuttle.game.player.Shuttle;

/**
 * @author Shuttle on 10/16/18.
 * Game rules constants (Rules)
 */
public final class Rules {
    //
    public static int STARS_COUNT;
    //1080
    public static final int WORLD_WIDTH = 1080;
    //1920
    public static final int WORLD_HEIGHT = 1920;
    //
    public static int SPACE_WIDTH;
    //
    public static int SPACE_HEIGHT;
    //CONSUMABLE
    //2
    public static final int CONSUMABLE_AT_START = 2;
    //6.0f
    public static final float CONSUMABLE_RESPAWN_TIME = 6.0f;
    //10000
    public static final int CONSUMABLE_BASE_SCORE_ADD = 10000;
    //
    //ANOMALY
    //0.1f
    public static final float ANOMALIES_BOOM_MIN_DELAY = 0.1f;
    //0.25f
    public static final float ANOMALIES_BOOM_MAX_DELAY = 0.25f;
    //2
    public static final int ANOMALIES_CORROSIVE_AT_START = 2;
    //0
    public static final int ANOMALIES_ELECTRIC_AT_START = 0;
    //0
    public static final int ANOMALIES_MAGNETIC_AT_START = 0;
    //0
    public static final int ANOMALIES_RADIATION_AT_START = 0;
    //5000
    public static final int ANOMALIES_BASE_SCORE_ADD = 5000;
    //30.0f
    public static final float ANOMALIES_ELECTRIC_RESPAWN_TIME = 30.0f;
    //1
    public static final int ANOMALIES_ELECTRIC_MAX_COUNT = 1;
    //600.0f
    public static float ANOMALY_ELECTRIC_VELOCITY = 600.0f;
    //0.5
    public static final float ANOMALY_ELECTRIC_DECREASE_FACTOR = 0.5f;
    //10.0f
    public static float ANOMALIES_ELECTRIC_HUNGRY_TIME = 10.0f;
    //
    //12
    public static final float ANOMALIES_RADIATION_RESPAWN_TIME = 12.0f;
    //120.0f
    public static final float ANOMALIES_RADIATION_LIVE_TIME = 120.0f;
    //2
    public static final int ANOMALIES_RADIATION_MAX_COUNT = 2;
    //500
    public static final int ANOMALIES_RADIATION_SCORE_DECREASE = 500;
    //0.4f
    public static final float ANOMALIES_RADIATION_EAT_TIME = 0.4f;
    //50.0f
    public static final float ANOMALIES_RADIATION_VELOCITY = 50.0f;
    //
    //15
    public static final float ANOMALIES_MAGNETIC_RESPAWN_TIME = 15.0f;
    //120.0f
    public static final float ANOMALIES_MAGNETIC_LIVE_TIME = 120.0f;
    //2
    public static final int ANOMALIES_MAGNETIC_MAX_COUNT = 2;
    //0.3f
    public static final float ANOMALIES_MAGNETIC_FUEL_DECREASE = 0.3f;
    //0.4f
    public static final float ANOMALIES_MAGNETIC_EAT_TIME = 0.4f;
    //50.0f
    public static final float ANOMALIES_MAGNETIC_VELOCITY = 50.0f;
    //
    //4.0f
    public static final float ANOMALIES_CORROSIVE_RESPAWN_TIME = 4.0f;
    //10.0f
    public static final float ANOMALIES_CORROSIVE_HUNGRY_TIME = 10.0f;
    //100
    public static final int ANOMALIES_CORROSIVE_MAX_COUNT = 100;
    //5.0f
    public static final float ANOMALIES_CORROSIVE_SLIM_TIME = 15.0f;
    //60
    public static int ANOMALIES_CORROSIVE_HUNT_TIME = 60;
    //60.0f
    public static final float ANOMALIES_CORROSIVE_VELOCITY = 60.0f;
    //
    //ASTEROID
    //4.0f
    public static final float ASTEROID_RESPAWN_TIME = 4.0f;
    //
    //PLAYER
    //500
    public static final int PLAYER_TAKE_RANGE = 500;
    //135.0f
    public static final float PLAYER_RADIAL_SPEED = 135.0f;
    //500.0f
    public static final float PLAYER_ENGINE_POWER = 500.0f;
    //5.0f
    public static final float PLAYER_MAX_POWER = 5.0f;
    //5.0f
    public static final float PLAYER_TIMER_HP_OUT_OF_MAP = 5.0f;
    //1
    public static final int PLAYER_HP_OUT_OF_MAP = 1;
    //0.2f
    public static final float PLAYER_LOW_LIFE_HP_PERCENT = 0.2f;
    //2.5f
    public static final float PLAYER_TIMER_FUEL_OUT_OF_MAP = 2.5f;
    //3.0f
    public static final float PLAYER_FUEL_OUT_OF_MAP = 3.0f;
    //20000
    //todo: need to be balanced
    public static final int SCORE_TO_LIVE = 20000;
    //1.0f
    public static final float PLAYER_FUEL_CONSUMPTION = 1.0f;
    //
    //CRATE
    //50
    public static final int CRATE_FUEL_ADD = 50;
    //2
    public static final int CRATE_REPAIR_ADD = 2;
    //BLACKHOLE
    //250
    public static final int BLACKHOLE_TAKE_RANGE = 250;
    //
    public static int BLACKHOLE_ROAR_RANGE;
    //20
    public static final int BLACKHOLE_EVENT_HORIZON = 20;
    //200
    public static final int BLACKHOLE_VELOCITY_CONSUME_POOLABLE = 200;
    //200
    public static final int BLACKHOLE_VELOCITY_CONSUME_PLAYER = 200;
    //5
    public static final int BLACKHOLE_GET_DAMAGE = 5;
    //GAME
    //false
    public static boolean GAME_OVER = false;
    //SOUND
    //0.1f
    public static final float FRAGMENT_KICK_PRETTY_SOUND_DELAY = 0.1f;
    //0.5f
    public static final float ANOMALY_KICK_PRETTY_SOUND_DELAY = 0.5f;
    //CAMERA
    //1500.0f
    public static final float CAMERA_SMOOTH_VELOCITY = 1500.0f;
    //600
    private static int BLACKHOLE_ROAR_FACTOR = 600;
    //SPACE
    //600
    private static final int STARS = 600;

    public static void setSpace(int n) {
        STARS_COUNT = STARS * n;
        SPACE_WIDTH = WORLD_WIDTH * n;
        SPACE_HEIGHT = WORLD_HEIGHT * n;
        BLACKHOLE_ROAR_RANGE = n * BLACKHOLE_ROAR_FACTOR;
    }

    public enum StartLevel {
        LOW {
            @Override
            public void setGameLevel(Shuttle player) {
                player.getStanding().setMaxFuel(250);
                player.getStanding().setFuel(250);
                player.getStanding().setHp(25);
                player.getStanding().setMaxHp(25);
                player.getStanding().setLives(3);
                player.setTakeRange(PLAYER_TAKE_RANGE);
                player.setMaxPower(PLAYER_MAX_POWER);
                player.setShieldLevel(GlobalInteractionLevels.NONE);
                player.setRadialSpeed(PLAYER_RADIAL_SPEED);
                player.setEnginePower(PLAYER_ENGINE_POWER);
                player.getRadar().setActive(true);
                //player.getGun().setActive(true);
                player.getStanding().setPlayingLevel(27);
                player.getStanding().setScoreToLive(SCORE_TO_LIVE);
            }
        },
        MEDIUM {
            @Override
            public void setGameLevel(Shuttle player) {
                player.getStanding().setMaxFuel(200);
                player.getStanding().setFuel(200);
                player.getStanding().setHp(20);
                player.getStanding().setMaxHp(20);
                player.getStanding().setLives(2);
                player.setTakeRange(PLAYER_TAKE_RANGE);
                player.setMaxPower(PLAYER_MAX_POWER);
                player.setShieldLevel(GlobalInteractionLevels.NONE);
                player.setRadialSpeed(PLAYER_RADIAL_SPEED);
                player.setEnginePower(PLAYER_ENGINE_POWER);
                player.getRadar().setActive(false);
                //player.getGun().setActive(false);
                player.getStanding().setPlayingLevel(21);
                player.getStanding().setScoreToLive(SCORE_TO_LIVE * 7);
            }
        },
        HIGH {
            @Override
            public void setGameLevel(Shuttle player) {
                player.getStanding().setMaxFuel(150);
                player.getStanding().setFuel(150);
                player.getStanding().setHp(15);
                player.getStanding().setMaxHp(15);
                player.getStanding().setLives(2);
                player.setTakeRange(PLAYER_TAKE_RANGE);
                player.setMaxPower(PLAYER_MAX_POWER);
                player.setShieldLevel(GlobalInteractionLevels.NONE);
                player.setRadialSpeed(PLAYER_RADIAL_SPEED);
                player.setEnginePower(PLAYER_ENGINE_POWER);
                player.getRadar().setActive(false);
                //player.getGun().setActive(false);
                player.getStanding().setPlayingLevel(15);
                player.getStanding().setScoreToLive(SCORE_TO_LIVE * 13);
            }
        },
        EXTREME {
            @Override
            public void setGameLevel(Shuttle player) {
                player.getStanding().setMaxFuel(100);
                player.getStanding().setFuel(100);
                player.getStanding().setHp(10);
                player.getStanding().setMaxHp(10);
                player.getStanding().setLives(2);
                player.setTakeRange(PLAYER_TAKE_RANGE);
                player.setMaxPower(PLAYER_MAX_POWER);
                player.setShieldLevel(GlobalInteractionLevels.NONE);
                player.setRadialSpeed(PLAYER_RADIAL_SPEED);
                player.setEnginePower(PLAYER_ENGINE_POWER);
                player.getRadar().setActive(false);
                //player.getGun().setActive(false);
                player.getStanding().setPlayingLevel(10);
                player.getStanding().setScoreToLive(SCORE_TO_LIVE * 18);
            }
        };

        public abstract void setGameLevel(Shuttle player);
    }
}