package com.shuttle.game.ui.groups.configs;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.utils.Assets;

import lombok.Setter;

/**
 * @author Shuttle on 29/03/20.
 */

public class ConfigCheckBox extends CheckBox {
    private TextureRegion block;
    private TextureRegion lock;
    @Setter
    private boolean locked;

    public ConfigCheckBox(String text, CheckBoxStyle style) {
        super(text, style);
        this.block = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_OFF));
        this.lock = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_ICO_CONFIG_LOCK));
        this.locked = false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (locked) {
            batch.draw(
                    lock,
                    getX() + (PaintConstants.UI_CONFIG_CELL_HEIGHT - PaintConstants.UI_ICO_CONFIG_WIDTH) / 2,
                    getY() + (PaintConstants.UI_CONFIG_CELL_HEIGHT - PaintConstants.UI_ICO_CONFIG_WIDTH) / 2
            );
        } else {
            super.draw(batch, parentAlpha);
            if (!isChecked()) {
                batch.draw(
                        block,
                        getX() + (PaintConstants.UI_CONFIG_CELL_HEIGHT - PaintConstants.UI_ICO_CONFIG_WIDTH) / 2,
                        getY() + (PaintConstants.UI_CONFIG_CELL_HEIGHT - PaintConstants.UI_ICO_CONFIG_WIDTH) / 2
                );
            }
        }
    }
}