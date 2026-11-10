package com.buran.game.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;

/**
 * Screen-space finishing for the game scene: bloom (bright pass, quarter-resolution
 * separable blur), black-hole lensing, colour grade, vignette, film grain, flashes and the
 * pause desaturation, all in one composite pass.
 */
public class PostProcessor implements Disposable {
    /** Per-frame inputs for the composite. */
    public static class Params {
        public boolean hole;
        public float holeX, holeY, holeRadius, holeSpin;
        public float aspect = 16f / 9f;
        public boolean grey;
        public float flash;
        public float flashR = 1f, flashG = 1f, flashB = 1f;
        public float tintR = 1f, tintG = 1f, tintB = 1f;
        public float bloom = 0.75f;
        public float threshold = 0.78f;
        public float vignette = 0.55f;
        public float grain = 0.022f;
    }

    private final ShaderProgram bright;
    private final ShaderProgram blur;
    private final ShaderProgram composite;
    private final Matrix4 ortho = new Matrix4();
    private FrameBuffer a, b;
    private TextureRegion aRegion, bRegion;
    private float time;
    private final boolean ok;

    public PostProcessor() {
        ShaderProgram.pedantic = false;
        String vert = Gdx.files.internal("shaders/space.vert").readString();
        bright = new ShaderProgram(vert, Gdx.files.internal("shaders/bright.frag").readString());
        blur = new ShaderProgram(vert, Gdx.files.internal("shaders/blur.frag").readString());
        composite = new ShaderProgram(vert, Gdx.files.internal("shaders/composite.frag").readString());
        boolean all = true;
        for (ShaderProgram p : new ShaderProgram[]{bright, blur, composite}) {
            if (!p.isCompiled()) {
                Gdx.app.error("PostProcessor", p.getLog());
                all = false;
            }
        }
        ok = all;
    }

    public boolean isAvailable() {
        return ok;
    }

    private void ensureTargets(int w, int h) {
        int bw = Math.max(1, w / 4);
        int bh = Math.max(1, h / 4);
        if (a != null && a.getWidth() == bw && a.getHeight() == bh) {
            return;
        }
        if (a != null) {
            a.dispose();
            b.dispose();
        }
        a = new FrameBuffer(Pixmap.Format.RGBA8888, bw, bh, false);
        b = new FrameBuffer(Pixmap.Format.RGBA8888, bw, bh, false);
        for (FrameBuffer f : new FrameBuffer[]{a, b}) {
            f.getColorBufferTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
        aRegion = flipped(a.getColorBufferTexture());
        bRegion = flipped(b.getColorBufferTexture());
    }

    private static TextureRegion flipped(Texture t) {
        TextureRegion r = new TextureRegion(t);
        r.flip(false, true);
        return r;
    }

    private void pass(SpriteBatch batch, FrameBuffer target, TextureRegion src, ShaderProgram shader) {
        target.begin();
        ortho.setToOrtho2D(0, 0, target.getWidth(), target.getHeight());
        batch.setProjectionMatrix(ortho);
        batch.setShader(shader);
        batch.disableBlending();
        batch.begin();
        if (shader == blur) {
            boolean horizontal = target == b;
            blur.setUniformf("u_dir", horizontal ? 1f / target.getWidth() : 0f, horizontal ? 0f : 1f / target.getHeight());
        } else if (shader == bright) {
            bright.setUniformf("u_threshold", currentThreshold);
        }
        batch.draw(src, 0, 0, target.getWidth(), target.getHeight());
        batch.end();
        batch.enableBlending();
        batch.setShader(null);
        target.end();
    }

    private float currentThreshold;

    /**
     * Builds the bloom texture from the scene. Call after the scene buffer is finished and
     * before {@link #draw}.
     */
    public void prepare(SpriteBatch batch, TextureRegion scene, int sceneW, int sceneH, Params p, float dt) {
        if (!ok) {
            return;
        }
        time += dt;
        ensureTargets(sceneW, sceneH);
        currentThreshold = p.threshold;
        pass(batch, a, scene, bright);
        pass(batch, b, aRegion, blur);   // horizontal into b
        pass(batch, a, bRegion, blur);   // vertical back into a
        pass(batch, b, aRegion, blur);   // second, wider round
        pass(batch, a, bRegion, blur);
    }

    /** Draws the finished scene with the batch's current projection (world/UI camera). */
    public void draw(SpriteBatch batch, TextureRegion scene, float x, float y, float w, float h,
                     int sceneW, int sceneH, Params p) {
        batch.setShader(composite);
        batch.begin();
        a.getColorBufferTexture().bind(1);
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
        composite.setUniformi("u_bloom", 1);
        composite.setUniformf("u_resolution", sceneW, sceneH);
        composite.setUniformf("u_time", time % 100f);
        composite.setUniformf("u_hole", p.hole ? 1f : 0f);
        composite.setUniformf("u_center", p.holeX, p.holeY);
        composite.setUniformf("u_radius", p.holeRadius);
        composite.setUniformf("u_spin", p.holeSpin);
        composite.setUniformf("u_aspect", p.aspect);
        composite.setUniformf("u_bloomStrength", p.bloom);
        composite.setUniformf("u_vignette", p.vignette);
        composite.setUniformf("u_grain", p.grain);
        composite.setUniformf("u_grey", p.grey ? 1f : 0f);
        composite.setUniformf("u_flash", p.flash);
        composite.setUniformf("u_flashColor", p.flashR, p.flashG, p.flashB);
        composite.setUniformf("u_tint", p.tintR, p.tintG, p.tintB);
        batch.disableBlending();
        batch.draw(scene, x, y, w, h);
        batch.end();
        batch.enableBlending();
        batch.setShader(null);
    }

    @Override
    public void dispose() {
        bright.dispose();
        blur.dispose();
        composite.dispose();
        if (a != null) {
            a.dispose();
            b.dispose();
        }
    }
}
