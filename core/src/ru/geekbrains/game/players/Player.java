package ru.geekbrains.game.players;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.engine.Sprite;

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

    public void setTarget(Vector2 target) {
        this.target = target;
        tmp.set(target);
        setAngle(tmp.sub(pos).angle() - 90);
    }

    public Player(Texture texture) {
        super(texture);
        tmp = new Vector2();
        tmp2 = new Vector2();
        tmp3 = new Vector2();
        setHeightProportion(0.15f);
        velocity = 1f;
        pos.set(0.0f,-0.0f);
        target = new Vector2();
        norDestanation = new Vector2();
        destanation = new Vector2();
    }

    public void update(float delta){
        tmp2.set(target);
        destanation = tmp2.sub(pos);
        tmp3.set(destanation);
        norDestanation = tmp3.nor();
        tmp3.set(norDestanation);
        if (destanation.len2() > 0.0001f) {
            pos.add(tmp3.scl(velocity*delta));
        }
    }
}
