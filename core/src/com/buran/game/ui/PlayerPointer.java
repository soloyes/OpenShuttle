package com.buran.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.buran.game.conf.PaintConstants;
import com.buran.game.conf.Rules;
import com.buran.game.player.Buran;
import com.buran.game.utils.Assets;

/**
 * @author soloyes on 8/28/18.
 */

public class PlayerPointer {
    private TextureRegion arrow;
    private TextureRegion mark;
    private Buran player;
    private Vector2 tmp1V2;
    private float width;
    private float height;
    private float blinkTimer;
    private float angle;
    private Vector2 tmp2V2;
    private Vector2 tmp3V3;
    private Vector2 tmp4V2;
    private float d;
    private Color color;

    public PlayerPointer(Buran player) {
        this.arrow = Assets.getInstance().getAtlas().findRegion(PaintConstants.POINTER_ARROW);
        this.mark = Assets.getInstance().getAtlas().findRegion(PaintConstants.POINTER_MARK);
        this.width = PaintConstants.POINTER_WIDTH;
        this.height = PaintConstants.POINTER_HEIGHT;
        this.player = player;
        this.tmp1V2 = new Vector2();
        this.tmp2V2 = new Vector2();
        this.tmp3V3 = new Vector2();
        this.tmp4V2 = new Vector2();
        this.d = PaintConstants.POINTER_D;
        this.color = Color.valueOf("ff0000b0");
    }

    public void render(SpriteBatch batch) {
        Color tmpColor = batch.getColor();
        boolean lost;
        if (Buran.position.x - player.getWidth() / 2 > Rules.SPACE_WIDTH) {
            tmp1V2.set(Rules.SPACE_WIDTH - width, Buran.position.y);
            lost = true;
        } else if (Buran.position.x + player.getWidth() / 2 < 0) {
            tmp1V2.set(0, Buran.position.y);
            lost = true;
        } else if (Buran.position.y - player.getHeight() / 2 > Rules.SPACE_HEIGHT) {
            tmp1V2.set(Buran.position.x, Rules.SPACE_HEIGHT - height);
            lost = true;
        } else if (Buran.position.y + player.getHeight() / 2 < 0) {
            tmp1V2.set(Buran.position.x, 0);
            lost = true;
        } else {
            lost = false;
        }

        if (tmp1V2.x <= 0 && tmp1V2.y <= 0) {
            tmp1V2.set(0, 0);
        }
        if (tmp1V2.x >= Rules.SPACE_WIDTH - width && tmp1V2.y <= 0) {
            tmp1V2.set(Rules.SPACE_WIDTH - width, 0);
        }
        if (tmp1V2.x >= Rules.SPACE_WIDTH - width && tmp1V2.y >= Rules.SPACE_HEIGHT - height) {
            tmp1V2.set(Rules.SPACE_WIDTH - width, Rules.SPACE_HEIGHT - height);
        }
        if (tmp1V2.x <= 0 && tmp1V2.y >= Rules.SPACE_HEIGHT - height) {
            tmp1V2.set(0, Rules.SPACE_HEIGHT - height);
        }

        if (lost) {
            if (blinkTimer <= PaintConstants.POINTER_BLINK_FREQUENCY) {
                batch.setColor(color);
                batch.draw(arrow, tmp1V2.x, tmp1V2.y, width / 2, height / 2, width, height, 1.0f, 1.0f, angle);
                batch.draw(mark, tmp4V2.x, tmp4V2.y, width / 2, height / 2, width, height, PaintConstants.POINTER_MARK_SCALE, PaintConstants.POINTER_MARK_SCALE, 0.0f);
                batch.setColor(tmpColor);
            } else {
                blinkTimer -= PaintConstants.POINTER_BLINK_FREQUENCY;
            }
        }
    }

    public void update(float dt) {
        blinkTimer += dt;
        tmp3V3.set(Buran.position.x, Buran.position.y);
        tmp2V2.set(tmp3V3.sub(Rules.SPACE_WIDTH / 2, Rules.SPACE_HEIGHT / 2));
        angle = tmp2V2.angle();
        tmp4V2.set(tmp1V2.x, tmp1V2.y).sub(d * MathUtils.cos(MathUtils.degreesToRadians * angle), d * MathUtils.sin(MathUtils.degreesToRadians * angle));
    }
}