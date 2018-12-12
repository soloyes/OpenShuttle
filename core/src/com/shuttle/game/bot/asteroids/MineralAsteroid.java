package com.shuttle.game.bot.asteroids;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.Regions;

/**
 * @author Shuttle on 11/26/18.
 */
public class MineralAsteroid extends AbstractAsteroid {
    public MineralAsteroid(GameScreen gs) {
        super(gs);
        textureRegions = Regions.split(
                new TextureRegion(
                        Assets.getInstance()
                                .getAtlas()
                                .findRegion(PaintConstants.ASTEROID_MINERAL_REGION)),
                8, 8, 32
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
        gs.getParticleEmitter().getBuilder().buildDust(getPosition().x, getPosition().y, 180, 32 + 32);
    }
}
