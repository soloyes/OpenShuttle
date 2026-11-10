package com.buran.game.ui.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.buran.game.conf.PaintConstants;
import com.buran.game.conf.Rules;
import com.buran.game.ui.groups.GroupType;
import com.buran.game.utils.Assets;

/**
 * The main-menu composition: the shuttle alone in its shield, drifting, and far behind it a
 * tumbling figure of a lost crew member. Replaces the portrait-era planet scene.
 */
public class MenuHero extends Group {
    private final TextureRegion buran;
    private final TextureRegion shield;
    private final TextureRegion astronaut;
    private float time;
    private final float baseX;
    private final float baseY;
    private float astronautX;
    private float astronautY;

    public MenuHero() {
        setName(GroupType.MENU_BACKGROUND.name());
        buran = Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_MENU_BACKGROUND_BURAN);
        shield = Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_MENU_BACKGROUND_SHIELD);
        astronaut = Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_MENU_BACKGROUND_ASTRONAUT);
        baseX = Rules.WORLD_WIDTH * 0.74f;
        baseY = Rules.WORLD_HEIGHT * 0.5f;
        astronautX = Rules.WORLD_WIDTH * 0.55f;
        astronautY = Rules.WORLD_HEIGHT * 0.78f;
    }

    private float fade = 1f;

    /** True while a menu panel (settings, results, info, rules) is open over the scene. */
    private boolean panelOpen() {
        if (getStage() == null) {
            return false;
        }
        for (com.badlogic.gdx.scenes.scene2d.Actor a : getStage().getActors()) {
            String n = a.getName();
            if (a.isVisible() && n != null && (n.equals(GroupType.CONFIG.name()) || n.equals(GroupType.RESULTS.name())
                    || n.equals(GroupType.RESULTS_DETAILED.name()) || n.equals(GroupType.INFO.name())
                    || n.equals(GroupType.RULES.name()))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        float target = panelOpen() ? 0.12f : 1f;
        fade += (target - fade) * Math.min(1f, delta * 6f);
        time += delta;
        astronautX += 14f * delta;
        astronautY -= 4f * delta;
        if (astronautX > Rules.WORLD_WIDTH + 200) {
            astronautX = Rules.WORLD_WIDTH * 0.45f;
            astronautY = Rules.WORLD_HEIGHT * 0.82f;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color old = batch.getColor().cpy();
        float a = parentAlpha * getColor().a * fade;

        // Distant, small and dim: someone out there.
        float as = 0.45f;
        batch.setColor(0.75f, 0.8f, 0.9f, 0.55f * a);
        batch.draw(astronaut,
                astronautX, astronautY,
                PaintConstants.UI_MENU_BACKGROUND_ASTRONAUT_WIDTH / 2f, PaintConstants.UI_MENU_BACKGROUND_ASTRONAUT_HEIGHT / 2f,
                PaintConstants.UI_MENU_BACKGROUND_ASTRONAUT_WIDTH, PaintConstants.UI_MENU_BACKGROUND_ASTRONAUT_HEIGHT,
                as, as, time * 9f);

        float scale = 2.1f;
        float bob = MathUtils.sin(time * 0.9f) * 22f;
        float tilt = MathUtils.sin(time * 0.45f) * 3f;
        float bw = PaintConstants.UI_MENU_BACKGROUND_BURAN_WIDTH;
        float bh = PaintConstants.UI_MENU_BACKGROUND_BURAN_HEIGHT;
        float sw = PaintConstants.UI_MENU_BACKGROUND_SHIELD_WIDTH;
        float sh = PaintConstants.UI_MENU_BACKGROUND_SHIELD_HEIGHT;
        float pulse = 1f + 0.025f * MathUtils.sin(time * 2.2f);

        batch.setColor(1f, 1f, 1f, 0.8f * a);
        batch.draw(shield, baseX - sw / 2f, baseY - sh / 2f + bob, sw / 2f, sh / 2f, sw, sh,
                scale * pulse, scale * pulse, tilt);
        batch.setColor(1f, 1f, 1f, a);
        batch.draw(buran, baseX - bw / 2f, baseY - bh / 2f + bob, bw / 2f, bh / 2f, bw, bh,
                scale, scale, tilt);
        batch.setColor(old);
    }
}
