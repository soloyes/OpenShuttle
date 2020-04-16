package com.shuttle.game.effects;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 03/07/18.
 */

public class FlyingText {
    private Matrix4 matrix4;
    private Matrix4 matrix1;
    private Vector2 position;
    private BitmapFont font32Coloured;
    private BitmapFont font32;
    private BitmapFont font;
    private String text;
    private float random;
    private float scale;
    private float translation;
    private boolean active;
    private boolean colored;
    private GlyphLayout glyphLayout;

    public FlyingText() {
        this.position = new Vector2(0, 0);
        this.font32Coloured = Assets.getInstance().getAssetManager().get(PaintConstants.FONT32_C);
        this.font32Coloured.getData().markupEnabled = true;
        this.font32 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT32);
        this.scale = 1.0f;
        this.matrix4 = new Matrix4();
        this.matrix1 = new Matrix4();
        this.active = false;
        this.text = "";
        this.glyphLayout = new GlyphLayout();
    }

    public void render(SpriteBatch batch) {
        if (active) {
            if (colored) {
                font = font32Coloured;
            } else {
                font = font32;
            }
            matrix4.set(matrix1);
            matrix4.scl(scale);
            batch.setTransformMatrix(matrix4);

            if (translation > 0.0f) {
                font.setColor(1, 1, 1, translation);
            }
            glyphLayout.setText(font, text);
            font.draw(batch,
                    glyphLayout,
                    (position.x - glyphLayout.width) / scale + random,
                    (position.y + glyphLayout.height) / scale + random
            );

            font.setColor(1, 1, 1, 1);
            batch.setTransformMatrix(matrix1);
        }
    }

    public void setup(float x, float y, String text, float scale, boolean colored) {
        this.active = true;
        this.position.set(x, y);
        this.scale = scale;
        this.translation = 0.0f;
        this.text = text;
        this.colored = colored;
    }

    public void update(float dt) {
        if (active) {

            float r = 0.0f;
            if (translation > 0.0f) {
                r = 3;
            }
            random = MathUtils.random(-translation * r, translation * r);

            scale -= dt * 8.0f;
            if (scale < 2.0f) {
                if (translation < 0.01f) {
                    translation = 1.0f;
                }
                scale = 2.0f;
            }
            if (translation > 0.0f) {
                translation -= dt * 1.0f;
                if (translation <= 0.01f) {
                    active = false;
                }
            }
        }
    }

    public boolean isActive() {
        return active;
    }
}