package com.shuttle.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shuttle.game.utils.Assets;

/**
 * Created by FlameXander on 12.03.2018.
 */

public class LoadingScreen extends BaseScreen implements Screen {
    private SpriteBatch batch;
    private Texture texture;
    private Pixmap pixmap;

    public LoadingScreen(SpriteBatch batch) {
        super(batch);
        this.batch = batch;
        pixmap = new Pixmap(1080, 60, Pixmap.Format.RGB888);
        pixmap.setColor(0.8f, 0.43f, 0.33f, 1f);
        pixmap.fill();
        this.texture = new Texture(pixmap);
    }

    @Override
    public void show() {
    }

    @Override
    public void createGUI() {

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (Assets.getInstance().getAssetManager().update()) {
            Assets.getInstance().makeLinks();
            ScreenManager.getInstance().goToTarget();
        }
        batch.begin();
        batch.draw(texture, 0, 0, 1080 * Assets.getInstance().getAssetManager().getProgress(), 60);
        batch.end();
    }

    @Override
    public void dispose() {
        texture.dispose();
        pixmap.dispose();
    }
}