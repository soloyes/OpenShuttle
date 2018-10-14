package com.shuttle.game.bot.consumable;


import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 10/11/18.
 */
public class Astronaut extends Consumable{
    public Astronaut() {
        this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.ASTRONAUT_REGION);
        this.width = PaintConstants.ASTRONAUT_WIDTH;
        this.height = PaintConstants.ASTRONAUT_HEIGHT;
        this.type = Type.ASTRONAUT;
    }
}
