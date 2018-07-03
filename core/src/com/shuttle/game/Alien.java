package com.shuttle.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.screens.GameScreen;

public class Alien implements Poolable {
    private GameScreen gs;
    private TextureRegion[] regions;
    private Vector2 position;
    private Vector2 velocity;
    private Vector2 tmp;
    private int level;
    private int maxLevel;
    private boolean active;
    private int width, height;
    //                       0     1     2     3
    // float[] probabilities = {0.4f, 0.7f, 0.9f, 1.0f};
    float[] probabilities = {1.0f, 0.6f, 0.3f, 0.1f};

    public int getLevel() {
        return level;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public boolean takeDamage(int dmg) {
        level -= dmg;
        return level < 0;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public Alien(GameScreen gs) {
        this.gs = gs;
        this.regions = Assets.getInstance().getAtlas().findRegion("aliens").split(128, 128)[0];
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.width = 128;
        this.height = 128;
        this.active = false;
        this.level = 1;
        this.tmp = new Vector2(0, 0);
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void init(float x, float y) {
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

    public void destroy() {
        active = false;
    }

    public void update(float dt) {
        velocity.scl(0.99f);
        position.mulAdd(velocity, dt);
        if (gs.getPlayer().getPosition().dst(position) < 2400.0f + 200 * level) {
            tmp.set(gs.getPlayer().getPosition()).sub(position).nor().scl(50.0f);
            velocity.mulAdd(tmp, dt);
        }
        if (gs.getPlayer().getPosition().dst(position) < 240 * level) {
            tmp.set(gs.getPlayer().getPosition()).sub(position).nor().scl(250.0f - 20 * level);
            velocity.mulAdd(tmp, dt);
        }
    }

    public Vector2 getPosition() {
        return position;
    }
}
