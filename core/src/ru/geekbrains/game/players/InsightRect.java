package ru.geekbrains.game.players;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.Map;

import ru.geekbrains.stargame.engine.Sprite;
import ru.geekbrains.stargame.engine.math.Rect;
import ru.geekbrains.stargame.engine.math.Rnd;

/**
 * Created by sol on 2/15/18.
 * Class storing worldCoordinates, parking rectangle and rectangle for action (interact with others)
 */

public abstract class InsightRect extends Sprite {

    protected Map<String, Object> music;

    protected Rect parkingRect = new Sprite();
    protected Rect influenceRect = new Rect();
    protected Rect worldBounds;

    protected Vector2 tmp1 = new Vector2();
    protected Vector2 tmp2 = new Vector2();
    protected Vector2 tmp3 = new Vector2();
    protected Vector2 norDirection = new Vector2();
    private Vector2 newItem = new Vector2();

    protected int rotation = 1;
    protected Player player;
    protected Sound itemSound;

    protected InsightRect(TextureRegion region) {
        super(region);
    }

    protected InsightRect(TextureRegion region, int i, int j, int frames) {
        super(region, i, j, frames);
    }

    protected InsightRect(){

    }

    protected void initParkingRect(float width, float height) {
        //Side size = insight diagonale size
        parkingRect.setWidth((float)
                Math.sqrt(Math.pow(width, 2) +
                        Math.pow(height, 2)));
        parkingRect.setHeight(parkingRect.getWidth());
        //
    }

    public void initInfluenceRect(Sprite base) {
    }

    protected void setPosParkingRect(Sprite base){
        parkingRect.pos.set(base.pos);
    }

    protected Vector2 generate(Side side){
        float x = 0;
        float y = 0;
        switch (side){
            case NORTH:
                x = Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight()) + parkingRect.getHalfWidth();
                y = worldBounds.getTop() + parkingRect.getHalfHeight();
                break;
            case SOUTH:
                x = Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight()) + parkingRect.getHalfWidth();
                y = worldBounds.getBottom() - parkingRect.getHalfHeight();
                break;
            case WEST:
                y = Rnd.nextFloat(worldBounds.getBottom(), worldBounds.getTop()) + parkingRect.getHalfHeight();
                x = worldBounds.getLeft() - parkingRect.getHalfWidth();
                break;
            case EAST:
                y = Rnd.nextFloat(worldBounds.getBottom(), worldBounds.getTop()) - parkingRect.getHalfHeight();
                x = worldBounds.getRight() + parkingRect.getHalfWidth();
        }
        return newItem.set(x ,y);
    }

    protected void newItem(Sprite base){
        parkingRect.pos.set(generate(Side.randomSide()));
        System.out.println(parkingRect.pos);
        base.pos.set(parkingRect.pos);

        tmp2.set(Rnd.nextFloat(-worldBounds.getWidth() / 4,worldBounds.getWidth() / 4),
                Rnd.nextFloat(-worldBounds.getHeight() / 4,worldBounds.getHeight() / 4));
        tmp1.set(base.pos);
        tmp2.sub(tmp1);
        norDirection.set(tmp2.nor());

        base.setAngle(base.getAngle() + 360 / (Rnd.nextInt(6) + 1));
        rotation = Rnd.nextInt(3) - 1;

        itemSound.play();
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
    }

    protected enum Side{
        EAST, WEST, SOUTH, NORTH;
        public static Side randomSide(){
            return Side.values()[Rnd.nextInt(Side.values().length)];
        }
    }
}
