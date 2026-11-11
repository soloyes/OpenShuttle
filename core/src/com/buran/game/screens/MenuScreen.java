package com.buran.game.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.buran.game.background.MenuSpace;
import com.buran.game.conf.MSConstants;
import com.buran.game.ui.groups.mvc.profiles.Profiles;
import com.buran.game.ui.menu.MainMenu;
import com.buran.game.ui.menu.MenuHero;

/**
 * Main menu screen: drifting nebula, the Buran hero and the telemetry-style menu.
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
        // The painted key art has its own Buran; the drawn one would compete with it.
        menuHero.setVisible(!com.badlogic.gdx.Gdx.files.internal("ui/menu.jpg").exists());
        heroWanted = menuHero.isVisible();
        stage.addActor(menuHero);
        stage.addActor(mainMenu);
        MainMenu.onIntro = this::playIntro;
        // Every launch opens with the intro (skippable); coming back from a flight does not.
        if (!introShown) {
            introShown = true;
            com.buran.game.platform.Shell.introPending(true);
            if (com.buran.game.conf.Rules.DEBUG_CHAPTER) {
                playComic(com.buran.game.ui.menu.Intro.CHAPTER_ONE);
            } else {
                playIntro();
            }
        } else if (com.buran.game.campaign.Campaign.takeChapterEnd() == 1) {
            playComic(com.buran.game.ui.menu.Intro.CHAPTER_ONE);
        }
    }

    /**
     * The opening comic, with its own score; the menu is hidden under it and comes back with
     * the menu song when the intro ends or is skipped.
     */
    private static boolean introShown;
    private boolean heroWanted;

    private void playIntro() {
        playComic(com.buran.game.ui.menu.Intro.INTRO);
    }

    private void playComic(com.buran.game.ui.menu.Intro.Script script) {
        mainMenu.setVisible(false);
        menuHero.setVisible(false);
        com.buran.game.ui.menu.Intro intro = new com.buran.game.ui.menu.Intro(script, () -> {
            mainMenu.setVisible(true);
            menuHero.setVisible(heroWanted);
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
