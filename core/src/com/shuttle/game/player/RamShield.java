package com.shuttle.game.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 9/2/18.
 */
public class RamShield {
    private TextureRegion texture;
    private Shuttle player;
    private int width, height;
    private float alpha;
    private float fromAlpha = 0.1f;
    private Color color;

    public RamShield(Shuttle player) {
        this.player = player;
        this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.SHUTTLE_RAM_SHIELD);
        this.width = PaintConstants.SHUTTLE_RAM_SHIELD_WIDTH;
        this.height = PaintConstants.SHUTTLE_RAM_SHIELD_HEIGHT;
        this.color = new Color();
    }

    public void blink() {
        alpha = 1.0f;
    }

    public void render(SpriteBatch batch) {
        Color tmpColor = batch.getColor();
        float a = tmpColor.a;
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
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void update(float dt) {
        fromAlpha += dt;
        if (fromAlpha >= 0.25)
            fromAlpha = 0.25f;
        switch (player.getShieldLevel()) {
            case NONE:
                fromAlpha = 0.0f;
                break;
            case WHITE:
                color.set(Color.WHITE);
                break;
            case GREEN:
                color.set(Color.GREEN);
                break;
            case YELLOW:
                color.set(Color.MAGENTA);
                break;
            case RED:
                color.set(Color.RED);
        }

        alpha -= dt;
        if (alpha <= fromAlpha) {
            alpha = fromAlpha;
        }
    }
}
