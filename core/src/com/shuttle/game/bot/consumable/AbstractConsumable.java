package com.shuttle.game.bot.consumable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.pool.AbstractPoolable;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.BoundCheck;
import com.shuttle.game.utils.effects.BoomBox;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    protected Map<Label.Layer, List<Label>> labels;
    @Getter
    @Setter
    protected boolean scanned;
    @Getter
    protected boolean danger;
    @Getter
    protected boolean golden;

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
        this.labels = new HashMap<Label.Layer, List<Label>>();
        this.labels.put(Label.Layer.BACK, new LinkedList<Label>());
        this.labels.put(Label.Layer.LABEL, new LinkedList<Label>());
        this.labels.put(Label.Layer.HIDDEN, new LinkedList<Label>());
        if (labels.get(Label.Layer.BACK).size() < 1) {
            this.labels.get(Label.Layer.BACK).add(new Label());
        }
        if (labels.get(Label.Layer.LABEL).size() < 1) {
            this.labels.get(Label.Layer.LABEL).add(new Label());
        }
        if (labels.get(Label.Layer.HIDDEN).size() < 1) {
            this.labels.get(Label.Layer.HIDDEN).add(new Label());
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
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }
}