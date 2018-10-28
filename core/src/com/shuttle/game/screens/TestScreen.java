package com.shuttle.game.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 6/28/18.
 */
public class TestScreen extends BaseScreen {
    private final float width;
    private final float height;
    private final Vector2 position;
    private final float scaleX, scaleY;
    private Vector2 center;
    private TextureRegion textureRegion;
    private float angle;
    private float r;

    public TestScreen(SpriteBatch batch) {
        super(batch);

        this.width = PaintConstants.SHUTTLE_WIDTH;
        this.height = PaintConstants.SHUTTLE_HEIGHT;
        this.r = 200;
        this.position = new Vector2(
                Rules.WORLD_WIDTH + r,
                Rules.WORLD_HEIGHT / 2
        );
        this.center = new Vector2(
                Rules.WORLD_WIDTH / 2,
                Rules.WORLD_HEIGHT / 2
        );
        this.scaleX = this.scaleY = 1.0f;
    }

    @Override
    public void show() {
        super.show();
        this.textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.SHUTTLE_REGION);
    }

    @Override
    public void createGUI() {
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        update(dt);
        batch.begin();
        batch.draw(textureRegion,
                position.x - width / 2, position.y - height / 2,
                width / 2, height / 2,
                width, height, scaleX, scaleY,
                0.0f
        );
        batch.end();
    }

    private void update(float dt) {
        angle += dt;
        r = r - 60*dt;
        if (r <=1){
            r = 200;
        }
        float R = (float)Math.log(r);
        position.x = center.x + 100*R * MathUtils.cos(angle);
        position.y = center.y + 100*R * MathUtils.sin(angle);
    }
}
