package com.shuttle.game.ui.groups.configs;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 10/12/19.
 */
public class ConfigImageButton extends ImageButton {
    private ImageButton imageButton;
    private TextureRegion click;
    private TextureRegion glow;

    public ConfigImageButton(Drawable imageUp) {
        super(imageUp);
        this.imageButton = new ImageButton(imageUp);
        imageUp.setMinWidth(PaintConstants.LEFT_RIGHT_UI_EMPTY_BOX_WIDTH);
        imageUp.setMinHeight(PaintConstants.LEFT_RIGHT_UI_EMPTY_BOX_HEIGHT);
        this.click = Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_UI_CLICK_CELL);
        this.glow = Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_UI_GLOW_CELL);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(glow, getX(), getY(),
                PaintConstants.LEFT_RIGHT_UI_CELL_WIDTH,
                PaintConstants.LEFT_RIGHT_UI_CELL_HEIGHT
        );
        super.draw(batch, parentAlpha);
        if (isPressed()) {
            batch.draw(click, getX(), getY(), PaintConstants.LEFT_RIGHT_UI_CELL_WIDTH, PaintConstants.LEFT_RIGHT_UI_CELL_HEIGHT);
        }
    }
}
