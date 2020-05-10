package com.shuttle.game.ui.groups.switches;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.conf.VConstants;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.effects.Vibrator;
import com.shuttle.game.ui.groups.PauseGroup;
import com.shuttle.game.utils.Assets;

import lombok.Getter;

/**
 * @author Shuttle on 3/30/19.
 */

public class Switch extends ImageButton {
    private TextureRegionDrawable onRegion;
    private TextureRegionDrawable offRegion;
    @Getter
    private boolean on;
    private float width;
    private float height;
    private BoomBox boomBox;

    public Switch(TextureRegionDrawable drawable, boolean isLeft) {
        super(drawable);
        this.boomBox = new BoomBox();
        this.onRegion = drawable;
        this.offRegion = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.SWITCH_OFF));
        this.width = PaintConstants.SWITCH_WIDTH;
        this.height = PaintConstants.SWITCH_HEIGHT;
        setPosition(
                isLeft ? 0 : Rules.WORLD_WIDTH - width,
                Rules.WORLD_HEIGHT / 2
        );
        setWidth(width);
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
                    boomBox.playSound(MSConstants.UI_SWITCH);
                }
            }
        });
        addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Vibrator.vibrate(VConstants.UI_CLICK);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!PauseGroup.isPause() && !Rules.GAME_OVER) {
            setVisible(true);
        } else {
            setVisible(false);
        }
    }
}