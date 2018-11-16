package com.shuttle.game.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shuttle.game.Background;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.screens.group.ConfigGroup;
import com.shuttle.game.screens.group.MenuBar;
import com.shuttle.game.screens.group.MenuGroup;
import com.shuttle.game.screens.group.ResultGroup;
import com.shuttle.game.screens.group.ShareGroup;

/**
 * @author Shuttle on 3/24/18.
 */
public class MenuScreen extends BaseScreen {
    private Background background;

    public MenuScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void show() {
        super.show();
        boomBox.stopAllMusicAndPlayThese(MSConstants.MENU_MUSIC);
    }

    @Override
    public void createGUI() {
        background = new Background();
        MenuGroup menuGroup = new MenuGroup();
        MenuBar menuBar = new MenuBar();
        ResultGroup resultGroup = new ResultGroup();
        ShareGroup shareGroup = new ShareGroup();
        menuBar.setGroup(menuGroup);
        stage.addActor(menuGroup);
        stage.addActor(new ConfigGroup(ScreenManager.ScreenType.MENU));
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
}