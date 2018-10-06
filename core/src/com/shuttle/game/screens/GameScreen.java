package com.shuttle.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.shuttle.game.bot.blackhole.Blackhole;
import com.shuttle.game.bot.pool.AbstractPoolable;
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
    private AsteroidEmitter asteroidEmitter;
    @Getter
    @Setter
    private Camera camera;
    private Vector2 tmp;
    private ShaderProgram shaderPause;
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
    private Blackhole blackhole;
    private FrameBuffer fbo;
    private TextureRegion fboTexRegion;
    private ShaderProgram shaderBlackHole;
    private float holeTime;
    private OrthographicCamera cameraHelper;

    public GameScreen(SpriteBatch batch, Camera camera) {
        super(batch);
        this.batch = batch;
        this.camera = camera;
        this.tmp = new Vector2(0, 0);
        this.shake = new Shake();
        this.cameraHelper = new OrthographicCamera(1080, 1920);
    }

    @Override
    public void show() {
        super.show();
        anomalyTimer = new AnomalyTimer();
        boomBox.stopAllMusicAndPlay(MSConstants.GAME_MUSIC);
        fbo = new FrameBuffer(Pixmap.Format.RGB565, Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight(), false);
        fboTexRegion = new TextureRegion(fbo.getColorBufferTexture());
        fboTexRegion.flip(false, true);

        shaderBlackHole = new ShaderProgram(
                Gdx.files.internal("shaders/black_hole.vert").readString(),
                Gdx.files.internal("shaders/black_hole.frag").readString()
        );

        shaderPause = new ShaderProgram(
                Gdx.files.internal("shaders/grey_screen.vert"),
                Gdx.files.internal("shaders/grey_screen.frag")
        );

        //todo: change logic in case shade not support. Move shader to other class
        if (!shaderPause.isCompiled()) {
            System.err.println(shaderPause.getLog());
            System.exit(0);
        }

        ShaderProgram.pedantic = false;

        if (!shaderBlackHole.isCompiled()) {
            System.err.println(shaderBlackHole.getLog());
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

        blackhole = new Blackhole();
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

        if (!Rules.GAME_OVER) {
            //FBO BEGIN
            fbo.begin();
            super.render(dt);
            update(dt);
            batch.setProjectionMatrix(camera.combined);
            if (pauseGroup.isPause()) {
                batch.setShader(shaderPause);
            } else {
                batch.setShader(null);
            }

            //BEGIN
            batch.begin();
            background.render(batch);
            if (preferences.getEffectsLevel() == 0) {
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
            //pointer.render(batch);
            batch.end();

            float shaderHoleX = (blackhole.getPosition().x - camera.position.x + Rules.WORLD_WIDTH / 2) / Rules.WORLD_WIDTH;
            float shaderHoleY = (blackhole.getPosition().y - camera.position.y + Rules.WORLD_HEIGHT / 2) / Rules.WORLD_HEIGHT;

            ScreenManager.getInstance().resetCamera();
            //FBO END
            fbo.end(v.getScreenX(), v.getScreenY(), v.getScreenWidth(), v.getScreenHeight());

            if (preferences.getEffectsLevel() == 0) {
                batch.setShader(null);
                //BEGIN
                batch.begin();
                super.render(dt);
                batch.draw(fboTexRegion, 0, 0, Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT);
                batch.end();
            } else {
                batch.setShader(shaderBlackHole);
                //BEGIN
                batch.begin();
                super.render(dt);
                shaderBlackHole.setUniformf("resolution", Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT);

                holeTime += dt;
                shaderBlackHole.setUniformf(
                        "time",
                        holeTime
                );
                shaderBlackHole.setUniformf(
                        "center",
                        shaderHoleX,
                        shaderHoleY
                );

                if (pauseGroup.isPause()) {
                    shaderBlackHole.setUniformf("isGrey", 1.0f);
                } else {
                    shaderBlackHole.setUniformf("isGrey", -1.0f);
                }
                batch.draw(fboTexRegion, 0, 0, Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT);
                batch.end();
            }

            if (pauseGroup.isPause()) {
                batch.setShader(shaderPause);
            } else {
                batch.setShader(null);
            }

            batch.begin();
            player.getStanding().render(batch);
            anomalyTimer.render(batch);
            if (!pauseGroup.isPause()) {
                joystick.render(batch);
            }
            batch.end();

            cameraHelperUpdate();
            batch.setProjectionMatrix(cameraHelper.combined);
            //BEGIN
            batch.begin();
            gameInfoSystem.render(batch);
            pointer.render(batch);
            batch.end();

            batch.setShader(null);
            stage.draw();

        } else {
            super.render(dt);
            boomBox.stopAllMusicAndPlayOnce(MSConstants.MENU_MUSIC);
            batch.begin();
            background.render(batch);
            gameInfoSystem.update(dt);
            gameInfoSystem.render(batch);
            player.getStanding().render(batch);
            batch.end();
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
            blackhole.update(dt);
            gameInfoSystem.update(dt);
            anomalyTimer.update(dt);
            pointer.update(dt);
            checkCollision(dt);
        }
        cameraUpdate();
        if (!pauseGroup.isPause()) {
            shake.update(dt, camera);
        }
        stage.act();
    }

    private void cameraHelperUpdate() {
        cameraUpdater(cameraHelper);
    }

    private void cameraUpdate() {
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
                    } else {
                        damageToPlayer(tmpAnomaly);
                        if (tmpAnomaly.getLevel() != 4) {
                            tmpAnomaly.takeDamage(player.getPushPower());
                        }
                        tmpAnomaly.setHungry(false);
                        tmpAnomaly.setSlimTimer(0);
                        prettyKick(MSConstants.PLAYER_KICKED_CORROSIVE);
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
                        player.getLifeShield().blink(1.0f);
                        particleEmitter.getBuilder().buildDust(tmpAsteroid.getPosition().x, tmpAsteroid.getPosition().y, 180, player.getShieldLevel().getLevel() * 16 + 32);
                        asteroidEmitter.boom(tmpAsteroid.getPosition(), player.getShieldLevel());
                        player.takeDamage(player.getShieldLevel().getLevel() + 1);
                        gameInfoSystem.setup(player.getPosition().x, player.getPosition().y, GameInfoSystem.GameInfoSystemPreset.SCREECH, 3.0f, false);
                        boomBox.playSound(MSConstants.ASTEROID_DESTROY);
                        //shake.shake(1, 35, (int) (tmpAsteroid.getVelocity().len() / 5));
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
                            showSpray(tmpAnomaly);
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
            ///
            collideBlackholePoolable(tmpAsteroid, dt);
            ///
        }
    }

    private void cameraUpdater(Camera camera) {
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
                player.getPosition().set(
                        MathUtils.random(0, Rules.SPACE_WIDTH),
                        MathUtils.random(0, Rules.SPACE_HEIGHT)
                );
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
                player.getStanding().changeLives(-1);
            }
        } else player.setTaken(false);
    }

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

    private void damageToAnomaly(AbstractPoolable anomaly) {
        anomaly.destroy();
        player.getRamShield().blink(1.0f);
        int asc = (anomaly.getLevel() + 1) * Rules.ANOMALIES_BASE_SCORE_ADD;
        player.getStanding().changeScore(asc);
        player.slowDown(anomaly.getLevel() * 50.0f);
        player.changePower(-anomaly.getLevel());
        showSpray(anomaly);
        player.killPlusPlus();
    }
    ///

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
    ///

    private void prettyKick(String sound) {
        if (player.isDelayAnomaly()) {
            boomBox.playSound(sound);
            player.setDelayAnomaly(false);
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
        shaderPause.dispose();
        player.getStanding().writeResult();
    }
}