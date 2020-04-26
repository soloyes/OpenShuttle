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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.shuttle.game.background.Background;
import com.shuttle.game.bot.anomaly.AbstractAnomaly;
import com.shuttle.game.bot.anomaly.CorrosiveAnomaly;
import com.shuttle.game.bot.anomaly.CorrosiveAnomalyEmitter;
import com.shuttle.game.bot.anomaly.ElectricalAnomaly;
import com.shuttle.game.bot.anomaly.ElectricalAnomalyEmitter;
import com.shuttle.game.bot.anomaly.MagneticAnomaly;
import com.shuttle.game.bot.anomaly.MagneticAnomalyEmitter;
import com.shuttle.game.bot.anomaly.RadiationAnomaly;
import com.shuttle.game.bot.anomaly.RadiationAnomalyEmitter;
import com.shuttle.game.bot.asteroids.AbstractAsteroid;
import com.shuttle.game.bot.asteroids.MetalAsteroidEmitter;
import com.shuttle.game.bot.asteroids.MineralAsteroidEmitter;
import com.shuttle.game.bot.blackhole.Blackhole;
import com.shuttle.game.bot.consumable.AbstractConsumable;
import com.shuttle.game.bot.consumable.Astronaut;
import com.shuttle.game.bot.consumable.AstronautEmitter;
import com.shuttle.game.bot.consumable.Crate;
import com.shuttle.game.bot.consumable.CrateEmitter;
import com.shuttle.game.bot.consumable.FuelEmitter;
import com.shuttle.game.bot.consumable.Storage;
import com.shuttle.game.bot.consumable.StorageEmitter;
import com.shuttle.game.bot.consumable.fuel.Fuel;
import com.shuttle.game.bot.rocket.RocketEmitter;
import com.shuttle.game.bot.rocket.RocketLauncher;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.PlayerInteractionLevels;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.conf.VConstants;
import com.shuttle.game.effects.PrettySound;
import com.shuttle.game.effects.Ray;
import com.shuttle.game.effects.Shake;
import com.shuttle.game.effects.Vibrator;
import com.shuttle.game.effects.particle.ParticleEmitter;
import com.shuttle.game.labelsticker.Label;
import com.shuttle.game.labelsticker.Sticker;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.player.Standing;
import com.shuttle.game.player.navigation.Radar;
import com.shuttle.game.ui.GameInfoSystem;
import com.shuttle.game.ui.Joystick;
import com.shuttle.game.ui.PlayerPointer;
import com.shuttle.game.ui.groups.PauseGroup;
import com.shuttle.game.ui.groups.configs.ConfigImageButton;
import com.shuttle.game.ui.groups.configs.LeftGroup;
import com.shuttle.game.ui.groups.configs.RightGroup;
import com.shuttle.game.ui.groups.mvc.profiles.Profiles;
import com.shuttle.game.ui.groups.mvc.results.DetailedResultGroup;
import com.shuttle.game.ui.groups.selector.SelectorGroup;
import com.shuttle.game.ui.groups.switches.SwitchGroup;
import com.shuttle.game.ui.panels.UILeftPanel;
import com.shuttle.game.ui.panels.UIRightPanel;
import com.shuttle.game.ui.panels.UITopPanel;
import com.shuttle.game.ui.panels.info.AnomalyTimer;
import com.shuttle.game.utils.BoundCheck;
import com.shuttle.game.utils.ListOfListsJoiner;
import com.shuttle.game.utils.ShaderManager;
import com.shuttle.game.utils.pool.AbstractPoolable;
import com.shuttle.game.utils.pool.ObjectPool;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 03/06/18.
 */

