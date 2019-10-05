package com.shuttle.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.shuttle.game.conf.GamePreferences;

/**
 * @author Shuttle on 10/8/18.
 */
public interface ShaderManager {
    void dispose();

    ShaderProgram getProgram();

    void load();

    ShaderManager PAUSE = new ShaderManager() {
        private ShaderProgram shaderPause;

        @Override
        public ShaderProgram getProgram() {
            if (shaderPause.isCompiled()) {
                return shaderPause;
            } else {
                GamePreferences.setShaderEnabled(false);
                return null;
            }
        }

        @Override
        public void load() {
            shaderPause = new ShaderProgram(
                    Gdx.files.internal("shaders/grey_screen.vert").readString(),
                    Gdx.files.internal("shaders/grey_screen.frag").readString()
            );
            ShaderProgram.pedantic = false;
        }

        @Override
        public void dispose() {
            if (shaderPause.isCompiled()) {
                shaderPause.dispose();
            }
        }
    };

    ShaderManager BLACKHOLE = new ShaderManager() {
        private ShaderProgram shaderBlackHole;

        @Override
        public ShaderProgram getProgram() {
            if (shaderBlackHole.isCompiled()) {
                return shaderBlackHole;
            } else {
                GamePreferences.setShaderEnabled(false);
                return null;
            }
        }

        @Override
        public void load() {
            shaderBlackHole = new ShaderProgram(
                    Gdx.files.internal("shaders/black_hole.vert").readString(),
                    Gdx.files.internal("shaders/black_hole.frag").readString()
            );
            ShaderProgram.pedantic = false;
        }

        @Override
        public void dispose() {
            if (shaderBlackHole.isCompiled()) {
                shaderBlackHole.dispose();
            }
        }
    };
}
