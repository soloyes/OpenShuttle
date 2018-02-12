package ru.geekbrains.game.ui;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.stargame.engine.Sprite;
import ru.geekbrains.stargame.engine.math.Rect;

/**
 * Created by sol on 2/12/18.
 */

public class Earth extends Sprite {

    private final float ROTATION = 0.05f;

    public Earth(TextureAtlas atlas) {
        super(atlas.findRegion("earth"));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.15f);
        pos.set(worldBounds.pos);
    }

    @Override
    public void update(float delta) {
        angle += ROTATION;
    }
}
