package com.shuttle.game.ui.panels.info;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shuttle.game.screens.GameScreen;

public class Bars {
    private BarFuel barFuel;
    private BarHP barHP;
    private BarLaser barLaser;
    private BarShield barShield;
    private GameScreen gs;

    public Bars(GameScreen gs) {
        this.barFuel = new BarFuel(gs);
        this.barHP = new BarHP(gs);
        this.barLaser = new BarLaser(gs);
        this.barShield = new BarShield(gs);
    }

    public void update(float dt) {
        barFuel.update(dt);
        barHP.update(dt);
        barLaser.update(dt);
        barShield.update(dt);
    }

    public void render(SpriteBatch batch) {
        barFuel.render(batch);
        barHP.render(batch);
        barLaser.render(batch);
        barShield.render(batch);
    }
}
