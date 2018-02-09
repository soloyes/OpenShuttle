package ru.geekbrains.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.game.Background;

import ru.geekbrains.game.star.StarsHandler;
import ru.geekbrains.game.ui.ExitButton;
import ru.geekbrains.game.ui.PlayButton;
import ru.geekbrains.stargame.engine.ActionListener;
import ru.geekbrains.stargame.engine.Base2DScreen;
import ru.geekbrains.stargame.engine.math.Rect;

public class MenuScreen extends Base2DScreen implements ActionListener {

    private Background background;
    private ExitButton buttonExit;
    private PlayButton buttonPlay;
    private StarsHandler stars;
    private TextureAtlas mainAtlas;

    public MenuScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        mainAtlas = new TextureAtlas("mainAtlas.atlas");
        background = new Background(mainAtlas);
        buttonExit = new ExitButton(mainAtlas, this);
        buttonPlay = new PlayButton(mainAtlas, this);
        stars = new StarsHandler(mainAtlas);
     }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    public void update(float delta) {
        stars.update(delta);
    }

    public void draw() {
        batch.begin();
        background.draw(batch);
        stars.draw(batch);
        buttonExit.draw(batch);
        buttonPlay.draw(batch);
        batch.end();
    }

    @Override
    protected void resize(Rect worldBounds) {
        background.resize(worldBounds);
        stars.resize(worldBounds);
        buttonExit.resize(worldBounds);
        buttonPlay.resize(worldBounds);
    }

    @Override
    public void dispose() {
        super.dispose();
        background.dispose();
        buttonPlay.dispose();
        buttonExit.dispose();
        mainAtlas.dispose();
        stars.dispose();
    }

    @Override
    protected void touchUp(Vector2 touch, int pointer) {
        buttonExit.touchUp(touch, pointer);
        buttonPlay.touchUp(touch, pointer);
    }

    @Override
    protected void touchDown(Vector2 touch, int pointer) {
        buttonExit.touchDown(touch, pointer);
        buttonPlay.touchDown(touch, pointer);
    }

    @Override
    protected void touchDragged(Vector2 touch, int pointer) {

    }

    @Override
    public void actionPerformed(Object src) {
        if (src == buttonExit) {
            Gdx.app.exit();
        } else if (src == buttonPlay) {
            game.setScreen(new GameScreen(game));
        } else {
            throw new RuntimeException("Unknown src " + src);
        }
    }
}