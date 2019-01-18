package com.shuttle.game.bot.consumable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.utils.Assets;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import lombok.Getter;
import lombok.Setter;

public class ActiveLabel {
    private LinkedList<LabelTime> activeLabel;
    private LinkedList<LabelTime> showedLabel;
    private LinkedList<LabelTime> freeLabel;
    @Getter
    @Setter
    protected Color color;
    private int width, height;
    private TextureRegion textureRegion;
    private HashMap<Label.Sticker, TextureRegion> textureRegions;
    private float t1;
    private float t2;
    private float t3;
    private float startPoint;
    private int maxShowed;



    private class LabelTime {
        private TextureRegion actTexture;
        private float time;
        float y;
        boolean show;
        float alpha;
        float fleshAlpha;
        private float scaleX, scaleY;

        public LabelTime(Label.Sticker sticker, float time) {
            this.actTexture = textureRegions.get(sticker);
            this.time = time;
            this.y = 0.0f;
            this.show = false;
            this.alpha = 1.0f;
            this.fleshAlpha = 0f;
        }

        public void init(Label.Sticker sticker) {
            actTexture = textureRegions.get(sticker);
            time = t1;
            y = 0.0f;
            show = false;
            alpha = 1.0f;
            fleshAlpha = 0f;
            scaleX = scaleY = 0.5f;
        }
    }

    public ActiveLabel() {
        this.activeLabel = new LinkedList<LabelTime>();
        this.freeLabel = new LinkedList<LabelTime>();
        this.showedLabel = new LinkedList<LabelTime>();
        this.color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        this.width = PaintConstants.PANEL_ELEMENT_WIDTH;
        this.height = PaintConstants.PANEL_ELEMENT_HEIGHT;
        this.t1 = PaintConstants.PANEL_ELEMENT_LIFETIME;
        this.t2 = PaintConstants.PANEL_ELEMENT_FLASH_TIME;
        this.t3 = PaintConstants.PANEL_ELEMENT_FADING_TIME;
        this.maxShowed = PaintConstants.PANEL_ELEMENTS_MAX;
        this.startPoint = Rules.WORLD_HEIGHT / 2 - height / 4;
        this.textureRegion = new TextureRegion();
        this.textureRegions = new HashMap<Label.Sticker, TextureRegion>();
        for (Label.Sticker st : Label.Sticker.values()
                ) {
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
                case ROCKET:
                    textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.ROCKET_LAUNCHER_LABEL_REGION);
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

        for (LabelTime lt : showedLabel
                ) {
            if (!lt.show) {
                color.a = lt.alpha;
                if(!lt.actTexture.equals(Assets.getInstance().getAtlas().findRegion(PaintConstants.FUEL_LABEL_REGION))) {
                    color.r = 0.254f;
                    color.b = 0.859f;
                    color.g = 0.234f;
                    batch.setColor(color);
                    batch.draw(Assets.getInstance().getAtlas().findRegion(PaintConstants.PATTERN_SIGN_LABEL_REGION),
                            0 - width / 8, startPoint - height / 4 - lt.y,
                            width / 4, height / 4,
                            width, height,
                            lt.scaleX, lt.scaleY,
                            0.0f
                    );
                }

                color.r = 0.332f;
                color.b = 0.898f;
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
    }

    public void update(float dt) {
        if (activeLabel.size() > 0) {
            for (Iterator<LabelTime> k = activeLabel.iterator(); k.hasNext(); ) {
                LabelTime temp = k.next();

                if (showedLabel.size() > 0) {
                    temp.time -= dt;
                    if (temp.time > t1 - t2) {
                        for (int i = 0; i < showedLabel.size(); i++) {
                            LabelTime labelTime = showedLabel.get(i);
                            labelTime.y += dt * (1.0f / t2) * height / 2;
                            labelTime.alpha -= dt * (1.0f / t2) * (1.0f / (float) maxShowed);
                            if (showedLabel.size() < 5)
                                startPoint += dt * (1.0f / t2) * height / 4;
                            if (labelTime.alpha < 0) labelTime.alpha = 0;
                        }
                    } else {
                        if (showedLabel.size() >= 5) {
                            freeLabel.add(showedLabel.remove(0));
                        }
                        showedLabel.add(temp);
                        k.remove();

                        temp.time = t1;
                        for (int i = 0; i < showedLabel.size(); i++) {
                            LabelTime labelTime = showedLabel.get(i);
                            labelTime.y = height / 2 * (showedLabel.size() - i - 1);
                            startPoint = Rules.WORLD_HEIGHT / 2 +  height * (showedLabel.size() - 1) / 4;
                            if (labelTime.alpha > 1.0f - (1.0f / (float) maxShowed) * (showedLabel.size() - i - 1))
                            labelTime.alpha = 1.0f - (1.0f / (float) maxShowed) * (showedLabel.size() - i - 1);
                        }
                    }
                } else {
                    startPoint = Rules.WORLD_HEIGHT / 2;
                    showedLabel.add(temp);
                    k.remove();
                }
            }
        }

        for (LabelTime sh : showedLabel) {
            sh.time -= dt;
            if (sh.time > t1 - t2) {
                sh.fleshAlpha = (t1 - sh.time) / t2;
                sh.scaleX = sh.scaleY = 0.5f + (t1 - sh.time) /  t2 / 10;
            } else {
                if (sh.time > t1 - t2 - t3) {
                    sh.scaleX = sh.scaleY = 0.5f + (sh.time - (t1 - t2 - t3)) / t3 / 10;
                    sh.fleshAlpha = (sh.time - (t1 - t2 - t3)) / t3 ;
                }
            }
        }
        for (int i = 0; i < showedLabel.size(); i++) {
            LabelTime labelTime = showedLabel.get(i);
            if (labelTime.time < t2 + t3 && labelTime.time > t2){
                System.out.println(labelTime.alpha);
                    labelTime.alpha -= dt * labelTime.alpha / (labelTime.time - t2);
                if (labelTime.alpha < 0){
                    labelTime.alpha = 0;
                }
            }
        }

            for (Iterator<LabelTime> k = showedLabel.iterator(); k.hasNext(); ) {
                LabelTime sh = k.next();
            if (sh.time < t2) {
                System.out.println(sh.alpha);
                    startPoint -= dt * (1.0f / t2) * height / 4;
                    if (sh.time < 0){
                        freeLabel.add(sh);
                        k.remove();
                        startPoint = Rules.WORLD_HEIGHT / 2 +  height * (showedLabel.size() - 1) / 4;
                    }
                }
            }
        //todo:refactor this

//        if (showedLabel.size() > 0) {
//            LabelTime sl = showedLabel.get(showedLabel.size() - 1);
//            for (int i = 0; i < showedLabel.size(); i++
//                    ) {
//                LabelTime sh = showedLabel.get(i);
//                if (sl.time < t1 - t2 && sl.time > t1 - t2 - t3) {
//                    sh.y += dt * height / 2 / (t3);
//                } else {
//                    if (sl.time < t1 - t2 - t3) {
//                        sh.y = height / 2 * (showedLabel.size() - i);
//                    }
//                }
//            }
//        }
    }
}
