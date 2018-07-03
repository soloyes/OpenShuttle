package com.shuttle.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameInfoSystem {
    private FlyingText[] elems;

    public GameInfoSystem() {
        this.elems = new FlyingText[20];
        for (int i = 0; i < elems.length; i++) {
            elems[i] = new FlyingText();
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < elems.length; i++) {
            elems[i].render(batch);
        }
    }

    public void setup(float x, float y, String text, float scale) {
        for (int i = 0; i < elems.length; i++) {
            if (!elems[i].isActive()) {
                elems[i].setup(x, y, text, scale);
                break;
            }
        }
    }

    public void update(float dt) {
        for (int i = 0; i < elems.length; i++) {
            elems[i].update(dt);
        }
    }
}
