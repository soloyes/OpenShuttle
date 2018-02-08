package ru.geekbrains.game.ui;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.stargame.engine.ActionListener;
import ru.geekbrains.stargame.engine.math.Rect;
import ru.geekbrains.stargame.engine.ui.ScaledTouchUpButton;

public class PlayButton extends ScaledTouchUpButton {

    public PlayButton(TextureAtlas atlas, ActionListener actionListener) {
        super(atlas.findRegion("play"), actionListener);
    }

    @Override
    public void resize(Rect worldBounds) {
        setBottom(worldBounds.getBottom());
        setLeft(worldBounds.getLeft());
    }
}