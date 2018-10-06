package com.shuttle.game.bot.anomaly;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.pool.AbstractPoolable;
import com.shuttle.game.screens.GameScreen;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 9/15/18.
 */
public abstract class AbstractAnomaly extends AbstractPoolable {
    @Getter
    @Setter
    protected boolean born;
    @Getter
    @Setter
    protected boolean hungry;
    @Getter
    @Setter
    protected float hungryTimer;
    protected Vector2 tmp;
    protected GameScreen gs;
    @Getter
    @Setter
    protected TextureRegion[] textureRegions;
    @Getter
    @Setter
    protected Color color;

    @Override
    public Vector2 getPosition() {
        return position;
    }

    abstract void render(SpriteBatch batch);

    abstract void update(float dt);
}
