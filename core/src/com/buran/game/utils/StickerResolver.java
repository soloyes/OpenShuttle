package com.buran.game.utils;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.buran.game.conf.PaintConstants;
import com.buran.game.labelsticker.Sticker;

/**
 * @author soloyes on 3/25/19.
 * Do not refactor to static method. libgdx unable to work with textures, calling from static context.
 */

public class StickerResolver {
    private TextureRegion textureRegion;
    private Operation operation;

    public StickerResolver() {
        this.textureRegion = new TextureRegion();
    }

    public Operation getOperation(Sticker sticker) {
        resolve(sticker);
        return operation;
    }

    private void resolve(Sticker sticker) {
        this.operation = Operation.DEFAULT;
        switch (sticker) {
            case FUEL:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.FUEL_LABEL_REGION);
                break;
            case FUEL_UI:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.FUEL_LABEL_REGION_UI);
                operation = Operation.SELF;
                break;
            case FREEZE:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.FREEZE_LABEL_REGION);
                operation = Operation.ATTACK;
                break;
            case DEGRADE:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.DEGRADE_LABEL_REGION);
                operation = Operation.ATTACK;
                break;
            case BOOST:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.BOOST_LABEL_REGION);
                operation = Operation.SELF;
                break;
            case SCAN:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.SCAN_LABEL_REGION);
                operation = Operation.PROTECTION;
                break;
            case LASER:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.LASER_LABEL_REGION);
                operation = Operation.ATTACK;
                break;
            case RADAR:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.RADAR_LABEL_REGION);
                operation = Operation.PROTECTION;
                break;
            case TRASH:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.TRASH_LABEL_REGION);
                operation = Operation.SELF;
                break;
            case REPAIR:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.REPAIR_LABEL_REGION);
                operation = Operation.SELF;
                break;
            case ROCKETS:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.ROCKETS_LABEL_REGION);
                operation = Operation.ATTACK;
                break;
            case SHABBY:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.SHABBY_SIGN_LABEL_REGION);
                break;
            case SHABBY_GOLD:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.SHABBY_SIGN_GOLD_LABEL_REGION);
                break;
            case SHIELD:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.BOX_SHIELD_LABEL_REGION);
                operation = Operation.PROTECTION;
                break;
            case SPLASH:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.SPLASH_LABEL_REGION);
                operation = Operation.ATTACK;
                break;
            case PATTERN:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.PATTERN_SIGN_LABEL_REGION);
                break;
            case PATTERN_GOLD:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.PATTERN_SIGN_GOLD_LABEL_REGION);
                break;
            case COMMANDER:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.ASTRONAUT_ICON);
                operation = Operation.SELF;
                break;
            case MEDIC:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.MEDIC_ICON);
                operation = Operation.SELF;
                break;
            case MECHANIC:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.MECHANIC_ICON);
                operation = Operation.SELF;
                break;
            case MILITARY:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.MILITARY_ICON);
                operation = Operation.SELF;
                break;
            case SCIENTIST:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.SCIENTIST_ICON);
                operation = Operation.SELF;
                break;
        }
    }

    public TextureRegion getRegion(Sticker sticker) {
        resolve(sticker);
        return textureRegion;
    }

    public enum Operation {
        DEFAULT, SELF, PROTECTION, ATTACK;
    }
}