package ru.geekbrains.game.players;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.engine.Sprite;
import ru.geekbrains.stargame.engine.math.Rect;
import ru.geekbrains.stargame.engine.math.Rnd;

/**
 * Created by sol on 2/6/18.
 */

public class Astronaut extends Sprite{

    private Rect worldBounds;

    private float velocity = 0.1f;
    private Vector2 norDirection = new Vector2();
    private Vector2 tmp;
    private Vector2 tmp1;
    private int rotation = 1;

    public Astronaut(TextureAtlas atlas) {
        super(atlas.findRegion("astronaut"));
        tmp = new Vector2();
        tmp1 = new Vector2();
        setHeightProportion(0.1f);
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(norDirection, delta * velocity);
        this.angle += (0.5f * rotation) % 360;
        checkAndHandleBounds();
    }

    private void generate(Side side){
        float x = 0;
        float y = 0;
        switch (side){
            case NORTH:
                x = Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight());
                y = Rnd.nextFloat(worldBounds.getTop(), worldBounds.getTop());
                break;
            case SOUTH:
                x = Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight());
                y = Rnd.nextFloat(
                        worldBounds.getBottom(),
                        worldBounds.getBottom() - this.getHeight());
                break;
            case WEST:
                y = Rnd.nextFloat(worldBounds.getBottom(), worldBounds.getTop());
                x = Rnd.nextFloat(
                        worldBounds.getLeft() - this.getWidth(),
                        worldBounds.getLeft());
                break;
            case EAST:
                y = Rnd.nextFloat(worldBounds.getBottom(), worldBounds.getTop());
                x = Rnd.nextFloat(
                        worldBounds.getRight(),
                        worldBounds.getRight() + this.getWidth());
        }
        pos.set(x, y);
        tmp1.set(Rnd.nextFloat(worldBounds.getWidth() / 4,worldBounds.getWidth() / 4),
                Rnd.nextFloat(-worldBounds.getHeight() / 4,worldBounds.getHeight() / 4));
        tmp.set(pos);
        tmp1.sub(tmp);
        norDirection.set(tmp1.nor());

        this.angle += 360 / (Rnd.nextInt(6) + 1);
        this.rotation = Rnd.nextInt(3) - 1;
    }

     private void checkAndHandleBounds() {
        if (this.getRight() < worldBounds.getLeft() ||
                        this.getLeft() > worldBounds.getRight() ||
                        this.getTop() < worldBounds.getBottom() ||
                        this.getBottom() > worldBounds.getTop())
        {
            generate(Side.randomSide());
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        generate(Side.EAST);
    }

    private enum Side{
        EAST, WEST, SOUTH, NORTH;
        public static Side randomSide(){
            return Side.values()[Rnd.nextInt(Side.values().length)];
        }
    }
}
