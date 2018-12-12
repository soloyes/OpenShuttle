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
        this.textureRegion = new TextureRegion();
        this.sticker = sticker;
    }

    public void render(SpriteBatch batch) {
        Color tmpColor = batch.getColor();
        color.a = PaintConstants.LABEL_ALPHA;
        batch.setColor(color);
        switch (sticker) {
            case FUEL:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.FUEL_LABEL_1_REGION);
                break;
            case BOOST:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.BOOST_LABEL_1_REGION);
                break;
            case SCAN:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.SCAN_LABEL_1_REGION);
                break;
            case LASER:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.LASER_LABEL_1_REGION);
                break;
            case RADAR:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.RADAR_LABEL_1_REGION);
                break;
            case TRASH:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.TRASH_LABEL_1_REGION);
                break;
            case REPAIR:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.REPAIR_LABEL_1_REGION);
                break;
            case ROCKET:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.ROCKET_LAUNCHER_LABEL_1_REGION);
                break;
            case SHABBY:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.SHABBY_SIGN_LABEL_1_REGION);
                break;
            case SHIELD:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.BOX_SHIELD_LABEL_1_REGION);
                break;
            case SPLASH:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.SPLASH_LABEL_1_REGION);
                break;
            case PATTERN:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.PATTERN_SIGN_LABEL_1_REGION);
        }

        batch.draw(textureRegion,
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
        FUEL
    }
}
