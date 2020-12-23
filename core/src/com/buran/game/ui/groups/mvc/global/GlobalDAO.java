package com.buran.game.ui.groups.mvc.global;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author soloyes on 23/12/20.
 */

@ToString
public class GlobalDAO {
    @Getter
    @Setter
    private boolean hasStarted;

    private GlobalDAO() {
    }

    public GlobalDAO(boolean hasStarted) {
        this.hasStarted = hasStarted;
    }
}