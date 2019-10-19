package com.shuttle.game.ui.groups.configs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.StickerResolver;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

/**
 * @author Shuttle on 10/12/19.
 */
public class ConfigImageButton extends ImageButton {
    private ImageButton imageButton;
    private TextureRegionDrawable textureRegionDrawable;
    private TextureRegion glow;
    private TextureRegion justNewRegion;
    private Map<StickerResolver.Operation, TextureRegion> types;
    private GlyphLayout glyphLayout;
    private Label bottomLabel;
    private Label topLabel;
    private BitmapFont font36;
    private StickerResolver.Operation operation = StickerResolver.Operation.DEFAULT;
    private boolean left;
    @Getter
    private boolean empty = true;
    private boolean justNew = true;

    public ConfigImageButton(Drawable imageUp, boolean left) {
        super(imageUp);
        this.font36 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT36, BitmapFont.class);
        Label.LabelStyle labelStyle36 = new Label.LabelStyle();
        labelStyle36.font = font36;
        this.imageButton = new ImageButton(imageUp);
        this.textureRegionDrawable = new TextureRegionDrawable();
        this.left = left;

        this.glow = Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_UI_GLOW_CELL);
        if (left) {
            this.justNewRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_UI_ITEM_NEW_CIRCLE);
        } else {
            this.justNewRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_UI_ITEM_NEW_STAR);
        }
        this.types = new HashMap<StickerResolver.Operation, TextureRegion>() {{
            put(
                    StickerResolver.Operation.ATTACK,
                    Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_UI_ITEM_TYPE_ATTACK)
            );
            put(
                    StickerResolver.Operation.SELF,
                    Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_UI_ITEM_TYPE_SELF)
            );
            put(
                    StickerResolver.Operation.PROTECTION,
                    Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_UI_ITEM_TYPE_PROTECTION)
            );
        }};

        this.glyphLayout = new GlyphLayout();
        this.bottomLabel = new Label("", labelStyle36);
        this.topLabel = new Label("", labelStyle36);

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                justNew = false;
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
        if (!empty && left) {
            imageButton.getImage().getDrawable().draw(batch, getX() + 18, getY() + 36, PaintConstants.LEFT_RIGHT_UI_EMPTY_BOX_WIDTH, PaintConstants.LEFT_RIGHT_UI_EMPTY_BOX_HEIGHT);
        } else {
            imageButton.getImage().getDrawable().draw(batch, getX(), getY(), PaintConstants.LEFT_RIGHT_UI_CELL_WIDTH, PaintConstants.LEFT_RIGHT_UI_CELL_HEIGHT);
        }

        if (!empty) {
            batch.setColor(color.r, color.g, color.b, a * 0.8f);
            float justNewX, justNewY;
            if (left) {
                justNewX = getX() + 84;
                justNewY = getY() + 36;
            } else {
                justNewX = getX() + 90;
                justNewY = getY() + 12;
            }
            if (justNew) {
                batch.draw(justNewRegion, justNewX, justNewY, PaintConstants.LEFT_RIGHT_UI_ITEM_NEW_WIDTH, PaintConstants.LEFT_RIGHT_UI_ITEM_NEW_HEIGHT);
            }
            float typeX, typeY;
            if (left) {
                typeX = getX() + 18;
                typeY = getY() + 36;
            } else {
                typeX = getX() + 12;
                typeY = getY() + 12;
            }
            batch.draw(types.get(operation), typeX, typeY, PaintConstants.LEFT_RIGHT_UI_ITEM_TYPE_WIDTH, PaintConstants.LEFT_RIGHT_UI_ITEM_TYPE_HEIGHT);

        }
        batch.setColor(color);

        if (!empty) {
            float bottomY;
            if (left) {
                bottomLabel.setText("10");
                bottomY = 36;
            } else {
                bottomLabel.setText("x1.25");
                topLabel.setText("200");
                bottomY = 24;
            }
            glyphLayout.setText(font36, bottomLabel.getText());

            font36.draw(batch,
                    glyphLayout,
                    getX() + PaintConstants.LEFT_RIGHT_UI_CELL_WIDTH / 2 - glyphLayout.width / 2,
                    getY() + bottomY
            );

            if (!left) {
                glyphLayout.setText(font36, topLabel.getText());
                font36.draw(batch,
                        glyphLayout,
                        getX() + PaintConstants.LEFT_RIGHT_UI_CELL_WIDTH / 2 - glyphLayout.width / 2,
                        getY() + PaintConstants.LEFT_RIGHT_UI_CELL_HEIGHT
                );
            }
        }
    }

    public void setupButton(TextureRegion textureRegion, StickerResolver.Operation operation) {
        textureRegionDrawable.setRegion(textureRegion);
        imageButton.getImage().setDrawable(textureRegionDrawable);
        this.operation = operation;
        empty = false;
    }
}
