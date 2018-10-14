package com.shuttle.game.bot.consumable;

import com.badlogic.gdx.math.MathUtils;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 10/11/18.
 */
public class Crate extends Consumable {
    public Crate(GameScreen gs) {
        super(gs);
        this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.CRATE_REGION);
        this.width = PaintConstants.CRATE_WIDTH;
        this.height = PaintConstants.CRATE_HEIGHT;
        this.type = Type.CRATE;
    }

    @Override
    public void init(float x, float y) {
        position.set(x, y);
        angle = MathUtils.random(0, 360);
        rotationSpeed = MathUtils.random(-20, 20);
        active = true;
        scaleX = saveScaleX = 1.0f;
        scaleY = saveScaleY = 1.0f;
    }
}
