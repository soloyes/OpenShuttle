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
import com.buran.game.player.Buran;

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
    private com.buran.game.ui.GameInfoSystem gameInfoSystem;
    private com.buran.game.ui.Joystick joystick;
    private com.buran.game.ui.PlayerPointer playerPointer;
    @Getter
    private com.buran.game.bot.blackhole.Blackhole blackhole;
    private FrameBuffer fbo;
    private TextureRegion fboTexRegion;
    private OrthographicCamera cameraHelper;
    private com.buran.game.ui.groups.mvc.results.DetailedResultGroup detailedResultGroup;
    private com.buran.game.ui.groups.PauseGroup pauseGroup;
    private com.buran.game.effects.Ray ray;
    private com.buran.game.effects.PrettySound prettyCorrosiveKick;
    private com.buran.game.effects.PrettySound prettyElectricKick;
    private com.buran.game.effects.PrettySound prettyFragmentHarvest;
    private float holeTime;
    private float fadeInAlpha;
    private boolean gameOverDeactivate;
    private com.buran.game.utils.ListOfListsJoiner<AbstractConsumable> consumableJoiner;
    private com.buran.game.utils.ListOfListsJoiner<AbstractAsteroid> asteroidsJoiner;
    private com.buran.game.utils.ListOfListsJoiner<AbstractAnomaly> anomalyJoiner;
    @Getter
    private UITopPanel uiTopPanel;
    private com.buran.game.player.Standing standing;
    private com.buran.game.ui.groups.mvc.profiles.Profiles profiles;

    private com.buran.game.bot.asteroids.MineralAsteroidEmitter mineralAsteroidEmitter;
    private com.buran.game.bot.asteroids.MetalAsteroidEmitter metalAsteroidEmitter;
    private com.buran.game.bot.anomaly.CorrosiveAnomalyEmitter corrosiveAnomalyEmitter;
    private com.buran.game.bot.anomaly.ElectricalAnomalyEmitter electricalAnomalyEmitter;
    private com.buran.game.bot.anomaly.MagneticAnomalyEmitter magneticAnomalyEmitter;
    private com.buran.game.bot.anomaly.RadiationAnomalyEmitter radiationAnomalyEmitter;
    private com.buran.game.bot.consumable.AstronautEmitter astronautEmitter;
    private com.buran.game.bot.consumable.CrateEmitter crateEmitter;
    private com.buran.game.bot.consumable.StorageEmitter storageEmitter;
    private com.buran.game.bot.consumable.FuelEmitter fuelEmitter;
    private com.buran.game.bot.rocket.RocketEmitter rocketEmitter;
    private com.buran.game.effects.particle.ParticleEmitter particleEmitter;
    private com.buran.game.bot.rocket.RocketLauncher rocketLauncher;
    private com.buran.game.effects.AsteroidLaunchEmitter asteroidLaunchEmitter;
    private TextureRegion nebulaRegion;
    private com.buran.game.effects.Oscillator nebulaAOscillator;
    private float nebulaA = 0.5f;
    private int nebulaRegionX, nebulaRegionY;

    public GameScreen(SpriteBatch batch, Camera camera) {
        super(batch);
        this.batch = batch;
        this.camera = camera;
        this.tmp1V2 = new Vector2(0, 0);
        this.tmp1V3 = new Vector3(0, 0, 0);
        this.cameraHelper = new OrthographicCamera(com.buran.game.conf.Rules.WORLD_WIDTH, com.buran.game.conf.Rules.WORLD_HEIGHT);
    }

    @Override
    public void show() {
        super.show();
        standing = com.buran.game.player.Standing.getInstance();
        fadeInAlpha = 0.0f;
        gameOverDeactivate = false;

        particleEmitter = new com.buran.game.effects.particle.ParticleEmitter();
        mineralAsteroidEmitter = new com.buran.game.bot.asteroids.MineralAsteroidEmitter(particleEmitter);
        metalAsteroidEmitter = new com.buran.game.bot.asteroids.MetalAsteroidEmitter(particleEmitter);
        corrosiveAnomalyEmitter = new com.buran.game.bot.anomaly.CorrosiveAnomalyEmitter(particleEmitter);
        electricalAnomalyEmitter = new com.buran.game.bot.anomaly.ElectricalAnomalyEmitter(particleEmitter);
        magneticAnomalyEmitter = new com.buran.game.bot.anomaly.MagneticAnomalyEmitter(particleEmitter);
        radiationAnomalyEmitter = new com.buran.game.bot.anomaly.RadiationAnomalyEmitter(particleEmitter);
        astronautEmitter = new com.buran.game.bot.consumable.AstronautEmitter(particleEmitter);
        crateEmitter = new com.buran.game.bot.consumable.CrateEmitter(particleEmitter);
        storageEmitter = new com.buran.game.bot.consumable.StorageEmitter(particleEmitter);
        fuelEmitter = new com.buran.game.bot.consumable.FuelEmitter(particleEmitter);
        rocketEmitter = new com.buran.game.bot.rocket.RocketEmitter();
        asteroidLaunchEmitter = new com.buran.game.effects.AsteroidLaunchEmitter();

        boomBox.stopAllMusicAndPlayThese(com.buran.game.conf.MSConstants.GAME_SONG, com.buran.game.conf.MSConstants.BLACKHOLE_ROAR_MUSIC);
        fbo = new FrameBuffer(Pixmap.Format.RGB565, Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight(), false);
        fboTexRegion = new TextureRegion(fbo.getColorBufferTexture());
        fboTexRegion.flip(false, true);

        com.buran.game.utils.ShaderManager.PAUSE.load();
        com.buran.game.utils.ShaderManager.BLACKHOLE.load();

        background = new Background();
        gameInfoSystem = new com.buran.game.ui.GameInfoSystem();
        joystick = new com.buran.game.ui.Joystick();

        blackhole = new com.buran.game.bot.blackhole.Blackhole();
        player = new Buran(joystick, particleEmitter);
        uiTopPanel = new UITopPanel();

        com.buran.game.utils.BoundCheck.avoidPosition(com.buran.game.bot.blackhole.Blackhole.position, Buran.position, (int) com.buran.game.conf.Rules.BLACKHOLE_TAKE_RANGE + 50, 0, com.buran.game.conf.Rules.SPACE_WIDTH, 0, com.buran.game.conf.Rules.SPACE_HEIGHT);
        playerPointer = new com.buran.game.ui.PlayerPointer(player);
        com.buran.game.conf.Rules.StartLevel.values()[com.buran.game.ui.groups.mvc.profiles.Profiles.getSettings().getLevel()].setGameLevel();

        InputMultiplexer im = new InputMultiplexer(player, stage, joystick);
        Gdx.input.setInputProcessor(im);

        calcMusicEffectVolume();
        prettyCorrosiveKick = com.buran.game.effects.PrettySound.getPretty(com.buran.game.conf.Rules.ANOMALY_KICK_PRETTY_SOUND_DELAY);
        prettyElectricKick = com.buran.game.effects.PrettySound.getPretty(com.buran.game.conf.Rules.ANOMALY_KICK_PRETTY_SOUND_DELAY);
        prettyFragmentHarvest = com.buran.game.effects.PrettySound.getPretty(com.buran.game.conf.Rules.FRAGMENT_KICK_PRETTY_SOUND_DELAY);
        ray = new com.buran.game.effects.Ray(particleEmitter, mineralAsteroidEmitter.getActiveList(), metalAsteroidEmitter.getActiveList());
        //todo: а тут что?
        consumableJoiner = com.buran.game.utils.ListOfListsJoiner.newAbstractConsumableList(astronautEmitter.getActiveList(), crateEmitter.getActiveList(), storageEmitter.getActiveList(), fuelEmitter.getActiveList());
        asteroidsJoiner = com.buran.game.utils.ListOfListsJoiner.newAbstractAsteroidList(mineralAsteroidEmitter.getActiveList(), metalAsteroidEmitter.getActiveList());
        anomalyJoiner = com.buran.game.utils.ListOfListsJoiner.newAbstractAnomalyList(corrosiveAnomalyEmitter.getActiveList(), electricalAnomalyEmitter.getActiveList(), radiationAnomalyEmitter.getActiveList(), magneticAnomalyEmitter.getActiveList());
        //
        rocketLauncher = new com.buran.game.bot.rocket.RocketLauncher(rocketEmitter, anomalyJoiner);
        storageEmitter.emit();
        this.profiles = new com.buran.game.ui.groups.mvc.profiles.Profiles();
        this.nebulaRegion = com.buran.game.utils.Assets.getInstance().getAtlas().findRegion(com.buran.game.conf.PaintConstants.CIRCLE_GLOW);
        nebulaRegionX = MathUtils.random(0, com.buran.game.conf.Rules.SPACE_WIDTH - com.buran.game.conf.Rules.WORLD_WIDTH);
        nebulaRegionY = MathUtils.random(0, com.buran.game.conf.Rules.SPACE_HEIGHT - com.buran.game.conf.Rules.WORLD_HEIGHT);
        this.nebulaAOscillator = new com.buran.game.effects.Oscillator();
    }

    private void calcMusicEffectVolume() {
        float v;
        if (Buran.position.dst(com.buran.game.bot.blackhole.Blackhole.position) >= com.buran.game.conf.Rules.BLACKHOLE_ROAR_RANGE) {
            v = 0.0f;
        } else if (Buran.position.dst(com.buran.game.bot.blackhole.Blackhole.position) <= com.buran.game.conf.Rules.BLACKHOLE_TAKE_RANGE) {
            v = 0.5f;
        } else {
            v = 1 - Buran.position.dst(com.buran.game.bot.blackhole.Blackhole.position) / com.buran.game.conf.Rules.BLACKHOLE_ROAR_RANGE;
        }
        boomBox.setMusicEffectVolume(com.buran.game.conf.MSConstants.BLACKHOLE_ROAR_MUSIC, v);
    }

    @Override
    public void createGUI() {
        // Workaround (Android restarts via app manager)
        com.buran.game.ui.groups.configs.RightGroup.doStart();
        com.buran.game.ui.groups.configs.LeftGroup.doStart();
        com.buran.game.ui.groups.selector.SelectorGroup.doStart();
        com.buran.game.ui.groups.switches.SwitchGroup.doStart();
        UILeftPanel.doStart();
        UIRightPanel.doStart();
        //
        detailedResultGroup = new com.buran.game.ui.groups.mvc.results.DetailedResultGroup();
        pauseGroup = new com.buran.game.ui.groups.PauseGroup();
        stage.addActor(detailedResultGroup);
        stage.addActor(pauseGroup);
        stage.addActor(com.buran.game.ui.groups.selector.SelectorGroup.getInstance());
        stage.addActor(com.buran.game.ui.groups.switches.SwitchGroup.getInstance());
    }

    @Override
    public void render(float dt) {
        FitViewport v = (FitViewport) ScreenManager.getInstance().getViewport();
        //FBO BEGIN
        fbo.begin();
        super.render(dt);
        update(dt);
        batch.setProjectionMatrix(camera.combined);
        if (com.buran.game.ui.groups.PauseGroup.isPause() || com.buran.game.conf.Rules.GAME_OVER) {
            batch.setShader(com.buran.game.utils.ShaderManager.PAUSE.getProgram());
        } else {
            batch.setShader(null);
        }
        //BEGIN
        batch.begin();
        background.render(batch);
        if (!com.buran.game.ui.groups.mvc.profiles.Profiles.getSettings().isShader() || com.buran.game.utils.ShaderManager.BLACKHOLE.getProgram() == null) {
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
        asteroidLaunchEmitter.render(batch);
        Color color = batch.getColor();
        color.a = nebulaA;
        batch.setColor(color);
        batch.draw(nebulaRegion,
                nebulaRegionX, nebulaRegionY,
                com.buran.game.conf.Rules.WORLD_WIDTH, (3 * com.buran.game.conf.Rules.WORLD_HEIGHT / 4)
        );
        batch.setColor(Color.WHITE);
        batch.end();

        float shaderHoleX = (com.buran.game.bot.blackhole.Blackhole.position.x - camera.position.x + com.buran.game.conf.Rules.WORLD_WIDTH / 2) / com.buran.game.conf.Rules.WORLD_WIDTH;
        float shaderHoleY = (com.buran.game.bot.blackhole.Blackhole.position.y - camera.position.y + com.buran.game.conf.Rules.WORLD_HEIGHT / 2) / com.buran.game.conf.Rules.WORLD_HEIGHT;

        //
        ScreenManager.getInstance().resetCamera();
        //FBO END
        fbo.end(v.getScreenX(), v.getScreenY(), v.getScreenWidth(), v.getScreenHeight());

        //BEGIN
        batch.begin();
        super.render(dt);
        if (!com.buran.game.ui.groups.mvc.profiles.Profiles.getSettings().isShader() || com.buran.game.utils.ShaderManager.BLACKHOLE.getProgram() == null) {
            batch.setShader(null);
            //BEGIN
            batch.draw(fboTexRegion, 0, 0, com.buran.game.conf.Rules.WORLD_WIDTH, com.buran.game.conf.Rules.WORLD_HEIGHT);
        } else {
            batch.setShader(com.buran.game.utils.ShaderManager.BLACKHOLE.getProgram());
            com.buran.game.utils.ShaderManager.BLACKHOLE.getProgram().setUniformf("resolution", com.buran.game.conf.Rules.WORLD_WIDTH, com.buran.game.conf.Rules.WORLD_HEIGHT);
            if (!com.buran.game.ui.groups.PauseGroup.isPause()) {
                holeTime += dt;
            }
            holeTime = holeTime % (MathUtils.PI2);
            com.buran.game.utils.ShaderManager.BLACKHOLE.getProgram().setUniformf("time", holeTime);
            com.buran.game.utils.ShaderManager.BLACKHOLE.getProgram().setUniformf("center", shaderHoleX, shaderHoleY);
            com.buran.game.utils.ShaderManager.BLACKHOLE.getProgram().setUniformf("dmax", blackhole.getShaderDiameter());

            if (com.buran.game.ui.groups.PauseGroup.isPause() || com.buran.game.conf.Rules.GAME_OVER) {
                com.buran.game.utils.ShaderManager.BLACKHOLE.getProgram().setUniformf("isGrey", 1.0f);
            } else {
                com.buran.game.utils.ShaderManager.BLACKHOLE.getProgram().setUniformf("isGrey", -1.0f);
            }
            batch.draw(fboTexRegion, 0, 0, com.buran.game.conf.Rules.WORLD_WIDTH, com.buran.game.conf.Rules.WORLD_HEIGHT);
        }
        //batch.end();

        if (com.buran.game.ui.groups.PauseGroup.isPause()) {
            batch.setShader(com.buran.game.utils.ShaderManager.PAUSE.getProgram());
            com.buran.game.utils.ShaderManager.PAUSE.getProgram().setUniformf("resolution", com.buran.game.conf.Rules.WORLD_WIDTH, com.buran.game.conf.Rules.WORLD_HEIGHT);
        } else {
            batch.setShader(null);
        }

        //BEGIN
        if (!com.buran.game.ui.groups.PauseGroup.isPause() && !com.buran.game.conf.Rules.GAME_OVER) {
            UILeftPanel.getInstance().render(batch);
            UIRightPanel.getInstance().render(batch);
        }

        if (!com.buran.game.conf.Rules.GAME_OVER) {
            uiTopPanel.render(batch);
            com.buran.game.ui.groups.selector.SelectorGroup.getInstance().render(batch);
        }

        if (!com.buran.game.ui.groups.PauseGroup.isPause()) {
            joystick.render(batch);
        }

        Color tmpColor = batch.getColor();
        if (com.buran.game.conf.Rules.GAME_OVER) {
            pauseGroup.setGameOver();
            fadeInAlpha += dt / 2;
            if (fadeInAlpha >= 1.0f) {
                fadeInAlpha = 1.0f;
            }
            detailedResultGroup.setColor(tmpColor.r, tmpColor.g, tmpColor.b, fadeInAlpha);
            if (!detailedResultGroup.isVisible()) {
                boomBox.stopAllMusicAndPlayThis(com.buran.game.conf.MSConstants.GAME_OVER);
                detailedResultGroup.setOver();
                profiles.dispose();
                detailedResultGroup.init();
                if (com.buran.game.conf.Rules.ADS_ENABLED) {
                    ScreenManager.getInstance().getAdsController().showBannerAd();
                }
            }
        }
        batch.end();

        batch.setProjectionMatrix(cameraHelper.combined);
        //BEGIN
        batch.begin();
        if (!com.buran.game.conf.Rules.GAME_OVER) {
            gameInfoSystem.render(batch);
            playerPointer.render(batch);
        }
        batch.end();

        batch.setShader(null);
        stage.draw();
    }

    public void update(float dt) {
        cameraUpdate(dt);
        if (!com.buran.game.ui.groups.PauseGroup.isPause()) {
            com.buran.game.effects.PrettySound.update(dt);
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
            if (!com.buran.game.conf.Rules.GAME_OVER) {
                uiTopPanel.update(dt);
                com.buran.game.player.Standing.getInstance().addTime(dt);
            }

            gameInfoSystem.update(dt);
            playerPointer.update(dt);
            if (!com.buran.game.conf.Rules.GAME_OVER) {
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
            if (!com.buran.game.player.navigation.Radar.isActive) {
                nebulaA = nebulaAOscillator.oscillate(nebulaA, 0.03f, 0.5f, 1.0f, dt);
            } else {
                nebulaA -= dt;
                if (nebulaA <= 0.0f) {
                    nebulaA = 0.0f;
                }
            }

        }
        cameraUpdate(dt);
        if (!com.buran.game.ui.groups.PauseGroup.isPause() && !com.buran.game.conf.Rules.GAME_OVER) {
            com.buran.game.effects.Shake.getInstance().update(dt, camera);
            standing.update(dt);
            com.buran.game.ui.groups.selector.SelectorGroup.getInstance().update(dt);
            UIRightPanel.getInstance().update(dt);
        }
        stage.act();
    }

    //It is works, somehow.
    //Cam position is reset when fbo.end(), copy-paste from cameraHelper is the hack.
    private void cameraUpdate(float dt) {
        if (player.isCameraSmooth()) {
            tmp1V3.set(Buran.position, 0.0f).sub(cameraHelper.position.x, cameraHelper.position.y, 0.0f).nor();
            if (!com.buran.game.ui.groups.PauseGroup.isPause()) {
                cameraHelper.position.mulAdd(tmp1V3, com.buran.game.conf.Rules.CAMERA_SMOOTH_VELOCITY * dt);
            }
            camera.position.set(cameraHelper.position);
            if (
                //parking
                    tmp1V2.set(Buran.position).sub(
                            cameraHelper.position.x, cameraHelper.position.y).len2() <
                            tmp1V3.scl(com.buran.game.conf.Rules.CAMERA_SMOOTH_VELOCITY * dt).len2()
                    ) {
                player.setCameraSmooth(false);
            }
        } else {
            camera.position.set(Buran.position.x, Buran.position.y, 0);
            cameraHelper.position.set(Buran.position.x, Buran.position.y, 0);
        }
        cameraUpdater(cameraHelper);
        cameraUpdater(camera);
    }

    private void checkCollision(float dt) {
        player.collideBlackhole(dt);
        calcMusicEffectVolume();

        boolean anyAtHarvestPoint = false;
        for (int i = 0; i < consumableJoiner.size(); i++) {
            com.buran.game.bot.consumable.AbstractConsumable consumable = consumableJoiner.get(i);
            if (consumable.isTaken()) {
                continue;
            }
            if (Buran.position.dst(consumable.getPosition()) <= com.buran.game.conf.PaintConstants.SHUTTLE_WIDTH / 2) {
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
                            c = com.buran.game.conf.Rules.PLAYER_DAMAGE_DONE_GOLDEN_FACTOR;
                        }
                        if (consumable instanceof com.buran.game.bot.consumable.Crate) {
                            a = com.buran.game.conf.Rules.PLAYER_DAMAGE_DONE_CRATE;
                        }
                        if (consumable instanceof com.buran.game.bot.consumable.fuel.Fuel) {
                            com.buran.game.effects.Shake.getInstance().shakeCamera(1, 35, 50);
                            a = com.buran.game.conf.Rules.PLAYER_DAMAGE_DONE_FUEL;
                        }
                        if (consumable instanceof com.buran.game.bot.consumable.Storage) {
                            a = com.buran.game.conf.Rules.PLAYER_DAMAGE_DONE_STORAGE;
                        }
                        if (consumable instanceof com.buran.game.bot.consumable.Astronaut) {
                            a = com.buran.game.conf.Rules.PLAYER_DAMAGE_DONE_ASTRONAUT;
                        }
                        com.buran.game.player.Standing.getInstance().changeDmgDone(c * a);
                        player.badEffect(consumable);
                    }
                }
            } else {
                consumable.setHarvested(false);
            }
            ray.setCount(24);
            ray.setSize(4);
            ray.setHarvestVelocity(com.buran.game.conf.Rules.PLAYER_RAY_CONSUMABLE_HARVEST_VELOCITY);
            ray.setTakeRange(com.buran.game.conf.Rules.PLAYER_CONSUMABLE_TAKE_RANGE);
            if (player.getShieldLevel().getLevel() < PlayerInteractionLevels.GREEN.getLevel() || consumable.isShielded()) {
                ray.setPreset(com.buran.game.effects.Ray.Preset.GREEN);
            } else {
                if (player.canCatch(consumable)) {
                    ray.setPreset(com.buran.game.effects.Ray.Preset.GREEN);
                } else {
                    ray.setPreset(com.buran.game.effects.Ray.Preset.RED);
                }
            }
            ray.harvest(consumable, dt);
            anyAtHarvestPoint |= consumable.isAtRayPoint();
        }

        player.showTakeRangeField(dt, anyAtHarvestPoint);

        for (int i = 0; i < corrosiveAnomalyEmitter.getActiveList().size(); i++) {
            com.buran.game.bot.anomaly.CorrosiveAnomaly anomaly = (com.buran.game.bot.anomaly.CorrosiveAnomaly) corrosiveAnomalyEmitter.getActiveList().get(i);
            collideWithRocket(anomaly);
            if (anomaly.isTaken()) {
                continue;
            }
            if (Buran.position.dst(anomaly.getPosition()) <= com.buran.game.conf.PaintConstants.ANOMALY_CORROSIVE_WIDTH / 2 && anomaly.isBorn()) {
                if (anomaly.getLevel() < player.getPushPower() || Buran.velocity.len() >= com.buran.game.conf.Rules.PLAYER_TURBO_THRESHOLD_VELOCITY) {
                    if (Buran.velocity.len() >= com.buran.game.conf.Rules.PLAYER_TURBO_THRESHOLD_VELOCITY) {
                        boomBox.playSound(com.buran.game.conf.MSConstants.BURST_CORROSIVE_ANOMALY);
                    } else {
                        boomBox.playSound(com.buran.game.conf.MSConstants.DESTROY_CORROSIVE_ANOMALY);
                    }
                    damageToAnomaly(anomaly);
                    com.buran.game.effects.Vibrator.vibrate(com.buran.game.conf.VConstants.PLAYER_ANOMALY_CORROSIVE_DESTROY, anomaly.getLevel());
                } else {
                    damageToPlayer(anomaly);
                    if (anomaly.getLevel() != 4) {
                        anomaly.takeDamage(player.getPushPower());
                    }
                    anomaly.setHungry(false);
                    anomaly.setSlimTimer(0);
                    prettyCorrosiveKick.playSound(com.buran.game.conf.MSConstants.KICKED_BY_CORROSIVE);
                    com.buran.game.effects.Vibrator.vibrate(com.buran.game.conf.VConstants.PLAYER_ANOMALY_CORROSIVE_COLLISION);
                }
            }
        }

        for (int i = 0; i < electricalAnomalyEmitter.getActiveList().size(); i++) {
            com.buran.game.bot.anomaly.ElectricalAnomaly anomaly = (com.buran.game.bot.anomaly.ElectricalAnomaly) electricalAnomalyEmitter.getActiveList().get(i);
            if (anomaly.isTaken()) {
                continue;
            }
            collideWithRocket(anomaly);
            if (Buran.position.dst(anomaly.getPosition()) < com.buran.game.conf.PaintConstants.ANOMALY_ELECTRIC_WIDTH / 2 && anomaly.isBorn()) {
                anomaly.setHungry(false);
                damageToPlayer(anomaly);
                com.buran.game.effects.Vibrator.vibrate(com.buran.game.conf.VConstants.PLAYER_ANOMALY_CORROSIVE_DESTROY, anomaly.getLevel());
                prettyElectricKick.playSound(com.buran.game.conf.MSConstants.KICKED_BY_ELECTRIC);
                anomaly.setV(anomaly.getV() * com.buran.game.conf.Rules.ANOMALY_ELECTRIC_DECREASE_FACTOR);
            }
        }

        //
        boolean tmp1 = false;
        for (int i = 0; i < radiationAnomalyEmitter.getActiveList().size(); i++) {
            com.buran.game.bot.anomaly.RadiationAnomaly anomaly = (com.buran.game.bot.anomaly.RadiationAnomaly) radiationAnomalyEmitter.getActiveList().get(i);
            if (anomaly.isTaken()) {
                continue;
            }
            collideWithRocket(anomaly);
            if (Buran.position.dst(anomaly.getPosition()) < com.buran.game.conf.PaintConstants.ANOMALY_RADIATION_WIDTH / 3 && anomaly.isBorn()) {
                anomaly.setEat(true);
                anomaly.setGlued(true);
            } else {
                anomaly.setEat(false);
                anomaly.setEatTimer(0.0f);
                anomaly.setGlued(false);
            }

            if (anomaly.isEat()) {
                anomaly.addEatTimer(dt);
                if (anomaly.getEatTimer() >= com.buran.game.conf.Rules.ANOMALIES_RADIATION_EAT_TIME) {
                    anomaly.setEatTimer(0.0f);
                    com.buran.game.player.Standing.getInstance().changeScore(-com.buran.game.conf.Rules.ANOMALIES_RADIATION_SCORE_DECREASE);
                    boomBox.playSound(com.buran.game.conf.MSConstants.LOSE_SCORE);
                    com.buran.game.ui.GameInfoSystem.setup(
                            Buran.position.x,
                            Buran.position.y,
                            "-[RED]" + com.buran.game.ui.GameInfoSystem.Preset.LOST_SCORE.getValue() + "[]",
                            2.0f,
                            true
                    );
                }
            }
            tmp1 |= anomaly.isGlued();
        }
        com.buran.game.player.Standing.getInstance().setScoreAlertRadiationAnomaly(tmp1);

        boolean tmp2 = false;
        for (int i = 0; i < magneticAnomalyEmitter.getActiveList().size(); i++) {
            com.buran.game.bot.anomaly.MagneticAnomaly anomaly = (com.buran.game.bot.anomaly.MagneticAnomaly) magneticAnomalyEmitter.getActiveList().get(i);
            if (anomaly.isTaken()) {
                continue;
            }
            collideWithRocket(anomaly);
            if (Buran.position.dst(anomaly.getPosition()) < com.buran.game.conf.PaintConstants.ANOMALY_MAGNETIC_HEIGHT / 3 && anomaly.isBorn()) {
                anomaly.setEat(true);
                anomaly.setGlued(true);
            } else {
                anomaly.setEat(false);
                anomaly.setEatTimer(0.0f);
                anomaly.setGlued(false);
            }

            if (anomaly.isEat()) {
                anomaly.addEatTimer(dt);
                if (anomaly.getEatTimer() >= com.buran.game.conf.Rules.ANOMALIES_MAGNETIC_EAT_TIME) {
                    anomaly.setEatTimer(0.0f);
                    com.buran.game.player.Standing.getInstance().changeFuel(-com.buran.game.conf.Rules.ANOMALIES_MAGNETIC_FUEL_DECREASE);
                    boomBox.playSound(com.buran.game.conf.MSConstants.LOSE_FUEL);
                    com.buran.game.ui.GameInfoSystem.setup(
                            Buran.position.x,
                            Buran.position.y,
                            "-[RED]" + com.buran.game.ui.GameInfoSystem.Preset.FUEL + "[]",
                            4.0f,
                            true
                    );
                }
            }
            tmp2 |= anomaly.isGlued();
        }
        com.buran.game.player.Standing.getInstance().setFuelAlertMagneticAnomaly(tmp2);
        //

        for (int i = 0; i < asteroidsJoiner.size(); i++) {
            com.buran.game.bot.asteroids.AbstractAsteroid asteroid = asteroidsJoiner.get(i);
            if (asteroid.isTaken()) {
                continue;
            }
            if (Buran.position.dst(asteroid.getPosition()) < asteroid.getHeight() * asteroid.getScaleX()) {
                if (!asteroid.isFragmented()) {
                    asteroid.destroyWithEffect();
                    com.buran.game.effects.Vibrator.vibrate(com.buran.game.conf.VConstants.PLAYER_ASTEROID_DESTROY);
                    player.getLifeShield().blink(1.0f);
                    com.buran.game.utils.pool.ObjectPool.EMITTERS.get(asteroid.getClass())
                            .boom(asteroid.getPosition(), PlayerInteractionLevels.NONE);
                    com.buran.game.player.Standing.getInstance().takeDamage(player.getShieldLevel().getLevel() + 1);
                    com.buran.game.ui.GameInfoSystem.setup(
                            Buran.position.x, Buran.position.y,
                            com.buran.game.ui.GameInfoSystem.Preset.SCREECH,
                            3.0f,
                            true
                    );
                    boomBox.playSound(com.buran.game.conf.MSConstants.DESTROY_ASTEROID);
                } else {
                    player.getRamShield().blink(0.5f);
                    com.buran.game.player.Standing.getInstance().changeRamPower(asteroid.getSizeForHarvest());
                    if (asteroid.getType() == com.buran.game.utils.pool.AbstractPoolable.Type.ASTEROID_MINERAL && Buran.isLaser) {
                        com.buran.game.player.Standing.getInstance().changeLaserChargeLevel(asteroid.getSizeForHarvest());
                    }
                    //pretty soundVolume if collect much fragments in little time
                    prettyFragmentHarvest.playSound(com.buran.game.conf.MSConstants.GET_FRAGMENT);
                    com.buran.game.player.Standing.getInstance().addToStatistics(asteroid.getType(), asteroid.getLevel());
                    asteroid.destroy();
                }
            }

            for (int j = 0; j < corrosiveAnomalyEmitter.getActiveList().size(); j++) {
                com.buran.game.bot.anomaly.CorrosiveAnomaly anomaly = (com.buran.game.bot.anomaly.CorrosiveAnomaly) corrosiveAnomalyEmitter.getActiveList().get(j);
                if (!asteroid.isFragmented() && anomaly.getPosition().dst(asteroid.getPosition()) < com.buran.game.conf.PaintConstants.ANOMALY_CORROSIVE_WIDTH / 2) {
                    asteroid.destroyWithEffect();
                    com.buran.game.utils.pool.ObjectPool.EMITTERS.get(asteroid.getClass())
                            .boom(asteroid.getPosition(), PlayerInteractionLevels.NONE);
                    com.buran.game.ui.GameInfoSystem.setup(anomaly.getPosition().x, anomaly.getPosition().y, com.buran.game.ui.GameInfoSystem.Preset.BOOM, 3.0f, false);
                    if (anomaly.getLevel() == 0) {
                        anomaly.destroyWithEffect();
                    } else {
                        anomaly.takeDamage(1);
                    }
                }
            }

            for (int k = 0; k < consumableJoiner.size(); k++) {
                com.buran.game.bot.consumable.AbstractConsumable consumable = consumableJoiner.get(k);
                if (!asteroid.isFragmented() && consumable.getPosition().dst(asteroid.getPosition()) < 128) {
                    asteroid.destroyWithEffect();
                    if (!consumable.isShielded()) {
                        consumable.destroyWithEffect();
                    }
                    com.buran.game.utils.pool.ObjectPool.EMITTERS.get(asteroid.getClass())
                            .boom(asteroid.getPosition(), PlayerInteractionLevels.NONE);
                    com.buran.game.ui.GameInfoSystem.setup(
                            consumable.getPosition().x,
                            consumable.getPosition().y,
                            com.buran.game.ui.GameInfoSystem.Preset.SAD,
                            3.0f,
                            true
                    );
                }
            }

            //
            if (asteroid.isFragmented()) {
                ray.setCount(12);
                ray.setSize(2);
                ray.setPreset(com.buran.game.effects.Ray.Preset.GREY);
                ray.setHarvestVelocity(com.buran.game.conf.Rules.PLAYER_RAY_FRAGMENT_HARVEST_VELOCITY);
                ray.setTakeRange(com.buran.game.conf.Rules.PLAYER_FRAGMENT_TAKE_RANGE);
                ray.harvest(asteroid, dt);
            } else {
                if (Buran.isLaser) {
                    ray.setCount(5);
                    ray.setSize(3);
                    ray.setPreset(com.buran.game.effects.Ray.Preset.RED);
                    ray.laser(player, asteroid, dt);
                }
            }
            //

        }
        rocketEmitter.checkCollision();
    }

    private void abstractAnomalyDeactivate(List<com.buran.game.bot.anomaly.AbstractAnomaly> anomalies) {
        for (int i = 0; i < anomalies.size(); i++) {
            if (anomalies.get(i).isActivated()) {
                anomalies.get(i).setActivated(false);
            }
        }
    }

    private void cameraUpdater(Camera camera) {
        if (camera.position.x < com.buran.game.conf.Rules.WORLD_WIDTH / 2) {
            camera.position.x = com.buran.game.conf.Rules.WORLD_WIDTH / 2;
        }
        if (camera.position.y < com.buran.game.conf.Rules.WORLD_HEIGHT / 2) {
            camera.position.y = com.buran.game.conf.Rules.WORLD_HEIGHT / 2;
        }
        if (camera.position.x > com.buran.game.conf.Rules.SPACE_WIDTH - com.buran.game.conf.Rules.WORLD_WIDTH / 2) {
            camera.position.x = com.buran.game.conf.Rules.SPACE_WIDTH - com.buran.game.conf.Rules.WORLD_WIDTH / 2;
        }
        if (camera.position.y > com.buran.game.conf.Rules.SPACE_HEIGHT - com.buran.game.conf.Rules.WORLD_HEIGHT / 2) {
            camera.position.y = com.buran.game.conf.Rules.SPACE_HEIGHT - com.buran.game.conf.Rules.WORLD_HEIGHT / 2;
        }
        camera.update();
    }

    private void consumeConsumable(com.buran.game.bot.consumable.AbstractConsumable consumable) {
        consumable.destroy();
        player.takeEffect(consumable);
        if (consumable instanceof com.buran.game.bot.consumable.Astronaut) {
            com.buran.game.bot.consumable.Astronaut astronaut = (com.buran.game.bot.consumable.Astronaut) consumable;
            Sticker profession = astronaut.getProfession();
            com.buran.game.player.Standing.getInstance().changeAstronauts(profession);
            UIRightPanel.getInstance().add(profession);
            com.buran.game.ui.groups.configs.RightGroup.getInstance().updateScroll(astronaut.getLabels().get(Label.Layer.LABEL).get(0).getSticker());
            com.buran.game.ui.groups.configs.ConfigImageButton button = com.buran.game.ui.groups.configs.RightGroup.getInstance().getButtonMap().get(profession);
            if (button != null) {
                button.changeCount(1);
            }
        }
        com.buran.game.player.Standing.getInstance().addToStatistics(consumable.getType(), consumable.getLevel());
    }

    private void collideWithRocket(com.buran.game.bot.anomaly.AbstractAnomaly anomaly) {
        float dst = Buran.position.dst(anomaly.getPosition());
        if (dst <= com.buran.game.conf.Rules.ROCKETS_TAKE_RANGE && anomaly.isBorn() && !anomaly.isTaken()) {
            anomaly.setAtRocketPoint(true);
        } else {
            anomaly.setAtRocketPoint(false);
        }
    }

    private void damageToAnomaly(com.buran.game.bot.anomaly.AbstractAnomaly abstractAnomaly) {
        abstractAnomaly.destroyWithEffect();
        player.getRamShield().blink(1.0f);
        player.slowDown(abstractAnomaly.getLevel() * 50.0f);
        if (Buran.velocity.len() <= com.buran.game.conf.Rules.PLAYER_ENGINE_POWER) {
            com.buran.game.player.Standing.getInstance().changeRamPower(-abstractAnomaly.getLevel());
        }
        abstractAnomaly.showSprayCollision();
        player.killPlusPlus(abstractAnomaly);
        com.buran.game.player.Standing.getInstance().addToStatistics(abstractAnomaly.getType(), abstractAnomaly.getLevel());
    }

    private void damageToPlayer(com.buran.game.utils.pool.AbstractPoolable anomaly) {
        if (anomaly.getLevel() >= 3) {
            if (com.buran.game.player.navigation.Radar.isActive) {
                boomBox.playSound(com.buran.game.conf.MSConstants.LOSE_RADAR);
                com.buran.game.player.navigation.Radar.isActive = false;
            }
        }

        if (player.addZigZag(0.5f * anomaly.getLevel() + 0.2f)) {
            com.buran.game.player.Standing.getInstance().takeDamage(anomaly.getLevel() + 1);
            com.buran.game.ui.GameInfoSystem.setup(
                    Buran.position.x,
                    Buran.position.y,
                    com.buran.game.ui.GameInfoSystem.Preset.BRAKED,
                    5.0f,
                    false
            );
            tmp1V2.set(Buran.position).sub(anomaly.getPosition()).nor().scl(300 + anomaly.getLevel() * 50);
            Buran.velocity.add(tmp1V2);
            com.buran.game.player.Standing.getInstance().setRamPower(0.0f);
            tmp1V2.scl(-1);
            anomaly.getVelocity().add(tmp1V2);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        com.buran.game.utils.ShaderManager.PAUSE.dispose();
        com.buran.game.utils.ShaderManager.BLACKHOLE.dispose();
        if (!com.buran.game.conf.Rules.GAME_OVER) {
            profiles.dispose();
        }
    }

    @Override
    public void resume() {
        if (!com.buran.game.conf.Rules.GAME_OVER && !com.buran.game.ui.groups.PauseGroup.isPause()) {
            boomBox.resumeThisMusic(com.buran.game.conf.MSConstants.GAME_SONG);
            boomBox.resumeThisMusic(com.buran.game.conf.MSConstants.BLACKHOLE_ROAR_MUSIC);
        } else if (com.buran.game.ui.groups.PauseGroup.isPause()) {
            boomBox.resumeThisMusic(com.buran.game.conf.MSConstants.MENU_SONG);
        } else if (com.buran.game.conf.Rules.GAME_OVER) {
            boomBox.resumeThisMusic(com.buran.game.conf.MSConstants.GAME_OVER);
        }
    }
}