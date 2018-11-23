package com.shuttle.game.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.shuttle.game.bot.anomaly.CorrosiveAnomaly;
import com.shuttle.game.bot.anomaly.ElectricalAnomaly;
import com.shuttle.game.bot.anomaly.MagneticAnomaly;
import com.shuttle.game.bot.anomaly.RadiationAnomaly;
import com.shuttle.game.bot.asteroids.Asteroid;
import com.shuttle.game.bot.consumable.Astronaut;
import com.shuttle.game.bot.consumable.Crate;
import com.shuttle.game.bot.consumable.Fuel;
import com.shuttle.game.bot.consumable.Storage;
import com.shuttle.game.bot.pool.AbstractPoolable;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.utils.Assets;

import java.util.ArrayList;

/**
 * @author Shuttle on 10/10/18.
 */
public class GameOver {
    private Label titleLabel;
    private Label resLabel;
    private ArrayList<AbstractPoolable> poolable;
    private GameScreen gs;
    private GlyphLayout glyphLayout;
    private BitmapFont font96;
    private float picX, picY = 1600.0f, dY = 100.0f;

    public GameOver(GameScreen gs) {
        this.gs = gs;
        this.glyphLayout = new GlyphLayout();
        Label.LabelStyle style96 = new Label.LabelStyle(
                Assets.getInstance().getAssetManager().get(PaintConstants.FONT96, BitmapFont.class),
                new Color(0.8f, 0.43f, 0.33f, 1f)
        );
        this.font96 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT96);
        font96.setColor(0.8f, 0.43f, 0.33f, 1f);

        this.titleLabel = new Label("GAME OVER", style96);
        this.resLabel = new Label("", style96);

        picX = ScreenManager.getInstance().getViewport().getWorldWidth() / 4;

        titleLabel.setPosition(ScreenManager.getInstance().getViewport().getWorldWidth() / 2
                        - titleLabel.getWidth() / 2,
                picY);

        poolable = new ArrayList<AbstractPoolable>();
        CorrosiveAnomaly corrosiveAnomalyTAN = new CorrosiveAnomaly(gs);
        CorrosiveAnomaly corrosiveAnomalyGREEN = new CorrosiveAnomaly(gs);
        CorrosiveAnomaly corrosiveAnomalyYELLOW = new CorrosiveAnomaly(gs);
        CorrosiveAnomaly corrosiveAnomalyRED = new CorrosiveAnomaly(gs);
        int[] levels = new int[]{0, 1, 2, 3};

        picY -= dY;
        corrosiveAnomalyTAN.init(picX, picY);
        corrosiveAnomalyTAN.setLevel(0);
        corrosiveAnomalyTAN.setScaleX(1.0f + levels[0] * 0.15f);
        corrosiveAnomalyTAN.setScaleY(1.0f + levels[0] * 0.15f);

        picY -= dY;
        corrosiveAnomalyGREEN.init(picX, picY);
        corrosiveAnomalyGREEN.setLevel(1);
        corrosiveAnomalyGREEN.setScaleX(1.0f + levels[1] * 0.15f);
        corrosiveAnomalyGREEN.setScaleY(1.0f + levels[1] * 0.15f);

        picY -= dY;
        corrosiveAnomalyYELLOW.init(picX, picY);
        corrosiveAnomalyYELLOW.setLevel(2);
        corrosiveAnomalyYELLOW.setScaleX(1.0f + levels[2] * 0.15f);
        corrosiveAnomalyYELLOW.setScaleY(1.0f + levels[2] * 0.15f);

        picY -= dY;
        corrosiveAnomalyRED.init(picX, picY);
        corrosiveAnomalyRED.setLevel(3);
        corrosiveAnomalyRED.setScaleX(1.0f + levels[3] * 0.15f);
        corrosiveAnomalyRED.setScaleY(1.0f + levels[3] * 0.15f);

        picY -= dY;
        ElectricalAnomaly electricalAnomaly = new ElectricalAnomaly(gs);
        electricalAnomaly.init(picX, picY);
        electricalAnomaly.setScaleX(1.0f);
        electricalAnomaly.setScaleY(1.0f);
        electricalAnomaly.getColor().a = 1.0f;

        picY -= dY;
        RadiationAnomaly radiationAnomaly = new RadiationAnomaly(gs);
        radiationAnomaly.init(picX, picY);
        radiationAnomaly.setScaleX(1.0f);
        radiationAnomaly.setScaleY(1.0f);
        radiationAnomaly.getColor().a = 1.0f;

        picY -= dY;
        MagneticAnomaly magneticAnomaly = new MagneticAnomaly(gs);
        magneticAnomaly.init(picX, picY);
        magneticAnomaly.setScaleX(1.0f);
        magneticAnomaly.setScaleY(1.0f);
        magneticAnomaly.getColor().a = 1.0f;

