package com.shuttle.game.ui.panels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.utils.Assets;

/**
 * @author Shuttle on 3/1/19.
 */
public class UIBonusPanel extends UIAbstractPanel {
    private int width, height, bWidth, bHeight;
    private float startPoint;
    private TextureRegion circleTexture[];
    private TextureRegion astronautBonus;
    private TextureRegion[] bonusRegions;

    public UIBonusPanel() {
        this.width = PaintConstants.LEFT_RIGHT_PANEL_CIRCLE_WIDTH;
        this.height = PaintConstants.LEFT_RIGHT_PANEL_CIRCLE_HEIGHT;
        this.bWidth = PaintConstants.BONUS_ICON_WIDTH;
        this.bHeight = PaintConstants.BONUS_ICON_HEIGHT;
        this.startPoint = Rules.WORLD_HEIGHT / 2;
        this.circleTexture = new TextureRegion[2];
        this.circleTexture[0] = Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_PANEL_FULL_CIRCLE);
        this.circleTexture[1] = Assets.getInstance().getAtlas().findRegion(PaintConstants.LEFT_RIGHT_PANEL_EMPTY_CIRCLE);
        this.astronautBonus = Assets.getInstance().getAtlas().findRegion(PaintConstants.ASTRONAUT_BONUS_ICON);
        this.bonusRegions = new TextureRegion[4];
        this.bonusRegions[0] = Assets.getInstance().getAtlas().findRegion(PaintConstants.MEDIC_BONUS_ICON);
        this.bonusRegions[1] = Assets.getInstance().getAtlas().findRegion(PaintConstants.SCIENTIST_BONUS_ICON);
        this.bonusRegions[2] = Assets.getInstance().getAtlas().findRegion(PaintConstants.MECHANIC_BONUS_ICON);
        this.bonusRegions[3] = Assets.getInstance().getAtlas().findRegion(PaintConstants.MILITARY_BONUS_ICON);
    }

    public void render(SpriteBatch batch) {
        Color tmpColor = batch.getColor();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < circleTexture.length; j++) {
                batch.draw(circleTexture[j],
                        Rules.WORLD_WIDTH - width - 32, startPoint - (22 + height) * i,
                        width / 2, height / 2,
                        width, height,
                        1.0f, 1.0f,
                        0.0f
                );
            }
            batch.draw(astronautBonus,
                    Rules.WORLD_WIDTH - width - 32, startPoint - (22 + bHeight) * i,
                    bWidth / 2, bHeight / 2,
                    bWidth, bHeight,
                    1.0f, 1.0f,
                    0.0f
            );
            batch.draw(bonusRegions[i],
                    Rules.WORLD_WIDTH - width - 32, startPoint - (22 + bHeight) * i,
                    bWidth / 2, bHeight / 2,
                    bWidth, bHeight,
                    1.0f, 1.0f,
                    0.0f
            );
        }
        batch.setColor(tmpColor);
    }

    public void update(float dt) {

    }
}