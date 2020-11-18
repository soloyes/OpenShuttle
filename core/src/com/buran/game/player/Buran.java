package com.buran.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.buran.game.bot.blackhole.Blackhole;
import com.buran.game.bot.consumable.AbstractConsumable;
import com.buran.game.bot.consumable.Consumable;
import com.buran.game.conf.Buffs;
import com.buran.game.conf.MSConstants;
import com.buran.game.conf.PaintConstants;
import com.buran.game.conf.PlayerInteractionLevels;
import com.buran.game.conf.Rules;
import com.buran.game.effects.Fibonacci;
import com.buran.game.effects.KillingSpree;
import com.buran.game.effects.particle.ParticleEmitter;
import com.buran.game.player.layers.LifeShield;
import com.buran.game.player.layers.RamShield;
import com.buran.game.player.layers.Scratch;
import com.buran.game.player.layers.TakeRangeField;
import com.buran.game.player.navigation.Radar;
import com.buran.game.screens.ScreenManager;
import com.buran.game.ui.Joystick;
import com.buran.game.ui.groups.mvc.profiles.Profiles;
import com.buran.game.utils.BoundCheck;
import com.buran.game.utils.BuranMath;
import com.buran.game.utils.pool.AbstractPoolable;

import java.util.LinkedList;

import lombok.Getter;
import lombok.Setter;

/**
 * @author soloyes on 03/06/18.
 */

public class Buran extends AbstractBuran {
    public static boolean isLaser;
    private static LinkedList<Integer> shadowsCoordinates;
    private static int F = 2 * (int) (Rules.PLAYER_TURBO_THRESHOLD_VELOCITY - Buffs.getPlayerEnginePower());
    private BoundCheck boundCheck;
    @Getter
    @Setter
    private Radar radar;
    @Getter
    @Setter
    private PlayerInteractionLevels shieldLevel;
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
    private float radialSpeed;
    private float enginePower;
    @Getter
    @Setter
    private boolean cameraSmooth;
    private boolean engineStarted;
    private LinkedList<Vector2> vector2List;
    private LinkedList<Float> angleList;
    private LinkedList<Float> alphaList;
    private float a;
    private ParticleEmitter particleEmitter;

    public Buran(Joystick joystick, ParticleEmitter particleEmitter) {
        this.particleEmitter = particleEmitter;
        this.joystick = joystick;
        this.standing = Standing.getInstance();
        this.radar = new Radar();
        this.boundCheck = new BoundCheck();
        this.lifeShield = new LifeShield();
        this.ramShield = new RamShield();
        this.takeRangeField = new TakeRangeField();
        this.scratch = new Scratch();
        this.vector2List = new LinkedList<Vector2>();
        this.angleList = new LinkedList<Float>();
        this.alphaList = new LinkedList<Float>();
        this.killSeries = 1;
        this.radialSpeed = Rules.PLAYER_RADIAL_SPEED;
        this.enginePower = Buffs.getPlayerEnginePower();
        setShadowsCoordinates();
        checkRamPower();
        respawn();
        Standing.getInstance().clear();
    }

    public static void setShadowsCoordinates() {
        shadowsCoordinates = Fibonacci.generate(F);
    }

    private void checkRamPower() {
        shieldLevel = PlayerInteractionLevels.getFromLevel((int) (Standing.getInstance().getRamPower()));
    }

    public boolean addZigZag(float amount) {
        if (zigzagEffect < 0.1f) {
            zigzagEffect += amount;
            return true;
        }
        return false;
    }

    public void badEffect(Consumable consumable) {
        consumable.badEffect();
    }

    public boolean canCatch(AbstractConsumable abstractConsumable) {
        tmp2V2.nor().setAngle(angle);
        tmp3V2.set(abstractConsumable.getPosition());
        tmp3V2.sub(position);
        float dif = tmp2V2.angle(tmp3V2);
        return (dif >= 30 && dif <= 100) || (dif <= -30 && dif >= -100);
    }

