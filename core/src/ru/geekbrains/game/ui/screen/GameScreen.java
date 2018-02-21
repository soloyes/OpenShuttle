package ru.geekbrains.game.ui.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.util.Map;

import ru.geekbrains.game.Logic.Lives;
import ru.geekbrains.game.Logic.Score;
import ru.geekbrains.game.ui.players.Alien;
import ru.geekbrains.game.ui.players.Astronaut;
import ru.geekbrains.game.ui.players.Player;
import ru.geekbrains.game.ui.star.StarsHandler;
import ru.geekbrains.game.ui.space.Earth;
import ru.geekbrains.stargame.engine.ActionListener;
import ru.geekbrains.stargame.engine.Base2DScreen;
import ru.geekbrains.stargame.engine.ui.Font;
import ru.geekbrains.stargame.engine.Rect;

public class GameScreen extends Base2DScreen implements ActionListener{

    private TextureAtlas mainAtlas;

    private StarsHandler stars;
    private Earth earth;

    private Player player;
    private Astronaut astronaut;
    private Alien alien;

    private Score score;
    private Lives lives;
    private GameState gameState;

    private Font font002;
    private Font font004;
    private StringBuilder sbScore;
    private StringBuilder sbGameOver;

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

        menuBar = new MenuBar(atlas, music);

        score = new Score();
        lives = new Lives(mainAtlas);

        font002 = new Font("fonts/font.fnt", "fonts/font.png");
        font004 = new Font("fonts/font.fnt", "fonts/font.png");

        sbScore = new StringBuilder();
        sbGameOver = new StringBuilder("GameOver");

        gameState = GameState.PLAY;
    }

    private void update(float delta) {
        stars.update(delta);
        earth.update(delta);
        astronaut.update(delta);
        alien.update(delta);

        if ( lives.getLives() != 0){
            player.update(delta);
        }
        else if (lives.getLives() == 0) {
            gameState = GameState.GAMEOVER;
            alien.setHungry(false);
        }
    }

    private void draw() {
        earth.draw(batch);
        stars.draw(batch);
        astronaut.draw(batch);
        alien.draw(batch);

        if (gameState.equals(GameState.PLAY)){
            player.draw(batch);
            printInfo();
        }
        else {
            GameOver();
        }
    }

    private void printInfo(){
        sbScore.setLength(0);
        if (score.getScore() < 0) {
            score.setScore(0);
        }
        font002.draw(batch, sbScore.append("Score: ").append(score.getScore()), worldBounds.getLeft(),worldBounds.getTop());
        lives.draw(batch);
    }

    private void GameOver(){
        font004.draw(batch, sbGameOver, 0.0f, 0.0f, Align.center);
        menuBar.draw(batch);

        if (backgroundMusic != gameMusic.get("menuScreen")) {
            backgroundMusic.stop();
            backgroundMusic = (Music) gameMusic.get("menuScreen");
            backgroundMusic.setLooping(true);
            backgroundMusic.play();
        }
    }

    public void playAgain(){
        gameState = GameState.PLAY;
        lives.initLives();
        score.initScore();

        alien.setHungry(true);
        alien.newItem(alien);

        astronaut.newItem(astronaut);

        backgroundMusic.stop();
        backgroundMusic = (Music) gameMusic.get("gameScreen");
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }

    @Override
    public void show() {
        super.show();
        alien.setScore(score);
        alien.setAstronaut(astronaut);
        alien.setLives(lives);

        player.setAstronaut(astronaut);
        player.setAlien(alien);
        player.setScore(score);
        player.setLives(lives);

        font002.setWordSize(0.02f);
        font004.setWordSize(0.04f);

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
        lives.resize(worldBounds);
        menuBar.resize(worldBounds);
    }

    @Override
    protected void touchDown(Vector2 touch, int pointer) {
        player.setTarget(touch);
        if (gameState == GameState.GAMEOVER){
            menuBar.touchDown(touch, pointer);
        }
    }

    @Override
    protected void touchDragged(Vector2 touch, int pointer) {
        player.setTarget(touch);
    }

    @Override
    protected void touchUp(Vector2 touch, int pointer) {
        if (gameState == GameState.GAMEOVER){
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
        lives.dispose();
        menuBar.dispose();
    }

    @Override
    public void actionPerformed(Object src) {
        menuBar.GameActionPerformed(src);
    }

    private enum GameState{
        PLAY, GAMEOVER;
    }
}