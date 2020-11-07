package com.buran.game.ui.groups.configs;

import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.ui.groups.GroupType;
import com.shuttle.game.ui.groups.configs.ConfigImageButton;
import com.shuttle.game.utils.Assets;

/**
 * @author soloyes on 10/11/19.
 */

public class RightGroup extends AbstractInfoGroup {
    private static RightGroup instance = new RightGroup();

    public static RightGroup getInstance() {
        if (instance == null) {
            instance = new RightGroup();
        }
        return instance;
    }

    public RightGroup() {
        this.setName(GroupType.RIGHT.name());
        this.up = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_UI_EMPTY_ASTRONAUT));

        com.shuttle.game.ui.groups.configs.ConfigImageButton button = new com.shuttle.game.ui.groups.configs.ConfigImageButton(up, isLeft);
        group.addActor(new Line(button, true));
        buttonList.add(button);
        for (int i = 1; i < N; i++) {
            button = new ConfigImageButton(up, isLeft);
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
        this.isLeft = false;
        scrollPane.setPosition(Rules.WORLD_WIDTH / 2 + PaintConstants.UI_SETTINGS_WIDTH / 2 + 12, 2 * Rules.WORLD_HEIGHT / 3 - PaintConstants.UI_SETTINGS_HEIGHT / 2 - (N * cellHeight) / 2);
        group.align(Align.right);
    }

    public void dispose() {
        instance = null;
    }

    public static void doStart() {
        instance = null;
    }
}