package com.shuttle.game.ui.groups.configs;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.ui.groups.GroupType;
import com.shuttle.game.ui.groups.selector.Selector;
import com.shuttle.game.utils.Assets;

/**
 * @author soloyes on 10/11/19.
 */

public class LeftGroup extends AbstractInfoGroup {
    private static LeftGroup instance = new LeftGroup();
    private TextureRegion textureRegion;

    public static LeftGroup getInstance() {
        if (instance == null) {
            instance = new LeftGroup();
        }
        return instance;
    }

    public LeftGroup() {
        this.setName(GroupType.LEFT.name());
        this.textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_UI_GLOW_UPDOWN);
        this.up = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_UI_EMPTY_BOX));

        ConfigImageButton button = new ConfigImageButton(up, isLeft);
        group.addActor(new Line(button, true));
        buttonList.add(button);
        for (int i = 1; i < N; i++) {
            button = new ConfigImageButton(up, isLeft);
            group.addActor(new Line(button, false));
            buttonList.add(button);
        }

        this.addActor(scrollPane);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        //up
        if (Y > 0 && buttonList.size() > N) {
            batch.draw(textureRegion, scrollPane.getX() - 28, 2 * Rules.WORLD_HEIGHT / 3 - PaintConstants.UI_SETTINGS_HEIGHT / 2 + (N * cellHeight) / 2 - PaintConstants.LEFT_RIGHT_UI_GLOW_UPDOWN_HEIGHT + delimiter,
                    PaintConstants.LEFT_RIGHT_UI_GLOW_UPDOWN_WIDTH / 2, PaintConstants.LEFT_RIGHT_UI_GLOW_UPDOWN_HEIGHT / 2,
                    PaintConstants.LEFT_RIGHT_UI_GLOW_UPDOWN_WIDTH, PaintConstants.LEFT_RIGHT_UI_GLOW_UPDOWN_HEIGHT,
                    1.0f, 1.0f, 180
            );
        }
        //down
        if ((int) Y != buttonList.size() - N && buttonList.size() > N) {
            batch.draw(textureRegion, scrollPane.getX() - 28, 2 * Rules.WORLD_HEIGHT / 3 - PaintConstants.UI_SETTINGS_HEIGHT / 2 - (N * cellHeight) / 2 - PaintConstants.LEFT_RIGHT_UI_GLOW_UPDOWN_HEIGHT / 2);
        }
    }

    @Override
    public void setScrollBarPositions() {
        scrollPane.setScrollBarPositions(true, true);
    }

    @Override
    protected void setPosition() {
        this.isLeft = true;
        scrollPane.setPosition(Rules.WORLD_WIDTH / 2 - PaintConstants.UI_SETTINGS_WIDTH / 2 - scrollPane.getWidth() - 12, 2 * Rules.WORLD_HEIGHT / 3 - PaintConstants.UI_SETTINGS_HEIGHT / 2 - (N * cellHeight) / 2);
        group.align(Align.left);
    }

    public void setCenter(Selector selector) {
        if (selector == null) {
            for (int i = 0; i < buttonList.size(); i++) {
                ConfigImageButton b = buttonList.get(i);
                b.setCenter(false);
            }
            return;
        }
        ConfigImageButton button = buttonMap.get(selector.getSticker());
        if (button != null) {
            for (int i = 0; i < buttonList.size(); i++) {
                ConfigImageButton b = buttonList.get(i);
                b.setCenter(false);
            }
            button.setCenter(true);
        }
    }

    public void dispose() {
        instance = null;
    }

    public static void doStart() {
        instance = null;
    }
}