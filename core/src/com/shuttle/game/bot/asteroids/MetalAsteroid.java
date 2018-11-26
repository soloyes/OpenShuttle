package com.shuttle.game.bot.asteroids;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.Regions;

/**
 * @author Shuttle on 11/26/18.
 */
public class MetalAsteroid extends AbstractAsteroid {
    public MetalAsteroid(GameScreen gs) {
        super(gs);
        textureRegions = Regions.split(
                new TextureRegion(
                        Assets.getInstance()
                                .getAtlas()
                                .findRegion(PaintConstants.ASTEROID_METAL_REGION)),
                8, 8, 64
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
    }
}
