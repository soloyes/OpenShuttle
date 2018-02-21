package ru.geekbrains.game.ui.players;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.Map;

import ru.geekbrains.game.logic.Score;
import ru.geekbrains.stargame.engine.Sprite;
import ru.geekbrains.stargame.engine.Rect;
import ru.geekbrains.stargame.engine.utils.math.Rnd;

/**
 * Created by sol on 2/15/18.
 * Class storing worldCoordinates, parking rectangle and rectangle for action (interact with others)
 * Also Base class for every player and bot
 */

public abstract class InsightRect extends Sprite {

    protected int rotation = 1;
    private boolean loud;

    protected Map<String, Object> music;

    protected Rect parkingRect = new Rect();
    protected Rect worldBounds;

    protected Vector2 tmp1 = new Vector2();
    protected Vector2 tmp2 = new Vector2();
    protected Vector2 tmp3 = new Vector2();
    protected Vector2 norDirection = new Vector2();
    private Vector2 newItem = new Vector2();

    protected Score score;
    protected Player player;
    protected Sound itemSound;

    public void setScore(Score score) {
        this.score = score;
    }

    public boolean isLoud() {
        return loud;
    }

    public void setLoud(boolean loud) {
        this.loud = loud;
    }

    protected InsightRect(){}

    protected InsightRect(TextureRegion region) {
        super(region);
    }

    protected InsightRect(TextureRegion region, int i, int j, int frames) {
        super(region, i, j, frames);
    }

    protected void initInsightRect(float width, float height, float times) {
        //Side size = base sprite diagonale size
        parkingRect.setWidth((float)
                Math.sqrt(Math.pow(width, 2) +
                        Math.pow(height, 2)));
        parkingRect.setHeight(parkingRect.getWidth());
        //
    }

    protected void setPosInsightRect(Sprite base){
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

    public void newItem(InsightRect base){
        parkingRect.pos.set(generate(Side.randomSide()));
        base.pos.set(parkingRect.pos);

        tmp2.set(Rnd.nextFloat(-worldBounds.getWidth() / 4,worldBounds.getWidth() / 4),
                Rnd.nextFloat(-worldBounds.getHeight() / 4,worldBounds.getHeight() / 4));
        tmp1.set(base.pos);
        tmp2.sub(tmp1);
        norDirection.set(tmp2.nor());

        base.setAngle(base.getAngle() + 360 / (Rnd.nextInt(6) + 1));
        rotation = Rnd.nextInt(3) - 1;

        if (isLoud()) {
            base.itemSound.play();
        }
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
