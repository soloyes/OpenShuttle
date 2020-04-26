package com.shuttle.game.ui.panels.info;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.anomaly.AbstractAnomaly;
import com.shuttle.game.bot.anomaly.CorrosiveAnomaly;
import com.shuttle.game.bot.anomaly.CorrosiveAnomalyEmitter;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.RadialSprite;
import com.shuttle.game.utils.TimeFormat;
import com.shuttle.game.utils.pool.ObjectPool;

import java.util.List;

/**
 * @author Shuttle on 9/10/18.
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
        font44.draw(batch,
                glyphLayout,
                Rules.WORLD_WIDTH / 2 - glyphLayout.width / 2,
                position.y - 14
        );
    }

    public void update(float dt) {
        counter -= dt;
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
        glyphLayout.setText(font44, TimeFormat.formatMs(counter));
        angle = -360.0f * counter / Rules.ANOMALIES_CORROSIVE_HUNT_TIME;
    }
}