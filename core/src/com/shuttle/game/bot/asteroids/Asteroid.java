package com.shuttle.game.bot.asteroids;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.pool.AbstractPoolable;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.effects.MoveProcessor;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.BoundCheck;
import com.shuttle.game.utils.Regions;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 6/30/18.
 */
public class Asteroid extends AbstractPoolable {
    private TextureRegion[] textureRegions;
    @Getter
    @Setter
    private boolean fragmented;
    @Getter
    @Setter
    private float sizeForHarvest;
    //Burn with laser
    @Getter
    @Setter
    private float timeToBurn;

    public Asteroid(GameScreen gs) {
        this.gs = gs;
        this.textureRegions = Regions.split(
                new TextureRegion(
                        Assets.getInstance()
                                .getAtlas()
                                .findRegion(PaintConstants.ASTEROID_REGION)),
                8, 8, 32
        );
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.angle = MathUtils.random(365f);
        this.active = true;
        this.boundCheck = new BoundCheck();
        this.type = Type.ASTEROID;
        this.moveProcessor = new MoveProcessor(gs);
        this.color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public void destroy() {
        active = false;
    }

    @Override
    public void basicRender(SpriteBatch batch) {
        Color tmpColor = batch.getColor();
        batch.setColor(color);
        batch.draw(
                textureRegions[(int) frame],
                position.x - width / 2,
                position.y - height / 2,
                width / 2, height / 2,
                width, height,
                scaleX, scaleY,
                angle
        );
        batch.setColor(tmpColor);
    }

    @Override
    public void basicUpdate(float dt) {
        frame += 24 * dt;
        frame %= textureRegions.length - 1;
    }

    @Override
    public void render(SpriteBatch batch) {
        basicRender(batch);
    }

    @Override
    public void update(float dt) {
        basicUpdate(dt);
        moveProcessor.update(this, dt);
        boundCheck.checkAndHandleBoundsAndDestroy(this);
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void init(Vector2 position, Vector2 destanation, float scale, boolean isFragment) {
        init(position.x, position.y);
        this.fragmented = isFragment;
        destanation.sub(position).nor();
        if (isFragment) {
            velocity.set(destanation).scl(MathUtils.random(25, 150));
        } else {
            velocity.set(destanation).scl(MathUtils.random(50, 300));
        }
        this.scaleX = this.saveScaleX = scale;
        this.scaleY = this.saveScaleY = scale;
        this.color.set(1.0f, 1.0f, 1.0f, 1.0f);
        this.spiralRadius = MathUtils.random(20, 60);
    }

    @Override
    public void init(float x, float y) {
        this.position.set(x, y);
        this.width = PaintConstants.ASTEROID_WIDTH;
        this.height = PaintConstants.ASTEROID_HEIGHT;
        this.active = true;
        this.taken = false;
    }
}
