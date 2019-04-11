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
import com.shuttle.game.bot.asteroids.MetalAsteroid;
import com.shuttle.game.bot.asteroids.MineralAsteroid;
import com.shuttle.game.bot.consumable.Astronaut;
import com.shuttle.game.bot.consumable.Crate;
import com.shuttle.game.bot.consumable.Storage;
import com.shuttle.game.bot.consumable.fuel.Fuel;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.player.Standing;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.pool.AbstractPoolable;

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
    private float picX, picY = 1700.0f, dY = 100.0f;

    //todo:// check GameOverScreen: how to calculate Astronauts + gold boxes to fix.
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

        picY -= dY;
        Fuel fuel = new Fuel(gs);
        fuel.init(picX, picY);
        fuel.setScaleX(0.7f);
        fuel.setScaleY(0.7f);

        picY -= dY;
        Storage storage = new Storage(gs);
        storage.init(picX, picY);
        storage.setScaleX(0.6f);
        storage.setScaleY(0.6f);

        picY -= dY;
        MineralAsteroid mineralAsteroid = new MineralAsteroid(gs);
        mineralAsteroid.init(picX, picY);
        mineralAsteroid.setScaleX(1.0f);
        mineralAsteroid.setScaleY(1.0f);

        picY -= dY;
        MetalAsteroid metalAsteroid = new MetalAsteroid(gs);
        metalAsteroid.init(picX, picY);
        metalAsteroid.setScaleX(1.0f);
        metalAsteroid.setScaleY(1.0f);

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
        poolable.add(mineralAsteroid);
        poolable.add(metalAsteroid);

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
                CorrosiveAnomaly anomaly = (CorrosiveAnomaly) abstractPoolable;
                batch.setColor(
                        anomaly.getColor().r,
                        anomaly.getColor().g,
                        anomaly.getColor().b,
                        tmpColor.a
                );
                anomaly.basicRender(batch);
            } else if (abstractPoolable instanceof ElectricalAnomaly) {
                batch.setColor(tmpColor);
                ElectricalAnomaly anomaly = (ElectricalAnomaly) abstractPoolable;
                anomaly.basicRender(batch);
            } else if (abstractPoolable instanceof MagneticAnomaly) {
                batch.setColor(tmpColor);
                MagneticAnomaly anomaly = (MagneticAnomaly) abstractPoolable;
                anomaly.basicRender(batch);
            } else if (abstractPoolable instanceof RadiationAnomaly) {
                batch.setColor(tmpColor);
                RadiationAnomaly anomaly = (RadiationAnomaly) abstractPoolable;
                anomaly.basicRender(batch);
            } else if (abstractPoolable instanceof Astronaut) {
                batch.setColor(tmpColor);
                Astronaut astronaut = (Astronaut) abstractPoolable;
                astronaut.basicRender(batch);
            } else if (abstractPoolable instanceof Crate) {
                batch.setColor(tmpColor);
                Crate crate = (Crate) abstractPoolable;
                crate.basicRender(batch);
            } else if (abstractPoolable instanceof Fuel) {
                batch.setColor(tmpColor);
                Fuel fuel = (Fuel) abstractPoolable;
                fuel.render(batch);
            } else if (abstractPoolable instanceof Storage) {
                batch.setColor(tmpColor);
                Storage tmpStorage = (Storage) abstractPoolable;
                tmpStorage.basicRender(batch);
            } else if (abstractPoolable instanceof MineralAsteroid) {
                batch.setColor(tmpColor);
                MineralAsteroid asteroid = (MineralAsteroid) abstractPoolable;
                asteroid.basicRender(batch);
            } else if (abstractPoolable instanceof MetalAsteroid) {
                batch.setColor(tmpColor);
                MetalAsteroid asteroid = (MetalAsteroid) abstractPoolable;
                asteroid.basicRender(batch);
            }
            batch.setColor(tmpColor);
            resLabel.setText(
                    Standing.getInstance()
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
        glyphLayout.setText(font96, "SCORE: " + String.valueOf(Standing.getInstance().getScore()));
    }
}
