package com.shuttle.game.bot.particle;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.pool.AbstractPoolable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by FlameXander on 02.07.2017.
 */
public class Particle extends AbstractPoolable {
    private float time, duration;
    private float startSize, endSize;
    @Getter
    @Setter
    private float currentSize;
    private float startR, startG, startB, startA;
    @Getter
    @Setter
    private float currentR, currentG, currentB, currentA;
    private float endR, endG, endB, endA;

    public Particle() {
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.startSize = 1.0f;
        this.endSize = 1.0f;
    }

    public void init(float x, float y, float vx, float vy, float duration, float startSize, float endSize, float startR, float startG, float startB, float startA, float endR, float endG, float endB, float endA) {
        init(x, y);
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

    @Override
    public void init(float x, float y) {
        this.position.x = x;
        this.position.y = y;
    }

    private void calculateCurrentValues() {
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

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public boolean isActive() {
        return active;
    }
}