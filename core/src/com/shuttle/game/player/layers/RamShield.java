package com.shuttle.game.player.layers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.PlayerInteractionLevels;
import com.shuttle.game.player.Buran;
import com.shuttle.game.player.Standing;
import com.shuttle.game.utils.Assets;

import lombok.Getter;
import lombok.Setter;

/**
 * @author soloyes on 9/2/18.
 */

public class RamShield {
    private TextureRegion texture;
    private int width, height;
    private float alpha;
    private float fromAlpha;
    @Getter
    @Setter
    private float scaleX, scaleY;
    @Getter
    private Color tmpColor;

    public RamShield() {
        this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.SHUTTLE_RAM_SHIELD);
        this.width = PaintConstants.SHUTTLE_RAM_SHIELD_WIDTH;
        this.height = PaintConstants.SHUTTLE_RAM_SHIELD_HEIGHT;
        this.tmpColor = new Color();
        this.scaleX = this.scaleY = 1.0f;
    }

    public void blink(float alpha) {
        float ramPower = Standing.getInstance().getRamPower();
        if (ramPower <= PlayerInteractionLevels.TAN.getLevel()) {
            this.alpha = 0.0f;
        } else {
            this.alpha = alpha;
        }
    }

    public void render(SpriteBatch batch) {
        tmpColor.a = alpha;
        batch.setColor(tmpColor);
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
        fromAlpha += dt;
        if (fromAlpha >= 0.2f) {
            fromAlpha = 0.2f;
        }

        float ramPower = Standing.getInstance().getRamPower();
        if (ramPower < PlayerInteractionLevels.TAN.getLevel()) {
            fromAlpha = 0.0f;
        } else if (ramPower >= PlayerInteractionLevels.RED.getLevel()) {
            tmpColor.set(Color.RED);
        } else if (ramPower >= PlayerInteractionLevels.YELLOW.getLevel()) {
            tmpColor.set(Color.YELLOW);
        } else if (ramPower >= PlayerInteractionLevels.GREEN.getLevel()) {
            tmpColor.set(Color.GREEN);
        } else if (ramPower >= PlayerInteractionLevels.TAN.getLevel()) {
            tmpColor.set(Color.TAN);
        }

        alpha -= dt;
        if (alpha <= fromAlpha) {
            alpha = fromAlpha;
        }
    }
}