    public void collideBlackhole(float dt) {
        if (position.dst(Blackhole.position) <= Buffs.getBlackholeTakeRange()) {
            isTaken = true;
            velocity.scl(0.5f);
            tmpV2.set(Blackhole.position).sub(position).nor().scl(Rules.BLACKHOLE_VELOCITY_CONSUME_PLAYER);
            position.mulAdd(tmpV2, dt);
            float dst = Blackhole.position.dst(position);
            float scaleX = dst / Buffs.getBlackholeTakeRange();
            float scaleY = dst / Buffs.getBlackholeTakeRange();
            a = dst / Buffs.getBlackholeTakeRange();
            setLayersScale(scaleX, scaleY);
            angle += 365 * dt * Rules.PLAYER_BLACKHOLE_ROTATION;
            if (Blackhole.position.dst(position) <= Rules.BLACKHOLE_EVENT_HORIZON) {
                BoundCheck.avoidPosition(Blackhole.position, position, (int) Buffs.getBlackholeTakeRange() + 50, 0, Rules.SPACE_WIDTH, 0, Rules.SPACE_HEIGHT);
                setLayersScale(1.0f, 1.0f);
                isTaken = false;
                boomBox.playSound(MSConstants.TELEPORT);
                if (!Profiles.getSettings().isChild()) {
                    Standing.getInstance().takeDamage(Buffs.getBlackholeDecrease());
                }
                cameraSmooth = true;
            }
        } else {
            setLayersScale(1.0f, 1.0f);
            isTaken = false;
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

    public void killPlusPlus(AbstractPoolable abstractPoolable) {
        if (abstractPoolable.getLevel() != 0) {
            killTimer = Rules.PLAYER_KILL_TIMER;
            int score = (abstractPoolable.getLevel()) * Rules.ANOMALIES_BASE_SCORE_ADD * killSeries;
            standing.changeScore(score, position, KillingSpree.getKillingSpreeFromKillsCount(killSeries).getText() + "[GREEN] +" + score + "[]", KillingSpree.getKillingSpreeFromKillsCount(killSeries).getTextSizeModifier(), true);
            killSeries++;
        }
    }

    public void render(SpriteBatch batch) {
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
        }
        if (isTaken) {
            tmpColor.a = a;
        }

        batch.setColor(tmpColor);
        batch.draw(
                textureRegion[!Rules.GAME_OVER ? 0 : 1],
                position.x - width / 2, position.y - height / 2,
                width / 2, height / 2,
                width, height,
                scaleX, scaleY,
                angle
        );

        tmpColor.a = 1.0f;
        batch.setColor(tmpColor);

        //ramShield.render(batch); is no need here
        //render from GameScreen with layer's order: player.getRamShield().render(batch);
        if (!Rules.GAME_OVER) {
            if (getShieldLevel().getLevel() >= 2) {
                takeRangeField.render(batch);
            }
            scratch.render(batch);
            lifeShield.render(batch);
            radar.render(batch);
        }
    }

    public static void respawn() {
        Buran.position.set(Rules.SPACE_WIDTH / 2, Rules.SPACE_HEIGHT / 2);
        Buran.velocity.set(0.0f, 0.0f);
        Radar.isActive = false;
        Standing.getInstance().setRamPower(0.0f);
    }

    public void showTakeRangeField(float dt, boolean anyAtHarvestPoint) {
        float a = takeRangeField.getAlpha();
        if (anyAtHarvestPoint) {
            if (a >= PaintConstants.BURAN_TAKE_RANGE_FIELD_MAX) {
                takeRangeField.setAlpha(PaintConstants.BURAN_TAKE_RANGE_FIELD_MAX);
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
        if (velocity.len() > 0) {
            velocity.scl(1.0f - amount / velocity.len());
        }
    }

    public void takeEffect(Consumable consumable) {
        consumable.consumed();
    }

    public void update(float dt) {
        makeShadow(dt);
        checkSeries(dt);
        checkRamPower();
        checkZigZagEffect(dt);
        angle = BuranMath.checkAngleBetween0And360(angle);
        checkFuel(dt);

        scratch.update(dt);
        lifeShield.update(dt);
        ramShield.update(dt);
        takeRangeField.update(dt);

        velocity.scl(/*FPS hack*/1.0f - dt/*FPS hack*/);

        if (!isTaken) {
            position.mulAdd(velocity, dt);
        }

        if (!isTaken) {
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

        if (!isTaken) {
            startEngine(dt);
        }
    }

    private void makeShadow(float dt) {
        float v = velocity.len() - Buffs.getPlayerEnginePower();
        if (v > 10) {
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
        if (standing.getFuel() <= 0.0f || Rules.GAME_OVER) {
            standing.setFuel(0.0f);
            velocity.scl(/*FPS hack*/1.0f - dt/*FPS hack*/);
        }
    }

    private void move(float dt, float angleToTarget) {
        if (zigzagEffect < 0.05f) {
            if (Math.abs(angle - angleToTarget) > /*FPS hack*/120.0f * dt/*FPS hack*/) {
                float dir = BuranMath.getRotationDirection(
                        angle,
                        angleToTarget
                );
                angle += dir * radialSpeed * dt;
                if (Math.abs(angle - angleToTarget) > 5.0f) {
                    particleEmitter.setupFromPointToPoint(
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
                particleEmitter.getBuilder().buildBuranEngineFire(position, angle,  /*FPS hack*/70 - j * 0.5f /*FPS hack*/, 1.0f, 60 * dt);
                if (engineStarted) {
                    boomBox.playSound(MSConstants.START_ENGINE);
                    engineStarted = false;
                }
            } else {
                particleEmitter.getBuilder().buildBuranLowLevelEngineFire(position, angle, 80);
                engineStarted = true;
            }
            if (velocity.len() > Rules.PLAYER_TURBO_THRESHOLD_VELOCITY) {
                particleEmitter.getBuilder().buildBuranFrontalTurboFire(position, angle, 90, 20);
            }
            if (velocity.len() > Buffs.getPlayerEnginePower() + 10) {
                particleEmitter.getBuilder().buildBuranShield(
                        position, angle, 90, 45,
                        PlayerInteractionLevels.RED.getR(), PlayerInteractionLevels.RED.getG(), PlayerInteractionLevels.RED.getB()
                );
            }
        }
    }

    public int getPushPower() {
        return (int) Standing.getInstance().getRamPower();
    }
}