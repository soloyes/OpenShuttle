package com.shuttle.game.bot.asteroids;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.effects.MoveProcessor;
import com.shuttle.game.utils.BoundCheck;
import com.shuttle.game.utils.pool.AbstractPoolable;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 11/26/18.
 */
public abstract class AbstractAsteroid extends AbstractPoolable {
    protected TextureRegion[] textureRegions;
    @Getter
    @Setter
    private boolean fragmented;
    @Getter
    @Setter
    private float sizeForHarvest;
    //Burn with laser
    @Getter
    @Setter
    private float timeToBurn;

    public AbstractAsteroid() {
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.angle = MathUtils.random(365f);
        this.active = true;
        this.boundCheck = new BoundCheck();
        this.moveProcessor = new MoveProcessor();
        this.color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        this.spreeColor = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public void destroy() {
        active = false;
    }

    @Override
    public void basicRender(SpriteBatch batch) {
        Color tmpColor = batch.getColor();
        batch.setColor(color);
        batch.draw(
                textureRegions[(int) frame],
                position.x - width / 2,
                position.y - height / 2,
                width / 2, height / 2,
                width, height,
                scaleX, scaleY,
                angle
        );
        batch.setColor(tmpColor);
    }

    @Override
    public void basicUpdate(float dt) {
        frame += 24 * dt;
        frame %= textureRegions.length - 1;
    }

    @Override
    public void render(SpriteBatch batch) {
        basicRender(batch);
    }

    @Override
    public void update(float dt) {
        basicUpdate(dt);
        moveProcessor.update(this, dt);
        boundCheck.checkAndHandleBoundsAndDestroy(this);
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void init(Vector2 position, Vector2 destination, float scale, boolean isFragment) {
        init(position.x, position.y);
        this.fragmented = isFragment;
        destination.sub(position).nor();
        if (isFragment) {
            velocity.set(destination).scl(MathUtils.random(25, 150));
        } else {
            velocity.set(destination).scl(MathUtils.random(50, 300));
        }
        this.scaleX = this.saveScaleX = scale;
        this.scaleY = this.saveScaleY = scale;
        this.color.set(1.0f, 1.0f, 1.0f, 1.0f);
        this.spreeColor.set(1.0f, 1.0f, 1.0f, 1.0f);
        this.spiralRadius = MathUtils.random(20, 60);
        this.atRayPoint = false;
    }

    public abstract void init(float x, float y);
}
