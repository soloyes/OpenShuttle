package com.shuttle.game.bot.consumable;

import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 10/11/18.
 */
public class Crate extends Consumable {
    public Crate() {
        this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.CRATE_REGION);
        this.width = PaintConstants.CRATE_WIDTH;
        this.height = PaintConstants.CRATE_HEIGHT;
        this.type = Type.CRATE;
    }
}
