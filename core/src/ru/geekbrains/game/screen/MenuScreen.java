package ru.geekbrains.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import java.util.Map;

import ru.geekbrains.game.star.StarsHandler;
import ru.geekbrains.game.ui.ExitButton;
import ru.geekbrains.game.ui.PlayButton;
import ru.geekbrains.stargame.engine.ActionListener;
import ru.geekbrains.stargame.engine.Base2DScreen;
import ru.geekbrains.stargame.engine.math.Rect;

public class MenuScreen extends Base2DScreen implements ActionListener {

    private ExitButton buttonExit;
    private PlayButton buttonPlay;
    private StarsHandler stars;
    private TextureAtlas mainAtlas;
    private Map<String, Object> gameMusic;
    private Music menuMusic;

    public MenuScreen(Game game, TextureAtlas atlas, Map<String, Object> gameMusic) {
        super(game);
        this.mainAtlas = atlas;
        this.gameMusic = gameMusic;
        this.menuMusic = (Music) gameMusic.get("menuScreen");
    }

    @Override
    public void show() {
        super.show();
        buttonExit = new ExitButton(mainAtlas, this);
        buttonPlay = new PlayButton(mainAtlas, this);
        stars = new StarsHandler(mainAtlas);
        menuMusic.setLooping(true);
        menuMusic.play();
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
        stars.draw(batch);
        buttonExit.draw(batch);
        buttonPlay.draw(batch);
        batch.end();
    }

    @Override
    protected void resize(Rect worldBounds) {
        stars.resize(worldBounds);
        buttonExit.resize(worldBounds);
        buttonPlay.resize(worldBounds);
    }

    @Override
    public void dispose() {
        super.dispose();
        buttonPlay.dispose();
        buttonExit.dispose();
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
            game.setScreen(new GameScreen(game, mainAtlas, gameMusic));
            menuMusic.stop();
        } else {
            throw new RuntimeException("Unknown src " + src);
        }
    }
}