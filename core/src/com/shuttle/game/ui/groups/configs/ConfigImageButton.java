package com.shuttle.game.ui.groups.configs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.StickerResolver;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Shuttle on 10/12/19.
 */
public class ConfigImageButton extends ImageButton {
    private ImageButton imageButton;
    private TextureRegion glow;
    private TextureRegion justNewRegion;
    private Map<StickerResolver.Operation, TextureRegion> types;
    private boolean left;
    private boolean empty;
    private boolean justnew = true;

    public ConfigImageButton(Drawable imageUp, boolean left) {
        super(imageUp);
        this.imageButton = new ImageButton(imageUp);
        this.left = left;
        imageUp.setMinWidth(PaintConstants.LEFT_RIGHT_UI_CELL_WIDTH);
        imageUp.setMinHeight(PaintConstants.LEFT_RIGHT_UI_CELL_HEIGHT);

        this.glow = Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_UI_GLOW_CELL);
        if (left) {
            this.justNewRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_UI_ITEM_NEW_CIRCLE);
        } else {
            this.justNewRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_UI_ITEM_NEW_STAR);
        }
        this.types = new HashMap<StickerResolver.Operation, TextureRegion>() {{
            put(
                    StickerResolver.Operation.ATTACK,
                    Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_UI_ITEM_TYPE_TRIANGLE)
            );
            put(
                    StickerResolver.Operation.SELF,
                    Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_UI_ITEM_TYPE_CIRCLE)
            );
            put(
                    StickerResolver.Operation.PROTECTION,
                    Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_UI_ITEM_TYPE_SQUARE)
            );
        }};

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                justnew = false;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(glow, getX(), getY(),
                PaintConstants.LEFT_RIGHT_UI_CELL_WIDTH,
                PaintConstants.LEFT_RIGHT_UI_CELL_HEIGHT
        );
        float a = 0.5f;
        if (isPressed()) {
            a = 1.0f;
        }
        Color color = batch.getColor();
        batch.setColor(color.r, color.g, color.b, a);
        if (empty && left) {
            imageButton.getImage().getDrawable().draw(batch, getX() + 18, getY() + 36, PaintConstants.LEFT_RIGHT_UI_EMPTY_BOX_WIDTH, PaintConstants.LEFT_RIGHT_UI_EMPTY_BOX_HEIGHT);
        } else {
            imageButton.getImage().getDrawable().draw(batch, getX(), getY(), PaintConstants.LEFT_RIGHT_UI_CELL_WIDTH, PaintConstants.LEFT_RIGHT_UI_CELL_HEIGHT);
        }
        batch.setColor(color);

        float justNewX, justNewY;
        if (left) {
            justNewX = getX() + 84;
            justNewY = getY() + 36;
        } else {
            justNewX = getX() + 90;
            justNewY = getY() + 12;
        }
        if (justnew) {
            batch.draw(justNewRegion, justNewX, justNewY, PaintConstants.LEFT_RIGHT_UI_ITEM_NEW_WIDTH, PaintConstants.LEFT_RIGHT_UI_ITEM_NEW_HEIGHT);
        }
    }
}
