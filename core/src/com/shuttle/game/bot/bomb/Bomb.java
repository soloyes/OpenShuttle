package com.shuttle.game.bot.bomb;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.pool.AbstractPoolable;

/**
 * @author Shuttle on 19/01/19.
 */
public class Bomb extends AbstractPoolable {
    private Vector2 position;
    private ParticleEffect particleEffect;
    private GameScreen gs;
    private Vector2 velocity;
    private Vector2 destination;
    private float alpha = 1.0f;

    public Bomb(GameScreen gs, ParticleEffect particleEffect) {
        this.gs = gs;
        this.particleEffect = particleEffect;
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.destination = new Vector2();
        this.width = 128;
        this.height = 128;
    }

    @Override
    public void basicRender(SpriteBatch batch) {
        particleEffect.draw(batch);
    }

    @Override
    public void collideBlackhole() {
        float dst = gs.getBlackhole().getPosition().dst(position);
        if (dst <= Rules.BLACKHOLE_TAKE_RANGE) {
            taken = true;
            velocity.scl(0.5f);
        }
    }

    @Override
    public void basicUpdate(float dt) {
        if (taken) {
            alpha -= dt / 5;
            particleEffect.getEmitters().get(0).getTransparency().setHigh(alpha);
            if (alpha <= 0.0f) {
                alpha = 0.0f;
                destroy();
            }
        }
        particleEffect.update(dt);
    }

    @Override
    public void render(SpriteBatch batch) {
        basicRender(batch);
    }

    @Override
    public void update(float dt) {
        basicUpdate(dt);
        collideBlackhole();
        position.mulAdd(velocity, 10);
        particleEffect.setPosition(position.x, position.y);
        velocity.scl(0.99f);
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public void init(float x, float y) {
        this.position.set(x, y);
        this.active = true;
        this.destination.set(MathUtils.random(0, Rules.SPACE_WIDTH), MathUtils.random(0, Rules.SPACE_HEIGHT));
        this.velocity = destination.sub(position).nor();
        this.alpha = 1.0f;
        this.taken = false;
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
