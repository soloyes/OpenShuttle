package com.buran.game.player;

import com.buran.game.effects.Juice;
import com.badlogic.gdx.math.Vector2;
import com.buran.game.bot.anomaly.AbstractAnomalyEmitter;
import com.buran.game.bot.asteroids.AbstractAsteroidEmitter;
import com.buran.game.bot.consumable.AstronautEmitter;
import com.buran.game.bot.consumable.CrateEmitter;
import com.buran.game.bot.consumable.FuelEmitter;
import com.buran.game.bot.consumable.StorageEmitter;
import com.buran.game.conf.DeathMode;
import com.buran.game.conf.MSConstants;
import com.buran.game.conf.Rules;
import com.buran.game.conf.VConstants;
import com.buran.game.effects.BoomBox;
import com.buran.game.effects.Shake;
import com.buran.game.effects.Vibrator;
import com.buran.game.labelsticker.Sticker;
import com.buran.game.screens.ScreenManager;
import com.buran.game.ui.GameInfoSystem;
import com.buran.game.ui.groups.mvc.profiles.Profiles;
import com.buran.game.ui.groups.mvc.results.DetailedResultDAO;
import com.buran.game.ui.groups.selector.SelectorGroup;
import com.buran.game.utils.pool.AbstractPoolable;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * @author soloyes on 8/2/18.
 */

public final class Standing {
    private static Standing instance;
    @Getter
    @Setter
    private long score, scoreView, scoreToLevel;
    @Getter
    @Setter
    private float fuel, maxFuel;
    @Getter
    @Setter
    private float hp, maxHp;
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
    private int playingLevel;
    private int startLevel;
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
    @Getter
    private Map<Sticker, Integer> astronauts;
    private Map<Sticker, Integer> usedItems;
    @Getter
    @Setter
    private float lifeShield, maxLifeShield;
    private float lifeShieldDelayTime;
    private boolean lifeShieldDelayFlag;
    private BoomBox boomBox;
    //Type, level, score
    private Map<AbstractPoolable.Type, Map<Integer, Integer>> statistics;
    private Profiles profiles;
    @Getter
    private float playTime;
    @Getter
    private float deathModeTime;
    private float dmgReceived;
    private float dmgDone;
    @Getter
    private int levelsToLive;

    public static Standing getInstance() {
        if (instance == null) {
            instance = new Standing();
        }
        return instance;
    }

    private Standing() {
        this.boomBox = new BoomBox();
        this.statistics = new HashMap<AbstractPoolable.Type, Map<Integer, Integer>>();
        this.astronauts = new HashMap<Sticker, Integer>();
        this.usedItems = new HashMap<Sticker, Integer>();
        this.profiles = new Profiles();
    }

    public void setStartLevel(int level) {
        playingLevel = level;
        startLevel = level;
    }

