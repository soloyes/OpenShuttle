package com.shuttle.game.bot;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.BoundCheck;
import com.shuttle.game.utils.Regions;

import lombok.Data;

/**
 * @author Shuttle on 6/30/18.
 */
@Data
public class Asteroid extends AbstractPoolable {
    private TextureRegion[] textureRegions;
    private Vector2 position;
    private Vector2 velocity;
    private float frame;
    private boolean active;
    private float rotation;
    private boolean fragmented;
    private float sizeForHarvest;
    private BoundCheck boundCheck;

    public Asteroid() {
        this.textureRegions = Regions.split(
                new TextureRegion(Assets.getInstance().getAssetManager().get(PaintConstants.ASTEROID_REGION, Texture.class)),
                8,
                8,
                32);
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.rotation = MathUtils.random(365f);
        this.active = false;
        this.boundCheck = new BoundCheck();
    }

    @Override
    public void destroy() {
        active = false;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void init(Vector2 position, Vector2 destanation, float size, boolean isFragment) {
        this.position.set(position);
        this.fragmented = isFragment;
        destanation.sub(position).nor();
        if (isFragment) {
            velocity.set(destanation).scl(MathUtils.random(25, 150));
        } else {
            velocity.set(destanation).scl(MathUtils.random(50, 300));
        }

        this.width = (int) (PaintConstants.ASTEROID_WIDTH * size);
        this.height = (int) (PaintConstants.ASTEROID_WIDTH * size);
        this.active = true;
    }

    public void render(SpriteBatch batch) {
        batch.draw(
                textureRegions[(int) frame],
                position.x - width / 2,
                position.y - height / 2,
                width / 2, height / 2, width, height, 1, 1, rotation
        );
    }

    public void update(float dt) {
        frame += 0.3f;
        frame %= textureRegions.length - 1;
        position.mulAdd(velocity, dt);
        checkAndHandleBounds();
    }

    private void checkAndHandleBounds() {
        boundCheck.checkAndHandleBounds(this);
    }
}
