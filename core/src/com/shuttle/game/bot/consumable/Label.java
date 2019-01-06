package com.shuttle.game.bot.consumable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
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
    private Sticker sticker;
    @Getter
    @Setter
    private Color color;
    @Setter
    private AbstractConsumable consumable;
    @Setter
    private float scaleFactor;
    private float angle;
    @Setter
    private float d;
    private Vector2 offset;

    public void setSticker(Sticker sticker) {
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
        }
        this.sticker = sticker;
    }

    public Label() {
        this.textureRegion = new TextureRegion();
        this.color = new Color();
        setSticker(Sticker.PATTERN);
        this.offset = new Vector2();
    }

    public void render(SpriteBatch batch) {
        Color tmpColor = batch.getColor();
        batch.setColor(color);
        offset.set(
                consumable.getPosition().x - consumable.getWidth() / 2 + d * MathUtils.cos(MathUtils.degreesToRadians * consumable.getAngle()) * consumable.getScaleX(),
                consumable.getPosition().y - consumable.getHeight() / 2 + d * MathUtils.sin(MathUtils.degreesToRadians * consumable.getAngle()) * consumable.getScaleY()
        );
        batch.draw(textureRegion,
                offset.x, offset.y,
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
        if (consumable.isTaken()) {
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
        PATTERN_GOLD,
        SHABBY,
        SHABBY_GOLD,
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

    public enum Layer {
        BACK, LABEL, HIDDEN
    }
}