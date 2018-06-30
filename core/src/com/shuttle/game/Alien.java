package com.shuttle.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Alien implements Poolable {
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;
    private int width, height;

    public Alien() {
        this.texture = Assets.getInstance().getAtlas().findRegion("alien");
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.width = texture.getRegionWidth();
        this.height = texture.getRegionHeight();
        this.active = false;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void init(float x, float y) {
        position.set(x, y);
        active = true;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - width / 2, position.y - height / 2, width / 2, height / 2, width, height, 1, 1, 0);
    }

    public void destroy() {
        active = false;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
    }

    public Vector2 getPosition() {
        return position;
    }
}
