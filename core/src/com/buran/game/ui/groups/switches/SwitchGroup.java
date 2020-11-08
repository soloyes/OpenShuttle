package com.buran.game.ui.groups.switches;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.buran.game.conf.PaintConstants;
import com.buran.game.utils.Assets;

import lombok.Getter;

/**
 * @author soloyes on 3/30/19.
 */

public class SwitchGroup extends Group {
    private static SwitchGroup instance;
    private TextureRegionDrawable region;
    @Getter
    private Switch leftSwitch;
    @Getter
    private Switch rightSwitch;

    public static SwitchGroup getInstance() {
        if (instance == null) {
            instance = new SwitchGroup();
        }
        return instance;
    }

    private SwitchGroup() {
        this.region = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.SWITCH_ON));
        this.leftSwitch = new Switch(region, true);
        this.rightSwitch = new Switch(region, false);
        this.addActor(leftSwitch);
        this.addActor(rightSwitch);
    }

    public void dispose() {
        instance = null;
    }

    public static void doStart() {
        instance = null;
    }
}