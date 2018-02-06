package ru.geekbrains.game.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.geekbrains.stargame.engine.ActionListener;
import ru.geekbrains.stargame.engine.math.Rect;
import ru.geekbrains.stargame.engine.ui.ScaledTouchUpButton;

public class ExitButton extends ScaledTouchUpButton {

    public ExitButton(TextureRegion textureRegion, float pressScale, ActionListener actionListener) {
        super(textureRegion, pressScale, actionListener);
        setHeightProportion(0.1f);
    }

    @Override
    public void resize(Rect worldBounds) {
        setBottom(worldBounds.getBottom());
        setRight(worldBounds.getRight());
    }
}