package com.shuttle.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Results;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.BoomBox;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 8/2/18.
 */
public class Standing {
    private StringBuilder tmpString;
    private String username;
    private int score, scoreView;
    private Results results;
    private BitmapFont font64;
    private BitmapFont font128;
    private Shuttle player;
    @Getter
    @Setter
    private float fuel, maxFuel;
    @Getter
    @Setter
    private int hp, maxHp;
    @Getter
    @Setter
    private int lives;
    @Getter
    @Setter
    private int playingLevel;
    @Getter
    @Setter
    private boolean hpAlert;
    @Getter
    @Setter
    private boolean fuelAlertOutOfBounds;
    @Getter
    @Setter
    private boolean scoreAlertRadiationAnomaly;
    @Getter
    @Setter
    private boolean fuelAlertMagneticAnomaly;
    //Score to lives with formula scoreToLive*n*(28 - level)
    private int n;
    private BoomBox boomBox;

    public Standing(Shuttle player) {
        this.boomBox = new BoomBox(new GamePreferences());
        this.tmpString = new StringBuilder();
        this.username = Gdx.files.local("username").readString();
        this.results = new Results();
        this.font64 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT64_C, BitmapFont.class);
        this.font128 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT128, BitmapFont.class);
        this.player = player;
        this.n = 1;
        //Able to change colour for part of the text
        font64.getData().markupEnabled = true;
    }

    public void changeFuel(float amount) {
        fuel += amount;
        if (fuel <= 0.0f) {
            fuel = 0.0f;
        }
    }

    public void changeFuel(int amount) {
        fuel += amount;
        if (fuel > maxFuel) {
            fuel = maxFuel;
        }
    }

    public void changeHP(int amount) {
        hp += amount;
        if (hp > maxHp) {
            hp = maxHp;
        }
    }

    public void changeLives(int amount) {
        lives += amount;
        if (lives == 0) {
            Rules.GAME_OVER = true;
            writeResult();
        }
    }

    public void writeResult() {
        results.setResult(username + " " + scoreView + " " + new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date()));
    }

    public void changeScore(int amount) {
        score += amount;
        double tmp = Rules.SCORE_TO_LIVE * n * (28 - playingLevel);
        if (score - tmp >= 0) {
            //todo: to be Balanced
            n += 3;
            lives++;
            player.getGs().getGameInfoSystem().setup(
                    player.getPosition().x,
                    player.getPosition().y,
                    "[GREEN]1UP[]",
                    4.0f,
                    true
            );
            boomBox.playSound(MSConstants.UP);
        }
    }

    public void render(SpriteBatch batch) {
        tmpString.setLength(0);
        if (!Rules.GAME_OVER) {
            tmpString
                    .append("NAME: ")
                    .append(username);
            tmpString
                    .append("\nSCORE: ");
            if (scoreAlertRadiationAnomaly) {
                tmpString
                        .append("[RED]")
                        .append(scoreView)
                        .append("[]");
            } else {
                tmpString
                        .append(scoreView);
            }
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
            if (fuelAlertOutOfBounds || fuelAlertMagneticAnomaly) {
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
}
