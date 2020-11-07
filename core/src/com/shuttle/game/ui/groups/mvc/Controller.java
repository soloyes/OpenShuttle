package com.shuttle.game.ui.groups.mvc;

import java.util.List;

/**
 * @author soloyes on 21/06/19.
 */

public interface Controller<T> {

    T add();

    T add(String s);

    void remove(T t);

    void update();

    void select(T t);

    List<T> getList();
}