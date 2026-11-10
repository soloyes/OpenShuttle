package com.buran.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.buran.game.background.Background;
import com.buran.game.background.SectorBorder;
import com.buran.game.world.Shelters;
import com.buran.game.campaign.Campaign;
import com.buran.game.campaign.SectorRun;
import com.buran.game.ui.menu.Theme;
import com.buran.game.world.ThreatOverlay;
import com.buran.game.world.ThreatTextures;
import com.buran.game.background.SpaceBackground;
import com.buran.game.bot.blackhole.BlackholeDebris;
import com.buran.game.bot.anomaly.AbstractAnomaly;
import com.buran.game.bot.anomaly.CorrosiveAnomaly;
import com.buran.game.bot.anomaly.CorrosiveAnomalyEmitter;
import com.buran.game.bot.anomaly.ElectricalAnomaly;
import com.buran.game.bot.anomaly.ElectricalAnomalyEmitter;
import com.buran.game.bot.anomaly.MagneticAnomaly;
import com.buran.game.bot.anomaly.MagneticAnomalyEmitter;
import com.buran.game.bot.anomaly.RadiationAnomaly;
import com.buran.game.bot.anomaly.RadiationAnomalyEmitter;
import com.buran.game.bot.asteroids.AbstractAsteroid;
import com.buran.game.bot.asteroids.MetalAsteroidEmitter;
import com.buran.game.bot.asteroids.MineralAsteroidEmitter;
import com.buran.game.bot.blackhole.Blackhole;
import com.buran.game.bot.blackhole.CircleRenderer;
import com.buran.game.bot.consumable.AbstractConsumable;
import com.buran.game.bot.consumable.Astronaut;
import com.buran.game.bot.consumable.AstronautEmitter;
import com.buran.game.bot.consumable.Crate;
import com.buran.game.bot.consumable.CrateEmitter;
import com.buran.game.bot.consumable.FuelEmitter;
import com.buran.game.bot.consumable.Storage;
import com.buran.game.bot.consumable.StorageEmitter;
import com.buran.game.bot.consumable.fuel.Fuel;
import com.buran.game.bot.rocket.RocketEmitter;
import com.buran.game.bot.rocket.RocketLauncher;
import com.buran.game.conf.Buffs;
import com.buran.game.conf.DeathMode;
import com.buran.game.conf.MSConstants;
import com.buran.game.conf.PaintConstants;
import com.buran.game.conf.PlayerInteractionLevels;
import com.buran.game.conf.Rules;
import com.buran.game.conf.VConstants;
import com.buran.game.effects.AsteroidLaunchEmitter;
import com.buran.game.effects.Oscillator;
import com.buran.game.effects.PrettySound;
import com.buran.game.effects.PostProcessor;
import com.buran.game.effects.Juice;
import com.buran.game.ui.menu.GameMenu;
import com.buran.game.ui.hud.TelemetryHud;
import com.buran.game.input.Gamepad;
import com.buran.game.effects.Ray;
import com.buran.game.effects.Shake;
import com.buran.game.effects.Vibrator;
import com.buran.game.effects.particle.ParticleEmitter;
import com.buran.game.labelsticker.Label;
import com.buran.game.labelsticker.Sticker;
import com.buran.game.player.Buran;
import com.buran.game.player.Standing;
import com.buran.game.player.navigation.Radar;
import com.buran.game.ui.GameInfoSystem;
import com.buran.game.ui.Joystick;
import com.buran.game.ui.groups.PauseGroup;
import com.buran.game.ui.groups.configs.ConfigImageButton;
import com.buran.game.ui.groups.configs.LeftGroup;
import com.buran.game.ui.groups.configs.RightGroup;
import com.buran.game.ui.groups.mvc.global.Global;
import com.buran.game.ui.groups.mvc.profiles.Profiles;
import com.buran.game.ui.groups.mvc.results.DetailedResultGroup;
import com.buran.game.ui.groups.selector.SelectorGroup;
import com.buran.game.ui.groups.switches.SwitchGroup;
import com.buran.game.ui.help.Tutorial;
import com.buran.game.ui.panels.UILeftPanel;
import com.buran.game.ui.panels.UIRightPanel;
import com.buran.game.ui.panels.UITopPanel;
import com.buran.game.utils.Assets;
import com.buran.game.utils.BoundCheck;
import com.buran.game.utils.ListOfListsJoiner;
import com.buran.game.utils.ShaderManager;
import com.buran.game.utils.pool.AbstractPoolable;
import com.buran.game.utils.pool.ObjectPool;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author soloyes on 03/06/18.
 */

public class GameScreen extends BaseScreen {
    @Getter
    @Setter
    private Buran player;
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
    private TelemetryHud hud;
    private Standing standing;
    private Profiles profiles;
    private Global global;
    private Tutorial tutorial;

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
    private AsteroidLaunchEmitter asteroidLaunchEmitter;
    private TextureRegion nebulaRegion;
    private Oscillator nebulaAOscillator;
    private float nebulaA = 0.5f;
    private int nebulaRegionX, nebulaRegionY;
    private CircleRenderer circleRenderer;
    private float adsDelay;

