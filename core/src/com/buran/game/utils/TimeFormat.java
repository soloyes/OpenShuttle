package com.buran.game.utils;

import java.util.Formatter;
import java.util.Locale;

/**
 * @author soloyes on 12/04/19.
 */

public class TimeFormat {
    private final static long ONE_SECOND = 1;
    private final static long SECONDS_IN_A_MINUTE = 60;

    private final static long ONE_MINUTE = ONE_SECOND * 60;
    private final static long MINUTES_IN_AN_HOUR = 60;

    private final static long ONE_HOUR = ONE_MINUTE * 60;
    private final static long HOURS_IN_A_DAY = 24;
    private final static long ONE_DAY = ONE_HOUR * 24;
    private final static long DAYS_IN_A_YEAR = 365;
    private static StringBuilder stringBuilder = new StringBuilder();
    private static Formatter formatter = new Formatter(stringBuilder, Locale.ENGLISH);

    public static String formatYDHMS(Number n) {
        String res = "";
        if (n != null) {
            long duration = n.longValue();

            duration /= ONE_SECOND;
            int seconds = (int) (duration % SECONDS_IN_A_MINUTE);
            duration /= SECONDS_IN_A_MINUTE;
            int minutes = (int) (duration % MINUTES_IN_AN_HOUR);
            duration /= MINUTES_IN_AN_HOUR;
            int hours = (int) (duration % HOURS_IN_A_DAY);
            duration /= HOURS_IN_A_DAY;
            int days = (int) (duration % DAYS_IN_A_YEAR);
            duration /= DAYS_IN_A_YEAR;
            int years = (int) (duration);
            if (days == 0) {
                res = String.format(Locale.ENGLISH, "%02dh %02dm %02ds", hours, minutes, seconds);
            } else if (years == 0) {
                res = String.format(Locale.ENGLISH, "%dD %02dh %02dm %02ds", days, hours, minutes, seconds);
            } else {
                res = String.format(Locale.ENGLISH, "%dY %dD %02dh% 02dm% 02ds", years, days, hours, minutes, seconds);
            }
        }
        return res;
    }

    public static StringBuilder formatMS(float n) {
        stringBuilder.setLength(0);
        if (n >= 0.0f) {
            int duration = (int) n;

            duration /= ONE_SECOND;
            int seconds = (int) (duration % SECONDS_IN_A_MINUTE);
            duration /= SECONDS_IN_A_MINUTE;
            int minutes = (int) (duration % MINUTES_IN_AN_HOUR);
            duration /= MINUTES_IN_AN_HOUR;
            int hours = (int) (duration % HOURS_IN_A_DAY);
            duration /= HOURS_IN_A_DAY;
            int days = (int) (duration % DAYS_IN_A_YEAR);
            duration /= DAYS_IN_A_YEAR;
            int years = duration;
            if (hours == 0) {
                formatter.format("%02d:%02d", minutes, seconds);
            } else if (days == 0) {
                formatter.format("%02d %02d %02d", hours, minutes, seconds);
            } else if (years == 0) {
                formatter.format("%d %02d %02d %02d", days, hours, minutes, seconds);
            } else {
                formatter.format("%d %d %02d %02d %02d", years, days, hours, minutes, seconds);
            }
        }
        return stringBuilder;
    }
}