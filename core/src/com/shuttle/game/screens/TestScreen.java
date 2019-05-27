package com.shuttle.game.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shuttle.game.bot.rocket.RocketEmitter;

/**
 * @author Shuttle on 6/28/18.
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
        RocketEmitter.getInstance().render(batch);
        batch.end();
    }

    private void update(float dt) {
        RocketEmitter.getInstance().update(dt);
    }
}
