package com.shuttle.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.screens.ScreenManager;

public class Shuttle {
    private GameScreen gs;
    private TextureRegion texture;
    private int width, height;
    private Vector2 position;
    private Vector2 velocity;
    private int score, scoreView;
    private float angle;
    private float acceleration;
    private float fuel, maxFuel;
    private int hp, maxHp;
    private int lives;
    private Vector2 tmp;
    private StringBuilder tmpString;
    private int takeRange;

    public int getTakeRange() {
        return takeRange;
    }

    public Shuttle(GameScreen gs, float x, float y) {
        this.gs = gs;
        this.texture = Assets.getInstance().getAtlas().findRegion("rocket");
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0.0f, 0.0f);
        this.tmp = new Vector2(0.0f, 0.0f);
        this.angle = 0.0f;
        this.acceleration = 500;
        this.width = texture.getRegionWidth();
        this.height = texture.getRegionHeight();
        this.tmpString = new StringBuilder(200);
        this.maxFuel = 120.0f;
        this.fuel = this.maxFuel;
        this.maxHp = 3;
        this.hp = this.maxHp;
        this.lives = 3;
        this.takeRange = 500;
    }

    public void addScore(int amount) {
        score += amount;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - width / 2, position.y - height / 2, width / 2, height / 2, width, height, 1, 1, angle);
    }

    public void renderGUI(SpriteBatch batch, BitmapFont font) {
        tmpString.setLength(0);
        tmpString.append("SCORE: ").append(scoreView);
        tmpString.append("\nHP: ").append(hp).append("/").append(maxHp).append(" x").append(lives);
        tmpString.append("\nFUEL: ").append((int) fuel).append("/").append((int) maxFuel);

        font.draw(batch, tmpString, 20, 1900);
    }

    public void update(float dt) {
        fuel -= dt;
        if (angle < 0.0f) {
            angle += 360.0f;
        }
        if (angle > 360.0f) {
            angle -= 360.0f;
        }
        if (scoreView < score) {
            int scoreDelta = (int) ((score - scoreView) * 0.1f);
            if (scoreDelta < 5) {
                scoreDelta = 5;
            }
            scoreView += scoreDelta;
            if (scoreView > score) {
                scoreView = score;
            }
        }
        velocity.scl(0.99f);
        position.mulAdd(velocity, dt);

        int dxy = 90;
        gs.getParticleEmitter().setup(
                position.x - 100 * (float) Math.cos(Math.toRadians(angle)),
                position.y - 100 * (float) Math.sin(Math.toRadians(angle)),
                MathUtils.random(-dxy, dxy),
                MathUtils.random(-dxy, dxy),
                0.5f,
                5.0f,
                0.4f,
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
                        position.x - 100 * (float) Math.cos(Math.toRadians(angle)),
                        position.y - 100 * (float) Math.sin(Math.toRadians(angle)),
                        MathUtils.random(-dxy, dxy),
                        MathUtils.random(-dxy, dxy),
                        0.3f,
                        8.0f,
                        3.0f,
                        1,
                        0.4f,
                        0,
                        1,
                        0,
                        0,
                        0,
                        0.2f
                );
            }
        }

        if (velocity.len() > 600) {
            dxy = 20;
            for (int i = -70; i < 70; i += 5) {
                float sa = Math.abs(70.0f - Math.abs(i)) / 70.0f;
                sa *= ((velocity.len() - 600) / 600.0f);
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
            float radialSpeed = 135.0f;
            tmp.set(Gdx.input.getX(), Gdx.input.getY());
            ScreenManager.getInstance().getViewport().unproject(tmp);
            float angleToTarget = tmp.sub(position).angle();
            if (angle > angleToTarget) {
                if (Math.abs(angle - angleToTarget) <= 180.0f) {
                    angle -= radialSpeed * dt;
                } else {
                    angle += radialSpeed * dt;
                }
            }
            if (angle < angleToTarget) {
                if (Math.abs(angle - angleToTarget) <= 180.0f) {
                    angle += radialSpeed * dt;
                } else {
                    angle -= radialSpeed * dt;
                }
            }
            velocity.add(acceleration * (float) Math.cos(Math.toRadians(angle)) * dt, acceleration * (float) Math.sin(Math.toRadians(angle)) * dt);
        }
    }

    public Vector2 getPosition() {
        return position;
    }
}
