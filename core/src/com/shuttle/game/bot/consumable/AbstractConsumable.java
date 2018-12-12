package com.shuttle.game.bot.consumable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.pool.AbstractPoolable;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.BoundCheck;
import com.shuttle.game.utils.effects.BoomBox;

import lombok.Setter;

public abstract class AbstractConsumable extends AbstractPoolable implements Consumable {
    protected TextureRegion texture;
    @Setter
    protected boolean labeled;
    protected Label label;

    public AbstractConsumable(GameScreen gs) {
        this.gs = gs;
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.active = false;
        this.boundCheck = new BoundCheck();
        this.boomBox = new BoomBox();
        this.color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        this.spreeColor = Color.GREEN;
        this.level = -1;
        this.label = new Label(Label.Sticker.PATTERN);
        this.label.setColor(Color.BLACK);
        this.label.setMaster(this);
    }

    public abstract void init(float x, float y);

    public abstract void consumed(GameScreen gs);

    @Override
    public void basicRender(SpriteBatch batch) {
        Color tmpColor = batch.getColor();
        batch.setColor(color);
        batch.draw(texture,
                position.x - width / 2, position.y - height / 2,
                width / 2, height / 2,
                width, height,
                scaleX, scaleY,
                angle
        );
        batch.setColor(tmpColor);
    }

    public abstract void update(float dt);

    @Override
    public void basicUpdate(float dt) {
        angle += rotationSpeed * dt;
    }

    @Override
    public void render(SpriteBatch batch) {
        basicRender(batch);
        if (labeled) {
            label.render(batch);
        }
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }
}