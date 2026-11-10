package com.buran.game.effects;

import com.buran.game.screens.GameScreen;

/**
 * Small doses of game feel: hit-stop (a few frames of near-freeze on heavy impacts) and
 * full-screen flashes. Kept in one place so the intensity can be tuned or switched off.
 */
public final class Juice {
    private static float hitStop;

    private Juice() {
    }

    /** Freeze the simulation for {@code seconds} of real time (the longest request wins). */
    public static void hitStop(float seconds) {
        hitStop = Math.max(hitStop, seconds);
    }

    /** Applies hit-stop to a frame delta. Called once per frame by the game loop. */
    public static float filter(float dt) {
        if (hitStop > 0f) {
            hitStop -= dt;
            return dt * 0.05f;
        }
        return dt;
    }

    public static void flash(float strength, float r, float g, float b) {
        GameScreen.flash(strength, r, g, b);
    }

    /** Hull or shield took a hit of the given size. */
    public static void damage(float amount, boolean shieldUp) {
        float s = Math.min(0.22f, 0.05f + amount * 0.02f);
        if (shieldUp) {
            flash(s * 0.6f, 0.35f, 0.85f, 1f);
        } else {
            flash(s, 1f, 0.22f, 0.12f);
        }
    }

    public static void lifeLost() {
        flash(0.55f, 1f, 0.2f, 0.1f);
        hitStop(0.14f);
    }

    public static void gameOver() {
        flash(0.75f, 1f, 1f, 1f);
        hitStop(0.3f);
    }

    public static void kill() {
        flash(0.05f, 1f, 0.95f, 0.85f);
        hitStop(0.02f);
    }

    public static void reset() {
        hitStop = 0f;
    }
}
