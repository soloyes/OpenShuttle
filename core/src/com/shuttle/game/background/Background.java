package com.shuttle.game.background;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @author soloyes on 12/8/18.
 */

public class Background {
    private StarSky starSky;
    private CloudSky cloudSky;

    public Background() {
        cloudSky = new CloudSky();
        starSky = new StarSky(cloudSky);
    }

    public void render(SpriteBatch batch) {
        cloudSky.render(batch);
        starSky.render(batch);
    }

    public void update(float dt) {
        cloudSky.update(dt);
        starSky.update(dt);
    }
}