    public GameScreen(SpriteBatch batch, Camera camera) {
        super(batch);
        this.batch = batch;
        this.camera = camera;
        this.tmp1V2 = new Vector2(0, 0);
        this.tmp1V3 = new Vector3(0, 0, 0);
        this.cameraHelper = new OrthographicCamera(Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT);
    }

    @Override
    public void show() {
        super.show();
        resetSessionState();
        standing = Standing.getInstance();
        fadeInAlpha = 0.0f;
        gameOverDeactivate = false;
        tutorial = new Tutorial();

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
        asteroidLaunchEmitter = new AsteroidLaunchEmitter();

        boomBox.stopAllMusicAndPlayThese(MSConstants.GAME_SONG, MSConstants.BLACKHOLE_ROAR_MUSIC);
        rebuildFbo(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        ShaderManager.PAUSE.load();
        ShaderManager.BLACKHOLE.load();

        background = new Background();
        if (sectorBorder != null) {
            sectorBorder.dispose();
        }
        sectorBorder = new SectorBorder();
        if (spaceBackground != null) {
            spaceBackground.dispose();
        }
        spaceBackground = new SpaceBackground();
        if (debris != null) {
            debris.dispose();
        }
        debris = new BlackholeDebris();
        if (postProcessor != null) {
            postProcessor.dispose();
        }
        postProcessor = new PostProcessor();
        flash = 0f;
        Juice.reset();
        runReported = false;
        gameInfoSystem = new GameInfoSystem();
        joystick = new Joystick();

        blackhole = new Blackhole();
        if (Campaign.current() != null && !Campaign.current().blackhole) {
            Blackhole.park();
        }
        player = new Buran(joystick, particleEmitter);
        uiTopPanel = new UITopPanel();
        hud = new TelemetryHud();

        BoundCheck.avoidPosition(Blackhole.position, Buran.position, (int) Buffs.getBlackholeTakeRange() + 50, 0, Rules.SPACE_WIDTH, 0, Rules.SPACE_HEIGHT);
        Shelters.create(Math.max(2, Rules.SPACE_WIDTH * Rules.SPACE_HEIGHT / (Rules.VIEW_WIDTH * Rules.VIEW_HEIGHT * 4)));
        if (Rules.DEBUG_START_AT_HOLE) {
            Buran.position.set(Blackhole.position.x + Buffs.getBlackholeTakeRange() * 0.9f, Blackhole.position.y);
        }
        Rules.StartLevel.values()[Profiles.getSettings().getLevel()].setGameLevel();
        if (Profiles.getSettings().getLevel() == 0) {
            astronautEmitter.setConsumablesMaxCount(Rules.CONSUMABLE_ASTRONAUT_MAX_COUNT * 2);
        }

        InputMultiplexer im = new InputMultiplexer(player, stage, joystick);
        Gdx.input.setInputProcessor(im);

        calcMusicEffectVolume();
        prettyCorrosiveKick = PrettySound.getPretty(Rules.ANOMALY_KICK_PRETTY_SOUND_DELAY);
        prettyElectricKick = PrettySound.getPretty(Rules.ANOMALY_KICK_PRETTY_SOUND_DELAY);
        prettyFragmentHarvest = PrettySound.getPretty(Rules.FRAGMENT_KICK_PRETTY_SOUND_DELAY);
        ray = new Ray(particleEmitter, mineralAsteroidEmitter.getActiveList(), metalAsteroidEmitter.getActiveList());
        //todo: Check how-to. Not relevant for gameplay
        consumableJoiner = ListOfListsJoiner.newAbstractConsumableList(astronautEmitter.getActiveList(), crateEmitter.getActiveList(), storageEmitter.getActiveList(), fuelEmitter.getActiveList());
        asteroidsJoiner = ListOfListsJoiner.newAbstractAsteroidList(mineralAsteroidEmitter.getActiveList(), metalAsteroidEmitter.getActiveList());
        anomalyJoiner = ListOfListsJoiner.newAbstractAnomalyList(corrosiveAnomalyEmitter.getActiveList(), electricalAnomalyEmitter.getActiveList(), radiationAnomalyEmitter.getActiveList(), magneticAnomalyEmitter.getActiveList());
        //
        rocketLauncher = new RocketLauncher(rocketEmitter, anomalyJoiner);
        if (threatTextures == null) {
            threatTextures = new ThreatTextures();
        }
        threatOverlay = new ThreatOverlay(anomalyJoiner, threatTextures);
        // Campaign sector: apply its threat table and goal; arcade keeps the classic rules.
        Rules.corrosiveHuntTime = Rules.ANOMALIES_CORROSIVE_HUNT_TIME;
        if (Campaign.current() != null) {
            sectorRun = new SectorRun(Campaign.current());
            sectorRun.configure();
            Shelters.create(Campaign.current().shelters);
        } else {
            sectorRun = null;
        }
        Campaign.setRun(sectorRun);
        storageEmitter.emit();
        this.profiles = new Profiles();
        this.nebulaRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.CIRCLE_GLOW);
        nebulaRegionX = MathUtils.random(0, Rules.SPACE_WIDTH - Rules.VIEW_WIDTH);
        nebulaRegionY = MathUtils.random(0, Rules.SPACE_HEIGHT - Rules.VIEW_HEIGHT);
        this.nebulaAOscillator = new Oscillator();
        this.circleRenderer = new CircleRenderer();
    }

