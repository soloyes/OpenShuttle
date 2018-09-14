package com.shuttle.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.GameInfoSystem;
import com.shuttle.game.Gun;
import com.shuttle.game.Joystick;
import com.shuttle.game.KillingSpree;
import com.shuttle.game.bot.Consumable;
import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.conf.GlobalInteractionLevels;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.BoomBox;
import com.shuttle.game.utils.BoundCheck;
import com.shuttle.game.utils.ShuttleMath;

import lombok.Data;

@Data
public class Shuttle {
    private GameScreen gs;
    private TextureRegion texture;
    private Vector2 position, velocity;
    private Vector2 tmp;
    private Radar radar;
    private Gun gun;
    private Joystick joystick;
    private int width, height;
    private float angle, radialSpeed;
    private float enginePower;
    private int takeRange;
    private float zigzagEffect;
    private int killSeries;
    private float killTimer;
    private float power, maxPower;
    private GlobalInteractionLevels shieldLevel;
    private GamePreferences preferences;
    private Standing standing;
    private BoundCheck boundCheck;
    private BoomBox boomBox;
    private LifeShield lifeShield;
    private RamShield ramShield;
    private Scratch scratch;
    private Vector2 textureVector;
    private Vector2 tmp2;

    public Shuttle(GameScreen gs, Joystick joystick, GamePreferences preferences) {
        this.boomBox = new BoomBox(new GamePreferences());
        this.standing = new Standing(this);
        this.gs = gs;
        this.joystick = joystick;
        this.preferences = preferences;
        this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.SHUTTLE_REGION);
        this.position = new Vector2(
                ScreenManager.getInstance().getViewport().getWorldWidth() / 2,
                ScreenManager.getInstance().getViewport().getWorldHeight() / 2
        );
        this.velocity = new Vector2();
        this.tmp = new Vector2();

        this.textureVector = new Vector2();
        this.tmp2 = new Vector2();

        this.width = PaintConstants.SHUTTLE_WIDTH;
        this.height = PaintConstants.SHUTTLE_HEIGHT;
        this.radar = new Radar(gs, this);
        this.gun = new Gun();
        this.boundCheck = new BoundCheck();

