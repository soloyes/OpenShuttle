package com.shuttle.game.ui.panels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.labelsticker.Sticker;
import com.shuttle.game.ui.groups.leftright.SwitchGroup;
import com.shuttle.game.utils.Assets;

public class UIRightPanel extends AbstractLatestPanel {
    private static UIRightPanel instance = new UIRightPanel();
    private TextureRegion astronautBonus;
    private TextureRegion[] bonusRegions;

    public void doRestart() {
        instance = null;
    }

    public static UIRightPanel getInstance() {
        if (instance == null) {
            instance = new UIRightPanel();
        }
        return instance;
    }

    private UIRightPanel() {
        this.circleTexture = new TextureRegion[2];
        this.circleTexture[0] = Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_PANEL_FULL_CIRCLE);
        this.circleTexture[1] = Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_PANEL_EMPTY_CIRCLE);
        this.astronautBonus = Assets.getInstance().getAtlas().findRegion(PaintConstants.ASTRONAUT_BONUS_ICON);
        this.bonusRegions = new TextureRegion[4];
        this.bonusRegions[0] = Assets.getInstance().getAtlas().findRegion(PaintConstants.MEDIC_BONUS_ICON);
        this.bonusRegions[1] = Assets.getInstance().getAtlas().findRegion(PaintConstants.SCIENTIST_BONUS_ICON);
        this.bonusRegions[2] = Assets.getInstance().getAtlas().findRegion(PaintConstants.MECHANIC_BONUS_ICON);
        this.bonusRegions[3] = Assets.getInstance().getAtlas().findRegion(PaintConstants.MILITARY_BONUS_ICON);
        this.x = Rules.WORLD_WIDTH;
    }

    @Override
    public void add(Sticker sticker) {
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
            batch.draw(astronautBonus,
                    x, startPoint - lt.getY(),
                    cWidth / 2, cHeight / 2,
                    cWidth, cHeight,
                    lt.getScaleX(), lt.getScaleY(),
                    0.0f
            );
            batch.draw(bonusRegions[i],
                    x, startPoint - lt.getY(),
                    cWidth / 2, cHeight / 2,
                    cWidth, cHeight,
                    lt.getScaleX(), lt.getScaleY(),
                    0.0f
            );


            if (lt.getTime() > t1 - t2 - t3) {
                color.a = lt.getFleshAlpha();
                batch.draw(astronautBonus,
                        x, startPoint - lt.getY(),
                        cWidth / 2, cHeight / 2,
                        cWidth, cHeight,
                        lt.getScaleX(), lt.getScaleY(),
                        0.0f
                );
                batch.draw(bonusRegions[i],
                        x, startPoint - lt.getY(),
                        cWidth / 2, cHeight / 2,
                        cWidth, cHeight,
                        lt.getScaleX(), lt.getScaleY(),
                        0.0f
                );
            }
        }
        batch.setColor(tmpColor);
    }

    @Override
    public void updateEnable(float dt) {
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
}