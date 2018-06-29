package com.shuttle.game.screens.group_screen;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.shuttle.game.Assets;

/**
 * @author Shuttle on 6/23/18.
 */
public class MenuGroup extends Group {
    private BitmapFont font96;

    public MenuGroup() {
        this.setName(GroupType.MENU.name());
        this.setVisible(true);
        font96 = Assets.getInstance().getAssetManager().get("fonts/gomarice96.ttf", BitmapFont.class);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        font96.draw(batch, "Shuttle", 0, 1800, 1080, 1, false);
    }
}
