package com.shuttle.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.utils.Assets;

public class Background {
    private TextureRegion starTexture;
    private Star[] stars;

    public Background() {
        this.starTexture = Assets.getInstance().getAtlas().findRegion(PaintConstants.STAR16_REGION);
        this.stars = new Star[Rules.STARS_COUNT];
        for (int i = 0; i < Rules.STARS_COUNT; i++) {
            this.stars[i] = new Star();
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < stars.length; i++) {
            batch.draw(
                    starTexture,
                    stars[i].position.x - stars[i].width / 2, stars[i].position.y - stars[i].height / 2,
                    stars[i].width / 2, stars[i].height / 2,
                    stars[i].width, stars[i].height,
                    stars[i].scale, stars[i].scale,
                    0
            );
            if (MathUtils.random(0, 300) < 2) {
                batch.draw(
                        starTexture,
                        stars[i].position.x - stars[i].width / 2, stars[i].position.y - stars[i].height / 2,
                        stars[i].width / 2, stars[i].height / 2,
                        stars[i].width, stars[i].height,
                        stars[i].scale * PaintConstants.STAR16_BLINK_FACTOR, stars[i].scale * PaintConstants.STAR16_BLINK_FACTOR,
                        0
                );
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
        private float scale;
        private float width;
        private float height;

        public Star() {
            this.position = new Vector2(MathUtils.random(0, Rules.SPACE_WIDTH), MathUtils.random(0, Rules.SPACE_HEIGHT));
            this.speed = MathUtils.random(5.0f, 60.0f);
            this.scale = MathUtils.random(0.5f, 1.5f);
            this.speed = MathUtils.random(5.0f, 60.0f);
            this.width = PaintConstants.STAR16_WIDTH;
            this.height = PaintConstants.STAR16_HEIGHT;
        }

        public void update(float dt) {
            position.x -= speed * dt;
            if (position.x < -width) {
                position.x = Rules.SPACE_WIDTH;
            }
        }
    }
}
