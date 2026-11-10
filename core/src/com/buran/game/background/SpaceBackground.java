package com.buran.game.background;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;
import com.buran.game.conf.Rules;

/**
 * Procedural deep-space backdrop: a domain-warped nebula (half resolution, two parallax
 * layers) plus a three-layer star field. Replaces the stretched 1024 cloud tiles.
 * <p>
 * Call {@link #prepare(Camera, float)} outside any other frame buffer, then
 * {@link #render(SpriteBatch)} inside the world pass.
 */
public class SpaceBackground implements Disposable {
    /** A palette for one sector of space. */
    public static class Palette {
        final Color deep;
        final Color gasA;
        final Color gasB;
        final float intensity;

        public Palette(String deep, String gasA, String gasB, float intensity) {
            this.deep = Color.valueOf(deep);
            this.gasA = Color.valueOf(gasA);
            this.gasB = Color.valueOf(gasB);
            this.intensity = intensity;
        }
    }

    /** Default look: cold dark blue with a teal body and a faint rose accent. */
    public static final Palette DEFAULT = new Palette("04050c", "24507e", "7a3463", 1.0f);

    private final ShaderProgram nebulaShader;
    private final ShaderProgram starShader;
    private final Texture white;
    private final Matrix4 screen = new Matrix4();
    private FrameBuffer nebulaFbo;
    private TextureRegion nebulaRegion;
    private final float seed;
    private Palette palette = DEFAULT;
    private float time;
    private boolean ok;

    public SpaceBackground() {
        ShaderProgram.pedantic = false;
        String vert = Gdx.files.internal("shaders/space.vert").readString();
        nebulaShader = new ShaderProgram(vert, Gdx.files.internal("shaders/nebula.frag").readString());
        starShader = new ShaderProgram(vert, Gdx.files.internal("shaders/stars.frag").readString());
        if (!nebulaShader.isCompiled()) {
            Gdx.app.error("SpaceBackground", "nebula: " + nebulaShader.getLog());
        }
        if (!starShader.isCompiled()) {
            Gdx.app.error("SpaceBackground", "stars: " + starShader.getLog());
        }
        ok = nebulaShader.isCompiled() && starShader.isCompiled();

        Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        p.setColor(Color.WHITE);
        p.fill();
        white = new Texture(p);
        p.dispose();

        seed = MathUtils.random(0f, 100f);
    }

    public boolean isAvailable() {
        return ok;
    }

    public void setPalette(Palette palette) {
        this.palette = palette;
    }

    public void update(float dt) {
        time += dt;
    }

    /** Renders the nebula into its half-resolution buffer for this frame. */
    public void prepare(SpriteBatch batch, Camera camera) {
        if (!ok) {
            return;
        }
        int w = Math.max(1, Gdx.graphics.getWidth() / 2);
        int h = Math.max(1, Gdx.graphics.getHeight() / 2);
        if (nebulaFbo == null || nebulaFbo.getWidth() != w || nebulaFbo.getHeight() != h) {
            if (nebulaFbo != null) {
                nebulaFbo.dispose();
            }
            nebulaFbo = new FrameBuffer(Pixmap.Format.RGBA8888, w, h, false);
            nebulaFbo.getColorBufferTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            nebulaRegion = new TextureRegion(nebulaFbo.getColorBufferTexture());
            nebulaRegion.flip(false, true);
        }

        nebulaFbo.begin();
        screen.setToOrtho2D(0, 0, w, h);
        batch.setProjectionMatrix(screen);
        batch.setShader(nebulaShader);
        batch.begin();
        nebulaShader.setUniformf("u_resolution", w, h);
        nebulaShader.setUniformf("u_camera", camera.position.x, camera.position.y);
        nebulaShader.setUniformf("u_view", Rules.VIEW_WIDTH, Rules.VIEW_HEIGHT);
        nebulaShader.setUniformf("u_time", time);
        nebulaShader.setUniformf("u_seed", seed);
        nebulaShader.setUniformf("u_deep", palette.deep.r, palette.deep.g, palette.deep.b);
        nebulaShader.setUniformf("u_gasA", palette.gasA.r, palette.gasA.g, palette.gasA.b);
        nebulaShader.setUniformf("u_gasB", palette.gasB.r, palette.gasB.g, palette.gasB.b);
        nebulaShader.setUniformf("u_intensity", palette.intensity);
        batch.draw(white, 0, 0, w, h);
        batch.end();
        batch.setShader(null);
        nebulaFbo.end();
    }

    /** Draws the backdrop over the current view. The batch must be begun with the world camera. */
    public void render(SpriteBatch batch, Camera camera) {
        if (!ok || nebulaRegion == null) {
            return;
        }
        float vw = Rules.VIEW_WIDTH;
        float vh = Rules.VIEW_HEIGHT;
        float x = camera.position.x - vw / 2f;
        float y = camera.position.y - vh / 2f;

        Color old = batch.getColor().cpy();
        batch.setColor(Color.WHITE);
        batch.disableBlending();
        batch.draw(nebulaRegion, x, y, vw, vh);
        batch.enableBlending();

        batch.flush();
        batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE);
        com.badlogic.gdx.graphics.glutils.ShaderProgram previous = batch.getShader();
        batch.setShader(starShader);
        starShader.setUniformf("u_resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        starShader.setUniformf("u_camera", camera.position.x, camera.position.y);
        starShader.setUniformf("u_view", vw, vh);
        starShader.setUniformf("u_time", time);
        starShader.setUniformf("u_seed", seed);
        starShader.setUniformf("u_pixel", vh / Math.max(1, Gdx.graphics.getHeight()));
        batch.draw(white, x, y, vw, vh);
        batch.flush();
        batch.setShader(previous);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setColor(old);
    }

    @Override
    public void dispose() {
        nebulaShader.dispose();
        starShader.dispose();
        white.dispose();
        if (nebulaFbo != null) {
            nebulaFbo.dispose();
        }
    }
}
