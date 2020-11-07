package com.buran.game.ui.groups.configs;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * @author soloyes on 10/12/19.
 */

public class ConfigScrollPane extends ScrollPane {
    public ConfigScrollPane(Actor widget, Skin skin) {
        super(widget, skin);
    }

    @Override
    protected float getMouseWheelX() {
        return 0.0f;
    }

    @Override
    protected float getMouseWheelY() {
        return 0.0f;
    }
}