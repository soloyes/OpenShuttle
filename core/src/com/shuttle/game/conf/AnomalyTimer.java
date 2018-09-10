package com.shuttle.game.conf;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 9/10/18.
 */
public class AnomalyTimer {
    private float counter;
    private BitmapFont font64;
    private StringBuilder sb;

    public AnomalyTimer() {
        this.font64 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT64_C, BitmapFont.class);
        this.sb = new StringBuilder();
    }

    public void render(SpriteBatch batch) {
        font64.draw(batch,
                sb,
                ScreenManager.getInstance().getViewport().getWorldWidth() / 2,
                ScreenManager.getInstance().getViewport().getWorldHeight() - 20
        );
    }

    public void update(float dt) {
        sb.setLength(0);
        counter -= dt;

        if (counter >= Rules.ANOMALIES_SHUTTLE_HUNT_TIMER * 0.6f) {
            sb.append((int) counter);
        } else if (counter >= Rules.ANOMALIES_SHUTTLE_HUNT_TIMER * 0.3f){
            sb.append("[YELLOW]").append((int) counter).append("[]");
        } else {
            sb.append("[RED]").append((int) counter).append("[]");
        }

        if (counter <= 0.0f) {
            counter = Rules.ANOMALIES_SHUTTLE_HUNT_TIMER + 1;
        }
    }
}
