package com.buran.game.ui.panels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.buran.game.conf.PaintConstants;
import com.buran.game.conf.Rules;
import com.buran.game.ui.groups.switches.SwitchGroup;
import com.buran.game.utils.Assets;

/**
 * @author soloyes on 15/05/19.
 */

public class UIRightPanel extends AbstractLatestPanel {
    private static UIRightPanel instance = new UIRightPanel();

    private UIRightPanel() {
        this.circleTexture = new TextureRegion[2];
        this.circleTexture[0] = Assets.getInstance().getAtlas().findRegion(PaintConstants.SELECTOR_OUT);
        this.circleTexture[1] = Assets.getInstance().getAtlas().findRegion(PaintConstants.CIRCLE_GLOW);
        this.x = Rules.WORLD_WIDTH;
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
        if (SwitchGroup.getInstance().getRightSwitch().isOn()) {
            x -= dt * 360;
        } else {
            x += dt * 360;
        }
        if (x >= Rules.WORLD_WIDTH + 32) {
            x = Rules.WORLD_WIDTH + 32;
        }
        if (x <= Rules.WORLD_WIDTH - width - 32) {
            x = Rules.WORLD_WIDTH - width - 32;
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