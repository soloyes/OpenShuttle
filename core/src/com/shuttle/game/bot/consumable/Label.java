package com.shuttle.game.bot.consumable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.utils.StickerToTexture;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 11/12/18.
 */
public class Label {
    @Getter
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
    private StickerToTexture stickerToTexture;

    public void setSticker(Sticker sticker) {
        this.sticker = sticker;
        this.textureRegion = stickerToTexture.getRegion(sticker);
    }

    public Label() {
        this.textureRegion = new TextureRegion();
        this.color = new Color();
        this.stickerToTexture = new StickerToTexture();
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
        ROCKETS,
        //Fuel
        FUEL,
        FUEL_UI,
        //Surprise
        PATTERN,
        PATTERN_GOLD,
        SHABBY,
        SHABBY_GOLD;
        public static List<Sticker> SELECTABLE = Arrays.asList(BOOST, REPAIR, SHIELD, SPLASH, SCAN, ROCKETS);
    }

    public enum Layer {
        BACK, LABEL, HIDDEN
    }
}