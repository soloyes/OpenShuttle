package com.buran.game.ui.groups.mvc.profiles;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.buran.game.conf.PaintConstants;
import com.buran.game.conf.Rules;
import com.buran.game.utils.Assets;
import com.buran.game.utils.RadialSprite;

import lombok.Setter;

/**
 * @author soloyes on 02/04/20.
 */

public class ProfilesCheckBox extends CheckBox {
    private RadialSprite load;
    @Setter
    private float pressedWait;
    @Setter
    private float deleteWait;
    @Setter
    private boolean confirm;

    public ProfilesCheckBox(String text, CheckBoxStyle style) {
        super(text, style);
        this.load = new RadialSprite(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_LOAD));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (pressedWait > 0.0f) {
            load.draw(
                    (SpriteBatch) batch,
                    getX(),
                    getY(),
                    -360 * (1 - 1.0f * (Rules.LINE_PRESSED_WAIT - pressedWait) / Rules.LINE_PRESSED_WAIT)
            );
        }

        if (confirm) {
            load.draw(
                    (SpriteBatch) batch,
                    getX(),
                    getY(),
                    -360 * (1 - 1.0f * (Rules.LINE_DELETE_WAIT - deleteWait) / Rules.LINE_DELETE_WAIT)
            );
        }
    }
}