package com.shuttle.game.ui.groups.configs;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.ui.groups.GroupType;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 10/11/19.
 */
public class LeftGroup extends AbstractInfoGroup {
    private static LeftGroup instance = new LeftGroup();
    private TextureRegion textureRegion;

    public LeftGroup() {
        this.setName(GroupType.LEFT.name());
        this.textureRegion = Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_UI_GLOW_UPDOWN);
        this.up = new TextureRegionDrawable(Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_UI_EMPTY_BOX));

        ConfigImageButton button = new ConfigImageButton(up, true);
        group.addActor(new Line(button, true));
        buttonList.add(button);
        for (int i = 1; i < N+5; i++) {
            button = new ConfigImageButton(up, true);
            group.addActor(new Line(button, false));
            buttonList.add(button);
        }

        this.addActor(scrollPane);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (x != buttonList.size() - N && buttonList.size() > N) {
            batch.draw(textureRegion, -2, 430);
        }
        if (x != 0 && buttonList.size() > N) {
            batch.draw(textureRegion, -2, 1112,
                    PaintConstants.LEFT_RIGHT_UI_GLOW_UPDOWN_WIDTH / 2, PaintConstants.LEFT_RIGHT_UI_GLOW_UPDOWN_HEIGHT / 2,
                    PaintConstants.LEFT_RIGHT_UI_GLOW_UPDOWN_WIDTH, PaintConstants.LEFT_RIGHT_UI_GLOW_UPDOWN_HEIGHT,
                    1.0f, 1.0f, 180
            );
        }
    }

    @Override
    public void setScrollBarPositions() {
        scrollPane.setScrollBarPositions(true, true);
    }

    @Override
    protected void setPosition() {
        scrollPane.setPosition(25, 477);
        group.align(Align.left);
    }

    public void dispose() {
        instance = null;
    }

    public static LeftGroup getInstance() {
        if (instance == null) {
            instance = new LeftGroup();
        }
        return instance;
    }
}
