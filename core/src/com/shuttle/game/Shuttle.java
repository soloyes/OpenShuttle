package com.shuttle.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.Consumable;
import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.utils.Assets;
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
    private float fuel, maxFuel;
    private float enginePower;
    private int hp, maxHp;
    private int lives;
    private int takeRange;
    private float zigzagEffect;
    private int killSeries;
    private float killTimer;
    private float power, maxPower;
    private ColorLevels shieldLevel;
    private GamePreferences preferences;
    private Score score;
    private int playingLevel;

    public Shuttle(GameScreen gs, Joystick joystick, GamePreferences preferences) {
        this.gs = gs;
        this.joystick = joystick;
        this.preferences = preferences;
        this.texture = Assets.getInstance().getAtlas().findRegion("rocket");
        this.position = new Vector2(
                ScreenManager.getInstance().getViewport().getWorldWidth() / 2,
                ScreenManager.getInstance().getViewport().getWorldHeight() / 2
        );
        this.velocity = new Vector2();
        this.tmp = new Vector2();
        this.width = texture.getRegionWidth();
        this.height = texture.getRegionHeight();
        this.score = new Score(this);
        this.radar = new Radar(gs, this);
        this.gun = new Gun();
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
            case Astronaut:
                score.addScore(-50000);
                gs.getGameInfoSystem().setup(position.x, position.y, "Murder!\n-50000", 8.0f);
                break;
            case Crate:
                gs.getGameInfoSystem().setup(position.x, position.y, "Be easy!", 3.0f);
                break;
        }
    }

    public void decreasePower(int amount) {
        power -= amount;
    }

    public void killPlusPlus() {
        killSeries++;
        killTimer = 7.0f;
        gs.getGameInfoSystem().setup(position.x, position.y, KillingSpree.getKillingSpreeFromKillsCount(killSeries).getText(), KillingSpree.getKillingSpreeFromKillsCount(killSeries).getTextSizeModifier());
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - width / 2, position.y - height / 2, width / 2, height / 2, width, height, 1, 1, angle);
        radar.render(batch);
    }

    public void resetPower() {
        power = 0.0f;
    }

    public void slowDown(float amount) {
        velocity.scl(1.0f - amount / velocity.len());
    }

    public void takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            hp = maxHp;
            lives--;
            gs.getGameInfoSystem().setup(position.x, position.y, "DESTROYED", 8);
            if (lives == 0) {
                Rules.GAME_OVER = true;
                score.writeResult();
            }
        }
    }

    public void takeEffect(Consumable.Type type) {
        switch (type) {
            case Astronaut:
                score.addScore(Rules.PLAYER_SCORE_ADD);
                gs.getGameInfoSystem().setup(position.x, position.y, "+" + Rules.PLAYER_SCORE_ADD, 3.0f);
                break;
            case Crate:
                int x = MathUtils.random(0, 5);
                if (x == 0) {
                    hp += Rules.CRATE_REPAIR_ADD;
                    if (hp > maxHp) {
                        hp = maxHp;
                    }
                    gs.getGameInfoSystem().setup(position.x, position.y, "REPAIR +" + Rules.CRATE_REPAIR_ADD, 3.0f);
                }
                if (x == 1) {
                    fuel += Rules.CRATE_FUEL_ADD;
                    if (fuel > maxFuel) {
                        fuel = maxFuel;
                    }
                    gs.getGameInfoSystem().setup(position.x, position.y, "FUEL +" + Rules.CRATE_FUEL_ADD, 3.0f);
                }
                if (x == 2) {
                    velocity.add(1500 * (float) Math.cos(Math.toRadians(angle)), 1500 * (float) Math.sin(Math.toRadians(angle)));
                    gs.getGameInfoSystem().setup(position.x, position.y, "TURBO X5", 5.0f);
                }
                if (x > 2) {
                    gs.getGameInfoSystem().setup(position.x, position.y, "TRASH", 2.0f);
                }
                break;
        }
    }

    public void update(float dt) {
        checkKillingSpreeSeries(dt);
        checkPower();
        checkZigZagEffect(dt);
        angle = ShuttleMath.checkAngleBetween0And360(angle);
        score.update();
        checkFuel(dt);
        createFXs();

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
        shieldLevel = ColorLevels.getFromLevel((int) (power));
    }

    private void checkZigZagEffect(float dt) {
        if (zigzagEffect > 0.0f) {
            zigzagEffect -= dt;
            angle += zigzagEffect * 25.0f;
            velocity.add(MathUtils.random(-30 * zigzagEffect, 30 * zigzagEffect), MathUtils.random(-30 * zigzagEffect, 30 * zigzagEffect));
        }
    }

    private void checkFuel(float dt) {
        fuel -= 1.0f * dt;
        if (fuel < 0.0f) {
            fuel = 0.0f;
            velocity.scl(0.97f);
        }
    }

    private void createFXs() {
        if (fuel > 0.1f && velocity.len() > 80) {
            gs.getParticleEmitter().getBuilder().buildShuttleEngineFire(position, angle, 90, 1.0f);
        } else {
            gs.getParticleEmitter().getBuilder().buildShuttleLowLevelEngineFire(position, angle, 90);
        }
        gs.getParticleEmitter().getBuilder().buildShuttleShield(position, angle, 90, 60, shieldLevel.getR(), shieldLevel.getG(), shieldLevel.getB());
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
                    gs.getParticleEmitter().setupFromPointToPoint(position.x + dir * 80 * (float) Math.cos(Math.toRadians(90 - angle)), position.y - dir * 80 * (float) Math.sin(Math.toRadians(90 - angle)), position.x + dir * 35 * (float) Math.cos(Math.toRadians(90 - angle)), position.y - dir * 35 * (float) Math.sin(Math.toRadians(90 - angle)), 2.0f, 0.0f, 0.5f, 0.5f, 0.5f, 0.5f, 0, 0, 0, 0);
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
            }
        }
    }

    public int getPushPower() {
        return (int) power;
    }
}
