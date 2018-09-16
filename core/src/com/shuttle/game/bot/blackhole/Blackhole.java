package com.shuttle.game.bot.blackhole;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.utils.Assets;

import lombok.Data;

/**
 * @author Shuttle on 9/15/18.
 */
@Data
public class Blackhole {
    private GameScreen gs;
    private TextureRegion texture;
    private TextureRegion glowRegion;
    private Vector2 position, start, velocity;
    private int width, height;
    private int glowWidth, glowHeight;
    private float scale;
    private float glowScale;
    private float rotation, angle, radialSpeed;
    private float takeRange;
    private int radius;
    private boolean oscillator;
    private boolean glowOscillator;

    public Blackhole() {
        this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.BLACKHOLE_HOLE_REGION);
        this.glowRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.BLACKHOLE_GLOW_REGION);
        this.start = new Vector2(
                Rules.SPACE_WIDTH / 2,
                Rules.SPACE_HEIGHT / 2
        );
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.width = PaintConstants.BLACKHOLE_HOLE_WIDTH;
        this.height = PaintConstants.BLACKHOLE_HOLE_HEIGHT;
        this.glowWidth = PaintConstants.BLACKHOLE_GLOW_WIDTH;
        this.glowHeight = PaintConstants.BLACKHOLE_GLOW_HEIGHT;
        this.takeRange = Rules.BLACKHOLE_TAKE_RANGE;
        this.scale = 1.0f;
        this.glowScale = 1.0f;
        this.angle = MathUtils.random(365);
        this.rotation = MathUtils.random(365);
        this.radialSpeed = MathUtils.random(30, 50);
        this.radius = MathUtils.random(Rules.SPACE_WIDTH / 4, 3 * Rules.SPACE_WIDTH / 4);
    }

    public void render(SpriteBatch batch) {
        batch.setColor(0.05f, 0.05f, 0.4f, 1.0f);
        batch.draw(
                glowRegion,
                position.x - glowWidth / 2, position.y - glowHeight / 2,
                glowWidth / 2,
                glowHeight / 2,
                glowWidth, glowHeight, glowScale, glowScale,
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
        position.x = start.x + radius * MathUtils.cos(angle);
        position.y = start.y + radius * MathUtils.sin(angle);

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

        if (glowOscillator) {
            glowScale -= dt / 20;
            if (glowScale <= 0.9f) {
                glowScale = 0.9f;
                glowOscillator = false;
            }
        } else {
            glowScale += dt / 20;
            if (glowScale >= 1.0f) {
                glowScale = 1.0f;
                glowOscillator = true;
            }
        }

    }
}
