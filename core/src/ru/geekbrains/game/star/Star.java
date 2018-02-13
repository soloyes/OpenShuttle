package ru.geekbrains.game.star;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.engine.Sprite;
import ru.geekbrains.stargame.engine.math.Rect;
import ru.geekbrains.stargame.engine.math.Rnd;

public class Star extends Sprite {

    private Vector2 v = new Vector2();
    private Rect worldBounds;

    public Vector2 getV() {
        return v;
    }

    public void setV(Vector2 v) {
        this.v = v;
    }

    public Star(TextureRegion region, float vx, float vy, float height) {
        super(region);
        v.set(vx, -vy);
        setHeightProportion(Rnd.nextFloat(height, height*2));
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        checkAndHandleBounds();
    }

    protected void checkAndHandleBounds() {
        if (this.getRight() < worldBounds.getLeft()) setLeft(worldBounds.getRight());
        if (this.getLeft() > worldBounds.getRight()) setRight(worldBounds.getLeft());
        if (this.getTop() < worldBounds.getBottom()) setBottom(worldBounds.getTop());
        if (this.getBottom() > worldBounds.getTop()) setTop(worldBounds.getBottom());
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        float posX = Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight());
        float posY = Rnd.nextFloat(worldBounds.getBottom(), worldBounds.getTop());
        pos.set(posX, posY);
    }
}