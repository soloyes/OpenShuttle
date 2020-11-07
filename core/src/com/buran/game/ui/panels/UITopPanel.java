package com.buran.game.ui.panels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @author soloyes on 15/05/19.
 */

public class UITopPanel {
    private com.buran.game.ui.panels.info.PlayerXP playerXP;
    private com.buran.game.ui.panels.info.PlayerHP playerHP;
    private com.buran.game.ui.panels.info.Bars bars;
    private com.buran.game.ui.panels.info.AnomalyTimer anomalyTimer;

    public UITopPanel() {
        this.playerXP = new com.buran.game.ui.panels.info.PlayerXP();
        this.bars = new com.buran.game.ui.panels.info.Bars();
        this.playerHP = new com.buran.game.ui.panels.info.PlayerHP();
        this.anomalyTimer = new com.buran.game.ui.panels.info.AnomalyTimer();
    }

    public void render(SpriteBatch batch) {
        playerXP.render(batch);
        bars.render(batch);
        anomalyTimer.render(batch);
        playerHP.render(batch);
    }

    public void update(float dt) {
        playerXP.update(dt);
        bars.update(dt);
        anomalyTimer.update(dt);
        playerHP.update(dt);
    }
}