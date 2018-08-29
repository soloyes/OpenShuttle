package com.shuttle.game.conf;

import com.shuttle.game.Shuttle;

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
    //CONSUMABLE//
    //10
    public static final int CONSUMABLES_AT_START = 5;
    //6.0f
    public static final float CONSUMABLES_RESPAWN_TIME = 6.0f;
    //
    //ALIEN//
    //0
    public static final int ALIENS_AT_START = 0;
    //5.0f
    public static final float ALIENS_RESPAWN_TIME = 5.0f;
    //20.0f
    public static final float ALIENS_HUNGRY_TIME = 20.0f;
    //60.0f
    public static final float ALIENS_SLIM_TIME = 6.0f;
    //50.0f
    public static final float ALIENS_E_VELOCITY = 50.0f;
    //500.0f
    public static final float ALIENS_F_VELOCITY = 500.0f;
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
    //400.0f
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
    //30
    //
    //CRATE
    public static final int CRATE_FUEL_ADD = 30;
    //1
    public static final int CRATE_REPAIR_ADD = 1;
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