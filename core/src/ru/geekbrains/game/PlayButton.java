package ru.geekbrains.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.geekbrains.stargame.engine.Sprite;
import ru.geekbrains.stargame.engine.math.Rect;

/**
 * Created by sol on 2/2/18.
 */

public class PlayButton extends Sprite {
    public PlayButton(TextureRegion region) {
        super(region);
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(worldBounds.getHeight()/10);
        //pos.set(worldBounds.pos.x, worldBounds.pos.y);
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(
                regions[frame], // текущий регион
                0.2f - getWidth(), 0, // точка отрисовки
                halfWidth, halfHeight, // точка вращения
                getWidth(), getHeight(), // ширина и высота
                scale, scale, // масштаб по x и y
                angle // угол вращения
        );
    }
}
