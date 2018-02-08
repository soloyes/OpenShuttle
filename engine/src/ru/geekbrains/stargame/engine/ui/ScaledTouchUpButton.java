package ru.geekbrains.stargame.engine.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.engine.ActionListener;
import ru.geekbrains.stargame.engine.Sprite;

public class ScaledTouchUpButton extends Sprite {

    private static final float BUTTON_PRESS_SCALE = 0.9f;
    private static final float BUTTON_SIZE_SCALE = 0.15f;
    private int pointer;
    private boolean pressed;
    private final ActionListener actionListener;

    public ScaledTouchUpButton(TextureRegion region, ActionListener actionListener) {
        super(region);
        setHeightProportion(BUTTON_SIZE_SCALE);
        this.actionListener = actionListener;
    }

    @Override
    public void touchDown(Vector2 touch, int pointer) {
        if (pressed || !isMe(touch)) {
            return;
        }
        this.pointer = pointer;
        scale = BUTTON_PRESS_SCALE;
        pressed = true;
    }

    @Override
    public void touchUp(Vector2 touch, int pointer) {
        if (this.pointer != pointer || !pressed) {
            return;
        }
        if (isMe(touch)) {
            actionListener.actionPerformed(this);
        }
        pressed = false;
        scale = 1f;
    }
}