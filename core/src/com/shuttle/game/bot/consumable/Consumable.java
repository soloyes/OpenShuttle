package com.shuttle.game.bot.consumable;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.pool.AbstractPoolable;
import com.shuttle.game.utils.BoundCheck;

public class Consumable extends AbstractPoolable {
    protected TextureRegion texture;

    public Consumable() {
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.active = false;
        this.boundCheck = new BoundCheck();
    }

    @Override
    public void init(float x, float y) {
        position.set(x, y);
        angle = MathUtils.random(0, 360);
        rotationSpeed = MathUtils.random(-20, 20);
        active = true;
        scaleX = saveScaleX = 1.0f;
        scaleY = saveScaleY = 1.0f;
    }

    @Override
    public void basicRender(SpriteBatch batch) {
        batch.draw(texture,
                position.x - width / 2, position.y - height / 2,
                width / 2, height / 2,
                width, height,
                scaleX, scaleY,
                angle
        );
    }

    @Override
    public void update(float dt) {
        basicUpdate(dt);
        boundCheck.checkAndHandleBoundsAndDestroy(this);
        position.mulAdd(velocity, dt);
    }

    @Override
    public void basicUpdate(float dt) {
        angle += rotationSpeed * dt;
    }

    @Override
    public void render(SpriteBatch batch) {
        basicRender(batch);
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }
}