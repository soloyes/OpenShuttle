package com.shuttle.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shuttle.game.ShuttleGame;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.ui.groups.mvc.profiles.Profiles;
import com.shuttle.game.utils.Assets;

import lombok.Getter;

/**
 * @author Shuttle on 03/06/18.
 */

public class ScreenManager {
    private static ScreenManager instance;
    private ShuttleGame game;
    private Screen targetScreen;
    private LoadingScreen loadingScreen;
    private GameScreen gameScreen;
    private MenuScreen menuScreen;
    private TestScreen testScreen;
    private SpriteBatch batch;
    private Viewport viewport;
    @Getter
    private Camera camera;
    @Getter
    private ScreenType currentScreen;

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
                Rules.setSpace(1);
                Assets.getInstance().loadAssets(ScreenType.MENU);
                break;
            case GAME:
                targetScreen = gameScreen;
                Rules.GAME_OVER = false;
                //SPACE_WIDTH/SPACE_HEIGHT depends from game level
                Assets.getInstance().loadAssets(ScreenType.GAME);
                Profiles profiles = new Profiles();
                Rules.setSpace(4 - profiles.getActive().getPreferences().getLevel());
                //
                break;
            case TEST:
                targetScreen = testScreen;
                Assets.getInstance().loadAssets(ScreenType.TEST);
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

    public void init(ShuttleGame game, SpriteBatch batch, boolean mobile) {
        this.game = game;
        this.batch = batch;
        if (mobile) {
            Rules.WORLD_WIDTH = Gdx.graphics.getWidth();
            Rules.WORLD_HEIGHT = Gdx.graphics.getHeight();
        } else {
            Rules.WORLD_WIDTH = 1080;
            Rules.WORLD_HEIGHT = 2520;
            // Rules.WORLD_HEIGHT = 1920;
        }

        this.camera = new OrthographicCamera(Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT);
        this.viewport = new FitViewport(Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT, camera);
        this.gameScreen = new GameScreen(batch, camera);
        this.menuScreen = new MenuScreen(batch);
        this.testScreen = new TestScreen(batch);
        this.loadingScreen = new LoadingScreen(batch);
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
        viewport.apply();
    }

    public Viewport getViewport() {
        return viewport;
    }

    public enum ScreenType {
        MENU, GAME, TEST
    }
}