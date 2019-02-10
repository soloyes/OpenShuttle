package com.shuttle.game.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shuttle.game.bot.rocket.RocketEmitter;

/**
 * @author Shuttle on 6/28/18.
 */
public class TestScreen extends BaseScreen {
    private RocketEmitter rocketEmitter;

    public TestScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void show() {
        super.show();
        this.rocketEmitter = new RocketEmitter(null);
    }

    @Override
    public void createGUI() {
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        update(dt);
        batch.begin();
        rocketEmitter.render(batch);
        batch.end();
    }

    private void update(float dt) {
        rocketEmitter.update(dt);
    }
}
