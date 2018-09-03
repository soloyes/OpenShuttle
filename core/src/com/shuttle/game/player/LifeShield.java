package com.shuttle.game.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 9/2/18.
 */
public class LifeShield {
    private TextureRegion texture;
    private Shuttle player;
    private int width, height;
    private float alpha;

    public LifeShield(Shuttle player) {
        this.player = player;
        this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.SHUTTLE_LIFE_SHIELD);
        this.width = PaintConstants.SHUTTLE_LIFE_SHIELD_WIDTH;
        this.height = PaintConstants.SHUTTLE_LIFE_SHIELD_HEIGHT;
    }

    public void blink() {
        alpha = 1.0f;
    }

    public void render(SpriteBatch batch) {
        Color color = batch.getColor();
        color.a = alpha;
        batch.setColor(color);
        batch.draw(
                texture,
                player.getPosition().x - width / 2,
                player.getPosition().y - height / 2,
                width / 2,
                height / 2,
                width,
                height,
                1,
                1,
                player.getAngle()
        );
    }

    public void update(float dt) {
        if (player.getStanding().getLives() == 1) {
            alpha = 0.0f;
        } else {
            alpha -= dt;
            if (alpha <= 0.0f) {
                alpha = 0.0f;
            }
        }
    }
}
