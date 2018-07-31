package com.shuttle.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

/**
 * @author Shuttle on 6/28/18.
 */
public class TestScreen extends BaseScreen {

    Texture texture;
    ShapeRenderer shapeRenderer;
    private ShaderProgram shaderProgram;

    public TestScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void createGUI() {
        texture = new Texture(Gdx.files.local("pics/game.png"));

        shaderProgram = new ShaderProgram(
                Gdx.files.internal("shaders/default1.vert"),
                Gdx.files.internal("shaders/default1.frag")
        );

        if (!shaderProgram.isCompiled()) {
            System.err.println(shaderProgram.getLog());
            System.exit(0);
        }

        shapeRenderer = new ShapeRenderer(5000, shaderProgram);
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.setProjectionMatrix(ScreenManager.getInstance().getViewport().getCamera().combined);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(50, 50,50);
        shapeRenderer.end();
        batch.begin();
        batch.draw(texture, 0 ,0);
        batch.end();
    }

    private void update(float delta) {
    }
}
