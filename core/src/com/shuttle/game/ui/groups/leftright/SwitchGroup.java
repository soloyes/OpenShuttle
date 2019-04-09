package com.shuttle.game.ui.groups.leftright;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.Assets;

import lombok.Getter;

/**
 * @author Shuttle on 3/30/19.
 */
public class SwitchGroup extends Group {
    private TextureRegionDrawable onRegion;
    @Getter
    private Switch leftSwitch;
    @Getter
    private Switch rightSwitch;

    public SwitchGroup(GameScreen gs) {
        this.onRegion = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.SWITCH_ON));
        this.leftSwitch = new Switch(onRegion, gs, true);
        this.rightSwitch = new Switch(onRegion, gs, false);
        this.addActor(leftSwitch);
        this.addActor(rightSwitch);
    }
}
