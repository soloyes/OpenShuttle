package com.shuttle.game.background;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.screens.ScreenManager;

import lombok.Getter;

/**
 * @author Shuttle on 12/8/18.
 */
public class Star {
    @Getter
    private Vector2 position;
    private Shuttle player;
    private Vector2 normal;
    private CloudSky cloudSky;
    private float speed;
    @Getter
    private float scaleX, scaleY;
    @Getter
    private float width, height;
    @Getter
    private Color color;
    private ScreenManager.ScreenType screenType;

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
            default:
                color = Color.WHITE;
        }
    }

    public void update(float dt) {
        if (screenType == ScreenManager.ScreenType.GAME){
            position.x -= player.getVelocity().x / (1000/ scaleX);
            position.y -= player.getVelocity().y / (1000/ scaleX);
            if (normal.set(player.getVelocity()).len() < 150){
                normal.set(player.getVelocity()).nor();
                position.x -= 0.1 * normal.x * scaleX;
                position.y -= 0.1 * normal.y * scaleX;
            }
        }
        else {
            if(cloudSky.getBCdx() > cloudSky.getWidthBC() / 8)
            {
                position.x += speed * dt + scaleX * dt* dt / 2;
            }
            else {
                position.x += speed * dt - scaleX * dt* dt / 2;
            }

            if(cloudSky.getOscillatorBC().prettyOscillate(cloudSky.getBCdx(), cloudSky.getFrequency1(), 1f, 0 , cloudSky.getWidthBC() / 4, dt) > cloudSky.getWidthBC() / 8){
                speed += scaleX * dt;
            }
            else {
                speed -= scaleX * dt;
            }
        }

        if (position.x < -width) {
            position.x = Rules.SPACE_WIDTH;
        }
        if (position.x > Rules.SPACE_WIDTH) {
            position.x = -width;
        }
        if (position.y < height) {
            position.y = Rules.SPACE_HEIGHT;
        }
        if (position.y > Rules.SPACE_HEIGHT) {
            position.y = height;
        }

    }

    public void setPlayer(Shuttle player) {
        this.player = player;
    }

    public void setCloudSky(CloudSky cloudSky) {
        this.cloudSky = cloudSky;
    }

    public void setScreenType(ScreenManager.ScreenType screenType) {
        this.screenType = screenType;
    }
}