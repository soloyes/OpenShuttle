package com.shuttle.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.consumable.AbstractConsumable;
import com.shuttle.game.bot.consumable.Consumable;
import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.conf.GlobalInteractionLevels;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.effects.Fibonacci;
import com.shuttle.game.effects.KillingSpree;
import com.shuttle.game.player.layers.LifeShield;
import com.shuttle.game.player.layers.RamShield;
import com.shuttle.game.player.layers.Scratch;
import com.shuttle.game.player.layers.TakeRangeField;
import com.shuttle.game.player.navigation.Radar;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.ui.GameInfoSystem;
import com.shuttle.game.ui.Joystick;
import com.shuttle.game.utils.BoundCheck;
import com.shuttle.game.utils.ShuttleMath;
import com.shuttle.game.utils.pool.AbstractPoolable;

import java.util.LinkedList;

import lombok.Getter;
import lombok.Setter;

public class Shuttle extends AbstractShuttle {
    private BoundCheck boundCheck;
    @Getter
    @Setter
    private Radar radar;
    @Getter
    @Setter
    private GlobalInteractionLevels shieldLevel;
    @Getter
    @Setter
    private LifeShield lifeShield;
    @Getter
    @Setter
    private RamShield ramShield;
    @Getter
    @Setter
    private TakeRangeField takeRangeField;
    @Getter
    @Setter
    private Scratch scratch;
    private float zigzagEffect;
    private float killTimer;
    private int killSeries;
    @Getter
    @Setter
    private float angle;
    @Getter
    @Setter
    private float radialSpeed;
    @Getter
    @Setter
    private float enginePower;
    @Getter
    @Setter
    private boolean cameraSmooth;
    private boolean engineStarted;
    private LinkedList<Integer> shadowsCoordinates;
    private LinkedList<Vector2> vector2List;
    private LinkedList<Float> angleList;
    private LinkedList<Float> alphaList;
    private int F;
    @Getter
    @Setter
    private boolean laser;

    public Shuttle(GameScreen gs, Joystick joystick) {
        super(gs);
        this.joystick = joystick;
        this.standing = Standing.getInstance();
        this.radar = new Radar(gs, this);
        this.boundCheck = new BoundCheck();
        this.lifeShield = new LifeShield(this);
        this.ramShield = new RamShield(this);
        this.takeRangeField = new TakeRangeField(this);
        this.scratch = new Scratch(this);
        this.F = 2 * (int) (Rules.PLAYER_TURBO_THRESHOLD_VELOCITY - Rules.PLAYER_ENGINE_POWER);
        this.vector2List = new LinkedList<Vector2>();
        this.angleList = new LinkedList<Float>();
        this.alphaList = new LinkedList<Float>();
        this.killSeries = 1;
        setShadowsCoordinates();
    }

    public void setShadowsCoordinates() {
        this.shadowsCoordinates = Fibonacci.generate(F);
    }

    public boolean addZigZag(float amount) {
        if (zigzagEffect < 0.1f) {
            zigzagEffect += amount;
            return true;
        }
        return false;
    }

    public void badEffect(Consumable consumable) {
        consumable.badEffect(gs);
    }

    public boolean canCatch(AbstractConsumable abstractConsumable) {
        tmp2V2.set(abstractConsumable.getPosition());
        tmp2V2.sub(position);

        float dif = angle - tmp2V2.angle();

        return (dif >= -100 && dif <= -30) || (dif <= 100 && dif >= 30);
    }

