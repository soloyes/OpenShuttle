package com.shuttle.game.ui.groups.configs;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.RadialSprite;

import lombok.Setter;

/**
 * @author Shuttle on 29/03/20.
 */

public class ConfigCheckBox extends CheckBox {
    private TextureRegion block;
    private TextureRegion lock;
    private RadialSprite load;
    @Setter
    private boolean locked;
    @Setter
    private float touchTime;

    public ConfigCheckBox(String text, CheckBoxStyle style) {
        super(text, style);
        this.block = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_OFF));
        this.lock = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_LOCK));
        this.load = new RadialSprite(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_LOAD));
        this.locked = false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (locked) {
            batch.draw(
                    lock,
                    getX() + (PaintConstants.UI_CELL_HEIGHT - PaintConstants.UI_ICO_CONFIG_WIDTH) / 2,
                    getY() + (PaintConstants.UI_CELL_HEIGHT - PaintConstants.UI_ICO_CONFIG_WIDTH) / 2
            );
        } else {
            super.draw(batch, parentAlpha);
            if (!isChecked()) {
                batch.draw(
                        block,
                        getX() + (PaintConstants.UI_CELL_HEIGHT - PaintConstants.UI_ICO_CONFIG_WIDTH) / 2,
                        getY() + (PaintConstants.UI_CELL_HEIGHT - PaintConstants.UI_ICO_CONFIG_WIDTH) / 2
                );
            }
        }

        if (touchTime > 0.0f) {
            load.draw(
                    (SpriteBatch) batch,
                    getX(),
                    getY(),
                    -360 * (1 - 1.0f * (Rules.SETTINGS_WAIT_DISABLE - touchTime) / Rules.SETTINGS_WAIT_DISABLE)
            );
        }
    }
}