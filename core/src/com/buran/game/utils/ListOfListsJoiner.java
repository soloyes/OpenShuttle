package com.buran.game.utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author soloyes on 11/22/18.
 */

public final class ListOfListsJoiner<T> {
    private List<List<? extends T>> list;

    @SafeVarargs
    public ListOfListsJoiner(List<? extends T>... lists) {
        this.list = new LinkedList<List<? extends T>>();
        join(lists);
    }

    @SafeVarargs
    private final void join(List<? extends T>... list) {
        this.list.addAll(Arrays.asList(list));
    }

    public T get(int i) {
        if (i < 0 || i >= size()) {
            throw new IndexOutOfBoundsException();
        } else {
            int j = 0;
            while (true) {
                if (i > list.get(j).size() - 1) {
                    i -= list.get(j).size();
                    j++;
                } else {
                    break;
                }
            }
            return list.get(j).get(i);
        }
    }

    public final int size() {
        int size = 0;
        for (int i = 0; i < list.size(); i++) {
            size += list.get(i).size();
        }
        return size;
    }

    public static ListOfListsJoiner<com.buran.game.bot.anomaly.AbstractAnomaly> newAbstractAnomalyList(List<com.buran.game.bot.anomaly.AbstractAnomaly>... lists) {
        return new ListOfListsJoiner<com.buran.game.bot.anomaly.AbstractAnomaly>(lists);
    }

    public static ListOfListsJoiner<com.buran.game.bot.asteroids.AbstractAsteroid> newAbstractAsteroidList(List<com.buran.game.bot.asteroids.AbstractAsteroid>... lists) {
        return new ListOfListsJoiner<com.buran.game.bot.asteroids.AbstractAsteroid>(lists);
    }

    public static ListOfListsJoiner<com.buran.game.bot.consumable.AbstractConsumable> newAbstractConsumableList(List<com.buran.game.bot.consumable.AbstractConsumable>... lists) {
        return new ListOfListsJoiner<com.buran.game.bot.consumable.AbstractConsumable>(lists);
    }
}