public class GameScreen extends BaseScreen {
    @Getter
    @Setter
    private Shuttle player;
    private Background background;
    @Getter
    @Setter
    private Camera camera;
    private Vector2 tmp1V2;
    private Vector3 tmp1V3;
    @Getter
    @Setter
    private GameInfoSystem gameInfoSystem;
    private Joystick joystick;
    private PlayerPointer playerPointer;
    @Getter
    @Setter
    private AnomalyTimer anomalyTimer;
    @Getter
    private Blackhole blackhole;
    private FrameBuffer fbo;
    private TextureRegion fboTexRegion;
    private OrthographicCamera cameraHelper;
    private DetailedResultGroup detailedResultGroup;
    private PauseGroup pauseGroup;
    private Ray ray;
    private PrettySound prettyCorrosiveKick;
    private PrettySound prettyElectricKick;
    private PrettySound prettyFragmentHarvest;
    private float holeTime;
    private float fadeInAlpha;
    private boolean gameOverDeactivate;
    private ListOfListsJoiner<AbstractConsumable> consumableJoiner;
    private ListOfListsJoiner<AbstractAsteroid> asteroidsJoiner;
    private ListOfListsJoiner<AbstractAnomaly> anomalyJoiner;
    @Getter
    private UITopPanel uiTopPanel;
    private Standing standing;
    private Profiles profiles;

    private MineralAsteroidEmitter mineralAsteroidEmitter;
    private MetalAsteroidEmitter metalAsteroidEmitter;
    private CorrosiveAnomalyEmitter corrosiveAnomalyEmitter;
    private ElectricalAnomalyEmitter electricalAnomalyEmitter;
    private MagneticAnomalyEmitter magneticAnomalyEmitter;
    private RadiationAnomalyEmitter radiationAnomalyEmitter;
    private AstronautEmitter astronautEmitter;
    private CrateEmitter crateEmitter;
    private StorageEmitter storageEmitter;
    private FuelEmitter fuelEmitter;
    private RocketEmitter rocketEmitter;
    private ParticleEmitter particleEmitter;
    private RocketLauncher rocketLauncher;

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

        particleEmitter = new ParticleEmitter();
        mineralAsteroidEmitter = new MineralAsteroidEmitter(particleEmitter);
        metalAsteroidEmitter = new MetalAsteroidEmitter(particleEmitter);
        corrosiveAnomalyEmitter = new CorrosiveAnomalyEmitter(particleEmitter);
        electricalAnomalyEmitter = new ElectricalAnomalyEmitter(particleEmitter);
        magneticAnomalyEmitter = new MagneticAnomalyEmitter(particleEmitter);
        radiationAnomalyEmitter = new RadiationAnomalyEmitter(particleEmitter);
        astronautEmitter = new AstronautEmitter(particleEmitter);
        crateEmitter = new CrateEmitter(particleEmitter);
        storageEmitter = new StorageEmitter(particleEmitter);
        fuelEmitter = new FuelEmitter(particleEmitter);
        rocketEmitter = new RocketEmitter();

        anomalyTimer = new AnomalyTimer();
        boomBox.stopAllMusicAndPlayThese(MSConstants.GAME_SONG, MSConstants.BLACKHOLE_ROAR_MUSIC);
        fbo = new FrameBuffer(Pixmap.Format.RGB565, Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight(), false);
        fboTexRegion = new TextureRegion(fbo.getColorBufferTexture());
        fboTexRegion.flip(false, true);

        ShaderManager.PAUSE.load();
        ShaderManager.BLACKHOLE.load();

        background = new Background();
        gameInfoSystem = new GameInfoSystem();
        joystick = new Joystick();

        blackhole = new Blackhole();
        player = new Shuttle(joystick, particleEmitter);
        standing = Standing.getInstance();
        uiTopPanel = new UITopPanel();

        BoundCheck.avoidPosition(Blackhole.position, Shuttle.position, (int) Rules.BLACKHOLE_TAKE_RANGE + 50, 0, Rules.SPACE_WIDTH, 0, Rules.SPACE_HEIGHT);
        playerPointer = new PlayerPointer(player);
        Rules.StartLevel.values()[Profiles.getSettings().getLevel()].setGameLevel();

        InputMultiplexer im = new InputMultiplexer(player, stage, joystick);
        Gdx.input.setInputProcessor(im);

