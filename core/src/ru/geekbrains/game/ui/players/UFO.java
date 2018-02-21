package ru.geekbrains.game.ui.players;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by sol on 2/15/18.
 */

public class UFO extends InsightRect {
    public UFO(TextureAtlas atlas) {
        super(atlas.findRegion("f"));
    }

    public UFO(TextureRegion region, int i, int j, int frames) {
        super(region, i, j, frames);
    }
}
