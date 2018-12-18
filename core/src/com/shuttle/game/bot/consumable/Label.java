package com.shuttle.game.bot.consumable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.utils.Assets;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 11/12/18.
 */
public class Label {
    private TextureRegion textureRegion;
    @Getter
    @Setter
    private Sticker sticker;
    @Getter
    @Setter
    private Color color;
    @Setter
    private AbstractConsumable consumable;
    @Setter
    private float scaleFactor;
    private float angle;

    public Label(Sticker sticker) {
        this.textureRegion = new TextureRegion();
        this.sticker = sticker;
        this.color = new Color();
    }

    public void render(SpriteBatch batch) {
        //todo: не оптимально
        switch (sticker) {
            case FUEL:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.FUEL_LABEL_REGION);
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
            case ROCKET:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.ROCKET_LAUNCHER_LABEL_REGION);
                break;
            case SHABBY:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.SHABBY_SIGN_LABEL_REGION);
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
        }

        Color tmpColor = batch.getColor();
        batch.setColor(color);
        batch.draw(textureRegion,
                consumable.getPosition().x - consumable.getWidth() / 2, consumable.getPosition().y - consumable.getHeight() / 2,
                consumable.getWidth() / 2, consumable.getHeight() / 2,
                consumable.getWidth(), consumable.getHeight(),
                consumable.getScaleX() / scaleFactor, consumable.getScaleY() / scaleFactor,
                angle
        );
        batch.setColor(tmpColor);
    }

    public void setColor(float r, float g, float b, float a) {
        this.color.r = r;
        this.color.g = g;
        this.color.b = b;
        this.color.a = a;
    }

    public void update(float dt) {
        angle = consumable.getAngle();
        if (consumable.isTaken()){
            if (color.a >= consumable.getColor().a) {
                color.a = consumable.getColor().a;
            }
        } else {
            color.a = PaintConstants.LABEL_ALPHA;
        }
    }

    public enum Sticker {
        //Surprise
        PATTERN,
        SHABBY,
        //Crate
        BOOST,
        REPAIR,
        SHIELD,
        TRASH,
        SPLASH,
        SCAN,
        //Storage
        LASER,
        RADAR,
        ROCKET,
        //Fuel
        FUEL,
    }
}
