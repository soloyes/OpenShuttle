package ru.geekbrains.game;

import com.badlogic.gdx.graphics.Texture;

import ru.geekbrains.stargame.engine.Sprite;
import ru.geekbrains.stargame.engine.math.Rect;

public class Background extends Sprite {

    public Background(Texture texture) {
        super(texture);
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(worldBounds.getHeight());
        pos.set(worldBounds.pos);
    }
}