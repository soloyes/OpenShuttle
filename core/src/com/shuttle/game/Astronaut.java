package com.shuttle.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Astronaut implements Poolable {
    private GameScreen gs;
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;
    private int width, height;
    private float angle;

    public Astronaut(GameScreen gs) {
        this.gs = gs;
        this.texture = Assets.getInstance().getAtlas().findRegion("astronaut");
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

    public void deactivate() {
        active = false;
    }

    public void init(float x, float y) {
        position.set(x, y);
        angle = MathUtils.random(0, 360);
        active = true;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - width / 2, position.y - height / 2, width / 2, height / 2, width, height, 1, 1, angle);
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
    }

    public Vector2 getPosition() {
        return position;
    }
}
