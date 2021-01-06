package com.buran.game.ui.panels.info;

import com.buran.game.conf.PaintConstants;
import com.buran.game.conf.Rules;
import com.buran.game.utils.Assets;

/**
 * @author soloyes on 3/14/19.
 */

public class BarHP extends AbstractBar {
    public BarHP() {
        this.scaleBar = Assets.getInstance().getAtlas().findRegion(PaintConstants.HP_BAR_SCALE);
        this.bar = Assets.getInstance().getAtlas().findRegion(PaintConstants.HP_BAR);
        this.icon = Assets.getInstance().getAtlas().findRegion(PaintConstants.HP_BAR_ICON);
        this.width = Rules.WORLD_WIDTH / 4;
        this.height = PaintConstants.BAR_HEIGHT;
        this.side = Side.LEFT;
        this.position.set(53 + PaintConstants.XP_WIDTH + 12, Rules.WORLD_HEIGHT - 168);
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
        glyphLayout.setText(font54,
                stringBuilder
                        .append((int)standing.getHp())
                        .append(" (")
                        .append((int)(100 * standing.getHp() / standing.getMaxHp()))
                        .append("%)")
        );
    }
}