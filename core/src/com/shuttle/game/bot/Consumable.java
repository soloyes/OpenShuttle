package com.shuttle.game.bot;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.utils.Assets;

import lombok.Data;

@Data
public class Consumable implements Poolable {
    private Type type;
    private TextureRegion texture;
    private TextureRegion[] textures;
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;
    private int width, height;
    private float angle, rotationSpeed;
    private float drawScale;

    public Consumable() {
        this.textures = new TextureRegion[2];
        this.textures[0] = Assets.getInstance().getAtlas().findRegion("astronaut");
        this.textures[1] = Assets.getInstance().getAtlas().findRegion("crate");

        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.width = textures[0].getRegionWidth();
        this.height = textures[0].getRegionHeight();
        this.texture = textures[0];
        this.active = false;
    }

    public void deactivate() {
        active = false;
    }

    public void init(float x, float y) {
        position.set(x, y);
        angle = MathUtils.random(0, 360);
        rotationSpeed = MathUtils.random(-20, 20);
        active = true;
        type = Type.values()[MathUtils.random(0, 1)];
        texture = textures[type.textureIndex];
        width = texture.getRegionWidth();
        height = texture.getRegionHeight();
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - width / 2, position.y - height / 2, width / 2, height / 2, width, height, drawScale, drawScale, angle);
    }

    public void update(float dt) {
        drawScale = 1.0f;
        position.mulAdd(velocity, dt);
        angle += rotationSpeed * dt;
    }

    public enum Type {
        Astronaut(0), Crate(1);
        private int textureIndex;

        Type(int textureIndex) {
            this.textureIndex = textureIndex;
        }
    }
}
