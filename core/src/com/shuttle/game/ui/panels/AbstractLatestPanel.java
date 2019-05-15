package com.shuttle.game.ui.panels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.labelsticker.Sticker;

import java.util.ArrayList;

public abstract class AbstractLatestPanel {
    protected int width, height, cWidth, cHeight;
    protected Color color;
    protected ArrayList<LabelTime> activeLabel;
    protected ArrayList<LabelTime> showLabel;
    protected ArrayList<LabelTime> freeLabel;
    protected ArrayList<Integer> needDel;
    protected TextureRegion circleTexture[];
    protected float startPoint;
    protected int maxShowed;
    protected float t1, t2, t3;
    protected float x;

    public abstract void render(SpriteBatch batch);

    public void update(float dt) {
        for (int i = 0; i < activeLabel.size(); i++) {
            LabelTime temp = activeLabel.get(i);
            if (showLabel.size() > 0) {
                temp.changeTime(-dt);
                if (temp.getTime() > t1 - t2) {
                    for (int k = 0; k < showLabel.size(); k++) {
                        LabelTime labelTime = showLabel.get(k);
                        labelTime.changeY(dt * (1.0f / t2) * (height + 22) / 2.0f);
                        labelTime.changeAlpha(-dt * (1.0f / t2) * (1.0f / (float) maxShowed));
                        if (showLabel.size() < maxShowed) {
                            if (labelTime.getAlpha() < 0) {
                                labelTime.setAlpha(0);
                            }
                        }
                    }
                } else {
                    if (showLabel.size() >= 4) {
                        freeLabel.add(showLabel.remove(0));
                    }

                    showLabel.add(temp);
                    needDel.add(i);
                    temp.setTime(t1);
                    for (int k = 0; k < showLabel.size(); k++) {
                        LabelTime labelTime = showLabel.get(k);
                        labelTime.setY((height + 22) * (showLabel.size() - k - 1));
                        if (labelTime.getAlpha() > 1.0f - (1.0f / (float) maxShowed) * (showLabel.size() - k - 1)) {
                            labelTime.setAlpha(1.0f - (1.0f / (float) maxShowed) * (showLabel.size() - k - 1));
                        }
                    }
                }
            } else {
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
            sh.changeTime(-dt);
            if (sh.getTime() > t1 - t2) {
                sh.setFleshAlpha((t1 - sh.getTime()) / t2);
                sh.setScaleX(1.0f + (t1 - sh.getTime()) / t2 / 10);
                sh.setScaleY(1.0f + (t1 - sh.getTime()) / t2 / 10);
            } else {
                if (sh.getTime() > t1 - t2 - t3) {
                    sh.setScaleX(1.0f + (sh.getTime() - (t1 - t2 - t3)) / t3 / 10);
                    sh.setScaleY(1.0f + (sh.getTime() - (t1 - t2 - t3)) / t3 / 10);
                    sh.setFleshAlpha((sh.getTime() - (t1 - t2 - t3)) / t3);
                }
            }
        }

        for (int i = 0; i < showLabel.size(); i++) {
            LabelTime labelTime = showLabel.get(i);
            if (labelTime.getTime() < t2 + t3 && labelTime.getTime() > t2) {
                labelTime.changeAlpha(-dt * labelTime.getAlpha() / (labelTime.getTime() - t2));
                if (labelTime.getAlpha() < 0) {
                    labelTime.setAlpha(0);
                }
            }
        }

        for (int i = 0; i < showLabel.size(); i++) {
            LabelTime sh = showLabel.get(i);
            if (sh.getTime() < t2) {
                if (sh.getTime() < 0) {
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
        }

        updateEnable(dt);
    }

    public abstract void add(Sticker sticker);

    public abstract void updateEnable(float dt);

    public AbstractLatestPanel() {
        this.width = PaintConstants.LEFT_RIGHT_PANEL_ELEMENT_WIDTH;
        this.height = PaintConstants.LEFT_RIGHT_PANEL_ELEMENT_HEIGHT;
        this.cWidth = PaintConstants.LEFT_RIGHT_PANEL_CIRCLE_WIDTH;
        this.cHeight = PaintConstants.LEFT_RIGHT_PANEL_CIRCLE_HEIGHT;
        this.maxShowed = PaintConstants.PANEL_ELEMENTS_MAX;
        this.color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        this.startPoint = Rules.WORLD_HEIGHT / 2;
        this.freeLabel = new ArrayList<LabelTime>();
        this.showLabel = new ArrayList<LabelTime>();
        this.needDel = new ArrayList<Integer>();
        this.activeLabel = new ArrayList<LabelTime>();
        this.t1 = PaintConstants.PANEL_ELEMENT_LIFETIME;
        this.t2 = PaintConstants.PANEL_ELEMENT_FLASH_TIME;
        this.t3 = PaintConstants.PANEL_ELEMENT_FADING_TIME;
    }
}
