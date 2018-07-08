package com.shuttle.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.Alien;
import com.shuttle.game.AlienEmitter;
import com.shuttle.game.Assets;
import com.shuttle.game.Asteroid;
import com.shuttle.game.AsteroidEmitter;
import com.shuttle.game.Background;
import com.shuttle.game.Consumable;
import com.shuttle.game.ConsumableEmitter;
import com.shuttle.game.GameInfoSystem;
import com.shuttle.game.ParticleEmitter;
import com.shuttle.game.Poolable;
import com.shuttle.game.Rules;
import com.shuttle.game.Shuttle;
import com.shuttle.game.screens.group_screen.PauseGroup;

import lombok.Data;

@Data
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
    private ShaderProgram shaderProgram;
    private GameInfoSystem gameInfoSystem;
    private PauseGroup pauseGroup;
    ///
    private Consumable tmpConsumable;
    private Asteroid tmpAsteroid;
    private Alien tmpAlien;
    ///

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
        shaderProgram = new ShaderProgram(
                Gdx.files.internal("shaders/default.vert"),
                Gdx.files.internal("shaders/default.frag")
        );

        if (!shaderProgram.isCompiled()) {
            System.err.println(shaderProgram.getLog());
            System.exit(0);
        }

        particleEmitter = new ParticleEmitter();
        alienEmitter = new AlienEmitter(this);
        consumableEmitter = new ConsumableEmitter();
        asteroidEmitter = new AsteroidEmitter();
        background = new Background();
        gameInfoSystem = new GameInfoSystem();
        player = new Shuttle(
                this,
                ScreenManager.getInstance().getViewport().getWorldWidth() / 2,
                ScreenManager.getInstance().getViewport().getWorldHeight() / 2
        );
        font64 = Assets.getInstance().getAssetManager().get("fonts/gomarice64.ttf", BitmapFont.class);
        pauseGroup = new PauseGroup();
        stage.addActor(pauseGroup);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        batch.setProjectionMatrix(camera.combined);
        if (pauseGroup.isPause()) {
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
        gameInfoSystem.render(batch);
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
        if (!pauseGroup.isPause()) {
            background.update(dt);
            player.update(dt);
            alienEmitter.update(dt);
            consumableEmitter.update(dt);
            asteroidEmitter.update(dt);
            particleEmitter.update(dt);
            gameInfoSystem.update(dt);
            checkCollision(dt);
        }
        cameraUpdate();
        stage.act();
    }

    public void cameraUpdate() {
        camera.position.set(player.getPosition().x, player.getPosition().y, 0);

        if (camera.position.x < Rules.WORLD_WIDTH / 2) {
            camera.position.x = Rules.WORLD_WIDTH / 2;
        }
        if (camera.position.y < Rules.WORLD_HEIGHT / 2) {
            camera.position.y = Rules.WORLD_HEIGHT / 2;
        }
        if (camera.position.x > Rules.SPACE_WIDTH - Rules.WORLD_WIDTH / 2) {
            camera.position.x = Rules.SPACE_WIDTH - Rules.WORLD_WIDTH / 2;
        }
        if (camera.position.y > Rules.SPACE_HEIGHT - Rules.WORLD_HEIGHT / 2) {
            camera.position.y = Rules.SPACE_HEIGHT - Rules.WORLD_HEIGHT / 2;
        }

        camera.update();
    }

    public void checkCollision(float dt) {
        for (int i = 0; i < consumableEmitter.getActiveList().size(); i++) {
            tmpConsumable = consumableEmitter.getActiveList().get(i);
            if (player.getPosition().dst(tmpConsumable.getPosition()) < 60) {
                if (player.getSpeedRang().getLevel() < Shuttle.SpeedRang.GREEN.getLevel()) {
                    tmpConsumable.deactivate();
                    player.takeEffect(tmpConsumable.getType());
                } else {
                    tmpConsumable.deactivate();
                    player.badEffect(tmpConsumable.getType());
                }
            }

            if (player.getPosition().dst(tmpConsumable.getPosition()) < player.getTakeRange()) {
                tmp.set(player.getPosition()).sub(tmpConsumable.getPosition()).nor().scl(200.0f);
                tmpConsumable.getPosition().mulAdd(tmp, dt);
                float dst = Vector2.dst(player.getPosition().x, player.getPosition().y, tmpConsumable.getPosition().x, tmpConsumable.getPosition().y);
                float k = dst / player.getTakeRange();
                k += 0.2f;
                if (k > 1.0f) {
                    k = 1.0f;
                }

                particleEmitter.setupFromPointToPoint(tmpConsumable.getPosition().x, tmpConsumable.getPosition().y, player.getPosition().x, player.getPosition().y, 1, 2, 0, 1, 0, 0.3f, 0.5f, 1, 0.5f, 0.3f);
                for (int j = 0; j < 24; j++) {
                    float ang = 6.28f / 24 * j;
                    float rad = 128 * k;

                    particleEmitter.setup(tmpConsumable.getPosition().x + rad * (float) Math.cos(ang) + MathUtils.random(-10, 10), tmpConsumable.getPosition().y + rad * (float) Math.sin(ang) + MathUtils.random(-10, 10), 0, 0, 0.1f, 4, 4, 0, 1, 0, 0.2f, 0, 1, 0, 0.0f);
                }

                tmpConsumable.setDrawScale(k);
            }
        }

        for (int i = 0; i < alienEmitter.getActiveList().size(); i++) {
            tmpAlien = alienEmitter.getActiveList().get(i);
            if (player.getPosition().dst(tmpAlien.getPosition()) < 160) {
                if (tmpAlien.getLevel() < player.getSpeedRang().getLevel()) {
                    tmpAlien.destroy();
                    int asc = (tmpAlien.getMaxLevel() + 1) * 5000;
                    player.addScore(asc);
                    player.slowDown(tmpAlien.getLevel() * 50.0f);
                    showSpray(tmpAlien);
                    player.killPlusPlus();
                } else {
                    if (tmpAlien.getLevel() == 3) {
                        player.getRadar().setActive(false);
                    }

                    if (player.addZigZag(0.5f * tmpAlien.getLevel() + 0.2f)) {
                        gameInfoSystem.setup(player.getPosition().x, player.getPosition().y, "BREAKED", 5);
                        player.takeDamage(tmpAlien.getLevel() + 1);
                        tmp.set(player.getPosition()).sub(tmpAlien.getPosition()).nor().scl(300 + tmpAlien.getLevel() * 50);
                        player.getVelocity().add(tmp);
                        tmp.scl(-1);
                        tmpAlien.getVelocity().add(tmp);
                        tmpAlien.takeDamage(player.getSpeedRang().getLevel());
                        tmpAlien.setHungry(false);
                        tmpAlien.setSlimTimer(0);
                    }
                }
            }
        }

        for (int i = 0; i < asteroidEmitter.getActiveList().size(); i++) {
            tmpAsteroid = asteroidEmitter.getActiveList().get(i);
            if (!tmpAsteroid.isFragment() && player.getPosition().dst(tmpAsteroid.getPosition()) < 128) {
                tmpAsteroid.destroy();
                showDust(tmpAsteroid);
                asteroidEmitter.boom(
                        tmpAsteroid.getPosition(),
                        Shuttle.SpeedRang.NONE
                );
                player.takeDamage(player.getSpeedRang().getLevel() + 1);
                gameInfoSystem.setup(player.getPosition().x, player.getPosition().y, "SCREECH", 3);
            }

            for (int j = 0; j < alienEmitter.getActiveList().size(); j++) {
                tmpAlien = alienEmitter.getActiveList().get(j);
                if (!tmpAsteroid.isFragment() && tmpAlien.getPosition().dst(tmpAsteroid.getPosition()) < 128) {
                    tmpAsteroid.destroy();
                    showDust(tmpAsteroid);
                    asteroidEmitter.boom(
                            tmpAsteroid.getPosition(),
                            Shuttle.SpeedRang.NONE
                    );
                    gameInfoSystem.setup(tmpAlien.getPosition().x, tmpAlien.getPosition().y, "BOOOOM", 3);
                    if (tmpAlien.getLevel() == 0) {
                        tmpAlien.destroy();
                        showSpray(tmpAlien);
                    } else {
                        tmpAlien.takeDamage(1);
                    }
                }
            }

            for (int j = 0; j < consumableEmitter.getActiveList().size(); j++) {
                tmpConsumable = consumableEmitter.getActiveList().get(j);
                if (!tmpAsteroid.isFragment() && tmpConsumable.getPosition().dst(tmpAsteroid.getPosition()) < 128) {
                    tmpAsteroid.destroy();
                    tmpConsumable.deactivate();
                    asteroidEmitter.boom(
                            tmpAsteroid.getPosition(),
                            Shuttle.SpeedRang.NONE
                    );
                    showDust(tmpAsteroid);
                    showSpray(tmpConsumable);
                    gameInfoSystem.setup(tmpConsumable.getPosition().x, tmpConsumable.getPosition().y, "SO SAD", 3);
                }
            }
        }
    }

    private void showSpray(Poolable a) {
        for (int j = 0; j < 30; j++) {
            particleEmitter.setup(a.getPosition().x + MathUtils.random(-30, 30), a.getPosition().y + MathUtils.random(-30, 30), MathUtils.random(-400, 400), MathUtils.random(-400, 400), 0.4f, 8, 4, 0, 1, 0, 1f, 0, 1, 0, 0.2f);
        }
    }

    private void showDust(Asteroid a) {
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
    }

    @Override
    public void dispose() {
        super.dispose();
        shaderProgram.dispose();
    }
}
