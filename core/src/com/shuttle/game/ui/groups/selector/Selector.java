package com.shuttle.game.ui.groups.selector;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.labelsticker.Sticker;
import com.shuttle.game.ui.groups.configs.ConfigImageButton;
import com.shuttle.game.ui.groups.configs.LeftGroup;
import com.shuttle.game.utils.StickerResolver;

import lombok.Getter;

/**
 * @author Shuttle on 3/22/19.
 */

public class Selector {
    @Getter
    private int count;
    @Getter
    private TextureRegion textureRegion;
    @Getter
    private Sticker sticker;
    private StickerResolver stickerResolver;

    public Selector(Sticker sticker) {
        this.count = 0;
        this.textureRegion = new TextureRegion();
        this.sticker = Sticker.PATTERN;
        this.stickerResolver = new StickerResolver();
        init(sticker);
    }

    private void init(Sticker sticker) {
        this.sticker = sticker;
        this.textureRegion = stickerResolver.getRegion(sticker);
    }

    public void changeCount(int amount) {
        count += amount;
        if (amount < 0){
            return;
        }
        ConfigImageButton button = LeftGroup.getInstance().getButtonMap().get(sticker);
        if (button != null){
            button.changeCount(amount);
        }
    }
}