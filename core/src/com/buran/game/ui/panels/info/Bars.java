package com.buran.game.ui.panels.info;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @author soloyes on 16/03/19.
 */

public class Bars {
    private BarFuel barFuel;
    private BarHP barHP;
    private BarLaser barLaser;
    private BarShield barShield;

    public Bars() {
        this.barFuel = new BarFuel();
        this.barHP = new BarHP();
        this.barLaser = new BarLaser();
        this.barShield = new BarShield();
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