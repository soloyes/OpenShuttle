package com.shuttle.game.utils;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.labelsticker.Sticker;

/**
 * @author Shuttle on 3/25/19.
 * Do not refactor to static method. libgds unable to work with textures, calling from static context.
 * Black boxes after restart game
 */
public class StickerToTexture {
    private TextureRegion textureRegion;

    public StickerToTexture() {
        this.textureRegion = new TextureRegion();
    }

    public TextureRegion getRegion(Sticker sticker) {
        switch (sticker) {
            case FUEL:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.FUEL_LABEL_REGION);
                break;
            case FUEL_UI:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.FUEL_LABEL_REGION_UI);
                break;
            case BOOST:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.BOOST_LABEL_REGION);
                break;
            case SCAN:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.SCAN_LABEL_REGION);
                break;
            case LASER:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.LASER_LABEL_REGION);
                break;
            case RADAR:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.RADAR_LABEL_REGION);
                break;
            case TRASH:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.TRASH_LABEL_REGION);
                break;
            case REPAIR:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.REPAIR_LABEL_REGION);
                break;
            case ROCKETS:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.ROCKETS_LABEL_REGION);
                break;
            case SHABBY:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.SHABBY_SIGN_LABEL_REGION);
                break;
            case SHABBY_GOLD:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.SHABBY_SIGN_GOLD_LABEL_REGION);
                break;
            case SHIELD:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.BOX_SHIELD_LABEL_REGION);
                break;
            case SPLASH:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.SPLASH_LABEL_REGION);
                break;
            case PATTERN:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.PATTERN_SIGN_LABEL_REGION);
                break;
            case PATTERN_GOLD:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.PATTERN_SIGN_GOLD_LABEL_REGION);
                break;
            //todo:// нужна иконка
            case REGULAR:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.ASTRONAUT_BONUS_ICON);
                break;
            case MEDIC:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.MEDIC_BONUS_ICON);
                break;
            case MECHANIC:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.MECHANIC_BONUS_ICON);
                break;
            case MILITARY:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.MILITARY_BONUS_ICON);
                break;
            case SCIENTIST:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.SCIENTIST_BONUS_ICON);
                break;
        }
        return textureRegion;
    }
}
