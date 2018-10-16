package com.shuttle.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.*;
import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.ShuttleGame;

public class ScreenManager {
    private static ScreenManager ourInstance = new ScreenManager();
    private ShuttleGame game;
    private Screen targetScreen;
    private LoadingScreen loadingScreen;
    private GameScreen gameScreen;
    private MenuScreen menuScreen;
    private TestScreen testScreen;
    private SpriteBatch batch;
    private Viewport viewport;
    private Camera camera;

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
        switch (type) {
            case MENU:
                targetScreen = menuScreen;
                Rules.setSpace(1);
                Assets.getInstance().loadAssets(ScreenType.MENU);
                break;
            case GAME:
                targetScreen = gameScreen;
                //SPACE_WIDTH/SPACE_HEIGHT depends from game level
                Rules.setSpace(4 - GamePreferences.getLevel());
                //
                Assets.getInstance().loadAssets(ScreenType.GAME);
                break;
            case TEST:
                targetScreen = testScreen;
                Assets.getInstance().loadAssets(ScreenType.TEST);
                break;
        }
    }

    public void resetCamera() {
        camera.position.set(540, 960, 0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }

    public void goToTarget() {
        game.setScreen(targetScreen);
    }

    public void init(ShuttleGame game, SpriteBatch batch) {
        this.game = game;
        this.batch = batch;
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

    public static ScreenManager getInstance() {
        return ourInstance;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public enum ScreenType {
        MENU, GAME, CONFIG, TEST
    }
}
