package com.shuttle.game.bot.blackhole;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.utils.Assets;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 9/15/18.
 */

public class Blackhole {
    private TextureRegion texture;
    private TextureRegion spiralRegion;
    private TextureRegion glowRegion;
    @Getter
    @Setter
    private Vector2 position;
    @Getter
    private Vector2 center;
    private int width, height;
    private int spiralWidth, spiralHeight;
    private int glowWidth, glowHeight;
    private float scale;
    private float spiralScale;
    private float glowScale;
    private float glowB;
    private float rotation, angle, radialSpeed;
    @Getter
    @Setter
    private float takeRange;
    @Getter
    private int radius;
    private boolean oscillator;
    private boolean spiralOscillator;
    private boolean glowOscillator;

    public Blackhole() {
        this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.BLACKHOLE_HOLE_REGION);
        this.spiralRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.BLACKHOLE_SPIRAL_REGION);
        this.glowRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.BLACKHOLE_GLOW_REGION);
        this.center = new Vector2(
                Rules.SPACE_WIDTH / 2,
                Rules.SPACE_HEIGHT / 2
        );
        this.position = new Vector2();
        this.width = PaintConstants.BLACKHOLE_HOLE_WIDTH;
        this.height = PaintConstants.BLACKHOLE_HOLE_HEIGHT;
        this.spiralWidth = PaintConstants.BLACKHOLE_SPIRAL_WIDTH;
        this.spiralHeight = PaintConstants.BLACKHOLE_SPIRAL_HEIGHT;
        this.glowWidth = PaintConstants.BLACKHOLE_GLOW_WIDTH;
        this.glowHeight = PaintConstants.BLACKHOLE_GLOW_HEIGHT;
        this.takeRange = Rules.BLACKHOLE_TAKE_RANGE;
        this.scale = 1.0f;
        this.spiralScale = 1.0f;
        this.glowScale = 1.0f;
        this.glowB = 0.0f;
        this.angle = MathUtils.random(365);
        this.rotation = MathUtils.random(365);
        this.radialSpeed = MathUtils.random(30, 50);
        this.radius = MathUtils.random(0, Rules.SPACE_WIDTH / 2);
    }

    public void render(SpriteBatch batch) {
        batch.setColor(0.1f, 0.0f, glowB, 1.0f);
        batch.draw(
                glowRegion,
                position.x - glowWidth / 2, position.y - glowHeight / 2,
                glowWidth / 2,
                glowHeight / 2,
                glowWidth, glowHeight, glowScale, glowScale,
                0.0f
        );
        batch.setColor(0.05f, 0.05f, 0.4f, 1.0f);
        batch.draw(
                spiralRegion,
                position.x - spiralWidth / 2, position.y - spiralHeight / 2,
                spiralWidth / 2,
                spiralHeight / 2,
                spiralWidth, spiralHeight, spiralScale, spiralScale,
                rotation * radialSpeed, true
        );
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        batch.draw(
                texture,
                position.x - width / 2, position.y - height / 2,
                width / 2,
                height / 2,
                width, height, scale, scale,
                rotation * radialSpeed
        );

    }

    public void update(float dt) {
        angle += dt / 50;
        if (angle >= 365) {
            angle = 0;
        }
        rotation += dt;
        if (rotation >= 365) {
            rotation = 0;
        }
        position.x = center.x + radius * MathUtils.cos(angle);
        position.y = center.y + radius * MathUtils.sin(angle);

        if (oscillator) {
            scale -= dt / 10;
            if (scale <= 0.8f) {
                scale = 0.8f;
                oscillator = false;
            }
        } else {
            scale += dt / 10;
            if (scale >= 1.0f) {
                scale = 1.0f;
                oscillator = true;
            }
        }

        if (spiralOscillator) {
            spiralScale -= dt / 20;
            if (spiralScale <= 0.9f) {
                spiralScale = 0.9f;
                spiralOscillator = false;
            }
        } else {
            spiralScale += dt / 20;
            if (spiralScale >= 1.0f) {
                spiralScale = 1.0f;
                spiralOscillator = true;
            }
        }

        if (glowOscillator) {
            glowB -= dt / 5;
            if (glowB <= 0.0f) {
                glowB = 0.0f;
                glowOscillator = false;
            }
        } else {
            glowB += dt / 5;
            if (glowB >= 0.5f) {
                glowB = 0.5f;
                glowOscillator = true;
            }
        }
    }
}
