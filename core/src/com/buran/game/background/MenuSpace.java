package com.buran.game.background;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import com.buran.game.conf.Rules;
import com.buran.game.screens.ScreenManager;

/**
 * Backdrop for menus and loading: the procedural nebula seen through a slowly drifting
 * camera, finished with a soft vignette. Same look as the game, without the HUD.
 */
public class MenuSpace implements Disposable {
    private final SpaceBackground space;
    private final OrthographicCamera camera;
    private final Texture vignette;
    private float time;

    public MenuSpace() {
        space = new SpaceBackground();
        camera = new OrthographicCamera(Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT);
        camera.position.set(MathUtils.random(0f, 20000f), MathUtils.random(0f, 20000f), 0f);
        vignette = createVignette();
    }

    private static Texture createVignette() {
        int s = 256;
        Pixmap p = new Pixmap(s, s, Pixmap.Format.RGBA8888);
        p.setBlending(Pixmap.Blending.None);
        for (int y = 0; y < s; y++) {
            for (int x = 0; x < s; x++) {
                float dx = (x + 0.5f) / s * 2f - 1f;
                float dy = (y + 0.5f) / s * 2f - 1f;
                float d = (float) Math.sqrt(dx * dx * 0.8f + dy * dy);
                float t = MathUtils.clamp((d - 0.55f) / 0.75f, 0f, 1f);
                float a = t * t * (3f - 2f * t) * 0.85f;
                p.setColor(0f, 0f, 0.01f, a);
                p.drawPixel(x, y);
            }
        }
        Texture t = new Texture(p);
        t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        p.dispose();
        return t;
    }

    public void update(float dt) {
        time += dt;
        space.update(dt);
        // A slow, slightly curving drift.
        camera.position.x += (38f + 10f * MathUtils.sin(time * 0.05f)) * dt;
        camera.position.y += (14f + 8f * MathUtils.cos(time * 0.04f)) * dt;
        camera.update();
    }

    /** Draws the backdrop. Call outside batch.begin()/end(). */
    public void render(SpriteBatch batch) {
        if (space.isAvailable()) {
            space.prepare(batch, camera, Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT);
            ScreenManager.getInstance().getViewport().apply();
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            space.render(batch, camera, Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT);
            batch.end();
        }
        batch.setProjectionMatrix(ScreenManager.getInstance().getCamera().combined);
        batch.begin();
        batch.draw(vignette, 0, 0, Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT);
        batch.end();
    }

    @Override
    public void dispose() {
        space.dispose();
        vignette.dispose();
    }
}
