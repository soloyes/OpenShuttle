package com.shuttle.game.bot.consumable;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.pool.AbstractPoolable;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.BoundCheck;

import lombok.Data;

@Data
public class Consumable extends AbstractPoolable {
    private Type type;
    private TextureRegion texture;
    private TextureRegion[] textures;

    public Consumable() {
        this.textures = new TextureRegion[2];
        this.textures[0] = Assets.getInstance().getAtlas().findRegion(PaintConstants.ASTRONAUT_REGION);
        this.textures[1] = Assets.getInstance().getAtlas().findRegion(PaintConstants.CRATE_REGION);
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.texture = textures[0];
        this.active = false;
        this.boundCheck = new BoundCheck();
        this.level = 0;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
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
        batch.draw(texture, position.x - width / 2, position.y - height / 2, width / 2, height / 2, width, height, scaleX, scaleY, angle);
    }

    public void update(float dt) {
        boundCheck.checkAndHandleBounds(this);
        scaleX = scaleY = 1.0f;
        position.mulAdd(velocity, dt);
        angle += rotationSpeed * dt;
    }

    public enum Type {
        ASTRONAUT(0), CRATE(1);
        private int textureIndex;

        Type(int textureIndex) {
            this.textureIndex = textureIndex;
        }
    }
}