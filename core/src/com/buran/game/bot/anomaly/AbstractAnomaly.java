package com.buran.game.bot.anomaly;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.buran.game.bot.rocket.RocketLauncher;
import com.buran.game.conf.PaintConstants;
import com.buran.game.effects.BoomBox;
import com.buran.game.effects.particle.ParticleEmitter;
import com.buran.game.utils.Assets;
import com.buran.game.utils.BoundCheck;
import com.buran.game.utils.pool.AbstractPoolable;

import lombok.Getter;
import lombok.Setter;

/**
 * @author soloyes on 9/15/18.
 */

public abstract class AbstractAnomaly extends AbstractPoolable {
    @Getter
    @Setter
    protected boolean hungry;
    @Getter
    @Setter
    protected float hungryTimer;
    protected Vector2 tmp;
    @Getter
    @Setter
    protected TextureRegion[] textureRegions;
    @Getter
    @Setter
    protected float v;
    @Getter
    protected TextureRegion atRocketRegion;
    protected ParticleEmitter particleEmitter;
    /** Outward shove from an EMP pulse (world units / s), fading out. */
    public final Vector2 knock = new Vector2();
    /** Corrosive temperament (tip 71): 0 hunter, 1 flanker, 2 lurker; -1 = not chosen yet. */
    public int temper = -1;
    /** A lurker has made its lunge. */
    public boolean lunged;
    /** Frame rate of the current frames (painted loops are slower than the old atlas ones). */
    protected float fps = 24f;
    /** Painted boss frames for this kind (null = none). */
    protected TextureRegion[] bossRegions;

    /** Painted frames replace the atlas ones when they exist. */
    protected void usePainted(String name, String boss) {
        TextureRegion[] p = com.buran.game.utils.Anim.frames(name);
        if (p != null) {
            textureRegions = p;
            fps = com.buran.game.utils.Anim.FPS;
        }
        if (boss != null) {
            bossRegions = com.buran.game.utils.Anim.frames(boss);
        }
    }

    /** The frame to draw now: the boss's own loop for a boss, else this kind's. */
    protected TextureRegion currentFrame() {
        TextureRegion[] r = bossHp > 0 && bossRegions != null ? bossRegions : textureRegions;
        return r[(int) (frame % r.length)];
    }

    /** Boss hit points (0 = not a boss): rockets, boosted rams or EMP take one each (tip 8). */
    public int bossHp;
    /** Seconds before a boss can be hit again. */
    public float bossCooldown;

    /** Turns this anomaly into a boss of the given strength. */
    public void makeBoss(int hp, int level) {
        bossHp = hp;
        this.level = level;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    public abstract void render(SpriteBatch batch);

    public abstract void update(float dt);

    public AbstractAnomaly() {
    }

    protected AbstractAnomaly(ParticleEmitter particleEmitter) {
        this.boomBox = new BoomBox();
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.tmp = new Vector2();
        this.color = new Color();
        this.spreeColor = new Color();
        this.boundCheck = new BoundCheck();
        this.atRocketRegion = new TextureRegion(
                Assets.getInstance()
                        .getAtlas()
                        .findRegion(PaintConstants.AT_ROCKET_POINT_REGION));
        this.particleEmitter = particleEmitter;
    }

    protected void drawAtRocketPoint(SpriteBatch batch) {
        if (atRocketPoint && RocketLauncher.isLaunched()) {
            Color color = batch.getColor();
            batch.setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, PaintConstants.AT_ROCKET_POINT_ALPHA);
            batch.draw(atRocketRegion,
                    position.x - width / 2, position.y - height / 2,
                    width / 2, height / 2,
                    width, height,
                    scaleX, scaleY,
                    clockwise * angle);
            batch.setColor(color);
        }
    }

    public void showSprayBoom() {
        com.buran.game.effects.Juice.kill();
        float R, G, B;
        for (int j = 0; j < 8; j++) {
            R = this.getSpreeColor().r;
            G = this.getSpreeColor().g;
            B = this.getSpreeColor().b;
            particleEmitter.setup(
                    this.getPosition().x,
                    this.getPosition().y,
                    MathUtils.random(-200, 200), MathUtils.random(-200, 200),
                    0.4f, 24, 0,
                    R, G, B, 1f,
                    R, G, B, 0.0f
            );
        }
    }

    public void showSprayCollision() {
        float R, G, B;
        for (int j = 0; j < 60; j++) {
            R = this.getSpreeColor().r;
            G = this.getSpreeColor().g;
            B = this.getSpreeColor().b;
            particleEmitter.setup(
                    this.getPosition().x + MathUtils.random(-30, 30),
                    this.getPosition().y + MathUtils.random(-30, 30),
                    MathUtils.random(-600, 600), MathUtils.random(-600, 600),
                    0.4f, 8, 0,
                    R, G, B, 1f,
                    R, G, B, 0.0f
            );
        }
    }
}