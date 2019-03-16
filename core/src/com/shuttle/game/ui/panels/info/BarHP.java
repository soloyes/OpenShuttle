package com.shuttle.game.ui.panels.info;

import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 3/14/19.
 */
public class BarHP extends AbstractBar {

    public BarHP(GameScreen gs) {
        super(gs);
        this.outBar = Assets.getInstance().getAtlas().findRegion(PaintConstants.HP_BAR_OUT);
        this.inBar = Assets.getInstance().getAtlas().findRegion(PaintConstants.HP_BAR_IN);
        this.icon = Assets.getInstance().getAtlas().findRegion(PaintConstants.HP_BAR_ICON);
        this.scale = Assets.getInstance().getAtlas().findRegion(PaintConstants.HP_BAR_SCALE);
        this.width = PaintConstants.HP_BAR_WIDTH;
        this.height = PaintConstants.HP_BAR_HEIGHT;
    }
}
