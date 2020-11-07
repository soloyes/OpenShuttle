package com.buran.game.bot.consumable.fuel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author Buran on 09/12/18.
 */

public class Box {
    private TextureRegion texture;
    private Fuel fuel;
    private int width, height;

    public Box(Fuel fuel) {
        this.fuel = fuel;
        this.width = PaintConstants.FUEL_WIDTH;
        this.height = PaintConstants.FUEL_HEIGHT;
    }

    public void init() {
        if (fuel.isGolden()) {
            this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.FUEL_GOLD_REGION);
        } else {
            this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.FUEL_REGION);
        }
    }

    public void render(SpriteBatch batch) {
        Color tmpColor = batch.getColor();
        batch.setColor(fuel.getColor());
        batch.draw(texture,
                fuel.getPosition().x - width / 2, fuel.getPosition().y - height / 2,
                width / 2, height / 2,
                width, height,
                fuel.getScaleX(), fuel.getScaleY(),
                fuel.getAngle()
        );
        batch.setColor(tmpColor);
    }
}