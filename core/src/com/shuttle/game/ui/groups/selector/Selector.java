package com.shuttle.game.ui.groups.selector;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.labelsticker.Sticker;
import com.shuttle.game.utils.StickerToTexture;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 3/22/19.
 */
public class Selector {
    @Getter
    @Setter
    private int count;
    @Getter
    private TextureRegion textureRegion;
    @Getter
    private Sticker sticker;
    private StickerToTexture stickerToTexture;

    public Selector(Sticker sticker) {
        this.count = 1;
        this.textureRegion = new TextureRegion();
        this.sticker = Sticker.PATTERN;
        this.stickerToTexture = new StickerToTexture();
        init(sticker);
    }

    private void init(Sticker sticker) {
        this.sticker = sticker;
        this.textureRegion = stickerToTexture.getRegion(sticker);
    }

    public void changeCount(int amount) {
        count += amount;
    }
}
