package com.shuttle.game.ui.groups.configs;


import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 29/03/20.
 */

public class SoundConfigCheckBoxStyle extends ConfigCheckBoxStyle {
    private Drawable[] drawables;

    public SoundConfigCheckBoxStyle(Drawable checkboxOff, Drawable checkboxOn) {
        super(checkboxOff, checkboxOn);
        this.drawables = new Drawable[4];
        drawables[0] = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_SOUND1));
        drawables[1] = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_SOUND2));
        drawables[2] = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_SOUND3));
        drawables[3] = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_SOUND4));
    }

    public void setFront(float i) {
        if (i >= 0.8f) {
            checkboxOn = drawables[0];
            checkboxOff = drawables[0];
        } else if (i >= 0.6f) {
            checkboxOn = drawables[1];
            checkboxOff = drawables[1];
        } else if (i >= 0.4f) {
            checkboxOn = drawables[2];
            checkboxOff = drawables[2];
        } else {
            checkboxOn = drawables[3];
            checkboxOff = drawables[3];
        }
    }
}