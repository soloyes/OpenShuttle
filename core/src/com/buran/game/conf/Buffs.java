package com.buran.game.conf;

import com.buran.game.labelsticker.Sticker;
import com.buran.game.player.Standing;

/**
 * @author soloyes on 13/11/20.
 */

public class Buffs {
    private static int repairAdd;
    private static float rechargeTimeRepairBoost;
    private static float rechargeSplashRockets;
    private static float fuelDecrease;
    private static int scoreDecrease;

    private static float repairAddN = 1.0f;
    private static float rechargeTimeRepairBoostN = 0.5f;
    private static float rechargeSplashRocketsN = 0.5f;
    private static float fuelDecreaseN = 0.5f;
    private static float scoreDecreaseN = 0.5f;

    public static int getRepairAdd() {
        int i = Standing.getInstance().getAstronauts().containsKey(Sticker.MEDIC) ? Standing.getInstance().getAstronauts().get(Sticker.MEDIC) : 0;
        if (i >= 0 && i <= repairAddN * 10) {
            repairAdd = Rules.CONSUMABLE_REPAIR_ADD;
        } else if (i > repairAddN * 10 && i <= repairAddN * 20) {
            repairAdd = Rules.CONSUMABLE_REPAIR_ADD + 1;
        } else if (i > repairAddN * 20 && i <= repairAddN * 30) {
            repairAdd = Rules.CONSUMABLE_REPAIR_ADD + 2;
        } else {
            repairAdd = Rules.CONSUMABLE_REPAIR_ADD + 3;
        }
        return repairAdd;
    }

    public static float getRechargeTimeSplashRockets() {
        int i = Standing.getInstance().getAstronauts().containsKey(Sticker.MILITARY) ? Standing.getInstance().getAstronauts().get(Sticker.MILITARY) : 0;
        if (i >= 0 && i <= rechargeSplashRocketsN * 10) {
            rechargeSplashRockets = Rules.PLAYER_RECHARGE_TIME;
        } else if (i > rechargeSplashRocketsN * 10 && i <= rechargeSplashRocketsN * 20) {
            rechargeSplashRockets = Rules.PLAYER_RECHARGE_TIME / 2.0f;
        } else if (i > rechargeSplashRocketsN * 20 && i <= rechargeSplashRocketsN * 30) {
            rechargeSplashRockets = Rules.PLAYER_RECHARGE_TIME / 3.0f;
        } else {
            rechargeSplashRockets = Rules.PLAYER_RECHARGE_TIME / 4.0f;
        }
        return rechargeSplashRockets;
    }

    public static float getRechargeTimeRepairBoost() {
        int i = Standing.getInstance().getAstronauts().containsKey(Sticker.MECHANIC) ? Standing.getInstance().getAstronauts().get(Sticker.MECHANIC) : 0;
        if (i >= 0 && i <= rechargeTimeRepairBoostN * 10) {
            rechargeTimeRepairBoost = Rules.PLAYER_RECHARGE_TIME;
        } else if (i > rechargeTimeRepairBoostN * 10 && i <= rechargeTimeRepairBoostN * 20) {
            rechargeTimeRepairBoost = Rules.PLAYER_RECHARGE_TIME / 2.0f;
        } else if (i > rechargeTimeRepairBoostN * 20 && i <= rechargeTimeRepairBoostN * 30) {
            rechargeTimeRepairBoost = Rules.PLAYER_RECHARGE_TIME / 3.0f;
        } else {
            rechargeTimeRepairBoost = Rules.PLAYER_RECHARGE_TIME / 4.0f;
        }
        return rechargeTimeRepairBoost;
    }

    public static float getFuelDecrease() {
        int i = Standing.getInstance().getAstronauts().containsKey(Sticker.MECHANIC) ? Standing.getInstance().getAstronauts().get(Sticker.MECHANIC) : 0;
        if (i >= 0 && i <= fuelDecreaseN * 10) {
            fuelDecrease = Rules.ANOMALIES_MAGNETIC_FUEL_DECREASE;
        } else if (i > fuelDecreaseN * 10 && i <= fuelDecreaseN * 20) {
            fuelDecrease = Rules.ANOMALIES_MAGNETIC_FUEL_DECREASE / 2.0f;
        } else if (i > fuelDecreaseN * 20 && i <= fuelDecreaseN * 30) {
            fuelDecrease = Rules.ANOMALIES_MAGNETIC_FUEL_DECREASE / 3.0f;
        } else {
            fuelDecrease = Rules.ANOMALIES_MAGNETIC_FUEL_DECREASE / 4.0f;
        }
        return fuelDecrease;
    }

    public static int getScoreDecrease() {
        int i = Standing.getInstance().getAstronauts().containsKey(Sticker.SCIENTIST) ? Standing.getInstance().getAstronauts().get(Sticker.SCIENTIST) : 0;
        if (i >= 0 && i <= scoreDecreaseN * 10) {
            scoreDecrease = Rules.ANOMALIES_RADIATION_SCORE_DECREASE;
        } else if (i > scoreDecreaseN * 10 && i <= scoreDecreaseN * 20) {
            scoreDecrease = (int) (Rules.ANOMALIES_RADIATION_SCORE_DECREASE / 2.0f);
        } else if (i > scoreDecreaseN * 20 && i <= scoreDecreaseN * 30) {
            scoreDecrease = (int) (Rules.ANOMALIES_RADIATION_SCORE_DECREASE / 3.0f);
        } else {
            scoreDecrease = (int) (Rules.ANOMALIES_RADIATION_SCORE_DECREASE / 4.0f);
        }
        return scoreDecrease;
    }
}