package com.shuttle.game.bot.consumable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.effects.Oscillator;

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
    private AbstractConsumable master;
    @Setter
    private float scaleFactor;
    private Oscillator oscillatorA;
    private float angle;

    public Label(Sticker sticker) {
        this.textureRegion = new TextureRegion();
        this.sticker = sticker;
        this.oscillatorA = new Oscillator();
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
            case BOX_SCAN_NOISE:
                textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.BOX_SCAN_NOISE_REGION);
                break;
        }

        Color tmpColor = batch.getColor();
        batch.setColor(color);
        batch.draw(textureRegion,
                master.getPosition().x - master.getWidth() / 2, master.getPosition().y - master.getHeight() / 2,
                master.getWidth() / 2, master.getHeight() / 2,
                master.getWidth(), master.getHeight(),
                master.getScaleX() / scaleFactor, master.getScaleY() / scaleFactor,
                angle
        );
        batch.setColor(tmpColor);
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
        //Box Scan Noise
        BOX_SCAN_NOISE
    }

    public void update(float dt) {
        switch (sticker) {
            case BOX_SCAN_NOISE:
                angle = 0;
                color.a = oscillatorA.oscillate(color.a, 0.2f, PaintConstants.LABEL_ALPHA_MIN, PaintConstants.LABEL_ALPHA_MAX, dt);
                break;
            default:
                angle = master.getAngle();
                color.a = PaintConstants.LABEL_ALPHA_MIN;
        }
    }
}
