package com.shuttle.game.bot;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.Regions;
import com.shuttle.game.conf.Rules;

import lombok.Data;

/**
 * @author Shuttle on 6/30/18.
 */
@Data
public class Asteroid implements Poolable {
    private TextureRegion[] textureRegions;
    private Vector2 position;
    private Vector2 velocity;
    private float frame;
    private boolean active;
    private int width, height;
    private float rotation;
    private boolean fragment;
    private float sizeForHarvest;

    public Asteroid() {
        this.textureRegions = Regions.split(
                new TextureRegion(Assets.getInstance().getAssetManager().get("pics/asteroid.png", Texture.class)),
                8,
                8,
                32);
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.rotation = MathUtils.random(365f);
        this.active = false;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    public void init(Vector2 position, Vector2 destanation, float acceleration, float size, boolean isFragment) {
        this.position.set(position);
        this.fragment = isFragment;
        destanation.sub(position).nor();
        velocity.set(destanation).scl(acceleration * MathUtils.random(50, 300));
        float s;
        if (!isFragment) {
            s = textureRegions[0].getRegionWidth();
        } else {
            s = MathUtils.random(
                    textureRegions[0].getRegionWidth(),
                    textureRegions[0].getRegionWidth()
            );
        }
        this.width = this.height = (int) (s * size);
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
        if (position.x + width / 2 < 0) destroy();
        if (position.x - width / 2 > Rules.SPACE_WIDTH) destroy();
        if (position.y + height / 2 < 0) destroy();
        if (position.y - height / 2 > Rules.SPACE_HEIGHT) destroy();
    }

    public void destroy() {
        active = false;
    }
}
