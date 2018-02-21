package ru.geekbrains.game.ui.space;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.stargame.engine.Sprite;
import ru.geekbrains.stargame.engine.Rect;

public class Background extends Sprite {

    public Background(TextureAtlas atlas) {
        super(atlas.findRegion("space"));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(worldBounds.getHeight());
        pos.set(worldBounds.pos);
    }
}