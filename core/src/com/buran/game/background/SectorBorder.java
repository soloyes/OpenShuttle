package com.buran.game.background;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import com.buran.game.conf.Rules;
import com.buran.game.player.Buran;

/**
 * The edge of the playable sector: one continuous glowing outline (shader, distance to the
 * rectangle) with marching dashes, fading in as the Buran approaches. Leaving the sector
 * costs hull and fuel and eventually a life, so the edge must be seen before it hurts.
 */
public class SectorBorder implements Disposable {
    private final ShaderProgram shader;
    private final Texture white;
    private final Color color = new Color(1f, 0.38f, 0.26f, 1f);
    private float time;

    public SectorBorder() {
        ShaderProgram.pedantic = false;
        shader = new ShaderProgram(Gdx.files.internal("shaders/space.vert").readString(),
                Gdx.files.internal("shaders/border.frag").readString());
        if (!shader.isCompiled()) {
            Gdx.app.error("SectorBorder", shader.getLog());
        }
        Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        p.setColor(Color.WHITE);
        p.fill();
        white = new Texture(p);
        p.dispose();
    }

    public void update(float dt) {
        time += dt;
    }

    /** Draw inside the world pass (batch begun with the world camera). */
    public void render(SpriteBatch batch, Camera camera) {
        if (!shader.isCompiled()) {
            return;
        }
        float w = Rules.SPACE_WIDTH;
        float h = Rules.SPACE_HEIGHT;
        float px = Buran.position.x;
        float py = Buran.position.y;
        // Distance from the ship to the nearest edge (negative = outside).
        float edge = Math.min(Math.min(px, w - px), Math.min(py, h - py));
        float fadeStart = Math.min(Rules.VIEW_WIDTH, Rules.VIEW_HEIGHT) * 0.75f;
        float alpha = MathUtils.clamp(1f - edge / fadeStart, 0f, 1f);
        if (alpha <= 0f) {
            return;
        }
        alpha *= 0.85f + 0.15f * MathUtils.sin(time * 5f);

        float vw = Rules.VIEW_WIDTH;
        float vh = Rules.VIEW_HEIGHT;
        batch.flush();
        com.badlogic.gdx.graphics.glutils.ShaderProgram previous = batch.getShader();
        batch.setShader(shader);
        batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE);
        shader.setUniformf("u_resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shader.setUniformf("u_camera", camera.position.x, camera.position.y);
        shader.setUniformf("u_view", vw, vh);
        shader.setUniformf("u_size", w, h);
        shader.setUniformf("u_time", time);
        shader.setUniformf("u_alpha", alpha);
        shader.setUniformf("u_pixel", vh / Math.max(1, Gdx.graphics.getHeight()));
        shader.setUniformf("u_color", color.r, color.g, color.b);
        batch.draw(white, camera.position.x - vw / 2f, camera.position.y - vh / 2f, vw, vh);
        batch.flush();
        batch.setShader(previous);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void dispose() {
        shader.dispose();
        white.dispose();
    }
}
