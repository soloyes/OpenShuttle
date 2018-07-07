package com.shuttle.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Background {
    private TextureRegion starTexture;
    private Star[] stars;

    public Background() {
        starTexture = Assets.getInstance().getAtlas().findRegion("star16");
        stars = new Star[Rules.STARS];
        for (int i = 0; i < Rules.STARS; i++) {
            stars[i] = new Star();
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < stars.length; i++) {
            batch.draw(starTexture, stars[i].position.x - 8, stars[i].position.y - 8, 16, 16, 16, 16, stars[i].size, stars[i].size, 0);
            if (MathUtils.random(0, 300) < 2) {
                batch.draw(starTexture, stars[i].position.x - 8, stars[i].position.y - 8, 16, 16, 16, 16, stars[i].size * 1.5f, stars[i].size * 1.5f, 0);
            }
        }
    }

    public void update(float dt) {
        for (int i = 0; i < stars.length; i++) {
            stars[i].update(dt);
        }
    }

    private class Star {
        private Vector2 position;
        private float speed;
        private float size;

        public Star() {
            position = new Vector2(MathUtils.random(0, Rules.SPACE_WIDTH), MathUtils.random(0, Rules.SPACE_HEIGHT));
            speed = MathUtils.random(5.0f, 60.0f);
            size = MathUtils.random(0.5f, 1.5f);
        }

        public void update(float dt) {
            position.x -= speed * dt;
            if (position.x < -20) {
                position.x = Rules.SPACE_WIDTH;
                position.y = MathUtils.random(0, Rules.SPACE_HEIGHT);
                speed = MathUtils.random(5.0f, 60.0f);
                size = MathUtils.random(0.5f, 1.5f);
            }
        }
    }
}
