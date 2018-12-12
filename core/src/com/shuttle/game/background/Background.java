package com.shuttle.game.background;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.screens.ScreenManager;

/**
 * @author Shuttle on 12/8/18.
 */
public class Background {
    private StarSky starSky;
    private CloudSky cloudSky;

    public void setPlayer(Shuttle shuttle) {
        cloudSky.setPlayer(shuttle);
    }

    public Background(ScreenManager.ScreenType screenType) {
        cloudSky = new CloudSky(screenType);
        starSky = new StarSky();
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
