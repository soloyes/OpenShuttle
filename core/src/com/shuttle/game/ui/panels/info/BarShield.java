package com.shuttle.game.ui.panels.info;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 3/14/19.
 */
public class BarShield extends AbstractBar {

    public BarShield(GameScreen gs) {
        super(gs);
        this.outBar = Assets.getInstance().getAtlas().findRegion(PaintConstants.SP_BAR_OUT);
        this.inBar = Assets.getInstance().getAtlas().findRegion(PaintConstants.SP_BAR_IN);
        this.icon = Assets.getInstance().getAtlas().findRegion(PaintConstants.SP_BAR_ICON);
        this.scale = Assets.getInstance().getAtlas().findRegion(PaintConstants.SP_BAR_SCALE);
        this.width = PaintConstants.SP_BAR_WIDTH;
        this.height = PaintConstants.SP_BAR_HEIGHT;
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch batch) {

    }
}
