package com.shuttle.game.player.layers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.utils.Assets;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 9/2/18.
 */
public class LifeShield {
    private TextureRegion texture;
    private Shuttle player;
    private int width, height;
    private float alpha;
    @Getter
    @Setter
    private float scaleX, scaleY;

    public LifeShield(Shuttle player) {
        this.player = player;
        this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.SHUTTLE_LIFE_SHIELD);
        this.width = PaintConstants.SHUTTLE_LIFE_SHIELD_WIDTH;
        this.height = PaintConstants.SHUTTLE_LIFE_SHIELD_HEIGHT;
        this.scaleX = this.scaleY = 1.0f;
    }

    public void blink(float alpha) {
        if (player.getStanding().getLives() == 1) {
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
                player.getPosition().x - width / 2, player.getPosition().y - height / 2,
                width / 2, height / 2,
                width, height,
                scaleX, scaleY,
                player.getAngle()
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
