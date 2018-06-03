package com.shuttle.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Shuttle {
    private GameScreen gs;
    private TextureRegion texture;
    private int width, height;
    private Vector2 position;
    private Vector2 velocity;
    private int score, scoreView;

    private float angle;
    private float acceleration;

    private Vector2 tmp;
    private StringBuilder tmpString;

    public Vector2 getPosition() {
        return position;
    }

    public void addScore(int amount) {
        score += amount;
    }

    public Shuttle(GameScreen gs, float x, float y, float acceleration) {
        this.gs = gs;
        this.texture = Assets.getInstance().getAtlas().findRegion("rocket");
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0.0f, 0.0f);
        this.tmp = new Vector2(0.0f, 0.0f);
        this.angle = 0.0f;
        this.acceleration = acceleration;
        this.width = texture.getRegionWidth();
        this.height = texture.getRegionHeight();
        this.tmpString = new StringBuilder(200);
    }


    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - width / 2, position.y - height / 2, width / 2, height / 2, width, height, 1, 1, angle);
    }

    public void renderGUI(SpriteBatch batch, BitmapFont font) {
        tmpString.setLength(0);
        tmpString.append("SCORE: ").append(scoreView);
        font.draw(batch, tmpString, 20, 1900);
    }

    public void update(float dt) {
        if (angle < 0.0f) {
            angle += 360.0f;
        }
        if (angle > 360.0f) {
            angle -= 360.0f;
        }
        if (scoreView < score) {
            scoreView += 20;
            if (scoreView > score) {
                scoreView = score;
            }
        }
        velocity.scl(0.99f);
        position.mulAdd(velocity, dt);

        int dxy = 20;
        gs.getParticleEmitter().setup(
                position.x - 65 * (float) Math.cos(Math.toRadians(angle)),
                position.y - 65 * (float) Math.sin(Math.toRadians(angle)),
                MathUtils.random(-dxy, dxy),
                MathUtils.random(-dxy, dxy),
                0.3f,
                5.0f,
                2.4f,
                1,
                0.6f,
                0,
                1,
                1,
                0,
                0,
                0.5f
        );
        if (MathUtils.random(0, 100) < 6) {
            dxy = 180;
            for (int i = 0; i < 3; i++) {
                gs.getParticleEmitter().setup(
                        position.x - 65 * (float) Math.cos(Math.toRadians(angle)),
                        position.y - 65 * (float) Math.sin(Math.toRadians(angle)),
                        MathUtils.random(-dxy, dxy),
                        MathUtils.random(-dxy, dxy),
                        0.2f,
                        8.0f,
                        6.0f,
                        1,
                        0f,
                        0,
                        1,
                        0,
                        0,
                        0,
                        1f
                );
            }
        }

        if(velocity.len() > 600) {
            dxy = 20;
            for (int i = -70; i < 70; i += 5) {
                float sa = Math.abs(70.0f - Math.abs(i)) / 70.0f;
                sa *= ((velocity.len()-600) / 600.0f);
                gs.getParticleEmitter().setup(
                        position.x + 90 * (float) Math.cos(Math.toRadians(angle + i)),
                        position.y + 90 * (float) Math.sin(Math.toRadians(angle + i)),
                        MathUtils.random(-dxy, dxy),
                        MathUtils.random(-dxy, dxy),
                        0.2f,
                        3.0f,
                        2.0f,
                        1,
                        1,
                        1,
                        sa,
                        0,
                        0,
                        1,
                        0.0f
                );
            }
        }

        if (Gdx.input.isTouched()) {
            tmp.set(Gdx.input.getX(), Gdx.input.getY());
            ScreenManager.getInstance().getViewport().unproject(tmp);
            float angleToTarget = tmp.sub(position).angle();
            if (angle > angleToTarget) {
                if (Math.abs(angle - angleToTarget) <= 180.0f) {
                    angle -= 90.0f * dt;
                } else {
                    angle += 90.0f * dt;
                }
            }
            if (angle < angleToTarget) {
                if (Math.abs(angle - angleToTarget) <= 180.0f) {
                    angle += 90.0f * dt;
                } else {
                    angle -= 90.0f * dt;
                }
            }
            velocity.add(acceleration * (float) Math.cos(Math.toRadians(angle)) * dt, acceleration * (float) Math.sin(Math.toRadians(angle)) * dt);
        }
    }
}
