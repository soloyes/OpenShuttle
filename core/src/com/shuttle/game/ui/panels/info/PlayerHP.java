package com.shuttle.game.ui.panels.info;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.player.Standing;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 4/22/19.
 */

public class PlayerHP {
    private int iconWidth, iconHeight;
    private Vector2 position;
    private BitmapFont font44;
    private GlyphLayout glyphLayout;
    private StringBuilder stringBuilder;
    private TextureRegion icon;
    private TextureRegion iconScale;
    private Color color;
    private float blinkTimer;

    public PlayerHP() {
        this.icon = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.SHUTTLE_ICON));
        this.iconScale = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.SHUTTLE_ICON_SCALE));
        this.iconWidth = PaintConstants.SHUTTLE_ICON_WIDTH;
        this.iconHeight = PaintConstants.SHUTTLE_ICON_HEIGHT;
        this.position = new Vector2();
        this.position.set(Rules.WORLD_WIDTH / 2, 1775);
        this.font44 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT44, BitmapFont.class);
        this.glyphLayout = new GlyphLayout();
        this.stringBuilder = new StringBuilder();
        this.color = new Color();
    }

    public void render(SpriteBatch batch) {
        Color tmpColor = batch.getColor();
        if (blinkTimer <= PaintConstants.SHUTTLE_ICON_BLINK_FREQUENCY) {
            batch.setColor(color);
            batch.draw(icon, position.x - iconWidth / 2, position.y - 6, iconWidth, iconHeight);
            int h = (int) ((1.0f - 1.0f * Standing.getInstance().getLevelsToLive() / Rules.LEVELS_TO_LIVE) * iconHeight);
            batch.draw(
                    iconScale.getTexture(),
                    position.x - iconWidth / 2,
                    position.y - 6,
                    iconScale.getRegionX(), iconScale.getRegionY() + h,
                    iconWidth, iconHeight - h
            );
        } else {
            blinkTimer = -PaintConstants.SHUTTLE_ICON_BLINK_FREQUENCY;
        }
        batch.setColor(tmpColor);
        font44.draw(batch, glyphLayout, position.x - glyphLayout.width / 2, position.y - glyphLayout.height / 2);
    }

    public void update(float dt) {
        stringBuilder.setLength(0);
        stringBuilder.append(Standing.getInstance().getLives());
        glyphLayout.setText(font44, stringBuilder);
        if (Standing.getInstance().getLives() == 1) {
            color.set(1.0f, 0.2f, 0.0f, 1f);
        } else {
            color.set(Color.WHITE);
        }
        if (Standing.getInstance().getLives() == 1 && 1.0f * Standing.getInstance().getHp() / Standing.getInstance().getMaxHp() <= 0.5f) {
            blinkTimer += dt;
        } else {
            blinkTimer = 0.0f;
        }
    }
}