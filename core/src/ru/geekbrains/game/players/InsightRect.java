package ru.geekbrains.game.players;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.geekbrains.stargame.engine.Sprite;
import ru.geekbrains.stargame.engine.math.Rect;

/**
 * Created by sol on 2/15/18.
 * Class storing worldCoordinates, parking rectangle and rectangle for action (interact with others)
 */

public class InsightRect extends Sprite {
    protected Rect parkingRect = new Rect();

    protected Rect influenceRect = new Rect();

    protected Rect worldBounds;

    protected InsightRect(TextureRegion region) {
        super(region);
    }

    public InsightRect(TextureRegion region, int i, int j, int frames) {
        super(region, i, j, frames);
    }

    protected void initParkingRect(Sprite base) {
        //Side size = insight diagonale size
        parkingRect.setWidth((float)
                Math.sqrt(Math.pow(base.getWidth(), 2) +
                        Math.pow(base.getHeight(), 2)));
        parkingRect.setHeight(parkingRect.getWidth());
        //
        setPosParkingRect(base);
    }

    public void initInfluenceRect(Sprite base) {
    }

    protected void setPosParkingRect(Sprite base){
        parkingRect.pos.set(base.pos);
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
    }
}
