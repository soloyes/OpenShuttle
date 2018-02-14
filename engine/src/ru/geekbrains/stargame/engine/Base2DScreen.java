package ru.geekbrains.stargame.engine;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.engine.math.MatrixUtils;
import ru.geekbrains.stargame.engine.math.Rect;

public abstract class Base2DScreen implements Screen, InputProcessor {

    protected Game game;

    private Rect screenBounds; // границы области рисования в пикселях
    private Rect worldBounds; // границы проекции мировых координат
    private Rect glBounds; // дефолтные границы проекции мир - gl

    private Matrix4 worldToGl;
    private Matrix3 screenToWorld;

    protected SpriteBatch batch;

    private final Vector2 touch = new Vector2();

    public Base2DScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        System.out.println("show");
        Gdx.input.setInputProcessor(this);
        this.screenBounds = new Rect();
        this.worldBounds = new Rect();
        this.glBounds = new Rect(0, 0, 1f, 1f);
        this.worldToGl = new Matrix4();
        this.screenToWorld = new Matrix3();
        if (batch != null) {
            throw new RuntimeException("batch != null, set screen without dispose dispose");
        }
        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("resize width= " + width + " height = " + height);
        screenBounds.setSize(width, height);
        screenBounds.setLeft(0);
        screenBounds.setBottom(0);

        float aspect = width / (float) height;
        worldBounds.setHeight(1f);
        worldBounds.setWidth(1f * aspect);
        MatrixUtils.calcTransitionMatrix(worldToGl, worldBounds, glBounds);
        batch.setProjectionMatrix(worldToGl);
        MatrixUtils.calcTransitionMatrix(screenToWorld, screenBounds, worldBounds);

        resize(worldBounds);
    }

    protected abstract void resize(Rect worldBounds);

    @Override
    public void pause() {
        System.out.println("pause");
    }

    @Override
    public void resume() {
        System.out.println("resume");
    }

    @Override
    public void hide() {
        System.out.println("hide");
    }

    @Override
    public void dispose() {
        System.out.println("dispose");
        batch.dispose();
        batch = null;
    }

    @Override
    public boolean keyDown(int keycode) {
        System.out.println("keyDown keycode=" + keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        System.out.println("keyUp keycode=" + keycode);
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        System.out.println("keyTyped character=" + character);
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touch.set(screenX, screenBounds.getHeight() - screenY).mul(screenToWorld);
        touchDown(touch, pointer);
        checkPlayerToched();
        System.out.println("touchDown X=" + touch.x + " Y=" + touch.y);
        return false;
    }

    protected abstract void touchDown(Vector2 touch, int pointer);

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        touch.set(screenX, screenBounds.getHeight() - screenY).mul(screenToWorld);
        touchUp(touch, pointer);
        System.out.println("touchUp X=" + touch.x + " Y=" + touch.y);
        return false;
    }

    protected abstract void touchUp(Vector2 touch, int pointer);

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        touch.set(screenX, screenBounds.getHeight() - screenY).mul(screenToWorld);
        touchDown(touch, pointer);
        checkPlayerToched();
        System.out.println("touchDragged X=" + touch.x + " Y=" + touch.y);
        return false;
    }

    protected abstract void touchDragged(Vector2 touch, int pointer);

    private void checkPlayerToched(){
        if (touch.x < worldBounds.getLeft()) touch.x = worldBounds.getLeft();
        if (touch.x > worldBounds.getRight()) touch.x = worldBounds.getRight();
        if (touch.y < worldBounds.getBottom()) touch.y = worldBounds.getBottom();
        if (touch.y > worldBounds.getTop()) touch.y = worldBounds.getTop();
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}