package com.buran.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.buran.game.ui.groups.mvc.profiles.Profiles;

/**
 * @author soloyes on 10/8/18.
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
                Profiles.getSettings().setShader(false);
                return null;
            }
        }

        @Override
        public void load() {
            ShaderProgram.pedantic = false;
            shaderPause = new ShaderProgram(
                    Gdx.files.internal("shaders/grey_screen.vert").readString(),
                    Gdx.files.internal("shaders/grey_screen.frag").readString()
            );
            if (!shaderPause.isCompiled()) {
                Gdx.app.error("ShaderManager", "shaderPause failed to compile: " + shaderPause.getLog());
            }

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
                Profiles.getSettings().setShader(false);
                return null;
            }
        }

        @Override
        public void load() {
            ShaderProgram.pedantic = false;
            shaderBlackHole = new ShaderProgram(
                    Gdx.files.internal("shaders/black_hole.vert").readString(),
                    Gdx.files.internal("shaders/black_hole.frag").readString()
            );
            if (!shaderBlackHole.isCompiled()) {
                Gdx.app.error("ShaderManager", "shaderBlackHole failed to compile: " + shaderBlackHole.getLog());
            }

        }

        @Override
        public void dispose() {
            if (shaderBlackHole.isCompiled()) {
                shaderBlackHole.dispose();
            }
        }
    };
}