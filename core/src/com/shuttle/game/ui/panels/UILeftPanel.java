package com.shuttle.game.ui.panels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.ui.groups.switches.SwitchGroup;
import com.shuttle.game.utils.Assets;

/**
 * @author soloyes on 15/05/19.
 */

public class UILeftPanel extends AbstractLatestPanel {
    private static UILeftPanel instance = new UILeftPanel();

    private UILeftPanel() {
        this.circleTexture = new TextureRegion[2];
        this.circleTexture[0] = Assets.getInstance().getAtlas().findRegion(PaintConstants.SELECTOR_OUT);
        this.circleTexture[1] = Assets.getInstance().getAtlas().findRegion(PaintConstants.CIRCLE_GLOW);
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
        if (SwitchGroup.getInstance().getLeftSwitch().isOn()) {
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