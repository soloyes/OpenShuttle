package com.shuttle.game.ui;

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

public class UILatestPanel {
    private ArrayList<LabelTime> activeLabel;
    private ArrayList<LabelTime> showLabel;
    private ArrayList<LabelTime> freeLabel;
    private ArrayList<Integer> needDel;
    @Getter
    @Setter
    protected Color color;
    private int width, height;
    private TextureRegion textureRegion;
    private TextureRegion patternTexture;
    private TextureRegion fuelText;
    private HashMap<Label.Sticker, TextureRegion> textureRegions;
    private float t1, t2, t3;
    private float startPoint;
    private int maxShowed;

    private class LabelTime {
        private TextureRegion actTexture;
        private float time;
        float y;
        float alpha;
        float fleshAlpha;
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
            this.scaleX = this.scaleY = 0.5f;
        }
    }

    public UILatestPanel() {
        this.activeLabel = new ArrayList<LabelTime>();
        this.freeLabel = new ArrayList<LabelTime>();
        this.showLabel = new ArrayList<LabelTime>();
        this.needDel = new ArrayList<Integer>();
        this.color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        this.width = PaintConstants.PANEL_ELEMENT_WIDTH;
        this.height = PaintConstants.PANEL_ELEMENT_HEIGHT;
        this.t1 = PaintConstants.PANEL_ELEMENT_LIFETIME;
        this.t2 = PaintConstants.PANEL_ELEMENT_FLASH_TIME;
        this.t3 = PaintConstants.PANEL_ELEMENT_FADING_TIME;
        this.maxShowed = PaintConstants.PANEL_ELEMENTS_MAX;
        this.startPoint = Rules.WORLD_HEIGHT / 2 - height / 4;
        this.textureRegion = new TextureRegion();
        this.patternTexture = Assets.getInstance().getAtlas().findRegion(PaintConstants.PATTERN_SIGN_LABEL_REGION);
        this.fuelText = Assets.getInstance().getAtlas().findRegion(PaintConstants.FUEL_LABEL_REGION);
        this.textureRegions = new HashMap<Label.Sticker, TextureRegion>();
        //todo: repeatable code
        for (int i = 0; i < Label.Sticker.values().length; i++) {
            Label.Sticker st = Label.Sticker.values()[i];
            switch (st) {
                case FUEL:
                    textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.FUEL_LABEL_REGION);
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
            if (!lt.actTexture.equals(fuelText)) {
                color.r = 0.25f;
                color.b = 0.85f;
                color.g = 0.23f;
                batch.setColor(color);
                batch.draw(patternTexture,
                        0 - width / 8, startPoint - height / 4 - lt.y,
                        width / 4, height / 4,
                        width, height,
                        lt.scaleX, lt.scaleY,
                        0.0f
                );
            }

            color.r = 0.33f;
            color.b = 0.89f;
            color.g = 0.57f;
            batch.setColor(color);
            batch.draw(lt.actTexture,
                    0 - width / 8, startPoint - height / 4 - lt.y,
                    width / 4, height / 4,
                    width, height,
                    lt.scaleX, lt.scaleY,
                    0.0f
            );


            if (lt.time > t1 - t2 - t3) {
                color.r = 1;
                color.b = 1;
                color.g = 1;
                color.a = lt.fleshAlpha;
                batch.setColor(color);
                batch.draw(lt.actTexture,
                        0 - width / 8, startPoint - height / 4 - lt.y,
                        width / 4, height / 4,
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
                            startPoint += dt * (1.0f / t2) * height / 4;
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
                        labelTime.y = height / 2 * (showLabel.size() - k - 1);
                        startPoint = Rules.WORLD_HEIGHT / 2 + height * (showLabel.size() - 1) / 4;
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
                sh.scaleX = sh.scaleY = 0.5f + (t1 - sh.time) / t2 / 10;
            } else {
                if (sh.time > t1 - t2 - t3) {
                    sh.scaleX = sh.scaleY = 0.5f + (sh.time - (t1 - t2 - t3)) / t3 / 10;
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
                startPoint -= dt * (1.0f / t2) * height / 4;
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
            startPoint = Rules.WORLD_HEIGHT / 2 + height * (showLabel.size() - 1) / 4;
        }

        //todo:refactor this
//        if (showLabel.size() > 0) {
//            LabelTime sl = showLabel.get(showLabel.size() - 1);
//            for (int i = 0; i < showLabel.size(); i++
//                    ) {
//                LabelTime sh = showLabel.get(i);
//                if (sl.time < t1 - t2 && sl.time > t1 - t2 - t3) {
//                    sh.y += dt * height / 2 / (t3);
//                } else {
//                    if (sl.time < t1 - t2 - t3) {
//                        sh.y = height / 2 * (showLabel.size() - i);
//                    }
//                }
//            }
//        }
    }
}
