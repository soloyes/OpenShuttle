package com.shuttle.game.conf;

import com.shuttle.game.player.Shuttle;

public class Rules {
    //3200
    //public static final int STARS_COUNT = 3200;
    //TEST
    public static final int STARS_COUNT = 400;
    //1080
    public static final int WORLD_WIDTH = 1080;
    //1920
    public static final int WORLD_HEIGHT = 1920;
    //*7
    //public static final int SPACE_WIDTH = WORLD_WIDTH * 7;
    //*7
    //public static final int SPACE_HEIGHT = WORLD_WIDTH * 7;
    //TEST
    public static final int SPACE_WIDTH = 1080 * 2;
    public static final int SPACE_HEIGHT = 1920 * 2;
    //
    //CONSUMABLE//
    //10
    public static final int CONSUMABLES_AT_START = 0;
    //6.0f
    public static final float CONSUMABLES_RESPAWN_TIME = 6.0f;
    //
    //ANOMALY
    //0
    public static final int ANOMALIES_AT_START = 0;
    //5.0f
    public static final float ANOMALIES_CORROSIVE_RESPAWN_TIME = 5.0f;
    //30
    public static final float ANOMALIES_ELECTRIC_RESPAWN_TIME = 30.0f;
    //15
    public static final float ANOMALIES_RADIATION_RESPAWN_TIME = 15.0f;
    //
    public static final float ANOMALIES_RADIATION_LIVE_TIME = 120.0f;
    //6
    public static final int ANOMALIES_RADIATION_MAX_COUNT = 6;
    //0.1f
    public static final float ANOMALIES_RADIATION_FUEL_DECREASE = 0.1f;
    //1.0f
    public static final float ANOMALIES_RADIATION_HUNGRY_TIME = 1.0f;
    //
    //10.0f
    public static final float ANOMALIES_CORROSIVE_HUNGRY_TIME = 10.0f;
    //30.0f
    public static final float ANOMALIES_SLIM_TIME = 10.0f;
    //50.0f
    public static final float ANOMALIES_VELOCITY = 50.0f;
    //60
    public static int ANOMALIES_SHUTTLE_HUNT_TIMER = 60;
    //10
    public static int ANOMALIES_ASTRONAUTS_HUNT_TIMER = 10;
    //500.0f
    public static final float ANOMALY_ELECTRIC_VELOCITY = 500.0f;
    //
    //ASTEROID
    //4.0f
    public static final float ASTEROIDS_RESPAWN_TIME = 4.0f;
    //
    //PLAYER
    //500
    public static final int PLAYER_TAKE_RANGE = 500;
    //10000
    public static final int PLAYER_SCORE_ADD = 10000;
    //135.0f
    public static final float PLAYER_RADIAL_SPEED = 135.0f;
    //500.0f
    public static final float PLAYER_ENGINE_POWER = 500.0f;
    //5.0f
    public static final float PLAYER_MAX_POWER = 5.0f;
    //5.0f
    public static final float PLAYER_TIMER_HP_OUT_OF_MAP = 5.0f;
    //1
    public static int PLAYER_HP_OUT_OF_MAP = 1;
    //2.5f
    public static final float PLAYER_TIMER_FUEL_OUT_OF_MAP = 2.5f;
    //3.0f
    public static final float PLAYER_FUEL_OUT_OF_MAP = 3.0f;
    //20000
    public static final int PLAYER_SCORE_TO_LIVE = 20000;
    //1.0f
    public static final float PLAYER_FUEL_CONSUMPTION = 1.0f;
    //
    //CRATE
    //50
    public static final int CRATE_FUEL_ADD = 50;
    //1
    public static final int CRATE_REPAIR_ADD = 2;
    //
    //false
    public static boolean GAME_OVER = false;

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
                player.getGun().setActive(true);
                player.getStanding().setPlayingLevel(27);
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
                player.getGun().setActive(false);
                player.getStanding().setPlayingLevel(21);
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
                player.getGun().setActive(false);
                player.getStanding().setPlayingLevel(15);
            }
        };

        public abstract void setGameLevel(Shuttle player);
    }
}