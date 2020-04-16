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
 * @author Shuttle on 11/18/18.
 */

public class TakeRangeField {
    private TextureRegion texture;
    private int width, height;
    @Getter
    @Setter
    private float alpha;
    @Getter
    @Setter
    private float scaleX, scaleY;
    private Color tmpColor;

    public TakeRangeField() {
        this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.SHUTTLE_TAKE_RANGE_FIELD_REGION);

        this.width = PaintConstants.SHUTTLE_TAKE_RANGE_FIELD_WIDTH;
        this.height = PaintConstants.SHUTTLE_TAKE_RANGE_FIELD_HEIGHT;
        this.tmpColor = new Color();
        this.scaleX = this.scaleY = 1.0f;
    }

    public void render(SpriteBatch batch) {
        tmpColor.set(1.0f, 1.0f, 1.0f, alpha);
        batch.setColor(tmpColor);
        batch.draw(
                texture,
                Shuttle.position.x - width / 2, Shuttle.position.y - height / 2,
                width / 2, height / 2,
                width, height,
                scaleX, scaleY,
                Shuttle.angle
        );
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void update(float dt) {
    }
}