        calcMusicEffectVolume();
        prettyCorrosiveKick = PrettySound.getPretty(Rules.ANOMALY_KICK_PRETTY_SOUND_DELAY);
        prettyElectricKick = PrettySound.getPretty(Rules.ANOMALY_KICK_PRETTY_SOUND_DELAY);
        prettyFragmentHarvest = PrettySound.getPretty(Rules.FRAGMENT_KICK_PRETTY_SOUND_DELAY);
        ray = new Ray(particleEmitter, mineralAsteroidEmitter.getActiveList(), metalAsteroidEmitter.getActiveList());
        //todo: а тут что?
        consumableJoiner = ListOfListsJoiner.newAbstractConsumableList(astronautEmitter.getActiveList(), crateEmitter.getActiveList(), storageEmitter.getActiveList(), fuelEmitter.getActiveList());
        asteroidsJoiner = ListOfListsJoiner.newAbstractAsteroidList(mineralAsteroidEmitter.getActiveList(), metalAsteroidEmitter.getActiveList());
        anomalyJoiner = ListOfListsJoiner.newAbstractAnomalyList(corrosiveAnomalyEmitter.getActiveList(), electricalAnomalyEmitter.getActiveList(), radiationAnomalyEmitter.getActiveList(), magneticAnomalyEmitter.getActiveList());
        //
        rocketLauncher = new RocketLauncher(rocketEmitter, anomalyJoiner);
        storageEmitter.emit();
        this.profiles = new Profiles();
    }

    private void calcMusicEffectVolume() {
        float v;
        if (Shuttle.position.dst(Blackhole.position) >= Rules.BLACKHOLE_ROAR_RANGE) {
            v = 0.0f;
        } else if (Shuttle.position.dst(Blackhole.position) <= Rules.BLACKHOLE_TAKE_RANGE) {
            v = 0.5f;
        } else {
            v = 1 - Shuttle.position.dst(Blackhole.position) / Rules.BLACKHOLE_ROAR_RANGE;
        }
        boomBox.setMusicEffectVolume(MSConstants.BLACKHOLE_ROAR_MUSIC, v);
    }

    @Override
    public void createGUI() {
        // Workaround (Android restarts via app manager)
        RightGroup.doStart();
        LeftGroup.doStart();
        SelectorGroup.doStart();
        SwitchGroup.doStart();
        UILeftPanel.doStart();
        UIRightPanel.doStart();
        //
        detailedResultGroup = DetailedResultGroup.getInstance();
        pauseGroup = new PauseGroup();
        stage.addActor(detailedResultGroup);
        stage.addActor(pauseGroup);
        stage.addActor(SelectorGroup.getInstance());
        stage.addActor(SwitchGroup.getInstance());
    }

    @Override
    public void render(float dt) {
        FitViewport v = (FitViewport) ScreenManager.getInstance().getViewport();
        //FBO BEGIN
        fbo.begin();
        super.render(dt);
        update(dt);
        batch.setProjectionMatrix(camera.combined);
        if (PauseGroup.isPause() || Rules.GAME_OVER) {
            batch.setShader(ShaderManager.PAUSE.getProgram());
        } else {
            batch.setShader(null);
        }
        //BEGIN
        batch.begin();
        background.render(batch);
        if (!Profiles.getSettings().isShader() || ShaderManager.BLACKHOLE.getProgram() == null) {
            blackhole.render(batch);
        }
        mineralAsteroidEmitter.render(batch);
        metalAsteroidEmitter.render(batch);
        fuelEmitter.render(batch);
        storageEmitter.render(batch);
        astronautEmitter.render(batch);
        crateEmitter.render(batch);
        corrosiveAnomalyEmitter.render(batch);
        electricalAnomalyEmitter.render(batch);
        particleEmitter.render(batch);
        player.render(batch);
        player.getRamShield().render(batch);
        radiationAnomalyEmitter.render(batch);
        magneticAnomalyEmitter.render(batch);
        rocketLauncher.render(batch);
        batch.end();

        float shaderHoleX = (Blackhole.position.x - camera.position.x + Rules.WORLD_WIDTH / 2) / Rules.WORLD_WIDTH;
        float shaderHoleY = (Blackhole.position.y - camera.position.y + Rules.WORLD_HEIGHT / 2) / Rules.WORLD_HEIGHT;

        //
        ScreenManager.getInstance().resetCamera();
        //FBO END
        fbo.end(v.getScreenX(), v.getScreenY(), v.getScreenWidth(), v.getScreenHeight());

        //BEGIN
        batch.begin();
        super.render(dt);
        if (!Profiles.getSettings().isShader() || ShaderManager.BLACKHOLE.getProgram() == null) {
            batch.setShader(null);
            //BEGIN
            batch.draw(fboTexRegion, 0, 0, Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT);
        } else {
            batch.setShader(ShaderManager.BLACKHOLE.getProgram());
            ShaderManager.BLACKHOLE.getProgram().setUniformf("resolution", Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT);
            if (!PauseGroup.isPause()) {
                holeTime += dt;
            }
            ShaderManager.BLACKHOLE.getProgram().setUniformf("time", holeTime);
            ShaderManager.BLACKHOLE.getProgram().setUniformf("center", shaderHoleX, shaderHoleY);
            ShaderManager.BLACKHOLE.getProgram().setUniformf("dmax", blackhole.getShaderDiameter());

            if (PauseGroup.isPause() || Rules.GAME_OVER) {
                ShaderManager.BLACKHOLE.getProgram().setUniformf("isGrey", 1.0f);
            } else {
                ShaderManager.BLACKHOLE.getProgram().setUniformf("isGrey", -1.0f);
            }
            batch.draw(fboTexRegion, 0, 0, Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT);
        }
        //batch.end();

        if (PauseGroup.isPause()) {
            batch.setShader(ShaderManager.PAUSE.getProgram());
            ShaderManager.PAUSE.getProgram().setUniformf("resolution", Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT);
        } else {
            batch.setShader(null);
        }

        //BEGIN
        if (!PauseGroup.isPause() && !Rules.GAME_OVER) {
            UILeftPanel.getInstance().render(batch);
            UIRightPanel.getInstance().render(batch);
        }

        if (!Rules.GAME_OVER) {
            uiTopPanel.render(batch);
            SelectorGroup.getInstance().render(batch);
        }

        if (!PauseGroup.isPause()) {
            joystick.render(batch);
        }

        Color tmpColor = batch.getColor();
        if (Rules.GAME_OVER) {
            boomBox.stopAllMusicAndPlayThis(MSConstants.GAME_OVER);
            pauseGroup.setGameOver();
            fadeInAlpha += dt / 2;
            if (fadeInAlpha >= 1.0f)
                fadeInAlpha = 1.0f;
            batch.setColor(tmpColor.r, tmpColor.g, tmpColor.b, fadeInAlpha);
            if (!detailedResultGroup.isVisible()) {
                detailedResultGroup.setOver();
                profiles.dispose();
                detailedResultGroup.init();
            }
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
        if (!PauseGroup.isPause()) {
            PrettySound.update(dt);
            background.update(dt);
            player.update(dt);
            corrosiveAnomalyEmitter.update(dt);
            electricalAnomalyEmitter.update(dt);
            radiationAnomalyEmitter.update(dt);
            magneticAnomalyEmitter.update(dt);
            astronautEmitter.update(dt);
            crateEmitter.update(dt);
            mineralAsteroidEmitter.update(dt);
            metalAsteroidEmitter.update(dt);
            fuelEmitter.update(dt);
            storageEmitter.update(dt);
            particleEmitter.update(dt);
            blackhole.update(dt);
            UILeftPanel.getInstance().update(dt);
            uiTopPanel.update(dt);
            rocketLauncher.update(dt);
            if (!Rules.GAME_OVER) {
                anomalyTimer.update(dt);
                Standing.getInstance().addTime(dt);
            }

            gameInfoSystem.update(dt);
            playerPointer.update(dt);
            if (!Rules.GAME_OVER) {
                boomBox.playRandomMusicEffects(dt);
                checkCollision(dt);
            } else {
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
        if (!PauseGroup.isPause() && !Rules.GAME_OVER) {
            Shake.getInstance().update(dt, camera);
            standing.update(dt);
            SelectorGroup.getInstance().update(dt);
            UIRightPanel.getInstance().update(dt);
        }
        stage.act();
    }

    //It is works, somehow.
    //Cam position is reset when fbo.end(), copy-paste from cameraHelper is the hack.
    private void cameraUpdate(float dt) {
        if (player.isCameraSmooth()) {
            tmp1V3.set(Shuttle.position, 0.0f).sub(cameraHelper.position.x, cameraHelper.position.y, 0.0f).nor();
            cameraHelper.position.mulAdd(tmp1V3, Rules.CAMERA_SMOOTH_VELOCITY * dt);
            camera.position.set(cameraHelper.position);
            if (
                //parking
                    tmp1V2.set(Shuttle.position).sub(
                            cameraHelper.position.x, cameraHelper.position.y).len2() <
                            tmp1V3.scl(Rules.CAMERA_SMOOTH_VELOCITY * dt).len2()
                    ) {
                player.setCameraSmooth(false);
            }
        } else {
            cameraHelper.position.set(Shuttle.position.x, Shuttle.position.y, 0);
            camera.position.set(Shuttle.position.x, Shuttle.position.y, 0);
        }
        cameraUpdater(cameraHelper);
        cameraUpdater(camera);
    }

    private void checkCollision(float dt) {
        player.collideBlackhole(dt);
        calcMusicEffectVolume();

        boolean anyAtHarvestPoint = false;
        for (int i = 0; i < consumableJoiner.size(); i++) {
            AbstractConsumable consumable = consumableJoiner.get(i);
            if (consumable.isTaken()) {
                continue;
            }
            if (Shuttle.position.dst(consumable.getPosition()) <= PaintConstants.SHUTTLE_WIDTH / 2) {
                if (player.getShieldLevel().getLevel() < PlayerInteractionLevels.GREEN.getLevel() || consumable.isShielded()) {
                    consumeConsumable(consumable);
                } else {
                    if (player.canCatch(consumable)) {
                        consumeConsumable(consumable);
                    } else {
                        consumable.destroyWithEffect();
                        int c = 1;
                        int a = 0;
                        if (consumable.isGolden()) {
                            c = Rules.PLAYER_DAMAGE_DONE_GOLDEN_FACTOR;
                        }
                        if (consumable instanceof Crate) {
                            a = Rules.PLAYER_DAMAGE_DONE_CRATE;
                        }
                        if (consumable instanceof Fuel) {
                            Shake.getInstance().shakeCamera(1, 35, 50);
                            a = Rules.PLAYER_DAMAGE_DONE_FUEL;
                        }
                        if (consumable instanceof Storage) {
                            a = Rules.PLAYER_DAMAGE_DONE_STORAGE;
                        }
                        if (consumable instanceof Astronaut) {
                            a = Rules.PLAYER_DAMAGE_DONE_ASTRONAUT;
                        }
                        Standing.getInstance().changeDmgDone(c * a);
                        player.badEffect(consumable);
                    }
                }
            }
            ray.setCount(24);
            ray.setSize(4);
            ray.setHarvestVelocity(Rules.PLAYER_RAY_CONSUMABLE_HARVEST_VELOCITY);
            ray.setTakeRange(Rules.PLAYER_CONSUMABLE_TAKE_RANGE);
            if (player.getShieldLevel().getLevel() < PlayerInteractionLevels.GREEN.getLevel() || consumable.isShielded()) {
                ray.setPreset(Ray.Preset.GREEN);
            } else {
                if (player.canCatch(consumable)) {
                    ray.setPreset(Ray.Preset.GREEN);
                } else {
                    ray.setPreset(Ray.Preset.RED);
                }
            }
            ray.harvest(player, consumable, dt);
            anyAtHarvestPoint |= consumable.isAtRayPoint();
        }

        player.showTakeRangeField(dt, anyAtHarvestPoint);

        for (int i = 0; i < corrosiveAnomalyEmitter.getActiveList().size(); i++) {
            CorrosiveAnomaly anomaly = (CorrosiveAnomaly) corrosiveAnomalyEmitter.getActiveList().get(i);
            collideWithRocket(anomaly);
            if (anomaly.isTaken()) {
                continue;
            }
            if (Shuttle.position.dst(anomaly.getPosition()) <= PaintConstants.ANOMALY_CORROSIVE_WIDTH / 2 && anomaly.isBorn()) {
                if (anomaly.getLevel() < player.getPushPower() || Shuttle.velocity.len() >= Rules.PLAYER_TURBO_THRESHOLD_VELOCITY) {
                    if (Shuttle.velocity.len() >= Rules.PLAYER_TURBO_THRESHOLD_VELOCITY) {
                        boomBox.playSound(MSConstants.BURST_CORROSIVE_ANOMALY);
                    } else {
                        boomBox.playSound(MSConstants.DESTROY_CORROSIVE_ANOMALY);
                    }
                    damageToAnomaly(anomaly);
                    //todo: duration must be positive. Here may be 0
                    Vibrator.vibrate(VConstants.PLAYER_ANOMALY_CORROSIVE_DESTROY, anomaly.getLevel());
                } else {
                    damageToPlayer(anomaly);
                    if (anomaly.getLevel() != 4) {
                        anomaly.takeDamage(player.getPushPower());
                    }
                    anomaly.setHungry(false);
                    anomaly.setSlimTimer(0);
                    prettyCorrosiveKick.playSound(MSConstants.KICKED_BY_CORROSIVE);
                    Vibrator.vibrate(VConstants.PLAYER_ANOMALY_CORROSIVE_COLLISION);
                }
            }
        }

        for (int i = 0; i < electricalAnomalyEmitter.getActiveList().size(); i++) {
            ElectricalAnomaly anomaly = (ElectricalAnomaly) electricalAnomalyEmitter.getActiveList().get(i);
            if (anomaly.isTaken()) {
                continue;
            }
            collideWithRocket(anomaly);
            if (Shuttle.position.dst(anomaly.getPosition()) < PaintConstants.ANOMALY_ELECTRIC_WIDTH / 2 && anomaly.isBorn()) {
                anomaly.setHungry(false);
                damageToPlayer(anomaly);
                Vibrator.vibrate(VConstants.PLAYER_ANOMALY_CORROSIVE_DESTROY, anomaly.getLevel());
                prettyElectricKick.playSound(MSConstants.KICKED_BY_ELECTRIC);
                anomaly.setV(anomaly.getV() * Rules.ANOMALY_ELECTRIC_DECREASE_FACTOR);
            }
        }

        //
        boolean tmp1 = false;
        for (int i = 0; i < radiationAnomalyEmitter.getActiveList().size(); i++) {
            RadiationAnomaly anomaly = (RadiationAnomaly) radiationAnomalyEmitter.getActiveList().get(i);
            if (anomaly.isTaken()) {
                continue;
            }
            collideWithRocket(anomaly);
            if (Shuttle.position.dst(anomaly.getPosition()) < PaintConstants.ANOMALY_RADIATION_WIDTH / 3 && anomaly.isBorn()) {
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
                    Standing.getInstance().changeScore(-Rules.ANOMALIES_RADIATION_SCORE_DECREASE);
                    boomBox.playSound(MSConstants.LOSE_SCORE);
                    GameInfoSystem.setup(
                            Shuttle.position.x,
                            Shuttle.position.y,
                            "-[RED]" + GameInfoSystem.Preset.LOST_SCORE.getValue() + "[]",
                            2.0f,
                            true
                    );
                }
            }
            tmp1 |= anomaly.isGlued();
        }
        Standing.getInstance().setScoreAlertRadiationAnomaly(tmp1);

        boolean tmp2 = false;
        for (int i = 0; i < magneticAnomalyEmitter.getActiveList().size(); i++) {
            MagneticAnomaly anomaly = (MagneticAnomaly) magneticAnomalyEmitter.getActiveList().get(i);
            if (anomaly.isTaken()) {
                continue;
            }
            collideWithRocket(anomaly);
            if (Shuttle.position.dst(anomaly.getPosition()) < PaintConstants.ANOMALY_MAGNETIC_HEIGHT / 3 && anomaly.isBorn()) {
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
                    Standing.getInstance().changeFuel(-Rules.ANOMALIES_MAGNETIC_FUEL_DECREASE);
                    boomBox.playSound(MSConstants.LOSE_FUEL);
                    GameInfoSystem.setup(
                            Shuttle.position.x,
                            Shuttle.position.y,
                            "-[RED]" + GameInfoSystem.Preset.FUEL + "[]",
                            4.0f,
                            true
                    );
                }
            }
            tmp2 |= anomaly.isGlued();
        }
        Standing.getInstance().setFuelAlertMagneticAnomaly(tmp2);
        //

        for (int i = 0; i < asteroidsJoiner.size(); i++) {
            AbstractAsteroid asteroid = asteroidsJoiner.get(i);
            if (asteroid.isTaken()) {
                continue;
            }
            if (Shuttle.position.dst(asteroid.getPosition()) < asteroid.getHeight() * asteroid.getScaleX()) {
                if (!asteroid.isFragmented()) {
                    asteroid.destroyWithEffect();
                    Vibrator.vibrate(VConstants.PLAYER_ASTEROID_DESTROY);
                    player.getLifeShield().blink(1.0f);
                    ObjectPool.EMITTERS.get(asteroid.getClass())
                            .boom(asteroid.getPosition(), PlayerInteractionLevels.NONE);
                    Standing.getInstance().takeDamage(player.getShieldLevel().getLevel() + 1);
                    GameInfoSystem.setup(
                            Shuttle.position.x, Shuttle.position.y,
                            GameInfoSystem.Preset.SCREECH,
                            3.0f,
                            true
                    );
                    boomBox.playSound(MSConstants.DESTROY_ASTEROID);
                } else {
                    player.getRamShield().blink(0.5f);
                    Standing.getInstance().changeRamPower(asteroid.getSizeForHarvest());
                    if (asteroid.getType() == AbstractPoolable.Type.ASTEROID_MINERAL && Shuttle.isLaser) {
                        Standing.getInstance().changeLaserChargeLevel(asteroid.getSizeForHarvest());
                    }
                    //pretty soundVolume if collect much fragments in little time
                    prettyFragmentHarvest.playSound(MSConstants.GET_FRAGMENT);
                    Standing.getInstance().addToStatistics(asteroid.getType(), asteroid.getLevel());
                    asteroid.destroy();
                }
            }

            for (int j = 0; j < corrosiveAnomalyEmitter.getActiveList().size(); j++) {
                CorrosiveAnomaly anomaly = (CorrosiveAnomaly) corrosiveAnomalyEmitter.getActiveList().get(j);
                if (!asteroid.isFragmented() && anomaly.getPosition().dst(asteroid.getPosition()) < PaintConstants.ANOMALY_CORROSIVE_WIDTH / 2) {
                    asteroid.destroyWithEffect();
                    ObjectPool.EMITTERS.get(asteroid.getClass())
                            .boom(asteroid.getPosition(), PlayerInteractionLevels.NONE);
                    GameInfoSystem.setup(anomaly.getPosition().x, anomaly.getPosition().y, GameInfoSystem.Preset.BOOM, 3.0f, false);
                    if (anomaly.getLevel() == 0) {
                        anomaly.destroyWithEffect();
                    } else {
                        anomaly.takeDamage(1);
                    }
                }
            }

            for (int k = 0; k < consumableJoiner.size(); k++) {
                AbstractConsumable consumable = consumableJoiner.get(k);
                if (!asteroid.isFragmented() && consumable.getPosition().dst(asteroid.getPosition()) < 128) {
                    asteroid.destroyWithEffect();
                    if (!consumable.isShielded()) {
                        consumable.destroyWithEffect();
                    }
                    ObjectPool.EMITTERS.get(asteroid.getClass())
                            .boom(asteroid.getPosition(), PlayerInteractionLevels.NONE);
                    GameInfoSystem.setup(
                            consumable.getPosition().x,
                            consumable.getPosition().y,
                            GameInfoSystem.Preset.SAD,
                            3.0f,
                            true
                    );
                }
            }

            //
            if (asteroid.isFragmented()) {
                ray.setCount(12);
                ray.setSize(2);
                ray.setPreset(Ray.Preset.GREY);
                ray.setHarvestVelocity(Rules.PLAYER_RAY_FRAGMENT_HARVEST_VELOCITY);
                ray.setTakeRange(Rules.PLAYER_FRAGMENT_TAKE_RANGE);
                ray.harvest(player, asteroid, dt);
            } else {
                if (Shuttle.isLaser) {
                    ray.setCount(5);
                    ray.setSize(3);
                    ray.setPreset(Ray.Preset.RED);
                    ray.laser(player, asteroid, dt);
                }
            }
            //

        }
        rocketEmitter.checkCollision();
    }

    private void abstractAnomalyDeactivate(List<AbstractAnomaly> anomalies) {
        for (int i = 0; i < anomalies.size(); i++) {
            if (anomalies.get(i).isActivated()) {
                anomalies.get(i).setActivated(false);
            }
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

    private void consumeConsumable(AbstractConsumable consumable) {
        consumable.destroy();
        player.takeEffect(consumable);
        if (consumable instanceof Astronaut) {
            Astronaut astronaut = (Astronaut) consumable;
            Sticker profession = astronaut.getProfession();
            Standing.getInstance().changeAstronauts(profession);
            UIRightPanel.getInstance().add(profession);
            RightGroup.getInstance().updateScroll(astronaut.getLabels().get(Label.Layer.LABEL).get(0).getSticker());
            ConfigImageButton button = RightGroup.getInstance().getButtonMap().get(profession);
            if (button != null) {
                button.changeCount(1);
            }
        }
        Standing.getInstance().addToStatistics(consumable.getType(), consumable.getLevel());
    }

    private void collideWithRocket(AbstractAnomaly anomaly) {
        float dst = Shuttle.position.dst(anomaly.getPosition());
        if (dst <= Rules.ROCKETS_TAKE_RANGE && anomaly.isBorn() && !anomaly.isTaken()) {
            anomaly.setAtRocketPoint(true);
        } else {
            anomaly.setAtRocketPoint(false);
        }
    }

    private void damageToAnomaly(AbstractAnomaly abstractAnomaly) {
        abstractAnomaly.destroyWithEffect();
        player.getRamShield().blink(1.0f);
        player.slowDown(abstractAnomaly.getLevel() * 50.0f);
        if (Shuttle.velocity.len() <= Rules.PLAYER_ENGINE_POWER) {
            Standing.getInstance().changeRamPower(-abstractAnomaly.getLevel());
        }
        abstractAnomaly.showSprayCollision();
        player.killPlusPlus(abstractAnomaly);
        Standing.getInstance().addToStatistics(abstractAnomaly.getType(), abstractAnomaly.getLevel());
    }

    private void damageToPlayer(AbstractPoolable anomaly) {
        if (anomaly.getLevel() >= 3) {
            if (Radar.isActive) {
                boomBox.playSound(MSConstants.LOSE_RADAR);
                Radar.isActive = false;
            }
        }

        if (player.addZigZag(0.5f * anomaly.getLevel() + 0.2f)) {
            Standing.getInstance().takeDamage(anomaly.getLevel() + 1);
            GameInfoSystem.setup(
                    Shuttle.position.x,
                    Shuttle.position.y,
                    GameInfoSystem.Preset.BRAKED,
                    5.0f,
                    false
            );
            tmp1V2.set(Shuttle.position).sub(anomaly.getPosition()).nor().scl(300 + anomaly.getLevel() * 50);
            Shuttle.velocity.add(tmp1V2);
            Standing.getInstance().setRamPower(0.0f);
            tmp1V2.scl(-1);
            anomaly.getVelocity().add(tmp1V2);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        ShaderManager.PAUSE.dispose();
        ShaderManager.BLACKHOLE.dispose();
        if (!Rules.GAME_OVER) {
            profiles.dispose();
        }
    }
}