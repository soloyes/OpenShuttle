package ru.geekbrains.game.players;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.game.star.StarsHandler;
import ru.geekbrains.stargame.engine.Sprite;
import ru.geekbrains.stargame.engine.math.Rect;

/**
 * Created by sol on 2/6/18.
 */

public class Player extends Sprite {

    private Vector2 target;
    private Vector2 destanation;
    private Vector2 norDestanation;
    private float velocity;
    private Vector2 tmp;
    private Vector2 tmp2;
    private Vector2 tmp3;
    private Rect player2TimesMore;
    StarsHandler stars;

    public void setTarget(Vector2 target) {
        this.target = target;
        tmp.set(target);
        setAngle(tmp.sub(pos).angle() - 90);
        tmp.set(target);
        stars.setV( tmp.sub(pos).angle());
    }

    public Player(TextureAtlas atlas, StarsHandler stars) {
        super(atlas.findRegion("player"), 1, 6, 6);
        tmp = new Vector2();
        tmp2 = new Vector2();
        tmp3 = new Vector2();
        setHeightProportion(0.15f);
        velocity = 0.5f;
        pos.set(0.0f,-0.0f);
        target = new Vector2();
        norDestanation = new Vector2();
        destanation = new Vector2();
        this.stars = stars;
        player2TimesMore = new Rect(this.pos.x, this.pos.y, getHeight()*2, getWidth()*2);
    }

    public void update(float delta) {
        player2TimesMore.pos.set(this.pos);

        //For smooth player manipulation
        if  (!this.isMe(target)) {
            tmp2.set(target);
            destanation = tmp2.sub(pos);
            tmp3.set(destanation);
            norDestanation = tmp3.nor();
            tmp3.set(norDestanation);
            //Parking
            if (destanation.len() > delta) {
                pos.mulAdd(tmp3, velocity * delta);
                frame += 1;
                frame %= 6;
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
