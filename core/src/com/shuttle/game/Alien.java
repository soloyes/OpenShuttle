package com.shuttle.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.screens.GameScreen;

import lombok.Data;

@Data
public class Alien implements Poolable {
    //                       0     1     2     3
    // float[] probabilities = {0.4f, 0.7f, 0.9f, 1.0f};
    float[] probabilities = {1.0f, 0.6f, 0.3f, 0.1f};
    private GameScreen gs;
    private TextureRegion[] regions;
    private Vector2 position;
    private Vector2 velocity;
    private Vector2 tmp;
    private int level;
    private int maxLevel;
    private boolean active;
    private int width, height;
    private float hungryTimer;
    private boolean hungry;
    private float slimTimer;

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
    }

    public void destroy() {
        active = false;
    }

    public void init(float x, float y) {

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
        batch.draw(regions[level], position.x - width / 2, position.y - height / 2, width / 2, height / 2, width, height, 1 + level * 0.15f, 1 + level * 0.15f, 0);
    }

    public void update(float dt) {
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
                tmp.set(gs.getPlayer().getPosition()).sub(position).nor().scl(50.0f);
                velocity.mulAdd(tmp, dt);
            }
            if (gs.getPlayer().getPosition().dst(position) < 240 * level) {
                tmp.set(gs.getPlayer().getPosition()).sub(position).nor().scl(250.0f - 20 * level);
                velocity.mulAdd(tmp, dt);
            }
        }
    }

    public boolean takeDamage(int dmg) {
        level -= dmg;
        return level < 0;
    }
}
