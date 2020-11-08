package com.buran.game.ui.groups;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.buran.game.conf.PaintConstants;
import com.buran.game.conf.Rules;
import com.buran.game.effects.Oscillator;
import com.buran.game.effects.Ray;
import com.buran.game.effects.particle.ParticleEmitter;
import com.buran.game.utils.Assets;

/**
 * @author soloyes on 01/04/20.
 */

public class MenuBackgroundGroup extends Group {
    private TextureRegion storage;
    private TextureRegion astronaut;
    private TextureRegion sitastronaut;
    private TextureRegion shuttle;
    private TextureRegion shield;
    private TextureRegion planet;
    private TextureRegion fuel;
    private TextureRegion crate;
    private TextureRegion asteroid1;
    private TextureRegion asteroid2;

    private Ray.Preset preset;
    private int count = 24;
    private float size = 4;

    private float rotation1;
    private float rotation2;
    private int random1;
    private int random2;
    private float shuttleY;
    private float backY;
    private float angle;
    private int radius;

    private float astronautScale;
    private float asteroidScale1;
    private float asteroidScale2;
    private float shieldScale;
    private Oscillator astronautScaleOscillator;
    private Oscillator asteroidScaleOscillator1;
    private Oscillator asteroidScaleOscillator2;
    private Oscillator shieldScaleOscillator;
    private Oscillator shuttleYOscillator;
    private Oscillator backYOscillator;
    private ParticleEmitter particleEmitter;

