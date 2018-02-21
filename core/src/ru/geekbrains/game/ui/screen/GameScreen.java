package ru.geekbrains.game.ui.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.util.Map;

import ru.geekbrains.game.logic.GameState;
import ru.geekbrains.game.ui.players.Alien;
import ru.geekbrains.game.ui.players.Astronaut;
import ru.geekbrains.game.ui.players.Player;
import ru.geekbrains.game.ui.star.StarsHandler;
import ru.geekbrains.game.ui.space.Earth;
import ru.geekbrains.stargame.engine.ActionListener;
import ru.geekbrains.stargame.engine.Base2DScreen;
import ru.geekbrains.stargame.engine.Rect;

public class GameScreen extends Base2DScreen implements ActionListener{

    private TextureAtlas mainAtlas;

    private StarsHandler stars;
    private Earth earth;

    private Player player;
    private Astronaut astronaut;
    private Alien alien;

    private GameState gameState;
    private MenuBar menuBar;

    public GameScreen(Game game, TextureAtlas atlas, Map<String, Object> music) {
        super(game);
        this.mainAtlas = atlas;
        gameMusic = music;
        backgroundMusic = (Music) music.get("gameScreen");

        stars = new StarsHandler(mainAtlas);
        earth = new Earth(mainAtlas);

        player = new Player(mainAtlas, stars);
        astronaut = new Astronaut(mainAtlas, player, music);
        alien = new Alien(mainAtlas, player, music);

        gameState = new GameState(mainAtlas);
        menuBar = new MenuBar(atlas, music);
    }

    private void update(float delta) {
        stars.update(delta);
        earth.update(delta);
        astronaut.update(delta);
        alien.update(delta);

        if (gameState.getLives().hasLives()){
            player.update(delta);
        }
        else {
            gameState.setGameOver();
            alien.setHungry(false);
            astronaut.setLoud(false);
            alien.setLoud(false);
        }
    }

    private void draw() {
        earth.draw(batch);
        stars.draw(batch);
        astronaut.draw(batch);
        alien.draw(batch);

        if (gameState.isPlaying()){
            player.draw(batch);
            printInfo();
        }
        else {
            printGameOver();
        }
    }

    private void printInfo(){
        gameState.getSbScore().setLength(0);
        gameState.getFont002().draw(
                batch,
                gameState.getSbScore().append("Score: ").append(gameState.getScore().getScore()),
                worldBounds.getLeft(),
                worldBounds.getTop());
        gameState.getLives().draw(batch);
    }

    private void printGameOver(){
        gameState.getFont004().draw(
                batch,
                gameState.getSbGameOver(),
                0.0f,
                0.0f,
                Align.center);
        menuBar.draw(batch);

        if (backgroundMusic != gameMusic.get("menuScreen")) {
            changeMusic((Music) gameMusic.get("menuScreen"));
        }
    }

    public void playAgain(){
        gameState.statePlayAgain();
        gameState.initLives();
        gameState.initScore();

        player.setTarget(gameState.getZeroVector());

        alien.setLoud(true);
        alien.setHungry(true);
        alien.newItem(alien);

        astronaut.setLoud(true);
        astronaut.newItem(astronaut);

        changeMusic((Music) gameMusic.get("gameScreen"));
    }

    private void changeMusic(Music music){
        backgroundMusic.stop();
        backgroundMusic = music;
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }

    @Override
    public void show() {
        super.show();
        alien.setScore(gameState.getScore());
        alien.setAstronaut(astronaut);
        alien.setLives(gameState.getLives());

        player.setAstronaut(astronaut);
        player.setAlien(alien);
        player.setScore(gameState.getScore());
        player.setLives(gameState.getLives());

        gameState.getFont002().setWordSize(0.02f);
        gameState.getFont004().setWordSize(0.04f);

        backgroundMusic.setLooping(true);
        backgroundMusic.play();

        menuBar.show(this);
        menuBar.setScreen(this);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        update(delta);
        draw();
        batch.end();
    }

    @Override
    protected void resize(Rect worldBounds) {
        earth.resize(worldBounds);
        stars.resize(worldBounds);
        player.resize(worldBounds);
        astronaut.resize(worldBounds);
        alien.resize(worldBounds);
        gameState.getLives().resize(worldBounds);
        menuBar.resize(worldBounds);
    }

    @Override
    protected void touchDown(Vector2 touch, int pointer) {
        player.setTarget(touch);
        if (!gameState.isPlaying()){
            menuBar.touchDown(touch, pointer);
        }
    }

    @Override
    protected void touchDragged(Vector2 touch, int pointer) {
        player.setTarget(touch);
    }

    @Override
    protected void touchUp(Vector2 touch, int pointer) {
        if (!gameState.isPlaying()){
            menuBar.touchUp(touch, pointer);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        earth.dispose();
        player.dispose();
        astronaut.dispose();
        alien.dispose();
        stars.dispose();
        gameState.getLives().dispose();
        menuBar.dispose();
    }

    @Override
    public void actionPerformed(Object src) {
        menuBar.GameActionPerformed(src);
    }
}