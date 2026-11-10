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
        MainMenu.onIntro = this::playIntro;
        // Every launch opens with the intro (skippable); coming back from a flight does not.
        if (!introShown) {
            introShown = true;
            com.buran.game.platform.Shell.introPending(true);
            playIntro();
        }
    }

    /**
     * The opening comic, with its own score; the menu is hidden under it and comes back with
     * the menu song when the intro ends or is skipped.
     */
    private static boolean introShown;

    private void playIntro() {
        mainMenu.setVisible(false);
        menuHero.setVisible(false);
        com.buran.game.ui.menu.Intro intro = new com.buran.game.ui.menu.Intro(() -> {
            mainMenu.setVisible(true);
            menuHero.setVisible(true);
            com.buran.game.ui.menu.MenuNav.set(mainMenu);
            com.buran.game.platform.Shell.introPending(false);
            boomBox.stopAllMusicAndPlayThese(MSConstants.MENU_SONG);
        }, () -> boomBox.stopAllMusicAndPlayThese(MSConstants.INTRO_SONG));
        stage.addActor(intro);
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