        picY -= dY;
        Astronaut astronaut = new Astronaut(gs);
        astronaut.setFake(true);
        astronaut.init(picX, picY);
        astronaut.setWidth(corrosiveAnomalyTAN.getWidth());
        astronaut.setHeight(corrosiveAnomalyTAN.getHeight());

        picY -= dY;
        Crate crate = new Crate(gs);
        crate.init(picX, picY);
        crate.setWidth(128);
        crate.setHeight(128);

        picY -= dY;
        Fuel fuel = new Fuel(gs);
        fuel.init(picX, picY);
        fuel.setWidth(128);
        fuel.setHeight(128);

        picY -= dY;
        Storage storage = new Storage(gs);
        storage.init(picX, picY);
        storage.setWidth(128);
        storage.setHeight(128);

        picY -= dY;
        Asteroid asteroid = new Asteroid(gs);
        asteroid.init(picX, picY);
        asteroid.setScaleX(1.0f);
        asteroid.setScaleY(1.0f);


        poolable.add(corrosiveAnomalyTAN);
        poolable.add(corrosiveAnomalyGREEN);
        poolable.add(corrosiveAnomalyYELLOW);
        poolable.add(corrosiveAnomalyRED);
        poolable.add(electricalAnomaly);
        poolable.add(magneticAnomaly);
        poolable.add(radiationAnomaly);
        poolable.add(astronaut);
        poolable.add(crate);
        poolable.add(fuel);
        poolable.add(storage);
        poolable.add(asteroid);

        picY -= dY;
    }

    public void draw(SpriteBatch batch) {
        titleLabel.draw(batch, batch.getColor().a);
        font96.getColor().a = batch.getColor().a;
        font96.draw(batch, glyphLayout,
                ScreenManager.getInstance().getViewport().getWorldWidth() / 2 - glyphLayout.width / 2,
                picY
        );
        Color tmpColor = batch.getColor();
        for (int i = 0; i < poolable.size(); i++) {
            AbstractPoolable abstractPoolable = poolable.get(i);
            if (abstractPoolable instanceof CorrosiveAnomaly) {
                CorrosiveAnomaly tmpCorrosive = (CorrosiveAnomaly) abstractPoolable;
                batch.setColor(
                        tmpCorrosive.getColor().r,
                        tmpCorrosive.getColor().g,
                        tmpCorrosive.getColor().b,
                        tmpColor.a
                );
                tmpCorrosive.basicRender(batch);
            } else if (abstractPoolable instanceof ElectricalAnomaly) {
                batch.setColor(tmpColor);
                ElectricalAnomaly tmpElectrical = (ElectricalAnomaly) abstractPoolable;
                tmpElectrical.basicRender(batch);
            } else if (abstractPoolable instanceof MagneticAnomaly) {
                batch.setColor(tmpColor);
                MagneticAnomaly tmpMagnetic = (MagneticAnomaly) abstractPoolable;
                tmpMagnetic.basicRender(batch);
            } else if (abstractPoolable instanceof RadiationAnomaly) {
                batch.setColor(tmpColor);
                RadiationAnomaly tmpRadiation = (RadiationAnomaly) abstractPoolable;
                tmpRadiation.basicRender(batch);
            } else if (abstractPoolable instanceof Astronaut) {
                batch.setColor(tmpColor);
                Astronaut tmpAstronaut = (Astronaut) abstractPoolable;
                tmpAstronaut.basicRender(batch);
            } else if (abstractPoolable instanceof Crate) {
                batch.setColor(tmpColor);
                Crate tmpCrate = (Crate) abstractPoolable;
                tmpCrate.basicRender(batch);
            } else if (abstractPoolable instanceof Fuel) {
                batch.setColor(tmpColor);
                Fuel tmpFuel = (Fuel) abstractPoolable;
                tmpFuel.basicRender(batch);
            } else if (abstractPoolable instanceof Storage) {
                batch.setColor(tmpColor);
                Storage tmpStorage = (Storage) abstractPoolable;
                tmpStorage.basicRender(batch);
            } else if (abstractPoolable instanceof Asteroid) {
                batch.setColor(tmpColor);
                Asteroid tmpAsteroid = (Asteroid) abstractPoolable;
                tmpAsteroid.basicRender(batch);
            }
            batch.setColor(tmpColor);
            resLabel.setText(
                    gs.getPlayer()
                            .getStanding()
                            .getFromStatistics(poolable.get(i).getType(), poolable.get(i).getLevel())
                            .toString()
            );
            resLabel.setPosition(
                    3 * poolable.get(i).getPosition().x,
                    poolable.get(i).getPosition().y - resLabel.getHeight() / 2
            );
            resLabel.draw(batch, batch.getColor().a);
        }
    }

    public void update(float delta) {
        for (int i = 0; i < poolable.size(); i++) {
            poolable.get(i).basicUpdate(delta);
        }
        glyphLayout.setText(font96, "SCORE: " + String.valueOf(gs.getPlayer().getStanding().getScore()));
    }
}
