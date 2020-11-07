package com.buran.game.ui.panels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author soloyes on 15/05/19.
 */

public class UIRightPanel extends AbstractLatestPanel {
    private static UIRightPanel instance = new UIRightPanel();

    private UIRightPanel() {
        this.circleTexture = new TextureRegion[2];
        this.circleTexture[0] = com.buran.game.utils.Assets.getInstance().getAtlas().findRegion(com.buran.game.conf.PaintConstants.SELECTOR_OUT);
        this.circleTexture[1] = com.buran.game.utils.Assets.getInstance().getAtlas().findRegion(com.buran.game.conf.PaintConstants.CIRCLE_GLOW);
        this.x = com.buran.game.conf.Rules.WORLD_WIDTH;
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
        if (com.buran.game.ui.groups.switches.SwitchGroup.getInstance().getRightSwitch().isOn()) {
            x -= dt * 360;
        } else {
            x += dt * 360;
        }
        if (x >= com.buran.game.conf.Rules.WORLD_WIDTH + 32) {
            x = com.buran.game.conf.Rules.WORLD_WIDTH + 32;
        }
        if (x <= com.buran.game.conf.Rules.WORLD_WIDTH - width - 32) {
            x = com.buran.game.conf.Rules.WORLD_WIDTH - width - 32;
        }
    }

    public void dispose() {
        instance = null;
    }

    public static void doStart() {
        instance = null;
    }

    public static UIRightPanel getInstance() {
        if (instance == null) {
            instance = new UIRightPanel();
        }
        return instance;
    }
}