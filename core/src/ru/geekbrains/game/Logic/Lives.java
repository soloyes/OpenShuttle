package ru.geekbrains.game.Logic;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.stargame.engine.Sprite;
import ru.geekbrains.stargame.engine.Rect;

/**
 * Created by sol on 2/20/18.
 */

public class Lives extends Sprite {
    private final int INIT_LIVES = 3;
    private int lives;
    private Rect worldBounds;

    public Lives(TextureAtlas atlas) {
        super(atlas.findRegion("rocket"), 1, 12, 12);
        setHeightProportion(0.07f);
        lives = INIT_LIVES;
    }

    public int decreaseAndGet() {
        return --lives;
    }

    @Override
    public void draw(SpriteBatch batch) {
        for (int i = 0; i < lives; i++) {
            this.pos.x = worldBounds.getLeft() + halfWidth * (i + 1);
            this.pos.y = worldBounds.getTop() - halfHeight - 0.02f;
            frame = 11;
            super.draw(batch);
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        this.worldBounds = worldBounds;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void initLives(){
        this.lives = INIT_LIVES;
    }
}