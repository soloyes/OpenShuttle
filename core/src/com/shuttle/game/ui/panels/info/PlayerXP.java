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
    private TextureRegion glow, circle;
    private RadialSprite scaleCircle;
    private int width, height;
    private Standing standing;
    private float angle;
    private Vector2 position;
    private BitmapFont font60_C;
    private BitmapFont font44;
    private BitmapFont font72;
    private StringBuilder scoreBuilder;
    private StringBuilder levelBuilder;
    private GlyphLayout score;
    private GlyphLayout level;
    private GlyphLayout levelLabel;

    public PlayerXP() {
        this.width = PaintConstants.XP_WIDTH;
        this.height = PaintConstants.XP_HEIGHT;
        this.glow = Assets.getInstance().getAtlas().findRegion(PaintConstants.CIRCLE_GLOW);
        this.circle = Assets.getInstance().getAtlas().findRegion(PaintConstants.SELECTOR_OUT_CENTER_BACK);
        this.scaleCircle = new RadialSprite(Assets.getInstance().getAtlas().findRegion(PaintConstants.XP_SCALE));
        this.standing = Standing.getInstance();
        this.position = new Vector2();
        this.position.set(43, Rules.WORLD_HEIGHT - height - 43);
        this.font44 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT44, BitmapFont.class);
        this.font60_C = Assets.getInstance().getAssetManager().get(PaintConstants.FONT60_C, BitmapFont.class);
        this.font72 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT72, BitmapFont.class);
        this.level = new GlyphLayout();
        this.levelLabel = new GlyphLayout(font44, "level");
        this.scoreBuilder = new StringBuilder();
        this.levelBuilder = new StringBuilder();
        this.score = new GlyphLayout();
    }

    public void render(SpriteBatch batch) {
        batch.draw(glow,
                position.x, position.y,
                width / 2, height / 2,
                width, height,
                1.0f, 1.0f,
                0.0f
        );
        font72.draw(
                batch,
                level, position.x + width / 2 - level.width / 2,
                position.y + height / 2 + level.height / 2 + 14
        );
        font44.draw(
                batch,
                levelLabel, position.x + width / 2 - levelLabel.width / 2,
                position.y + height / 2 - levelLabel.height / 2
        );
        batch.draw(circle,
                position.x, position.y,
                width / 2, height / 2,
                width, height,
                1.0f, 1.0f,
                0.0f
        );
        scaleCircle.draw(batch,
                position.x, position.y,
                width, height,
                -angle
        );
        font60_C.draw(
                batch,
                score, position.x + width / 2 - score.width / 2,
                1695
        );
    }

    public void update(float dt) {
        if (standing.getScoreView() <= 0) {
            angle = 1;
        } else {
            angle = 360 * (1 - 1.0f * (standing.getScoreToLevel() - standing.getScoreView()) / standing.getScoreToLevel());
        }

        if (standing.isScoreAlertRadiationAnomaly()) {
            scaleCircle.setColor(Color.RED);
            font60_C.setColor(Color.RED);
        } else {
            scaleCircle.setColor(Color.WHITE);
            font60_C.setColor(Color.WHITE);
        }
        scoreBuilder.setLength(0);
        scoreBuilder = Rounder.round(Standing.getInstance().getScoreView());
        score.setText(font60_C, scoreBuilder);
        levelBuilder.setLength(0);
        levelBuilder.append(standing.getPlayingLevel());
        level.setText(font72, levelBuilder);
    }
}