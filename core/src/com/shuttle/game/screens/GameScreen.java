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
import com.shuttle.game.bot.blackhole.Blackhole;
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
import com.shuttle.game.bot.asteroids.Asteroid;
import com.shuttle.game.bot.asteroids.AsteroidEmitter;
import com.shuttle.game.Background;
import com.shuttle.game.bot.consumable.Consumable;
import com.shuttle.game.bot.consumable.ConsumableEmitter;
import com.shuttle.game.GameInfoSystem;
import com.shuttle.game.bot.particle.ParticleEmitter;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.screens.group.PauseGroup;
import com.shuttle.game.utils.BoundCheck;
import com.shuttle.game.utils.effects.Ray;
import com.shuttle.game.utils.effects.Shake;
import com.shuttle.game.utils.effects.Vibrator;

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
    private ConsumableEmitter consumableEmitter;
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
    private AsteroidEmitter asteroidEmitter;
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
    private float holeTime;
    private float fadeInAlpha;
    private boolean gameOverDeactivate;

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
        consumableEmitter = new ConsumableEmitter(this);
        asteroidEmitter = new AsteroidEmitter(this);
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
        asteroidEmitter.render(batch);
        player.render(batch);
        consumableEmitter.render(batch);
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
            background.update(dt);
            player.update(dt);
            corrosiveAnomalyEmitter.update(dt);
            electricalAnomalyEmitter.update(dt);
            radiationAnomalyEmitter.update(dt);
            magneticAnomalyEmitter.update(dt);
            consumableEmitter.update(dt);
            asteroidEmitter.update(dt);
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
        collideBlackholePlayer(dt);
        calcMusicEffectVolume();

        for (int i = 0; i < consumableEmitter.getActiveList().size(); i++) {
            Consumable tmpConsumable = consumableEmitter.getActiveList().get(i);
            if (!tmpConsumable.isTaken()) {
                if (player.getPosition().dst(tmpConsumable.getPosition()) <= 128) {
                    if (player.getShieldLevel().getLevel() < GlobalInteractionLevels.GREEN.getLevel()) {
                        tmpConsumable.destroy();
                        player.takeEffect(tmpConsumable.getType());
                        player.getStanding().addToStatistics(tmpConsumable.getType(), tmpConsumable.getLevel());
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
                ray.setCount(24);
                ray.setSize(4);
                ray.setPreset(Ray.Preset.GREEN);
                ray.harvest(player, tmpConsumable, particleEmitter, dt);
            }
            ///
            collideBlackholePoolable(tmpConsumable);
            ///
        }

        for (int i = 0; i < corrosiveAnomalyEmitter.getActiveList().size(); i++) {
            CorrosiveAnomaly tmpAnomaly = (CorrosiveAnomaly) corrosiveAnomalyEmitter.getActiveList().get(i);
            if (!tmpAnomaly.isTaken()) {
                if (player.getPosition().dst(tmpAnomaly.getPosition()) <= PaintConstants.ANOMALY_CORROSIVE_WIDTH / 2 && tmpAnomaly.isBorn()) {
                    if (tmpAnomaly.getLevel() < player.getPushPower()) {
                        damageToAnomaly(tmpAnomaly);
                        boomBox.playSound(MSConstants.ANOMALY_CORROSIVE_DESTROY);
                        Vibrator.vibrate(VConstants.PLAYER_ANOMALY_CORROSIVE_DESTROY, tmpAnomaly.getLevel());
                    } else {
                        damageToPlayer(tmpAnomaly);
                        if (tmpAnomaly.getLevel() != 4) {
                            tmpAnomaly.takeDamage(player.getPushPower());
                        }
                        tmpAnomaly.setHungry(false);
                        tmpAnomaly.setSlimTimer(0);
                        prettyKick(MSConstants.PLAYER_KICKED_CORROSIVE);
                        Vibrator.vibrate(VConstants.PLAYER_ANOMALY_CORROSIVE_COLLISION);
                    }
                }
            }
            ///
            collideBlackholePoolable(tmpAnomaly);
            ///
        }

        for (int i = 0; i < electricalAnomalyEmitter.getActiveList().size(); i++) {
            ElectricalAnomaly tmpAnomaly = (ElectricalAnomaly) electricalAnomalyEmitter.getActiveList().get(i);
            if (!tmpAnomaly.isTaken()) {
                if (player.getPosition().dst(tmpAnomaly.getPosition()) < PaintConstants.ANOMALY_ELECTRIC_WIDTH / 2 && tmpAnomaly.isBorn()) {
                    tmpAnomaly.setHungry(false);
                    damageToPlayer(tmpAnomaly);
                    Vibrator.vibrate(VConstants.PLAYER_ANOMALY_CORROSIVE_DESTROY, tmpAnomaly.getLevel());
                    prettyKick(MSConstants.PLAYER_KICKED_ELECTRIC);
                    tmpAnomaly.setV(tmpAnomaly.getV() * Rules.ANOMALY_ELECTRIC_DECREASE_FACTOR);
                }
            }
            ///
            collideBlackholePoolable(tmpAnomaly);
            ///
        }

        //
        boolean tmp1 = false;
        for (int i = 0; i < radiationAnomalyEmitter.getActiveList().size(); i++) {
            RadiationAnomaly tmpAnomaly = (RadiationAnomaly) radiationAnomalyEmitter.getActiveList().get(i);
            if (!tmpAnomaly.isTaken()) {
                if (player.getPosition().dst(tmpAnomaly.getPosition()) < PaintConstants.ANOMALY_RADIATION_WIDTH / 3 && tmpAnomaly.isBorn()) {
                    tmpAnomaly.setEat(true);
                    tmpAnomaly.setGlued(true);
                } else {
                    tmpAnomaly.setEat(false);
                    tmpAnomaly.setEatTimer(0.0f);
                    tmpAnomaly.setGlued(false);
                }

                if (tmpAnomaly.isEat()) {
                    tmpAnomaly.addEatTimer(dt);
                    if (tmpAnomaly.getEatTimer() >= Rules.ANOMALIES_RADIATION_EAT_TIME) {
                        tmpAnomaly.setEatTimer(0.0f);
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
                tmp1 |= tmpAnomaly.isGlued();
            }
            ///
            collideBlackholePoolable(tmpAnomaly);
            ///
        }
        player.getStanding().setScoreAlertRadiationAnomaly(tmp1);

        boolean tmp2 = false;
        for (int i = 0; i < magneticAnomalyEmitter.getActiveList().size(); i++) {
            MagneticAnomaly tmpAnomaly = (MagneticAnomaly) magneticAnomalyEmitter.getActiveList().get(i);
            if (!tmpAnomaly.isTaken()) {
                if (player.getPosition().dst(tmpAnomaly.getPosition()) < PaintConstants.ANOMALY_MAGNETIC_HEIGHT / 3 && tmpAnomaly.isBorn()) {
                    tmpAnomaly.setEat(true);
                    tmpAnomaly.setGlued(true);
                } else {
                    tmpAnomaly.setEat(false);
                    tmpAnomaly.setEatTimer(0.0f);
                    tmpAnomaly.setGlued(false);
                }

                if (tmpAnomaly.isEat()) {
                    tmpAnomaly.addEatTimer(dt);
                    if (tmpAnomaly.getEatTimer() >= Rules.ANOMALIES_MAGNETIC_EAT_TIME) {
                        tmpAnomaly.setEatTimer(0.0f);
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
                tmp2 |= tmpAnomaly.isGlued();
            }
            ///
            collideBlackholePoolable(tmpAnomaly);
            ///
        }
        player.getStanding().setFuelAlertMagneticAnomaly(tmp2);
        //

        for (int i = 0; i < asteroidEmitter.getActiveList().size(); i++) {
            Asteroid tmpAsteroid = asteroidEmitter.getActiveList().get(i);
            if (!tmpAsteroid.isTaken()) {
                if (player.getPosition().dst(tmpAsteroid.getPosition()) < tmpAsteroid.getHeight() * tmpAsteroid.getScaleX()) {
                    if (!tmpAsteroid.isFragmented()) {
                        tmpAsteroid.destroy();
                        Vibrator.vibrate(VConstants.PLAYER_ASTEROID_DESTROY);
                        if (GamePreferences.getEffectsLevel() == GamePreferences.EffectLevel.HIGH.getValue()) {
                            Gdx.input.vibrate(VConstants.PLAYER_ASTEROID_DESTROY);
                        }
                        player.getLifeShield().blink(1.0f);
                        particleEmitter.getBuilder().buildDust(tmpAsteroid.getPosition().x, tmpAsteroid.getPosition().y, 180, player.getShieldLevel().getLevel() * 16 + 32);
                        asteroidEmitter.boom(tmpAsteroid.getPosition(), player.getShieldLevel());
                        player.takeDamage(player.getShieldLevel().getLevel() + 1);
                        gameInfoSystem.setup(
                                player.getPosition().x, player.getPosition().y,
                                GameInfoSystem.GameInfoSystemPreset.SCREECH,
                                3.0f,
                                true
                        );
                        boomBox.playSound(MSConstants.ASTEROID_DESTROY);
                        player.getStanding().addToStatistics(tmpAsteroid.getType(), tmpAsteroid.getLevel());
                    } else {
                        player.getRamShield().blink(0.5f);
                        player.setPower(player.getPower() + tmpAsteroid.getSizeForHarvest());
                        //pretty sound if collect much fragments in little time
                        if (player.isDelayFragment()) {
                            boomBox.playSound(MSConstants.GET_FRAGMENT);
                            player.setDelayFragment(false);
                        }
                        tmpAsteroid.destroy();
                    }
                }

                //
                if (tmpAsteroid.isFragmented()) {
                    ray.setCount(12);
                    ray.setSize(2);
                    ray.setPreset(Ray.Preset.GREY);
                    ray.harvest(player, tmpAsteroid, particleEmitter, dt);
                }
                else {
                    ray.setCount(5);
                    ray.setSize(3);
                    ray.setPreset(Ray.Preset.RED);
                    ray.laser(player, tmpAsteroid, particleEmitter, dt);
                }
                //

                for (int j = 0; j < corrosiveAnomalyEmitter.getActiveList().size(); j++) {
                    CorrosiveAnomaly tmpAnomaly = (CorrosiveAnomaly) corrosiveAnomalyEmitter.getActiveList().get(j);
                    if (!tmpAsteroid.isFragmented() && tmpAnomaly.getPosition().dst(tmpAsteroid.getPosition()) < PaintConstants.ANOMALY_CORROSIVE_WIDTH / 2) {
                        tmpAsteroid.destroy();
                        particleEmitter.getBuilder().buildDust(tmpAsteroid.getPosition().x, tmpAsteroid.getPosition().y, 180, 32 + 32);
                        asteroidEmitter.boom(tmpAsteroid.getPosition(), GlobalInteractionLevels.NONE);
                        gameInfoSystem.setup(tmpAnomaly.getPosition().x, tmpAnomaly.getPosition().y, GameInfoSystem.GameInfoSystemPreset.BOOM, 3.0f, false);
                        if (tmpAnomaly.getLevel() == 0) {
                            tmpAnomaly.destroy();
                            tmpAnomaly.showSprayCollision(particleEmitter);
                        } else {
                            tmpAnomaly.takeDamage(1);
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
                        tmpConsumable.showSprayCollision(particleEmitter);
                        gameInfoSystem.setup(
                                tmpConsumable.getPosition().x,
                                tmpConsumable.getPosition().y,
                                GameInfoSystem.GameInfoSystemPreset.SAD,
                                3.0f,
                                true
                        );
                    }
                }
            }
            ///
            collideBlackholePoolable(tmpAsteroid);
            ///
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

    ///
    private void collideBlackholePlayer(float dt) {
        if (player.getPosition().dst(blackhole.getPosition()) <= Rules.BLACKHOLE_TAKE_RANGE) {
            player.setTaken(true);
            player.getVelocity().scl(0.5f);
            tmp1V2.set(blackhole.getPosition()).sub(player.getPosition()).nor().scl(Rules.BLACKHOLE_VELOCITY_CONSUME_POOLABLE);
            player.getPosition().mulAdd(tmp1V2, dt);
            float dst = blackhole.getPosition().dst(player.getPosition());
            float scaleX = dst / Rules.BLACKHOLE_TAKE_RANGE;
            float scaleY = dst / Rules.BLACKHOLE_TAKE_RANGE;
            player.setScaleX(scaleX);
            player.setScaleY(scaleY);
            player.getRamShield().setScaleX(scaleX);
            player.getRamShield().setScaleY(scaleY);
            player.getLifeShield().setScaleX(scaleX);
            player.getLifeShield().setScaleY(scaleY);
            player.getScratch().setScaleX(scaleX);
            player.getScratch().setScaleY(scaleY);
            player.setAngle(player.getAngle() + 365 * dt);
            if (blackhole.getPosition().dst(player.getPosition()) <= Rules.BLACKHOLE_EVENT_HORIZON) {
                BoundCheck.avoidPosition(blackhole.getPosition(), player.getPosition(), (int) Rules.BLACKHOLE_TAKE_RANGE + 50, 0, Rules.SPACE_WIDTH, 0, Rules.SPACE_HEIGHT);
                player.setScaleX(1.0f);
                player.setScaleY(1.0f);
                player.getRamShield().setScaleX(1.0f);
                player.getRamShield().setScaleY(1.0f);
                player.getLifeShield().setScaleX(1.0f);
                player.getLifeShield().setScaleY(1.0f);
                player.getScratch().setScaleX(1.0f);
                player.getScratch().setScaleY(1.0f);
                player.setTaken(false);
                boomBox.playSound(MSConstants.BLACKHOLE_TELEPORT);
                player.takeDamage(Rules.BLACKHOLE_GET_DAMAGE);
                player.setCameraSmooth(true);
            }
        } else {
            player.setScaleX(1.0f);
            player.setScaleY(1.0f);
            player.getRamShield().setScaleX(1.0f);
            player.getRamShield().setScaleY(1.0f);
            player.getLifeShield().setScaleX(1.0f);
            player.getLifeShield().setScaleY(1.0f);
            player.getScratch().setScaleX(1.0f);
            player.getScratch().setScaleY(1.0f);
            player.setTaken(false);
        }
    }
    ///

    ///
    private void collideBlackholePoolable(AbstractPoolable abstractPoolable) {
        float dst = blackhole.getPosition().dst(abstractPoolable.getPosition());
        if (dst <= Rules.BLACKHOLE_TAKE_RANGE) {
            abstractPoolable.setTaken(true);
            abstractPoolable.getVelocity().scl(0.5f);
            float scaleX = (dst / Rules.BLACKHOLE_TAKE_RANGE) * abstractPoolable.getSaveScaleX();
            float scaleY = (dst / Rules.BLACKHOLE_TAKE_RANGE) * abstractPoolable.getSaveScaleY();
            if (scaleX >= abstractPoolable.getSaveScaleX()) {
                scaleX = abstractPoolable.getSaveScaleX();
            }
            if (scaleY >= abstractPoolable.getSaveScaleY()) {
                scaleY = abstractPoolable.getSaveScaleY();
            }
            abstractPoolable.setScaleX(scaleX);
            abstractPoolable.setScaleY(scaleY);
            if (dst <= Rules.BLACKHOLE_EVENT_HORIZON) {
                abstractPoolable.destroy();
            }
        } else {
            if (!(abstractPoolable instanceof Consumable)) {
                abstractPoolable.setScaleX(abstractPoolable.getSaveScaleX());
                abstractPoolable.setScaleY(abstractPoolable.getSaveScaleY());
            }
            abstractPoolable.setTaken(false);
        }
    }
    //

    private void damageToAnomaly(AbstractPoolable abstractPoolable) {
        abstractPoolable.destroy();
        player.getRamShield().blink(1.0f);
        int asc = (abstractPoolable.getLevel()) * Rules.ANOMALIES_BASE_SCORE_ADD;
        player.getStanding().changeScore(asc);
        player.slowDown(abstractPoolable.getLevel() * 50.0f);
        player.changePower(-abstractPoolable.getLevel());
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

    private void prettyKick(String sound) {
        if (player.isDelayAnomaly()) {
            boomBox.playSound(sound);
            player.setDelayAnomaly(false);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        shaderManager.dispose();
        player.getStanding().writeResult();
    }
}