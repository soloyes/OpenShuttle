package com.shuttle.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.Assets;
import com.shuttle.game.ParticleEmitter;
import com.shuttle.game.Regions;

/**
 * @author Shuttle on 6/28/18.
 */
public class TestScreen extends BaseScreen {
    private Vector2 tmp;
    private Asteroid asteroid;
    private Asteroid[] asteroids;
    private int random;
    private ParticleEmitter particleEmitter;

    public TestScreen(SpriteBatch batch) {
        super(batch);
        tmp = new Vector2(0, 0);
        asteroids = new Asteroid[15];
    }

    @Override
    public void createGUI() {
        particleEmitter = new ParticleEmitter();

        asteroid = new Asteroid();
        for (int i = 0; i < asteroids.length; i++) {
            asteroids[i] = new Asteroid();
        }

        asteroid.position.set(
                ScreenManager.getInstance().getViewport().getWorldWidth() / 2 - asteroid.textureRegions[0].getRegionWidth() / 2,
                ScreenManager.getInstance().getViewport().getWorldHeight() / 2 - asteroid.textureRegions[0].getRegionHeight() / 2
        );
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        batch.begin();
        if (asteroid.active) {
            asteroid.render(delta);
        } else {
            for (int i = 0; i < random; i++) {
                asteroids[i].render(delta);
            }
        }
        particleEmitter.render(batch);
        batch.end();
    }

    private void update(float delta) {
        particleEmitter.update(delta);
        if (asteroid.active) {
            asteroid.update(delta);
        } else {
            for (int i = 0; i < random; i++) {
                asteroids[i].update(delta);
            }
        }
        checkTouch();
    }

    private void checkTouch() {
        if (Gdx.input.isTouched()) {
            tmp.set(Gdx.input.getX(), Gdx.input.getY());
            ScreenManager.getInstance().getViewport().unproject(tmp);

            if (asteroid.rectangle.contains(tmp) && asteroid.active) {
                asteroid.active = false;
                random = MathUtils.random(3, asteroids.length - 1);
                for (int i = 0; i < random; i++) {
                    asteroids[i].velocity.set(
                            MathUtils.random(-70, 70),
                            MathUtils.random(-70, 70)
                    );
                    asteroids[i].position.set(
                            asteroid.position.x + asteroid.width / 2,
                            asteroid.position.y + asteroid.height / 2
                    );
                    int s = MathUtils.random(16, 64);
                    asteroids[i].setSize(s, s);
                    asteroids[i].i = MathUtils.random(0, 31);
                }
                float dxy = 180;
                for (int i = 0; i < 32; i++) {
                    particleEmitter.setup(
                            asteroid.position.x + asteroid.width / 2,
                            asteroid.position.y + asteroid.height / 2,
                            MathUtils.random(-dxy, dxy),
                            MathUtils.random(-dxy, dxy),
                            1f,
                            6.0f,
                            1.0f,
                            0.17f,
                            0.17f,
                            0.17f,
                            1,
                            0,
                            0,
                            0,
                            0
                    );
                }
            }
        }
    }

    private class Asteroid {
        private TextureRegion[] textureRegions;
        private float i;
        private Vector2 position;
        private Rectangle rectangle;
        private boolean active = true;
        private int width;
        private int height;
        private Vector2 velocity;
        private float rotation;

        public Asteroid() {
            textureRegions = Regions.split(
                    new TextureRegion(Assets.getInstance().getAssetManager().get("pics/asteroid.png", Texture.class)),
                    8,
                    8,
                    32);
            position = new Vector2(0, 0);
            rectangle = new Rectangle(position.x, position.y, 0, 0);
            width = textureRegions[0].getRegionWidth();
            height = textureRegions[0].getRegionHeight();
            velocity = new Vector2(0, 0);
            rotation = MathUtils.random(365f);
        }

        public void render(float delta) {
            if (active) {
                batch.draw(
                        textureRegions[(int) (i += 0.3f)],
                        position.x,
                        position.y,
                        width / 2, height / 2, width, height, 1, 1, rotation
                );
                i %= textureRegions.length - 1;
            }
        }

        public void setSize(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public void update(float delta) {
            this.position.mulAdd(velocity, delta);
            rectangle.setPosition(position);
            rectangle.setSize(textureRegions[0].getRegionWidth(), textureRegions[0].getRegionHeight());
        }
    }
}
