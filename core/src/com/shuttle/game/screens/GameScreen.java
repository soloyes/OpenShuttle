package com.shuttle.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.AbstractPoolable;
import com.shuttle.game.bot.anomaly.CorrosiveAnomaly;
import com.shuttle.game.bot.anomaly.ElectricalAnomaly;
import com.shuttle.game.bot.anomaly.ElectricalAnomalyEmitter;
import com.shuttle.game.bot.anomaly.MagneticAnomaly;
import com.shuttle.game.bot.anomaly.MagneticAnomalyEmitter;
import com.shuttle.game.bot.anomaly.RadiationAnomaly;
import com.shuttle.game.bot.anomaly.RadiationAnomalyEmitter;
import com.shuttle.game.conf.AnomalyTimer;
import com.shuttle.game.player.PlayerPointer;
import com.shuttle.game.conf.GlobalInteractionLevels;
import com.shuttle.game.Joystick;
import com.shuttle.game.bot.anomaly.CorrosiveAnomalyEmitter;
import com.shuttle.game.bot.Asteroid;
import com.shuttle.game.bot.AsteroidEmitter;
import com.shuttle.game.Background;
import com.shuttle.game.bot.Consumable;
import com.shuttle.game.bot.ConsumableEmitter;
import com.shuttle.game.GameInfoSystem;
import com.shuttle.game.bot.ParticleEmitter;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.screens.group.PauseGroup;
import com.shuttle.game.utils.Shake;

import lombok.Data;

@Data
public class GameScreen extends BaseScreen {
    private Shuttle player;
    private Background background;
    private ParticleEmitter particleEmitter;
    private ConsumableEmitter consumableEmitter;
    private CorrosiveAnomalyEmitter corrosiveAnomalyEmitter;
    private ElectricalAnomalyEmitter electricalAnomalyEmitter;
    private RadiationAnomalyEmitter radiationAnomalyEmitter;
    private MagneticAnomalyEmitter magneticAnomalyEmitter;
    private AsteroidEmitter asteroidEmitter;
    private Camera camera;
    private Vector2 tmp;
    private ShaderProgram shaderProgram;
    private GameInfoSystem gameInfoSystem;
    private PauseGroup pauseGroup;
    private Joystick joystick;
    private PlayerPointer pointer;
    private Shake shake;
    private AnomalyTimer anomalyTimer;

    public GameScreen(SpriteBatch batch, Camera camera) {
        super(batch);
        this.batch = batch;
        this.camera = camera;
        this.tmp = new Vector2(0, 0);
        this.shake = new Shake();
    }

    @Override
    public void show() {
        super.show();
        boomBox.stopAllMusicAndPlay(MSConstants.GAME_MUSIC);
        shaderProgram = new ShaderProgram(
                Gdx.files.internal("shaders/default.vert"),
                Gdx.files.internal("shaders/default.frag")
        );
        if (!shaderProgram.isCompiled()) {
            System.err.println(shaderProgram.getLog());
            System.exit(0);
        }
        particleEmitter = new ParticleEmitter();
        corrosiveAnomalyEmitter = new CorrosiveAnomalyEmitter(this);
        electricalAnomalyEmitter = new ElectricalAnomalyEmitter(this);
        radiationAnomalyEmitter = new RadiationAnomalyEmitter(this);
        magneticAnomalyEmitter = new MagneticAnomalyEmitter(this);
        consumableEmitter = new ConsumableEmitter();
        asteroidEmitter = new AsteroidEmitter();
        background = new Background();
        gameInfoSystem = new GameInfoSystem();
        joystick = new Joystick();
        InputMultiplexer im = new InputMultiplexer(stage, joystick);
        Gdx.input.setInputProcessor(im);

        player = new Shuttle(this, joystick, preferences);
        pointer = new PlayerPointer(player);
        Rules.StartLevel.values()[preferences.getLevel()].setGameLevel(player);

        anomalyTimer = new AnomalyTimer();
    }

