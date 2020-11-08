package com.buran.game.player.layers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.buran.game.conf.PaintConstants;
import com.buran.game.player.Buran;
import com.buran.game.player.Standing;
import com.buran.game.utils.Assets;

import lombok.Getter;
import lombok.Setter;

/**
 * @author soloyes on 9/2/18.
 */

public class LifeShield {
    private TextureRegion texture;
    private int width, height;
    private float alpha;
    @Getter
    @Setter
    private float scaleX, scaleY;

    public LifeShield() {
        this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.BURAN_LIFE_SHIELD);
        this.width = PaintConstants.BURAN_LIFE_SHIELD_WIDTH;
        this.height = PaintConstants.BURAN_LIFE_SHIELD_HEIGHT;
        this.scaleX = this.scaleY = 1.0f;
    }

    public void blink(float alpha) {
        if (Standing.getInstance().getLifeShield() <= 0) {
            this.alpha = 0.0f;
        } else {
            this.alpha = alpha;
        }
    }

    public void render(SpriteBatch batch) {
        Color color = batch.getColor();
        color.a = alpha;
        batch.setColor(color);
        batch.draw(
                texture,
                Buran.position.x - width / 2, Buran.position.y - height / 2,
                width / 2, height / 2,
                width, height,
                scaleX, scaleY,
                Buran.angle
        );
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void update(float dt) {
        alpha -= dt;
        if (alpha <= 0.0f) {
            alpha = 0.0f;
        }
    }
}