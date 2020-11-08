package com.buran.game.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * @author soloyes on 16/04/20.
 */

public class UIButton extends Button {
    public UIButton(Drawable imageUp) {
        super(imageUp);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float a;
        if (isPressed()) {
            a = 0.62f;
        } else {
            a = 1.0f;
        }
        super.draw(batch, a);
    }
}