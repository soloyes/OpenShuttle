package com.shuttle.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shuttle.game.Assets;
import com.shuttle.game.buttons.MenuBar;

/**
 * @author Shuttle on 3/24/18.
 */
public class MenuScreen extends BaseScreen {
    private BitmapFont font96;
    private SpriteBatch batch;

    public MenuScreen(SpriteBatch batch) {
        super(batch);
        this.batch = batch;
    }

    @Override
    public void show() {
        super.show();
        Assets.getInstance()
                .getAssetManager()
                .get("music/menu.ogg", Music.class)
                .play();
        Assets.getInstance()
                .getAssetManager()
                .get("music/menu.ogg", Music.class)
                .setLooping(true);
    }

    @Override
    public void createGUI() {
        font96 = Assets.getInstance().getAssetManager().get("fonts/gomarice96.ttf", BitmapFont.class);
        stage.addActor(new MenuBar());
        System.out.println(Gdx.graphics.getWidth() + " " + Gdx.graphics.getHeight());
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