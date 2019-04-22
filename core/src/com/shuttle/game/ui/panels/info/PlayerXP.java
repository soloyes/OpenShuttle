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
import com.shuttle.game.utils.RadialSprite;
import com.shuttle.game.utils.Rounder;

/**
 * @author Shuttle on 3/3/19.
 */
public class PlayerXP {
    private TextureRegion outCircle, inCircle;
    private RadialSprite scaleCircle;
    private int width, height;
    private Standing standing;
    private float alpha;
    private Color color;
    private Vector2 position;
    private BitmapFont font60_C;
    private StringBuilder stringBuilder;
    private GlyphLayout glyphLayout;

    public PlayerXP() {
        this.width = PaintConstants.XP_WIDTH;
        this.height = PaintConstants.XP_HEIGHT;
        this.outCircle = Assets.getInstance().getAtlas().findRegion(PaintConstants.XP_OUT_CIRCLE);
        this.inCircle = Assets.getInstance().getAtlas().findRegion(PaintConstants.XP_IN_CIRCLE);
        this.scaleCircle = new RadialSprite(Assets.getInstance().getAtlas().findRegion(PaintConstants.XP_SCALE));
        this.standing = Standing.getInstance();
        this.color = new Color();
        this.position = new Vector2();
        this.position.set(44, Rules.WORLD_HEIGHT - height - 44);
        this.font60_C = Assets.getInstance().getAssetManager().get(PaintConstants.FONT60_C, BitmapFont.class);
        this.stringBuilder = new StringBuilder();
        this.glyphLayout = new GlyphLayout();
    }

    public void render(SpriteBatch batch) {
        Color tmpColor = batch.getColor();
        batch.setColor(color);
        batch.draw(outCircle,
                position.x, position.y,
                width / 2, height / 2,
                width, height,
                1.0f, 1.0f,
                0.0f
        );
        batch.setColor(tmpColor);
        scaleCircle.draw(batch,
                position.x, position.y,
                width, height,
                -alpha
        );
        batch.draw(inCircle,
                position.x, position.y,
                width / 2, height / 2,
                width, height,
                1.0f, 1.0f,
                0.0f
        );
        font60_C.draw(
                batch,
                glyphLayout, position.x + width / 2 - glyphLayout.width / 2,
                position.y + 2 * height / 3 - glyphLayout.height / 2
        );
        batch.setColor(tmpColor);
    }

    public void update(float dt) {
        if (standing.getScoreView() <= 0) {
            alpha = 1;
        } else {
            alpha = 360 * (1 - 1.0f * (standing.getScoreToLive() - standing.getScoreView()) / standing.getScoreToLive());
        }

        if (standing.isScoreAlertRadiationAnomaly()) {
            color.set(1.0f, 0.2f, 0.0f, 1f);
        } else {
            color.set(0.08f, 0.27f, 0.27f, 1.0f);
        }
        stringBuilder.setLength(0);
        stringBuilder = Rounder.round(Standing.getInstance().getScoreView());
        glyphLayout.setText(font60_C, stringBuilder);
    }
}
