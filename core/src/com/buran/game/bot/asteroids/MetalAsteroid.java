package com.buran.game.bot.asteroids;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.buran.game.conf.PaintConstants;
import com.buran.game.effects.particle.ParticleEmitter;
import com.buran.game.utils.Assets;
import com.buran.game.utils.Regions;

/**
 * @author soloyes on 11/26/18.
 */

public class MetalAsteroid extends AbstractAsteroid {
    public MetalAsteroid(ParticleEmitter particleEmitter) {
        super(particleEmitter);
        this.particleEmitter = particleEmitter;
        textureRegions = Regions.split(
                new TextureRegion(
                        Assets.getInstance()
                                .getAtlas()
                                .findRegion(PaintConstants.ASTEROID_METAL_REGION)),
                8, 8, PaintConstants.ASTEROID_METAL_FRAMES
        );
        type = Type.ASTEROID_METAL;
    }

    @Override
    public void init(float x, float y) {
        this.position.set(x, y);
        this.width = PaintConstants.ASTEROID_METAL_WIDTH;
        this.height = PaintConstants.ASTEROID_METAL_HEIGHT;
        this.active = true;
        this.taken = false;
        this.level = -4;
    }

    @Override
    public void destroyWithEffect() {
        super.destroyWithEffect();
        particleEmitter.getBuilder().buildDust(getPosition().x, getPosition().y, 180, 32 + 32);
    }
}