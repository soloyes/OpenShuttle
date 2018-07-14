package com.shuttle.game.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shuttle.game.Background;
import com.shuttle.game.screens.group_screen.ConfigGroup;
import com.shuttle.game.screens.group_screen.MenuBar;
import com.shuttle.game.screens.group_screen.MenuGroup;

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
        boomBox.play("music/menu.ogg");
    }

    @Override
    public void createGUI() {
        background = new Background();
        MenuGroup menuGroup = new MenuGroup();
        MenuBar menuBar = new MenuBar();
        menuBar.setGroup(menuGroup);
        stage.addActor(menuGroup);
        stage.addActor(new ConfigGroup());
        stage.addActor(menuBar);
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
    }
}