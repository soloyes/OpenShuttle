package com.buran.game.ui.groups.switches;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.buran.game.conf.MSConstants;
import com.buran.game.conf.PaintConstants;
import com.buran.game.conf.VConstants;
import com.buran.game.effects.BoomBox;
import com.buran.game.effects.Vibrator;
import com.buran.game.ui.groups.selector.SelectorGroup;
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
    private BoomBox boomBox;
    @Getter
    private boolean isLeft;

    public static SwitchGroup getInstance() {
        if (instance == null) {
            instance = new SwitchGroup();
        }
        return instance;
    }

    private SwitchGroup() {
        this.boomBox = new BoomBox();
        this.region = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.SWITCH_ON));
        this.leftSwitch = new Switch(region, true);
        this.rightSwitch = new Switch(region, false);
        this.addActor(leftSwitch);
        this.addActor(rightSwitch);
        leftSwitch.setOn(false);
        rightSwitch.setOn(true);
        isLeft = true;
        SelectorGroup.getInstance().changePosition(true);
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (leftSwitch.isPressed() && leftSwitch.isOn()) {
                    leftSwitch.setOn(!leftSwitch.isOn());
                    rightSwitch.setOn(!leftSwitch.isOn());
                    boomBox.playSound(MSConstants.UI_SWITCH);
                    Vibrator.vibrateUI(VConstants.UI_CLICK);
                    SelectorGroup.getInstance().changePosition(true);
                    isLeft = true;
                }
                if (rightSwitch.isPressed() && rightSwitch.isOn()) {
                    rightSwitch.setOn(!rightSwitch.isOn());
                    leftSwitch.setOn(!rightSwitch.isOn());
                    boomBox.playSound(MSConstants.UI_SWITCH);
                    Vibrator.vibrateUI(VConstants.UI_CLICK);
                    SelectorGroup.getInstance().changePosition(false);
                    isLeft = false;
                }
            }
        });
    }

    public void dispose() {
        instance = null;
    }

    public static void doStart() {
        instance = null;
    }
}