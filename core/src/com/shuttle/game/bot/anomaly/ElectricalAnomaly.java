package com.shuttle.game.bot.anomaly;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.AbstractPoolable;
import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.BoomBox;
import com.shuttle.game.utils.BoundCheck;
import com.shuttle.game.utils.Regions;

import lombok.Data;

/**
 * @author Shuttle on 9/11/18.
 */
@Data
public class ElectricalAnomaly extends AbstractPoolable {
    private GameScreen gs;
    private TextureRegion[] textureRegions;
    private Vector2 tmp;
    private boolean touched;
    private boolean born;
    private boolean hungry;
    private BoundCheck boundCheck;
    private BoomBox boomBox;
    private Color color;

    public ElectricalAnomaly(GameScreen gs) {
        this.boomBox = new BoomBox(new GamePreferences());
        this.gs = gs;
        this.textureRegions = Regions.split(new TextureRegion(Assets.getInstance().getAssetManager().get(PaintConstants.ANOMALY_ELECTRIC_REGION, Texture.class)),
                4,
                4,
                16
        );
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.tmp = new Vector2();
        this.width = PaintConstants.ANOMALY_ELECTRIC_WIDTH;
        this.height = PaintConstants.ANOMALY_ELECTRIC_HEIGHT;
        this.boundCheck = new BoundCheck();
        this.color = new Color();
    }

    public void init(float x, float y) {
        this.touched = false;
        this.hungry = true;
        this.angle = MathUtils.random(365);
        this.clockwise = MathUtils.randomSign();
        this.position.set(x, y);
        this.active = true;
        this.born = false;
        this.level = 4;
        this.scaleX = scaleY = 1 + level * 0.15f;
        this.color.a = 0.0f;
    }

    public void render(SpriteBatch batch) {
        batch.setColor(1.0f, 1.0f, 1.0f, color.a);
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

        frame += 24 * dt;
        angle += 48 * dt;
        if (frame >= textureRegions.length)
            frame = 0.0f;
        if (angle >= 365)
            angle = 0;

        if (born) {

            boundCheck.checkAndHandleBounds(this);

            if (Gdx.input.isTouched()) {
                tmp.set(Gdx.input.getX(), Gdx.input.getY());
                ScreenManager.getInstance().getViewport().unproject(tmp);
                if (tmp.dst(position) < 128f) {
                    hungry = false;
                    touched = true;
                }
            }

            velocity.scl(0.99f);
            position.mulAdd(velocity, dt);
            if (hungry) {
                if (gs.getPlayer().getPosition().dst(position) < 2400.0f + 200 * level) {

                    tmp.set(gs.getPlayer().getPosition()).sub(position).nor().scl(Rules.ALIENS_F_VELOCITY);
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
        if (touched) {
            scaleX = scaleY += dt;
            if (scaleX >= 2.0f) {
                destroy();
                boomBox.playSound(MSConstants.ANOMALY_ELECTRIC_DESTROY);
            }
        } else {
            color.a += dt / 5;
            if (color.a >= 1.0) {
                color.a = 1.0f;
                born = true;
            }
        }
    }
}
