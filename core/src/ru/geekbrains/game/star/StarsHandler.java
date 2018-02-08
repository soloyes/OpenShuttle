package ru.geekbrains.game.star;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.game.players.Player;
import ru.geekbrains.stargame.engine.math.Rect;
import ru.geekbrains.stargame.engine.math.Rnd;

/**
 * Created by sol on 2/6/18.
 */

public class StarsHandler {
    private final int STARS = 50;
    private Star[] stars = new Star[STARS];
    private TextureRegion starTexture;

    public StarsHandler(TextureAtlas atlas) {
        starTexture = atlas.findRegion("star");
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(
                    starTexture,
                    Rnd.nextFloat(-0.05f, 0.05f),
                    Rnd.nextFloat(-0.3f, -0.1f),
                    0.009f);
        }
    }

    public void setV() {
        for (int i = 0; i < stars.length; i++) {
            stars[i].setV(stars[i].getV().rotate(10));
        }
    }

    public void draw(SpriteBatch batch){
        for (int i = 0; i < stars.length; i++) {
            stars[i].draw(batch);
        }
    }

    public void update(float delta){
        for (int i = 0; i < stars.length; i++) {
            stars[i].update(delta);
        }
    }

    public void resize(Rect worldBounds) {
        for (int i = 0; i < stars.length; i++) {
            stars[i].resize(worldBounds);
        }
    }

    public void dispose(){
        starTexture.getTexture().dispose();
    }
}
