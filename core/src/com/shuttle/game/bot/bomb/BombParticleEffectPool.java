package com.shuttle.game.bot.bomb;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * @author Shuttle on 19/01/19.
 */
public class BombParticleEffectPool extends ParticleEffectPool {
    private ParticleEffect effect;
    private BombParticleEffectPool(ParticleEffect effect, int initialCapacity, int max) {
        super(effect, initialCapacity, max);
    }

    public static class BombParticleBuilder {
        private ParticleEffect particleEffect;
        private int initialCapacity;
        private int maxCapacity;

        public BombParticleBuilder() {
        }

        public BombParticleBuilder setParticleEffect(FileHandle effectFile, TextureAtlas atlas) {
            particleEffect = new ParticleEffect();
            particleEffect.load(effectFile, atlas);
            return this;
        }

        public BombParticleBuilder setInitialCapacity(int initialCapacity) {
            this.initialCapacity = initialCapacity;
            return this;
        }

        public BombParticleBuilder setMaxCapacity(int maxCapacity) {
            this.maxCapacity = maxCapacity;
            return this;
        }

        public BombParticleEffectPool build() {
            BombParticleEffectPool bombParticleEffectPool = new BombParticleEffectPool(this.particleEffect, this.initialCapacity, this.maxCapacity);
            bombParticleEffectPool.effect = this.particleEffect;
            return bombParticleEffectPool;
        }
    }

    public void dispose(){
        this.effect.dispose();
    }
}
