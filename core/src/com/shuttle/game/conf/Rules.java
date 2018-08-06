package com.shuttle.game.conf;

import com.shuttle.game.ColorLevels;
import com.shuttle.game.Shuttle;

public class Rules {
    //3200
    //public static final int STARS_COUNT = 3200;
    //TEST
    public static final int STARS_COUNT = 200;
    //1080
    public static final int WORLD_WIDTH = 1080;
    //1920
    public static final int WORLD_HEIGHT = 1920;
    //*7
    //public static final int SPACE_WIDTH = WORLD_WIDTH * 7;
    //*7
    //public static final int SPACE_HEIGHT = WORLD_WIDTH * 7;
    //TEST
    public static final int SPACE_WIDTH = 1080;
    public static final int SPACE_HEIGHT = 1920;
    //
    //10
    public static final int CONSUMABLES_AT_START = 0;
    //6.0f
    public static final float CONSUMABLES_RESPAWN_TIME = 60.0f;
    //0
    public static final int ALIENS_AT_START = 0;
    //5.0f
    public static final float ALIENS_RESPAWN_TIME = 50.0f;
    //20.0f
    public static final float ALIENS_HUNGRY_TIME = 20.0f;
    //60.0f
    public static final float ALIENS_SLIM_TIME = 60.0f;
    //4.0f
    public static final float ASTEROIDS_RESPAWN_TIME = 4.0f;
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

    public enum StartLevel {
        LOW {
            @Override
            public void setGameLevel(Shuttle player) {
                player.setMaxFuel(250);
                player.setFuel(250);
                player.setHp(25);
                player.setMaxHp(25);
                player.setLives(3);
                player.setTakeRange(PLAYER_TAKE_RANGE);
                player.setMaxPower(PLAYER_MAX_POWER);
                player.setShieldLevel(ColorLevels.NONE);
                player.setRadialSpeed(PLAYER_RADIAL_SPEED);
                player.setEnginePower(PLAYER_ENGINE_POWER);
                player.getRadar().setActive(true);
                player.getGun().setActive(true);
                player.setPlayingLevel(27);
            }
        },
        MEDIUM {
            @Override
            public void setGameLevel(Shuttle player) {
                player.setMaxFuel(200);
                player.setFuel(200);
                player.setHp(20);
                player.setMaxHp(20);
                player.setLives(2);
                player.setTakeRange(PLAYER_TAKE_RANGE);
                player.setMaxPower(PLAYER_MAX_POWER);
                player.setShieldLevel(ColorLevels.NONE);
                player.setRadialSpeed(PLAYER_RADIAL_SPEED);
                player.setEnginePower(PLAYER_ENGINE_POWER);
                player.getRadar().setActive(false);
                player.getGun().setActive(false);
                player.setPlayingLevel(21);
            }
        },
        HIGH {
            @Override
            public void setGameLevel(Shuttle player) {
                player.setMaxFuel(150);
                player.setFuel(150);
                player.setHp(15);
                player.setMaxHp(15);
                player.setLives(2);
                player.setTakeRange(PLAYER_TAKE_RANGE);
                player.setMaxPower(PLAYER_MAX_POWER);
                player.setShieldLevel(ColorLevels.NONE);
                player.setRadialSpeed(PLAYER_RADIAL_SPEED);
                player.setEnginePower(PLAYER_ENGINE_POWER);
                player.getRadar().setActive(false);
                player.getGun().setActive(false);
                player.setPlayingLevel(15);
            }
        };

        public abstract void setGameLevel(Shuttle player);
    }
}
