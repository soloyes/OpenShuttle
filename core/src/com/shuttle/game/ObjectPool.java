package com.shuttle.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FlameXander on 23.09.2017.
 */
public abstract class ObjectPool<T extends Poolable> {
    protected List<T> activeList;
    protected List<T> freeList;

    public ObjectPool() {
        this.activeList = new ArrayList<T>();
        this.freeList = new ArrayList<T>();
    }

    public void addObjectsToFreeList(int size) {
        for (int i = 0; i < size; i++) {
            freeList.add(newObject());
        }
    }

    protected abstract T newObject();

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

    public T getActiveElement() {
        if (freeList.size() == 0) {
            freeList.add(newObject());
        }
        T temp = freeList.remove(freeList.size() - 1);
        activeList.add(temp);
        return temp;
    }

    public List<T> getActiveList() {
        return activeList;
    }

    public List<T> getFreeList() {
        return freeList;
    }
}
