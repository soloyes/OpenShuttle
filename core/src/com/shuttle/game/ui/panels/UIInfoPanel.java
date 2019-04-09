package com.shuttle.game.ui.panels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.ui.panels.info.AnomalyTimer;
import com.shuttle.game.ui.panels.info.Bars;
import com.shuttle.game.ui.panels.info.PlayerXP;

/**
 * @author Shuttle on 3/1/19.
 */
public class UIInfoPanel {

    private PlayerXP playerXP;
    private Bars bars;
    private AnomalyTimer anomalyTimer;
    private GameScreen gs;

    public UIInfoPanel(GameScreen gs) {
        this.gs = gs;
        this.playerXP = new PlayerXP();
        this.bars = new Bars(gs);
        this.anomalyTimer = new AnomalyTimer(gs);
    }

    public void update(float dt) {
        playerXP.update(dt);
        bars.update(dt);
        anomalyTimer.update(dt);
    }

    public void render(SpriteBatch batch) {
        playerXP.render(batch);
        bars.render(batch);
        anomalyTimer.render(batch);
    }

    public void dispose() {
        anomalyTimer.dispose();
    }
}
