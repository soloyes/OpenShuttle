package com.shuttle.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.shuttle.game.Alien;
import com.shuttle.game.AlienEmitter;
import com.shuttle.game.Assets;
import com.shuttle.game.Asteroid;
import com.shuttle.game.AsteroidEmitter;
import com.shuttle.game.Background;
import com.shuttle.game.Consumable;
import com.shuttle.game.ConsumableEmitter;
import com.shuttle.game.ParticleEmitter;
import com.shuttle.game.Rules;
import com.shuttle.game.Shuttle;
import com.shuttle.game.screens.group_screen.ConfigGroup;

public class GameScreen extends BaseScreen {
    private Shuttle player;
    private Background background;
    private ParticleEmitter particleEmitter;
    private ConsumableEmitter consumableEmitter;
    private AlienEmitter alienEmitter;
    private AsteroidEmitter asteroidEmitter;
    private BitmapFont font64;
    private Camera camera;
    private Vector2 tmp;
    private boolean pause;
    private ShaderProgram shaderProgram;

    public GameScreen(SpriteBatch batch, Camera camera) {
        super(batch);
        this.batch = batch;
        this.camera = camera;
        this.tmp = new Vector2(0, 0);
    }

    @Override
    public void show() {
        super.show();
        if (preferences.isMusicEnabled()) {
            Assets.getInstance()
                    .getAssetManager()
                    .get("music/game.ogg", Music.class)
                    .setVolume(preferences.getMusicVolume());
            Assets.getInstance()
                    .getAssetManager()
                    .get("music/game.ogg", Music.class)
                    .play();
            Assets.getInstance()
                    .getAssetManager()
                    .get("music/game.ogg", Music.class)
                    .setLooping(true);
        }
    }

    @Override
    public void createGUI() {
        pause = false;
        shaderProgram = new ShaderProgram(
                Gdx.files.internal("shaders/default.vert"),
                Gdx.files.internal("shaders/default.frag")
        );

        if (!shaderProgram.isCompiled()) {
            System.err.println(shaderProgram.getLog());
            System.exit(0);
        }

        particleEmitter = new ParticleEmitter();
        alienEmitter = new AlienEmitter();
        consumableEmitter = new ConsumableEmitter();
        asteroidEmitter = new AsteroidEmitter();
        background = new Background();
        player = new Shuttle(
                this,
                ScreenManager.getInstance().getViewport().getWorldWidth() / 2,
                ScreenManager.getInstance().getViewport().getWorldHeight() / 2
        );
        asteroidEmitter.setShuttle(player);
        font64 = Assets.getInstance().getAssetManager().get("fonts/gomarice64.ttf", BitmapFont.class);

        Skin skin = Assets.getInstance().getAssetManager().get("uiskin/gameSkin.json");
        Button pauseBtn = new Button(skin, "default");
        pauseBtn.setOrigin(Align.center);
        pauseBtn.setSize(128f, 128f);
        pauseBtn.setPosition(
                ScreenManager.getInstance().getViewport().getWorldWidth() - pauseBtn.getWidth() - 20,
                ScreenManager.getInstance().getViewport().getWorldHeight() - pauseBtn.getHeight() - 20
        );

        final ConfigGroup configGroup = new ConfigGroup();
        configGroup.setVisible(false);
        final Button buttonMenu = new Button(skin, "default");
        buttonMenu.setVisible(false);
        final Button buttonRestart = new Button(skin, "default");
        buttonRestart.setVisible(false);

        buttonMenu.setOrigin(Align.center);
        buttonRestart.setOrigin(Align.center);

        buttonMenu.setSize(128f, 128f);
        buttonRestart.setSize(128f, 128f);

        buttonRestart.setPosition(
                ScreenManager.getInstance().getViewport().getWorldWidth() - buttonRestart.getWidth(),
                0
        );

        buttonMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
            }
        });

        buttonRestart.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
            }
        });

        pauseBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pause = !pause;
                configGroup.setVisible(pause);
                buttonMenu.setVisible(pause);
                buttonRestart.setVisible(pause);
            }
        });

        stage.addActor(pauseBtn);
        stage.addActor(configGroup);
        stage.addActor(buttonMenu);
        stage.addActor(buttonRestart);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        batch.setProjectionMatrix(camera.combined);
        if (pause) {
            batch.setShader(shaderProgram);
        } else {
            batch.setShader(null);
        }

        //BEGIN
        batch.begin();
        background.render(batch);
        asteroidEmitter.render(batch);
        alienEmitter.render(batch);
        consumableEmitter.render(batch);
        player.render(batch);
        particleEmitter.render(batch);
        player.drawRadar(batch, player.getPosition().x, player.getPosition().y);
        batch.end();

        ScreenManager.getInstance().resetCamera();
        //BEGIN
        batch.begin();
        player.renderGUI(batch, font64);
        batch.end();

        batch.setShader(null);
        stage.draw();
    }

    public void update(float dt) {
        cameraUpdate();
        if (!pause) {
            background.update(dt);
            player.update(dt);
            alienEmitter.update(dt);
            consumableEmitter.update(dt);
            asteroidEmitter.update(dt);
            particleEmitter.update(dt);
            checkCollision(dt);
        }
        cameraUpdate();
        stage.act();
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
                    particleEmitter.setup(a.getPosition().x + rad * (float) Math.cos(ang) + MathUtils.random(-10, 10), a.getPosition().y + rad * (float) Math.sin(ang) + MathUtils.random(-10, 10), MathUtils.random(-20, 20), MathUtils.random(-20, 20), 0.05f, 3, 3, 0, 1, 0, 0.2f, 0, 1, 0, 0.0f);
                }

                a.setDrawScale(k);
            }
        }

        for (int i = 0; i < alienEmitter.getActiveList().size(); i++) {
            Alien a = alienEmitter.getActiveList().get(i);
            if (player.getPosition().dst(a.getPosition()) < 160 && player.getSpeedRang().getLevel() > 0) {
                a.destroy();
                player.addScore(5000);
                for (int j = 0; j < 30; j++) {
                    particleEmitter.setup(a.getPosition().x + MathUtils.random(-30, 30), a.getPosition().y + MathUtils.random(-30, 30), MathUtils.random(-400, 400), MathUtils.random(-400, 400), 0.4f, 8, 4, 0, 1, 0, 1f, 0, 1, 0, 0.2f);

                }
            }
        }

        for (int i = 0; i < asteroidEmitter.getActiveList().size(); i++) {
            Asteroid a = asteroidEmitter.getActiveList().get(i);
            if (!a.isFragment() && player.getPosition().dst(a.getPosition()) < 128) {
                a.destroy();
                float dxy = 180;
                int dustCover = 0;
                switch (player.getSpeedRang()) {
                    case NONE:
                        dustCover += 32;
                        break;
                    case WHITE:
                        dustCover += 64;
                        break;
                    case GREEN:
                        dustCover += 96;
                        break;
                    case YELLOW:
                        dustCover += 128;
                        break;
                    case RED:
                        dustCover += 140;
                        break;
                }
                for (int j = 0; j < dustCover; j++) {
                    particleEmitter.setup(
                            a.getPosition().x,
                            a.getPosition().y,
                            MathUtils.random(-dxy, dxy),
                            MathUtils.random(-dxy, dxy),
                            1f,
                            6.0f,
                            1.0f,
                            0.17f,
                            0.17f,
                            0.17f,
                            1,
                            0,
                            0,
                            0,
                            0
                    );
                }
                asteroidEmitter.boom(a.getPosition());
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        shaderProgram.dispose();
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
