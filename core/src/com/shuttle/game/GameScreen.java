package com.shuttle.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private Shuttle player;
    private Background background;
    private ParticleEmitter particleEmitter;
    private ConsumableEmitter consumableEmitter;
    private AlienEmitter alienEmitter;
    private BitmapFont font64;
    private Camera camera;

    public GameScreen(SpriteBatch batch, Camera camera) {
        this.batch = batch;
        this.camera = camera;
    }

    @Override
    public void show() {
        particleEmitter = new ParticleEmitter();
        alienEmitter = new AlienEmitter(this);
        consumableEmitter = new ConsumableEmitter(this);
        background = new Background();
        player = new Shuttle(this, 540, 960);
        font64 = Assets.getInstance().getAssetManager().get("gomarice64.ttf", BitmapFont.class);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0.05f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        background.render(batch);
        player.render(batch);
        particleEmitter.render(batch);
        alienEmitter.render(batch);
        consumableEmitter.render(batch);
        batch.end();
        ScreenManager.getInstance().resetCamera();
        batch.begin();
        player.renderGUI(batch, font64);
        batch.end();
    }

    public void update(float dt) {
        cameraUpdate();
        background.update(dt);
        player.update(dt);
        alienEmitter.update(dt);
        consumableEmitter.update(dt);
        particleEmitter.update(dt);
        checkCollision();
        cameraUpdate();
    }

    public void cameraUpdate() {
        camera.position.set(player.getPosition().x, player.getPosition().y, 0);
        if (camera.position.x < 540) {
            camera.position.x = 540;
        }
        if (camera.position.y < 960) {
            camera.position.y = 960;
        }
        if (camera.position.x > Rules.SPACE_WIDTH - 540) {
            camera.position.x = Rules.SPACE_WIDTH - 540;
        }
        if (camera.position.y > Rules.SPACE_HEIGHT - 960) {
            camera.position.y = Rules.SPACE_HEIGHT - 960;
        }

        camera.update();
    }

    public void checkCollision() {
        for (int i = 0; i < consumableEmitter.getActiveList().size(); i++) {
            Consumable a = consumableEmitter.getActiveList().get(i);
            if (player.getPosition().dst(a.getPosition()) < 144) {
                a.deactivate();
                player.addScore(1000);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        ScreenManager.getInstance().resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public AlienEmitter getAlienEmitter() {
        return alienEmitter;
    }

    public ConsumableEmitter getConsumableEmitter() {
        return consumableEmitter;
    }

    public ParticleEmitter getParticleEmitter() {
        return particleEmitter;
    }
}