    @Override
    public void createGUI() {
        pauseGroup = new PauseGroup();
        stage.addActor(pauseGroup);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (!Rules.GAME_OVER) {
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
            player.render(batch);
            consumableEmitter.render(batch);
            corrosiveAnomalyEmitter.render(batch);
            electricalAnomalyEmitter.render(batch);
            radiationAnomalyEmitter.render(batch);
            magneticAnomalyEmitter.render(batch);
            player.getRamShield().render(batch);
            particleEmitter.render(batch);
            gameInfoSystem.render(batch);
            pointer.render(batch);
            batch.end();

            ScreenManager.getInstance().resetCamera();
            //BEGIN
            batch.begin();
            player.getStanding().render(batch);
            anomalyTimer.render(batch);
            if (!pauseGroup.isPause()) {
                joystick.render(batch);
            }
            batch.end();

            batch.setShader(null);

            stage.draw();
        } else {
            boomBox.stopAllMusicAndPlayOnce(MSConstants.MENU_MUSIC);
            gameInfoSystem.update(delta);
            batch.setShader(shaderProgram);
            batch.begin();
            background.update(delta);
            background.render(batch);
            gameInfoSystem.render(batch);
            player.getStanding().render(batch);
            batch.end();
            batch.setShader(null);
            pauseGroup.setGameOver();
            stage.draw();
        }
    }

    public void update(float dt) {
        cameraUpdate();
        if (!pauseGroup.isPause()) {
            background.update(dt);
            player.update(dt);
            corrosiveAnomalyEmitter.update(dt);
            electricalAnomalyEmitter.update(dt);
            radiationAnomalyEmitter.update(dt);
            magneticAnomalyEmitter.update(dt);
            consumableEmitter.update(dt);
            asteroidEmitter.update(dt);
            particleEmitter.update(dt);
            gameInfoSystem.update(dt);
            anomalyTimer.update(dt);
            checkCollision(dt);
            pointer.update(dt);
        }
        cameraUpdate();
        if (!pauseGroup.isPause()) {
            shake.update(dt, camera);
        }
        stage.act();
    }

