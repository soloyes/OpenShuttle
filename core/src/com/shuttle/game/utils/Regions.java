package com.shuttle.game.utils;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author Shuttle on 29/06/18.
 */

public class Regions {
    public static TextureRegion[] split(TextureRegion region, int rows, int cols, int frames) {
        if (region == null) throw new RuntimeException("Split null region");
        TextureRegion[] regions = new TextureRegion[frames];
        int tileWidth = region.getRegionWidth() / cols;
        int tileHeight = region.getRegionHeight() / rows;

        int frame = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                regions[frame] = new TextureRegion(region, tileWidth * j, tileHeight * i, tileWidth, tileHeight);
                if (frame == frames - 1) return regions;
                frame++;
            }
        }
        return regions;
    }
}