package com.shuttle.game.ui.panels.info;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.player.Standing;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 3/14/19.
 */

public abstract class AbstractBar {
    protected TextureRegion scaleBar, bar, icon;
    protected int width, height;
    protected int iconWidth, iconHeight;
    protected Standing standing;
    protected Color color;
    protected Vector2 position;
    protected Side side;
    protected float percentage;
    protected boolean redAlert;
    protected GlyphLayout glyphLayout;
    protected BitmapFont font32;
    protected StringBuilder stringBuilder;

    public AbstractBar() {
        this.standing = Standing.getInstance();
        this.color = new Color();
        this.scaleBar = new TextureRegion();
        this.bar = new TextureRegion();
        this.icon = new TextureRegion();
        this.position = new Vector2();
        this.percentage = 1.0f;
        this.redAlert = false;
        this.iconWidth = PaintConstants.BAR_ICON_WIDTH;
        this.iconHeight = PaintConstants.BAR_ICON_HEIGHT;
        this.font32 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT32, BitmapFont.class);
        this.glyphLayout = new GlyphLayout();
        this.stringBuilder = new StringBuilder();
    }

    abstract void update(float dt);

    protected void render(SpriteBatch batch) {
        Color tmpColor = batch.getColor();
        batch.setColor(color);
        batch.draw(bar,
                position.x, position.y,
                width / 2, height / 2,
                width, height,
                1.0f, 1.0f,
                0.0f
        );
        batch.draw(
                scaleBar.getTexture(),
                position.x, position.y,
                width / 2, height / 2,
                width * percentage, height,
                1.0f, 1.0f,
                180 * side.factor,
                scaleBar.getRegionX(), scaleBar.getRegionY(),
                (int) (scaleBar.getRegionWidth() * percentage), scaleBar.getRegionHeight(),
                false, false);
        batch.setColor(tmpColor);

        batch.draw(icon,
                side.factor == 0 ? position.x : position.x + width - iconWidth,
                position.y,
                iconWidth / 2, iconHeight / 2,
                iconWidth, iconHeight,
                1.0f, 1.0f,
                0.0f
        );

        font32.draw(batch, glyphLayout,
                side.factor == 0 ? (position.x + 28) : (position.x + width - 28 - glyphLayout.width),
                position.y + height + 28
        );
        batch.setColor(tmpColor);
    }

    protected enum Side {
        LEFT(0), RIGHT(1);
        private int factor;

        Side(int factor) {
            this.factor = factor;
        }
    }
}