    public MenuBackgroundGroup() {
        setName(GroupType.MENU_BACKGROUND.name());
        this.particleEmitter = new ParticleEmitter();
        this.storage = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_MENU_BACKGROUND_STORAGE));
        this.astronaut = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_MENU_BACKGROUND_ASTRONAUT));
        this.sitastronaut = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_MENU_BACKGROUND_SIT_ASTRONAUT));
        this.shuttle = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_MENU_BACKGROUND_SHUTTLE));
        this.shield = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_MENU_BACKGROUND_SHIELD));
        this.planet = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_MENU_BACKGROUND_PLANET));
        this.fuel = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_MENU_BACKGROUND_FUEL));
        this.crate = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_MENU_BACKGROUND_CRATE));
        this.asteroid1 = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_MENU_BACKGROUND_ASTEROID1));
        this.asteroid2 = new TextureRegion(Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_MENU_BACKGROUND_ASTEROID2));
        this.preset = Ray.Preset.GREEN;
        this.astronautScaleOscillator = new Oscillator();
        this.astronautScale = 1.0f;
        this.asteroidScaleOscillator1 = new Oscillator();
        this.asteroidScale1 = 1.0f;
        this.asteroidScaleOscillator2 = new Oscillator();
        this.asteroidScale2 = 1.0f;
        this.shieldScaleOscillator = new Oscillator();
        this.shieldScale = 1.0f;
        this.shuttleYOscillator = new Oscillator();
        this.shuttleY = 1.0f * PaintConstants.UI_MENU_BACKGROUND_SHUTTLE_Y;
        this.backYOscillator = new Oscillator();
        this.backY = PaintConstants.UI_MENU_BACKGROUND_PLANET_Y;
        this.random1 = MathUtils.randomSign();
        this.random2 = -random1;
        this.radius = 200;
        this.angle = 180;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(Color.WHITE);
        float ratio = 1.0f * Rules.WORLD_WIDTH / PaintConstants.UI_MENU_BACKGROUND_PLANET_WIDTH;
        batch.draw(planet,
                PaintConstants.UI_MENU_BACKGROUND_PLANET_X, PaintConstants.UI_MENU_BACKGROUND_PLANET_Y - backY,
                PaintConstants.UI_MENU_BACKGROUND_PLANET_WIDTH * ratio, PaintConstants.UI_MENU_BACKGROUND_PLANET_HEIGHT
        );
        batch.draw(storage,
                Rules.WORLD_WIDTH * 0.42f, PaintConstants.UI_MENU_BACKGROUND_STORAGE_Y - backY
        );
        batch.draw(
                astronaut,
                PaintConstants.UI_MENU_BACKGROUND_ASTRONAUT_X,
                PaintConstants.UI_MENU_BACKGROUND_ASTRONAUT_Y,
                PaintConstants.UI_MENU_BACKGROUND_ASTRONAUT_WIDTH / 2,
                PaintConstants.UI_MENU_BACKGROUND_ASTRONAUT_HEIGHT / 2,
                PaintConstants.UI_MENU_BACKGROUND_ASTRONAUT_WIDTH,
                PaintConstants.UI_MENU_BACKGROUND_ASTRONAUT_HEIGHT,
                astronautScale,
                astronautScale,
                0.0f
        );
        batch.draw(sitastronaut,
                Rules.WORLD_WIDTH * 0.55f, PaintConstants.UI_MENU_BACKGROUND_SIT_ASTRONAUT_Y - backY
        );
        batch.draw(shuttle, PaintConstants.UI_MENU_BACKGROUND_SHUTTLE_X, shuttleY, PaintConstants.UI_MENU_BACKGROUND_SHUTTLE_WIDTH, PaintConstants.UI_MENU_BACKGROUND_SHUTTLE_HEIGHT);
        batch.draw(
                shield,
                PaintConstants.UI_MENU_BACKGROUND_SHIELD_X,
                PaintConstants.UI_MENU_BACKGROUND_SHIELD_Y,
                PaintConstants.UI_MENU_BACKGROUND_SHIELD_WIDTH / 2,
                PaintConstants.UI_MENU_BACKGROUND_SHIELD_HEIGHT / 2,
                PaintConstants.UI_MENU_BACKGROUND_SHIELD_WIDTH,
                PaintConstants.UI_MENU_BACKGROUND_SHIELD_HEIGHT,
                shieldScale, shieldScale,
                0.0f
        );
        batch.draw(
                crate,
                PaintConstants.UI_MENU_BACKGROUND_CRATE_X,
                PaintConstants.UI_MENU_BACKGROUND_CRATE_Y,
                PaintConstants.UI_MENU_BACKGROUND_CRATE_WIDTH / 2,
                PaintConstants.UI_MENU_BACKGROUND_CRATE_HEIGHT / 2,
                PaintConstants.UI_MENU_BACKGROUND_CRATE_WIDTH,
                PaintConstants.UI_MENU_BACKGROUND_CRATE_HEIGHT,
                1.0f, 1.0f,
                rotation1 * random1
        );
        batch.draw(
                asteroid1,
                Rules.WORLD_WIDTH * 0.65f,
                PaintConstants.UI_MENU_BACKGROUND_ASTEROID1_Y,
                PaintConstants.UI_MENU_BACKGROUND_ASTEROID1_WIDTH / 2,
                PaintConstants.UI_MENU_BACKGROUND_ASTEROID1_HEIGHT / 2,
                PaintConstants.UI_MENU_BACKGROUND_ASTEROID1_WIDTH,
                PaintConstants.UI_MENU_BACKGROUND_ASTEROID1_HEIGHT,
                asteroidScale1, asteroidScale1,
                rotation1 * random1
        );
        batch.draw(
                asteroid2,
                Rules.WORLD_WIDTH * 0.85f + radius * MathUtils.cos(angle),
                PaintConstants.UI_MENU_BACKGROUND_ASTEROID2_Y + radius * MathUtils.sin(angle),
                PaintConstants.UI_MENU_BACKGROUND_ASTEROID2_WIDTH / 2,
                PaintConstants.UI_MENU_BACKGROUND_ASTEROID2_HEIGHT / 2,
                PaintConstants.UI_MENU_BACKGROUND_ASTEROID2_WIDTH,
                PaintConstants.UI_MENU_BACKGROUND_ASTEROID2_HEIGHT,
                asteroidScale2, asteroidScale2,
                rotation2 * random2
        );
        batch.draw(
                fuel,
                PaintConstants.UI_MENU_BACKGROUND_FUEL_X,
                PaintConstants.UI_MENU_BACKGROUND_FUEL_Y,
                PaintConstants.UI_MENU_BACKGROUND_FUEL_WIDTH / 2,
                PaintConstants.UI_MENU_BACKGROUND_FUEL_HEIGHT / 2,
                PaintConstants.UI_MENU_BACKGROUND_FUEL_WIDTH,
                PaintConstants.UI_MENU_BACKGROUND_FUEL_HEIGHT,
                1.0f, 1.0f,
                rotation1 * random2
        );

        particleEmitter.setupFromPointToPoint(
                PaintConstants.UI_MENU_BACKGROUND_SHIELD_X + PaintConstants.UI_MENU_BACKGROUND_SHIELD_WIDTH / 3 - 30,
                PaintConstants.UI_MENU_BACKGROUND_SHIELD_Y + PaintConstants.UI_MENU_BACKGROUND_SHIELD_HEIGHT / 3 - 20,
                PaintConstants.UI_MENU_BACKGROUND_ASTRONAUT_X + PaintConstants.UI_MENU_BACKGROUND_ASTRONAUT_WIDTH / 2,
                PaintConstants.UI_MENU_BACKGROUND_ASTRONAUT_Y + PaintConstants.UI_MENU_BACKGROUND_ASTRONAUT_HEIGHT / 2,
                1.0f,
                2.0f,
                preset.getStartR(), preset.getStartG(), preset.getStartB(), 0.3f,
                preset.getEndR(), preset.getEndG(), preset.getEndB(), 0.3f
        );

        for (int j = 0; j < count; j++) {
            float ang = 6.28f / count * j;
            float radX = astronautScale * PaintConstants.UI_MENU_BACKGROUND_ASTRONAUT_WIDTH / 2;
            float radY = astronautScale * PaintConstants.UI_MENU_BACKGROUND_ASTRONAUT_HEIGHT / 2;
            particleEmitter.setup(
                    PaintConstants.UI_MENU_BACKGROUND_ASTRONAUT_X + PaintConstants.UI_MENU_BACKGROUND_ASTRONAUT_WIDTH / 2 + radX * (float) Math.cos(ang) + MathUtils.random(-10, 10),
                    PaintConstants.UI_MENU_BACKGROUND_ASTRONAUT_Y + PaintConstants.UI_MENU_BACKGROUND_ASTRONAUT_HEIGHT / 2 + radY * (float) Math.sin(ang) + MathUtils.random(-10, 10),
                    0, 0, 0.1f, size, size,
                    preset.getStartR(), preset.getStartG(), preset.getStartB(), 0.2f,
                    preset.getEndR(), preset.getEndG(), preset.getEndB(), 0.0f
            );
        }

        particleEmitter.render((SpriteBatch) batch);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        particleEmitter.update(delta);
        astronautScale = astronautScaleOscillator.oscillate(astronautScale, 0.05f, 0.9f, 1.0f, delta);
        asteroidScale1 = asteroidScaleOscillator1.oscillate(asteroidScale1, 0.01f, 0.9f, 1.0f, delta);
        asteroidScale2 = asteroidScaleOscillator2.oscillate(asteroidScale2, 0.03f, 0.8f, 1.0f, delta);
        shieldScale = shieldScaleOscillator.oscillate(shieldScale, 0.09f, 0.95f, 1.0f, delta);
        shuttleY = shuttleYOscillator.oscillate(shuttleY, 12.0f, PaintConstants.UI_MENU_BACKGROUND_SHUTTLE_Y, PaintConstants.UI_MENU_BACKGROUND_SHUTTLE_Y + 15, delta);
        backY = backYOscillator.oscillate(backY, 6.0f, PaintConstants.UI_MENU_BACKGROUND_PLANET_Y, PaintConstants.UI_MENU_BACKGROUND_PLANET_Y + 50, delta);
        rotation1 += delta;
        rotation2 += 10 * delta;
        angle += delta / 5;
    }
}