package com.shuttle.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.shuttle.game.bot.anomaly.AbstractAnomaly;
import com.shuttle.game.bot.asteroids.AbstractAsteroid;
import com.shuttle.game.bot.asteroids.MetalAsteroidEmitter;
import com.shuttle.game.bot.asteroids.MineralAsteroidEmitter;
import com.shuttle.game.bot.blackhole.Blackhole;
import com.shuttle.game.bot.consumable.AstronautEmitter;
import com.shuttle.game.bot.consumable.Consumable;
import com.shuttle.game.bot.consumable.CrateEmitter;
import com.shuttle.game.bot.consumable.FuelEmitter;
import com.shuttle.game.bot.consumable.StorageEmitter;
import com.shuttle.game.bot.pool.AbstractPoolable;
import com.shuttle.game.bot.anomaly.CorrosiveAnomaly;
import com.shuttle.game.bot.anomaly.ElectricalAnomaly;
import com.shuttle.game.bot.anomaly.ElectricalAnomalyEmitter;
import com.shuttle.game.bot.anomaly.MagneticAnomaly;
import com.shuttle.game.bot.anomaly.MagneticAnomalyEmitter;
import com.shuttle.game.bot.anomaly.RadiationAnomaly;
import com.shuttle.game.bot.anomaly.RadiationAnomalyEmitter;
import com.shuttle.game.bot.anomaly.AnomalyTimer;
import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.VConstants;
import com.shuttle.game.player.navigation.PlayerPointer;
import com.shuttle.game.conf.GlobalInteractionLevels;
import com.shuttle.game.Joystick;
import com.shuttle.game.bot.anomaly.CorrosiveAnomalyEmitter;
import com.shuttle.game.Background;
import com.shuttle.game.bot.consumable.AbstractConsumable;
import com.shuttle.game.GameInfoSystem;
import com.shuttle.game.bot.particle.ParticleEmitter;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.screens.group.PauseGroup;
import com.shuttle.game.utils.ActiveListJoiner;
import com.shuttle.game.utils.BoundCheck;
import com.shuttle.game.utils.effects.PrettySound;
import com.shuttle.game.utils.effects.Ray;
import com.shuttle.game.utils.effects.Shake;
import com.shuttle.game.utils.effects.Vibrator;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class GameScreen extends BaseScreen {
    @Getter
    @Setter
    private Shuttle player;
    private Background background;
    @Getter
    @Setter
    private ParticleEmitter particleEmitter;
    @Getter
    @Setter
    private AstronautEmitter astronautEmitter;
    @Getter
    @Setter
    private CrateEmitter crateEmitter;
    @Getter
    @Setter
    private CorrosiveAnomalyEmitter corrosiveAnomalyEmitter;
    @Getter
    @Setter
    private ElectricalAnomalyEmitter electricalAnomalyEmitter;
    @Getter
    @Setter
    private RadiationAnomalyEmitter radiationAnomalyEmitter;
    @Getter
    @Setter
    private MagneticAnomalyEmitter magneticAnomalyEmitter;
    @Getter
    @Setter
    private MetalAsteroidEmitter metalAsteroidEmitter;
    @Getter
    @Setter
    private MineralAsteroidEmitter mineralAsteroidEmitter;
    @Getter
    @Setter
    private FuelEmitter fuelEmitter;
    @Getter
    @Setter
    private StorageEmitter storageEmitter;
    @Getter
    @Setter
    private Camera camera;
    private Vector2 tmp1V2;
    private Vector3 tmp1V3;
    @Getter
    @Setter
    private GameInfoSystem gameInfoSystem;
    @Getter
    private PauseGroup pauseGroup;
    private Joystick joystick;
    private PlayerPointer playerPointer;
    @Getter
    @Setter
    private Shake shake;
    @Getter
    @Setter
    private AnomalyTimer anomalyTimer;
    @Getter
    private Blackhole blackhole;
    private FrameBuffer fbo;
    private TextureRegion fboTexRegion;
    private OrthographicCamera cameraHelper;
    private ShaderManager shaderManager;
    private ShaderProgram shaderBlackHole;
    private ShaderProgram shaderPause;
    private GameOver gameOver;
    private Ray ray;
    private PrettySound prettyCorrosiveKick;
    private PrettySound prettyElectricKick;
    private PrettySound prettyFragmentHarvest;
    private float holeTime;
    private float fadeInAlpha;
    private boolean gameOverDeactivate;
    private List<List<? extends AbstractConsumable>> consumableList;
    private List<List<? extends AbstractAsteroid>> asteroidsList;

    public GameScreen(SpriteBatch batch, Camera camera) {
        super(batch);
        this.batch = batch;
        this.camera = camera;
        this.tmp1V2 = new Vector2(0, 0);
        this.tmp1V3 = new Vector3(0, 0, 0);
        this.cameraHelper = new OrthographicCamera(1080, 1920);
    }

    @Override
    public void show() {
        super.show();
        fadeInAlpha = 0.0f;
        gameOverDeactivate = false;
        shaderManager = new ShaderManager();
        anomalyTimer = new AnomalyTimer(this);
        boomBox.stopAllMusicAndPlayThese(MSConstants.GAME_MUSIC, MSConstants.BLACKHOLE_ROAR_MUSIC);
        fbo = new FrameBuffer(Pixmap.Format.RGB565, Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight(), false);
        fboTexRegion = new TextureRegion(fbo.getColorBufferTexture());
        fboTexRegion.flip(false, true);

        shaderPause = shaderManager.getProgram(ShaderManager.ShaderType.PAUSE);
        shaderBlackHole = shaderManager.getProgram(ShaderManager.ShaderType.BLACKHOLE);

        particleEmitter = new ParticleEmitter();
        corrosiveAnomalyEmitter = new CorrosiveAnomalyEmitter(this);
        electricalAnomalyEmitter = new ElectricalAnomalyEmitter(this);
        radiationAnomalyEmitter = new RadiationAnomalyEmitter(this);
        magneticAnomalyEmitter = new MagneticAnomalyEmitter(this);
        astronautEmitter = new AstronautEmitter(this);
        crateEmitter = new CrateEmitter(this);
        metalAsteroidEmitter = new MetalAsteroidEmitter(this);
        mineralAsteroidEmitter = new MineralAsteroidEmitter(this);
        fuelEmitter = new FuelEmitter(this);
        storageEmitter = new StorageEmitter(this);
        background = new Background();
        gameInfoSystem = new GameInfoSystem();
        joystick = new Joystick();

        blackhole = new Blackhole();
        player = new Shuttle(this, joystick);
        BoundCheck.avoidPosition(blackhole.getPosition(), player.getPosition(), (int) Rules.BLACKHOLE_TAKE_RANGE + 50, 0, Rules.SPACE_WIDTH, 0, Rules.SPACE_HEIGHT);
        playerPointer = new PlayerPointer(player);
        Rules.StartLevel.values()[GamePreferences.getLevel()].setGameLevel(player);
        gameOver = new GameOver(this);
        shake = new Shake();

        InputMultiplexer im = new InputMultiplexer(player, stage, joystick);
        Gdx.input.setInputProcessor(im);
        ray = new Ray(this);
        calcMusicEffectVolume();
        prettyCorrosiveKick = PrettySound.getPretty(Rules.ANOMALY_KICK_PRETTY_SOUND_DELAY);
        prettyElectricKick = PrettySound.getPretty(Rules.ANOMALY_KICK_PRETTY_SOUND_DELAY);
        prettyFragmentHarvest = PrettySound.getPretty(Rules.FRAGMENT_KICK_PRETTY_SOUND_DELAY);
        //
        ActiveListJoiner<AbstractConsumable> consumableJoiner = new ActiveListJoiner<AbstractConsumable>();
        ActiveListJoiner<AbstractAsteroid> asteroidsJoiner = new ActiveListJoiner<AbstractAsteroid>();
        consumableList = consumableJoiner.join(astronautEmitter.getActiveList(), crateEmitter.getActiveList(), fuelEmitter.getActiveList(), storageEmitter.getActiveList());
        asteroidsList = asteroidsJoiner.join(metalAsteroidEmitter.getActiveList(), mineralAsteroidEmitter.getActiveList());
        //
    }

    private void calcMusicEffectVolume() {
        float v;
        if (player.getPosition().dst(blackhole.getPosition()) >= Rules.BLACKHOLE_ROAR_RANGE) {
            v = 0.0f;
        } else if (player.getPosition().dst(blackhole.getPosition()) <= Rules.BLACKHOLE_TAKE_RANGE) {
            v = 0.5f;
        } else {
            v = 1 - player.getPosition().dst(blackhole.getPosition()) / Rules.BLACKHOLE_ROAR_RANGE;
        }
        boomBox.setMusicEffectVolume(MSConstants.BLACKHOLE_ROAR_MUSIC, v);
    }

    @Override
    public void createGUI() {
        pauseGroup = new PauseGroup();
        stage.addActor(pauseGroup);
    }

    @Override
    public void render(float dt) {
        //todo: refactor this
        FitViewport v = (FitViewport) ScreenManager.getInstance().getViewport();

        //FBO BEGIN
        fbo.begin();
        super.render(dt);
        update(dt);
        batch.setProjectionMatrix(camera.combined);
        if (pauseGroup.isPause() || Rules.GAME_OVER) {
            batch.setShader(shaderPause);
        } else {
            batch.setShader(null);
        }
        //BEGIN
        batch.begin();
        background.render(batch);
        if (GamePreferences.getEffectsLevel() == GamePreferences.EffectLevel.LOW.getValue() || shaderBlackHole == null) {
            blackhole.render(batch);
        }
        metalAsteroidEmitter.render(batch);
        mineralAsteroidEmitter.render(batch);
        fuelEmitter.render(batch);
        storageEmitter.render(batch);
        player.render(batch);
        astronautEmitter.render(batch);
        crateEmitter.render(batch);
        corrosiveAnomalyEmitter.render(batch);
        electricalAnomalyEmitter.render(batch);
        radiationAnomalyEmitter.render(batch);
        magneticAnomalyEmitter.render(batch);
        player.getRamShield().render(batch);
        particleEmitter.render(batch);
        batch.end();

        float shaderHoleX = (blackhole.getPosition().x - camera.position.x + Rules.WORLD_WIDTH / 2) / Rules.WORLD_WIDTH;
        float shaderHoleY = (blackhole.getPosition().y - camera.position.y + Rules.WORLD_HEIGHT / 2) / Rules.WORLD_HEIGHT;

        //
        ScreenManager.getInstance().resetCamera();
        //FBO END
        fbo.end(v.getScreenX(), v.getScreenY(), v.getScreenWidth(), v.getScreenHeight());

        //BEGIN
        batch.begin();
        super.render(dt);
        if (GamePreferences.getEffectsLevel() == GamePreferences.EffectLevel.LOW.getValue() || shaderBlackHole == null) {
            batch.setShader(null);
            //BEGIN
            batch.draw(fboTexRegion, 0, 0, Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT);
        } else {
            batch.setShader(shaderBlackHole);
            shaderBlackHole.setUniformf("resolution", Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT);
            holeTime += dt;
            shaderBlackHole.setUniformf("time", holeTime);
            shaderBlackHole.setUniformf("center", shaderHoleX, shaderHoleY);
            shaderBlackHole.setUniformf("dmax", blackhole.getShaderDiameter());

            if (pauseGroup.isPause() || Rules.GAME_OVER) {
                shaderBlackHole.setUniformf("isGrey", 1.0f);
            } else {
                shaderBlackHole.setUniformf("isGrey", -1.0f);
            }
            batch.draw(fboTexRegion, 0, 0, Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT);
        }
        //batch.end();

        if (pauseGroup.isPause()) {
            batch.setShader(shaderPause);
        } else {
            batch.setShader(null);
        }

        //BEGIN
        //batch.begin();
        if (!Rules.GAME_OVER) {
            player.getStanding().render(batch);
            anomalyTimer.render(batch);
        }

        if (!pauseGroup.isPause()) {
            joystick.render(batch);
        }

        Color tmpColor = batch.getColor();
        if (Rules.GAME_OVER) {
            boomBox.stopAllMusicAndPlayThis(MSConstants.MENU_MUSIC);
            pauseGroup.setGameOver();
            fadeInAlpha += dt / 2;
            if (fadeInAlpha >= 1.0f)
                fadeInAlpha = 1.0f;
            batch.setColor(tmpColor.r, tmpColor.g, tmpColor.b, fadeInAlpha);
            gameOver.draw(batch);
        }
        batch.end();

        batch.setProjectionMatrix(cameraHelper.combined);
        //BEGIN
        batch.begin();
        if (!Rules.GAME_OVER) {
            gameInfoSystem.render(batch);
            playerPointer.render(batch);
        }
        batch.end();

        batch.setShader(null);
        stage.draw();
    }

    public void update(float dt) {
        cameraUpdate(dt);
        if (!pauseGroup.isPause()) {
            PrettySound.update(dt);
            background.update(dt);
            player.update(dt);
            corrosiveAnomalyEmitter.update(dt);
            electricalAnomalyEmitter.update(dt);
            radiationAnomalyEmitter.update(dt);
            magneticAnomalyEmitter.update(dt);
            astronautEmitter.update(dt);
            crateEmitter.update(dt);
            metalAsteroidEmitter.update(dt);
            mineralAsteroidEmitter.update(dt);
            fuelEmitter.update(dt);
            storageEmitter.update(dt);
            particleEmitter.update(dt);
            blackhole.update(dt);
            if (!Rules.GAME_OVER) {
                anomalyTimer.update(dt);
            }

            gameInfoSystem.update(dt);
            playerPointer.update(dt);
            if (!Rules.GAME_OVER) {
                boomBox.playRandomMusicEffects(dt);
                checkCollision(dt);
            } else {
                gameOver.update(dt);
                if (!gameOverDeactivate) {
                    abstractAnomalyDeactivate(corrosiveAnomalyEmitter.getActiveList());
                    abstractAnomalyDeactivate(corrosiveAnomalyEmitter.getFreeList());
                    abstractAnomalyDeactivate(electricalAnomalyEmitter.getActiveList());
                    abstractAnomalyDeactivate(electricalAnomalyEmitter.getFreeList());
                    abstractAnomalyDeactivate(magneticAnomalyEmitter.getActiveList());
                    abstractAnomalyDeactivate(magneticAnomalyEmitter.getFreeList());
                    abstractAnomalyDeactivate(radiationAnomalyEmitter.getActiveList());
                    abstractAnomalyDeactivate(radiationAnomalyEmitter.getFreeList());
                    gameOverDeactivate = true;
                }
            }
        }
        cameraUpdate(dt);
        if (!pauseGroup.isPause() && !Rules.GAME_OVER) {
            shake.update(dt, camera);
        }
        stage.act();
    }

    //todo: It is works, somehow :)
    //todo: Позиция обнулсяеся во время fbo.end(), поэтому хак - копипаста из cameraHelper
    private void cameraUpdate(float dt) {
        if (player.isCameraSmooth()) {
            tmp1V3.set(player.getPosition(), 0.0f).sub(cameraHelper.position.x, cameraHelper.position.y, 0.0f).nor();
            cameraHelper.position.mulAdd(tmp1V3, Rules.CAMERA_SMOOTH_VELOCITY * dt);
            camera.position.set(cameraHelper.position);
            if (
                //parking
                    tmp1V2.set(player.getPosition()).sub(
                            cameraHelper.position.x, cameraHelper.position.y).len2() <
                            tmp1V3.scl(Rules.CAMERA_SMOOTH_VELOCITY * dt).len2()
                    ) {
                player.setCameraSmooth(false);
            }
        } else {
            cameraHelper.position.set(player.getPosition().x, player.getPosition().y, 0);
            camera.position.set(player.getPosition().x, player.getPosition().y, 0);
        }
        cameraUpdater(cameraHelper);
        cameraUpdater(camera);
    }

    private void checkCollision(float dt) {
        player.collideBlackhole(dt);
        calcMusicEffectVolume();

        boolean anyAtHarvestPoint = false;
        for (int i = 0; i < consumableList.size(); i++) {
            for (int j = 0; j < consumableList.get(i).size(); j++) {
                AbstractConsumable abstractConsumable = consumableList.get(i).get(j);
                if (!abstractConsumable.isTaken()) {
                    if (player.getPosition().dst(abstractConsumable.getPosition()) <= PaintConstants.SHUTTLE_WIDTH / 2) {
                        if (player.getShieldLevel().getLevel() < GlobalInteractionLevels.GREEN.getLevel()) {
                            abstractConsumable.destroy();
                            player.takeEffect(abstractConsumable);
                            player.getStanding().addToStatistics(abstractConsumable.getType(), abstractConsumable.getLevel());
                        } else {
                            if (!player.canCatch(abstractConsumable)) {
                                abstractConsumable.destroy();
                                player.badEffect(abstractConsumable);
                            } else {
                                abstractConsumable.destroy();
                                player.takeEffect(abstractConsumable);
                            }
                        }
                    }
                    ray.setCount(24);
                    ray.setSize(4);
                    ray.setHarvestVelocity(Rules.PLAYER_RAY_CONSUMABLE_HARVEST_VELOCITY);
                    ray.setTakeRange(Rules.PLAYER_CONSUMABLE_TAKE_RANGE);
                    if (player.getShieldLevel().getLevel() < 2) {
                        ray.setPreset(Ray.Preset.GREEN);
                    } else {
                        if (player.canCatch(abstractConsumable)) {
                            ray.setPreset(Ray.Preset.GREEN);
                        } else {
                            ray.setPreset(Ray.Preset.RED);
                        }
                    }
                    ray.harvest(player, abstractConsumable, particleEmitter, dt);
                    anyAtHarvestPoint |= abstractConsumable.isAtRayPoint();
                }
            }
        }

        player.showTakeRangeField(dt, anyAtHarvestPoint);

        for (int i = 0; i < corrosiveAnomalyEmitter.getActiveList().size(); i++) {
            CorrosiveAnomaly anomaly = (CorrosiveAnomaly) corrosiveAnomalyEmitter.getActiveList().get(i);
            if (!anomaly.isTaken()) {
                if (player.getPosition().dst(anomaly.getPosition()) <= PaintConstants.ANOMALY_CORROSIVE_WIDTH / 2 && anomaly.isBorn()) {
                    if (anomaly.getLevel() < player.getPushPower() || player.getVelocity().len() >= Rules.PLAYER_TURBO_THRESHOLD_VELOCITY) {
                        damageToAnomaly(anomaly);
                        boomBox.playSound(MSConstants.ANOMALY_CORROSIVE_DESTROY);
                        Vibrator.vibrate(VConstants.PLAYER_ANOMALY_CORROSIVE_DESTROY, anomaly.getLevel());
                    } else {
                        damageToPlayer(anomaly);
                        if (anomaly.getLevel() != 4) {
                            anomaly.takeDamage(player.getPushPower());
                        }
                        anomaly.setHungry(false);
                        anomaly.setSlimTimer(0);
                        prettyCorrosiveKick.playSound(MSConstants.PLAYER_KICKED_CORROSIVE);
                        Vibrator.vibrate(VConstants.PLAYER_ANOMALY_CORROSIVE_COLLISION);
                    }
                }
            }
        }

        for (int i = 0; i < electricalAnomalyEmitter.getActiveList().size(); i++) {
            ElectricalAnomaly anomaly = (ElectricalAnomaly) electricalAnomalyEmitter.getActiveList().get(i);
            if (!anomaly.isTaken()) {
                if (player.getPosition().dst(anomaly.getPosition()) < PaintConstants.ANOMALY_ELECTRIC_WIDTH / 2 && anomaly.isBorn()) {
                    anomaly.setHungry(false);
                    damageToPlayer(anomaly);
                    Vibrator.vibrate(VConstants.PLAYER_ANOMALY_CORROSIVE_DESTROY, anomaly.getLevel());
                    prettyElectricKick.playSound(MSConstants.PLAYER_KICKED_ELECTRIC);
                    anomaly.setV(anomaly.getV() * Rules.ANOMALY_ELECTRIC_DECREASE_FACTOR);
                }
            }
        }

        //
        boolean tmp1 = false;
        for (int i = 0; i < radiationAnomalyEmitter.getActiveList().size(); i++) {
            RadiationAnomaly anomaly = (RadiationAnomaly) radiationAnomalyEmitter.getActiveList().get(i);
            if (!anomaly.isTaken()) {
                if (player.getPosition().dst(anomaly.getPosition()) < PaintConstants.ANOMALY_RADIATION_WIDTH / 3 && anomaly.isBorn()) {
                    anomaly.setEat(true);
                    anomaly.setGlued(true);
                } else {
                    anomaly.setEat(false);
                    anomaly.setEatTimer(0.0f);
                    anomaly.setGlued(false);
                }

                if (anomaly.isEat()) {
                    anomaly.addEatTimer(dt);
                    if (anomaly.getEatTimer() >= Rules.ANOMALIES_RADIATION_EAT_TIME) {
                        anomaly.setEatTimer(0.0f);
                        player.getStanding().changeScore(-Rules.ANOMALIES_RADIATION_SCORE_DECREASE);
                        boomBox.playSound(MSConstants.PLAYER_LOSE_SCORE);
                        gameInfoSystem.setup(
                                player.getPosition().x,
                                player.getPosition().y,
                                GameInfoSystem.GameInfoSystemPreset.LOST_SCORE,
                                2.0f,
                                true
                        );
                    }
                }
                tmp1 |= anomaly.isGlued();
            }
        }
        player.getStanding().setScoreAlertRadiationAnomaly(tmp1);

        boolean tmp2 = false;
        for (int i = 0; i < magneticAnomalyEmitter.getActiveList().size(); i++) {
            MagneticAnomaly anomaly = (MagneticAnomaly) magneticAnomalyEmitter.getActiveList().get(i);
            if (!anomaly.isTaken()) {
                if (player.getPosition().dst(anomaly.getPosition()) < PaintConstants.ANOMALY_MAGNETIC_HEIGHT / 3 && anomaly.isBorn()) {
                    anomaly.setEat(true);
                    anomaly.setGlued(true);
                } else {
                    anomaly.setEat(false);
                    anomaly.setEatTimer(0.0f);
                    anomaly.setGlued(false);
                }

                if (anomaly.isEat()) {
                    anomaly.addEatTimer(dt);
                    if (anomaly.getEatTimer() >= Rules.ANOMALIES_MAGNETIC_EAT_TIME) {
                        anomaly.setEatTimer(0.0f);
                        player.getStanding().changeFuel(-Rules.ANOMALIES_MAGNETIC_FUEL_DECREASE);
                        boomBox.playSound(MSConstants.PLAYER_LOSE_FUEL);
                        gameInfoSystem.setup(
                                player.getPosition().x,
                                player.getPosition().y,
                                GameInfoSystem.GameInfoSystemPreset.LOST_FUEL,
                                4.0f,
                                true
                        );
                    }
                }
                tmp2 |= anomaly.isGlued();
            }
        }
        player.getStanding().setFuelAlertMagneticAnomaly(tmp2);
        //

        for (int i = 0; i < asteroidsList.size(); i++) {
            for (int j = 0; j < asteroidsList.get(i).size(); j++) {
                AbstractAsteroid asteroid = asteroidsList.get(i).get(j);
                if (!asteroid.isTaken()) {
                    if (player.getPosition().dst(asteroid.getPosition()) < asteroid.getHeight() * asteroid.getScaleX()) {
                        if (!asteroid.isFragmented()) {
                            asteroid.destroy();
                            Vibrator.vibrate(VConstants.PLAYER_ASTEROID_DESTROY);
                            if (GamePreferences.getEffectsLevel() == GamePreferences.EffectLevel.HIGH.getValue()) {
                                Gdx.input.vibrate(VConstants.PLAYER_ASTEROID_DESTROY);
                            }
                            player.getLifeShield().blink(1.0f);
                            particleEmitter.getBuilder().buildDust(asteroid.getPosition().x, asteroid.getPosition().y, 180, player.getShieldLevel().getLevel() * 16 + 32);
                            switch (asteroid.getType()) {
                                case ASTEROID_METAL:
                                    metalAsteroidEmitter.boom(asteroid.getPosition(), player.getShieldLevel());
                                    break;
                                case ASTEROID_MINERAL:
                                    mineralAsteroidEmitter.boom(asteroid.getPosition(), player.getShieldLevel());
                                    break;
                            }
                            player.takeDamage(player.getShieldLevel().getLevel() + 1);
                            gameInfoSystem.setup(
                                    player.getPosition().x, player.getPosition().y,
                                    GameInfoSystem.GameInfoSystemPreset.SCREECH,
                                    3.0f,
                                    true
                            );
                            boomBox.playSound(MSConstants.ASTEROID_DESTROY);
                            player.getStanding().addToStatistics(asteroid.getType(), asteroid.getLevel());
                        } else {
                            player.getRamShield().blink(0.5f);
                            player.setPower(player.getPower() + asteroid.getSizeForHarvest());
                            //pretty sound if collect much fragments in little time
                            prettyFragmentHarvest.playSound(MSConstants.GET_FRAGMENT);
                            asteroid.destroy();
                        }
                    }

                    for (int k = 0; k < corrosiveAnomalyEmitter.getActiveList().size(); k++) {
                        CorrosiveAnomaly anomaly = (CorrosiveAnomaly) corrosiveAnomalyEmitter.getActiveList().get(k);
                        if (!asteroid.isFragmented() && anomaly.getPosition().dst(asteroid.getPosition()) < PaintConstants.ANOMALY_CORROSIVE_WIDTH / 2) {
                            asteroid.destroy();
                            particleEmitter.getBuilder().buildDust(asteroid.getPosition().x, asteroid.getPosition().y, 180, 32 + 32);
                            switch (asteroid.getType()) {
                                case ASTEROID_METAL:
                                    metalAsteroidEmitter.boom(asteroid.getPosition(), GlobalInteractionLevels.NONE);
                                    break;
                                case ASTEROID_MINERAL:
                                    mineralAsteroidEmitter.boom(asteroid.getPosition(), GlobalInteractionLevels.NONE);
                                    break;
                            }
                            gameInfoSystem.setup(anomaly.getPosition().x, anomaly.getPosition().y, GameInfoSystem.GameInfoSystemPreset.BOOM, 3.0f, false);
                            if (anomaly.getLevel() == 0) {
                                anomaly.destroy();
                                anomaly.showSprayCollision(particleEmitter);
                            } else {
                                anomaly.takeDamage(1);
                            }
                        }
                    }

                    for (int m = 0; m < consumableList.size(); m++) {
                        for (int p = 0; p < consumableList.get(m).size(); p++) {
                            AbstractConsumable abstractConsumable = consumableList.get(m).get(p);
                            if (!asteroid.isFragmented() && abstractConsumable.getPosition().dst(asteroid.getPosition()) < 128) {
                                asteroid.destroy();
                                abstractConsumable.destroy();
                                //todo: repeatable code :(
                                switch (asteroid.getType()) {
                                    case ASTEROID_METAL:
                                        metalAsteroidEmitter.boom(asteroid.getPosition(), GlobalInteractionLevels.NONE);
                                        break;
                                    case ASTEROID_MINERAL:
                                        mineralAsteroidEmitter.boom(asteroid.getPosition(), GlobalInteractionLevels.NONE);
                                        break;
                                }
                                particleEmitter.getBuilder().buildDust(asteroid.getPosition().x, asteroid.getPosition().y, 180, 32 + 32);
                                abstractConsumable.showSprayCollision(particleEmitter);
                                gameInfoSystem.setup(
                                        abstractConsumable.getPosition().x,
                                        abstractConsumable.getPosition().y,
                                        GameInfoSystem.GameInfoSystemPreset.SAD,
                                        3.0f,
                                        true
                                );
                            }
                        }
                    }

                    //
                    if (asteroid.isFragmented()) {
                        ray.setCount(12);
                        ray.setSize(2);
                        ray.setPreset(Ray.Preset.GREY);
                        ray.setHarvestVelocity(Rules.PLAYER_RAY_FRAGMENT_HARVEST_VELOCITY);
                        ray.setTakeRange(Rules.PLAYER_FRAGMENT_TAKE_RANGE);
                        ray.harvest(player, asteroid, particleEmitter, dt);
                    } else {
                        ray.setCount(5);
                        ray.setSize(3);
                        ray.setPreset(Ray.Preset.RED);
                        ray.laser(player, asteroid, particleEmitter, dt);
                    }
                    //
                }
            }
        }
    }

    private void abstractAnomalyDeactivate(List<AbstractAnomaly> anomalies) {
        for (int i = 0; i < anomalies.size(); i++) {
            if (anomalies.get(i).isActivated())
                anomalies.get(i).setActivated(false);
        }
    }

    private void cameraUpdater(Camera camera) {
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

    private void damageToAnomaly(AbstractPoolable abstractPoolable) {
        abstractPoolable.destroy();
        player.getRamShield().blink(1.0f);
        player.slowDown(abstractPoolable.getLevel() * 50.0f);
        if (player.getVelocity().len() <= Rules.PLAYER_ENGINE_POWER) {
            player.changePower(-abstractPoolable.getLevel());
        }
        abstractPoolable.showSprayCollision(particleEmitter);
        player.killPlusPlus(abstractPoolable);
        player.getStanding().addToStatistics(abstractPoolable.getType(), abstractPoolable.getLevel());
    }

    private void damageToPlayer(AbstractPoolable anomaly) {
        if (anomaly.getLevel() >= 3) {
            if (player.getRadar().isActive()) {
                boomBox.playSound(MSConstants.LOST_RADAR);
                player.getRadar().setActive(false);
                player.getRadar().generatePosition();
            }
        }

        if (player.addZigZag(0.5f * anomaly.getLevel() + 0.2f)) {
            player.takeDamage(anomaly.getLevel() + 1);
            gameInfoSystem.setup(
                    player.getPosition().x,
                    player.getPosition().y,
                    GameInfoSystem.GameInfoSystemPreset.BRAKED,
                    5.0f,
                    false
            );
            tmp1V2.set(player.getPosition()).sub(anomaly.getPosition()).nor().scl(300 + anomaly.getLevel() * 50);
            player.getVelocity().add(tmp1V2);
            player.resetPower();
            tmp1V2.scl(-1);
            anomaly.getVelocity().add(tmp1V2);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        shaderManager.dispose();
        player.getStanding().writeResult();
    }
}