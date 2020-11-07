package com.buran.game.player;

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
    private Map<Sticker, Integer> astronauts;
    private Map<Sticker, Integer> usedItems;
    @Getter
    @Setter
    private float lifeShield, maxLifeShield;
    private float lifeShieldDelayTime;
    private boolean lifeShieldDelayFlag;
    private BoomBox boomBox;
    //Type, level, score
    private Map<com.buran.game.utils.pool.AbstractPoolable.Type, Map<Integer, Integer>> statistics;
    private com.buran.game.ui.groups.mvc.profiles.Profiles profiles;
    @Getter
    private float playTime;
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
        this.statistics = new HashMap<com.buran.game.utils.pool.AbstractPoolable.Type, Map<Integer, Integer>>();
        this.astronauts = new HashMap<Sticker, Integer>();
        this.usedItems = new HashMap<Sticker, Integer>();
        this.profiles = new com.buran.game.ui.groups.mvc.profiles.Profiles();
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
        dmgReceived = 0;
        dmgDone = 0;
        levelsToLive = 0;
    }

    public void addToStatistics(com.buran.game.utils.pool.AbstractPoolable.Type type, Integer level) {
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

    public void changeScore(int amount) {
        score += amount;
        calcScoreToLevel();
        if (score - scoreToLevel >= 0) {
            addLevelToLive();
            playingLevel -= 1;
            if (playingLevel < 1) {
                playingLevel = 1;
            } else {
                com.buran.game.ui.GameInfoSystem.setup(
                        ScreenManager.getInstance().getCamera().position.x,
                        ScreenManager.getInstance().getCamera().position.y,
                        String.valueOf(playingLevel),
                        6.0f,
                        2.0f,
                        false,
                        true
                );
                boomBox.playSound(com.buran.game.conf.MSConstants.LEVEL_UP);
                Shake.getInstance().shakeCamera(0.3f, 35, 150);
                Vibrator.vibrate(com.buran.game.conf.VConstants.LEVEL_UP);
            }
        }
    }

    private void calcScoreToLevel() {
        //todo: to be Balanced
        scoreToLevel = 0;
        for (int i = startLevel; i >= playingLevel; i--) {
            scoreToLevel += com.buran.game.conf.Rules.INDEX_SCORE_TO_LEVEL * i * (28 - i);
        }
    }

    private void addLevelToLive() {
        levelsToLive += 1;
        if (levelsToLive >= com.buran.game.conf.Rules.LEVELS_TO_LIVE) {
            levelsToLive = 0;
            lives++;
            boomBox.playSound(com.buran.game.conf.MSConstants.UP);
            com.buran.game.ui.GameInfoSystem.setup(
                    Buran.position.x,
                    Buran.position.y,
                    "+[GREEN]" + com.buran.game.ui.GameInfoSystem.Preset.UP + "[]",
                    4.0f,
                    true
            );
        }
    }

    public void takeDamage(int amount) {
        if (!com.buran.game.conf.Rules.GAME_OVER) {
            float count = amount * com.buran.game.conf.Rules.PLAYER_SHIELD_DAMAGE_FACTOR;
            lifeShield -= count;
            dmgReceived += count;
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
                if (!com.buran.game.conf.Rules.GAME_OVER) {
                    Vibrator.vibrate(com.buran.game.conf.VConstants.PLAYER_LOST_LIVE);
                    boomBox.playSound(com.buran.game.conf.MSConstants.DOWN);
                }
            }
        }
        //if hp is <= X% of maxHP
        float p = 1.0f * hp / maxHp;
        if (p <= com.buran.game.conf.Rules.PLAYER_LOW_LIFE_HP_PERCENT) {
            boomBox.playSound(com.buran.game.conf.MSConstants.LOW_LIFE_WARNING);
        }
    }

    public void changeLives(int amount) {
        lives += amount;
        if (lives == 0) {
            com.buran.game.conf.Rules.GAME_OVER = true;
        } else {
            Buran.respawn();
            com.buran.game.ui.GameInfoSystem.setup(
                    Buran.position.x,
                    Buran.position.y,
                    "[RED]DESTROYED[]",
                    8.0f,
                    true
            );
        }
    }

    public com.buran.game.ui.groups.mvc.results.DetailedResultDAO getDetails() {
        com.buran.game.ui.groups.mvc.results.DetailedResultDAO detailedResultDAO = new com.buran.game.ui.groups.mvc.results.DetailedResultDAO();
        //Set game details
        int count;
        // score
        detailedResultDAO.setScore(scoreView);
        // level
        detailedResultDAO.setLevelAchieved(playingLevel);
        // time
        detailedResultDAO.setTime((int) playTime);
        // dmgDone
        detailedResultDAO.setDmgDone((int) dmgDone);
        // dmgReceived
        detailedResultDAO.setDmgReceived((int) dmgReceived);
        // enemiesDestroyed
        count = 0;
        for (Map.Entry<com.buran.game.utils.pool.AbstractPoolable.Type, Map<Integer, Integer>> i : statistics.entrySet()) {
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
        for (Map.Entry<com.buran.game.utils.pool.AbstractPoolable.Type, Map<Integer, Integer>> i : statistics.entrySet()) {
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
        for (Map.Entry<com.buran.game.utils.pool.AbstractPoolable.Type, Map<Integer, Integer>> i : statistics.entrySet()) {
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
        c = com.buran.game.bot.consumable.AstronautEmitter.getEmitted();
        float ate = 0.0f;
        if (c != 0) {
            ate = 1.0f * detailedResultDAO.getAstronautsTaken() / c;
        }

        c = com.buran.game.ui.groups.selector.SelectorGroup.getInstance().getItems();
        float uie = 0.0f;
        if (c != 0) {
            uie = 1.0f * detailedResultDAO.getUsedItems() / c;
        }

        c = com.buran.game.bot.consumable.CrateEmitter.getEmitted() + com.buran.game.bot.consumable.CrateEmitter.getEmittedGolden() +
                com.buran.game.bot.consumable.StorageEmitter.getEmitted() + com.buran.game.bot.consumable.StorageEmitter.getEmittedGolden() +
                com.buran.game.bot.consumable.FuelEmitter.getEmitted() + com.buran.game.bot.consumable.FuelEmitter.getEmittedGolden();
        float pie = 0.0f;
        if (c != 0) {
            pie = 1.0f * detailedResultDAO.getPickUpItems() / c;
        }

        c = com.buran.game.bot.asteroids.AbstractAsteroidEmitter.getEmitted();
        float mae = 0.0f;
        if (c != 0) {
            mae = 1.0f * detailedResultDAO.getMinedAsteroids() / c;
        }

        c = com.buran.game.bot.anomaly.AbstractAnomalyEmitter.getEmitted();
        float ede = 0.0f;
        if (c != 0) {
            ede = 1.0f * detailedResultDAO.getEnemiesDestroyed() / c;
        }

        float te;
        if (detailedResultDAO.getTime() <= com.buran.game.conf.Rules.GAME_SESSION_TIME) {
            te = 1.0f * detailedResultDAO.getTime() / com.buran.game.conf.Rules.GAME_SESSION_TIME;
        } else {
            te = 1.0f * com.buran.game.conf.Rules.GAME_SESSION_TIME / detailedResultDAO.getTime();
        }

        c = com.buran.game.bot.consumable.AstronautEmitter.getEmitted() * com.buran.game.conf.Rules.PLAYER_DAMAGE_DONE_ASTRONAUT +
                (com.buran.game.bot.consumable.CrateEmitter.getEmitted() + com.buran.game.bot.consumable.CrateEmitter.getEmittedGolden() * com.buran.game.conf.Rules.PLAYER_DAMAGE_DONE_GOLDEN_FACTOR) * com.buran.game.conf.Rules.PLAYER_DAMAGE_DONE_CRATE +
                (com.buran.game.bot.consumable.StorageEmitter.getEmitted() + com.buran.game.bot.consumable.StorageEmitter.getEmittedGolden() * com.buran.game.conf.Rules.PLAYER_DAMAGE_DONE_GOLDEN_FACTOR) * com.buran.game.conf.Rules.PLAYER_DAMAGE_DONE_STORAGE +
                (com.buran.game.bot.consumable.FuelEmitter.getEmitted() + com.buran.game.bot.consumable.FuelEmitter.getEmittedGolden() * com.buran.game.conf.Rules.PLAYER_DAMAGE_DONE_GOLDEN_FACTOR) * com.buran.game.conf.Rules.PLAYER_DAMAGE_DONE_FUEL;
        float dde = 1.0f;
        if (c != 0) {
            dde = 1 - (1.0f * detailedResultDAO.getDmgDone() / c);
        }

        //todo: get avg from play testing
        float dre = 0.5f;

        detailedResultDAO.setEffectiveness(Math.abs((int) (scoreView * (ate + uie + pie + mae + ede + dde + te + dre) / 8)));
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
            lifeShield += dt / com.buran.game.conf.Rules.PLAYER_SHIELD_REPAIR_FACTOR;
        }
        if (lifeShield > maxLifeShield) {
            lifeShield = maxLifeShield;
        }
        if (lifeShield <= 0.0f) {
            lifeShieldDelayFlag = true;
            lifeShield = 0.0f;
        }
        if (lifeShieldDelayTime <= com.buran.game.conf.Rules.PLAYER_SHIELD_DELAY_TIME) {
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

    public void addTime(float amount) {
        playTime += amount;
    }
}