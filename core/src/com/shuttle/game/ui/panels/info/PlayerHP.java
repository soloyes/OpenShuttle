package com.shuttle.game.ui.panels.info;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.player.Standing;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 4/22/19.
 */

public class PlayerHP {
    private int width, height;
    private Vector2 position;
    private BitmapFont font64;
    private StringBuilder stringBuilder;

    public PlayerHP() {
        this.width = PaintConstants.XP_WIDTH;
        this.height = PaintConstants.XP_HEIGHT;
        this.position = new Vector2();
        this.position.set(44, Rules.WORLD_HEIGHT - height - 44);
        this.font64 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT64_C, BitmapFont.class);
        this.stringBuilder = new StringBuilder();
    }

    public void render(SpriteBatch batch) {
        Color tmpColor = batch.getColor();
        font64.draw(batch, stringBuilder, position.x, position.y);
        batch.setColor(tmpColor);
    }

    public void update(float dt) {
        stringBuilder.setLength(0);
        stringBuilder.append(Standing.getInstance().getLives());
    }
}