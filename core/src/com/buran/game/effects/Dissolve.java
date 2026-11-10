package com.buran.game.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.buran.game.utils.Assets;

/**
 * Draws sprites crumbling into sand (black-hole capture). Wrap a sprite draw with
 * {@link #begin} / {@link #end}; the amount goes from 0 (intact) to 1 (gone).
 */
public final class Dissolve {
    private static ShaderProgram shader;
    private static boolean failed;
    private static ShaderProgram previous;

    private Dissolve() {
    }

    private static ShaderProgram shader() {
        if (shader == null && !failed) {
            ShaderProgram.pedantic = false;
            shader = new ShaderProgram(Gdx.files.internal("shaders/dissolve.vert").readString(),
                    Gdx.files.internal("shaders/dissolve.frag").readString());
            if (!shader.isCompiled()) {
                Gdx.app.error("Dissolve", shader.getLog());
                failed = true;
                shader = null;
            }
        }
        return shader;
    }

    /** @return false if the effect is unavailable (draw normally). */
    public static boolean begin(SpriteBatch batch, float amount) {
        ShaderProgram s = shader();
        if (s == null || amount <= 0f) {
            return false;
        }
        previous = batch.getShader();
        batch.setShader(s);
        s.setUniformf("u_dissolve", Math.min(1f, amount));
        com.badlogic.gdx.graphics.Texture t = Assets.getInstance().getAtlas().getTextures().first();
        s.setUniformf("u_atlas", t.getWidth(), t.getHeight());
        return true;
    }

    public static void end(SpriteBatch batch) {
        batch.setShader(previous);
        previous = null;
    }
}
