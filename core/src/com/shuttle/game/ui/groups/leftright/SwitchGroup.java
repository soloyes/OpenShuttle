package com.shuttle.game.ui.groups.leftright;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.utils.Assets;

import lombok.Getter;

/**
 * @author Shuttle on 3/30/19.
 */
public class SwitchGroup extends Group {
    private static SwitchGroup instance;

    public static SwitchGroup getInstance() {
        if (instance == null) {
            instance = new SwitchGroup();
        }
        return instance;
    }

    private TextureRegionDrawable onRegion;
    @Getter
    private Switch leftSwitch;
    @Getter
    private Switch rightSwitch;

    private SwitchGroup() {
        this.onRegion = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.SWITCH_ON));
        this.leftSwitch = new Switch(onRegion, true);
        this.rightSwitch = new Switch(onRegion, false);
        this.addActor(leftSwitch);
        this.addActor(rightSwitch);
    }

    public void dispose() {
        instance = null;
    }
}
