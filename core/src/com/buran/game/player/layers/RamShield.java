package com.buran.game.player.layers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.buran.game.conf.PaintConstants;
import com.buran.game.conf.PlayerInteractionLevels;
import com.buran.game.player.Buran;
import com.buran.game.player.Standing;
import com.buran.game.utils.Assets;

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
        this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.BURAN_RAM_SHIELD);
        this.width = PaintConstants.BURAN_RAM_SHIELD_WIDTH;
        this.height = PaintConstants.BURAN_RAM_SHIELD_HEIGHT;
        this.tmpColor = new Color();
        this.scaleX = this.scaleY = 1.0f;
    }

    private float time;

    public void blink(float alpha) {
        if (!Buran.ramOn) {
            return;
        }
        this.alpha = Math.max(this.alpha, alpha);
    }

    /**
     * On: glows in the level's colour and breathes. Switching on: a bright flash that settles
     * onto the hull. Switching off: the field swells and dissolves.
     */
    public void render(SpriteBatch batch) {
        float age = Buran.ramSwitchAge;
        float scale = 1f;
        float a;
        if (Buran.ramOn) {
            float breathe = 0.36f + 0.12f * (float) Math.sin(time * 4f);
            a = Math.max(breathe, alpha);
            scale = 1f + 0.03f * (float) Math.sin(time * 2.2f);
            if (age < 0.4f) {
                float k = age / 0.4f;
                a = Math.max(a, 1f - k * 0.6f);
                scale = 1.3f - 0.3f * k;
            }
        } else if (age < 0.45f) {
            float k = age / 0.45f;
            a = 0.6f * (1f - k);
            scale = 1f + 0.35f * k;
        } else {
            return;
        }
        if (Buran.ramDenied && age < 0.5f) {
            tmpColor.set(0.6f, 0.6f, 0.65f, 1f);
        }
        tmpColor.a = a;
        batch.setColor(tmpColor);
        batch.draw(
                texture,
                Buran.position.x - width / 2, Buran.position.y - height / 2,
                width / 2, height / 2,
                width, height,
                scaleX * scale, scaleY * scale,
                Buran.angle
        );
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void update(float dt) {
        time += dt;
        float ramPower = Standing.getInstance().getRamPower();
        if (ramPower >= PlayerInteractionLevels.RED.getLevel()) {
            tmpColor.set(Color.RED);
        } else if (ramPower >= PlayerInteractionLevels.YELLOW.getLevel()) {
            tmpColor.set(Color.YELLOW);
        } else if (ramPower >= PlayerInteractionLevels.GREEN.getLevel()) {
            tmpColor.set(Color.GREEN);
        } else {
            tmpColor.set(Color.TAN);
        }
        alpha = Math.max(0f, alpha - dt * 1.5f);
    }
}