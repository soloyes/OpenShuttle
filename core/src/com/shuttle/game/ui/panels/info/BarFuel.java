package com.shuttle.game.ui.panels.info;

import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 3/14/19.
 */

public class BarFuel extends AbstractBar {
    public BarFuel() {
        this.scaleBar = Assets.getInstance().getAtlas().findRegion(PaintConstants.FP_BAR_SCALE);
        this.bar = Assets.getInstance().getAtlas().findRegion(PaintConstants.FP_BAR);
        this.icon = Assets.getInstance().getAtlas().findRegion(PaintConstants.FP_BAR_ICON);
        this.width = Rules.WORLD_WIDTH / 4;
        this.height = PaintConstants.BAR_HEIGHT;
        this.side = Side.RIGHT;
        this.position.set(Rules.WORLD_WIDTH - width - 43 - PaintConstants.PLAY_PAUSE_BUTTON_WIDTH - 14, Rules.WORLD_HEIGHT - 168);
    }

    @Override
    void update(float dt) {
        percentage = 1.0f * standing.getFuel() / standing.getMaxFuel();
        if (standing.isFuelAlertOutOfBounds() || standing.isFuelAlertMagneticAnomaly()) {
            color.set(1.0f, 0.2f, 0.0f, 1f);
        } else {
            color.set(1.0f, 0.94f, 0.0f, 1.0f);
        }
        stringBuilder.setLength(0);
        glyphLayout.setText(font32,
                stringBuilder
                        .append((int) standing.getFuel())
                        .append(" (")
                        .append((int) (100 * standing.getFuel() / standing.getMaxFuel()))
                        .append("%)")
        );
    }
}