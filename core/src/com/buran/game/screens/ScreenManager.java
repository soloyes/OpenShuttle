package com.buran.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.buran.game.AdsController;
import com.buran.game.BuranGame;
import com.buran.game.conf.Rules;
import com.buran.game.ui.groups.mvc.profiles.Profiles;
import com.buran.game.utils.Assets;

import lombok.Getter;
import lombok.Setter;

/**
 * @author soloyes on 03/06/18.
 */

public class ScreenManager {
    private static ScreenManager instance;
    private BuranGame game;
    private Screen targetScreen;
    private LoadingScreen loadingScreen;
    private GameScreen gameScreen;
    private MenuScreen menuScreen;
    private TestScreen testScreen;
    private AdsScreen adsScreen;
    private SpriteBatch batch;
    private Viewport viewport;
    @Getter
    private Camera camera;
    @Getter
    private ScreenType currentScreen;
    @Getter
    @Setter
    private AdsController adsController;

    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    private ScreenManager() {
    }

    public void changeScreen(ScreenType type) {
        Screen screen = game.getScreen();
        Gdx.input.setInputProcessor(null);
        Assets.getInstance().clear();
        if (screen != null) {
            screen.dispose();
        }
        resetCamera();
        game.setScreen(loadingScreen);
        currentScreen = type;
        switch (type) {
            case MENU:
                targetScreen = menuScreen;
                Rules.GAME_OVER = false;
                Rules.DEATH_MODE = false;
                Rules.setSpace(1);
                Assets.getInstance().loadAssets(ScreenType.MENU);
                break;
            case GAME:
                targetScreen = gameScreen;
                Rules.GAME_OVER = false;
                Rules.DEATH_MODE = false;
                //SPACE_WIDTH/SPACE_HEIGHT depends from game level
                Assets.getInstance().loadAssets(ScreenType.GAME);
                Profiles profiles = new Profiles();
                if (Rules.DEBUG){
                    profiles.getActive().getPreferences().setLevel(3);
                    profiles.write();
                }
                Rules.setSpace(4 - profiles.getActive().getPreferences().getLevel());
                //
                break;
            case TEST:
                targetScreen = testScreen;
                Assets.getInstance().loadAssets(ScreenType.TEST);
                break;
            case ADS:
                targetScreen = adsScreen;
                Assets.getInstance().loadAssets(ScreenType.ADS);
                break;
        }
    }

    public void resetCamera() {
        camera.position.set(Rules.WORLD_WIDTH / 2, Rules.WORLD_HEIGHT / 2, 0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }

    public void goToTarget() {
        game.setScreen(targetScreen);
    }

    public void init(BuranGame game, SpriteBatch batch, boolean mobile, int width, int height) {
        this.game = game;
        this.batch = batch;
        Rules.WORLD_HEIGHT = 1920;
        if (mobile) {
            if (Gdx.graphics.getHeight() > Rules.WORLD_HEIGHT) {
                Rules.WORLD_HEIGHT = Gdx.graphics.getHeight();
            }
            Rules.WORLD_WIDTH = Rules.WORLD_HEIGHT * Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
        } else {
            if (height > Rules.WORLD_HEIGHT) {
                Rules.WORLD_HEIGHT = height;
            }
            Rules.WORLD_WIDTH = Rules.WORLD_HEIGHT * width / height;
        }

        this.camera = new OrthographicCamera(Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT);
        this.viewport = new FitViewport(Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT, camera);
        this.gameScreen = new GameScreen(batch, camera);
        this.menuScreen = new MenuScreen(batch);
        this.testScreen = new TestScreen(batch);
        this.loadingScreen = new LoadingScreen(batch);
        this.adsScreen = new AdsScreen(batch);
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
        viewport.apply();
    }

    public Viewport getViewport() {
        return viewport;
    }

    public enum ScreenType {
        MENU, GAME, TEST, ADS
    }
}