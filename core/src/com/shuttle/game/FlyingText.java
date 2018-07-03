package com.shuttle.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class FlyingText {
    private Matrix4 matrix4;
    private Matrix4 matrix1;
    private Vector2 position;
    private BitmapFont font;
    private float scale;
    private float translation;
    private boolean active;
    private String text;

    public boolean isActive() {
        return active;
    }

    public FlyingText() {
        this.position = new Vector2(0, 0);
        this.font = Assets.getInstance().getAssetManager().get("fonts/gomarice64.ttf");
        this.scale = 1.0f;
        this.matrix4 = new Matrix4();
        this.matrix1 = new Matrix4();
        this.active = false;
        this.text = "";
    }

    public void setup(float x, float y, String text, float scale) {
        this.active = true;
        this.position.set(x, y);
        this.scale = scale;
        this.translation = 0.0f;
        this.text = text;
    }

    public void render(SpriteBatch batch) {
        if (active) {
            matrix4.set(matrix1);
            matrix4.scl(scale);
            batch.setTransformMatrix(matrix4);
            float r = 0.0f;
            if (translation > 0.0f) {
                r = 3;
            }
            if (translation > 0.0f) {
                font.setColor(1, 1, 1, translation);
            }
            font.draw(batch, text, (position.x - (text.length() / 2) * 36) / scale + MathUtils.random(-translation * r, translation * r), (position.y + 32) / scale + MathUtils.random(-translation * r, translation * r));
            font.setColor(1, 1, 1, 1);
            batch.setTransformMatrix(matrix1);
        }
    }

    public void update(float dt) {
        if (active) {
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
}
