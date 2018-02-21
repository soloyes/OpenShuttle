package ru.geekbrains.game.ui.space;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.stargame.engine.Sprite;
import ru.geekbrains.stargame.engine.Rect;

/**
 * Created by sol on 2/12/18.
 * Class for store and manipulate with Earth.
 * The background is solud color + starts, which generate by StarsHandler.
 */

public class Earth extends Sprite {

    private final float ROTATION = -2f;
    private float r;
    private boolean scaleOut;

    public Earth(TextureAtlas atlas) {
        super(atlas.findRegion("planet"), 6, 6, 32);
        scaleOut = false;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.15f);
        pos.set(worldBounds.pos);
    }

    @Override
    public void update(float delta) {
        angle += ROTATION * delta;
        angle %= 360;
        r += 10 * delta;
        if (r >= 1) {
            frame ++;
            r = 0;
        }
        frame %= 32;

        if (scaleOut) {
            scale -= 0.007f * delta;
            if (scale <= 0.8) {
                scaleOut = false;
            }
        }
        else {
            scale += 0.007f * delta;
            if (scale >= 1.5) scaleOut = true;
        }
    }
}
