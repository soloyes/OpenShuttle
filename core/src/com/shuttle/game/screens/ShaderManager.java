package com.shuttle.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.shuttle.game.conf.GamePreferences;

/**
 * @author Shuttle on 10/8/18.
 */
public class ShaderManager {
    private ShaderProgram shaderBlackHole;
    private ShaderProgram shaderPause;

    public ShaderManager() {
        shaderBlackHole = new ShaderProgram(
                Gdx.files.internal("shaders/black_hole.vert").readString(),
                Gdx.files.internal("shaders/black_hole.frag").readString()
        );

        shaderPause = new ShaderProgram(
                Gdx.files.internal("shaders/grey_screen.vert").readString(),
                Gdx.files.internal("shaders/grey_screen.frag").readString()
        );
        ShaderProgram.pedantic = false;
    }

    public void dispose() {
        if (shaderPause.isCompiled()) {
            shaderPause.dispose();
        }
        if (shaderBlackHole.isCompiled()) {
            shaderBlackHole.dispose();
        }
    }

    public ShaderProgram getProgram(ShaderType type) {
        switch (type) {
            case PAUSE:
                if (shaderPause.isCompiled()) {
                    return shaderPause;
                } else {
                    GamePreferences.setEffectsLevel(0);
                    return null;
                }
            case BLACKHOLE:
                if (shaderBlackHole.isCompiled()) {
                    return shaderBlackHole;
                } else {
                    GamePreferences.setEffectsLevel(0);
                    return null;
                }
        }
        return null;
    }

    public enum ShaderType {
        PAUSE, BLACKHOLE
    }
}

