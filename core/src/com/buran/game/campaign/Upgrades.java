package com.buran.game.campaign;

import com.buran.game.player.Standing;

/**
 * Ship upgrades earned with medals, never bought (tips 76, 77, 89): the better you fly the
 * campaign, the better the Buran gets, in the campaign and in the arcade alike.
 */
public final class Upgrades {
    public static final String[] NAMES = {
            "REINFORCED HULL", "LARGER TANKS", "EMP CAPACITOR", "RAM COILS", "SHIELD MATRIX", "LASER LENS",
    };
    public static final String[] WHAT = {
            "HULL +15%", "FUEL +15%", "EMP RECHARGES IN 6 S", "RAM SHIELD DRAINS 40% SLOWER", "SHIELD +15%", "LASER +15%",
    };
    public static final int[] COST = {4, 8, 12, 16, 20, 26};

    private Upgrades() {
    }

    /** Medals earned over the whole campaign. */
    public static int medals() {
        int m = 0;
        for (Sector s : Campaign.sectors()) {
            m += Integer.bitCount(Campaign.medals(s.number));
        }
        return m;
    }

    /** How many upgrades are unlocked. */
    public static int unlocked() {
        int m = medals();
        int n = 0;
        while (n < COST.length && m >= COST[n]) {
            n++;
        }
        return n;
    }

    public static boolean has(int i) {
        return unlocked() > i;
    }

    /** Applies the unlocked upgrades to a fresh run (after the start level is set). */
    public static void apply() {
        Standing st = Standing.getInstance();
        if (has(0)) {
            st.setMaxHp(st.getMaxHp() * 1.15f);
            st.setHp(st.getMaxHp());
        }
        if (has(1)) {
            st.setMaxFuel(st.getMaxFuel() * 1.15f);
            st.setFuel(st.getMaxFuel());
        }
        if (has(4)) {
            st.setMaxLifeShield(st.getMaxLifeShield() * 1.15f);
            st.setLifeShield(st.getMaxLifeShield());
        }
        if (has(5)) {
            st.setMaxLaserChargeLevel(st.getMaxLaserChargeLevel() * 1.15f);
        }
    }

    public static float empCooldown() {
        return has(2) ? 6f : 8f;
    }

    public static float ramDrain(float base) {
        return has(3) ? base * 0.6f : base;
    }
}
