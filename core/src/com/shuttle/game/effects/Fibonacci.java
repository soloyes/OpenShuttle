package com.shuttle.game.effects;

import java.util.LinkedList;

public class Fibonacci {
    private static LinkedList<Integer> list = new LinkedList<Integer>();

    private static LinkedList<Integer> fibonacci(int n) {
        list.add(1);
        list.add(1);
        for (int i = 2; i < n; i++) {
            int t = list.get(i - 1) + list.get(i - 2);
            if (t >= n) {
                break;
            } else {
                list.add(t);
            }
        }
        return list;
    }

    public static LinkedList<Integer> generate(int to) {
        list.clear();
        list = fibonacci(to);
        //We no need sequence as 1 1 3 5 8... Better 1 3 5 8..
        list.removeFirst();
        return list;
    }
}
