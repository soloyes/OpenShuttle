package com.shuttle.game.ui.panels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shuttle.game.ui.panels.info.AnomalyTimer;
import com.shuttle.game.ui.panels.info.Bars;
import com.shuttle.game.ui.panels.info.PlayerHP;
import com.shuttle.game.ui.panels.info.PlayerXP;

/**
 * @author Shuttle on 3/1/19.
 */
public class UIInfoPanel {
    private PlayerXP playerXP;
    private PlayerHP playerHP;
    private Bars bars;
    private AnomalyTimer anomalyTimer;

    public UIInfoPanel() {
        this.playerXP = new PlayerXP();
        this.bars = new Bars();
        this.anomalyTimer = new AnomalyTimer();
        this.playerHP = new PlayerHP();
    }

    public void update(float dt) {
        playerXP.update(dt);
        bars.update(dt);
        anomalyTimer.update(dt);
        playerHP.update(dt);
    }

    public void render(SpriteBatch batch) {
        playerXP.render(batch);
        bars.render(batch);
        anomalyTimer.render(batch);
        playerHP.render(batch);
    }

    public void dispose() {
        anomalyTimer.dispose();
    }
}
