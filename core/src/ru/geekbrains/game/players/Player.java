package ru.geekbrains.game.players;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

    public void setTarget(Vector2 target) {
        this.target = target;
    }

    public Player(TextureRegion region) {
        super(region);
        setHeightProportion(0.1f);
        velocity = 1f;
        pos.set(0.0f,0.0f);
        target = new Vector2();
        norDestanation = new Vector2();
        destanation = new Vector2();
    }

    public void update(float delta){
        destanation = target.cpy().sub(pos);
        norDestanation = destanation.cpy().nor();
        if (destanation.len2() > 0.0001f) {
            pos.add(norDestanation.cpy().scl(velocity*delta));
        }
    }
}
