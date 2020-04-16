package com.shuttle.game.bot.anomaly;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.rocket.RocketLauncher;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.BoundCheck;
import com.shuttle.game.utils.pool.AbstractPoolable;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 9/15/18.
 */

public abstract class AbstractAnomaly extends AbstractPoolable {
    @Getter
    @Setter
    protected boolean hungry;
    @Getter
    @Setter
    protected float hungryTimer;
    protected Vector2 tmp;
    @Getter
    @Setter
    protected TextureRegion[] textureRegions;
    @Getter
    @Setter
    protected float v;
    @Getter
    protected TextureRegion atRocketRegion;

    @Override
    public Vector2 getPosition() {
        return position;
    }

    public abstract void render(SpriteBatch batch);

    public abstract void update(float dt);

    protected AbstractAnomaly() {
        this.boomBox = new BoomBox();
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.tmp = new Vector2();
        this.color = new Color();
        this.spreeColor = new Color();
        this.boundCheck = new BoundCheck();
        this.atRocketRegion = new TextureRegion(
                Assets.getInstance()
                        .getAtlas()
                        .findRegion(PaintConstants.AT_ROCKET_POINT_REGION));
    }

    protected void drawAtRocketPoint(SpriteBatch batch) {
        if (atRocketPoint && RocketLauncher.getInstance().isLaunched()) {
            Color color = batch.getColor();
            batch.setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, PaintConstants.AT_ROCKET_POINT_ALPHA);
            batch.draw(atRocketRegion,
                    position.x - width / 2, position.y - height / 2,
                    width / 2, height / 2,
                    width, height,
                    scaleX, scaleY,
                    clockwise * angle);
            batch.setColor(color);
        }
    }
}