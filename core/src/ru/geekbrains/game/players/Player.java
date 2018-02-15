package ru.geekbrains.game.players;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.game.star.StarsHandler;

/**
 * Created by sol on 2/6/18.
 */

public class Player extends InsightRect {

    private final float VELOCITY = 0.4f;

    private Vector2 target;
    private Vector2 destanation;
    private Vector2 norDestanation;

    private StarsHandler stars;

    public void setTarget(Vector2 target) {
        this.target = target;
        checkAndHandleBounds();
        tmp1.set(target);
        setAngle(tmp1.sub(pos).angle() - 90);
        tmp1.set(target);
        stars.setVAngle(tmp1.sub(pos).angle());
    }

    public Player(TextureAtlas atlas, StarsHandler stars) {
        super(atlas.findRegion("rocket"), 1, 12, 12);
        target = new Vector2(0.0f, 0.0f);
        norDestanation = new Vector2();
        destanation = new Vector2();

        setHeightProportion(0.15f);
        pos.set(0.0f, 1f);
        setAngle(180);
        this.stars = stars;

        initParkingRect(this.getWidth(), this.getHeight());
    }

    private void checkAndHandleBounds(){
        if (worldBounds.getRight() - target.x < parkingRect.getHalfWidth())
            target.x = worldBounds.getRight() - parkingRect.getHalfWidth() / 2;
        if (target.y - worldBounds.getBottom() < parkingRect.getHalfHeight())
            target.y = worldBounds.getBottom() + parkingRect.getHalfHeight() / 2;
        if (target.x - worldBounds.getLeft() < parkingRect.getHalfWidth())
            target.x = worldBounds.getLeft() + parkingRect.getHalfWidth() / 2;
        if (worldBounds.getTop() - target.y < parkingRect.getHalfWidth())
            target.y = worldBounds.getTop() - parkingRect.getHalfWidth() / 2;
    }

    @Override
    public void update(float delta) {
        //Calculate player target
        if  (!this.isMe(target)) {
            tmp2.set(target);
            destanation = tmp2.sub(pos);
            tmp3.set(destanation);
            norDestanation = tmp3.nor();
            tmp3.set(norDestanation);
            //Parking
            if (destanation.len() > delta) {
                pos.mulAdd(tmp3, VELOCITY * delta);
                frame += 1;
                frame %= 12;
            }
            else {
                pos.set(target);
            }
            //
        }
        else {
            frame = 0;
        }
        //
    }
}
