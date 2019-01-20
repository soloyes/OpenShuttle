package com.shuttle.game.bot.consumable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.effects.Oscillator;
import com.shuttle.game.utils.Assets;

import lombok.Setter;

public class Shield {
    private TextureRegion texture;
    private AbstractConsumable consumable;
    private float width, height;
    @Setter
    private Color color;
    private Oscillator oscillatorA;
    @Setter
    private float time;
    private float frequency;

    public Shield(AbstractConsumable consumable) {
        this.consumable = consumable;
        switch (consumable.getType()) {
            case FUEL:
                this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.SHIELD_FUEL_REGION);
                this.width = PaintConstants.SHIELD_FUEL_WIDTH;
                this.height = PaintConstants.SHIELD_FUEL_HEIGHT;
                break;
            case STORAGE:
                this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.SHIELD_STORAGE_REGION);
                this.width = PaintConstants.SHIELD_STORAGE_WIDTH;
                this.height = PaintConstants.SHIELD_STORAGE_HEIGHT;
                break;
            case CRATE:
                this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.SHIELD_CRATE_REGION);
                this.width = PaintConstants.SHIELD_CRATE_WIDTH;
                this.height = PaintConstants.SHIELD_CRATE_HEIGHT;
                break;
        }
        this.color = Color.valueOf("00ffffff");
        this.oscillatorA = new Oscillator();
    }

    public void render(SpriteBatch batch) {
        Color tmpColor = batch.getColor();
        batch.setColor(color);
        batch.draw(texture,
                consumable.getPosition().x - width / 2, consumable.getPosition().y - height / 2,
                width / 2, height / 2,
                width, height,
                consumable.getScaleX(), consumable.getScaleY(),
                consumable.getAngle()
        );
        batch.setColor(tmpColor);
    }

    public void update(float dt) {
        if (consumable.isShielded()) {
            time -= dt;
            if (time <= 1.0f) {
                consumable.setShielded(false);
                time = Rules.CONSUMABLE_SHIELD_TIME;
            }
            frequency = Rules.CONSUMABLE_SHIELD_TIME / time;
            frequency = frequency / Rules.CONSUMABLE_SHIELD_TIME;
            frequency = 10 * frequency;
            float da = (frequency % 10) / 10;
            float minA = PaintConstants.SHIELD_ALPHA_MIN - da;
            float maxA = PaintConstants.SHIELD_ALPHA_MAX - da;
            if (minA <= 0.0f) {
                minA = 0.0f;
            }
            if (maxA <= 0.0f) {
                maxA = 0.0f;
            }
            color.a = oscillatorA.oscillate(color.a, frequency, minA, maxA, dt);
        }
    }
}
