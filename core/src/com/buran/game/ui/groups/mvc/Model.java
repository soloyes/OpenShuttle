package com.buran.game.ui.groups.mvc;

import java.util.List;

/**
 * @author soloyes on 21/06/19.
 */

public interface Model<T> {

    T addEmpty();

    void addDummy();

    void write();

    void setEmpty(T t);

    void remove(T t);

    void dispose();

    void flush();

    void activate(T t);

    T getActive();

    List<T> getList();
}