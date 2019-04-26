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
import java.util.List;

/**
 * @author Shuttle on 10/10/18.
 */
public class GameOver {
    private Label titleLabel;
    private Label resultLabel;
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
        this.resultLabel = new Label("", style96);

        picX = ScreenManager.getInstance().getViewport().getWorldWidth() / 4;

        titleLabel.setPosition(ScreenManager.getInstance().getViewport().getWorldWidth() / 2
                        - titleLabel.getWidth() / 2,
                picY);

        picY -= dY;
        poolable = new ArrayList<AbstractPoolable>();
        int[] levels = new int[]{0, 1, 2, 3};

        List<Class<? extends AbstractPoolable>> classes = new ArrayList<Class<? extends AbstractPoolable>>() {{
            add(CorrosiveAnomaly.class);
            add(ElectricalAnomaly.class);
            add(RadiationAnomaly.class);
            add(MagneticAnomaly.class);
            add(Crate.class);
            add(Storage.class);
            add(Fuel.class);
            add(Astronaut.class);
            add(MetalAsteroid.class);
            add(MineralAsteroid.class);
        }};

        for (int i = 0; i < classes.size(); i++) {
            if (classes.get(i) == Storage.class || classes.get(i) == Fuel.class) {
                init(gs, classes.get(i), 0.7f, 0.7f);
            } else if (classes.get(i) == CorrosiveAnomaly.class) {
                for (int j = 0; j < 4; j++) {
                    init(gs, classes.get(i), 1.0f + levels[j] * 0.15f, 1.0f + levels[j] * 0.15f);
                    poolable.get(j).setLevel(j);
                }
            } else {
                init(gs, classes.get(i), 1.0f, 1.0f);
            }
        }
    }

    private void init(GameScreen gs, Class<? extends AbstractPoolable> abstractPoolableClass, float scaleX, float scaleY) {
        AbstractPoolable abstractPoolable;
        try {
            Constructor constructor = abstractPoolableClass.getConstructor();
            abstractPoolable = (AbstractPoolable) constructor.newInstance();
            abstractPoolable.setFake(true);
            abstractPoolable.init(picX, picY);
            abstractPoolable.setScaleX(scaleX);
            abstractPoolable.setScaleY(scaleY);
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
            resultLabel.setText(
                    Standing.getInstance()
                            .getFromStatistics(poolable.get(i).getType(), poolable.get(i).getLevel())
                            .toString()
            );
            resultLabel.setPosition(
                    3 * poolable.get(i).getPosition().x,
                    poolable.get(i).getPosition().y - resultLabel.getHeight() / 2
            );
            resultLabel.draw(batch, batch.getColor().a);
        }
    }

    public void update(float delta) {
        for (int i = 0; i < poolable.size(); i++) {
            poolable.get(i).basicUpdate(delta);
        }
        glyphLayout.setText(font96, "SCORE: " + String.valueOf(Standing.getInstance().getScore()));
    }
}
