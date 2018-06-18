package com.shuttle.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.screens.GameScreen;

public class Consumable implements Poolable {
    public enum Type {
        Astronaut(0), Crate(1);

        private int textureIndex;

        Type(int textureIndex) {
            this.textureIndex = textureIndex;
        }
    }

    private GameScreen gs;
    private Type type;
    private TextureRegion texture;
    private TextureRegion[] textures;
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;
    private int width, height;
    private float angle;

    public Consumable(GameScreen gs) {
        this.gs = gs;
        this.textures = new TextureRegion[2];
        this.textures[0] = Assets.getInstance().getAtlas().findRegion("astronaut");
        this.textures[1] = Assets.getInstance().getAtlas().findRegion("crate");
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.width = textures[0].getRegionWidth();
        this.height = textures[0].getRegionHeight();
        this.texture = textures[0];
        this.type = Type.Astronaut;
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
        type = Type.values()[MathUtils.random(0, 1)];
        texture = textures[type.textureIndex];
        width = texture.getRegionWidth();
        height = texture.getRegionHeight();
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
