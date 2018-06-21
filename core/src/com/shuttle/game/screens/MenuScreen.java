package com.shuttle.game.screens;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shuttle.game.Assets;
import com.shuttle.game.buttons.MenuBar;
import com.shuttle.game.conf.GamePreferences;

/**
 * @author Shuttle on 3/24/18.
 */
public class MenuScreen extends BaseScreen {
    private BitmapFont font96;
    private SpriteBatch batch;
    private GamePreferences preferences;

    public MenuScreen(SpriteBatch batch) {
        super(batch);
        this.batch = batch;
        //:todo Maybe better to to Util class Preferences instead current realization.
        preferences = new GamePreferences();
    }

    @Override
    public void show() {
        super.show();
        if (preferences.isMusicEnabled()) {
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
                    .setVolume(preferences.getMusicVolume());
        }
    }

    @Override
    public void createGUI() {
        font96 = Assets.getInstance().getAssetManager().get("fonts/gomarice96.ttf", BitmapFont.class);
        stage.addActor(new MenuBar());
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        font96.draw(batch, "Shuttle", 0, 1800, 1080, 1, false);
        batch.end();
        stage.act(delta);
        stage.draw();
    }
}