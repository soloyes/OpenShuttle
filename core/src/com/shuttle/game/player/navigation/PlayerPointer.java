package com.shuttle.game.player.navigation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 8/28/18.
 */
public class PlayerPointer {
    private TextureRegion point;
    private Shuttle player;
    private Vector2 tmp;
    private float width;
    private float height;
    private float blinkTimer;

    public PlayerPointer(Shuttle player) {
        this.point = Assets.getInstance().getAtlas().findRegion(PaintConstants.STAR16_REGION);
        this.player = player;
        this.tmp = new Vector2();
        this.width = PaintConstants.POINTER_WIDTH;
        this.height = PaintConstants.POINTER_HEIGHT;
    }

    public void render(SpriteBatch batch) {
        boolean lost;
        if (player.getPosition().x - player.getWidth() / 2 > Rules.SPACE_WIDTH) {
            tmp.set(Rules.SPACE_WIDTH - width, player.getPosition().y);
            lost = true;
        } else if (player.getPosition().x + player.getWidth() / 2 < 0) {
            tmp.set(0, tmp.y = player.getPosition().y);
            lost = true;
        } else if (player.getPosition().y - player.getHeight() / 2 > Rules.SPACE_HEIGHT) {
            tmp.set(player.getPosition().x, Rules.SPACE_HEIGHT - height);
            lost = true;
        } else if (player.getPosition().y + player.getHeight() / 2 < 0) {
            tmp.set(player.getPosition().x, 0);
            lost = true;
        } else {
            lost = false;
        }

        if (tmp.x <= 0 && tmp.y <= 0) tmp.set(0, 0);
        if (tmp.x >= Rules.SPACE_WIDTH - width && tmp.y <= 0) tmp.set(Rules.SPACE_WIDTH - width, 0);
        if (tmp.x >= Rules.SPACE_WIDTH - width && tmp.y >= Rules.SPACE_HEIGHT - height)
            tmp.set(Rules.SPACE_WIDTH - width, Rules.SPACE_HEIGHT - height);
        if (tmp.x <= 0 && tmp.y >= Rules.SPACE_HEIGHT - height)
            tmp.set(0, Rules.SPACE_HEIGHT - height);

        if (lost) {
            if (blinkTimer <= 0.33f) {
                batch.draw(point, tmp.x, tmp.y, width, height, width, height, 1.0f, 1.0f, 0.0f);
            } else {
                blinkTimer -= 0.33f;
            }

        }
    }

    public void update(float dt) {
        blinkTimer += dt;
    }
}
