package com.shuttle.game.ui.groups.selector;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.bot.consumable.Label;
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
    private Label.Sticker sticker;
    private StickerToTexture stickerToTexture;

    public Selector(Label label) {
        this.count = 1;
        this.textureRegion = new TextureRegion();
        this.sticker = Label.Sticker.PATTERN;
        this.stickerToTexture = new StickerToTexture();
        init(label.getSticker());
    }

    private void init(Label.Sticker sticker) {
        this.sticker = sticker;
        this.textureRegion = stickerToTexture.getRegion(sticker);
    }

    public void addCount(int amount) {
        count += amount;
    }
}
