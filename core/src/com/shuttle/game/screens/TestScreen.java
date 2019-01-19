package com.shuttle.game.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shuttle.game.bot.bomb.BombEmitter;

/**
 * @author Shuttle on 6/28/18.
 */
public class TestScreen extends BaseScreen {
    private BombEmitter bombEmitter;

    public TestScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void show() {
        super.show();
        this.bombEmitter = new BombEmitter();
    }

    @Override
    public void createGUI() {
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        update(dt);
        batch.begin();
        bombEmitter.render(batch);
        batch.end();
    }

    private void update(float dt) {
        bombEmitter.update(dt);
    }

    @Override
    public void dispose() {
        super.dispose();
        bombEmitter.dispose();
    }
}
