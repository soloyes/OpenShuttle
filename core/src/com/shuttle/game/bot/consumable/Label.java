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
    @Setter
    private Sticker sticker;
    @Getter
    @Setter
    private Color color;
    @Setter
    private AbstractConsumable master;
    @Setter
    private float scaleFactor;

    public Label(Sticker sticker) {
        this.sticker = sticker;
    }

    public void render(SpriteBatch batch) {
        Color tmpColor = batch.getColor();
        color.a = PaintConstants.LABEL_ALPHA;
        batch.setColor(color);
        batch.draw(sticker.sticker,
                master.getPosition().x - master.getWidth() / 2, master.getPosition().y - master.getHeight() / 2,
                master.getWidth() / 2, master.getHeight() / 2,
                master.getWidth(), master.getHeight(),
                master.getScaleX() / scaleFactor, master.getScaleY() / scaleFactor,
                master.getAngle()
        );
        batch.setColor(tmpColor);
    }

    public enum Sticker {
        //Empty
        PATTERN(getRegion(PaintConstants.PATTERN_SIGN_LABEL_2_REGION)),
        SHABBY(getRegion(PaintConstants.SHABBY_SIGN_LABEL_2_REGION)),
        //Crate
        BOOST(getRegion(PaintConstants.BOOST_LABEL_1_REGION)),
        REPAIR(getRegion(PaintConstants.REPAIR_LABEL_1_REGION)),
        SHIELD(getRegion(PaintConstants.BOX_SHIELD_LABEL_1_REGION)),
        TRASH(getRegion(PaintConstants.TRASH_LABEL_1_REGION)),
        SPLASH(getRegion(PaintConstants.SPLASH_LABEL_1_REGION)),
        SCAN(getRegion(PaintConstants.SCAN_LABEL_1_REGION)),
        //Storage
        LASER(getRegion(PaintConstants.LASER_LABEL_1_REGION)),
        RADAR(getRegion(PaintConstants.RADAR_LABEL_1_REGION)),
        ROCKET(getRegion(PaintConstants.ROCKET_LAUNCHER_LABEL_1_REGION)),
        //Fuel
        FUEL(getRegion(PaintConstants.FUEL_LABEL_1_REGION));
        private TextureRegion sticker;

        Sticker(TextureRegion regionSticker) {
            this.sticker = regionSticker;
        }

        private static TextureRegion getRegion(String region) {
            return Assets.getInstance().getAtlas().findRegion(region);
        }
    }
}
