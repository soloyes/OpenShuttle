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
import com.shuttle.game.bot.consumable.CrateEmitter;
import com.shuttle.game.bot.consumable.FuelEmitter;
import com.shuttle.game.bot.consumable.StorageEmitter;
import com.shuttle.game.bot.consumable.fuel.Fuel;
import com.shuttle.game.bot.rocket.RocketLauncher;
import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.conf.GlobalInteractionLevels;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.conf.VConstants;
import com.shuttle.game.effects.PrettySound;
import com.shuttle.game.effects.Ray;
import com.shuttle.game.effects.Shake;
import com.shuttle.game.effects.Vibrator;
import com.shuttle.game.effects.particle.ParticleEmitter;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.player.Standing;
import com.shuttle.game.ui.GameInfoSystem;
import com.shuttle.game.ui.Joystick;
import com.shuttle.game.ui.PlayerPointer;
import com.shuttle.game.ui.groups.PauseGroup;
import com.shuttle.game.ui.groups.leftright.SwitchGroup;
import com.shuttle.game.ui.groups.selector.SelectorGroup;
import com.shuttle.game.ui.panels.UIBonusPanel;
import com.shuttle.game.ui.panels.UIInfoPanel;
import com.shuttle.game.ui.panels.UILatestPanel;
import com.shuttle.game.ui.panels.info.AnomalyTimer;
import com.shuttle.game.utils.ActiveListJoiner;
import com.shuttle.game.utils.BoundCheck;
import com.shuttle.game.utils.ShaderManager;
import com.shuttle.game.utils.pool.AbstractPoolable;

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
    @Getter
    private SelectorGroup selectorGroup;
    @Getter
    private SwitchGroup switchGroup;
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
    @Getter
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
    @Getter
    private List<List<? extends AbstractAnomaly>> anomalyList;
    private List<List<? extends AbstractAsteroid>> asteroidsList;
    @Getter
    private UIInfoPanel uiInfoPanel;
    @Getter
    private UILatestPanel uiLatestPanel;
    private UIBonusPanel uiBonusPanel;
    @Getter
    private RocketLauncher rocketLauncher;
    private Standing standing;

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
        boomBox.stopAllMusicAndPlayThese(MSConstants.GAME_SONG, MSConstants.BLACKHOLE_ROAR_MUSIC);
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
        rocketLauncher = new RocketLauncher(this);

        blackhole = new Blackhole();
        player = new Shuttle(this, joystick);
        standing = Standing.getInstance();
        uiLatestPanel = new UILatestPanel(this);
        uiBonusPanel = new UIBonusPanel(this);
        uiInfoPanel = new UIInfoPanel(this);

        BoundCheck.avoidPosition(blackhole.getPosition(), Shuttle.position, (int) Rules.BLACKHOLE_TAKE_RANGE + 50, 0, Rules.SPACE_WIDTH, 0, Rules.SPACE_HEIGHT);
        background.setPlayer(player);
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
        ActiveListJoiner<AbstractAnomaly> anomalyJoiner = new ActiveListJoiner<AbstractAnomaly>();
        consumableList = consumableJoiner.join(astronautEmitter.getActiveList(), crateEmitter.getActiveList(), fuelEmitter.getActiveList(), storageEmitter.getActiveList());
        anomalyList = anomalyJoiner.join(corrosiveAnomalyEmitter.getActiveList(), electricalAnomalyEmitter.getActiveList(), magneticAnomalyEmitter.getActiveList(), radiationAnomalyEmitter.getActiveList());
        asteroidsList = asteroidsJoiner.join(metalAsteroidEmitter.getActiveList(), mineralAsteroidEmitter.getActiveList());
        //
        storageEmitter.emit();
    }

    private void calcMusicEffectVolume() {
        float v;
        if (Shuttle.position.dst(blackhole.getPosition()) >= Rules.BLACKHOLE_ROAR_RANGE) {
            v = 0.0f;
        } else if (Shuttle.position.dst(blackhole.getPosition()) <= Rules.BLACKHOLE_TAKE_RANGE) {
            v = 0.5f;
        } else {
            v = 1 - Shuttle.position.dst(blackhole.getPosition()) / Rules.BLACKHOLE_ROAR_RANGE;
        }
        boomBox.setMusicEffectVolume(MSConstants.BLACKHOLE_ROAR_MUSIC, v);
    }

    @Override
    public void createGUI() {
        pauseGroup = new PauseGroup();
        selectorGroup = new SelectorGroup(this);
        switchGroup = new SwitchGroup(this);
        stage.addActor(pauseGroup);
        stage.addActor(selectorGroup);
        stage.addActor(switchGroup);
    }

    @Override
    public void render(float dt) {
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
        if (GamePreferences.getPrefEffectsLevel() == GamePreferences.EffectLevel.LOW.getValue() || shaderBlackHole == null) {
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
        rocketLauncher.render(batch);
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
        if (GamePreferences.getPrefEffectsLevel() == GamePreferences.EffectLevel.LOW.getValue() || shaderBlackHole == null) {
            batch.setShader(null);
            //BEGIN
            batch.draw(fboTexRegion, 0, 0, Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT);
        } else {
            batch.setShader(shaderBlackHole);
            shaderBlackHole.setUniformf("resolution", Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT);
            if(!pauseGroup.isPause()) {
                holeTime += dt;
            }
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
        if (!Rules.GAME_OVER) {
            uiLatestPanel.render(batch);
            uiBonusPanel.render(batch);
            uiInfoPanel.render(batch);
            selectorGroup.render(batch);
        }

        if (!pauseGroup.isPause()) {
            joystick.render(batch);
        }

        Color tmpColor = batch.getColor();
        if (Rules.GAME_OVER) {
            boomBox.stopAllMusicAndPlayThis(MSConstants.MENU_SONG);
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
            uiLatestPanel.update(dt);
            uiInfoPanel.update(dt);
            rocketLauncher.update(dt);
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
            standing.update(dt);
            selectorGroup.update(dt);
            uiBonusPanel.update(dt);
        }
        stage.act();
    }

    //todo: It is works, somehow.
    //todo: Cam position is reset when fbo.end(), copy-paste from cameraHelper is the hack.
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
        for (int i = 0; i < consumableList.size(); i++) {
            for (int j = 0; j < consumableList.get(i).size(); j++) {
                AbstractConsumable consumable = consumableList.get(i).get(j);
                if (!consumable.isTaken()) {
                    if (Shuttle.position.dst(consumable.getPosition()) <= PaintConstants.SHUTTLE_WIDTH / 2) {
                        if (player.getShieldLevel().getLevel() < GlobalInteractionLevels.GREEN.getLevel() || consumable.isShielded()) {
                            consumable.destroy();
                            player.takeEffect(consumable);
                            if (consumable instanceof Astronaut){
                                if( ((Astronaut) consumable).getMultiplier() == 1 ){
                                    player.setA1(player.getA1() + 1);
                                    uiBonusPanel.addBonus(1);
                                }
                                if( ((Astronaut) consumable).getMultiplier() == 2 ){
                                    player.setA2(player.getA2() + 1);
                                    uiBonusPanel.addBonus(2);
                                }
                                if( ((Astronaut) consumable).getMultiplier() == 3 ){
                                    player.setA3(player.getA3() + 1);
                                    uiBonusPanel.addBonus(3);
                                }
                                if( ((Astronaut) consumable).getMultiplier() == 4 ){
                                    player.setA4(player.getA4() + 1);
                                    uiBonusPanel.addBonus(4);
                                }
                            }
                            Standing.getInstance().addToStatistics(consumable.getType(), consumable.getLevel());
                        } else {
                            if (!player.canCatch(consumable)) {
                                consumable.destroyWithEffect();
                                if (consumable instanceof Fuel) {
                                    shake.shake(1, 35, 50);
                                }
                                player.badEffect(consumable);
                            } else {
                                consumable.destroy();
                                player.takeEffect(consumable);
                            }
                        }
                    }
                    ray.setCount(24);
                    ray.setSize(4);
                    ray.setHarvestVelocity(Rules.PLAYER_RAY_CONSUMABLE_HARVEST_VELOCITY);
                    ray.setTakeRange(Rules.PLAYER_CONSUMABLE_TAKE_RANGE);
                    if (player.getShieldLevel().getLevel() < GlobalInteractionLevels.GREEN.getLevel() || consumable.isShielded()) {
                        ray.setPreset(Ray.Preset.GREEN);
                    } else {
                        if (player.canCatch(consumable)) {
                            ray.setPreset(Ray.Preset.GREEN);
                        } else {
                            ray.setPreset(Ray.Preset.RED);
                        }
                    }
                    ray.harvest(player, consumable, particleEmitter, dt);
                    anyAtHarvestPoint |= consumable.isAtRayPoint();
                }
            }
        }

        player.showTakeRangeField(dt, anyAtHarvestPoint);

        for (int i = 0; i < corrosiveAnomalyEmitter.getActiveList().size(); i++) {
            CorrosiveAnomaly anomaly = (CorrosiveAnomaly) corrosiveAnomalyEmitter.getActiveList().get(i);
            if (!anomaly.isTaken()) {
                if (Shuttle.position.dst(anomaly.getPosition()) <= PaintConstants.ANOMALY_CORROSIVE_WIDTH / 2 && anomaly.isBorn()) {
                    if (anomaly.getLevel() < player.getPushPower() || Shuttle.velocity.len() >= Rules.PLAYER_TURBO_THRESHOLD_VELOCITY) {
                        if (Shuttle.velocity.len() >= Rules.PLAYER_TURBO_THRESHOLD_VELOCITY) {
                            boomBox.playSound(MSConstants.BURST_CORROSIVE_ANOMALY);
                        } else {
                            boomBox.playSound(MSConstants.DESTROY_CORROSIVE_ANOMALY);
                        }
                        damageToAnomaly(anomaly);
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
            collideWithRocket(anomaly);
        }

        for (int i = 0; i < electricalAnomalyEmitter.getActiveList().size(); i++) {
            ElectricalAnomaly anomaly = (ElectricalAnomaly) electricalAnomalyEmitter.getActiveList().get(i);
            if (!anomaly.isTaken()) {
                if (Shuttle.position.dst(anomaly.getPosition()) < PaintConstants.ANOMALY_ELECTRIC_WIDTH / 2 && anomaly.isBorn()) {
                    anomaly.setHungry(false);
                    damageToPlayer(anomaly);
                    Vibrator.vibrate(VConstants.PLAYER_ANOMALY_CORROSIVE_DESTROY, anomaly.getLevel());
                    prettyElectricKick.playSound(MSConstants.KICKED_BY_ELECTRIC);
                    anomaly.setV(anomaly.getV() * Rules.ANOMALY_ELECTRIC_DECREASE_FACTOR);
                }
            }
            collideWithRocket(anomaly);
        }

        //
        boolean tmp1 = false;
        for (int i = 0; i < radiationAnomalyEmitter.getActiveList().size(); i++) {
            RadiationAnomaly anomaly = (RadiationAnomaly) radiationAnomalyEmitter.getActiveList().get(i);
            if (!anomaly.isTaken()) {
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
            collideWithRocket(anomaly);
        }
        Standing.getInstance().setScoreAlertRadiationAnomaly(tmp1);

        boolean tmp2 = false;
        for (int i = 0; i < magneticAnomalyEmitter.getActiveList().size(); i++) {
            MagneticAnomaly anomaly = (MagneticAnomaly) magneticAnomalyEmitter.getActiveList().get(i);
            if (!anomaly.isTaken()) {
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
            collideWithRocket(anomaly);
        }
        Standing.getInstance().setFuelAlertMagneticAnomaly(tmp2);
        //

        for (int i = 0; i < asteroidsList.size(); i++) {
            for (int j = 0; j < asteroidsList.get(i).size(); j++) {
                AbstractAsteroid asteroid = asteroidsList.get(i).get(j);
                if (!asteroid.isTaken()) {
                    if (Shuttle.position.dst(asteroid.getPosition()) < asteroid.getHeight() * asteroid.getScaleX()) {
                        if (!asteroid.isFragmented()) {
                            asteroid.destroyWithEffect();
                            Vibrator.vibrate(VConstants.PLAYER_ASTEROID_DESTROY);
                            player.getLifeShield().blink(1.0f);
                            switch (asteroid.getType()) {
                                case ASTEROID_METAL:
                                    metalAsteroidEmitter.boom(asteroid.getPosition(), player.getShieldLevel());
                                    break;
                                case ASTEROID_MINERAL:
                                    mineralAsteroidEmitter.boom(asteroid.getPosition(), player.getShieldLevel());
                                    break;
                            }
                            player.takeDamage(player.getShieldLevel().getLevel() + 1);
                            GameInfoSystem.setup(
                                    Shuttle.position.x, Shuttle.position.y,
                                    GameInfoSystem.Preset.SCREECH,
                                    3.0f,
                                    true
                            );
                            boomBox.playSound(MSConstants.DESTROY_ASTEROID);
                            Standing.getInstance().addToStatistics(asteroid.getType(), asteroid.getLevel());
                        } else {
                            player.getRamShield().blink(0.5f);
                            Standing.getInstance().changeRamPower(asteroid.getSizeForHarvest());
                            if (asteroid.getType() == AbstractPoolable.Type.ASTEROID_MINERAL && player.isLaser()) {
                                Standing.getInstance().changeLaserChargeLevel(asteroid.getSizeForHarvest());
                            }
                            //pretty sound if collect much fragments in little time
                            prettyFragmentHarvest.playSound(MSConstants.GET_FRAGMENT);
                            asteroid.destroy();
                        }
                    }

                    for (int k = 0; k < corrosiveAnomalyEmitter.getActiveList().size(); k++) {
                        CorrosiveAnomaly anomaly = (CorrosiveAnomaly) corrosiveAnomalyEmitter.getActiveList().get(k);
                        if (!asteroid.isFragmented() && anomaly.getPosition().dst(asteroid.getPosition()) < PaintConstants.ANOMALY_CORROSIVE_WIDTH / 2) {
                            asteroid.destroyWithEffect();
                            switch (asteroid.getType()) {
                                case ASTEROID_METAL:
                                    metalAsteroidEmitter.boom(asteroid.getPosition(), GlobalInteractionLevels.NONE);
                                    break;
                                case ASTEROID_MINERAL:
                                    mineralAsteroidEmitter.boom(asteroid.getPosition(), GlobalInteractionLevels.NONE);
                                    break;
                            }
                            GameInfoSystem.setup(anomaly.getPosition().x, anomaly.getPosition().y, GameInfoSystem.Preset.BOOM, 3.0f, false);
                            if (anomaly.getLevel() == 0) {
                                anomaly.destroyWithEffect();
                            } else {
                                anomaly.takeDamage(1);
                            }
                        }
                    }

                    for (int m = 0; m < consumableList.size(); m++) {
                        for (int p = 0; p < consumableList.get(m).size(); p++) {
                            AbstractConsumable consumable = consumableList.get(m).get(p);
                            if (!asteroid.isFragmented() && consumable.getPosition().dst(asteroid.getPosition()) < 128) {
                                asteroid.destroyWithEffect();
                                if (!consumable.isShielded()) {
                                    consumable.destroyWithEffect();
                                }
                                switch (asteroid.getType()) {
                                    case ASTEROID_METAL:
                                        metalAsteroidEmitter.boom(asteroid.getPosition(), GlobalInteractionLevels.NONE);
                                        break;
                                    case ASTEROID_MINERAL:
                                        mineralAsteroidEmitter.boom(asteroid.getPosition(), GlobalInteractionLevels.NONE);
                                        break;
                                }
                                GameInfoSystem.setup(
                                        consumable.getPosition().x,
                                        consumable.getPosition().y,
                                        GameInfoSystem.Preset.SAD,
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
                        if (player.isLaser()) {
                            ray.setCount(5);
                            ray.setSize(3);
                            ray.setPreset(Ray.Preset.RED);
                            ray.laser(player, asteroid, particleEmitter, dt);
                        }
                    }
                    //
                }
            }
        }
        rocketLauncher.getRocketEmitter().checkCollision();
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
        abstractPoolable.destroyWithEffect();
        player.getRamShield().blink(1.0f);
        player.slowDown(abstractPoolable.getLevel() * 50.0f);
        if (Shuttle.velocity.len() <= Rules.PLAYER_ENGINE_POWER) {
            Standing.getInstance().changeRamPower(-abstractPoolable.getLevel());
        }
        abstractPoolable.showSprayCollision(particleEmitter);
        player.killPlusPlus(abstractPoolable);
        Standing.getInstance().addToStatistics(abstractPoolable.getType(), abstractPoolable.getLevel());
    }

    private void damageToPlayer(AbstractPoolable anomaly) {
        if (anomaly.getLevel() >= 3) {
            if (player.getRadar().isActive()) {
                boomBox.playSound(MSConstants.LOSE_RADAR);
                player.getRadar().setActive(false);
            }
        }

        if (player.addZigZag(0.5f * anomaly.getLevel() + 0.2f)) {
            player.takeDamage(anomaly.getLevel() + 1);
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

    private void collideWithRocket(AbstractAnomaly anomaly) {
        float dst = Shuttle.position.dst(anomaly.getPosition());
        if (dst <= Rules.ROCKETS_TAKE_RANGE && anomaly.isBorn() && !anomaly.isTaken()) {
            anomaly.setAtRocketPoint(true);
        } else {
            anomaly.setAtRocketPoint(false);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        shaderManager.dispose();
        Standing.getInstance().writeResult();
        uiInfoPanel.dispose();
    }
}