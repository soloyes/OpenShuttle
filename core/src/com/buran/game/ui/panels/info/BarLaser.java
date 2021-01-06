package com.buran.game.ui.panels.info;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.buran.game.conf.PaintConstants;
import com.buran.game.conf.Rules;
import com.buran.game.player.Buran;
import com.buran.game.utils.Assets;

/**
 * @author soloyes on 3/14/19.
 */

public class BarLaser extends AbstractBar {
    private TextureRegion block;

    public BarLaser() {
        this.scaleBar = Assets.getInstance().getAtlas().findRegion(PaintConstants.LP_BAR_SCALE);
        this.bar = Assets.getInstance().getAtlas().findRegion(PaintConstants.LP_BAR);
        this.icon = Assets.getInstance().getAtlas().findRegion(PaintConstants.LP_BAR_ICON);
        this.block = Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_OFF);
        this.width = Rules.WORLD_WIDTH / 4;
        this.height = PaintConstants.BAR_HEIGHT;
        this.side = Side.RIGHT;
        this.position.set(Rules.WORLD_WIDTH - width - 53 - PaintConstants.PLAY_PAUSE_BUTTON_WIDTH - 14, Rules.WORLD_HEIGHT - 90);
    }

    @Override
    void update(float dt) {
        percentage = 1.0f * standing.getLaserChargeLevel() / standing.getMaxLaserChargeLevel();
        color.set(1.0f, 0.67f, 1.0f, 1.0f);
        stringBuilder.setLength(0);
        glyphLayout.setText(font54,
                stringBuilder
                        .append((int) standing.getLaserChargeLevel())
                        .append(" (")
                        .append((int) (100 * standing.getLaserChargeLevel() / standing.getMaxLaserChargeLevel()))
                        .append("%)")
        );
    }

    @Override
    protected void render(SpriteBatch batch) {
        super.render(batch);
        if (!Buran.isLaser) {
            Color color = batch.getColor();
            batch.setColor(Color.RED);
            batch.draw(block,
                    side.getFactor() == 0 ? position.x : position.x + width - iconWidth, position.y + height + 2,
                    iconWidth * 1.5f, iconWidth * 1.5f);
            batch.setColor(color);
        }
    }
}