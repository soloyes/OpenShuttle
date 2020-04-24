package com.shuttle.game.ui.panels.info;

import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 3/14/19.
 */

public class BarHP extends AbstractBar {
    public BarHP() {
        this.scaleBar = Assets.getInstance().getAtlas().findRegion(PaintConstants.HP_BAR_SCALE);
        this.bar = Assets.getInstance().getAtlas().findRegion(PaintConstants.HP_BAR);
        this.icon = Assets.getInstance().getAtlas().findRegion(PaintConstants.HP_BAR_ICON);
        this.width = PaintConstants.BAR_WIDTH;
        this.height = PaintConstants.BAR_HEIGHT;
        this.side = Side.LEFT;
        this.position.set(197, 1751);
    }

    @Override
    void update(float dt) {
        percentage = 1.0f * standing.getHp() / standing.getMaxHp();
        if (standing.isHpAlert() && standing.getLifeShield() <= 0) {
            color.set(1.0f, 0.2f, 0.0f, 1f);
        } else {
            color.set(0.8f, 0.43f, 0.33f, 1f);
        }
        stringBuilder.setLength(0);
        glyphLayout.setText(font32,
                stringBuilder
                        .append(standing.getHp())
                        .append(" (")
                        .append(100 * standing.getHp() / standing.getMaxHp())
                        .append("%)")
        );
    }
}