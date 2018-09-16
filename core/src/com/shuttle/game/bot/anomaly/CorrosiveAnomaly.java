package com.shuttle.game.bot.anomaly;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.BoomBox;
import com.shuttle.game.utils.BoundCheck;
import com.shuttle.game.utils.Regions;

import lombok.Data;

@Data
public class CorrosiveAnomaly extends AbstractAnomaly {
    float[] probabilities = {1.0f, 0.8f, 0.5f, 0.3f};
    private TextureRegion glowRegion;
    private float slimTimer;
    private int glowWidth, glowHeight;
    private boolean oscillator;

    public CorrosiveAnomaly(GameScreen gs) {
        this.boomBox = new BoomBox(new GamePreferences());
        this.gs = gs;
        this.textureRegions = Regions.split(new TextureRegion(Assets.getInstance().getAssetManager().get(PaintConstants.ANOMALY_CORROSIVE_REGION, Texture.class)),
                3,
                4,
                12
        );
        this.glowRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.ANOMALY_GLOW);
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.tmp = new Vector2();
        this.width = PaintConstants.ANOMALY_CORROSIVE_WIDTH;
        this.height = PaintConstants.ANOMALY_CORROSIVE_HEIGHT;
        this.glowWidth = PaintConstants.ANOMALY_GLOW_WIDTH;
        this.glowHeight = PaintConstants.ANOMALY_GLOW_HEIGHT;
        this.boundCheck = new BoundCheck();
        this.color = new Color();
    }

    @Override
    public void init(float x, float y) {
        this.hungryTimer = 0;
        this.slimTimer = 0;
        this.hungry = true;
        this.angle = MathUtils.random(365);
        this.clockwise = MathUtils.randomSign();
        this.position.set(x, y);
        this.active = true;
        this.born = false;
        this.oscillator = true;
        this.color.a = PaintConstants.ANOMALY_GLOW_OSCILLATOR_MAX;
        float rnd = MathUtils.random(0.0f, 1.0f);
        for (int i = probabilities.length - 1; i > 0; i--) {
            if (rnd < probabilities[i]) {
                level = i;
                break;
            }
        }
        this.interacting = true;
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
                clockwise * angle
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
                clockwise * angle
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
        }

        frame += 24 * dt;
        angle += 48 * dt;
        if (frame >= textureRegions.length)
            frame = 0.0f;
        if (angle >= 365)
            angle = 0;

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

            slimTimer += dt;
            if (slimTimer > Rules.ANOMALIES_CORROSIVE_SLIM_TIME) {
                slimTimer = 0;
                if (level > 0) {
                    takeDamage(1);
                }
            }

            if (!hungry) {
                hungryTimer += dt;
            }

            if (hungryTimer > Rules.ANOMALIES_CORROSIVE_HUNGRY_TIME) {
                hungryTimer = 0;
                hungry = true;
            }

            velocity.scl(0.99f);
            position.mulAdd(velocity, dt);
            if (hungry) {
                if (gs.getPlayer().getPosition().dst(position) < 2400.0f + 200 * level) {
                    tmp.set(gs.getPlayer().getPosition()).sub(position).nor().scl(Rules.ANOMALIES_CORROSIVE_VELOCITY);
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
        if (scaleX > 1 + level * 0.15f) {
            scaleX = saveScaleX = 1 + level * 0.15f;
            scaleY = saveScaleY = 1 + level * 0.15f;
            born = true;
        }
    }

    public void takeDamage(int dmg) {
        level -= dmg;
    }
}