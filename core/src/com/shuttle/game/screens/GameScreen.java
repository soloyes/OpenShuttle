package com.shuttle.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shuttle.game.AlienEmitter;
import com.shuttle.game.Assets;
import com.shuttle.game.Background;
import com.shuttle.game.Consumable;
import com.shuttle.game.ConsumableEmitter;
import com.shuttle.game.ParticleEmitter;
import com.shuttle.game.Rules;
import com.shuttle.game.Shuttle;
import com.shuttle.game.conf.GamePreferences;

public class GameScreen extends BaseScreen implements Screen {
    private SpriteBatch batch;
    private Shuttle player;
    private Background background;
    private ParticleEmitter particleEmitter;
    private ConsumableEmitter consumableEmitter;
    private AlienEmitter alienEmitter;
    private BitmapFont font64;
    private Camera camera;
    private GamePreferences preferences;

    public GameScreen(SpriteBatch batch, Camera camera) {
        super(batch);
        this.batch = batch;
        this.camera = camera;
        //:todo Maybe better to to Util class Preferences instead current realization.
        preferences = new GamePreferences();
    }

    @Override
    public void show() {
        createGUI();
        if (preferences.isMusicEnabled()) {
            Assets.getInstance()
                    .getAssetManager()
                    .get("music/game.ogg", Music.class)
                    .play();
            Assets.getInstance()
                    .getAssetManager()
                    .get("music/game.ogg", Music.class)
                    .setLooping(true);
            Assets.getInstance()
                    .getAssetManager()
                    .get("music/game.ogg", Music.class)
                    .setVolume(preferences.getMusicVolume());
        }
    }

    @Override
    void createGUI() {
        particleEmitter = new ParticleEmitter();
        alienEmitter = new AlienEmitter(this);
        consumableEmitter = new ConsumableEmitter(this);
        background = new Background();
        player = new Shuttle(this, 540, 960);
        font64 = Assets.getInstance().getAssetManager().get("fonts/gomarice64.ttf", BitmapFont.class);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
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
