package com.shuttle.game.screens.group_screen;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shuttle.game.Assets;
import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.screens.BaseScreen;

/**
 * @author Shuttle on 3/24/18.
 */
public class MenuScreen extends BaseScreen {
    public MenuScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void show() {
        super.show();
        if (GamePreferences.getOurInstance().isMusicEnabled()) {
            Assets.getInstance()
                    .getAssetManager()
                    .get("music/menu.ogg", Music.class)
                    .play();
            Assets.getInstance()
                    .getAssetManager()
                    .get("music/menu.ogg", Music.class)
                    .setLooping(true);
            Assets.getInstance()
                    .getAssetManager()
                    .get("music/menu.ogg", Music.class)
                    .setVolume(GamePreferences.getOurInstance().getMusicVolume());
        }
    }

    @Override
    public void createGUI() {
        stage.addActor(new MenuGroup());
        stage.addActor(new ConfigGroup());
        stage.addActor(new MenuBar());
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.draw();
    }
}