package com.shuttle.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.Assets;
import com.shuttle.game.ParticleEmitter;

/**
 * @author Shuttle on 6/28/18.
 */
public class TestScreen extends BaseScreen {
    private TextureRegion texture;
    private ShaderProgram shaderProgram;
    private ParticleEmitter particleEmitter;

    public TestScreen(SpriteBatch batch) {
        super(batch);
        shaderProgram = new ShaderProgram(Gdx.files.internal("shaders/default.vert"), Gdx.files.internal("shaders/default.frag"));

        //ShaderProgram.pedantic = false;
    }

    @Override
    public void createGUI() {
        //particleEmitter = new ParticleEmitter();
        this.texture = Assets.getInstance().getAtlas().findRegion("rocket");
        if (!shaderProgram.isCompiled()) {
            System.err.println(shaderProgram.getLog());
            System.exit(0);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        batch.setShader(shaderProgram);
        batch.begin();

        batch.draw(texture, 450, 450);
       // particleEmitter.render(batch);
        batch.end();
        batch.setShader(null);
        batch.begin();
        batch.draw(texture, 500, 500);
        batch.end();
    }

    private void update(float delta) {
//        particleEmitter.update(delta);
//        float dxy = 180;
//        particleEmitter.setup(
//                500,
//                500,
//                MathUtils.random(-dxy, dxy),
//                MathUtils.random(-dxy, dxy),
//                0.4f,
//                3.0f,
//                2.0f,
//                1f,
//                0f,
//                0f,
//                1,
//                0,
//                1,
//                0,
//                0.0f
//        );
    }

    @Override
    public void dispose() {
        super.dispose();
        shaderProgram.dispose();
    }
}
