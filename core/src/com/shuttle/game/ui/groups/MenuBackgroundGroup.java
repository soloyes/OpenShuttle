package com.shuttle.game.ui.groups;

import com.badlogic.gdx.scenes.scene2d.Group;

public class MenuBackgroundGroup extends Group {
    private static MenuBackgroundGroup instance;


    public static MenuBackgroundGroup getInstance() {
        if (instance == null) {
            instance = new MenuBackgroundGroup();
        }
        return instance;
    }

    private MenuBackgroundGroup() {
        setName(GroupType.MENU_BACKGROUND.name());

    }
}
