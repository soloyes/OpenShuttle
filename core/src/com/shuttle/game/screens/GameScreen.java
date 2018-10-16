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
import com.badlogic.gdx.math.MathUtils;
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
import com.shuttle.game.conf.VConstants;
import com.shuttle.game.player.PlayerPointer;
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
import com.shuttle.game.utils.Shake;

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
    private Vector2 tmp;
    private Vector3 tmp1;
    @Getter
    @Setter
    private GameInfoSystem gameInfoSystem;
    private PauseGroup pauseGroup;
    private Joystick joystick;
    private PlayerPointer pointer;
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
    private float holeTime;
    private OrthographicCamera cameraHelper;
    private ShaderManager shaderManager;
    private ShaderProgram shaderBlackHole;
    private ShaderProgram shaderPause;
    private float fadeInAlpha;
    private boolean gameOverDeactivate;
    private GameOver gameOver;

    public GameScreen(SpriteBatch batch, Camera camera) {
        super(batch);
        this.batch = batch;
        this.camera = camera;
        this.tmp = new Vector2(0, 0);
        this.tmp1 = new Vector3(0, 0, 0);
        this.shake = new Shake();
        this.cameraHelper = new OrthographicCamera(1080, 1920);
    }

    @Override
    public void show() {
        super.show();
        fadeInAlpha = 0.0f;
        gameOverDeactivate = false;
        shaderManager = new ShaderManager();
        anomalyTimer = new AnomalyTimer(this);
        boomBox.stopAllMusicAndPlay(MSConstants.GAME_MUSIC);
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
        asteroidEmitter = new AsteroidEmitter();
        background = new Background();
        gameInfoSystem = new GameInfoSystem();
        joystick = new Joystick();
        InputMultiplexer im = new InputMultiplexer(stage, joystick);
        Gdx.input.setInputProcessor(im);

        blackhole = new Blackhole();
        player = new Shuttle(this, joystick);
        pointer = new PlayerPointer(player);
        Rules.StartLevel.values()[GamePreferences.getLevel()].setGameLevel(player);
        gameOver = new GameOver(this);
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
            boomBox.stopAllMusicAndPlayOnce(MSConstants.MENU_MUSIC);
            pauseGroup.setGameOver();
            fadeInAlpha += dt / 3;
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
            pointer.render(batch);
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
            pointer.update(dt);
            if (!Rules.GAME_OVER) {
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
            tmp1.set(player.getPosition().cpy().sub(cameraHelper.position.x, cameraHelper.position.y).nor(), 0.0f);
            cameraHelper.position.mulAdd(tmp1, Rules.CAMERA_SMOOTH_VELOCITY * dt);
            camera.position.set(cameraHelper.position);
            if (
                //parking
                    player.getPosition().cpy().sub(
                            cameraHelper.position.x, cameraHelper.position.y).len2() <
                            tmp1.scl(Rules.CAMERA_SMOOTH_VELOCITY * dt).len2()
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

        for (int i = 0; i < consumableEmitter.getActiveList().size(); i++) {
            Consumable tmpConsumable = consumableEmitter.getActiveList().get(i);
            if (!tmpConsumable.isTaken()) {
                if (player.getPosition().dst(tmpConsumable.getPosition()) < 128) {
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
                if (!player.isTaken()) {
                    if (player.getPosition().dst(tmpConsumable.getPosition()) < player.getTakeRange()) {
                        tmp.set(player.getPosition()).sub(tmpConsumable.getPosition()).nor().scl(200.0f);
                        tmpConsumable.getPosition().mulAdd(tmp, dt);
                        float dst = Vector2.dst(player.getPosition().x, player.getPosition().y, tmpConsumable.getPosition().x, tmpConsumable.getPosition().y);
                        float scaleX = (dst / blackhole.getTakeRange()) * tmpConsumable.getSaveScaleX();
                        float scaleY = (dst / blackhole.getTakeRange()) * tmpConsumable.getSaveScaleY();
                        if (scaleX >= tmpConsumable.getSaveScaleX()) {
                            scaleX = tmpConsumable.getSaveScaleX();
                        }
                        if (scaleY >= tmpConsumable.getSaveScaleY()) {
                            scaleY = tmpConsumable.getSaveScaleY();
                        }

                        tmpConsumable.setScaleX(scaleX);
                        tmpConsumable.setScaleY(scaleY);

                        particleEmitter.setupFromPointToPoint(tmpConsumable.getPosition().x, tmpConsumable.getPosition().y, player.getPosition().x, player.getPosition().y, 1, 2, 0, 1, 0, 0.3f, 0.5f, 1, 0.5f, 0.3f);
                        for (int j = 0; j < 24; j++) {
                            float ang = 6.28f / 24 * j;
                            float radX = tmpConsumable.getWidth() / 2 * scaleX;
                            float radY = tmpConsumable.getHeight() / 2 * scaleY;

                            particleEmitter.setup(tmpConsumable.getPosition().x + radX * (float) Math.cos(ang) + MathUtils.random(-10, 10), tmpConsumable.getPosition().y + radY * (float) Math.sin(ang) + MathUtils.random(-10, 10), 0, 0, 0.1f, 4, 4, 0, 1, 0, 0.2f, 0, 1, 0, 0.0f);
                        }

                        tmpConsumable.setScaleX(scaleX);
                        tmpConsumable.setScaleY(scaleY);
                    }
                } else {
                    tmpConsumable.setScaleX(tmpConsumable.getScaleX() + dt);
                    tmpConsumable.setScaleY(tmpConsumable.getScaleY() + dt);
                    if (tmpConsumable.getScaleX() >= tmpConsumable.getSaveScaleX()) {
                        tmpConsumable.setScaleX(tmpConsumable.getSaveScaleX());
                    }
                    if (tmpConsumable.getScaleY() >= tmpConsumable.getSaveScaleY()) {
                        tmpConsumable.setScaleY(tmpConsumable.getSaveScaleY());
                    }
                }
            }
            ///
            collideBlackholePoolable(tmpConsumable, dt);
            ///
        }

        for (int i = 0; i < corrosiveAnomalyEmitter.getActiveList().size(); i++) {
            CorrosiveAnomaly tmpAnomaly = (CorrosiveAnomaly) corrosiveAnomalyEmitter.getActiveList().get(i);
            if (!tmpAnomaly.isTaken()) {
                if (player.getPosition().dst(tmpAnomaly.getPosition()) < 100 && tmpAnomaly.isBorn()) {
                    if (tmpAnomaly.getLevel() < player.getPushPower()) {
                        damageToAnomaly(tmpAnomaly);
                        boomBox.playSound(MSConstants.ANOMALY_CORROSIVE_DESTROY);
                        if (GamePreferences.getEffectsLevel() == GamePreferences.EffectLevel.HIGH.getValue()) {
                            Gdx.input.vibrate(VConstants.PLAYER_ANOMALY_CORROSIVE_DESTROY);
                        }
                    } else {
                        damageToPlayer(tmpAnomaly);
                        if (tmpAnomaly.getLevel() != 4) {
                            tmpAnomaly.takeDamage(player.getPushPower());
                        }
                        tmpAnomaly.setHungry(false);
                        tmpAnomaly.setSlimTimer(0);
                        prettyKick(MSConstants.PLAYER_KICKED_CORROSIVE);
                        if (GamePreferences.getEffectsLevel() == GamePreferences.EffectLevel.HIGH.getValue()) {
                            Gdx.input.vibrate(VConstants.PLAYER_ANOMALY_CORROSIVE_COLLISION);
                        }
                    }
                }
            }
            ///
            collideBlackholePoolable(tmpAnomaly, dt);
            ///
        }

        for (int i = 0; i < electricalAnomalyEmitter.getActiveList().size(); i++) {
            ElectricalAnomaly tmpAnomaly = (ElectricalAnomaly) electricalAnomalyEmitter.getActiveList().get(i);
            if (!tmpAnomaly.isTaken()) {
                if (player.getPosition().dst(tmpAnomaly.getPosition()) < 100 && tmpAnomaly.isBorn()) {
                    if (tmpAnomaly.getLevel() < player.getPushPower()) {
                        damageToPlayer(tmpAnomaly);
                    } else {
                        tmpAnomaly.setHungry(false);
                        damageToPlayer(tmpAnomaly);
                        prettyKick(MSConstants.PLAYER_KICKED_ELECTRIC);
                    }
                }
            }
            ///
            collideBlackholePoolable(tmpAnomaly, dt);
            ///
        }

        //
        boolean tmp1 = false;
        for (int i = 0; i < radiationAnomalyEmitter.getActiveList().size(); i++) {
            RadiationAnomaly tmpAnomaly = (RadiationAnomaly) radiationAnomalyEmitter.getActiveList().get(i);
            if (!tmpAnomaly.isTaken()) {
                if (player.getPosition().dst(tmpAnomaly.getPosition()) < 70 && tmpAnomaly.isBorn()) {
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
            collideBlackholePoolable(tmpAnomaly, dt);
            ///
        }
        player.getStanding().setScoreAlertRadiationAnomaly(tmp1);

        boolean tmp2 = false;
        for (int i = 0; i < magneticAnomalyEmitter.getActiveList().size(); i++) {
            MagneticAnomaly tmpAnomaly = (MagneticAnomaly) magneticAnomalyEmitter.getActiveList().get(i);
            if (!tmpAnomaly.isTaken()) {
                if (player.getPosition().dst(tmpAnomaly.getPosition()) < 70 && tmpAnomaly.isBorn()) {
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
            collideBlackholePoolable(tmpAnomaly, dt);
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

                for (int j = 0; j < corrosiveAnomalyEmitter.getActiveList().size(); j++) {
                    CorrosiveAnomaly tmpAnomaly = (CorrosiveAnomaly) corrosiveAnomalyEmitter.getActiveList().get(j);
                    if (!tmpAsteroid.isFragmented() && tmpAnomaly.getPosition().dst(tmpAsteroid.getPosition()) < 128) {
                        tmpAsteroid.destroy();
                        particleEmitter.getBuilder().buildDust(tmpAsteroid.getPosition().x, tmpAsteroid.getPosition().y, 180, 32 + 32);
                        asteroidEmitter.boom(tmpAsteroid.getPosition(), GlobalInteractionLevels.NONE);
                        gameInfoSystem.setup(tmpAnomaly.getPosition().x, tmpAnomaly.getPosition().y, GameInfoSystem.GameInfoSystemPreset.BOOM, 3.0f, false);
                        if (tmpAnomaly.getLevel() == 0) {
                            tmpAnomaly.destroy();
                            tmpAnomaly.showSpray(particleEmitter);
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
                        tmpConsumable.showSpray(particleEmitter);
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
            collideBlackholePoolable(tmpAsteroid, dt);
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
        if (player.getPosition().dst(blackhole.getPosition()) < Rules.BLACKHOLE_TAKE_RANGE) {
            player.setTaken(true);
            player.getVelocity().scl(0.5f);
            tmp.set(blackhole.getPosition()).sub(player.getPosition()).nor().scl(Rules.BLACKHOLE_VELOCITY_CONSUME_PLAYER);
            player.getPosition().mulAdd(tmp, dt);
            float dst = blackhole.getPosition().dst(player.getPosition());
            float scaleX = dst / blackhole.getTakeRange();
            float scaleY = dst / blackhole.getTakeRange();

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
                float x = MathUtils.random(0, Rules.SPACE_WIDTH);
                float y = MathUtils.random(0, Rules.SPACE_HEIGHT);
                while (!(blackhole.getPosition().cpy().sub(x, y).len() >= Rules.BLACKHOLE_TAKE_RANGE + 50)) {
                    //Prevent generation near blackhole
                    x = MathUtils.random(0, Rules.SPACE_WIDTH);
                    y = MathUtils.random(0, Rules.SPACE_HEIGHT);
                }
                player.getPosition().set(x, y);
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
                player.takeDamage(player.getStanding().getHp());
                player.setCameraSmooth(true);
            }
        } else player.setTaken(false);
    }
    ///

    ///
    private void collideBlackholePoolable(AbstractPoolable poolable, float dt) {
        if (blackhole.getPosition().dst(poolable.getPosition()) < blackhole.getTakeRange()) {
            poolable.setTaken(true);
            tmp.set(blackhole.getPosition()).sub(poolable.getPosition()).nor().scl(Rules.BLACKHOLE_VELOCITY_CONSUME_POOLABLE);
            poolable.getPosition().mulAdd(tmp, dt);
            float dst = blackhole.getPosition().dst(poolable.getPosition());
            float scaleX = (dst / blackhole.getTakeRange()) * poolable.getSaveScaleX();
            float scaleY = (dst / blackhole.getTakeRange()) * poolable.getSaveScaleY();
            if (scaleX >= poolable.getSaveScaleX()) {
                scaleX = poolable.getSaveScaleX();
            }
            if (scaleY >= poolable.getSaveScaleY()) {
                scaleY = poolable.getSaveScaleY();
            }

            poolable.setScaleX(scaleX);
            poolable.setScaleY(scaleY);
            if (blackhole.getPosition().dst(poolable.getPosition()) < Rules.BLACKHOLE_EVENT_HORIZON) {
                poolable.destroy();
            }
        } else poolable.setTaken(false);
    }
    ///

    private void damageToAnomaly(AbstractPoolable anomaly) {
        anomaly.destroy();
        player.getRamShield().blink(1.0f);
        int asc = (anomaly.getLevel() + 1) * Rules.ANOMALIES_BASE_SCORE_ADD;
        player.getStanding().changeScore(asc);
        player.slowDown(anomaly.getLevel() * 50.0f);
        player.changePower(-anomaly.getLevel());
        anomaly.showSpray(particleEmitter);
        player.killPlusPlus();
        player.getStanding().addToStatistics(anomaly.getType(), anomaly.getLevel());
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
            tmp.set(player.getPosition()).sub(anomaly.getPosition()).nor().scl(300 + anomaly.getLevel() * 50);
            player.getVelocity().add(tmp);
            player.resetPower();
            tmp.scl(-1);
            anomaly.getVelocity().add(tmp);
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