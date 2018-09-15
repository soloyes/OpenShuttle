package com.shuttle.game.bot.particle;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.pool.ObjectPool;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.utils.Assets;

/**
 * Created by FlameXander on 02.07.2017.
 */
public class ParticleEmitter extends ObjectPool<Particle> {
    private TextureRegion particleTexture;
    private ParticleEffectBuilder builder;
    private Vector2 tmp;

    public ParticleEmitter() {
        this.particleTexture = Assets.getInstance().getAtlas().findRegion(PaintConstants.STAR16_REGION);
        this.builder = new ParticleEffectBuilder(this);
        this.tmp = new Vector2(0, 0);
    }

    @Override
    protected Particle newObject() {
        return new Particle();
    }

    public void render(SpriteBatch batch) {
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        for (int i = 0; i < activeList.size(); i++) {
            Particle particle = activeList.get(i);
            batch.setColor(particle.getCurrentR(), particle.getCurrentG(), particle.getCurrentB(), particle.getCurrentA());
            batch.draw(particleTexture, particle.getPosition().x - 8, particle.getPosition().y - 8, 8, 8, 16, 16, particle.getCurrentSize(), particle.getCurrentSize(), 0);
        }
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void setup(float x, float y, float vx, float vy, float duration, float startSize, float endSize, float startR, float startG, float startB, float startA, float endR, float endG, float endB, float endA) {
        getActiveElement().init(x, y, vx, vy, duration, startSize, endSize, startR, startG, startB, startA, startR, startG, startB, startA);
    }

    public void setupFromPointToPoint(float x1, float y1, float x2, float y2, float size1, float size2, float r1, float g1, float b1, float a1, float r2, float g2, float b2, float a2) {
        float dst = Vector2.dst(x1, y1, x2, y2) / 10.0f;
        tmp.set(x2, y2);
        tmp.sub(x1, y1);
        tmp.nor();
        tmp.scl(10.0f);
        for (int i = 0; i < (int) dst; i++) {
            float ox = x1 + i * tmp.x;
            float oy = y1 + i * tmp.y;
            getActiveElement().init(ox + MathUtils.random(-10, 10), oy + MathUtils.random(-10, 10), 0, 0, 0.1f, size1, size2, r1, g1, b1, a1, r2, g2, b2, a2);
        }
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }

    public ParticleEffectBuilder getBuilder() {
        return builder;
    }
}
