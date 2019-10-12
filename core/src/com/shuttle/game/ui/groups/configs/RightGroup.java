package com.shuttle.game.ui.groups.configs;

import com.badlogic.gdx.utils.Align;
import com.shuttle.game.ui.groups.GroupType;

/**
 * @author Shuttle on 10/11/19.
 */
public class RightGroup extends AbstractInfoGroup {
    private static RightGroup instance = new RightGroup();

    public RightGroup() {
        this.setName(GroupType.RIGHT.name());

        for (int i = 0; i < 5; i++) {
            ConfigImageButton button = new ConfigImageButton(up);
            table.row().width(cellWidth).height(cellHeight);
            table.add(button);
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
        scrollPane.setPosition(893, 477);
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
