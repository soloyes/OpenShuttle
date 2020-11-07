package com.shuttle.game.ui.groups.mvc;

/**
 * @author soloyes on 21/06/19.
 */

public interface View<T> {
    void init();

    void setController(Controller<T> controller);
}