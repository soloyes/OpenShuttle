package com.shuttle.game.player;

import com.badlogic.gdx.Gdx;
import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.Results;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.ui.GameInfoSystem;
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
    private String username;
    @Getter
    @Setter
    private int score, scoreView, scoreToLive;
    private Results results;
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
    @Getter
    @Setter
    private float laserChargeLevel, maxLaserChargeLevel;
    //Score to lives with formula scoreToLive*n*(28 - level)
    private int n;
    private BoomBox boomBox;
    //Type, level, score
    private Map<AbstractPoolable.Type, Map<Integer, Integer>> statistics;

    private static Standing instance;

    public static Standing getInstance() {
        if (instance == null) {
            instance = new Standing();
        }
        return instance;
    }

    private Standing() {
        this.boomBox = new BoomBox();
        this.username = Gdx.files.local("username").readString();
        this.results = new Results();
        this.n = 1;
        this.statistics = new HashMap<AbstractPoolable.Type, Map<Integer, Integer>>();
        //Able to change colour for part of the text
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

    public void changeLaserChargeLevel(float amount) {
        laserChargeLevel += amount;
        if (laserChargeLevel <= 0.0f) {
            laserChargeLevel = 0.0f;
        }
        if (laserChargeLevel > maxLaserChargeLevel) {
            laserChargeLevel = maxLaserChargeLevel;
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
            GameInfoSystem.setup(
                    Shuttle.position.x,
                    Shuttle.position.y,
                    "+[GREEN]" + GameInfoSystem.Preset.UP + "[]",
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
