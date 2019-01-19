package com.shuttle.game.bot.bomb;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.pool.AbstractPoolable;

/**
 * @author Shuttle on 19/01/19.
 */
public class Bomb extends AbstractPoolable {
    private Vector2 position;
    private ParticleEffect particleEffect;
    private BombParticleEffectPool bombParticleEffectPool;

    public Bomb(BombParticleEffectPool bombParticleEffectPool) {
        this.bombParticleEffectPool = bombParticleEffectPool;
        this.particleEffect = bombParticleEffectPool.obtain();
        this.position = new Vector2();
    }

    @Override
    public void basicRender(SpriteBatch batch) {
        particleEffect.draw(batch);
    }

    @Override
    public void basicUpdate(float dt) {
        particleEffect.update(dt);
    }

    @Override
    public void render(SpriteBatch batch) {
        basicRender(batch);
    }

    @Override
    public void update(float dt) {
        basicUpdate(dt);
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public void init(float x, float y) {
        this.position.set(x, y);
        this.active = true;
        this.particleEffect.setPosition(position.x, position.y);
    }

    @Override
    public void destroy() {
        super.destroy();
        this.bombParticleEffectPool.free((ParticleEffectPool.PooledEffect) particleEffect);
    }
}
