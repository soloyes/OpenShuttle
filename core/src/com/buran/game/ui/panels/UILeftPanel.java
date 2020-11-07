package com.buran.game.ui.panels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author soloyes on 15/05/19.
 */

public class UILeftPanel extends AbstractLatestPanel {
    private static UILeftPanel instance = new UILeftPanel();

    private UILeftPanel() {
        this.circleTexture = new TextureRegion[2];
        this.circleTexture[0] = com.buran.game.utils.Assets.getInstance().getAtlas().findRegion(com.buran.game.conf.PaintConstants.SELECTOR_OUT);
        this.circleTexture[1] = com.buran.game.utils.Assets.getInstance().getAtlas().findRegion(com.buran.game.conf.PaintConstants.CIRCLE_GLOW);
        this.x = 0.0f;
    }

    @Override
    public void basicRender(SpriteBatch batch, LabelTime lt) {
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
        if (com.buran.game.ui.groups.switches.SwitchGroup.getInstance().getLeftSwitch().isOn()) {
            x += dt * 360;
        } else {
            x -= dt * 360;
        }
        if (x >= 32) {
            x = 32;
        }
        if (x <= -cWidth - 32) {
            x = -cWidth - 32;
        }
    }

    public void dispose() {
        instance = null;
    }

    public static void doStart() {
        instance = null;
    }

    public static UILeftPanel getInstance() {
        if (instance == null) {
            instance = new UILeftPanel();
        }
        return instance;
    }
}