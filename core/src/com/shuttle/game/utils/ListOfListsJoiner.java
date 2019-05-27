package com.shuttle.game.utils;

import com.shuttle.game.bot.anomaly.AbstractAnomaly;
import com.shuttle.game.bot.anomaly.CorrosiveAnomalyEmitter;
import com.shuttle.game.bot.anomaly.ElectricalAnomalyEmitter;
import com.shuttle.game.bot.anomaly.MagneticAnomalyEmitter;
import com.shuttle.game.bot.anomaly.RadiationAnomalyEmitter;
import com.shuttle.game.bot.asteroids.AbstractAsteroid;
import com.shuttle.game.bot.asteroids.MetalAsteroidEmitter;
import com.shuttle.game.bot.asteroids.MineralAsteroidEmitter;
import com.shuttle.game.bot.consumable.AbstractConsumable;
import com.shuttle.game.bot.consumable.AstronautEmitter;
import com.shuttle.game.bot.consumable.CrateEmitter;
import com.shuttle.game.bot.consumable.FuelEmitter;
import com.shuttle.game.bot.consumable.StorageEmitter;

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

    public static ListOfListsJoiner<AbstractAnomaly> newAbstractAnomalyList() {
        return new ListOfListsJoiner<AbstractAnomaly>(
                CorrosiveAnomalyEmitter.getInstance().getActiveList(),
                ElectricalAnomalyEmitter.getInstance().getActiveList(),
                MagneticAnomalyEmitter.getInstance().getActiveList(),
                RadiationAnomalyEmitter.getInstance().getActiveList()
        );
    }

    public static ListOfListsJoiner<AbstractAsteroid> newAbstractAsteroidList() {
        return new ListOfListsJoiner<AbstractAsteroid>(
                MetalAsteroidEmitter.getInstance().getActiveList(),
                MineralAsteroidEmitter.getInstance().getActiveList()
        );
    }

    public static ListOfListsJoiner<AbstractConsumable> newAbstractConsumableList() {
        return new ListOfListsJoiner<AbstractConsumable>(
                AstronautEmitter.getInstance().getActiveList(),
                CrateEmitter.getInstance().getActiveList(),
                FuelEmitter.getInstance().getActiveList(),
                StorageEmitter.getInstance().getActiveList()
        );
    }


}