    public void collideBlackhole(float dt) {
        if (position.dst(gs.getBlackhole().getPosition()) <= Rules.BLACKHOLE_TAKE_RANGE) {
            taken = true;
            velocity.scl(0.5f);
            tmpV2.set(gs.getBlackhole().getPosition()).sub(position).nor().scl(Rules.BLACKHOLE_VELOCITY_CONSUME_PLAYER);
            position.mulAdd(tmpV2, dt);
            float dst = gs.getBlackhole().getPosition().dst(position);
            float scaleX = dst / Rules.BLACKHOLE_TAKE_RANGE;
            float scaleY = dst / Rules.BLACKHOLE_TAKE_RANGE;
            setLayersScale(scaleX, scaleY);
            angle = angle + 365 * dt;
            if (gs.getBlackhole().getPosition().dst(position) <= Rules.BLACKHOLE_EVENT_HORIZON) {
                BoundCheck.avoidPosition(gs.getBlackhole().getPosition(), position, (int) Rules.BLACKHOLE_TAKE_RANGE + 50, 0, Rules.SPACE_WIDTH, 0, Rules.SPACE_HEIGHT);
                setLayersScale(1.0f, 1.0f);
                taken = false;
                boomBox.playSound(MSConstants.TELEPORT);
                if (!GamePreferences.isChildEnabled()) {
                    takeDamage(Rules.BLACKHOLE_GET_DAMAGE);
                }
                cameraSmooth = true;
            }
        } else {
            setLayersScale(1.0f, 1.0f);
            taken = false;
        }
    }

