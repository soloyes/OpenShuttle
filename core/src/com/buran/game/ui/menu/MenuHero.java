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
 * The main-menu composition: the shuttle drifting, reeling in a nearby astronaut with its tractor
 * beam, and far behind it a
 * tumbling figure of a lost crew member. Replaces the portrait-era planet scene.
 */
public class MenuHero extends Group {
    private final TextureRegion buran;
    private final TextureRegion astronaut;
    private float time;
    private final float baseX;
    private final float baseY;
    private float astronautX;
    private float astronautY;

    private final MainMenu menu;

    public MenuHero(MainMenu menu) {
        this.menu = menu;
        setName(GroupType.MENU_BACKGROUND.name());
        buran = Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_MENU_BACKGROUND_BURAN);
        astronaut = Assets.getInstance().getAtlas().findRegion(PaintConstants.UI_MENU_BACKGROUND_ASTRONAUT);
        // Landscape: right third. Portrait: between the title and the menu.
        baseX = Theme.portrait() ? Rules.WORLD_WIDTH * 0.55f : Rules.WORLD_WIDTH * 0.74f;
        baseY = Theme.portrait() ? Rules.WORLD_HEIGHT * 0.60f : Rules.WORLD_HEIGHT * 0.5f;
        astronautX = Rules.WORLD_WIDTH * 0.55f;
        astronautY = Rules.WORLD_HEIGHT * 0.78f;
    }

    private float fade = 1f;

    private boolean panelOpen() {
        return menu != null && menu.isPanelOpen();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        float target = panelOpen() ? (Theme.portrait() ? 0f : 0.12f) : 1f;
        fade += (target - fade) * Math.min(1f, delta * 6f);
        time += delta;
        astronautX += 14f * delta;
        astronautY -= 4f * delta;
        if (astronautX > Rules.WORLD_WIDTH + 200) {
            astronautX = Rules.WORLD_WIDTH * 0.45f;
            astronautY = Rules.WORLD_HEIGHT * 0.82f;
        }
    }

    private com.badlogic.gdx.graphics.Texture dot;

    private com.badlogic.gdx.graphics.Texture dot() {
        if (dot == null) {
            int n = 32;
            com.badlogic.gdx.graphics.Pixmap p = new com.badlogic.gdx.graphics.Pixmap(n, n, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
            p.setBlending(com.badlogic.gdx.graphics.Pixmap.Blending.None);
            for (int y = 0; y < n; y++) {
                for (int x = 0; x < n; x++) {
                    float dx = (x + 0.5f) / n * 2f - 1f;
                    float dy = (y + 0.5f) / n * 2f - 1f;
                    float d = MathUtils.clamp(1f - (float) Math.sqrt(dx * dx + dy * dy), 0f, 1f);
                    p.setColor(1f, 1f, 1f, d * d);
                    p.drawPixel(x, y);
                }
            }
            dot = new com.badlogic.gdx.graphics.Texture(p);
            dot.setFilter(com.badlogic.gdx.graphics.Texture.TextureFilter.Linear, com.badlogic.gdx.graphics.Texture.TextureFilter.Linear);
            p.dispose();
        }
        return dot;
    }

    /** Rescue cycle: a nearby astronaut is reeled in by the tractor beam, then taken aboard. */
    private static final float RESCUE_CYCLE = 10f;

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color old = batch.getColor().cpy();
        float a = parentAlpha * getColor().a * fade;
        float aw = PaintConstants.UI_MENU_BACKGROUND_ASTRONAUT_WIDTH;
        float ah = PaintConstants.UI_MENU_BACKGROUND_ASTRONAUT_HEIGHT;

        // Far away, small and dim: someone still out there.
        float as = 0.45f;
        batch.setColor(0.75f, 0.8f, 0.9f, 0.55f * a);
        batch.draw(astronaut, astronautX, astronautY, aw / 2f, ah / 2f, aw, ah, as, as, time * 9f);

        float scale = Theme.portrait() ? 1.45f : 2.1f;
        float bob = MathUtils.sin(time * 0.9f) * 22f;
        float tilt = MathUtils.sin(time * 0.45f) * 3f;
        float bw = PaintConstants.UI_MENU_BACKGROUND_BURAN_WIDTH;
        float bh = PaintConstants.UI_MENU_BACKGROUND_BURAN_HEIGHT;
        float k = scale / 2.1f;

        // Nearby astronaut drifting in along the beam (the nose points lower left).
        float c = time % RESCUE_CYCLE;
        float pull = MathUtils.clamp(c / (RESCUE_CYCLE - 1.5f), 0f, 1f);
        float ease = pull * pull * (3f - 2f * pull);
        // Emitter at the nose tip, clear of the hull; the astronaut starts far down-left.
        float emX = baseX - 215f * k;
        float emY = baseY - 160f * k + bob;
        float axp = emX + MathUtils.lerp(-760f, -70f, ease) * k;
        float ayp = emY + MathUtils.lerp(-420f, -40f, ease) * k + MathUtils.sin(time * 1.3f) * 10f * (1f - ease);
        float taken = MathUtils.clamp((c - (RESCUE_CYCLE - 1.5f)) / 0.6f, 0f, 1f);
        float appear = MathUtils.clamp(c / 1.2f, 0f, 1f);
        float astroAlpha = a * appear * (1f - taken);
        float astroScale = 0.75f * k * (1f - 0.35f * ease) * (1f - 0.6f * taken);

        if (astroAlpha > 0.01f) {
            // Beam: a soft cone of light with pulses running toward the ship.
            com.badlogic.gdx.graphics.Texture d = dot();
            batch.setBlendFunction(com.badlogic.gdx.graphics.GL20.GL_SRC_ALPHA, com.badlogic.gdx.graphics.GL20.GL_ONE);
            int steps = 28;
            for (int i = 0; i <= steps; i++) {
                float u = i / (float) steps;
                float x = MathUtils.lerp(emX, axp, u);
                float y = MathUtils.lerp(emY, ayp, u);
                float w = MathUtils.lerp(30f, 150f, u) * k;
                batch.setColor(0.45f, 0.9f, 1f, 0.13f * astroAlpha);
                batch.draw(d, x - w / 2f, y - w / 2f, w, w);
            }
            for (int pIdx = 0; pIdx < 4; pIdx++) {
                float u = 1f - ((time * 0.7f + pIdx * 0.25f) % 1f);
                float x = MathUtils.lerp(emX, axp, u);
                float y = MathUtils.lerp(emY, ayp, u);
                float w = MathUtils.lerp(24f, 90f, u) * k;
                batch.setColor(0.6f, 0.95f, 1f, 0.55f * astroAlpha * (1f - u * 0.5f));
                batch.draw(d, x - w / 2f, y - w / 2f, w, w);
            }
            batch.setColor(0.6f, 0.95f, 1f, 0.5f * astroAlpha);
            batch.draw(d, emX - 26f * k, emY - 26f * k, 52f * k, 52f * k);
            batch.setBlendFunction(com.badlogic.gdx.graphics.GL20.GL_SRC_ALPHA, com.badlogic.gdx.graphics.GL20.GL_ONE_MINUS_SRC_ALPHA);

            batch.setColor(1f, 1f, 1f, astroAlpha);
            batch.draw(astronaut, axp - aw / 2f, ayp - ah / 2f, aw / 2f, ah / 2f, aw, ah, astroScale, astroScale,
                    MathUtils.sin(time * 0.8f) * 25f);
        }

        // The shuttle itself, no shield in the menu.
        batch.setColor(1f, 1f, 1f, a);
        batch.draw(buran, baseX - bw / 2f, baseY - bh / 2f + bob, bw / 2f, bh / 2f, bw, bh, scale, scale, tilt);
        if (taken > 0f && taken < 1f) {
            // Soft flash at the hatch as the astronaut is taken aboard.
            batch.setBlendFunction(com.badlogic.gdx.graphics.GL20.GL_SRC_ALPHA, com.badlogic.gdx.graphics.GL20.GL_ONE);
            float fw = 160f * k * (0.5f + taken);
            batch.setColor(0.7f, 0.95f, 1f, 0.6f * (1f - taken) * a);
            batch.draw(dot(), emX - fw / 2f, emY - fw / 2f, fw, fw);
            batch.setBlendFunction(com.badlogic.gdx.graphics.GL20.GL_SRC_ALPHA, com.badlogic.gdx.graphics.GL20.GL_ONE_MINUS_SRC_ALPHA);
        }
        batch.setColor(old);
    }
}
