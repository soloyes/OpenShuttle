package com.shuttle.game.ui.panels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.ui.groups.leftright.SwitchGroup;
import com.shuttle.game.utils.Assets;

import java.util.ArrayList;

public class UIBonusPanel {
    private static UIBonusPanel instance;
    private int width, height, bWidth, bHeight;
    private Color color;
    private ArrayList<LabelTime> activeLabel;
    private ArrayList<LabelTime> showLabel;
    private ArrayList<LabelTime> freeLabel;
    private ArrayList<Integer> needDel;
    private TextureRegion circleTexture[];
    private TextureRegion astronautBonus;
    private TextureRegion[] bonusRegions;
    private float startPoint;
    private int maxShowed;
    private float t1, t2, t3;
    private float x;

    public static UIBonusPanel getInstance() {
        if (instance == null) {
            instance = new UIBonusPanel();
        }
        return instance;
    }

    public void doRestart() {
        instance = null;
    }

    private UIBonusPanel() {
        this.width = PaintConstants.LEFT_RIGHT_PANEL_CIRCLE_WIDTH;
        this.height = PaintConstants.LEFT_RIGHT_PANEL_CIRCLE_HEIGHT;
        this.bWidth = PaintConstants.BONUS_ICON_WIDTH;
        this.bHeight = PaintConstants.BONUS_ICON_HEIGHT;
        this.startPoint = Rules.WORLD_HEIGHT / 2;
        this.maxShowed = PaintConstants.PANEL_ELEMENTS_MAX;
        this.color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        this.freeLabel = new ArrayList<LabelTime>();
        this.showLabel = new ArrayList<LabelTime>();
        this.needDel = new ArrayList<Integer>();
        this.activeLabel = new ArrayList<LabelTime>();
        this.circleTexture = new TextureRegion[2];
        this.circleTexture[0] = Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_PANEL_FULL_CIRCLE);
        this.circleTexture[1] = Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_PANEL_EMPTY_CIRCLE);
        this.t1 = PaintConstants.PANEL_ELEMENT_LIFETIME;
        this.t2 = PaintConstants.PANEL_ELEMENT_FLASH_TIME;
        this.t3 = PaintConstants.PANEL_ELEMENT_FADING_TIME;
        this.x = Rules.WORLD_WIDTH;
        this.astronautBonus = Assets.getInstance().getAtlas().findRegion(PaintConstants.ASTRONAUT_BONUS_ICON);
        this.bonusRegions = new TextureRegion[4];
        this.bonusRegions[0] = Assets.getInstance().getAtlas().findRegion(PaintConstants.MEDIC_BONUS_ICON);
        this.bonusRegions[1] = Assets.getInstance().getAtlas().findRegion(PaintConstants.SCIENTIST_BONUS_ICON);
        this.bonusRegions[2] = Assets.getInstance().getAtlas().findRegion(PaintConstants.MECHANIC_BONUS_ICON);
        this.bonusRegions[3] = Assets.getInstance().getAtlas().findRegion(PaintConstants.MILITARY_BONUS_ICON);
    }

    public void addBonus() {
        if (freeLabel.size() == 0) {
            freeLabel.add(new LabelTime());
        }
        LabelTime temp = freeLabel.remove(freeLabel.size() - 1);
        temp.init();
        activeLabel.add(temp);
    }

    public void render(SpriteBatch batch) {
        Color tmpColor = batch.getColor();
        for (int i = 0; i < showLabel.size(); i++) {
            LabelTime tmp = showLabel.get(i);
            color.a = tmp.alpha;
            batch.setColor(color);
            for (int j = 0; j < circleTexture.length; j++) {
                batch.draw(circleTexture[j],
                        x, startPoint - tmp.y,
                        width / 2, height / 2,
                        width, height,
                        1.0f, 1.0f,
                        0.0f
                );
            }
            batch.draw(astronautBonus,
                    x, startPoint - tmp.y,
                    bWidth / 2, bHeight / 2,
                    bWidth, bHeight,
                    tmp.scaleX, tmp.scaleY,
                    0.0f
            );
            batch.draw(bonusRegions[i],
                    x, startPoint - tmp.y,
                    bWidth / 2, bHeight / 2,
                    bWidth, bHeight,
                    tmp.scaleX, tmp.scaleY,
                    0.0f
            );


            if (tmp.time > t1 - t2 - t3) {
                color.a = tmp.fleshAlpha;
                batch.setColor(color);
                batch.draw(astronautBonus,
                        x, startPoint - tmp.y,
                        bWidth / 2, bHeight / 2,
                        bWidth, bHeight,
                        tmp.scaleX, tmp.scaleY,
                        0.0f
                );
                batch.draw(bonusRegions[i],
                        x, startPoint - tmp.y,
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
        for (int i = 0; i < activeLabel.size(); i++) {
            LabelTime temp = activeLabel.get(i);
            if (showLabel.size() > 0) {
                temp.time -= dt;
                if (temp.time > t1 - t2) {
                    for (int k = 0; k < showLabel.size(); k++) {
                        LabelTime labelTime = showLabel.get(k);
                        labelTime.y += dt * (1.0f / t2) * (height + 22) / 2.0f;
                        labelTime.alpha -= dt * (1.0f / t2) * (1.0f / (float) maxShowed);
                        if (showLabel.size() < maxShowed)
                            //startPoint += dt * (1.0f / t2) * (height + 22) / 2;
                            if (labelTime.alpha < 0) labelTime.alpha = 0;
                    }
                } else {
                    if (showLabel.size() >= 4) {
                        freeLabel.add(showLabel.remove(0));
                    }

                    showLabel.add(temp);
                    needDel.add(i);
                    temp.time = t1;
                    for (int k = 0; k < showLabel.size(); k++) {
                        LabelTime labelTime = showLabel.get(k);
                        labelTime.y = (height + 22) * (showLabel.size() - k - 1);
                        //startPoint = Rules.WORLD_HEIGHT / 2 - (height + 22) * (4 - showLabel.size()) / 2;
                        if (labelTime.alpha > 1.0f - (1.0f / (float) maxShowed) * (showLabel.size() - k - 1))
                            labelTime.alpha = 1.0f - (1.0f / (float) maxShowed) * (showLabel.size() - k - 1);
                    }
                }
            } else {
                //startPoint = Rules.WORLD_HEIGHT / 2 - (height + 22) * (4 - showLabel.size()) / 2;
                showLabel.add(temp);
                needDel.add(i);
            }
        }

        if (needDel.size() > 0) {
            for (int i = needDel.size() - 1; i >= 0; i--) {
                activeLabel.remove((int) needDel.get(i));
                needDel.remove(i);
            }
        }

        for (int i = 0; i < showLabel.size(); i++) {
            LabelTime sh = showLabel.get(i);
            sh.time -= dt;
            if (sh.time > t1 - t2) {
                sh.fleshAlpha = (t1 - sh.time) / t2;
                sh.scaleX = sh.scaleY = 1.0f + (t1 - sh.time) / t2 / 10;
            } else {
                if (sh.time > t1 - t2 - t3) {
                    sh.scaleX = sh.scaleY = 1.0f + (sh.time - (t1 - t2 - t3)) / t3 / 10;
                    sh.fleshAlpha = (sh.time - (t1 - t2 - t3)) / t3;
                }
            }
        }

        for (int i = 0; i < showLabel.size(); i++) {
            LabelTime labelTime = showLabel.get(i);
            if (labelTime.time < t2 + t3 && labelTime.time > t2) {
                labelTime.alpha -= dt * labelTime.alpha / (labelTime.time - t2);
                if (labelTime.alpha < 0) {
                    labelTime.alpha = 0;
                }
            }
        }

        for (int i = 0; i < showLabel.size(); i++) {
            LabelTime sh = showLabel.get(i);
            if (sh.time < t2) {
                //startPoint -= dt * (1.0f / t2) * (height + 22) / 2;
                //sh.y -= dt * (1.0f / t2) * (height + 22) / 2;
                if (sh.time < 0) {
                    freeLabel.add(sh);
                    needDel.add(i);

                }
            }
        }
        if (needDel.size() > 0) {
            for (int i = needDel.size() - 1; i >= 0; i--) {
                showLabel.remove((int) needDel.get(i));
                needDel.remove(i);
            }
            //startPoint = Rules.WORLD_HEIGHT / 2  - (height + 22) * (4 - showLabel.size()) / 2;
        }

        if (SwitchGroup.getInstance().getRightSwitch().isOn()) {
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
        float x, y;
        float alpha;
        float fleshAlpha;
        private float time;
        private float scaleX, scaleY;

        public LabelTime() {
            this.x = 0.0f;
            this.y = 0.0f;
            this.alpha = 1.0f;
            this.time = t1;
            this.scaleX = this.scaleY = 1.0f;
            this.fleshAlpha = 0.0f;
        }

        public void init() {
            this.x = 0.0f;
            this.y = 0.0f;
            this.alpha = 1.0f;
            this.time = t1;
            this.scaleX = this.scaleY = 1.0f;
            this.fleshAlpha = 0.0f;
        }
    }
}