package com.shuttle.game.ui.panels.info;

import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 3/14/19.
 */

public class BarLaser extends AbstractBar {
    public BarLaser() {
        this.scaleBar = Assets.getInstance().getAtlas().findRegion(PaintConstants.LP_BAR_SCALE);
        this.bar = Assets.getInstance().getAtlas().findRegion(PaintConstants.LP_BAR);
        this.icon = Assets.getInstance().getAtlas().findRegion(PaintConstants.LP_BAR_ICON);
        this.width = PaintConstants.LP_BAR_WIDTH;
        this.height = PaintConstants.LP_BAR_HEIGHT;
        this.side = Side.RIGHT;
        this.position.set(631, 1830);
    }

    @Override
    void update(float dt) {
        percentage = 1.0f * standing.getLaserChargeLevel() / standing.getMaxLaserChargeLevel();
        color.set(1.0f, 0.67f, 1.0f, 1.0f);
        stringBuilder.setLength(0);
        glyphLayout.setText(font32,
                stringBuilder
                        .append((int) standing.getLaserChargeLevel())
                        .append(" (")
                        .append((int) (100 * standing.getLaserChargeLevel() / standing.getMaxLaserChargeLevel()))
                        .append("%)")
        );
    }
}