package com.shuttle.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.AlienEmitter;
import com.shuttle.game.Assets;
import com.shuttle.game.Background;
import com.shuttle.game.Consumable;
import com.shuttle.game.ConsumableEmitter;
import com.shuttle.game.ParticleEmitter;
import com.shuttle.game.Rules;
import com.shuttle.game.Shuttle;

public class GameScreen extends BaseScreen implements Screen {
    private SpriteBatch batch;
    private Shuttle player;
    private Background background;
    private ParticleEmitter particleEmitter;
    private ConsumableEmitter consumableEmitter;
    private AlienEmitter alienEmitter;
    private BitmapFont font64;
    private Camera camera;
    private Vector2 tmp;

    public GameScreen(SpriteBatch batch, Camera camera) {
        super(batch);
        this.batch = batch;
        this.camera = camera;
        this.tmp = new Vector2(0, 0);
    }

    @Override
    public void show() {
        createGUI();
        Assets.getInstance()
                .getAssetManager()
                .get("music/game.ogg", Music.class)
                .play();
        Assets.getInstance()
                .getAssetManager()
                .get("music/game.ogg", Music.class)
                .setLooping(true);
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
        alienEmitter.render(batch);
        consumableEmitter.render(batch);
        particleEmitter.render(batch);
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
        checkCollision(dt);
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

    public void checkCollision(float dt) {
        for (int i = 0; i < consumableEmitter.getActiveList().size(); i++) {
            Consumable a = consumableEmitter.getActiveList().get(i);
            if (player.getPosition().dst(a.getPosition()) < 60) {
                a.deactivate();
                player.addScore(1000);
            }

            if (player.getPosition().dst(a.getPosition()) < player.getTakeRange()) {
                particleEmitter.setupFromPointToPoint(a.getPosition().x, a.getPosition().y, player.getPosition().x, player.getPosition().y, 1400, 1, 2, 0, 1, 0, 0.3f, 0.5f, 1, 0.5f, 0.3f);

                tmp.set(player.getPosition());
                tmp.sub(a.getPosition());
                tmp.nor();
                tmp.scl(200.0f);
                a.getPosition().mulAdd(tmp, dt);
                float dst = Vector2.dst(player.getPosition().x, player.getPosition().y, a.getPosition().x, a.getPosition().y);
                float k = dst / player.getTakeRange();
                k += 0.2f;
                if (k > 1.0f) {
                    k = 1.0f;
                }

                for (int j = 0; j < 24; j++) {
                    float ang = 6.28f / 24 * j;
                    float rad = 128 * k;
                    particleEmitter.setup(a.getPosition().x + rad * (float)Math.cos(ang) + MathUtils.random(-10, 10), a.getPosition().y + rad * (float)Math.sin(ang) + MathUtils.random(-10, 10), MathUtils.random(-20,20), MathUtils.random(-20,20), 0.05f,3,3,0,1,0,0.2f,0,1,0,0.0f);
                }

                a.setDrawScale(k);
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
