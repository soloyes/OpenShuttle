package com.buran.game.ui.groups.switches;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.buran.game.conf.PaintConstants;
import com.buran.game.conf.Rules;
import com.buran.game.ui.groups.PauseGroup;
import com.buran.game.utils.Assets;

import lombok.Getter;
import lombok.Setter;

/**
 * @author soloyes on 3/30/19.
 */

public class Switch extends ImageButton {
    private TextureRegionDrawable onRegion;
    private TextureRegionDrawable offRegion;
    @Getter
    @Setter
    private boolean on;
    private float width;
    private float height;

    public Switch(TextureRegionDrawable drawable, boolean isLeft) {
        super(drawable);
        this.onRegion = drawable;
        this.offRegion = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.SWITCH_OFF));
        this.width = PaintConstants.SWITCH_WIDTH;
        this.height = PaintConstants.SWITCH_HEIGHT;
        setPosition(
                isLeft ? 0 : Rules.WORLD_WIDTH - width,
                Rules.WORLD_HEIGHT / 3 - height / 2 + 70
        );
        setWidth(width);
        setOrigin(Align.center);
        setTransform(true);
        setRotation(isLeft ? 0.0f : 180.0f);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (on) {
            getStyle().imageUp = onRegion;
        } else {
            getStyle().imageUp = offRegion;
        }
        if (!PauseGroup.isPause() && !Rules.GAME_OVER) {
            setVisible(true);
        } else {
            setVisible(false);
        }
    }
}