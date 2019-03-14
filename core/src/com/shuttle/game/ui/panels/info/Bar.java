package com.shuttle.game.ui.panels.info;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.screens.GameScreen;

public class Bar {
    private TextureRegion outBar, inBar, icon, scalse;
    private GameScreen gs;

    public Bar(GameScreen gs) {
        this.gs = gs;
    }
}
