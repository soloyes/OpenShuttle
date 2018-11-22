package com.shuttle.game.utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Shuttle on 11/22/18.
 */
public class ActiveListJoiner<T> {

//    public static void main(String[] args) {
//        LinkedList<Astronaut> linkedList = new LinkedList<Astronaut>();
//        LinkedList<Crate> linkedList1 = new LinkedList<Crate>();
//        ActiveListJoiner<Consumable> joiner = new ActiveListJoiner<Consumable>();
//        List<List<? extends Consumable>> list = joiner.join(linkedList, linkedList1);
//        for (int i = 0; i < list.size(); i++) {
//            for (int j = 0; j < list.get(i).size(); j++) {
//                System.out.println(list.get(i).get(j));
//            }
//        }
//    }

    @SafeVarargs
    public final List<List<? extends T>> join(List<? extends T>... list) {
        List<List<? extends T>> l = new LinkedList<List<? extends T>>();
        l.addAll(Arrays.asList(list));
        return l;
    }
}
