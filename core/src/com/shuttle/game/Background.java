package com.shuttle.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.utils.Assets;

public class Background {
    private TextureRegion starTexture;
    private Star[] stars;

    public Background() {
        this.starTexture = Assets.getInstance().getAtlas().findRegion("star16");
        this.stars = new Star[Rules.STARS_COUNT];
        for (int i = 0; i < Rules.STARS_COUNT; i++) {
            this.stars[i] = new Star();
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < stars.length; i++) {
            batch.draw(starTexture, stars[i].position.x - 8, stars[i].position.y - 8, 8, 8, 16, 16, stars[i].size, stars[i].size, 0);
            if (MathUtils.random(0, 300) < 2) {
                batch.draw(starTexture, stars[i].position.x - 8, stars[i].position.y - 8, 8, 8, 16, 16, stars[i].size * 1.5f, stars[i].size * 1.5f, 0);
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
            this.position = new Vector2(MathUtils.random(0, Rules.SPACE_WIDTH), MathUtils.random(0, Rules.SPACE_HEIGHT));
            this.speed = MathUtils.random(5.0f, 60.0f);
            this.size = MathUtils.random(0.5f, 1.5f);
        }

        public void update(float dt) {
            position.x -= speed * dt;
            if (position.x < -20) {
                size = MathUtils.random(0.5f, 1.5f);
                speed = MathUtils.random(5.0f, 60.0f);
                position.y = MathUtils.random(0, Rules.SPACE_HEIGHT);
            }
        }
    }
}
