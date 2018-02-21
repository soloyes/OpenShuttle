package ru.geekbrains.game.logic;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.engine.ui.Font;

/**
 * Created by sol on 2/21/18.
 * Class for store and manipulate with game state
 */

public class GameState {
    private Score score;
    private Lives lives;
    private State state;

    private Font font002;
    private Font font004;
    private StringBuilder sbScore;
    private StringBuilder sbGameOver;

    private Vector2 zeroVector = new Vector2(0.0f, 0.0f);

    public Lives getLives() { return lives; }

    public void setGameOver() {
        this.state = State.GAMEOVER;
    }

    public boolean isPlaying(){
        return state.equals(State.PLAY);
    }

    public Font getFont002() { return font002; }

    public Font getFont004() { return font004; }

    public StringBuilder getSbScore() { return sbScore; }

    public Score getScore() { return score; }

    public StringBuilder getSbGameOver() { return sbGameOver; }

    public void statePlayAgain(){
        state = State.PLAY;
    }

    public void initScore(){
        score.initScore();
    }

    public void initLives(){
        lives.initLives();
    }

    public Vector2 getZeroVector() { return zeroVector; }

    public GameState(TextureAtlas atlas) {
        score = new Score();
        lives = new Lives(atlas);

        font002 = new Font("fonts/font.fnt", "fonts/font.png");
        font004 = new Font("fonts/font.fnt", "fonts/font.png");

        sbScore = new StringBuilder();
        sbGameOver = new StringBuilder("GameOver");

        state = State.PLAY;
    }

    private enum State{
        PLAY, GAMEOVER;
    }
}
