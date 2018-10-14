package com.shuttle.game.bot.consumable;

import com.badlogic.gdx.math.MathUtils;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.Assets;

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
    }

    @Override
    public void init(float x, float y) {
        position.set(x, y);
        angle = MathUtils.random(0, 360);
        rotationSpeed = MathUtils.random(-20, 20);
        active = true;
        scaleX = saveScaleX = 1.0f;
        scaleY = saveScaleY = 1.0f;
        if (boundCheck.checkAndHandleBoundsInCamera(this,gs.getCamera())){
            boomBox.playSound(MSConstants.ASTRONAUT_ALIVE);
        }
    }
}
