package com.shuttle.game.ui.panels.info;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.player.Standing;
import com.shuttle.game.screens.GameScreen;

/**
 * @author Shuttle on 3/14/19.
 */
public abstract class AbstractBar {
    protected TextureRegion outBar, inBar, icon, scale;
    protected GameScreen gs;
    protected int width, height;
    protected Standing standing;
    protected Color color;
    protected Vector2 position;
    protected Side side;
    protected float percentage;

    public AbstractBar(GameScreen gs) {
        this.gs = gs;
        this.standing = gs.getPlayer().getStanding();
        this.color = Color.WHITE;
        this.outBar = new TextureRegion();
        this.inBar = new TextureRegion();
        this.icon = new TextureRegion();
        this.scale = new TextureRegion();
        this.position = new Vector2();
        this.percentage = 1.0f;
    }

    abstract void update(float dt);

    protected void render(SpriteBatch batch) {
        Color tmpColor = batch.getColor();
        batch.setColor(color);
        batch.draw(inBar,
                position.x, position.y,
                width / 2, height / 2,
                width, height,
                1.0f, 1.0f,
                0.0f
        );
        batch.draw(scale,
                position.x, position.y,
                width / 2, height / 2,
                width * percentage, height,
                1.0f, 1.0f,
                180 * side.factor
        );
        batch.draw(outBar,
                position.x, position.y,
                width / 2, height / 2,
                width, height,
                1.0f, 1.0f,
                0.0f
        );
        batch.draw(icon,
                position.x, position.y,
                width / 2, height / 2,
                width, height,
                1.0f, 1.0f,
                0.0f
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
