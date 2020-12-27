package com.buran.game.ui.help;

import com.badlogic.gdx.scenes.scene2d.Group;

public class Tutorial extends Group {
    private HandPointer handPointer;

    public Tutorial() {
        this.handPointer = new HandPointer();
        this.addActor(handPointer);
    }
}
