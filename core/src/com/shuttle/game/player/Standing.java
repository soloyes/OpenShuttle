package com.shuttle.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Results;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.ui.GameInfoSystem;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.pool.AbstractPoolable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 8/2/18.
 */
public class Standing {
    private StringBuilder tmpString;
    private String username;
    @Getter
    private int score, scoreView;
    @Setter
    private int scoreToLive;
    private Results results;
    private BitmapFont font64;
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
    //Type, level, score
    private Map<AbstractPoolable.Type, Map<Integer, Integer>> statistics;

    public Standing(Shuttle player) {
        this.boomBox = new BoomBox();
        this.tmpString = new StringBuilder();
        this.username = Gdx.files.local("username").readString();
        this.results = new Results();
        this.font64 = Assets.getInstance().getAssetManager().get(PaintConstants.FONT64_C, BitmapFont.class);
        this.player = player;
        this.n = 1;
        this.statistics = new HashMap<AbstractPoolable.Type, Map<Integer, Integer>>();
        //Able to change colour for part of the text
        font64.getData().markupEnabled = true;
    }

    public void addToStatistics(AbstractPoolable.Type type, Integer level) {
        if (statistics.get(type) == null) {
            Map<Integer, Integer> map = new HashMap<Integer, Integer>();
            map.put(level, 1);
            statistics.put(type, map);

        } else {
            Map<Integer, Integer> map = statistics.get(type);
            if (statistics.get(type).get(level) == null) {
                map.put(level, 1);
            } else {
                map.put(level, statistics.get(type).get(level) + 1);
            }
        }
    }

    public void changeFuel(float amount) {
        fuel += amount;
        if (fuel <= 0.0f) {
            fuel = 0.0f;
        }
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
        results.setResult(
                username +
                        " " +
                        scoreView +
                        " "
                        + new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date()) +
                        " " +
                        Rules.StartLevel.values()[GamePreferences.getLevel()].name()
        );
    }

    public void changeScore(int amount) {
        score += amount;
        scoreToLive = Rules.SCORE_TO_LIVE * n * (28 - playingLevel);
        if (score - scoreToLive >= 0) {
            //todo: to be Balanced
            n += 3;
            lives++;
            player.getGs().getGameInfoSystem().setup(
                    player.getPosition().x,
                    player.getPosition().y,
                    "+[GREEN]" + GameInfoSystem.GameInfoSystemPreset.UP + "[]",
                    4.0f,
                    true
            );
            boomBox.playSound(MSConstants.UP);
            scoreToLive = Rules.SCORE_TO_LIVE * n * (28 - playingLevel);
        }
    }

    public Integer getFromStatistics(AbstractPoolable.Type type, Integer level) {
        if (statistics.get(type) == null) {
            return 0;
        }
        if (statistics.get(type).get(level) == null) {
            return 0;
        }
        return statistics.get(type).get(level);
    }

    public void render(SpriteBatch batch) {
        tmpString.setLength(0);

        tmpString
                .append("NAME: ")
                .append(username);
        tmpString
                .append("\nSCORE: ");
        if (scoreAlertRadiationAnomaly) {
            tmpString
                    .append("[RED]")
                    .append(scoreView)
                    .append("[]")
                    .append("/")
                    .append(scoreToLive);
        } else {
            tmpString
                    .append(scoreView)
                    .append("/")
                    .append(scoreToLive);
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
