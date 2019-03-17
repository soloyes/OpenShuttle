package com.shuttle.game.player.layers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.player.Standing;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.Regions;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 9/3/18.
 */
public class Scratch {
    private TextureRegion[] textures;
    private Shuttle player;
    private int width, height;
    @Getter
    @Setter
    private float scaleX, scaleY;
    private float alpha;
    private int frame;
    private Color tmpColor;

    public Scratch(Shuttle player) {
        this.player = player;
        this.textures = Regions.split(
                Assets.getInstance().getAtlas().findRegion(PaintConstants.SHUTTLE_SCRATCH),
                1,
                5,
                5
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
                Shuttle.position.x - width / 2, Shuttle.position.y - height / 2,
                width / 2, height / 2,
                width, height,
                scaleX, scaleY,
                player.getAngle()
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