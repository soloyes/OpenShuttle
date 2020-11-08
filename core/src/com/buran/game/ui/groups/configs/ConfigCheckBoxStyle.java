package com.buran.game.ui.groups.configs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.buran.game.conf.PaintConstants;
import com.buran.game.utils.Assets;

/**
 * @author soloyes on 29/03/20.
 */

public class ConfigCheckBoxStyle extends CheckBox.CheckBoxStyle {

    public ConfigCheckBoxStyle(Drawable checkboxOff, Drawable checkboxOn) {
        super(
                checkboxOff,
                checkboxOn,
                Assets.getInstance().getAssetManager().get(PaintConstants.FONT54, BitmapFont.class),
                new Color(0.8f, 0.43f, 0.33f, 1f)
        );

        checkboxOn.setMinWidth(PaintConstants.UI_ICO_CONFIG_WIDTH);
        checkboxOn.setMinHeight(PaintConstants.UI_ICO_CONFIG_HEIGHT);
        checkboxOff.setMinWidth(PaintConstants.UI_ICO_CONFIG_WIDTH);
        checkboxOff.setMinHeight(PaintConstants.UI_ICO_CONFIG_HEIGHT);
    }
}