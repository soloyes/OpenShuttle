package com.buran.game.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.buran.game.background.MenuSpace;
import com.buran.game.conf.MSConstants;
import com.buran.game.ui.groups.mvc.profiles.Profiles;
import com.buran.game.ui.menu.MainMenu;
import com.buran.game.ui.menu.MenuHero;

/**
 * Main menu screen: drifting nebula, the shuttle hero and the telemetry-style menu.
 *
 * @author soloyes on 3/24/18.
 */
public class MenuScreen extends BaseScreen {
    private MenuSpace space;
    private MainMenu mainMenu;
    private MenuHero menuHero;

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
        Profiles profiles = new Profiles();
        profiles.init();
        if (space != null) {
            space.dispose();
        }
        space = new MenuSpace();
        mainMenu = new MainMenu();
        menuHero = new MenuHero(mainMenu);
        stage.addActor(menuHero);
        stage.addActor(mainMenu);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        space.update(delta);
        stage.act(delta);
        space.render(batch);
        stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (mainMenu != null) {
            mainMenu.remove();
        }
        if (space != null) {
            space.dispose();
            space = null;
        }
    }

    @Override
    public void resume() {
        boomBox.resumeAllMusic();
    }
}
