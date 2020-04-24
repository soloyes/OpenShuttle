package com.shuttle.game.ui.panels.info;

import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 15/03/19.
 */

public class BarShield extends AbstractBar {
    public BarShield() {
        this.scaleBar = Assets.getInstance().getAtlas().findRegion(PaintConstants.SP_BAR_SCALE);
        this.bar = Assets.getInstance().getAtlas().findRegion(PaintConstants.SP_BAR);
        this.icon = Assets.getInstance().getAtlas().findRegion(PaintConstants.SP_BAR_ICON);
        this.width = PaintConstants.SP_BAR_WIDTH;
        this.height = PaintConstants.SP_BAR_HEIGHT;
        this.side = Side.LEFT;
        this.position.set(197, 1830);
    }

    @Override
    void update(float dt) {
        percentage = 1.0f * standing.getLifeShield() / standing.getMaxLifeShield();
        if (standing.isHpAlert() && standing.getLifeShield() > 0) {
            color.set(1.0f, 0.2f, 0.0f, 1f);
        } else {
            color.set(0.28f, 1.0f, 1.0f, 1.0f);
        }
        stringBuilder.setLength(0);
        glyphLayout.setText(font32,
                stringBuilder
                        .append((int) standing.getLifeShield())
                        .append(" (")
                        .append((int) (100 * standing.getLifeShield() / standing.getMaxLifeShield()))
                        .append("%)")
        );
    }
}