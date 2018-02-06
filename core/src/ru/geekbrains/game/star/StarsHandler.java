package ru.geekbrains.game.star;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.geekbrains.stargame.engine.math.Rect;
import ru.geekbrains.stargame.engine.math.Rnd;

/**
 * Created by sol on 2/6/18.
 */

public class StarsHandler {
    private static final int STARS = 50;
    private static Star[] stars = new Star[STARS];

    private static Texture starTexture = new Texture("star.png");

    public static Star[] getStars() {
        return stars;
    }

    static {
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(
                    new TextureRegion(starTexture), Rnd.nextFloat(-0.05f, 0.05f), Rnd.nextFloat(-0.5f, -0.1f), 0.01f);
        }
    }

    public static void draw(SpriteBatch batch){
        for (int i = 0; i < stars.length; i++) {
            stars[i].draw(batch);
        }
    }

    public static void update(float delta){
        for (int i = 0; i < stars.length; i++) {
            stars[i].update(delta);
        }
    }

    public static void resize(Rect worldBounds) {
        for (int i = 0; i < stars.length; i++) {
            stars[i].resize(worldBounds);
        }
    }

    public static void dispose(){
        starTexture.dispose();
    }
}
