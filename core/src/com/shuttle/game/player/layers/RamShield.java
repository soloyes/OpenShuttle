package com.shuttle.game.player.layers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.conf.GlobalInteractionLevels;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.utils.Assets;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 9/2/18.
 */
public class RamShield {
    private TextureRegion texture;
    private Shuttle player;
    private int width, height;
    private float alpha;
    private float fromAlpha;
    @Getter
    @Setter
    private float scaleX, scaleY;
    @Getter
    private Color tmpColor;

    public RamShield(Shuttle player) {
        this.player = player;
        this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.SHUTTLE_RAM_SHIELD);
        this.width = PaintConstants.SHUTTLE_RAM_SHIELD_WIDTH;
        this.height = PaintConstants.SHUTTLE_RAM_SHIELD_HEIGHT;
        this.tmpColor = new Color();
        this.scaleX = this.scaleY = 1.0f;
    }

    public void blink(float alpha) {
        if (player.getShieldLevel() == GlobalInteractionLevels.NONE) {
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
                player.getPosition().x - width / 2, player.getPosition().y - height / 2,
                width / 2, height / 2,
                width, height,
                scaleX, scaleY,
                player.getAngle()
        );
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void update(float dt) {
        fromAlpha += dt;
        if (fromAlpha >= 0.2f)
            fromAlpha = 0.2f;
        switch (player.getShieldLevel()) {
            case NONE:
                fromAlpha = 0.0f;
                break;
            case TAN:
                tmpColor.set(Color.TAN);
                break;
            case GREEN:
                tmpColor.set(Color.GREEN);
                break;
            case YELLOW:
                tmpColor.set(Color.YELLOW);
                break;
            case RED:
                tmpColor.set(Color.RED);
        }

        alpha -= dt;
        if (alpha <= fromAlpha) {
            alpha = fromAlpha;
        }
    }
}
