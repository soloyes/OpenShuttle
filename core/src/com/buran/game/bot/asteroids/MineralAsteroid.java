package com.buran.game.bot.asteroids;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.effects.particle.ParticleEmitter;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.Regions;

/**
 * @author soloyes on 11/26/18.
 */

public class MineralAsteroid extends AbstractAsteroid {
    public MineralAsteroid(ParticleEmitter particleEmitter) {
        super(particleEmitter);
        this.particleEmitter = particleEmitter;
        textureRegions = Regions.split(
                new TextureRegion(
                        Assets.getInstance()
                                .getAtlas()
                                .findRegion(PaintConstants.ASTEROID_MINERAL_REGION)),
                8, 8, PaintConstants.ASTEROID_MINERAL_FRAMES
        );
        type = Type.ASTEROID_MINERAL;
    }

    @Override
    public void init(float x, float y) {
        this.position.set(x, y);
        this.width = PaintConstants.ASTEROID_MINERAL_WIDTH;
        this.height = PaintConstants.ASTEROID_MINERAL_HEIGHT;
        this.active = true;
        this.taken = false;
        this.level = -5;
    }

    @Override
    public void destroyWithEffect() {
        super.destroyWithEffect();
        particleEmitter.getBuilder().buildDust(getPosition().x, getPosition().y, 180, 32 + 32);
    }
}