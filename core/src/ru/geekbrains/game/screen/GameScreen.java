package ru.geekbrains.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import java.util.Map;

import ru.geekbrains.game.players.Alien;
import ru.geekbrains.game.players.Astronaut;
import ru.geekbrains.game.players.Player;
import ru.geekbrains.game.star.StarsHandler;
import ru.geekbrains.game.ui.Earth;
import ru.geekbrains.stargame.engine.Base2DScreen;
import ru.geekbrains.stargame.engine.math.Rect;

public class GameScreen extends Base2DScreen {

    private TextureAtlas mainAtlas;

    private Earth earth;
    private StarsHandler stars;

    private Player player;
    private Astronaut astronaut;
    private Alien alien;

    private Map<String, Object> music;
    private Music gameScreenMusic;

    public GameScreen(Game game, TextureAtlas atlas, Map<String, Object> music) {
        super(game);
        this.mainAtlas = atlas;
        this.music = music;
        this.gameScreenMusic = (Music) music.get("gameScreen");
    }

    @Override
    public void show() {
        super.show();
        stars = new StarsHandler(mainAtlas);
        player = new Player(mainAtlas, stars);
        astronaut = new Astronaut(mainAtlas, player, music);
        alien = new Alien(mainAtlas, player, music);
        earth = new Earth(mainAtlas);
        gameScreenMusic.setLooping(true);

        gameScreenMusic.play();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        update(delta);
        draw();
        batch.end();
    }

    private void update(float delta) {
        stars.update(delta);
        player.update(delta);
        earth.update(delta);
        astronaut.update(delta);
        alien.update(delta);
    }

    private void draw() {
        earth.draw(batch);
        stars.draw(batch);
        player.draw(batch);
        astronaut.draw(batch);
        alien.draw(batch);
    }

    @Override
    protected void resize(Rect worldBounds) {
        earth.resize(worldBounds);
        stars.resize(worldBounds);
        player.resize(worldBounds);
        astronaut.resize(worldBounds);
        alien.resize(worldBounds);
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
    protected void touchUp(Vector2 touch, int pointer) {}

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
        alien.dispose();
        stars.dispose();
    }
}