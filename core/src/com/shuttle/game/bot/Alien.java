package com.shuttle.game.bot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.BoundCheck;

import lombok.Data;

@Data
public class Alien extends AbstractPoolable {
    float[] probabilities = {1.0f, 0.8f, 0.5f, 0.3f, 0.05f};
    private GameScreen gs;
    private TextureRegion[] regions;
    private Vector2 position;
    private Vector2 velocity;
    private Vector2 tmp;
    private int level;
    private int maxLevel;
    private boolean active;
    private float hungryTimer;
    private boolean hungry;
    private float slimTimer;
    private float scaleX;
    private float scaleY;
    private boolean touched;
    private BoundCheck boundCheck;

    public Alien(GameScreen gs) {
        this.gs = gs;
        this.regions = Assets.getInstance().getAtlas().findRegion("aliens").split(128, 128)[0];
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.tmp = new Vector2();
        this.width = 128;
        this.height = 128;
        this.level = 1;
        this.active = false;
        this.boundCheck = new BoundCheck();
    }

    public void init(float x, float y) {
        this.touched = false;
        this.hungryTimer = 0;
        this.slimTimer = 0;
        this.hungry = true;

        position.set(x, y);
        active = true;
        float rnd = MathUtils.random(0.0f, 1.0f);
        for (int i = probabilities.length - 1; i >= 0; i--) {
            if (rnd < probabilities[i]) {
                level = i;
                maxLevel = level;
                break;
            }
        }
    }

    public void render(SpriteBatch batch) {
        if (!touched) {
            this.scaleX = this.scaleY = 1 + level * 0.15f;
        }
        batch.draw(regions[level], position.x - width / 2, position.y - height / 2, width / 2, height / 2, width, height, scaleX, scaleY, 0);
    }

    public void update(float dt) {
        boundCheck.checkAndHandleBounds(this);

        if (Gdx.input.isTouched() && level == 4) {
            tmp.set(Gdx.input.getX(), Gdx.input.getY());
            ScreenManager.getInstance().getViewport().unproject(tmp);
            if (tmp.dst(position) < 128f) {
                hungry = false;
                touched = true;
            }
        }
        if (touched) {
            slimTimer = 0;
            scaleX = scaleY += 0.01f;
            if (scaleX >= 2.0f) {
                destroy();
            }
        }

        slimTimer += dt;
        hungryTimer += dt;
        if (slimTimer > Rules.ALIENS_SLIM_TIME) {
            slimTimer = 0;
            if (level > 0) {
                takeDamage(1);
            }
        }

        if (hungryTimer > Rules.ALIENS_HUNGRY_TIME) {
            hungryTimer = 0;
            hungry = true;
        }

        velocity.scl(0.99f);
        position.mulAdd(velocity, dt);
        if (hungry) {
            if (gs.getPlayer().getPosition().dst(position) < 2400.0f + 200 * level) {
                float v;
                if (getLevel() == 4) {
                    v = 500.00f;
                } else {
                    v = 50.0f;
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

    @Override
    public void destroy() {
        active = false;
    }

    public void takeDamage(int dmg) {
        level -= dmg;
    }
}
