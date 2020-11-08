package com.buran.game.ui.groups.configs;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.buran.game.conf.PaintConstants;
import com.buran.game.utils.Assets;

/**
 * @author soloyes on 29/03/20.
 */

public class LevelConfigCheckBoxStyle extends ConfigCheckBoxStyle {
    private Drawable[] drawables;

    public LevelConfigCheckBoxStyle(Drawable checkboxOff, Drawable checkboxOn) {
        super(checkboxOff, checkboxOn);
        this.drawables = new Drawable[4];
        drawables[0] = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_LEVEL1));
        drawables[1] = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_LEVEL2));
        drawables[2] = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_LEVEL3));
        drawables[3] = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_LEVEL4));
    }

    public void setFront(int i) {
        if (i >= drawables.length) {
            i = drawables.length - 1;
        }
        checkboxOn = drawables[i];
        checkboxOff = drawables[i];
    }
}