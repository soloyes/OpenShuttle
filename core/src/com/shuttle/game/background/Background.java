package com.shuttle.game.background;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import lombok.Getter;

/**
 * @author Shuttle on 12/8/18.
 */
public class Background {
    private StarSky starSky;
    @Getter
    private CloudSky cloudSky;

    public Background() {
        starSky = new StarSky();
        cloudSky = new CloudSky();
    }

    public void render(SpriteBatch batch) {
        starSky.render(batch);
        cloudSky.render(batch);
    }

    public void update(float dt) {
        starSky.update(dt);
        cloudSky.update(dt);
    }
}
