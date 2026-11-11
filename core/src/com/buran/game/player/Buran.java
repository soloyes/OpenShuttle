package com.buran.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.buran.game.bot.blackhole.Blackhole;
import com.buran.game.bot.blackhole.CircleRenderer;
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
import com.buran.game.input.Gamepad;
import com.buran.game.labelsticker.Sticker;
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
    /**
     * The ram shield is switched by the player (A). Charged by metal fragments; while on it
     * slowly drains, breaks what it outranks and lets crew in only over the wings. Off, nothing
     * is rammed and everything is caught safely.
     */
    public static boolean ramOn;
    private static com.badlogic.gdx.graphics.Texture halo;

    private static com.badlogic.gdx.graphics.Texture haloTexture() {
        int n = 128;
        com.badlogic.gdx.graphics.Pixmap p = new com.badlogic.gdx.graphics.Pixmap(n, n, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        p.setBlending(com.badlogic.gdx.graphics.Pixmap.Blending.None);
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                float dx = (x + 0.5f) / n * 2f - 1f;
                float dy = (y + 0.5f) / n * 2f - 1f;
                float k = Math.max(0f, 1f - (float) Math.sqrt(dx * dx + dy * dy));
                p.setColor(1f, 1f, 1f, k * k);
                p.drawPixel(x, y);
            }
        }
        com.badlogic.gdx.graphics.Texture t = new com.badlogic.gdx.graphics.Texture(p);
        t.setFilter(com.badlogic.gdx.graphics.Texture.TextureFilter.Linear, com.badlogic.gdx.graphics.Texture.TextureFilter.Linear);
        p.dispose();
        return t;
    }
    /** Seconds since the ram shield was last switched (for the flash), and whether it was denied. */
    public static float ramSwitchAge = 99f;
    public static boolean ramDenied;
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
        if (ramOn && Standing.getInstance().getRamPower() < 1f) {
            ramOn = false;   // ran dry
            ramSwitchAge = 0f;
            new com.buran.game.effects.BoomBox().playSound(com.buran.game.conf.MSConstants.RAM_OFF);
        }
        shieldLevel = ramOn ? PlayerInteractionLevels.getFromLevel((int) (Standing.getInstance().getRamPower()))
                : PlayerInteractionLevels.NONE;
    }

    /** A: switches the ram shield; it will not come on without at least one level of charge. */
    public static void toggleRam() {
        ramSwitchAge = 0f;
        if (!ramOn && Standing.getInstance().getRamPower() < 1f) {
            ramDenied = true;
            new com.buran.game.effects.BoomBox().playSound(com.buran.game.conf.MSConstants.RAM_DENIED);
            return;
        }
        ramDenied = false;
        ramOn = !ramOn;
        new com.buran.game.effects.BoomBox().playSound(ramOn ? com.buran.game.conf.MSConstants.RAM_ON
                : com.buran.game.conf.MSConstants.RAM_OFF);
    }

    /** Ram power that counts now: the charge when the shield is on, nothing when off. */
    public static int ramLevel() {
        return ramOn ? (int) Standing.getInstance().getRamPower() : 0;
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
            // Fall along the accretion disk: Keplerian orbit in the tilted disk plane (same tilt
            // as the shader and the sand), spiralling inward faster the closer it gets.
            float tilt = com.buran.game.bot.blackhole.BlackholeDebris.TILT;
            float px = position.x - Blackhole.position.x;
            float py = (position.y - Blackhole.position.y) / tilt;
            float r = Math.max(1f, (float) Math.sqrt(px * px + py * py));
            float phi = MathUtils.atan2(py, px);
            float omega = 5200f / (float) Math.pow(Math.max(r, 40f), 1.5f);
            phi += omega * dt;
            // Quick, accelerating plunge: about a second and a half from capture to the horizon.
            r -= (r * 0.8f + 60f + 2000f / Math.max(r, 40f)) * dt;
            r = Math.max(r, 0f);
            position.set(Blackhole.position.x + r * MathUtils.cos(phi), Blackhole.position.y + r * MathUtils.sin(phi) * tilt);
            // Nose along the orbit instead of spinning.
            float tx = -MathUtils.sin(phi);
            float ty = MathUtils.cos(phi) * tilt;
            float heading = MathUtils.atan2(ty, tx) * MathUtils.radiansToDegrees;
            float diff = ((heading - angle) % 360f + 540f) % 360f - 180f;
            angle += diff * Math.min(1f, dt * 5f);
            float dst = Blackhole.position.dst(position);
            float k = dst / Buffs.getBlackholeTakeRange();
            a = k;
            crumble();
            setLayersScale(0.75f + 0.25f * k, 0.75f + 0.25f * k);
            if (Blackhole.position.dst(position) <= Math.max(Rules.BLACKHOLE_EVENT_HORIZON, Buffs.getBlackholeTakeRange() * 0.17f)) {
                BoundCheck.avoidPosition(Blackhole.position, position, (int) Buffs.getBlackholeTakeRange() + 50, 0, Rules.SPACE_WIDTH, 0, Rules.SPACE_HEIGHT);
                setLayersScale(1.0f, 1.0f);
                isTaken = false;
                boomBox.playSound(MSConstants.TELEPORT);
                rematerialize = REMATERIALIZE_TIME;
                a = 1f;
                if (!Profiles.getSettings().isChild()) {
                    Standing.getInstance().takeDamage(Buffs.getBlackholeDecrease());
                }
                cameraSmooth = true;
            }
        } else {
            setLayersScale(1.0f, 1.0f);
            isTaken = false;
            crumbled = 0f;
        }
    }

    private float crumbled;

    /** How far the hull has turned to sand, 0..1 (done two thirds of the way in). */
    private float dissolveAmount() {
        float horizon = 0.17f;
        return MathUtils.clamp((1f - a) / (1f - horizon) * 1.5f, 0f, 1f);
    }

    /**
     * The hull breaks into coarse grains in its own colours, spread over its footprint, and the
     * disk winds them in. Grains are released in step with the dissolve shader eating the sprite.
     */
    private void crumble() {
        float d = dissolveAmount();
        if (d < crumbled) {
            crumbled = d;
        }
        int total = 450;
        int n = (int) ((d - crumbled) * total);
        if (n <= 0) {
            return;
        }
        crumbled += n / (float) total;
        float cos = MathUtils.cosDeg(angle);
        float sin = MathUtils.sinDeg(angle);
        for (int i = 0; i < n; i++) {
            float u = MathUtils.random(-0.5f, 0.5f) * width * scaleX;
            float v = MathUtils.random(-0.5f, 0.5f) * height * scaleY * (1f - Math.abs(u) / (width * scaleX));
            float x = position.x + u * cos - v * sin;
            float y = position.y + u * sin + v * cos;
            float pick = MathUtils.random();
            if (pick < 0.55f) {
                com.buran.game.bot.blackhole.BlackholeDebris.grain(x, y, 0.95f, 0.95f, 0.97f, MathUtils.random(1.3f, 2.2f));
            } else if (pick < 0.85f) {
                com.buran.game.bot.blackhole.BlackholeDebris.grain(x, y, 1f, 0.42f, 0.38f, MathUtils.random(1.3f, 2.2f));
            } else {
                com.buran.game.bot.blackhole.BlackholeDebris.grain(x, y, 0.5f, 0.56f, 0.68f, MathUtils.random(1.2f, 1.8f));
            }
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
            score = (int) (score * Sticker.COMMANDER.getCoefficient());
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
        if (!Rules.GAME_OVER && !isTaken) {
            // Read the ship over any nebula (tips 46-49): a soft cool halo and a dark rim behind it.
            float cr = tmpColor.r, cg = tmpColor.g, cb = tmpColor.b, ca = tmpColor.a;
            if (halo == null) {
                halo = haloTexture();
            }
            float hs = Math.max(width, height) * 1.9f;
            batch.setColor(0.75f, 0.85f, 1f, 0.16f);
            batch.draw(halo, position.x - hs / 2f, position.y - hs / 2f, hs, hs);
            batch.setColor(0f, 0f, 0.02f, 0.55f);
            batch.draw(textureRegion[0], position.x - width / 2, position.y - height / 2, width / 2, height / 2,
                    width, height, scaleX * 1.07f, scaleY * 1.07f, angle);
            tmpColor.set(cr, cg, cb, ca);
            batch.setColor(cr, cg, cb, ca);
        }
        float dis = isTaken ? dissolveAmount() : rematerialize / REMATERIALIZE_TIME;
        batch.setColor(tmpColor);
        boolean dissolving = dis > 0f && com.buran.game.effects.Dissolve.begin(batch, dis);
        batch.draw(
                textureRegion[!Rules.GAME_OVER ? 0 : 1],
                position.x - width / 2, position.y - height / 2,
                width / 2, height / 2,
                width, height,
                scaleX, scaleY,
                angle
        );
        if (dissolving) {
            com.buran.game.effects.Dissolve.end(batch);
        }

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
        CircleRenderer.scan = false;
        Standing.getInstance().setRamPower(0.0f);
        ramOn = false;
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

    private static final float REMATERIALIZE_TIME = 0.9f;
    /** Seconds left of re-assembling from sand after the black hole throws the ship out. */
    private float rematerialize;

    public void update(float dt) {
        if (rematerialize > 0f) {
            rematerialize = Math.max(0f, rematerialize - dt);
        }
        makeShadow(dt);
        checkSeries(dt);
        ramSwitchAge += dt;
        if (ramOn && !Rules.GAME_OVER) {
            Standing.getInstance().changeRamPower(-Rules.RAM_DRAIN * dt);
        }
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
            if (Gamepad.get().isTwin()) {
                moveTwin(dt);
            } else if (Gamepad.get().isSteering()) {
                move(dt, Gamepad.get().getSteerAngle());
            } else if (joystick.isActive()) {
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

        if (!isLaser){
            boomBox.stopMusicEffect(MSConstants.LASER);
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
                (Gamepad.get().isSteering() ? Gamepad.get().getSteerPower()
                        : joystick.isActive() ? joystick.getPower() : 1) * enginePower;
        velocity.add(
                acceleration * (float) Math.cos(Math.toRadians(angle)) * dt,
                acceleration * (float) Math.sin(Math.toRadians(angle)) * dt
        );
    }

    /** Both sticks: the right one turns the nose, the left one is throttle (up) or brake (down). */
    private void moveTwin(float dt) {
        if (zigzagEffect < 0.05f) {
            angle += Gamepad.get().getTurn() * radialSpeed * dt;
        }
        float throttle = Gamepad.get().getThrottle();
        if (throttle > 0f) {
            float acceleration = throttle * enginePower;
            velocity.add(acceleration * (float) Math.cos(Math.toRadians(angle)) * dt,
                    acceleration * (float) Math.sin(Math.toRadians(angle)) * dt);
        } else if (throttle < 0f) {
            velocity.scl(Math.max(0f, 1f + throttle * 1.6f * dt));
        }
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
        return ramLevel();
    }
}