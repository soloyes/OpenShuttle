package com.buran.game.conf;

import com.buran.game.labelsticker.Sticker;

/**
 * @author soloyes on 13/11/20.
 */

public class Buffs {

    public static int getScoreDecrease() {
        return (int) (Sticker.COMMANDER.getCoefficient() * (Rules.ANOMALIES_RADIATION_SCORE_DECREASE / Sticker.SCIENTIST.getCoefficient()));
    }

    public static float getAsteroidBurnTime() {
        return Rules.ASTEROID_BURN_TIME / Sticker.SCIENTIST.getCoefficient();
    }

    public static int getRepairAdd() {
        return (int) (Rules.CONSUMABLE_REPAIR_ADD * Sticker.MEDIC.getCoefficient());
    }

    public static float getBlackholeDecrease() {
        return Rules.BLACKHOLE_GET_DAMAGE / Sticker.MEDIC.getCoefficient();
    }

    public static float getRechargeTimeSplashRockets() {
        return Rules.PLAYER_RECHARGE_TIME / Sticker.MILITARY.getCoefficient();
    }

    public static float getAsteroidSizeFactor() {
        return Sticker.MILITARY.getCoefficient();
    }

    public static float getRechargeTimeRepairBoost() {
        return Rules.PLAYER_RECHARGE_TIME / Sticker.MECHANIC.getCoefficient();
    }

    public static float getFuelDecrease() {
        return Rules.ANOMALIES_MAGNETIC_FUEL_DECREASE / Sticker.MECHANIC.getCoefficient();
    }

    public static float getBlackholeTakeRange() {
        return Rules.BLACKHOLE_TAKE_RANGE / Sticker.COMMANDER.getCoefficient();
    }

    public static float getBlackholeNoiseRange() {
        return getBlackholeTakeRange() * 2;
    }

    public static float getPlayerEnginePower() {
        return Rules.PLAYER_ENGINE_POWER_COMPONENT1 + Rules.PLAYER_ENGINE_POWER_COMPONENT2 * Sticker.COMMANDER.getCoefficient();
    }
}