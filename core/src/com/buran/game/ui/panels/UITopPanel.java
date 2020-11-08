package com.buran.game.ui.panels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.buran.game.ui.panels.info.AnomalyTimer;
import com.buran.game.ui.panels.info.Bars;
import com.buran.game.ui.panels.info.PlayerHP;
import com.buran.game.ui.panels.info.PlayerXP;

/**
 * @author soloyes on 15/05/19.
 */

public class UITopPanel {
    private PlayerXP playerXP;
    private PlayerHP playerHP;
    private Bars bars;
    private AnomalyTimer anomalyTimer;

    public UITopPanel() {
        this.playerXP = new PlayerXP();
        this.bars = new Bars();
        this.playerHP = new PlayerHP();
        this.anomalyTimer = new AnomalyTimer();
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