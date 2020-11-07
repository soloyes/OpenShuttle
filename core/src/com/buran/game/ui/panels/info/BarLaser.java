package com.buran.game.ui.panels.info;

/**
 * @author soloyes on 3/14/19.
 */

public class BarLaser extends AbstractBar {
    public BarLaser() {
        this.scaleBar = Assets.getInstance().getAtlas().findRegion(PaintConstants.LP_BAR_SCALE);
        this.bar = Assets.getInstance().getAtlas().findRegion(PaintConstants.LP_BAR);
        this.icon = Assets.getInstance().getAtlas().findRegion(PaintConstants.LP_BAR_ICON);
        this.width = Rules.WORLD_WIDTH / 4;
        this.height = PaintConstants.BAR_HEIGHT;
        this.side = Side.RIGHT;
        this.position.set(Rules.WORLD_WIDTH - width - 43 - PaintConstants.PLAY_PAUSE_BUTTON_WIDTH - 14, Rules.WORLD_HEIGHT - 90);
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