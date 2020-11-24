package com.buran.game.ui.groups;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.buran.game.conf.PaintConstants;
import com.buran.game.labelsticker.Sticker;
import com.buran.game.utils.Assets;
import com.buran.game.utils.StickerResolver;

/**
 * @author soloyes on 10/11/20.
 */

public class Card extends Group {
    private TextureRegion back;
    private Table table;
    private StickerResolver stickerResolver;
    private Label.LabelStyle labelStyle54;

    public Card(Sticker sticker) {
        this.stickerResolver = new StickerResolver();
        labelStyle54 = new Label.LabelStyle(
                Assets.getInstance().getAssetManager().get(PaintConstants.FONT54, BitmapFont.class),
                null
        );
        this.back = Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_RULES_BACKGROUND);
        this.table = new Table();
        Image image = new Image(stickerResolver.getRegion(sticker));
        table.row().height(32).width(PaintConstants.UI_CONFIG_CELL_WIDTH);
        table.add();
        table.row().height(PaintConstants.ASTRONAUT_HEIGHT);
        table.add(image).width(PaintConstants.ASTRONAUT_WIDTH);
        table.row().height(32);
        table.add();
        table.row();
        Label l = new Label(sticker.getDescription(), labelStyle54);
        l.getStyle().font.getData().markupEnabled = true;
        l.setWidth(PaintConstants.UI_CONFIG_CELL_WIDTH);
        l.setWrap(true);
        table.add(l).width(PaintConstants.UI_CONFIG_CELL_WIDTH - 32);
        this.setWidth(PaintConstants.UI_CONFIG_CELL_WIDTH);
        this.setHeight(PaintConstants.UI_SETTINGS_HEIGHT);
        table.align(Align.top);
        table.setFillParent(true);
        this.addActor(table);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(back, getX(), getY());
        super.draw(batch, parentAlpha);
    }
}
