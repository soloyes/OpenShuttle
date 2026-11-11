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
        if (life != null) {
            life.update(dt);
        }
        // A slow, slightly curving drift.
        camera.position.x += (38f + 10f * MathUtils.sin(time * 0.05f)) * dt;
        camera.position.y += (14f + 8f * MathUtils.cos(time * 0.04f)) * dt;
        camera.update();
    }

    /** Draws the backdrop. Call outside batch.begin()/end(). */
    /** Painted key art behind the menu (assets/ui/menu.jpg); the procedural sky if it is missing. */
    private com.badlogic.gdx.graphics.Texture art;
    private boolean artTried;
    private MenuArtLife life;

    private com.badlogic.gdx.graphics.Texture art() {
        if (!artTried) {
            artTried = true;
            com.badlogic.gdx.files.FileHandle f = com.badlogic.gdx.Gdx.files.internal("ui/menu.jpg");
            if (f.exists()) {
                art = new com.badlogic.gdx.graphics.Texture(f);
                art.setFilter(com.badlogic.gdx.graphics.Texture.TextureFilter.Linear,
                        com.badlogic.gdx.graphics.Texture.TextureFilter.Linear);
                life = new MenuArtLife();
            }
        }
        return art;
    }

    /** The key art, covering the screen, drifting very slowly (a held breath, not a pan). */
    private void drawArt(SpriteBatch batch, com.badlogic.gdx.graphics.Texture t) {
        float W = Rules.WORLD_WIDTH;
        float H = Rules.WORLD_HEIGHT;
        float zoom = 1.06f + 0.02f * MathUtils.sin(time * 0.05f);
        float scale = Math.max(W / t.getWidth(), H / t.getHeight()) * zoom;
        float w = t.getWidth() * scale;
        float h = t.getHeight() * scale;
        float x = (W - w) / 2f + (w - W) * 0.35f * MathUtils.sin(time * 0.03f);
        float y = (H - h) / 2f + (h - H) * 0.3f * MathUtils.cos(time * 0.04f);
        batch.setColor(1f, 1f, 1f, 1f);
        batch.draw(t, x, y, w, h);
        if (life != null) {
            life.draw(batch, x, y, w, h);
        }
        // The menu sits on the left: let that side sink into the dark.
        int bands = 24;
        float span = W * 0.5f;
        for (int i = 0; i < bands; i++) {
            float k = 1f - i / (float) bands;
            com.buran.game.ui.menu.Theme.rect(batch, span * i / bands, 0, span / bands + 1f, H,
                    com.badlogic.gdx.graphics.Color.BLACK, 0.55f * k * k);
        }
    }

    public void render(SpriteBatch batch) {
        com.badlogic.gdx.graphics.Texture t = art();
        if (t != null) {
            ScreenManager.getInstance().getViewport().apply();
            batch.setProjectionMatrix(ScreenManager.getInstance().getCamera().combined);
            batch.begin();
            drawArt(batch, t);
            batch.draw(vignette, 0, 0, Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT);
            batch.end();
            return;
        }
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
        if (art != null) {
            art.dispose();
        }
        if (life != null) {
            life.dispose();
        }
    }
}
