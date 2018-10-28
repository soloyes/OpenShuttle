package com.shuttle.game.bot.consumable;

import com.badlogic.gdx.math.MathUtils;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.MoveProcessor;

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
        this.moveProcessor = new MoveProcessor(gs);
    }

    @Override
    public void init(float x, float y) {
        this.position.set(x, y);
        this.angle = MathUtils.random(0, 360);
        this.rotationSpeed = MathUtils.random(-20, 20);
        this.active = true;
        this.scaleX = this.saveScaleX = 1.0f;
        this.scaleY = this.saveScaleY = 1.0f;
        this.taken = false;
    }

    @Override
    public void update(float dt) {
        basicUpdate(dt);
        moveProcessor.update(this, dt);
        boundCheck.checkAndHandleBoundsAndDestroy(this);
    }
}
