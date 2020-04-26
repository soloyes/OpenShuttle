package com.shuttle.game.ui.groups.selector;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.labelsticker.Sticker;
import com.shuttle.game.ui.groups.configs.ConfigImageButton;
import com.shuttle.game.ui.groups.configs.LeftGroup;
import com.shuttle.game.utils.Assets;
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
    private float rechargeTime;
    @Getter
    private boolean active;
    protected GlyphLayout glyphLayout;
    private BitmapFont font32;
    private StringBuilder stringBuilder;

    public Selector(Sticker sticker) {
        this.count = 0;
        this.font32 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT32, BitmapFont.class);
        this.glyphLayout = new GlyphLayout();
        this.stringBuilder = new StringBuilder();
        this.textureRegion = new TextureRegion();
        this.sticker = Sticker.PATTERN;
        this.stickerResolver = new StickerResolver();
        this.rechargeTime = Rules.PLAYER_RECHARGE_TIME;
        this.active = true;
        init(sticker);
    }

    private void init(Sticker sticker) {
        this.sticker = sticker;
        this.textureRegion = stickerResolver.getRegion(sticker);
    }

    public void changeCount(int amount) {
        count += amount;

        ConfigImageButton button = LeftGroup.getInstance().getButtonMap().get(sticker);
        if (button != null) {
            button.changeCount(amount);
        }
    }

    public void draw(SpriteBatch batch, float x, float y, float originX, float originY, float width, float height, float v, float v1, float v2) {
        batch.draw(textureRegion,
                x, y,
                originX, originY,
                width, height,
                v, v1, v2
        );
        font32.draw(batch, glyphLayout, x + width / 2 - glyphLayout.width / 2, y - 14);
    }

    public void update(float dt) {
        rechargeTime += dt;
        if (rechargeTime >= Rules.PLAYER_RECHARGE_TIME) {
            rechargeTime = Rules.PLAYER_RECHARGE_TIME;
            active = true;
        }
        stringBuilder.setLength(0);
        glyphLayout.setText(font32, stringBuilder.append(count));
    }

    public float getAngle() {
        return -360 * (1 - 1.0f * (Rules.PLAYER_RECHARGE_TIME - rechargeTime) / Rules.PLAYER_RECHARGE_TIME);
    }

    public void press() {
        if (active) {
            active = false;
            rechargeTime = 0.0f;
        }
    }
}