package com.shuttle.game.player;

import com.shuttle.game.conf.GamePreferences;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.conf.VConstants;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.effects.Shake;
import com.shuttle.game.effects.Vibrator;
import com.shuttle.game.labelsticker.Sticker;
import com.shuttle.game.ui.GameInfoSystem;
import com.shuttle.game.ui.groups.mvc.profiles.Profiles;
import com.shuttle.game.ui.groups.mvc.results.DetailedResultDAO;
import com.shuttle.game.utils.Rounder;
import com.shuttle.game.utils.pool.AbstractPoolable;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Shuttle on 8/2/18.
 */
public final class Standing {
    private static Standing instance;
    @Getter
    @Setter
    private long score, scoreView, scoreToLive;
    @Getter
    @Setter
    private float fuel, maxFuel;
    @Getter
    @Setter
    private int hp, maxHp;
    @Getter
    @Setter
    private float ramPower;
    @Getter
    @Setter
    private float ramMaxPower;
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
    private Map<Sticker, Integer> astronauts;
    @Getter
    @Setter
    private float lifeShield, maxLifeShield;
    private float lifeShieldDelayTime;
    private boolean lifeShieldDelayFlag;
    //Score to lives with formula scoreToLive*n*(28 - level)
    private int n;
    private BoomBox boomBox;
    //Type, level, score
    private Map<AbstractPoolable.Type, Map<Integer, Integer>> statistics;
    private Profiles profiles;

    private Standing() {
        this.boomBox = new BoomBox();
        this.n = 1;
        this.statistics = new HashMap<AbstractPoolable.Type, Map<Integer, Integer>>();
        this.astronauts = new HashMap<Sticker, Integer>();
        this.profiles = new Profiles();
    }

    public void dispose() {
        statistics.clear();
        Rules.StartLevel.values()[GamePreferences.getLevel()].setGameLevel();
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

    public void changeAstronauts(Sticker profession) {
        if (astronauts.get(profession) == null) {
            astronauts.put(profession, 1);
        } else {
            astronauts.put(profession, astronauts.get(profession) + 1);
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

    public void changeLaserChargeLevel(float amount) {
        laserChargeLevel += amount;
        if (laserChargeLevel <= 0.0f) {
            laserChargeLevel = 0.0f;
        }
        if (laserChargeLevel > maxLaserChargeLevel) {
            laserChargeLevel = maxLaserChargeLevel;
        }
    }

    public void changeRamPower(float amount) {
        ramPower += amount;
        if (ramPower >= ramMaxPower) {
            ramPower = ramMaxPower;
        }
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

    public void takeDamage(int amount) {
        if (!Rules.GAME_OVER) {
            lifeShield -= amount * Rules.PLAYER_SHIELD_DAMAGE_FACTOR;
            if (lifeShield <= 0.0f) {
                changeHP(lifeShield);
            }
        }
    }

    public void changeHP(float amount) {
        hp += amount;
        if (hp > maxHp) {
            hp = maxHp;
        }

        if (hp <= 0) {
            hp = maxHp;
            lifeShield = maxLifeShield;
            ramPower = 0.0f;
            changeLives(-1);
            if (lives != 0) {
                Shake.getInstance().shakeCamera(1, 35, 50);
                fuel = maxFuel;
                if (!Rules.GAME_OVER) {
                    Vibrator.vibrate(VConstants.PLAYER_LOST_LIVE);
                    boomBox.playSound(MSConstants.DOWN);
                }
            }
        }
        //if hp is <= X% of maxHP
        float p = 1.0f * hp / maxHp;
        if (p <= Rules.PLAYER_LOW_LIFE_HP_PERCENT) {
            boomBox.playSound(MSConstants.LOW_LIFE_WARNING);
        }
    }

    public void changeLives(int amount) {
        lives += amount;
        if (lives == 0) {
            Rules.GAME_OVER = true;
            writeResult();
        } else {
            Shuttle.respawn();
            GameInfoSystem.setup(
                    Shuttle.position.x,
                    Shuttle.position.y,
                    "[RED]DESTROYED[]",
                    8.0f,
                    true
            );
        }
    }

    public void writeResult() {
        //
        DetailedResultDAO detailedResultDAO = new DetailedResultDAO();
        detailedResultDAO.setScore(scoreView <= Integer.MAX_VALUE ? String.valueOf(scoreView) : String.valueOf(Rounder.round(scoreView)));
        //
        profiles.writeResults(detailedResultDAO);
    }

    public void update(float dt) {
        lifeShieldTicTac(dt);
        scoreTicTac(dt);
    }

    private void lifeShieldTicTac(float dt) {
        if (!lifeShieldDelayFlag) {
            lifeShield += dt / Rules.PLAYER_SHIELD_REPAIR_FACTOR;
        }
        if (lifeShield > maxLifeShield) {
            lifeShield = maxLifeShield;
        }
        if (lifeShield <= 0.0f) {
            lifeShieldDelayFlag = true;
            lifeShield = 0.0f;
        }
        if (lifeShieldDelayTime <= Rules.PLAYER_SHIELD_DELAY_TIME) {
            lifeShieldDelayTime += dt;
        } else {
            lifeShieldDelayFlag = false;
            lifeShieldDelayTime = 0.0f;
        }
    }

    private void scoreTicTac(float dt) {
        if (scoreView < score) {
            scoreView += 10000 * dt;
            if (scoreView > score) {
                scoreView = score;
            }
        }
        if (scoreView > score) {
            scoreView -= 10000 * dt;
            if (scoreView < score) {
                scoreView = score;
            }
        }
    }

    public static Standing getInstance() {
        if (instance == null) {
            instance = new Standing();
        }
        return instance;
    }
}