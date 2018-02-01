package ru.geekbrains.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.engine.Base2DScreen;
import ru.geekbrains.stargame.engine.math.Rect;

public class MenuScreen extends Base2DScreen {

    private Texture backgroundTexture;
    private Texture playButtonTexture;
    private Texture exitButtonTexture;

    private Background background;
    private PlayButton playButton;
    private ExitButton exitButton;

    public MenuScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        backgroundTexture = new Texture("Grid.png");
        playButtonTexture = new Texture("play.png");
        exitButtonTexture = new Texture("exit.png");

        background = new Background(new TextureRegion(backgroundTexture));
        playButton = new PlayButton(new TextureRegion(playButtonTexture));
        exitButton = new ExitButton(new TextureRegion(exitButtonTexture));
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        batch.begin();
        background.draw(batch);
        playButton.draw(batch);
        exitButton.draw(batch);
        batch.end();
    }

    @Override
    protected void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        playButton.resize(worldBounds);
        exitButton.resize(worldBounds);
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        playButtonTexture.dispose();
        exitButtonTexture.dispose();
        super.dispose();
    }

    @Override
    protected void touchUp(Vector2 touch, int pointer) {
        super.touchUp(touch, pointer);
    }
}