package com.shuttle.game.ui.groups.configs;

import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.ui.groups.GroupType;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 10/11/19.
 */
public class RightGroup extends AbstractInfoGroup {
    private static RightGroup instance = new RightGroup();

    public RightGroup() {
        this.setName(GroupType.RIGHT.name());
        this.up = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_UI_EMPTY_ASTRONAUT));

        ConfigImageButton button = new ConfigImageButton(up, false);
        group.addActor(new Line(button, true));
        buttonList.add(button);
        for (int i = 1; i < N; i++) {
            button = new ConfigImageButton(up, false);
            group.addActor(new Line(button, false));
            buttonList.add(button);
        }

        group.addActor(table);
        this.addActor(scrollPane);
    }

    @Override
    public void setScrollBarPositions() {
        scrollPane.setScrollBarPositions(true, false);
    }

    @Override
    protected void setPosition() {
        scrollPane.setPosition(894, 477);
        group.align(Align.right);
    }

    public void dispose() {
        instance = null;
    }

    public static RightGroup getInstance() {
        if (instance == null) {
            instance = new RightGroup();
        }
        return instance;
    }
}
