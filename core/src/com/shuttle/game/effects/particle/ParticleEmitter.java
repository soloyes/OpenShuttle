package com.shuttle.game.effects.particle;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.pool.AbstractPoolable;
import com.shuttle.game.utils.pool.ObjectPool;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by FlameXander on 02.07.2017.
 */
public class ParticleEmitter extends ObjectPool<ParticleEmitter.Particle> {
    private static ParticleEmitter instance;
    private TextureRegion particleTexture;
    private ParticleEffectBuilder builder;
    private Vector2 tmp;

    private ParticleEmitter() {
        this(Particle.class);
        this.particleTexture = Assets.getInstance().getAtlas().findRegion(PaintConstants.STAR16_REGION);
        this.builder = new ParticleEffectBuilder(this);
        this.tmp = new Vector2(0, 0);
    }

    private ParticleEmitter(Class<? extends Particle> clazz) {
        super(clazz);
    }

    @Override
    protected Particle newObject() {
        return new Particle();
    }

    @Override
    public void doRestart() {
        instance = null;
    }

    public void render(SpriteBatch batch) {
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        for (int i = 0; i < activeList.size(); i++) {
            Particle particle = activeList.get(i);
            batch.setColor(particle.getCurrentR(), particle.getCurrentG(), particle.getCurrentB(), particle.getCurrentA());
            activeList.get(i).render(batch);
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

    public static ParticleEmitter getInstance() {
        if (instance == null) {
            instance = new ParticleEmitter();
        }
        return instance;
    }

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

        @Override
        public void basicUpdate(float dt) {
            time += dt;
            calculateCurrentValues();
            position.mulAdd(velocity, dt);
            if (time > duration) {
                destroy();
            }
        }

        @Override
        public void update(float dt) {
            basicUpdate(dt);
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

        @Override
        public void render(SpriteBatch batch) {
            basicRender(batch);
        }

        @Override
        public void basicRender(SpriteBatch batch) {
            batch.draw(
                    particleTexture,
                    this.getPosition().x - 8, this.getPosition().y - 8,
                    8, 8,
                    16, 16,
                    this.getCurrentSize(), this.getCurrentSize(),
                    0
            );
        }
    }
}
