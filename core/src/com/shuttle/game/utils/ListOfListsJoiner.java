package com.shuttle.game.utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Shuttle on 11/22/18.
 */
public final class ListOfListsJoiner<T> {
    private List<List<? extends T>> list;

    @SafeVarargs
    public ListOfListsJoiner(List<? extends T>... list) {
        this.list = new LinkedList<List<? extends T>>();
        join(list);
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

    @SafeVarargs
    private final void join(List<? extends T>... list) {
        this.list.addAll(Arrays.asList(list));
    }
}
