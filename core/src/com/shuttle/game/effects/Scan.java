package com.shuttle.game.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.consumable.AbstractConsumable;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.utils.Assets;

import lombok.Setter;

/**
 * @author Shuttle on 12/17/18.
 */

public class Scan {
    private TextureRegion texture;
    private AbstractConsumable consumable;
    @Setter
    private Color color;
    private Oscillator oscillatorA;
    @Setter
    private float time;
    private float frequency;
    private float scaleFactor;
    private Vector2 position;

    public Scan(AbstractConsumable consumable) {
        this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.BOX_SCAN_NOISE_REGION);
        this.consumable = consumable;
        this.color = Color.valueOf("00ffffff");
        this.oscillatorA = new Oscillator();
        switch (consumable.getType()) {
            case CRATE:
                this.scaleFactor = 1.0f;
                break;
            case STORAGE:
                this.scaleFactor = 0.5f;
                break;
        }
        this.position = new Vector2();
        Shake.getInstance().subscribe(position);
    }

    public void render(SpriteBatch batch) {
        Color tmpColor = batch.getColor();
        batch.setColor(color);
        batch.draw(texture,
                position.x, position.y,
                consumable.getWidth() / 2, consumable.getHeight() / 2,
                consumable.getWidth(), consumable.getHeight(),
                consumable.getScaleX() / scaleFactor, consumable.getScaleY() / scaleFactor,
                0.0f
        );
        batch.setColor(tmpColor);
    }

    public void update(float dt) {
        if (consumable.isScanned()) {
            time -= dt;
            if (time <= 1.0f) {
                consumable.setScanned(false);
                time = Rules.CONSUMABLE_SCAN_TIME;
            }
            frequency = Rules.CONSUMABLE_SCAN_TIME / time;
            frequency = frequency / Rules.CONSUMABLE_SCAN_TIME;
            frequency = 10 * frequency;
            float da = (frequency % 10) / 10;
            float minA = PaintConstants.SCAN_ALPHA_MIN - da;
            float maxA = PaintConstants.SCAN_ALPHA_MAX - da;
            if (minA <= 0.0f) {
                minA = 0.0f;
            }
            if (maxA <= 0.0f) {
                maxA = 0.0f;
            }
            color.a = oscillatorA.oscillate(color.a, frequency, minA, maxA, dt);
        }
        position.x = consumable.getPosition().x - consumable.getWidth() / 2;
        position.y = consumable.getPosition().y - consumable.getHeight() / 2;
    }
}