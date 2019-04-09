package com.shuttle.game.ui.panels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.Assets;

import java.util.ArrayList;

/**
 * @author Shuttle on 3/1/19.
 */
public class UIBonusPanel {
    private int width, height, bWidth, bHeight;
    private float startPoint;
    private TextureRegion circleTexture[];
    private TextureRegion astronautBonus;
    private TextureRegion[] bonusRegions;
    private ArrayList<LabelTime> activeLabel;
    private Color color;
    private float t1, t2, t3;
    private float x;
    private GameScreen gs;

    //todo:// check GameOverScreen: how to calculate Astronauts + gold boxes to fix.
    public UIBonusPanel(GameScreen gs) {
        this.gs = gs;
        this.width = PaintConstants.LEFT_RIGHT_PANEL_CIRCLE_WIDTH;
        this.height = PaintConstants.LEFT_RIGHT_PANEL_CIRCLE_HEIGHT;
        this.bWidth = PaintConstants.BONUS_ICON_WIDTH;
        this.bHeight = PaintConstants.BONUS_ICON_HEIGHT;
        this.startPoint = Rules.WORLD_HEIGHT / 2;
        this.color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        this.circleTexture = new TextureRegion[2];
        this.circleTexture[0] = Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_PANEL_FULL_CIRCLE);
        this.circleTexture[1] = Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_PANEL_EMPTY_CIRCLE);
        this.astronautBonus = Assets.getInstance().getAtlas().findRegion(PaintConstants.ASTRONAUT_BONUS_ICON);
        this.bonusRegions = new TextureRegion[4];
        this.bonusRegions[0] = Assets.getInstance().getAtlas().findRegion(PaintConstants.MEDIC_BONUS_ICON);
        this.bonusRegions[1] = Assets.getInstance().getAtlas().findRegion(PaintConstants.SCIENTIST_BONUS_ICON);
        this.bonusRegions[2] = Assets.getInstance().getAtlas().findRegion(PaintConstants.MECHANIC_BONUS_ICON);
        this.bonusRegions[3] = Assets.getInstance().getAtlas().findRegion(PaintConstants.MILITARY_BONUS_ICON);
        this.activeLabel = new ArrayList<LabelTime>();
        for (int i = 0; i < 4; i++) {
            this.activeLabel.add(new LabelTime());
        }
        this.t1 = PaintConstants.PANEL_ELEMENT_LIFETIME;
        //this.t2 = PaintConstants.PANEL_ELEMENT_FLASH_TIME;
        this.t2 = 0.4f;
        //this.t3 = PaintConstants.PANEL_ELEMENT_FADING_TIME;
        this.t3 = 0.6f;
        this.x = Rules.WORLD_WIDTH;
    }

    public void addBonus(int num) {

        if (activeLabel.get(num - 1).time <= 0) {
            activeLabel.get(num - 1).time = 10;
            activeLabel.get(num - 1).active = true;
        }
    }

    public void render(SpriteBatch batch) {
        Color tmpColor = batch.getColor();

        for (int i = 0; i < 4; i++) {
            LabelTime tmp = activeLabel.get(i);
            color.a = tmp.alpha;
            batch.setColor(color);
            for (int j = 0; j < circleTexture.length; j++) {
                batch.draw(circleTexture[j],
                        x, startPoint - (22 + height) * i,
                        width / 2, height / 2,
                        width, height,
                        1.0f, 1.0f,
                        0.0f
                );
            }
            batch.draw(astronautBonus,
                    x, startPoint - (22 + bHeight) * i,
                    bWidth / 2, bHeight / 2,
                    bWidth, bHeight,
                    tmp.scaleX, tmp.scaleY,
                    0.0f
            );
            batch.draw(bonusRegions[i],
                    x, startPoint - (22 + bHeight) * i,
                    bWidth / 2, bHeight / 2,
                    bWidth, bHeight,
                    tmp.scaleX, tmp.scaleY,
                    0.0f
            );


            if (tmp.time > t1 - 2.0f * t2 - t3) {
                color.a = tmp.fleshAlpha;
                batch.setColor(color);
                batch.draw(astronautBonus,
                        x, startPoint - (22 + bHeight) * i,
                        bWidth / 2, bHeight / 2,
                        bWidth, bHeight,
                        tmp.scaleX, tmp.scaleY,
                        0.0f
                );
                batch.draw(bonusRegions[i],
                        x, startPoint - (22 + bHeight) * i,
                        bWidth / 2, bHeight / 2,
                        bWidth, bHeight,
                        tmp.scaleX, tmp.scaleY,
                        0.0f
                );
            }
        }
        batch.setColor(tmpColor);
    }

    public void update(float dt) {
        for (int i = 0; i < 4; i++) {
            LabelTime tmp = activeLabel.get(i);

            if (tmp.active) {
                if (tmp.time > 0) {
                    tmp.time -= dt;
                }
                if (10.0f - tmp.time <= t2) {
                    tmp.x += dt * (1.0f / t2) * (width + 32) * 2.0f;
                    tmp.alpha += dt * (1.0f / t2) * 2.0f;
                    if (tmp.alpha > 1) tmp.alpha = 1.0f;
                }
                if (10.0f - tmp.time > t2 && 10.0f - tmp.time < t2 + t3) {
                    tmp.x = (width + 32);
                }
                if (tmp.time < t2 && tmp.time > 0) {
                    tmp.x -= dt * (1.0f / t2) * (width + 32) * 2.0f;
                    tmp.alpha -= dt * (1.0f / t2) * 2.0f;
                    if (tmp.alpha < 0) tmp.alpha = 0.0f;
                }
                if (tmp.time <= 0) {
                    tmp.x = 0;
                }
            }
        }
        for (int i = 0; i < activeLabel.size(); i++) {
            LabelTime sh = activeLabel.get(i);
            sh.time -= dt;
            if (sh.time > 10.0f - 2.0f * t2) {
                sh.fleshAlpha = (10.0f - sh.time) / t2;
                sh.scaleX = sh.scaleY = 1.0f + (10.0f - sh.time) / (2.0f * t2) / 10;
            } else {
                if (sh.time > 10.0f - 2.0f * t2 - t3) {
                    sh.scaleX = sh.scaleY = 1.0f + (sh.time - (10.0f - 2.0f * t2 - t3)) / t3 / 10.0f;
                    sh.fleshAlpha = (sh.time - (10.0f - 2.0f * t2 - t3)) / t3;
                }
            }
        }

        if (gs.getSwitchGroup().getRightSwitch().isOn()) {
            x -= dt * 360;
        } else {
            x += dt * 360;
        }
        if (x >= Rules.WORLD_WIDTH) {
            x = Rules.WORLD_WIDTH;
        }
        if (x <= Rules.WORLD_WIDTH - width - 32) {
            x = Rules.WORLD_WIDTH - width - 32;
        }
    }

    //todo: separate class
    private class LabelTime {
        float x;
        float alpha;
        float fleshAlpha;
        private boolean active;
        private float time;
        private float scaleX, scaleY;

        public LabelTime() {
            this.x = 0.0f;
            this.alpha = 0.0f;
            this.active = false;
            this.time = 0.0f;
            this.scaleX = this.scaleY = 1.0f;
            this.fleshAlpha = 0.0f;
        }
    }
}