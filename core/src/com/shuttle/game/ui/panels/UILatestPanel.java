package com.shuttle.game.ui.panels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.bot.consumable.Label;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.utils.Assets;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;

public class UILatestPanel extends UIAbstractPanel {
    @Getter
    @Setter
    protected Color color;
    private ArrayList<LabelTime> activeLabel;
    private ArrayList<LabelTime> showLabel;
    private ArrayList<LabelTime> freeLabel;
    private ArrayList<Integer> needDel;
    private int width, height, cWidth, cHeight;
    private TextureRegion textureRegion;
    private TextureRegion circleTexture[];
    private HashMap<Label.Sticker, TextureRegion> textureRegions;
    private float t1, t2, t3;
    private float startPoint;
    private int maxShowed;

    public UILatestPanel() {
        this.activeLabel = new ArrayList<LabelTime>();
        this.freeLabel = new ArrayList<LabelTime>();
        this.showLabel = new ArrayList<LabelTime>();
        this.needDel = new ArrayList<Integer>();
        this.color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        this.width = PaintConstants.PANEL_ELEMENT_WIDTH;
        this.height = PaintConstants.PANEL_ELEMENT_HEIGHT;
        this.cWidth = PaintConstants.LEFT_RIGHT_PANEL_CIRCLE_WIDTH;
        this.cHeight = PaintConstants.LEFT_RIGHT_PANEL_CIRCLE_HEIGHT;
        this.t1 = PaintConstants.PANEL_ELEMENT_LIFETIME;
        this.t2 = PaintConstants.PANEL_ELEMENT_FLASH_TIME;
        this.t3 = PaintConstants.PANEL_ELEMENT_FADING_TIME;
        this.maxShowed = PaintConstants.PANEL_ELEMENTS_MAX;
        this.startPoint = Rules.WORLD_HEIGHT / 2;
        this.textureRegion = new TextureRegion();
        this.circleTexture = new TextureRegion[2];
        this.circleTexture[0] = Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_PANEL_FULL_CIRCLE);
        this.circleTexture[1] = Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_PANEL_EMPTY_CIRCLE);
        this.textureRegions = new HashMap<Label.Sticker, TextureRegion>();
        //todo: repeatable code
        for (int i = 0; i < Label.Sticker.values().length; i++) {
            Label.Sticker st = Label.Sticker.values()[i];
            switch (st) {
                case FUEL:
                    textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.FUEL_LABEL_REGION_UI);
                    break;
                case BOOST:
                    textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.BOOST_LABEL_REGION);
                    break;
                case SCAN:
                    textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.SCAN_LABEL_REGION);
                    break;
                case LASER:
                    textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.LASER_LABEL_REGION);
                    break;
                case RADAR:
                    textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.RADAR_LABEL_REGION);
                    break;
                case TRASH:
                    textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.TRASH_LABEL_REGION);
                    break;
                case REPAIR:
                    textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.REPAIR_LABEL_REGION);
                    break;
                case ROCKETS:
                    textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.ROCKETS_LABEL_REGION);
                    break;
                case SHABBY:
                    textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.SHABBY_SIGN_LABEL_REGION);
                    break;
                case SHIELD:
                    textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.BOX_SHIELD_LABEL_REGION);
                    break;
                case SPLASH:
                    textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.SPLASH_LABEL_REGION);
                    break;
                case PATTERN:
                    textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.PATTERN_SIGN_LABEL_REGION);
                    break;
            }
            textureRegions.put(st, textureRegion);
        }
    }

    public void addLabel(Label.Sticker sticker) {
        if (freeLabel.size() == 0) {
            freeLabel.add(new LabelTime(sticker, 0.0f));
        }
        LabelTime temp = freeLabel.remove(freeLabel.size() - 1);
        temp.init(sticker);
        activeLabel.add(temp);
    }

    public void render(SpriteBatch batch) {
        Color tmpColor = batch.getColor();
        for (int i = 0; i < showLabel.size(); i++) {
            LabelTime lt = showLabel.get(i);
            color.a = lt.alpha;
            batch.setColor(color);
            for (int j = 0; j < circleTexture.length; j++) {
                batch.draw(circleTexture[j],
                        22, startPoint - cHeight / 2 - lt.y,
                        cWidth / 2, cHeight / 2,
                        cWidth, cHeight,
                        lt.scaleX, lt.scaleY,
                        0.0f
                );
            }
            batch.setColor(color);
            batch.draw(lt.actTexture,
                    32, startPoint - height / 2 - lt.y,
                    width / 2, height / 2,
                    width, height,
                    lt.scaleX, lt.scaleY,
                    0.0f
            );
            if (lt.time > t1 - t2 - t3) {
                color.a = lt.fleshAlpha;
                batch.setColor(color);
                batch.draw(lt.actTexture,
                        32, startPoint - height / 2 - lt.y,
                        width / 2, height / 2,
                        width, height,
                        lt.scaleX, lt.scaleY,
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
                        labelTime.y += dt * (1.0f / t2) * height / 2;
                        labelTime.alpha -= dt * (1.0f / t2) * (1.0f / (float) maxShowed);
                        if (showLabel.size() < 5)
                            startPoint += dt * (1.0f / t2) * height / 2;
                        if (labelTime.alpha < 0) labelTime.alpha = 0;
                    }
                } else {
                    if (showLabel.size() >= 5) {
                        freeLabel.add(showLabel.remove(0));
                    }
                    showLabel.add(temp);
                    needDel.add(i);
                    temp.time = t1;
                    for (int k = 0; k < showLabel.size(); k++) {
                        LabelTime labelTime = showLabel.get(k);
                        labelTime.y = (height + 22) * (showLabel.size() - k - 1);
                        startPoint = Rules.WORLD_HEIGHT / 2 + height * (showLabel.size() - 1) / 2;
                        if (labelTime.alpha > 1.0f - (1.0f / (float) maxShowed) * (showLabel.size() - k - 1))
                            labelTime.alpha = 1.0f - (1.0f / (float) maxShowed) * (showLabel.size() - k - 1);
                    }
                }
            } else {
                startPoint = Rules.WORLD_HEIGHT / 2;
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
                startPoint -= dt * (1.0f / t2) * height / 2;
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
            startPoint = Rules.WORLD_HEIGHT / 2 + height * (showLabel.size() - 1) / 2;
        }
    }

    private class LabelTime {
        float y;
        float alpha;
        float fleshAlpha;
        private TextureRegion actTexture;
        private float time;
        private float scaleX, scaleY;

        public LabelTime(Label.Sticker sticker, float time) {
            this.actTexture = textureRegions.get(sticker);
            this.time = time;
            this.y = 0.0f;
            this.alpha = 1.0f;
            this.fleshAlpha = 0f;
        }

        public void init(Label.Sticker sticker) {
            this.actTexture = textureRegions.get(sticker);
            this.time = t1;
            this.y = 0.0f;
            this.alpha = 1.0f;
            this.fleshAlpha = 0f;
            this.scaleX = this.scaleY = 1.0f;
        }
    }
}
