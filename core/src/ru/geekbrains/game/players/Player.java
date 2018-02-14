package ru.geekbrains.game.players;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.game.star.StarsHandler;
import ru.geekbrains.stargame.engine.Sprite;

/**
 * Created by sol on 2/6/18.
 */

public class Player extends Sprite {

    private Vector2 target;
    private Vector2 destanation;
    private Vector2 norDestanation;
    private final float VELOCITY = 0.5f;
    private Vector2 tmp;
    private Vector2 tmp2;
    private Vector2 tmp3;
    private StarsHandler stars;

    public void setTarget(Vector2 target) {
        this.target = target;
        tmp.set(target);
        setAngle(tmp.sub(pos).angle() - 90);
        tmp.set(target);
        stars.setVAngle(tmp.sub(pos).angle());
    }

    public Player(TextureAtlas atlas, StarsHandler stars) {
        super(atlas.findRegion("rocket"), 1, 12, 12);
        tmp = new Vector2();
        tmp2 = new Vector2();
        tmp3 = new Vector2();
        target = new Vector2(0.0f, 0.0f);
        norDestanation = new Vector2();
        destanation = new Vector2();

        setHeightProportion(0.15f);
        pos.set(0.0f, 1f);
        setAngle(180);

        this.stars = stars;
    }

    public void update(float delta) {
        //For smooth player manipulation
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
            else pos.set(target);
            //
        }
        else {
            frame = 0;
        }
        //
    }
}
