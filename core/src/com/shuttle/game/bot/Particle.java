package com.shuttle.game.bot;

import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.Poolable;

import lombok.Data;

/**
 * Created by FlameXander on 02.07.2017.
 */
@Data
public class Particle implements Poolable {
    private Vector2 position;
    private Vector2 velocity;
    private float r1, g1, b1, a1;
    private float r2, g2, b2, a2;
    private boolean active;
    private float time;
    private float duration;
    private float size1, size2;

    public Particle() {
        position = new Vector2(0, 0);
        velocity = new Vector2(0, 0);
        size1 = 1.0f;
        size2 = 1.0f;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void init(float x, float y, float vx, float vy, float duration, float size1, float size2, float r1, float g1, float b1, float a1, float r2, float g2, float b2, float a2) {
        this.position.x = x;
        this.position.y = y;
        this.velocity.x = vx;
        this.velocity.y = vy;
        this.r1 = r1;
        this.r2 = r2;
        this.g1 = g1;
        this.g2 = g2;
        this.b1 = b1;
        this.b2 = b2;
        this.a1 = a1;
        this.a2 = a2;
        this.time = 0.0f;
        this.duration = duration;
        this.size1 = size1;
        this.size2 = size2;
        this.active = true;
    }

    public void update(float dt) {
        time += dt;
        position.mulAdd(velocity, dt);
        if (time > duration) {
            deactivate();
        }
    }

    public void deactivate() {
        active = false;
    }
}