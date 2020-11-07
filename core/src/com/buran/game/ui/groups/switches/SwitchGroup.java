package com.buran.game.ui.groups.switches;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.ui.groups.switches.Switch;
import com.shuttle.game.utils.Assets;

import lombok.Getter;

/**
 * @author soloyes on 3/30/19.
 */

public class SwitchGroup extends Group {
    private static SwitchGroup instance;
    private TextureRegionDrawable region;
    @Getter
    private com.shuttle.game.ui.groups.switches.Switch leftSwitch;
    @Getter
    private com.shuttle.game.ui.groups.switches.Switch rightSwitch;

    public static SwitchGroup getInstance() {
        if (instance == null) {
            instance = new SwitchGroup();
        }
        return instance;
    }

    private SwitchGroup() {
        this.region = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.SWITCH_ON));
        this.leftSwitch = new com.shuttle.game.ui.groups.switches.Switch(region, true);
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