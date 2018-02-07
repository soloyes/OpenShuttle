package ru.geekbrains.game.star;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.engine.Sprite;
import ru.geekbrains.stargame.engine.math.Rect;
import ru.geekbrains.stargame.engine.math.Rnd;

public class Star extends Sprite {

    private final Vector2 v = new Vector2();
    private Rect worldBounds;

    public Star(Texture texture, float vx, float vy, float height) {
        super(texture);
        v.set(vx, vy);
        setHeightProportion(Rnd.nextFloat(height, height*2));
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        checkAndHandleBounds();
    }

    protected void checkAndHandleBounds() {
        if (getRight() < worldBounds.getLeft()) setLeft(worldBounds.getRight());
        if (getLeft() > worldBounds.getRight()) setRight(worldBounds.getLeft());
        if (getTop() < worldBounds.getBottom()) setBottom(worldBounds.getTop());
        if (getBottom() > worldBounds.getTop()) setTop(worldBounds.getBottom());
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        float posX = Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight());
//        float posX = worldBounds.getLeft() + 0.2f;
        float posY = Rnd.nextFloat(worldBounds.getBottom(), worldBounds.getTop());
//        float posY = worldBounds.getTop();
        pos.set(posX, posY);
    }
}