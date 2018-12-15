package com.shuttle.game.background;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 12/8/18.
 */
public class StarSky {
    private TextureRegion starTexture;
    private Star[] stars;

    public StarSky(CloudSky cloudSky) {
        this.stars = new Star[Rules.STARS_COUNT];
        this.starTexture = Assets.getInstance().getAtlas().findRegion(PaintConstants.STAR16_REGION);
        for (int i = 0; i < stars.length / 2; i++) {
            stars[i] = new Star(0.3f, 0.8f);
            stars[i].setCloudSky(cloudSky);
        }
        for (int i = stars.length / 2; i < 2 * stars.length / 3; i++) {
            stars[i] = new Star(0.8f, 1.2f);
            stars[i].setCloudSky(cloudSky);
        }
        for (int i = 2 * stars.length / 3; i < stars.length; i++) {
            stars[i] = new Star(1.2f, 1.8f);
            stars[i].setCloudSky(cloudSky);
        }
    }

    public void setPlayer(Shuttle player) {
        for (int i = 0; i < stars.length / 2; i++) {
            stars[i].setPlayer(player);
        }
        for (int i = stars.length / 2; i < 2 * stars.length / 3; i++) {
            stars[i].setPlayer(player);
        }
        for (int i = 2 * stars.length / 3; i < stars.length; i++) {
            stars[i].setPlayer(player);
        }
    }

    public void render(SpriteBatch batch) {
        Color color = batch.getColor();
        for (int i = 0; i < stars.length; i++) {
            batch.setColor(stars[i].getColor());
            batch.draw(
                    starTexture,
                    stars[i].getPosition().x - stars[i].getWidth() / 2, stars[i].getPosition().y - stars[i].getHeight() / 2,
                    stars[i].getWidth() / 2, stars[i].getHeight() / 2,
                    stars[i].getWidth(), stars[i].getHeight(),
                    stars[i].getScaleX(), stars[i].getScaleY(),
                    0
            );
            if (MathUtils.random(0, 300) < 2) {
                batch.setColor(stars[i].getColor());
                batch.draw(
                        starTexture,
                        stars[i].getPosition().x - stars[i].getWidth() / 2, stars[i].getPosition().y - stars[i].getHeight() / 2,
                        stars[i].getWidth() / 2, stars[i].getHeight() / 2,
                        stars[i].getWidth(), stars[i].getHeight(),
                        stars[i].getScaleX() * PaintConstants.STAR16_BLINK_FACTOR, stars[i].getScaleY() * PaintConstants.STAR16_BLINK_FACTOR,
                        0
                );
            }
        }
        batch.setColor(color);
    }

    public void update(float dt) {
        for (int i = 0; i < stars.length; i++) {
            stars[i].update(dt);
        }
    }
}
