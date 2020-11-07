package com.shuttle.game.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @author soloyes on 6/28/18.
 */

public class TestScreen extends BaseScreen {
    public TestScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void createGUI() {
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        update(dt);
        batch.begin();
        batch.end();
    }

    private void update(float dt) {
    }
}