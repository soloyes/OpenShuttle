package ru.geekbrains.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import java.util.Map;

import ru.geekbrains.game.players.Astronaut;
import ru.geekbrains.game.players.Player;
import ru.geekbrains.game.star.StarsHandler;
import ru.geekbrains.game.ui.Earth;
import ru.geekbrains.stargame.engine.Base2DScreen;
import ru.geekbrains.stargame.engine.math.Rect;

public class GameScreen extends Base2DScreen {

    private Earth earth;
    private StarsHandler stars;
    private TextureAtlas mainAtlas;
    private Player player;
    private Astronaut astronaut;
    private Map<String, Music> gameMusic;

    public GameScreen(Game game, TextureAtlas atlas, Map<String, Music> gameMusic) {
        super(game);
        this.mainAtlas = atlas;
        this.gameMusic = gameMusic;
    }

    @Override
    public void show() {
        super.show();
        stars = new StarsHandler(mainAtlas);
        player = new Player(mainAtlas, stars);
        astronaut = new Astronaut(mainAtlas);
        earth = new Earth(mainAtlas);
        gameMusic.get("gameScreen").setLooping(true);
        gameMusic.get("gameScreen").play();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        update(delta);
        draw();
        batch.end();
    }

    public void update(float delta) {
        stars.update(delta);
        player.update(delta);
        astronaut.update(delta);
        earth.update(delta);
    }

    public void draw() {
        earth.draw(batch);
        stars.draw(batch);
        player.draw(batch);
        astronaut.draw(batch);
    }

    @Override
    protected void resize(Rect worldBounds) {
        earth.resize(worldBounds);
        stars.resize(worldBounds);
        player.resize(worldBounds);
        astronaut.resize(worldBounds);
    }

    @Override
    protected void touchDown(Vector2 touch, int pointer) {
        player.setTarget(touch);
    }

    @Override
    protected void touchDragged(Vector2 touch, int pointer) {
        player.setTarget(touch);
    }

    @Override
    protected void touchUp(Vector2 touch, int pointer) {

    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public void dispose() {
        super.dispose();
        earth.dispose();
        player.dispose();
        astronaut.dispose();
        stars.dispose();
    }
}