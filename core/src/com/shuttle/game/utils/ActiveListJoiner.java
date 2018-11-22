package com.shuttle.game.utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Shuttle on 11/22/18.
 */
public class ActiveListJoiner<T> {
    @SafeVarargs
    public final List<List<? extends T>> join(List<? extends T>... list) {
        List<List<? extends T>> l = new LinkedList<List<? extends T>>();
        l.addAll(Arrays.asList(list));
        return l;
    }
}
