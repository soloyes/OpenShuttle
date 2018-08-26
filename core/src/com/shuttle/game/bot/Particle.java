package com.shuttle.game.bot;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import lombok.Data;

/**
 * Created by FlameXander on 02.07.2017.
 */
@Data
public class Particle extends AbstractPoolable {
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;
    private float time, duration;
    private float startSize, currentSize, endSize;
    private float startR, startG, startB, startA;
    private float currentR, currentG, currentB, currentA;
    private float endR, endG, endB, endA;

    public Particle() {
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.startSize = 1.0f;
        this.endSize = 1.0f;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void init(float x, float y, float vx, float vy, float duration, float startSize, float endSize, float startR, float startG, float startB, float startA, float endR, float endG, float endB, float endA) {
        this.position.x = x;
        this.position.y = y;
        this.velocity.x = vx;
        this.velocity.y = vy;
        this.startR = startR;
        this.startG = startG;
        this.startB = startB;
        this.startA = startA;
        this.endR = endR;
        this.endG = endG;
        this.endB = endB;
        this.endA = endA;
        this.time = 0.0f;
        this.duration = duration;
        this.startSize = startSize;
        this.endSize = endSize;
        this.active = true;
        calculateCurrentValues();
    }

    public void calculateCurrentValues() {
        float percentage = time / duration;
        currentSize = MathUtils.lerp(startSize, endSize, percentage);
        currentR = MathUtils.lerp(startR, endR, percentage);
        currentG = MathUtils.lerp(startG, endG, percentage);
        currentB = MathUtils.lerp(startB, endB, percentage);
        currentA = MathUtils.lerp(startA, endA, percentage);
    }

    public void update(float dt) {
        time += dt;
        calculateCurrentValues();
        position.mulAdd(velocity, dt);
        if (time > duration) {
            destroy();
        }
    }

    @Override
    public void destroy() {
        active = false;
    }
}