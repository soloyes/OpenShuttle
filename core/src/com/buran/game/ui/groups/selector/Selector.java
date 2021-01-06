package com.buran.game.ui.groups.selector;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.buran.game.conf.Buffs;
import com.buran.game.conf.PaintConstants;
import com.buran.game.conf.Rules;
import com.buran.game.labelsticker.Sticker;
import com.buran.game.ui.groups.configs.ConfigImageButton;
import com.buran.game.ui.groups.configs.LeftGroup;
import com.buran.game.ui.groups.switches.SwitchGroup;
import com.buran.game.utils.Assets;
import com.buran.game.utils.StickerResolver;

import lombok.Getter;

/**
 * @author soloyes on 3/22/19.
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
    private float rechargeTimeMoreThan;
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
        if (count >= Rules.SELECTOR_MAX_COUNT) {
            count = Rules.SELECTOR_MAX_COUNT;
        }

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
        if (SwitchGroup.getInstance().isLeft()) {
            font32.draw(batch, glyphLayout, x + width + glyphLayout.width / 2, y + height / 2 + glyphLayout.height / 2);
        } else {
            font32.draw(batch, glyphLayout, x - 5 * glyphLayout.width / 4, y + height / 2 + glyphLayout.height / 2);
        }
    }

    public void update(float dt) {
        rechargeTime += dt;
        if (rechargeTime >= rechargeTimeMoreThan) {
            rechargeTime = rechargeTimeMoreThan;
            active = true;
        }
        stringBuilder.setLength(0);
        glyphLayout.setText(font32, stringBuilder.append(count));
    }

    public float getAngle() {
        return -360 * (1 - 1.0f * (rechargeTimeMoreThan - rechargeTime) / rechargeTimeMoreThan);
    }

    public void press() {
        if (active) {
            active = false;
            rechargeTime = 0.0f;
            switch (sticker) {
                case SPLASH:
                case ROCKETS:
                    rechargeTimeMoreThan = Buffs.getRechargeTimeSplashRockets();
                    break;
                case REPAIR:
                case BOOST:
                    rechargeTimeMoreThan = Buffs.getRechargeTimeRepairBoost();
                    break;
                default:
                    rechargeTimeMoreThan = Rules.PLAYER_RECHARGE_TIME;
                    break;
            }
        }
    }
}