    private void calcMusicEffectVolume() {
        float v;
        if (Buran.position.dst(Blackhole.position) >= Rules.BLACKHOLE_ROAR_RANGE) {
            v = 0.0f;
        } else if (Buran.position.dst(Blackhole.position) <= Buffs.getBlackholeTakeRange()) {
            v = 0.5f;
        } else {
            v = 1 - Buran.position.dst(Blackhole.position) / Rules.BLACKHOLE_ROAR_RANGE;
        }
        boomBox.setMusicEffectVolume(MSConstants.BLACKHOLE_ROAR_MUSIC, v);
    }

    @Override
    public void createGUI() {
        global = new Global();
        global.init();
        global.setGlobalStart(true);
        // Workaround (Android restarts via app manager)
        RightGroup.doStart();
        LeftGroup.doStart();
        SelectorGroup.doStart();
        SwitchGroup.doStart();
        UILeftPanel.doStart();
        UIRightPanel.doStart();
        //
        detailedResultGroup = new DetailedResultGroup();
        pauseGroup = new PauseGroup();
        tutorial = new Tutorial();
        stage.addActor(detailedResultGroup);
        stage.addActor(pauseGroup);
        stage.addActor(tutorial);
        // Side tabs are a touch-screen affordance; on web/desktop they only clutter the TV frame.
        SwitchGroup.getInstance().setVisible(Gdx.app.getType() == com.badlogic.gdx.Application.ApplicationType.Android
                || Gdx.app.getType() == com.badlogic.gdx.Application.ApplicationType.iOS);
        stage.addActor(SwitchGroup.getInstance());
        if (hud == null) {
            hud = new TelemetryHud();   // createGUI runs before the rest of show()
        }
        // Gamepad/keyboard use the HUD cargo bay; the touch carousel is for phones.
        SelectorGroup.getInstance().setVisible(!hud.usesCargoBay());
        stage.addActor(SelectorGroup.getInstance());
        gameMenu = new GameMenu();
        stage.addActor(gameMenu);
    }

