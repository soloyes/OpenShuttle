package com.shuttle.game.background;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;

import lombok.Getter;

/**
 * @author Shuttle on 12/8/18.
 */
public class Star {
    @Getter
    private Vector2 position;
    private float speed;
    @Getter
    private float scaleX, scaleY;
    @Getter
    private float width, height;
    @Getter
    private Color color;

    public Star() {
        this.position = new Vector2(MathUtils.random(0, Rules.SPACE_WIDTH), MathUtils.random(0, Rules.SPACE_HEIGHT));
        this.speed = MathUtils.random(5.0f, 60.0f);
        this.scaleX = this.scaleY = MathUtils.random(0.5f, 1.5f);
        this.speed = MathUtils.random(5.0f, 60.0f);
        this.width = PaintConstants.STAR16_WIDTH;
        this.height = PaintConstants.STAR16_HEIGHT;
        int r = MathUtils.random(0, 20);
        switch (r) {
            case 0:
                color = Color.RED;
                break;
            case 1:
                color = Color.YELLOW;
                break;
            case 2:
                color = Color.GREEN;
                break;
            case 3:
                color = Color.PURPLE;
                break;
            case 4:
                color = Color.CYAN;
                break;
            case 5:
                color = Color.CORAL;
            default:
                color = Color.WHITE;
        }
    }

    public void update(float dt) {
        position.x -= speed * dt;
        if (position.x < -width) {
            position.x = Rules.SPACE_WIDTH;
        }
    }
}