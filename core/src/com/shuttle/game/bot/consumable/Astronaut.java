package com.shuttle.game.bot.consumable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.effects.MoveProcessor;

/**
 * @author Shuttle on 10/11/18.
 */
public class Astronaut extends Consumable {
    public Astronaut(GameScreen gs) {
        super(gs);
        this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.ASTRONAUT_REGION);
        this.width = PaintConstants.ASTRONAUT_WIDTH;
        this.height = PaintConstants.ASTRONAUT_HEIGHT;
        this.type = Type.ASTRONAUT;
        this.moveProcessor = new MoveProcessor(gs);
    }

    @Override
    public void init(float x, float y) {
        this.position.set(x, y);
        this.angle = MathUtils.random(0, 360);
        this.rotationSpeed = MathUtils.random(-20, 20);
        this.scaleX = this.saveScaleX = 1.0f;
        this.scaleY = this.saveScaleY = 1.0f;
        this.active = true;
        this.taken = false;
        this.color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        this.spiralRadius = MathUtils.random(20, 60);
        playSound();
    }

    private void playSound() {
        if (!fake && boundCheck.checkAndHandleBoundsInCamera(this, gs.getCamera())) {
            boomBox.playSound(MSConstants.ASTRONAUT_ALIVE);
        }
    }

    @Override
    public void update(float dt) {
        basicUpdate(dt);
        moveProcessor.update(this, dt);
        boundCheck.checkAndHandleBoundsAndDestroy(this);
    }
}
