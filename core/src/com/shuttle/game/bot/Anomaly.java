package com.shuttle.game.bot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.BoomBox;
import com.shuttle.game.utils.BoundCheck;
import com.shuttle.game.utils.Regions;

import lombok.Data;

@Data
public class Anomaly extends AbstractPoolable {
    float[] probabilities = {1.0f, 0.8f, 0.5f, 0.3f, 0.05f};
    private GameScreen gs;
    private TextureRegion[] textureRegions;
    private TextureRegion glowRegion;
    private Vector2 position;
    private Vector2 velocity;
    private Vector2 tmp;
    private int level;
    private boolean active;
    private float hungryTimer;
    private boolean hungry;
    private float slimTimer;
    private float scaleX;
    private float scaleY;
    private boolean touched;
    private boolean born;
    private int glowWidth, glowHeight;
    private boolean oscillator;
    private BoundCheck boundCheck;
    private BoomBox boomBox;
    private Color color;
    private float frame;
    private float rotation;
    private float angle;
    private int clockwise;

    public Anomaly(GameScreen gs) {
        this.boomBox = new BoomBox(new GamePreferences());
        this.gs = gs;
        this.textureRegions = Regions.split(new TextureRegion(Assets.getInstance().getAssetManager().get(PaintConstants.ANOMALY_REGION, Texture.class)),
                3,
                4,
                12
        );
        this.glowRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.ANOMALY_GLOW);
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.tmp = new Vector2();
        this.width = PaintConstants.ANOMALY_WIDTH;
        this.height = PaintConstants.ANOMALY_HEIGHT;
        this.glowWidth = PaintConstants.ANOMALY_GLOW_WIDTH;
        this.glowHeight = PaintConstants.ANOMALY_GLOW_HEIGHT;
        this.boundCheck = new BoundCheck();
        this.color = new Color();
    }

    public void init(float x, float y) {
        this.touched = false;
        this.hungryTimer = 0;
        this.slimTimer = 0;
        this.hungry = true;
        this.angle = MathUtils.random(365);
        this.clockwise = MathUtils.randomSign();
        this.position.set(x, y);
        this.active = true;
        this.born = false;
        this.oscillator = true;
        this.scaleX = this.scaleY = 0.0f;
        this.color.a = PaintConstants.ANOMALY_GLOW_OSCILLATOR_MAX;
        float rnd = MathUtils.random(0.0f, 1.0f);
        for (int i = probabilities.length - 1; i >= 0; i--) {
            if (rnd < probabilities[i]) {
                level = i;
                break;
            }
        }
    }

    public void render(SpriteBatch batch) {
        batch.setColor(color);
        batch.draw(
                glowRegion,
                position.x - glowWidth / 2,
                position.y - glowHeight / 2,
                glowWidth / 2,
                glowHeight / 2,
                glowWidth,
                glowHeight,
                scaleX,
                scaleY,
                clockwise * rotation
        );

        batch.draw(
                textureRegions[(int) (frame % textureRegions.length)],
                position.x - width / 2,
                position.y - height / 2,
                width / 2,
                height / 2,
                width,
                height,
                scaleX,
                scaleY,
                clockwise * rotation
        );
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void update(float dt) {
        scaleAndBorn(dt);

        switch (level) {
            case 0:
                color.set(Color.TAN.r, Color.TAN.g, Color.TAN.b, color.a);
                break;
            case 1:
                color.set(Color.GREEN.r, Color.GREEN.g, Color.GREEN.b, color.a);
                break;
            case 2:
                color.set(Color.YELLOW.r, Color.YELLOW.g, Color.YELLOW.b, color.a);
                break;
            case 3:
                color.set(Color.RED.r, Color.RED.g, Color.RED.b, color.a);
                break;
            case 4:
                color.set(Color.MAGENTA.r, Color.MAGENTA.g, Color.MAGENTA.b, color.a);
        }

        frame += 24 * dt;
        rotation += 48 * dt;
        if (frame >= textureRegions.length)
            frame = 0.0f;
        if (rotation >= 365)
            rotation = 0;

        if (born) {

            if (oscillator) {
                color.a -= dt / 2;
                if (color.a <= 0.0f) {
                    color.a = 0.0f;
                    oscillator = false;
                }
            } else {
                color.a += dt / 2;
                if (color.a >= PaintConstants.ANOMALY_GLOW_OSCILLATOR_MAX) {
                    color.a = PaintConstants.ANOMALY_GLOW_OSCILLATOR_MAX;
                    oscillator = true;
                }
            }

            boundCheck.checkAndHandleBounds(this);

            if (Gdx.input.isTouched() && level == 4) {
                tmp.set(Gdx.input.getX(), Gdx.input.getY());
                ScreenManager.getInstance().getViewport().unproject(tmp);
                if (tmp.dst(position) < 128f) {
                    hungry = false;
                    touched = true;
                }
            }

            slimTimer += dt;
            hungryTimer += dt;
            if (slimTimer > Rules.ANOMALIES_SLIM_TIME) {
                slimTimer = 0;
                if (level > 0) {
                    takeDamage(1);
                }
            }

            if (hungryTimer > Rules.ANOMALIES_HUNGRY_TIME) {
                hungryTimer = 0;
                hungry = true;
            }

            velocity.scl(0.99f);
            position.mulAdd(velocity, dt);
            if (hungry) {
                if (gs.getPlayer().getPosition().dst(position) < 2400.0f + 200 * level) {
                    float v;
                    if (getLevel() == 4) {
                        v = Rules.ALIENS_F_VELOCITY;
                    } else {
                        v = Rules.ANOMALIES_VELOCITY;
                    }
                    tmp.set(gs.getPlayer().getPosition()).sub(position).nor().scl(v);
                    velocity.mulAdd(tmp, dt);
                }
                if (gs.getPlayer().getPosition().dst(position) < 240 * level) {
                    tmp.set(gs.getPlayer().getPosition()).sub(position).nor().scl(250.0f - 20 * level);
                    velocity.mulAdd(tmp, dt);
                }
            }
        }
    }

    private void scaleAndBorn(float dt) {
        scaleX = scaleY += dt;
        if (touched) {
            slimTimer = 0;
            if (scaleX >= 2.0f) {
                destroy();
                boomBox.playSound(MSConstants.F_ALIEN_DESTROY);
            }
        } else {
            if (scaleX > 1 + level * 0.15f) {
                scaleX = scaleY = 1 + level * 0.15f;
                born = true;
            }
        }
    }

    public void takeDamage(int dmg) {
        level -= dmg;
    }

    @Override
    public void destroy() {
        active = false;
    }
}
