package com.shuttle.game.ui.groups.switches;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.ui.groups.PauseGroup;
import com.shuttle.game.utils.Assets;

import lombok.Getter;

/**
 * @author Shuttle on 3/30/19.
 */
//todo:// sound here
public class Switch extends ImageButton {
    private TextureRegionDrawable onRegion;
    private TextureRegionDrawable offRegion;
    @Getter
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
                isLeft ? width / 2 : Rules.WORLD_WIDTH - 3 * width / 2,
                477 + (5 * PaintConstants.LEFT_RIGHT_UI_CELL_HEIGHT) / 2 - height / 2
        );

        setOrigin(Align.center);
        setTransform(true);
        setRotation(isLeft ? 0.0f : 180.0f);
        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (isPressed()) {
                    if (on) {
                        getStyle().imageUp = onRegion;
                        on = false;
                    } else {
                        getStyle().imageUp = offRegion;
                        on = true;
                    }
                }
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!PauseGroup.getInstance().isPause() && !Rules.GAME_OVER) {
            super.draw(batch, parentAlpha);
        }
    }
}
