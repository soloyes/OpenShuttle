package com.shuttle.game.ui.panels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.labelsticker.Sticker;
import com.shuttle.game.utils.StickerToTexture;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractLatestPanel {
    protected HashMap<Sticker, TextureRegion> textureRegions;
    protected int width, height, cWidth, cHeight;
    protected Color color;
    protected TextureRegion circleTexture[];
    protected float startPoint;
    protected float x;
    private ArrayList<LabelTime> activeLabel;
    private ArrayList<LabelTime> showLabel;
    private ArrayList<LabelTime> freeLabel;
    private ArrayList<Integer> needDel;
    private int maxShowed;
    private float t1, t2, t3;

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
        this.textureRegions = new HashMap<Sticker, TextureRegion>();
        StickerToTexture stickerToTexture = new StickerToTexture();
        for (int i = 0; i < Sticker.values().length; i++) {
            Sticker sticker = Sticker.values()[i];
            textureRegions.put(sticker, stickerToTexture.getRegion(sticker));
        }
    }

    public void add(Sticker sticker) {
        TextureRegion region = textureRegions.get(sticker);
        if (freeLabel.size() == 0) {
            freeLabel.add(new LabelTime(region));
        }
        LabelTime temp = freeLabel.remove(freeLabel.size() - 1);
        if (sticker == Sticker.FUEL) {
            region = textureRegions.get(Sticker.FUEL_UI);
        }
        temp.init(region);
        activeLabel.add(temp);
    }

    public void render(SpriteBatch batch) {
        Color tmpColor = batch.getColor();
        for (int i = 0; i < showLabel.size(); i++) {
            LabelTime lt = showLabel.get(i);
            color.a = lt.getAlpha();
            batch.setColor(color);
            for (int j = 0; j < circleTexture.length; j++) {
                batch.draw(circleTexture[j],
                        x, startPoint - lt.getY(),
                        cWidth / 2, cHeight / 2,
                        cWidth, cHeight,
                        lt.getScaleX(), lt.getScaleY(),
                        0.0f
                );
            }
            basicRender(batch, lt);
            if (lt.getTime() > t1 - t2 - t3) {
                color.a = lt.getFleshAlpha();
                basicRender(batch, lt);
            }
        }
        batch.setColor(tmpColor);
    }

    public abstract void basicRender(SpriteBatch batch, LabelTime lt);

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
            if (labelTime.getAlpha() < 0) {
                labelTime.setAlpha(0);
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

    public abstract void updateEnable(float dt);
}