    @Override
    public void render(float dt) {
        FitViewport v = (FitViewport) ScreenManager.getInstance().getViewport();
        spaceBackground.prepare(batch, camera);
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
        if (spaceBackground.isAvailable()) {
            spaceBackground.render(batch, camera);
        } else {
            background.render(batch);
        }
        sectorBorder.render(batch, camera);
        Shelters.render(batch, threatTextures);
        if (sectorRun != null) {
            sectorRun.renderWorld(batch, threatTextures);
        }
        batch.end();
        //END

        //BEGIN
        batch.begin();
        circleRenderer.render(camera);
        batch.end();
        //END

        //BEGIN
        batch.begin();
        if (!Profiles.getSettings().isShader() || ShaderManager.BLACKHOLE.getProgram() == null) {
            if (Blackhole.enabled) {
                blackhole.render(batch);
            }
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
        threatOverlay.renderWorld(batch, Theme.font(44));
        rocketLauncher.render(batch);
        asteroidLaunchEmitter.render(batch);
        if (Profiles.getSettings().isShader()) {
            if (Blackhole.enabled) {
                debris.render(batch);
            }
        }
        batch.setColor(Color.WHITE);
        batch.end();

        float shaderHoleX = (Blackhole.position.x - camera.position.x + Rules.VIEW_WIDTH / 2f) / Rules.VIEW_WIDTH;
        float shaderHoleY = (Blackhole.position.y - camera.position.y + Rules.VIEW_HEIGHT / 2f) / Rules.VIEW_HEIGHT;

        //
        ScreenManager.getInstance().resetCamera();
        //FBO END
        fbo.end(v.getScreenX(), v.getScreenY(), v.getScreenWidth(), v.getScreenHeight());

        if (!PauseGroup.isPause()) {
            holeTime += dt;
        }
        if (postProcessor.isAvailable()) {
            post.hole = Profiles.getSettings().isShader() && Blackhole.enabled;
            post.holeX = shaderHoleX;
            post.holeY = shaderHoleY;
            // Lens radius = capture range, in units of the visible height.
            post.holeRadius = Buffs.getBlackholeTakeRange() / Rules.VIEW_HEIGHT;
            post.holeSpin = holeTime;
            post.aspect = (float) Rules.VIEW_WIDTH / Rules.VIEW_HEIGHT;
            post.grey = PauseGroup.isPause() || Rules.GAME_OVER;
            post.flash = Math.max(0f, flash);
            post.flashR = flashColor.r;
            post.flashG = flashColor.g;
            post.flashB = flashColor.b;
            flash -= dt * 2.5f;
            postProcessor.prepare(batch, fboTexRegion, fbo.getWidth(), fbo.getHeight(), post, dt);
            v.apply();
            batch.setProjectionMatrix(camera.combined);
            super.render(dt);
            postProcessor.draw(batch, fboTexRegion, 0, 0, Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT, fbo.getWidth(), fbo.getHeight(), post);
            batch.begin();
        } else {
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
            holeTime = holeTime % (MathUtils.PI2);
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
        }

        if (PauseGroup.isPause() && ShaderManager.PAUSE.getProgram() != null) {
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
            if (!PauseGroup.isPause()) {
                threatOverlay.renderEdges(batch, cameraHelper, Theme.font(32));
            }
            hud.render(batch);
            if (sectorRun != null) {
                sectorRun.renderHud(batch, cameraHelper, threatTextures, Theme.font(54), Theme.font(44));
            }
            if (!PauseGroup.isPause() && !hud.usesCargoBay()) {
                SelectorGroup.getInstance().render(batch);
            }
        }

        if (!PauseGroup.isPause()) {
            joystick.render(batch);
        }

        Color tmpColor = batch.getColor();
        if (Rules.GAME_OVER) {
            pauseGroup.setGameOver();
            fadeInAlpha += dt / 2;
            if (fadeInAlpha >= 1.0f) {
                fadeInAlpha = 1.0f;
            }
            detailedResultGroup.setColor(tmpColor.r, tmpColor.g, tmpColor.b, fadeInAlpha);
            if (!runReported) {
                // Record the run once; the report itself is drawn by GameMenu.
                runReported = true;
                boomBox.stopAllMusicAndPlayThis(MSConstants.GAME_OVER);
                profiles.dispose();
                if (Rules.ADS_ENABLED) {
                    ScreenManager.getInstance().getAdsController().showBannerAd(true);
                }
            }
        }
        batch.end();

        batch.setProjectionMatrix(cameraHelper.combined);
        //BEGIN
        batch.begin();
        if (!Rules.GAME_OVER) {
            gameInfoSystem.render(batch);
        }
        batch.end();

        batch.setShader(null);
        stage.draw();
    }

    public void update(float dt) {
        boolean held = PauseGroup.isPause() || (sectorRun != null && sectorRun.isClear());
        com.buran.game.platform.Radio.setPaused(PauseGroup.isPause());
        com.buran.game.platform.Radio.update();
        if (sectorRun != null && !held && !Rules.GAME_OVER) {
            sectorRun.update(dt);
        }
        // The briefing holds the world still too, so nothing happens while the radio talks.
        boolean frozen = held || (sectorRun != null && sectorRun.isBriefing());
        if (Gamepad.get().isPulse() && !frozen && !Rules.GAME_OVER) {
            firePulseEffect();
        }
        sectorBorder.update(dt);
        if (!frozen) {
            Shelters.update(dt);
            threatOverlay.update(dt);
        }
        spaceBackground.update(dt);
        if (!frozen) {
            if (Blackhole.enabled) {
                debris.update(dt);
            }
        }
        cameraUpdate(dt);
        if (!frozen) {
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
            rocketLauncher.update(dt);
            asteroidLaunchEmitter.update(dt);
            if (!Rules.GAME_OVER) {
                uiTopPanel.update(dt);
                hud.update(dt);
                Standing.getInstance().addTime(dt);
            }

            gameInfoSystem.update(dt);
            if (!Rules.GAME_OVER) {
                if (!Rules.DEATH_MODE) {
                    boomBox.playRandomMusicEffects(dt);
                } else {
                    DeathMode.update(dt);
                }
                calcAdsDelay(dt);
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
            if (!Radar.isActive) {
                nebulaA = nebulaAOscillator.oscillate(nebulaA, 0.03f, 0.5f, 1.0f, dt);
            } else {
                nebulaA -= dt;
                if (nebulaA <= 0.0f) {
                    nebulaA = 0.0f;
                }
            }
        } else {
            if (Rules.ADS_ENABLED && profiles.getActive().getPreferences().getRunTimes() >= Rules.ADS_RUNTIME_DEATH_MODE) {
                ScreenManager.getInstance().getAdsController().hideBannerAd();
            }
        }
        cameraUpdate(dt);
        if (!frozen && !Rules.GAME_OVER) {
            Shake.getInstance().update(dt, camera);
            standing.update(dt);
            SelectorGroup.getInstance().update(dt);
            UIRightPanel.getInstance().update(dt);
            circleRenderer.update(dt);
        }
        stage.act(dt);
    }

    //It is works, somehow.
    //Cam position is reset when fbo.end(), copy-paste from cameraHelper is the hack.
    private final Vector2 lookAhead = new Vector2();
    private SectorBorder sectorBorder;
    private ThreatTextures threatTextures;
    private ThreatOverlay threatOverlay;
    private SectorRun sectorRun;
    private GameMenu gameMenu;
    private boolean runReported;
    private SpaceBackground spaceBackground;
    private BlackholeDebris debris;
    private PostProcessor postProcessor;
    private final PostProcessor.Params post = new PostProcessor.Params();
    /** Full-screen flash strength (decays); see {@link #flash(float, float, float, float)}. */
    private static float flash;
    private static final com.badlogic.gdx.graphics.Color flashColor = new com.badlogic.gdx.graphics.Color(1f, 1f, 1f, 1f);

    /** Flash the whole scene, e.g. on a hit or an explosion. */
    public static void flash(float strength, float r, float g, float b) {
        if (strength > flash) {
            flash = strength;
            flashColor.set(r, g, b, 1f);
        }
    }

    private void cameraUpdate(float dt) {
        if (player.isCameraSmooth()) {
            tmp1V3.set(Buran.position, 0.0f).sub(cameraHelper.position.x, cameraHelper.position.y, 0.0f).nor();
            if (!PauseGroup.isPause()) {
                cameraHelper.position.mulAdd(tmp1V3, Rules.CAMERA_SMOOTH_VELOCITY * dt);
            }
            camera.position.set(cameraHelper.position);
            if (
                //parking
                    tmp1V2.set(Buran.position).sub(
                            cameraHelper.position.x, cameraHelper.position.y).len2() <
                            tmp1V3.scl(Rules.CAMERA_SMOOTH_VELOCITY * dt).len2()
                    ) {
                player.setCameraSmooth(false);
            }
        } else {
            // Look ahead along the velocity and ease toward it (critically damped feel).
            lookAhead.set(Buran.velocity).scl(Rules.CAMERA_LOOK_AHEAD_TIME)
                    .limit(Math.min(Rules.VIEW_WIDTH, Rules.VIEW_HEIGHT) * Rules.CAMERA_LOOK_AHEAD_MAX);
            float k = 1.0f - (float) Math.exp(-Rules.CAMERA_FOLLOW_RATE * dt);
            cameraHelper.position.x += (Buran.position.x + lookAhead.x - cameraHelper.position.x) * k;
            // Centre the ship in the window above the console, not in the whole screen.
            float lift = Rules.hudFraction() * Rules.VIEW_HEIGHT / 2f;
            cameraHelper.position.y += (Buran.position.y - lift + lookAhead.y - cameraHelper.position.y) * k;
            camera.position.set(cameraHelper.position);
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
            if (Buran.position.dst(consumable.getPosition()) <= PaintConstants.BURAN_WIDTH / 2) {
                consumable.setHarvested(true);
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
            } else {
                consumable.setHarvested(false);
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
            ray.harvest(consumable, dt);
            anyAtHarvestPoint |= consumable.isAtRayPoint();
        }

        player.showTakeRangeField(dt, anyAtHarvestPoint);

        for (int i = 0; i < corrosiveAnomalyEmitter.getActiveList().size(); i++) {
            CorrosiveAnomaly anomaly = (CorrosiveAnomaly) corrosiveAnomalyEmitter.getActiveList().get(i);
            collideWithRocket(anomaly);
            if (anomaly.isTaken()) {
                continue;
            }
            if (Buran.position.dst(anomaly.getPosition()) <= PaintConstants.ANOMALY_CORROSIVE_WIDTH / 2 && anomaly.isBorn()) {
                if (anomaly.getLevel() < player.getPushPower() || Buran.velocity.len() >= Rules.PLAYER_TURBO_THRESHOLD_VELOCITY) {
                    if (Buran.velocity.len() >= Rules.PLAYER_TURBO_THRESHOLD_VELOCITY) {
                        boomBox.playSound(MSConstants.BURST_CORROSIVE_ANOMALY);
                        int score = (int) (Rules.ANOMALIES_BURST_SCORE_ADD * Sticker.COMMANDER.getCoefficient());
                        Standing.getInstance().changeScore(score, Buran.position, GameInfoSystem.Preset.SCORE + "[GREEN] +" + score + "[]", 3.0f, true);
                        GameInfoSystem.setup(
                                ScreenManager.getInstance().getCamera().position.x,
                                ScreenManager.getInstance().getCamera().position.y,
                                String.valueOf(GameInfoSystem.Preset.ROCKETMAN.getValue()),
                                3.0f,
                                2.0f,
                                true,
                                true
                        );
                    } else {
                        boomBox.playSound(MSConstants.DESTROY_CORROSIVE_ANOMALY);
                    }
                    damageToAnomaly(anomaly);
                    Vibrator.vibrateGame(VConstants.PLAYER_ANOMALY_CORROSIVE_DESTROY, anomaly.getLevel());
                } else {
                    damageToPlayer(anomaly);
                    if (anomaly.getLevel() != 4) {
                        anomaly.takeDamage(player.getPushPower());
                    }
                    anomaly.setHungry(false);
                    anomaly.setSlimTimer(0);
                    prettyCorrosiveKick.playSound(MSConstants.KICKED_BY_CORROSIVE);
                    Vibrator.vibrateGame(VConstants.PLAYER_ANOMALY_CORROSIVE_COLLISION);
                }
            }
        }

        for (int i = 0; i < electricalAnomalyEmitter.getActiveList().size(); i++) {
            ElectricalAnomaly anomaly = (ElectricalAnomaly) electricalAnomalyEmitter.getActiveList().get(i);
            if (anomaly.isTaken()) {
                continue;
            }
            collideWithRocket(anomaly);
            if (Buran.position.dst(anomaly.getPosition()) < PaintConstants.ANOMALY_ELECTRIC_WIDTH / 2 && anomaly.isBorn()) {
                anomaly.setHungry(false);
                damageToPlayer(anomaly);
                Vibrator.vibrateGame(VConstants.PLAYER_ANOMALY_CORROSIVE_DESTROY, anomaly.getLevel());
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
            if (Buran.position.dst(anomaly.getPosition()) < PaintConstants.ANOMALY_RADIATION_WIDTH / 3 && anomaly.isBorn()) {
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
                    Standing.getInstance().changeScore(-Buffs.getScoreDecrease(), Buran.position, "-[RED]" + GameInfoSystem.Preset.LOST_SCORE.getValue() + "[]", 2.0f, true);
                    boomBox.playSound(MSConstants.LOSE_SCORE);
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
            if (Buran.position.dst(anomaly.getPosition()) < PaintConstants.ANOMALY_MAGNETIC_HEIGHT / 3 && anomaly.isBorn()) {
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
                    Standing.getInstance().changeFuel(-Buffs.getFuelDecrease());
                    boomBox.playSound(MSConstants.LOSE_FUEL);
                    GameInfoSystem.setup(
                            Buran.position.x,
                            Buran.position.y,
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
            if (Buran.position.dst(asteroid.getPosition()) < asteroid.getHeight() * asteroid.getScaleX()) {
                if (!asteroid.isFragmented()) {
                    asteroid.destroyWithEffect();
                    Vibrator.vibrateGame(VConstants.PLAYER_ASTEROID_DESTROY);
                    player.getLifeShield().blink(1.0f);
                    ObjectPool.EMITTERS.get(asteroid.getClass())
                            .boom(asteroid.getPosition(), PlayerInteractionLevels.NONE);
                    Standing.getInstance().takeDamage(player.getShieldLevel().getLevel() + 1);
                    GameInfoSystem.setup(
                            Buran.position.x, Buran.position.y,
                            GameInfoSystem.Preset.SCREECH,
                            3.0f,
                            true
                    );
                    boomBox.playSound(MSConstants.DESTROY_ASTEROID);
                } else {
                    player.getRamShield().blink(0.5f);
                    switch (asteroid.getType()) {
                        case ASTEROID_METAL:
                            Standing.getInstance().changeRamPower(asteroid.getSizeForHarvest() * Buffs.getAsteroidSizeFactor());
                            break;
                        case ASTEROID_MINERAL:
                            Standing.getInstance().changeLaserChargeLevel(asteroid.getSizeForHarvest());
                            break;
                    }
                    //pretty soundVolume if collect much fragments in little time
                    prettyFragmentHarvest.playSound(MSConstants.GET_FRAGMENT);
                    Standing.getInstance().addToStatistics(asteroid.getType(), asteroid.getLevel());
                    asteroid.destroy();
                }
            }

            for (int j = 0; j < corrosiveAnomalyEmitter.getActiveList().size(); j++) {
                CorrosiveAnomaly anomaly = (CorrosiveAnomaly) corrosiveAnomalyEmitter.getActiveList().get(j);
                if (asteroid.isActive() && !asteroid.isFragmented() && anomaly.getPosition().dst(asteroid.getPosition()) < PaintConstants.ANOMALY_CORROSIVE_WIDTH / 2) {
                    asteroid.destroyWithEffect();
                    ObjectPool.EMITTERS.get(asteroid.getClass())
                            .boom(asteroid.getPosition(), PlayerInteractionLevels.NONE);
                    GameInfoSystem.setup(anomaly.getPosition().x, anomaly.getPosition().y, GameInfoSystem.Preset.BOOM, 3.0f, false);
                    if (anomaly.getLevel() == 0) {
                        anomaly.showSprayBoom();
                        anomaly.destroy();
                    } else {
                        anomaly.takeDamage(1);
                    }
                }
            }

            for (int k = 0; k < consumableJoiner.size(); k++) {
                AbstractConsumable consumable = consumableJoiner.get(k);
                if (asteroid.isActive() && !asteroid.isFragmented() && consumable.getPosition().dst(asteroid.getPosition()) < 128) {
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
                switch (asteroid.getType()) {
                    case ASTEROID_METAL:
                        ray.setPreset(Ray.Preset.GREY);
                        break;
                    case ASTEROID_MINERAL:
                        ray.setPreset(Ray.Preset.MAGENTA);
                        break;
                }
                ray.setHarvestVelocity(Rules.PLAYER_RAY_FRAGMENT_HARVEST_VELOCITY);
                ray.setTakeRange(Rules.PLAYER_FRAGMENT_TAKE_RANGE);
                ray.harvest(asteroid, dt);
            } else {
                if (Buran.isLaser) {
                    ray.setCount(5);
                    ray.setSize(3);
                    ray.setPreset(Ray.Preset.RED);
                    ray.laser(asteroid, dt);
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
        ((OrthographicCamera) camera).zoom = Rules.VIEW_ZOOM;
        float halfW = Rules.VIEW_WIDTH / 2f;
        float halfH = Rules.VIEW_HEIGHT / 2f;
        // The world is seen through the window above the console: from the camera centre the
        // window reaches halfH up and (halfH - console) down.
        float console = Rules.hudFraction() * Rules.VIEW_HEIGHT;
        float down = halfH - console;
        // Stay mostly inside the map, but peek a quarter view past the edge so the border is seen...
        float peekX = Rules.VIEW_WIDTH * 0.25f;
        float peekY = (Rules.VIEW_HEIGHT - console) * 0.25f;
        camera.position.x = MathUtils.clamp(camera.position.x, halfW - peekX, Rules.SPACE_WIDTH - halfW + peekX);
        camera.position.y = MathUtils.clamp(camera.position.y, down - peekY, Rules.SPACE_HEIGHT - halfH + peekY);
        // ...but never lose the ship: past the edge the camera follows it out, inside the window.
        float marginX = halfW * 0.6f;
        float windowHalf = (Rules.VIEW_HEIGHT - console) / 2f;
        float windowCentre = console / 2f;
        camera.position.x = MathUtils.clamp(camera.position.x, Buran.position.x - marginX, Buran.position.x + marginX);
        camera.position.y = MathUtils.clamp(camera.position.y,
                Buran.position.y - windowCentre - windowHalf * 0.6f, Buran.position.y - windowCentre + windowHalf * 0.6f);
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
        float dst = Buran.position.dst(anomaly.getPosition());
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
        if (Buran.velocity.len() <= Buffs.getPlayerEnginePower()) {
            Standing.getInstance().changeRamPower(-1.0f * abstractAnomaly.getLevel() / Rules.PLAYER_RAM_POWER_DECREASE_FACTOR);
        }
        abstractAnomaly.showSprayCollision();
        player.killPlusPlus(abstractAnomaly);
    }

    private void damageToPlayer(AbstractPoolable anomaly) {
        if (anomaly.getLevel() >= 3) {
            if (Radar.isActive) {
                boomBox.playSound(MSConstants.LOSE_RADAR);
                Radar.isActive = false;
                CircleRenderer.scan = false;
            }
        }

        if (anomaly.getLevel() == 4) {
            Buran.isLaser = false;
        }

        if (player.addZigZag(0.5f * anomaly.getLevel() + 0.2f)) {
            Standing.getInstance().takeDamage(anomaly.getLevel() + 1);
            GameInfoSystem.setup(
                    Buran.position.x,
                    Buran.position.y,
                    GameInfoSystem.Preset.BRAKED,
                    5.0f,
                    false
            );
            tmp1V2.set(Buran.position).sub(anomaly.getPosition()).nor().scl(300 + anomaly.getLevel() * 50);
            Buran.velocity.add(tmp1V2);
            Standing.getInstance().setRamPower(0.0f);
            tmp1V2.scl(-1);
            anomaly.getVelocity().add(tmp1V2);
        }
    }

    /** Expanding cyan ring for the EMP pulse. */
    private void firePulseEffect() {
        boomBox.playSound(MSConstants.GET_SCANNER);
        Shake.getInstance().shakeCamera(0.25f, 35, 12);
        float duration = 0.45f;
        float speed = Rules.PULSE_RANGE / duration;
        for (int i = 0; i < 90; i++) {
            float a = i * MathUtils.PI2 / 90;
            particleEmitter.setup(Buran.position.x, Buran.position.y,
                    MathUtils.cos(a) * speed, MathUtils.sin(a) * speed,
                    duration, 3.0f, 1.0f,
                    0.4f, 0.95f, 1.0f, 0.9f,
                    0.4f, 0.95f, 1.0f, 0.0f);
        }
        // The pulse shorts out electric anomalies and stuns the rest (they lose the scent).
        float r2 = Rules.PULSE_RANGE * Rules.PULSE_RANGE;
        boolean killed = false;
        for (int i = electricalAnomalyEmitter.getActiveList().size() - 1; i >= 0; i--) {
            AbstractAnomaly a = electricalAnomalyEmitter.getActiveList().get(i);
            if (a.isBorn() && !a.isTaken() && a.isActive() && a.getPosition().dst2(Buran.position) <= r2) {
                a.destroyWithEffect();
                player.killPlusPlus(a);
                killed = true;
            }
        }
        for (int i = 0; i < anomalyJoiner.size(); i++) {
            AbstractAnomaly a = anomalyJoiner.get(i);
            if (a.isActive() && a.getPosition().dst2(Buran.position) <= r2) {
                a.setHungry(false);
            }
        }
        if (killed) {
            boomBox.playSound(MSConstants.DESTROY_CORROSIVE_ANOMALY);
            Juice.flash(0.25f, 0.45f, 0.9f, 1f);
        }
    }

    /**
     * Everything below is static or lives on enum singletons, so it survives from one game
     * to the next unless reset here.
     */
    private void resetSessionState() {
        for (Sticker crew : Sticker.ASTRONAUTS) {
            crew.setCoefficient(Rules.STANDARD_COEFFICIENT);
        }
        Buran.isLaser = false;
        Standing.getInstance().setLaserChargeLevel(0.0f);
        PrettySound.clear();
    }

    /** Scene buffer: 8-bit channels (RGB565 banded the dark gradients), rebuilt when the window changes. */
    private void rebuildFbo(int width, int height) {
        if (width <= 0 || height <= 0) {
            return;
        }
        if (fbo != null) {
            if (fbo.getWidth() == width && fbo.getHeight() == height) {
                return;
            }
            fbo.dispose();
        }
        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
        fboTexRegion = new TextureRegion(fbo.getColorBufferTexture());
        fboTexRegion.flip(false, true);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        if (fbo != null) {
            rebuildFbo(width, height);
        }
    }

    /** The app lost focus (hidden tab, Xbox guide): pause the game instead of letting it run blind. */
    @Override
    public void pause() {
        if (!Rules.GAME_OVER && !PauseGroup.isPause()) {
            PauseGroup.togglePause();
        }
        super.pause();
    }

    @Override
    public void dispose() {
        super.dispose();
        if (fbo != null) {
            fbo.dispose();
            fbo = null;
        }
        if (circleRenderer != null) {
            circleRenderer.dispose();
        }
        if (sectorBorder != null) {
            sectorBorder.dispose();
            sectorBorder = null;
        }
        if (threatTextures != null) {
            threatTextures.dispose();
            threatTextures = null;
        }
        if (spaceBackground != null) {
            spaceBackground.dispose();
            spaceBackground = null;
        }
        if (debris != null) {
            debris.dispose();
            debris = null;
        }
        if (postProcessor != null) {
            postProcessor.dispose();
            postProcessor = null;
        }
        global.dispose();
        ShaderManager.PAUSE.dispose();
        ShaderManager.BLACKHOLE.dispose();
        if (!Rules.GAME_OVER) {
            profiles.dispose();
        }
    }

    @Override
    public void resume() {
        if (!Rules.GAME_OVER) {
            if (Rules.DEATH_MODE) {
                if (PauseGroup.isPause()) {
                    boomBox.resumeThisMusic(MSConstants.PAUSE);
                } else {
                    boomBox.resumeThisMusic(MSConstants.DEATH_MODE);
                    boomBox.resumeThisMusic(MSConstants.BLACKHOLE_ROAR_MUSIC);
                }
            } else {
                if (PauseGroup.isPause()) {
                    boomBox.resumeThisMusic(MSConstants.PAUSE);
                } else {
                    boomBox.resumeThisMusic(MSConstants.GAME_SONG);
                    boomBox.resumeThisMusic(MSConstants.BLACKHOLE_ROAR_MUSIC);
                }
            }
        } else {
            boomBox.resumeThisMusic(MSConstants.GAME_OVER);
        }
    }

    private void calcAdsDelay(float dt) {
        if (Rules.ADS_ENABLED) {
            if (profiles.getActive().getPreferences().getRunTimes() >= Rules.ADS_RUNTIME_DEATH_MODE && Rules.DEATH_MODE) {
                adsDelay += dt;
                if (adsDelay >= Rules.ADS_DELAY) {
                    adsDelay = 0.0f;
                    ScreenManager.getInstance().getAdsController().showBannerAd(false);
                }
            } else if (profiles.getActive().getPreferences().getRunTimes() >= Rules.ADS_RUNTIME_GAME) {
                adsDelay += dt;
                if (adsDelay >= Rules.ADS_DELAY) {
                    adsDelay = 0.0f;
                    ScreenManager.getInstance().getAdsController().showBannerAd(false);
                }
            }
        }
    }
}