package com.shuttle.game.background;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.utils.Assets;

/**
 * @author soloyes on 12/8/18.
 */

public class StarSky {
    private TextureRegion starTexture;
    private Star[] stars;

    public StarSky(CloudSky cloudSky) {
        this.starTexture = Assets.getInstance().getAtlas().findRegion(PaintConstants.STAR16_REGION);
        this.stars = new Star[Rules.STARS_COUNT];
        for (int i = 0; i < stars.length / 2; i++) {
            stars[i] = new Star(0.3f, 0.5f);
            stars[i].setCloudSky(cloudSky);
        }
        for (int i = stars.length / 2; i < 2 * stars.length / 3; i++) {
            stars[i] = new Star(0.5f, 1.0f);
            stars[i].setCloudSky(cloudSky);
        }
        for (int i = 2 * stars.length / 3; i < stars.length; i++) {
            stars[i] = new Star(1.0f, 1.2f);
            stars[i].setCloudSky(cloudSky);
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < stars.length; i++) {
            stars[i].render(batch, starTexture);
        }
    }

    public void update(float dt) {
        for (int i = 0; i < stars.length; i++) {
            stars[i].update(dt);
        }
    }
}