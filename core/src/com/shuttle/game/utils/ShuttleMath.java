package com.shuttle.game.utils;

/**
 * @author soloyes on 03/07/18.
 */

public class ShuttleMath {
    public static float checkAngleBetween0And360(float angle) {
        if (angle < 0.0f) {
            angle += 360.0f;
        }
        if (angle > 360.0f) {
            angle -= 360.0f;
        }
        return angle;
    }

    public static float getRotationDirection(float angleFrom, float angleTo) {
        if (angleFrom >= angleTo) {
            if (Math.abs(angleFrom - angleTo) <= 180.0f) {
                return -1.0f;
            } else {
                return 1.0f;
            }
        } else {
            if (Math.abs(angleFrom - angleTo) <= 180.0f) {
                return 1.0f;
            } else {
                return -1.0f;
            }
        }
    }
}