package ru.geekbrains.game.ui.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import java.util.Map;

import ru.geekbrains.game.ui.star.StarsHandler;
import ru.geekbrains.stargame.engine.ActionListener;
import ru.geekbrains.stargame.engine.Base2DScreen;
import ru.geekbrains.stargame.engine.Rect;

public class MenuScreen extends Base2DScreen implements ActionListener {

    private MenuBar menuBar;
    private StarsHandler stars;
    private TextureAtlas mainAtlas;

    public MenuScreen(Game game, TextureAtlas atlas, Map<String, Object> music) {
        super(game);
        this.mainAtlas = atlas;

        gameMusic = music;
        backgroundMusic = (Music) gameMusic.get("menuScreen");

        stars = new StarsHandler(mainAtlas);
        menuBar = new MenuBar(atlas, gameMusic);
    }

    @Override
    public void show() {
        super.show();

        menuBar.show(this);
        menuBar.setScreen(this);

        backgroundMusic.setLooping(true);
        backgroundMusic.play();
     }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    private void update(float delta) {
        stars.update(delta);
    }

    private void draw() {
        batch.begin();
        stars.draw(batch);
        menuBar.draw(batch);
        batch.end();
    }

    @Override
    protected void resize(Rect worldBounds) {
        stars.resize(worldBounds);
        menuBar.resize(worldBounds);
    }

    @Override
    public void dispose() {
        super.dispose();
        stars.dispose();
        menuBar.dispose();
    }

    @Override
    protected void touchUp(Vector2 touch, int pointer) {
        menuBar.touchUp(touch, pointer);
    }

    @Override
    protected void touchDown(Vector2 touch, int pointer) {
        menuBar.touchDown(touch, pointer);
    }

    @Override
    protected void touchDragged(Vector2 touch, int pointer) {

    }

    @Override
    public void actionPerformed(Object src) {
        menuBar.MenuActionPerformed(src);
    }
}