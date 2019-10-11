package com.shuttle.game.ui.panels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.ui.groups.switches.SwitchGroup;
import com.shuttle.game.utils.Assets;

public class UIRightPanel extends AbstractLatestPanel {
    private static UIRightPanel instance = new UIRightPanel();
    private TextureRegion astronautBonus;

    private UIRightPanel() {
        this.circleTexture = new TextureRegion[2];
        this.circleTexture[0] = Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_PANEL_FULL_CIRCLE);
        this.circleTexture[1] = Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_PANEL_EMPTY_CIRCLE);
        this.astronautBonus = Assets.getInstance().getAtlas().findRegion(PaintConstants.ASTRONAUT_BONUS_ICON);
        this.x = Rules.WORLD_WIDTH;
    }

    @Override
    public void basicRender(SpriteBatch batch, LabelTime lt) {
        batch.draw(astronautBonus,
                x, startPoint - lt.getY(),
                cWidth / 2, cHeight / 2,
                cWidth, cHeight,
                lt.getScaleX(), lt.getScaleY(),
                0.0f
        );
        batch.draw(lt.getActTexture(),
                x, startPoint - lt.getY(),
                width / 2, height / 2,
                width, height,
                lt.getScaleX(), lt.getScaleY(),
                0.0f
        );
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

    public void dispose() {
        instance = null;
    }

    public static UIRightPanel getInstance() {
        if (instance == null) {
            instance = new UIRightPanel();
        }
        return instance;
    }
}