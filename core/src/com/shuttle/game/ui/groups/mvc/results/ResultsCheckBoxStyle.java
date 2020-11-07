package com.shuttle.game.ui.groups.mvc.results;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.utils.Assets;

/**
 * @author soloyes on 02/04/20.
 */

public class ResultsCheckBoxStyle extends CheckBox.CheckBoxStyle {

    public ResultsCheckBoxStyle(Drawable checkboxOff, Drawable checkboxOn) {
        super(
                checkboxOff,
                checkboxOn,
                Assets.getInstance().getAssetManager().get(PaintConstants.FONT54, BitmapFont.class),
                new Color(0.8f, 0.43f, 0.33f, 1f)
        );

        checkboxOn.setMinWidth(PaintConstants.UI_CELL_WIDTH);
        checkboxOn.setMinHeight(PaintConstants.UI_CELL_HEIGHT);
        checkboxOff.setMinWidth(PaintConstants.UI_CELL_WIDTH);
        checkboxOff.setMinHeight(PaintConstants.UI_CELL_HEIGHT);
    }
}