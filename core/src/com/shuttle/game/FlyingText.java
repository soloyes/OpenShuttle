package com.shuttle.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.utils.Assets;

public class FlyingText {
    private Matrix4 matrix4;
    private Matrix4 matrix1;
    private Vector2 position;
    private BitmapFont font64colored;
    private BitmapFont font64;
    private BitmapFont font;
    private String text;
    private float random;
    private float scale;
    private float translation;
    private boolean active;
    private boolean colored;

    public FlyingText() {
        this.position = new Vector2(0, 0);
        this.font64colored = Assets.getInstance().getAssetManager().get("fonts/gomarice64colored.ttf");
        this.font64colored.getData().markupEnabled = true;
        this.font64 = Assets.getInstance().getAssetManager().get("fonts/gomarice64.ttf");
        this.scale = 1.0f;
        this.matrix4 = new Matrix4();
        this.matrix1 = new Matrix4();
        this.active = false;
        this.text = "";
    }

    public void render(SpriteBatch batch) {
        if (active) {
            if (colored){
                font = font64colored;
            } else {
                font = font64;
            }
            matrix4.set(matrix1);
            matrix4.scl(scale);
            batch.setTransformMatrix(matrix4);

            if (translation > 0.0f) {
                font.setColor(1, 1, 1, translation);
            }
            font.draw(batch,
                    text,
                    (position.x - (text.length() / 2) * 36) / scale + random,
                    (position.y + 32) / scale + random
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
