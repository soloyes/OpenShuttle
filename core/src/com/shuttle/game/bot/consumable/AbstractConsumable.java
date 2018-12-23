package com.shuttle.game.bot.consumable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.pool.AbstractPoolable;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.BoundCheck;
import com.shuttle.game.utils.effects.BoomBox;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractConsumable extends AbstractPoolable implements Consumable {
    protected TextureRegion texture;
    @Getter
    @Setter
    protected boolean shielded;
    protected Shield shield;
    protected Scan scan;
    @Setter
    protected boolean labeled;
    @Getter
    protected Label[] labels;
    @Getter
    @Setter
    protected boolean scanned;

    public AbstractConsumable(GameScreen gs) {
        this.gs = gs;
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.active = false;
        this.boundCheck = new BoundCheck();
        this.boomBox = new BoomBox();
        this.color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        this.spreeColor = Color.valueOf("00ff00ff");
        this.level = -1;
        this.labels = new Label[3];
        //Back
        this.labels[0] = new Label();
        //Front, exactly Sticker is set in init() method
        this.labels[1] = new Label();
        //Hidden if labels[1].getSticker() = Label.Sticker.PATTERN
        this.labels[2] = new Label();
        this.labels[0].setColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, PaintConstants.LABEL_ALPHA);
        this.labels[1].setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, PaintConstants.LABEL_ALPHA);
        this.labels[2].setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, PaintConstants.LABEL_ALPHA);
        for (int i = 0; i < labels.length; i++) {
            this.labels[i].setConsumable(this);
        }
    }

    public abstract void consumed(GameScreen gs);

    public abstract void init(float x, float y);

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
            labels[0].render(batch);
            if (!scanned) {
                labels[1].render(batch);
            } else {
                labels[2].render(batch);
            }
        }
        if (shielded && !taken) {
            shield.render(batch);
        }
        if (scanned && !taken) {
            scan.render(batch);
        }
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }
}