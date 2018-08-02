package com.shuttle.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shuttle.game.conf.Results;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.utils.Assets;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Shuttle on 8/2/18.
 */
public class Score {
    private StringBuilder tmpString;
    private String username;
    private int score, scoreView;
    private Results results;
    private BitmapFont font64;
    private BitmapFont font128;
    private Shuttle player;

    public Score(Shuttle player) {
        this.tmpString = new StringBuilder();
        this.username = Gdx.files.local("username").readString();
        this.results = new Results();
        this.font64 = Assets.getInstance().getAssetManager().get("fonts/gomarice64.ttf", BitmapFont.class);
        this.font128 = Assets.getInstance().getAssetManager().get("fonts/gomarice128.ttf", BitmapFont.class);
        this.player = player;
    }

    public void addScore(int amount) {
        score += amount;
    }

    public void render(SpriteBatch batch) {
        tmpString.setLength(0);
        if (!Rules.GAME_OVER) {
            tmpString.append("NAME: ").append(username);
            tmpString.append("\nSCORE: ").append(scoreView);
            tmpString
                    .append("\nHP: ")
                    .append(player.getHp())
                    .append("/")
                    .append(player.getMaxHp())
                    .append(" x")
                    .append(player.getLives());
            tmpString
                    .append("\nFUEL: ")
                    .append((int) player.getFuel())
                    .append("/")
                    .append((int) player.getMaxFuel());
            // tmpString.append("\nSPEED: ").append(velocity.len());
            font64.draw(batch, tmpString, 20, 1900);
        } else {
            tmpString.append("Game Over!");
            tmpString.append("\nSCORE: ").append(scoreView);
            font128.draw(
                    batch,
                    tmpString,
                    ScreenManager.getInstance().getViewport().getWorldWidth() - font128.getRegion().getRegionWidth(),
                    ScreenManager.getInstance().getViewport().getWorldHeight() / 2
            );
        }
    }

    public void update() {
        checkScore();
    }

    private void checkScore() {
        if (scoreView < score) {
            scoreView += 500;
            if (scoreView > score) {
                scoreView = score;
            }
        }
        if (scoreView > score) {
            scoreView -= 500;
            if (scoreView < score) {
                scoreView = score;
            }
        }
    }

    public void writeResult() {
        results.setResult(username + " " + scoreView + " " + new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date()));
    }
}
