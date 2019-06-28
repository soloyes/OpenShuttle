package com.shuttle.game.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shuttle.game.background.Background;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.ui.groups.ConfigGroup;
import com.shuttle.game.ui.groups.MenuBar;
import com.shuttle.game.ui.groups.MenuGroup;
import com.shuttle.game.ui.groups.ShareGroup;
import com.shuttle.game.ui.groups.mvc.results.ResultGroup;

/**
 * @author Shuttle on 3/24/18.
 */
public class MenuScreen extends BaseScreen {
    private Background background;
    private MenuGroup menuGroup;

    public MenuScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void show() {
        super.show();
        boomBox.stopAllMusicAndPlayThese(MSConstants.MENU_SONG);
    }

    @Override
    public void createGUI() {
        background = new Background();
        menuGroup = new MenuGroup();
        MenuBar menuBar = new MenuBar();
        ResultGroup resultGroup = new ResultGroup();
        ShareGroup shareGroup = new ShareGroup();
        stage.addActor(menuGroup);
        stage.addActor(ConfigGroup.getInstance());
        stage.addActor(menuBar);
        stage.addActor(resultGroup);
        stage.addActor(shareGroup);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        batch.begin();
        background.render(batch);
        batch.end();
        stage.draw();
    }

    public void update(float delta) {
        background.update(delta);
        stage.act();
    }

    @Override
    public void dispose() {
        super.dispose();
        menuGroup.dispose();
        ConfigGroup.getInstance().dispose();
    }
}