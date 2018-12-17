package com.shuttle.game.bot.consumable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.effects.Oscillator;

import lombok.Setter;

/**
 * @author Shuttle on 12/17/18.
 */
public class Scan {
    private TextureRegion texture;
    private AbstractConsumable consumable;
    private float width, height;
    @Setter
    private Color color;
    private Oscillator oscillatorA;
    @Setter
    private float time;
    private float frequency;
    private float scaleFactor;

    public Scan(AbstractConsumable consumable) {
        this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.BOX_SCAN_NOISE_REGION);
        this.consumable = consumable;
        this.color = Color.valueOf("00ffffff");
        this.oscillatorA = new Oscillator();
        switch (consumable.getType()){
            case CRATE:
                this.scaleFactor = 1.0f;
                break;
            case STORAGE:
                this.scaleFactor = 0.5f;
                break;
        }
    }

    public void render(SpriteBatch batch) {
        Color tmpColor = batch.getColor();
        batch.setColor(color);
        batch.draw(texture,
                consumable.getPosition().x - consumable.getWidth() / 2, consumable.getPosition().y - consumable.getHeight() / 2,
                consumable.getWidth() / 2, consumable.getHeight() / 2,
                consumable.getWidth(), consumable.getHeight(),
                consumable.getScaleX() / scaleFactor, consumable.getScaleY() / scaleFactor,
                0.0f
        );
        batch.setColor(tmpColor);
    }

    public void update(float dt) {

    }
}