    public void clear() {
        statistics.clear();
        astronauts.clear();
        usedItems.clear();
        playTime = 0;
        deathModeTime = 0;
        dmgReceived = 0;
        dmgDone = 0;
        levelsToLive = 0;
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

    /** Crew members taken aboard in this run (all professions). */
    public int getAstronautsTotal() {
        int n = 0;
        if (astronauts != null) {
            for (Integer v : astronauts.values()) {
                n += v;
            }
        }
        return n;
    }

    /** Asteroids burnt down to fragments in this run. */
    public int getMinedTotal() {
        int n = 0;
        if (statistics != null) {
            for (Map.Entry<AbstractPoolable.Type, Map<Integer, Integer>> e : statistics.entrySet()) {
                if (e.getKey() == AbstractPoolable.Type.ASTEROID_METAL || e.getKey() == AbstractPoolable.Type.ASTEROID_MINERAL) {
                    for (Integer v : e.getValue().values()) {
                        n += v;
                    }
                }
            }
        }
        return n;
    }

    public void changeUsedItems(Sticker item) {
        if (usedItems.get(item) == null) {
            usedItems.put(item, 1);
        } else {
            usedItems.put(item, usedItems.get(item) + 1);
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

    public void changeDmgDone(float amount) {
        dmgDone += amount;
    }

    public void changeScore(int amount, Vector2 position, String text, float scale, boolean colored) {
        score += amount;
        GameInfoSystem.setup(
                position.x,
                position.y,
                text,
                scale,
                colored
        );
        calcScoreToLevel();
        if (score - scoreToLevel >= 0) {
            playingLevel += 1;
            if (playingLevel >= 19) {
                playingLevel = 19;
                if (!Rules.DEATH_MODE) {
                    DeathMode.activate();
                    boomBox.stopAllMusicAndPlayThese(MSConstants.DEATH_MODE, MSConstants.BLACKHOLE_ROAR_MUSIC);
                    GameInfoSystem.setup(
                            ScreenManager.getInstance().getCamera().position.x,
                            ScreenManager.getInstance().getCamera().position.y,
                            GameInfoSystem.Preset.DEATH_MODE.getValue(),
                            4.0f,
                            2.0f,
                            true,
                            true
                    );
                }
            } else {
                addLevelToLive();
                if (playingLevel % 5 == 0) {
                    GameInfoSystem.setup(
                            ScreenManager.getInstance().getCamera().position.x,
                            ScreenManager.getInstance().getCamera().position.y,
                            String.valueOf(GameInfoSystem.Preset.KEEP_GOING.getValue()),
                            4.0f,
                            2.0f,
                            true,
                            true
                    );
                    if (playingLevel == 5){
                        boomBox.playSound(MSConstants.LEVEL_5_KEEP_GOING);
                    } else if (playingLevel == 10){
                        boomBox.playSound(MSConstants.LEVEL_10_KEEP_GOING);
                    } else if (playingLevel == 15){
                        boomBox.playSound(MSConstants.LEVEL_15_KEEP_GOING);
                    }
                } else {
                    GameInfoSystem.setup(
                            ScreenManager.getInstance().getCamera().position.x,
                            ScreenManager.getInstance().getCamera().position.y,
                            String.valueOf(playingLevel),
                            6.0f,
                            2.0f,
                            false,
                            true
                    );
                }
                boomBox.playSound(MSConstants.LEVEL_UP);
                Shake.getInstance().shakeCamera(0.3f, 35, 150);
                Vibrator.vibrateGame(VConstants.LEVEL_UP);
            }
        }
    }

    private void calcScoreToLevel() {
        //todo: Balance
        scoreToLevel = 0;
        for (int i = startLevel; i <= playingLevel; i++) {
            scoreToLevel += Rules.INDEX_SCORE_TO_LEVEL * i * (38 - i);
        }
    }

    private void addLevelToLive() {
        levelsToLive += 1;
        if (levelsToLive >= Rules.LEVELS_TO_LIVE) {
            levelsToLive = 0;
            lives++;
            boomBox.playSound(MSConstants.UP);
            GameInfoSystem.setup(
                    Buran.position.x,
                    Buran.position.y,
                    "+1[GREEN] " + GameInfoSystem.Preset.UP + "[]",
                    4.0f,
                    true
            );
        }
    }

    public void takeDamage(float amount) {
        if (!Rules.GAME_OVER) {
            float count = amount * Rules.PLAYER_SHIELD_DAMAGE_FACTOR;
            if (count > 0f) {
                Juice.damage(count, lifeShield - count > 0f);
            }
            lifeShield -= count;
            dmgReceived += count;
            if (lifeShield <= 0.0f) {
                changeHP(lifeShield);
            }
        }
    }

    public void changeHP(float amount) {
        if (Rules.GAME_OVER) {
            // The ship keeps updating under the game-over screen; it must not lose more lives.
            return;
        }
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
                    Vibrator.vibrateGame(VConstants.PLAYER_LOST_LIVE);
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
        if (amount < 0) {
            if (lives <= 0) {
                Juice.gameOver();
            } else {
                Juice.lifeLost();
            }
        }
        if (lives <= 0) {
            lives = 0;
            Rules.GAME_OVER = true;
        } else {
            if (amount < 0) {
                Buran.respawn();
                GameInfoSystem.setup(
                        Buran.position.x,
                        Buran.position.y,
                        "[RED]DESTROYED[]",
                        8.0f,
                        true
                );
            } else {
                GameInfoSystem.setup(
                        Buran.position.x,
                        Buran.position.y,
                        "+1[GREEN] " + GameInfoSystem.Preset.UP + "[]",
                        4.0f,
                        true
                );
            }
        }
    }

    public DetailedResultDAO getDetails() {
        DetailedResultDAO detailedResultDAO = new DetailedResultDAO();
        //Set game details
        int count;
        // score
        detailedResultDAO.setScore(scoreView);
        // level
        detailedResultDAO.setLevelAchieved(playingLevel);
        // time
        detailedResultDAO.setTime((int) playTime);
        // deathModeTIme
        detailedResultDAO.setDeathModeTime((int) deathModeTime);
        // dmgDone
        detailedResultDAO.setDmgDone((int) dmgDone);
        // dmgReceived
        detailedResultDAO.setDmgReceived((int) dmgReceived);
        // enemiesDestroyed
        count = 0;
        for (Map.Entry<AbstractPoolable.Type, Map<Integer, Integer>> i : statistics.entrySet()) {
            switch (i.getKey()) {
                case ELECTRIC:
                case MAGNETIC:
                case RADIATION:
                case CORROSIVE:
                    for (Map.Entry<Integer, Integer> j : i.getValue().entrySet()) {
                        count += j.getValue();
                    }
            }
        }
        detailedResultDAO.setEnemiesDestroyed(count);
        // minedAsteroids
        count = 0;
        for (Map.Entry<AbstractPoolable.Type, Map<Integer, Integer>> i : statistics.entrySet()) {
            switch (i.getKey()) {
                case ASTEROID_METAL:
                case ASTEROID_MINERAL:
                    for (Map.Entry<Integer, Integer> j : i.getValue().entrySet()) {
                        count += j.getValue();
                    }
            }
        }
        detailedResultDAO.setMinedAsteroids(count);
        // pickUpItems
        count = 0;
        for (Map.Entry<AbstractPoolable.Type, Map<Integer, Integer>> i : statistics.entrySet()) {
            switch (i.getKey()) {
                case CRATE:
                case STORAGE:
                case FUEL:
                    for (Map.Entry<Integer, Integer> j : i.getValue().entrySet()) {
                        count += j.getValue();
                    }
            }
        }
        detailedResultDAO.setPickUpItems(count);
        // usedItems
        count = 0;
        for (Map.Entry<Sticker, Integer> i : usedItems.entrySet()) {
            count += i.getValue();
        }
        detailedResultDAO.setUsedItems(count);
        // astronautsTaken
        count = 0;
        for (Map.Entry<Sticker, Integer> i : astronauts.entrySet()) {
            count += i.getValue();
        }
        detailedResultDAO.setAstronautsTaken(count);
        // effectiveness
        //
        float c;
        c = AstronautEmitter.getEmitted();
        float ate = 0.0f;
        if (c != 0) {
            ate = 1.0f * detailedResultDAO.getAstronautsTaken() / c;
        }

        c = SelectorGroup.getInstance().getItems();
        float uie = 0.0f;
        if (c != 0) {
            uie = 1.0f * detailedResultDAO.getUsedItems() / c;
        }

        c = CrateEmitter.getEmitted() + CrateEmitter.getEmittedGolden() +
                StorageEmitter.getEmitted() + StorageEmitter.getEmittedGolden() +
                FuelEmitter.getEmitted() + FuelEmitter.getEmittedGolden();
        float pie = 0.0f;
        if (c != 0) {
            pie = 1.0f * detailedResultDAO.getPickUpItems() / c;
        }

        c = AbstractAsteroidEmitter.getEmitted();
        float mae = 0.0f;
        if (c != 0) {
            mae = 1.0f * detailedResultDAO.getMinedAsteroids() / c;
        }

        c = AbstractAnomalyEmitter.getEmitted();
        float ede = 0.0f;
        if (c != 0) {
            ede = 1.0f * detailedResultDAO.getEnemiesDestroyed() / c;
        }

        float te;
        if (detailedResultDAO.getTime() <= Rules.GAME_SESSION_TIME) {
            te = 1.0f * detailedResultDAO.getTime() / Rules.GAME_SESSION_TIME;
        } else {
            te = 1.0f * Rules.GAME_SESSION_TIME / detailedResultDAO.getTime();
        }

        float dmte;
        if (detailedResultDAO.getDeathModeTime() <= Rules.GAME_SESSION_TIME) {
            dmte = 1.0f * detailedResultDAO.getDeathModeTime() / Rules.GAME_SESSION_TIME;
        } else {
            dmte = 1.0f * Rules.GAME_SESSION_TIME / detailedResultDAO.getDeathModeTime();
        }

        c = AstronautEmitter.getEmitted() * Rules.PLAYER_DAMAGE_DONE_ASTRONAUT +
                (CrateEmitter.getEmitted() + CrateEmitter.getEmittedGolden() * Rules.PLAYER_DAMAGE_DONE_GOLDEN_FACTOR) * Rules.PLAYER_DAMAGE_DONE_CRATE +
                (StorageEmitter.getEmitted() + StorageEmitter.getEmittedGolden() * Rules.PLAYER_DAMAGE_DONE_GOLDEN_FACTOR) * Rules.PLAYER_DAMAGE_DONE_STORAGE +
                (FuelEmitter.getEmitted() + FuelEmitter.getEmittedGolden() * Rules.PLAYER_DAMAGE_DONE_GOLDEN_FACTOR) * Rules.PLAYER_DAMAGE_DONE_FUEL;
        float dde = 1.0f;
        if (c != 0) {
            dde = 1 - (1.0f * detailedResultDAO.getDmgDone() / c);
        }

        int ef = (int) (100 * (ate + uie + pie + mae + ede + dde + te + dmte) / 8);
        detailedResultDAO.setEffectiveness(ef >= 100 ? 100 : ef);
        //
        return detailedResultDAO;
    }

    public void writeResult() {
        profiles.writeResults(getDetails());
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
            scoreView += 45000 * dt;
            if (scoreView > score) {
                scoreView = score;
            }
        }
        if (scoreView > score) {
            scoreView -= 45000 * dt;
            if (scoreView < score) {
                scoreView = score;
            }
        }
    }

    public void incrementMaxHP() {
        maxHp += Rules.INCREMENT_OF_SINGLE_BAR * Sticker.MEDIC.getCoefficient();
    }

    public void incrementMaxLifeShield() {
        maxLifeShield += Rules.INCREMENT_OF_SINGLE_BAR * Sticker.MECHANIC.getCoefficient();
    }

    public void incrementMaxFuel() {
        maxFuel += Rules.INCREMENT_OF_SINGLE_BAR * Sticker.SCIENTIST.getCoefficient();
    }

    public void incrementLaserChargeLevel() {
        maxLaserChargeLevel += Rules.INCREMENT_OF_SINGLE_BAR * Sticker.MILITARY.getCoefficient();
    }

    public void incrementScore() {
    }

    public void addTime(float amount) {
        playTime += amount;
        if (Rules.DEATH_MODE) {
            deathModeTime += amount;
        }
    }
}