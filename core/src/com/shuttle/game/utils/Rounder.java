package com.shuttle.game.utils;

import java.text.DecimalFormat;

/**
 * @author Shuttle on 4/22/19.
 */
public class Rounder {
    private static DecimalFormat d = new DecimalFormat("#,##0.#");
    private static StringBuilder stringBuilder = new StringBuilder();
    private static String minus = "-";
    private static String plus = "";

    public static StringBuilder round(long n) {
        boolean sign = true;
        if (n < 0) {
            sign = false;
            n = -n;
        } else if (n <= 999) {
            return stringBuilder.append(n);
        }

        final String[] units = new String[]{"", "K", "M", "B", "P"};
        int digitGroups = (int) (Math.log10(n) / Math.log10(1000));
        return stringBuilder
                .append(sign ? plus : minus)
                .append(d.format(n / Math.pow(1000, digitGroups)))
                .append(units[digitGroups]);
    }
}
