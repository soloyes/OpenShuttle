package com.shuttle.game.ui.panels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.labelsticker.Sticker;
import com.shuttle.game.ui.groups.leftright.SwitchGroup;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.StickerToTexture;

import java.util.HashMap;

public class UILeftPanel extends AbstractLatestPanel {
    private static UILeftPanel instance = new UILeftPanel();
    private HashMap<Sticker, TextureRegion> textureRegions;

    public void doRestart() {
        instance = null;
    }

    public static UILeftPanel getInstance() {
        if (instance == null) {
            instance = new UILeftPanel();
        }
        return instance;
    }

    private UILeftPanel() {
        this.circleTexture = new TextureRegion[2];
        this.circleTexture[0] = Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_PANEL_FULL_CIRCLE);
        this.circleTexture[1] = Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_PANEL_EMPTY_CIRCLE);
        this.textureRegions = new HashMap<Sticker, TextureRegion>();
        StickerToTexture stickerToTexture = new StickerToTexture();
        for (int i = 0; i < Sticker.values().length; i++) {
            Sticker sticker = Sticker.values()[i];
            textureRegions.put(sticker, stickerToTexture.getRegion(sticker));
        }
        this.x = 0.0f;
    }

    @Override
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
            batch.draw(lt.getActTexture(),
                    x, startPoint - lt.getY(),
                    width / 2, height / 2,
                    width, height,
                    lt.getScaleX(), lt.getScaleY(),
                    0.0f
            );
            if (lt.getTime() > t1 - t2 - t3) {
                color.a = lt.getFleshAlpha();
                batch.draw(lt.getActTexture(),
                        x, startPoint - lt.getY(),
                        width / 2, height / 2,
                        width, height,
                        lt.getScaleX(), lt.getScaleY(),
                        0.0f
                );
            }
        }
        batch.setColor(tmpColor);
    }

    @Override
    public void updateEnable(float dt) {
        if (SwitchGroup.getInstance().getLeftSwitch().isOn()) {
            x += dt * 360;
        } else {
            x -= dt * 360;
        }
        if (x >= 32) {
            x = 32;
        }
        if (x <= -cWidth) {
            x = -cWidth;
        }
    }
}