    private void cameraUpdate() {
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

    private void checkCollision(float dt) {
        for (int i = 0; i < consumableEmitter.getActiveList().size(); i++) {
            Consumable tmpConsumable = consumableEmitter.getActiveList().get(i);
            if (player.getPosition().dst(tmpConsumable.getPosition()) < 128) {
                if (player.getShieldLevel().getLevel() < GlobalInteractionLevels.GREEN.getLevel()) {
                    tmpConsumable.destroy();
                    player.takeEffect(tmpConsumable.getType());
                } else {
                    if (!player.canCatch(tmpConsumable)) {
                        tmpConsumable.destroy();
                        player.badEffect(tmpConsumable.getType());
                    } else {
                        tmpConsumable.destroy();
                        player.takeEffect(tmpConsumable.getType());
                    }
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

                tmpConsumable.setScaleX(k);
                tmpConsumable.setScaleY(k);
            }
        }

        for (int i = 0; i < corrosiveAnomalyEmitter.getActiveList().size(); i++) {
            CorrosiveAnomaly tmpAnomaly = corrosiveAnomalyEmitter.getActiveList().get(i);
            if (player.getPosition().dst(tmpAnomaly.getPosition()) < 100 && tmpAnomaly.isBorn()) {
                if (tmpAnomaly.getLevel() < player.getPushPower()) {
                    damageToAnomaly(tmpAnomaly);
                    boomBox.playSound(MSConstants.ANOMALY_CORROSIVE_DESTROY);
                } else {
                    damageToPlayer(tmpAnomaly);
                    if (tmpAnomaly.getLevel() != 4) {
                        tmpAnomaly.takeDamage(player.getPushPower());
                    }
                    tmpAnomaly.setHungry(false);
                    tmpAnomaly.setSlimTimer(0);
                }
            }
        }

        for (int i = 0; i < electricalAnomalyEmitter.getActiveList().size(); i++) {
            ElectricalAnomaly tmpAnomaly = electricalAnomalyEmitter.getActiveList().get(i);
            if (player.getPosition().dst(tmpAnomaly.getPosition()) < 100 && tmpAnomaly.isBorn()) {
                if (tmpAnomaly.getLevel() < player.getPushPower()) {
                    damageToPlayer(tmpAnomaly);
                } else {
                    damageToPlayer(tmpAnomaly);
                }
            }
        }

        //
        boolean tmp1 = false;
        for (int i = 0; i < radiationAnomalyEmitter.getActiveList().size(); i++) {
            RadiationAnomaly tmpAnomaly = radiationAnomalyEmitter.getActiveList().get(i);
            if (player.getPosition().dst(tmpAnomaly.getPosition()) < 50 && tmpAnomaly.isBorn()) {
                tmpAnomaly.setHungry(true);
                tmpAnomaly.setGlued(true);
            } else {
                tmpAnomaly.setHungry(false);
                tmpAnomaly.setHungryTimer(0.0f);
                tmpAnomaly.setGlued(false);
            }

            if (tmpAnomaly.isHungry()) {
                tmpAnomaly.addHungryTimer(dt);
                if (tmpAnomaly.getHungryTimer() >= Rules.ANOMALIES_RADIATION_HUNGRY_TIME) {
                    tmpAnomaly.setHungryTimer(0.0f);
                    player.getStanding().changeScore(-Rules.ANOMALIES_RADIATION_SCORE_DECREASE);
                }
            }
            tmp1 |= tmpAnomaly.isGlued();
        }
        player.getStanding().setScoreAlertRadiationAnomaly(tmp1);

        boolean tmp2 = false;
        for (int i = 0; i < magneticAnomalyEmitter.getActiveList().size(); i++) {
            MagneticAnomaly tmpAnomaly = magneticAnomalyEmitter.getActiveList().get(i);
            if (player.getPosition().dst(tmpAnomaly.getPosition()) < 50 && tmpAnomaly.isBorn()) {
                tmpAnomaly.setHungry(true);
                tmpAnomaly.setGlued(true);
            } else {
                tmpAnomaly.setHungry(false);
                tmpAnomaly.setHungryTimer(0.0f);
                tmpAnomaly.setGlued(false);
            }

            if (tmpAnomaly.isHungry()) {
                tmpAnomaly.addHungryTimer(dt);
                if (tmpAnomaly.getHungryTimer() >= Rules.ANOMALIES_MAGNETIC_HUNGRY_TIME) {
                    tmpAnomaly.setHungryTimer(0.0f);
                    player.getStanding().changeFuel(-Rules.ANOMALIES_MAGNETIC_FUEL_DECREASE);
                }
            }
            tmp2 |= tmpAnomaly.isGlued();
        }
        player.getStanding().setFuelAlertMagneticAnomaly(tmp2);
        //

        for (int i = 0; i < asteroidEmitter.getActiveList().size(); i++) {
            Asteroid tmpAsteroid = asteroidEmitter.getActiveList().get(i);
            if (player.getPosition().dst(tmpAsteroid.getPosition()) < tmpAsteroid.getHeight()) {
                if (!tmpAsteroid.isFragmented()) {
                    tmpAsteroid.destroy();
                    player.getLifeShield().blink(1.0f);
                    particleEmitter.getBuilder().buildDust(tmpAsteroid.getPosition().x, tmpAsteroid.getPosition().y, 180, player.getShieldLevel().getLevel() * 16 + 32);
                    asteroidEmitter.boom(tmpAsteroid.getPosition(), player.getShieldLevel());
                    player.takeDamage(player.getShieldLevel().getLevel() + 1);
                    gameInfoSystem.setup(player.getPosition().x, player.getPosition().y, GameInfoSystem.GameInfoSystemPreset.SCREECH, 3.0f, false);
                    //shake.shake(1, 35, (int) (tmpAsteroid.getVelocity().len() / 5));
                } else {
                    player.getRamShield().blink(0.5f);
                    player.setPower(player.getPower() + tmpAsteroid.getSizeForHarvest());
                    tmpAsteroid.destroy();
                }
            }

            for (int j = 0; j < corrosiveAnomalyEmitter.getActiveList().size(); j++) {
                CorrosiveAnomaly tmpCorrosiveAnomaly = corrosiveAnomalyEmitter.getActiveList().get(j);
                if (!tmpAsteroid.isFragmented() && tmpCorrosiveAnomaly.getPosition().dst(tmpAsteroid.getPosition()) < 128) {
                    tmpAsteroid.destroy();
                    particleEmitter.getBuilder().buildDust(tmpAsteroid.getPosition().x, tmpAsteroid.getPosition().y, 180, 32 + 32);
                    asteroidEmitter.boom(tmpAsteroid.getPosition(), GlobalInteractionLevels.NONE);
                    gameInfoSystem.setup(tmpCorrosiveAnomaly.getPosition().x, tmpCorrosiveAnomaly.getPosition().y, GameInfoSystem.GameInfoSystemPreset.BOOM, 3.0f, false);
                    if (tmpCorrosiveAnomaly.getLevel() == 0) {
                        tmpCorrosiveAnomaly.destroy();
                        showSpray(tmpCorrosiveAnomaly);
                    } else {
                        tmpCorrosiveAnomaly.takeDamage(1);
                    }
                }
            }

            for (int j = 0; j < consumableEmitter.getActiveList().size(); j++) {
                Consumable tmpConsumable = consumableEmitter.getActiveList().get(j);
                if (!tmpAsteroid.isFragmented() && tmpConsumable.getPosition().dst(tmpAsteroid.getPosition()) < 128) {
                    tmpAsteroid.destroy();
                    tmpConsumable.destroy();
                    asteroidEmitter.boom(tmpAsteroid.getPosition(), GlobalInteractionLevels.NONE);
                    particleEmitter.getBuilder().buildDust(tmpAsteroid.getPosition().x, tmpAsteroid.getPosition().y, 180, 32 + 32);
                    showSpray(tmpConsumable);
                    gameInfoSystem.setup(
                            tmpConsumable.getPosition().x,
                            tmpConsumable.getPosition().y,
                            GameInfoSystem.GameInfoSystemPreset.SAD,
                            3.0f,
                            false
                    );
                }
            }
        }
    }

    private void damageToAnomaly(AbstractPoolable anomaly) {
        anomaly.destroy();
        player.getRamShield().blink(1.0f);
        int asc = (anomaly.getLevel() + 1) * 5000;
        player.getStanding().changeScore(asc);
        player.slowDown(anomaly.getLevel() * 50.0f);
        player.decreasePower(anomaly.getLevel());
        showSpray(anomaly);
        player.killPlusPlus();
    }

    private void damageToPlayer(AbstractPoolable anomaly) {
        if (anomaly.getLevel() >= 3) {
            if (player.getRadar().isActive()) {
                boomBox.playSound(MSConstants.LOST_RADAR);
            }
            player.getRadar().setActive(false);
        }

        //player.getLifeShield().blink();
        //shake.shake((tmpAnomaly.getLevel() + 1) / 3.0f, 35, (int) (player.getVelocity().len() / 10));
        if (player.addZigZag(0.5f * anomaly.getLevel() + 0.2f)) {
            player.takeDamage(anomaly.getLevel() + 1);
            gameInfoSystem.setup(
                    player.getPosition().x,
                    player.getPosition().y,
                    GameInfoSystem.GameInfoSystemPreset.BRAKED,
                    5.0f,
                    false
            );
            tmp.set(player.getPosition()).sub(anomaly.getPosition()).nor().scl(300 + anomaly.getLevel() * 50);
            player.getVelocity().add(tmp);
            player.resetPower();
            tmp.scl(-1);
            anomaly.getVelocity().add(tmp);
        }
    }

    private void showSpray(AbstractPoolable poolable) {
        float R = 0, G = 0.6f, B = 0;
        for (int j = 0; j < 60; j++) {
            if (poolable instanceof CorrosiveAnomaly) {
                R = ((CorrosiveAnomaly) (poolable)).getColor().r;
                G = ((CorrosiveAnomaly) (poolable)).getColor().g;
                B = ((CorrosiveAnomaly) (poolable)).getColor().b;
            }
            particleEmitter.setup(
                    poolable.getPosition().x + MathUtils.random(-30, 30),
                    poolable.getPosition().y + MathUtils.random(-30, 30),
                    MathUtils.random(-400, 400), MathUtils.random(-400, 400),
                    0.4f,
                    8,
                    1,
                    R, G, B,
                    1f,
                    R, G, B,
                    0.0f
            );
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        shaderProgram.dispose();
        player.getStanding().writeResult();
    }
}