package com.shuttle.game.ui.panels.info;

import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 3/14/19.
 */

public class BarLaser extends AbstractBar {

    public BarLaser() {
        this.outBar = Assets.getInstance().getAtlas().findRegion(PaintConstants.LP_BAR_OUT);
        this.inBar = Assets.getInstance().getAtlas().findRegion(PaintConstants.LP_BAR_IN);
        this.icon = Assets.getInstance().getAtlas().findRegion(PaintConstants.LP_BAR_ICON);
        this.scale = Assets.getInstance().getAtlas().findRegion(PaintConstants.LP_BAR_SCALE);
        this.width = PaintConstants.LP_BAR_WIDTH;
        this.height = PaintConstants.LP_BAR_HEIGHT;
        this.side = Side.RIGHT;
        this.position.set(3 * Rules.WORLD_WIDTH / 4 - width + 32, Rules.WORLD_HEIGHT - 2 * height - 20);
    }

    @Override
    void update(float dt) {
        percentage = 1.0f * standing.getLaserChargeLevel() / standing.getMaxLaserChargeLevel();
        color.set(1.0f, 1.0f, 1.0f, 1.0f);
    }
}