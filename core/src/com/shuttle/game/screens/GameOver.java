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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

        //todo: add Corrosive here
        List<Class<? extends AbstractPoolable>> classes = new LinkedList<Class<? extends AbstractPoolable>>();
        classes.add(ElectricalAnomaly.class);
        classes.add(RadiationAnomaly.class);
        classes.add(MagneticAnomaly.class);
        classes.add(Crate.class);
        classes.add(Storage.class);
        classes.add(Fuel.class);
        classes.add(Astronaut.class);
        classes.add(MetalAsteroid.class);
        classes.add(MineralAsteroid.class);

        //todo: anomalies should start with alpha = 1.0f
        for (int i = 0; i < classes.size(); i++) {
            picY -= dY;
            try {
                Constructor constructor = classes.get(i).getConstructor(GameScreen.class);
                AbstractPoolable abstractPoolable = (AbstractPoolable) constructor.newInstance(gs);
                abstractPoolable.init(picX, picY);
                abstractPoolable.setScaleX(1.0f);
                abstractPoolable.setScaleY(1.0f);
                if (classes.get(i) == Storage.class || classes.get(i) == Fuel.class) {
                    abstractPoolable.setScaleX(0.7f);
                    abstractPoolable.setScaleY(0.7f);
                }
                poolable.add(abstractPoolable);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        poolable.add(corrosiveAnomalyTAN);
        poolable.add(corrosiveAnomalyGREEN);
        poolable.add(corrosiveAnomalyYELLOW);
        poolable.add(corrosiveAnomalyRED);

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
            } else {
                batch.setColor(tmpColor);
            }
            abstractPoolable.basicRender(batch);
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
