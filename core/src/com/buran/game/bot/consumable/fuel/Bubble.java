package com.buran.game.bot.consumable.fuel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Buran on 09/12/18.
 */

public class Bubble {
    //0.27f (texture size to isLeft/right line)
    //0.156f (texture size to up/down line)
    private static float A = 0.27f;
    private static float B = 0.156f;

    private TextureRegion texture;
    private Fuel fuel;
    private int width, height;
    private Vector2 position;
    private Vector2 velocity;
    @Getter
    @Setter
    protected float scaleX, scaleY;

    public Bubble(Fuel fuel) {
        this.fuel = fuel;
        this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.FUEL_BUBBLE_REGION);
        this.width = PaintConstants.FUEL_BUBBLE_WIDTH;
        this.height = PaintConstants.FUEL_BUBBLE_HEIGHT;
        this.position = new Vector2();
        this.velocity = new Vector2();
    }

    public void init(float x, float y) {
        position.set(MathUtils.random(0, x - x * A * 2 - width), MathUtils.random(0, y - y * B * 2 - height));
        scaleX = scaleY = MathUtils.random(0.5f, 1.0f);
        velocity.set(0, MathUtils.random(5, 25));
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        if (position.y + height > fuel.getHeight() - fuel.getHeight() * B * 2) {
            position.y = fuel.getHeight() - height - fuel.getHeight() * B * 2;
            velocity.set(velocity.x, -velocity.y);
        }
        if (position.y < 0) {
            position.y = 0;
            velocity.set(velocity.x, -velocity.y);
        }
    }

    public void render(SpriteBatch batch) {
        Color tmpColor = batch.getColor();
        batch.setColor(fuel.getColor());
        batch.draw(texture,
                fuel.getPosition().x - fuel.getWidth() / 2 + fuel.getWidth() * A + position.x, fuel.getPosition().y - fuel.getHeight() / 2 + fuel.getHeight() * B + position.y,
                fuel.getWidth() / 2 - fuel.getWidth() * A - position.x, fuel.getHeight() / 2 - fuel.getHeight() * B - position.y,
                width, height,
                fuel.getScaleX() * scaleX, fuel.getScaleY() * scaleY,
                fuel.getAngle()
        );
        batch.setColor(tmpColor);
    }
}