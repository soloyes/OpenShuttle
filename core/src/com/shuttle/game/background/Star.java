package com.shuttle.game.background;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.screens.ScreenManager;

import lombok.Setter;

/**
 * @author Shuttle on 12/8/18.
 */

public class Star {
    private Vector2 position;
    private Vector2 normal;
    @Setter
    private CloudSky cloudSky;
    private float speed;
    private float scaleX, scaleY;
    private float width, height;
    private Color color;

    public Star(float startScale, float endScale) {
        this.position = new Vector2(MathUtils.random(0, Rules.SPACE_WIDTH), MathUtils.random(0, Rules.SPACE_HEIGHT));
        this.scaleX = this.scaleY = MathUtils.random(startScale, endScale);
        this.speed = 0;
        this.width = PaintConstants.STAR16_WIDTH;
        this.height = PaintConstants.STAR16_HEIGHT;
        this.normal = new Vector2();
        int r = MathUtils.random(0, 20);
        switch (r) {
            case 0:
                color = Color.CORAL;
                break;
            case 1:
                color = Color.LIGHT_GRAY;
                break;
            case 2:
                color = Color.PURPLE;
                break;
            case 3:
                color = Color.GOLDENROD;
                break;
            case 4:
                color = Color.FIREBRICK;
                break;
            case 5:
                color = Color.RED;
                break;
            default:
                color = Color.WHITE;
        }
    }

    public void update(float dt) {
        if (ScreenManager.getInstance().getCurrentScreen() == ScreenManager.ScreenType.GAME) {
            position.x -= Shuttle.velocity.x / (1000 / scaleX);
            position.y -= Shuttle.velocity.y / (1000 / scaleX);
            if (normal.set(Shuttle.velocity).len() < 150) {
                normal.set(Shuttle.velocity).nor();
                position.x -= 0.1 * normal.x * scaleX;
                position.y -= 0.1 * normal.y * scaleX;
            }
        } else {
            if (dt > 1) dt = 1;
            if (cloudSky.getBcDx() > PaintConstants.BACKGROUND_OSCILLATE_WIDTH) {
                position.x += speed * dt + scaleX * dt * dt / 2;
            } else {
                position.x += speed * dt - scaleX * dt * dt / 2;
            }

            float tmp = cloudSky.getBcOscillator().prettyOscillate(cloudSky.getBcDx(), cloudSky.getBcFrequency(), 1f, 0, cloudSky.getBcWidth() / 4, dt);
            if (tmp > PaintConstants.BACKGROUND_OSCILLATE_WIDTH) {
                speed += scaleX * dt;
            } else {
                speed -= scaleX * dt;
            }
        }

        if (position.x < -width) {
            position.x = Rules.SPACE_WIDTH + position.x%Rules.SPACE_WIDTH ;
        }
        if (position.x > Rules.SPACE_WIDTH) {
            position.x = -width + position.x%Rules.SPACE_WIDTH ;
        }
        if (position.y < -height) {
            position.y = Rules.SPACE_HEIGHT + position.y%Rules.SPACE_HEIGHT;
        }
        if (position.y > Rules.SPACE_HEIGHT) {
            position.y = -height + position.y%Rules.SPACE_HEIGHT;
        }
    }

    public void render(SpriteBatch batch, TextureRegion textureRegion) {
        Color tmpColor = batch.getColor();
        batch.setColor(color);
        batch.draw(
                textureRegion,
                position.x - width / 2, position.y - height / 2,
                width / 2, height / 2,
                width, height,
                scaleX, scaleY,
                0
        );
        if (MathUtils.random(0, 300) < 2) {
            batch.draw(
                    textureRegion,
                    position.x - width / 2, position.y - height / 2,
                    width / 2, height / 2,
                    width, height,
                    scaleX * PaintConstants.STAR16_BLINK_FACTOR, scaleY * PaintConstants.STAR16_BLINK_FACTOR,
                    0
            );
        }
        batch.setColor(tmpColor);
    }
}