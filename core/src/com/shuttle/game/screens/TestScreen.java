package com.shuttle.game.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shuttle.game.conf.Results;
import com.shuttle.game.screens.group.ResultGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Shuttle on 6/28/18.
 */
public class TestScreen extends BaseScreen {
    private ResultGroup resultGroup;
    private Results results;
    public TestScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void createGUI() {
        resultGroup = new ResultGroup();
        stage.addActor(resultGroup);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        stage.draw();
    }

    private void update(float delta) {
        stage.act();
    }
}