    private void setLayersScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        ramShield.setScaleX(scaleX);
        ramShield.setScaleY(scaleY);
        lifeShield.setScaleX(scaleX);
        lifeShield.setScaleY(scaleY);
        scratch.setScaleX(scaleX);
        scratch.setScaleY(scaleY);
        takeRangeField.setScaleX(scaleX);
        takeRangeField.setScaleY(scaleY);
    }

    public void takeDamage(int amount) {
        standing.takeDamage(amount);
    }

    public void killPlusPlus(AbstractPoolable abstractPoolable) {
        if (abstractPoolable.getLevel() != 0) {
            killTimer = Rules.PLAYER_KILL_TIMER;
            GameInfoSystem.setup(
                    position.x, position.y,
                    KillingSpree.getKillingSpreeFromKillsCount(killSeries).getText(),
                    KillingSpree.getKillingSpreeFromKillsCount(killSeries).getTextSizeModifier(),
                    false
            );

            int score = (abstractPoolable.getLevel()) * Rules.ANOMALIES_BASE_SCORE_ADD * killSeries;
            GameInfoSystem.setup(
                    position.x,
                    position.y,
                    GameInfoSystem.Preset.SCORE + "[GREEN] +" + score + "[]",
                    3.0f,
                    true
            );
            standing.changeScore(score);
            killSeries++;
        } else {
            GameInfoSystem.setup(
                    position.x, position.y,
                    KillingSpree.getKillingSpreeFromKillsCount(0).getText(),
                    KillingSpree.getKillingSpreeFromKillsCount(0).getTextSizeModifier(),
                    false
            );
        }

    }

    public void render(SpriteBatch batch) {
        engineParticleEmitter.render(batch);
        Color tmpColor = batch.getColor();

        for (int i = 0; i < vector2List.size(); i++) {
            tmpColor.a = alphaList.get(i);
            batch.setColor(tmpColor);
            int t = i < 3 ? 3 : i < 7 ? 2 : i <= vector2List.size() ? 1 : 1;
            batch.draw(
                    textureRegion[t],
                    vector2List.get(i).x, vector2List.get(i).y,
                    widthBlur / 2, heightBlur / 2,
                    widthBlur, heightBlur,
                    scaleXBlur, scaleYBlur,
                    angleList.get(i)
            );
            tmpColor.a = 1.0f;
            batch.setColor(tmpColor);
        }

        if (Rules.GAME_OVER) {
            tmpColor.a = 0.3f;
            batch.setColor(tmpColor);
        }
        batch.draw(
                textureRegion[!Rules.GAME_OVER ? 0 : 1],
                position.x - width / 2, position.y - height / 2,
                width / 2, height / 2,
                width, height,
                scaleX, scaleY,
                angle
        );
        if (Rules.GAME_OVER) {
            tmpColor.a = 1.0f;
            batch.setColor(tmpColor);
        }
        //ramShield.render(batch); - render from GameScreen with layer's order
        if (!Rules.GAME_OVER) {
            if (getShieldLevel().getLevel() >= 2) {
                takeRangeField.render(batch);
            }
            scratch.render(batch);
            lifeShield.render(batch);
            radar.render(batch);
        }
    }

    public void showTakeRangeField(float dt, boolean anyAtHarvestPoint) {
        float a = takeRangeField.getAlpha();
        if (anyAtHarvestPoint) {
            if (a >= PaintConstants.SHUTTLE_TAKE_RANGE_FIELD_MAX) {
                takeRangeField.setAlpha(PaintConstants.SHUTTLE_TAKE_RANGE_FIELD_MAX);
            } else {
                takeRangeField.setAlpha(a + dt);
            }
        } else {
            if (a <= 0.0f) {
                takeRangeField.setAlpha(0.0f);
            } else {
                takeRangeField.setAlpha(a - dt);
            }
        }
    }

    public void slowDown(float amount) {
        if (velocity.len() > 0)
            velocity.scl(1.0f - amount / velocity.len());
    }

    public void takeEffect(Consumable consumable) {
        consumable.consumed(gs);
    }

    public void update(float dt) {
        makeShadow(dt);
        engineParticleEmitter.update(dt);
        checkSeries(dt);
        checkRamPower();
        checkZigZagEffect(dt);
        angle = ShuttleMath.checkAngleBetween0And360(angle);
        checkFuel(dt);

        scratch.update(dt);
        lifeShield.update(dt);
        ramShield.update(dt);
        takeRangeField.update(dt);

        velocity.scl(/*FPS hack*/1.0f - dt/*FPS hack*/);

        if (!taken) {
            position.mulAdd(velocity, dt);
        }

        if (!taken) {
            if (joystick.isActive()) {
                float angleToTarget = joystick.getAngle();
                move(dt, angleToTarget);
            } else {
                if (Gdx.input.isTouched()) {
                    if (pointer.size() == 0) {
                        tmpV2.set(Gdx.input.getX(), Gdx.input.getY());
                    } else {
                        tmpV2.set(Gdx.input.getX(pointer.peekLast()), Gdx.input.getY(pointer.peekLast()));
                    }
                    ScreenManager.getInstance().getViewport().unproject(tmpV2);
                    float angleToTarget = tmpV2.sub(position).angle();
                    move(dt, angleToTarget);
                }
            }
        }

        radar.update(dt);
        boundCheck.checkAndHandleBounds(this, dt);

        if (!taken) {
            startEngine(dt);
        }
    }

    private void makeShadow(float dt) {
        float v = velocity.len() - Rules.PLAYER_ENGINE_POWER;
        if (v >= 0) {
            if (v <= shadowsCoordinates.getLast()) {
                if (shadowsCoordinates.size() > 1) {
                    shadowsCoordinates.removeLast();
                }
                Vector2 tmp = position.cpy();
                tmp.set(
                        tmp.x - (float) Math.cos(Math.toRadians(angle)) - width / 2,
                        tmp.y - (float) Math.sin(Math.toRadians(angle)) - height / 2
                );
                vector2List.add(tmp);
                angleList.add(angle);
                int i = vector2List.size() - 1;
                alphaList.add(1.0f - (0.7f / (i / 2 + 1)));
            }
        } else {
            for (int i = 0; i < alphaList.size(); i++) {
                float t = alphaList.get(i) - dt;
                if (t <= 0.0f) {
                    t = 0.0f;
                }
                alphaList.set(i, t);
                if (alphaList.get(alphaList.size() - 1) == 0.0f) {
                    vector2List.clear();
                    angleList.clear();
                    alphaList.clear();
                    setShadowsCoordinates();
                }
            }
        }
    }

    private void checkSeries(float dt) {
        if (killTimer > 0.0f) {
            killTimer -= dt;
            if (killTimer <= 1.0f) {
                killSeries = 1;
            }
        }
    }

    private void checkRamPower() {
        shieldLevel = GlobalInteractionLevels.getFromLevel((int) (Standing.getInstance().getRamPower()));
    }

    private void checkZigZagEffect(float dt) {
        if (zigzagEffect > 0.0f) {
            lifeShield.blink(1.0f);
            zigzagEffect -= dt;
            angle += zigzagEffect * 25.0f;
            velocity.add(MathUtils.random(-30 * zigzagEffect, 30 * zigzagEffect), MathUtils.random(-30 * zigzagEffect, 30 * zigzagEffect));
        }
    }

    private void checkFuel(float dt) {
        if (!Rules.GAME_OVER) {
            standing.changeFuel(-Rules.PLAYER_FUEL_CONSUMPTION * dt);
        }
        if (standing.getFuel() <= 0.0f) {
            standing.setFuel(0.0f);
            velocity.scl(/*FPS hack*/1.0f - dt/*FPS hack*/);
        }
    }

    private void move(float dt, float angleToTarget) {
        if (zigzagEffect < 0.05f) {
            if (Math.abs(angle - angleToTarget) > /*FPS hack*/120.0f * dt/*FPS hack*/) {
                float dir = ShuttleMath.getRotationDirection(
                        angle,
                        angleToTarget
                );
                angle += dir * radialSpeed * dt;
                if (Math.abs(angle - angleToTarget) > 5.0f) {
                    gs.getParticleEmitter()
                            .setupFromPointToPoint(
                                    position.x + dir * 80 * (float) Math.cos(Math.toRadians(90 - angle)),
                                    position.y - dir * 80 * (float) Math.sin(Math.toRadians(90 - angle)),
                                    position.x + dir * 35 * (float) Math.cos(Math.toRadians(90 - angle)),
                                    position.y - dir * 35 * (float) Math.sin(Math.toRadians(90 - angle)),
                                    2.0f,
                                    0.0f,
                                    0.5f, 0.5f, 0.5f, 0.5f,
                                    0, 0, 0, 0
                            );
                }
            }
        }

        float acceleration =
                (joystick.isActive() ? joystick.getPower() : 1) * enginePower;
        velocity.add(
                acceleration * (float) Math.cos(Math.toRadians(angle)) * dt,
                acceleration * (float) Math.sin(Math.toRadians(angle)) * dt
        );
    }

    private void startEngine(float dt) {
        /*FPS hack*/
        int j = (int) (60 * dt / 2);
        /*FPS hack*/
        for (int i = 0; i <= j; i++) {
            if (standing.getFuel() > 0.1f && velocity.len() >= 80 && !Rules.GAME_OVER) {
                engineParticleEmitter.getBuilder().buildShuttleEngineFire(position, angle,  /*FPS hack*/70 - j * 0.5f /*FPS hack*/, 1.0f, 60 * dt);
                if (engineStarted) {
                    boomBox.playSound(MSConstants.START_ENGINE);
                    engineStarted = false;
                }
            } else {
                engineParticleEmitter.getBuilder().buildShuttleLowLevelEngineFire(position, angle, 80);
                engineStarted = true;
            }
            if (velocity.len() >= Rules.PLAYER_TURBO_THRESHOLD_VELOCITY) {
                gs.getParticleEmitter().getBuilder().buildShuttleFrontalTurboFire(position, angle, 90, 20);
            }
            if (velocity.len() >= Rules.PLAYER_ENGINE_POWER) {
                gs.getParticleEmitter().getBuilder().buildShuttleShield(
                        position, angle, 90, 45,
                        GlobalInteractionLevels.RED.getR(), GlobalInteractionLevels.RED.getG(), GlobalInteractionLevels.RED.getB()
                );
            }
        }
    }

    public int getPushPower() {
        return (int) Standing.getInstance().getRamPower();
    }

    public static void respawn(){
        Shuttle.position.set(Rules.SPACE_WIDTH / 2, Rules.SPACE_HEIGHT / 2);
        Shuttle.velocity.set(0.0f, 0.0f);
    }
}