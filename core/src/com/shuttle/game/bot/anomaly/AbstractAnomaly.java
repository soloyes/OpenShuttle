package com.shuttle.game.bot.anomaly;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.pool.AbstractPoolable;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.BoomBox;

import lombok.Data;

/**
 * @author Shuttle on 9/15/18.
 */
@Data
public abstract class AbstractAnomaly extends AbstractPoolable {
    protected boolean born;
    protected boolean hungry;
    protected float hungryTimer;
    protected Vector2 tmp;
    protected GameScreen gs;
    protected TextureRegion[] textureRegions;
    protected Color color;

    @Override
    public Vector2 getPosition() {
        return position;
    }

    abstract void render(SpriteBatch batch);

    abstract void update(float dt);
}
