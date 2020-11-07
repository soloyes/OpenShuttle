package com.buran.game.player.layers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.buran.game.conf.PaintConstants;
import com.buran.game.player.Buran;
import com.buran.game.player.Standing;
import com.buran.game.utils.Assets;
import com.buran.game.utils.Regions;

import lombok.Getter;
import lombok.Setter;

/**
 * @author soloyes on 9/3/18.
 */

public class Scratch {
    private TextureRegion[] textures;
    private int width, height;
    @Getter
    @Setter
    private float scaleX, scaleY;
    private float alpha;
    private int frame;
    private Color tmpColor;

    public Scratch() {
        this.textures = Regions.split(
                Assets.getInstance().getAtlas().findRegion(PaintConstants.SHUTTLE_SCRATCH),
                1, 5, 5
        );
        this.width = PaintConstants.SHUTTLE_SCRATCH_WIDTH;
        this.height = PaintConstants.SHUTTLE_SCRATCH_HEIGHT;
        this.tmpColor = new Color();
        this.scaleX = this.scaleY = 1.0f;
    }

    public void render(SpriteBatch batch) {
        tmpColor.a = alpha;
        batch.setColor(tmpColor);
        batch.draw(
                textures[frame],
                Buran.position.x - width / 2, Buran.position.y - height / 2,
                width / 2, height / 2,
                width, height,
                scaleX, scaleY,
                Buran.angle
        );
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void update(float dt) {
        if (Standing.getInstance().getLives() != 1) {
            alpha -= dt;
            if (alpha <= 0) {
                alpha = 0.0f;
            }
        } else {
            alpha = 1.0f;
        }

        int hp = (100 * Standing.getInstance().getHp()) / Standing.getInstance().getMaxHp();

        if (hp >= 0 && hp < 20)
            frame = 4;
        else if (hp >= 20 && hp < 40)
            frame = 3;
        else if (hp >= 40 && hp < 60)
            frame = 2;
        else if (hp >= 60 && hp < 80)
            frame = 1;
        else if (hp >= 60 && hp < 100)
            frame = 0;
        else
            alpha = 0.0f;
    }
}