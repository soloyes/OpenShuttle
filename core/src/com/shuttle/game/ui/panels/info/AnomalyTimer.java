package com.shuttle.game.ui.panels.info;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.anomaly.AbstractAnomaly;
import com.shuttle.game.bot.anomaly.CorrosiveAnomalyEmitter;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.utils.Assets;

import java.util.List;

/**
 * @author Shuttle on 9/10/18.
 */
public class AnomalyTimer {
    private TextureRegion timerBackground, timerBox, timerIcon;
    private Texture texture;
    private Color color;
    private int width, height, border;
    private float percentage;
    private float counter;
    private Vector2 position;

    public AnomalyTimer() {
        this.counter = Rules.ANOMALIES_CORROSIVE_HUNT_TIME;
        this.width = PaintConstants.TIMER_WIDTH;
        this.height = PaintConstants.TIMER_HEIGHT;
        this.border = 16;
        this.timerBackground = Assets.getInstance().getAtlas().findRegion(PaintConstants.TIMER_BACKGROUND);
        this.timerBox = Assets.getInstance().getAtlas().findRegion(PaintConstants.TIMER_BOX);
        this.timerIcon = Assets.getInstance().getAtlas().findRegion(PaintConstants.TIMER_ICON);

        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGB888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        this.texture = new Texture(pixmap);
        this.color = new Color();
        this.position = new Vector2();
        this.position.set(Rules.WORLD_WIDTH / 2 - width / 2, Rules.WORLD_HEIGHT - height - 44);
    }

    public void dispose() {
        texture.dispose();
    }

    public void render(SpriteBatch batch) {
        Color tmpColor = batch.getColor();
        batch.draw(timerBackground,
                position.x, position.y,
                width / 2, height / 2,
                width, height,
                1.0f, 1.0f,
                0.0f
        );
        batch.setColor(color);
        batch.draw(
                texture,
                position.x + width / 6 + border / 4, position.y + border,
                2 * width / 3 - border / 2, (height - 2 * border) * percentage
        );
        batch.setColor(tmpColor);
        batch.draw(timerBox,
                position.x, position.y,
                width / 2, height / 2,
                width, height,
                1.0f, 1.0f,
                0.0f
        );
        batch.draw(timerIcon,
                position.x, position.y,
                width / 2, height / 2,
                width, height,
                1.0f, 1.0f,
                0.0f
        );
        batch.setColor(tmpColor);
    }

    public void update(float dt) {
        counter -= dt;
        if (counter >= Rules.ANOMALIES_CORROSIVE_HUNT_TIME * 0.6f) {
            color.set(0.25f, 0.78f, 0.78f, 1f);
        } else if (counter >= Rules.ANOMALIES_CORROSIVE_HUNT_TIME * 0.3f) {
            color.set(1.0f, 0.8f, 0.3f, 1f);
        } else {
            color.set(1.0f, 0.2f, 0.0f, 1f);
        }

        if (counter <= 0.0f) {
            counter = Rules.ANOMALIES_CORROSIVE_HUNT_TIME;
            List<AbstractAnomaly> anomalies = CorrosiveAnomalyEmitter.getInstance().getActiveList();
            for (int i = 0; i < anomalies.size(); i++) {
                if (!anomalies.get(i).isActivated()) {
                    anomalies.get(i).setActivated(true);
                }
            }
        }
        percentage = 1.0f * counter / Rules.ANOMALIES_CORROSIVE_HUNT_TIME;
    }
}
