package com.shuttle.game.ui.panels.info;

import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 3/14/19.
 */
public class BarFuel extends AbstractBar {

    public BarFuel(GameScreen gs) {
        super(gs);
        this.outBar = Assets.getInstance().getAtlas().findRegion(PaintConstants.FP_BAR_OUT);
        this.inBar = Assets.getInstance().getAtlas().findRegion(PaintConstants.FP_BAR_IN);
        this.icon = Assets.getInstance().getAtlas().findRegion(PaintConstants.FP_BAR_ICON);
        this.scale = Assets.getInstance().getAtlas().findRegion(PaintConstants.FP_BAR_SCALE);
        this.width = PaintConstants.FP_BAR_WIDTH;
        this.height = PaintConstants.FP_BAR_HEIGHT;
        this.side = Side.RIGHT;
        this.position.set(3 * Rules.WORLD_WIDTH / 4 - width + 32, Rules.WORLD_HEIGHT - 4 * height);
    }

    @Override
    void update(float dt) {
        percentage = 1.0f * standing.getFuel() / standing.getMaxFuel();
    }
}
