package com.shuttle.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Shuttle on 6/30/18.
 */
public class Asteroid implements Poolable {
    private float velocity;
    private TextureRegion[] textureRegions;
    private Vector2 position;
    private Vector2 target;
    private float frame;
    private boolean active;
    private int width, height;
    private float rotation;

    public Asteroid() {
        this.textureRegions = Regions.split(
                new TextureRegion(Assets.getInstance().getAssetManager().get("pics/asteroid.png", Texture.class)),
                8,
                8,
                32);
        this.position = new Vector2(0, 0);
        this.rotation = MathUtils.random(365f);
        this.velocity = MathUtils.random(100, 300);
        this.width = 128;
        this.height = 128;
        this.active = false;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void init(float x, float y, Vector2 target) {
        this.position.set(x, y);
        this.target = target;
        target.sub(position).nor();
        this.active = true;
    }

    public void render(SpriteBatch batch) {
        frame += 0.3f;
        batch.draw(
                textureRegions[(int) frame],
                position.x - width / 2,
                position.y - height / 2,
                width / 2, height / 2, width, height, 1, 1, rotation
        );
        frame %= textureRegions.length - 1;
    }

    public void update(float delta) {
        position.mulAdd(target, velocity * delta);
        checkAndHandleBounds();
    }

    private void checkAndHandleBounds() {
        if (position.x + width < 0) destroy();
        if (position.x > Rules.SPACE_WIDTH) destroy();
        if (position.y + height < 0) destroy();
        if (position.y > Rules.SPACE_HEIGHT) destroy();
    }

    public void destroy() {
        active = false;
    }

    public int getHeight() {
        return height;
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getWidth() {
        return width;
    }
}
