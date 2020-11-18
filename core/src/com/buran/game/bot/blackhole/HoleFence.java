package com.buran.game.bot.blackhole;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.buran.game.conf.Buffs;
import com.buran.game.player.navigation.Radar;

/**
 * @author soloyes on 15/11/20.
 */

public class HoleFence {
    private ShapeRenderer shapeRenderer;

    public HoleFence() {
        this.shapeRenderer = new ShapeRenderer();
        shapeRenderer.setColor(Color.DARK_GRAY);
    }

    public void render(Camera camera) {
        if (Radar.isActive) {
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.circle(Blackhole.position.x, Blackhole.position.y, Buffs.getBlackholeTakeRange());
            shapeRenderer.end();
        }
    }

    public void update(float dt) {
    }
}
