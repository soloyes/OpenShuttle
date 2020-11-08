package com.buran.game.utils;

import java.text.DecimalFormat;

/**
 * @author soloyes on 4/22/19.
 */

public class Rounder {
    private static DecimalFormat d = new DecimalFormat("#,##0.#");
    private static StringBuilder stringBuilder = new StringBuilder();
    private static String minus = "-";
    private static String plus = "";

    public static float convert(long n, long max) {
        stringBuilder.setLength(0);
        if (n >= max) {
            return 2.0f;
        } else if (n >= 0.8f * max) {
            return 1.8f;
        } else if (n >= 0.6f * max) {
            return 1.6f;
        } else if (n >= 0.4f * max) {
            return 1.4f;
        } else if (n >= 0.2f * max) {
            return 1.2f;
        } else {
            return 1.0f;
        }
    }

    public static StringBuilder round(long n) {
        stringBuilder.setLength(0);
        boolean sign = true;
        if (n < 0) {
            sign = false;
            n = -n;
        } else if (n <= 999) {
            return stringBuilder.append(n);
        }

        final String[] units = new String[]{"", "K", "M", "B", "T", "P", "E"};
        int digitGroups = (int) (Math.log10(n) / Math.log10(1000));
        return stringBuilder
                .append(sign ? plus : minus)
                .append(d.format(n / Math.pow(1000, digitGroups)))
                .append(units[digitGroups]);
    }
}