        this.lifeShield = new LifeShield(this);
        this.ramShield = new RamShield(this);
        this.scratch = new Scratch(this);
    }

    public boolean addZigZag(float amount) {
        if (zigzagEffect < 0.1f) {
            zigzagEffect += amount;
            return true;
        }
        return false;
    }

    public void badEffect(Consumable.Type type) {
        switch (type) {
            case ASTRONAUT:
                standing.changeScore(-50000);
                gs.getGameInfoSystem().setup(position.x, position.y, "Murder!\n-50000", 8.0f, false);
                boomBox.playSound(MSConstants.KILL_ASTRONAUT);
                break;
            case CRATE:
                gs.getGameInfoSystem().setup(position.x, position.y, "Be easy!", 3.0f, false);
                boomBox.playSound(MSConstants.KILL_CRATE);
                break;
        }
    }

    public boolean canCatch(Consumable consumable) {
        tmp2.set(consumable.getPosition());
        tmp2.sub(position);

        float dif = angle - tmp2.angle();

        return dif >= -110 && dif <= -40 || dif <= 110 && dif >= 40;
    }

    public void changePower(int amount) {
        power += amount;
    }

    public void killPlusPlus() {
        killSeries++;
        killTimer = 7.0f;
        gs.getGameInfoSystem().setup(
                position.x, position.y,
                KillingSpree.getKillingSpreeFromKillsCount(killSeries).getText(),
                KillingSpree.getKillingSpreeFromKillsCount(killSeries).getTextSizeModifier(),
                false
        );
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - width / 2, position.y - height / 2, width / 2, height / 2, width, height, 1, 1, angle);
        scratch.render(batch);
        lifeShield.render(batch);
        //ramShield.render(batch);
        radar.render(batch);
    }

    public void resetPower() {
        power = 0.0f;
    }

    public void slowDown(float amount) {
        velocity.scl(1.0f - amount / velocity.len());
    }

    public void takeDamage(int amount) {
        standing.changeHP(-amount);
        if (standing.getHp() <= 0) {
            standing.setHp(standing.getMaxHp());
            standing.changeLives(-1);
            if (standing.getLives() != 0) {
                gs.getShake().shake(1, 35, 50);
                standing.setFuel(standing.getMaxFuel());
                gs.getGameInfoSystem().setup(
                        position.x,
                        position.y,
                        "[RED]DESTROYED[]",
                        8.0f,
                        true
                );
            }
        }
    }

    public void takeEffect(Consumable.Type type) {
        switch (type) {
            case ASTRONAUT:
                standing.changeScore(Rules.PLAYER_SCORE_ADD);
                gs.getGameInfoSystem().setup(position.x, position.y, "+" + Rules.PLAYER_SCORE_ADD, 3.0f, false);
                boomBox.playSound(MSConstants.GET_ASTRONAUT);
                break;
            case CRATE:
                int x = MathUtils.random(0, 5);
                if (x == 0) {
                    standing.changeHP(Rules.CRATE_REPAIR_ADD);
                    gs.getGameInfoSystem()
                            .setup(
                                    position.x,
                                    position.y,
                                    GameInfoSystem.GameInfoSystemPreset.REPAIR,
                                    3.0f,
                                    false
                            );
                }
                if (x == 1) {
                    standing.changeFuel(Rules.CRATE_FUEL_ADD);
                    gs.getGameInfoSystem()
                            .setup(
                                    position.x,
                                    position.y,
                                    GameInfoSystem.GameInfoSystemPreset.FUEL,
                                    3.0f,
                                    false
                            );
                    boomBox.playSound(MSConstants.GET_FUEL);
                }
                if (x == 2) {
                    velocity.add(
                            3 * Rules.PLAYER_ENGINE_POWER * (float) Math.cos(Math.toRadians(angle)),
                            3 * Rules.PLAYER_ENGINE_POWER * (float) Math.sin(Math.toRadians(angle))
                    );
                    gs.getGameInfoSystem()
                            .setup(
                                    position.x,
                                    position.y,
                                    GameInfoSystem.GameInfoSystemPreset.TURBO,
                                    5.0f,
                                    false
                            );
                    boomBox.playSound(MSConstants.TURBO);
                }
                if (x > 2) {
                    gs.getGameInfoSystem()
                            .setup(
                                    position.x,
                                    position.y,
                                    GameInfoSystem.GameInfoSystemPreset.TRASH,
                                    2.0f,
                                    false
                            );
                    boomBox.playSound(MSConstants.GET_TRASH);
                }
                break;
        }
    }

    public void update(float dt) {
        checkKillingSpreeSeries(dt);
        checkPower();
        checkZigZagEffect(dt);
        angle = ShuttleMath.checkAngleBetween0And360(angle);
        standing.update();
        checkFuel(dt);
        createFXs();

        scratch.update(dt);
        lifeShield.update(dt);
        ramShield.update(dt);

        velocity.scl(0.99f);
        position.mulAdd(velocity, dt);

        if (!preferences.isJoyStickEnabled()) {
            joystick.setLastId(-1);
        }
        if (joystick.isActive()) {
            float angleToTarget = joystick.getAngle();
            move(dt, angleToTarget);
        } else {
            if (Gdx.input.isTouched()) {
                tmp.set(Gdx.input.getX(), Gdx.input.getY());
                ScreenManager.getInstance().getViewport().unproject(tmp);
                float angleToTarget = tmp.sub(position).angle();
                move(dt, angleToTarget);
            }
        }

        checkRadar(dt);
        checkBounds(dt);
    }

    private void checkKillingSpreeSeries(float dt) {
        if (killTimer > 0.0f) {
            killTimer -= dt;
            if (killTimer <= 0.0f) {
                killSeries = 0;
            }
        }
    }

    private void checkPower() {
        if (power >= maxPower) {
            power = maxPower;
        }
        shieldLevel = GlobalInteractionLevels.getFromLevel((int) (power));
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
        standing.changeFuel(-Rules.PLAYER_FUEL_CONSUMPTION * dt);
        if (standing.getFuel() <= 0.0f) {
            standing.setFuel(0.0f);
            velocity.scl(0.97f);
        }
    }

    private void createFXs() {
        if (standing.getFuel() > 0.1f && velocity.len() > 80) {
            gs.getParticleEmitter().getBuilder().buildShuttleEngineFire(position, angle, 90, 1.0f);
        } else {
            gs.getParticleEmitter().getBuilder().buildShuttleLowLevelEngineFire(position, angle, 90);
        }
        //gs.getParticleEmitter().getBuilder().buildShuttleShield(position, angle, 90, 60, shieldLevel.getR(), shieldLevel.getG(), shieldLevel.getB());
        if (velocity.len() > 1200.0f) {
            gs.getParticleEmitter().getBuilder().buildShuttleFrontalTurboFire(position, angle, 90, 20);
        }
    }

    private void move(float dt, float angleToTarget) {
        if (zigzagEffect < 0.05f) {
            if (Math.abs(angle - angleToTarget) > 2.0f) {
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
        velocity.add(acceleration * (float) Math.cos(Math.toRadians(angle)) * dt, acceleration * (float) Math.sin(Math.toRadians(angle)) * dt);
    }

    private void checkRadar(float dt) {
        if (!radar.isActive()) {
            radar.update(dt);
            if (position.dst(radar.getPosition()) < 128) {
                radar.setActive(true);
                boomBox.playSound(MSConstants.GET_RADAR);
            }
        }
    }

    private void checkBounds(float dt) {
        boundCheck.checkAndHandleBounds(this, dt);
    }

    public int getPushPower() {
        return (int) power;
    }
}