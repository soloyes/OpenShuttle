package com.shuttle.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Shuttle on 7/1/18.
 */
public class Fragment implements Poolable {
    private TextureRegion[] textureRegions;
    private Vector2 position;
    private Vector2 velocity;
    private float frame;
    private boolean active;
    private int width, height;
    private float rotation;

    public Fragment() {
        this.textureRegions = Regions.split(
                new TextureRegion(Assets.getInstance().getAssetManager().get("pics/asteroid.png", Texture.class)),
                8,
                8,
                32);
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.rotation = MathUtils.random(365f);
        this.active = false;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void init(float x, float y, Vector2 velocity) {
        this.velocity = velocity;
        this.rotation = MathUtils.random(365f);
        int s = MathUtils.random(16, 64);
        this.width = height = s;
        this.position.set(x, y);
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
        position.mulAdd(velocity, delta);
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
}
