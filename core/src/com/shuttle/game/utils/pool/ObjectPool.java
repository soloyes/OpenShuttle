package com.shuttle.game.utils.pool;

import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.conf.GlobalInteractionLevels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by FlameXander on 23.09.2017.
 */
public abstract class ObjectPool<T extends Poolable> {
    protected List<T> activeList;
    protected List<T> freeList;
    public static HashMap<Class<? extends Poolable>, ObjectPool> EMITTERS = new HashMap<Class<? extends Poolable>, ObjectPool>();

    public ObjectPool(Class<? extends T> clazz) {
        this.activeList = new ArrayList<T>();
        this.freeList = new ArrayList<T>();
        EMITTERS.put(clazz, this);
    }

    public void boom(Vector2 position, GlobalInteractionLevels none) {
    }

    public void checkPool() {
        for (int i = activeList.size() - 1; i >= 0; i--) {
            if (!activeList.get(i).isActive()) {
                free(i);
            }
        }
    }

    public void free(int index) {
        freeList.add(activeList.remove(index));
    }

    public void doRestart() {
        activeList.clear();
        freeList.clear();
    }

    public T getActiveElement() {
        if (freeList.size() == 0) {
            freeList.add(newObject());
        }
        T temp = freeList.remove(freeList.size() - 1);
        activeList.add(temp);
        return temp;
    }

    protected abstract T newObject();

    public List<T> getActiveList() {
        return activeList;
    }

    public List<T> getFreeList() {
        return freeList;
    }
}
