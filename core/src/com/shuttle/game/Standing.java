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

import lombok.Data;

/**
 * @author Shuttle on 8/2/18.
 */
@Data
public class Standing {
    private StringBuilder tmpString;
    private String username;
    private int score, scoreView;
    private Results results;
    private BitmapFont font64;
    private BitmapFont font128;
    private Shuttle player;
    private float fuel, maxFuel;
    private int hp, maxHp;
    private int lives;
    private int playingLevel;
    private boolean hpAlert;
    private boolean fuelAlert;
    //Score to lives with formula scoreToLive*n*(28 - level)
    private int n;

    public Standing(Shuttle player) {
        this.tmpString = new StringBuilder();
        this.username = Gdx.files.local("username").readString();
        this.results = new Results();
        this.font64 = Assets.getInstance().getAssetManager().get("fonts/gomarice64colored.ttf", BitmapFont.class);
        this.font128 = Assets.getInstance().getAssetManager().get("fonts/gomarice128.ttf", BitmapFont.class);
        this.player = player;
        this.n = 1;
        //Able to change color for part of the text
        font64.getData().markupEnabled = true;
    }

    public void decreaseFuel(float amount) {
        fuel -= amount;
    }

    public void decreaseHP(int amount) {
        hp -= amount;
    }

    public void decreaseLives(int amount) {
        lives -= amount;
    }

    public void increaseFuel(int amount) {
        fuel += amount;
        if (fuel > maxFuel) {
            fuel = maxFuel;
        }
    }

    public void increaseHP(int amount) {
        hp += amount;
        if (hp > maxHp) {
            hp = maxHp;
        }
    }

    public void increaseScore(int amount) {
        score += amount;
        double tmp = Rules.PLAYER_SCORE_TO_LIVE * n * (28 - playingLevel);
        System.out.println(tmp);
        if (score - tmp >= 0) {
            n++;
            lives++;
            player.getGs().getGameInfoSystem().setup(
                    player.getPosition().x,
                    player.getPosition().y,
                    "[GREEN]1UP[]",
                    4.0f,
                    true
            );
        }
    }

    public void render(SpriteBatch batch) {
        tmpString.setLength(0);
        if (!Rules.GAME_OVER) {
            tmpString
                    .append("NAME: ")
                    .append(username);
            tmpString
                    .append("\nSCORE: ")
                    .append(scoreView);
            tmpString
                    .append("\nHP: ");
            if (hpAlert) {
                tmpString
                        .append("[RED]")
                        .append(hp)
                        .append("[]");
            } else {
                tmpString.append(hp);
            }
            tmpString
                    .append("/")
                    .append(maxHp)
                    .append(" x")
                    .append(lives);

            tmpString
                    .append("\nFUEL: ");
            if (fuelAlert) {
                tmpString
                        .append("[RED]")
                        .append((int) fuel)
                        .append("[]");
            } else {
                tmpString.append((int) fuel);
            }
            tmpString
                    .append("/")
                    .append((int) maxFuel);
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
        scoreTicTac();
    }

    private void scoreTicTac() {
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
