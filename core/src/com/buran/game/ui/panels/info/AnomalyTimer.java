package com.buran.game.ui.panels.info;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.buran.game.bot.anomaly.AbstractAnomaly;
import com.buran.game.bot.anomaly.CorrosiveAnomaly;
import com.buran.game.bot.anomaly.CorrosiveAnomalyEmitter;
import com.buran.game.conf.PaintConstants;
import com.buran.game.conf.Rules;
import com.buran.game.utils.Assets;
import com.buran.game.utils.RadialSprite;
import com.buran.game.utils.TimeFormat;
import com.buran.game.utils.pool.ObjectPool;

import java.util.List;

/**
 * @author soloyes on 9/10/18.
 */

public class AnomalyTimer {
    private TextureRegion timerBackground;
    private RadialSprite timerBox;
    private Color color;
    private int width, height;
    private float counter;
    private Vector2 position;
    private float angle;
    private GlyphLayout glyphLayout;
    private BitmapFont font44;

    public AnomalyTimer() {
        this.counter = Rules.ANOMALIES_CORROSIVE_HUNT_TIME;
        this.width = PaintConstants.TIMER_WIDTH;
        this.height = PaintConstants.TIMER_HEIGHT;
        this.timerBackground = Assets.getInstance().getAtlas().findRegion(PaintConstants.TIMER_BACKGROUND);
        this.timerBox = new RadialSprite(Assets.getInstance().getAtlas().findRegion(PaintConstants.TIMER_BOX));
        this.font44 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT44, BitmapFont.class);
        this.glyphLayout = new GlyphLayout();
        this.color = new Color();
        this.position = new Vector2();
        this.position.set(Rules.WORLD_WIDTH / 2 - width / 2, Rules.WORLD_HEIGHT - height / 2 - 43);
    }

    public void render(SpriteBatch batch) {
        float x, y;
        if (!Rules.DEATH_MODE) {
            batch.draw(timerBackground,
                    position.x, position.y,
                    width / 2, height / 2,
                    width, height,
                    1.0f, 1.0f,
                    0.0f
            );
            timerBox.draw(batch, position.x, position.y,
                    width, height,
                    angle
            );
            x = Rules.WORLD_WIDTH / 2 - glyphLayout.width / 2;
            y = position.y - 14;
        } else {
            x = Rules.WORLD_WIDTH / 2 - glyphLayout.width / 2;
            y = position.y + glyphLayout.height / 2;
        }
        font44.draw(batch, glyphLayout, x, y);
    }

    public void update(float dt) {
        if (!Rules.DEATH_MODE) {
            counter -= dt;
        }
        if (counter >= Rules.ANOMALIES_CORROSIVE_HUNT_TIME * 0.6f) {
            color.set(Color.WHITE);
        } else if (counter >= Rules.ANOMALIES_CORROSIVE_HUNT_TIME * 0.3f) {
            color.set(1.0f, 0.8f, 0.3f, 1f);
        } else {
            color.set(1.0f, 0.2f, 0.0f, 1f);
        }

        timerBox.setColor(color);

        if (counter <= 0.0f) {
            counter = Rules.ANOMALIES_CORROSIVE_HUNT_TIME;
            CorrosiveAnomalyEmitter corrosiveAnomalyEmitter = (CorrosiveAnomalyEmitter) ObjectPool.EMITTERS.get(CorrosiveAnomaly.class);
            List<AbstractAnomaly> anomalies = corrosiveAnomalyEmitter.getActiveList();
            for (int i = 0; i < anomalies.size(); i++) {
                if (!anomalies.get(i).isActivated()) {
                    anomalies.get(i).setActivated(true);
                }
            }
        }
        angle = -360.0f * counter / Rules.ANOMALIES_CORROSIVE_HUNT_TIME;
        if (!Rules.DEATH_MODE) {
            glyphLayout.setText(font44, TimeFormat.formatMS(counter));
        } else {
            glyphLayout.setText(font44, "Death mode");
            float w = glyphLayout.width;
            glyphLayout.setText(font44, "Death mode", Color.RED, w, Align.center, false);
        }
    }
}