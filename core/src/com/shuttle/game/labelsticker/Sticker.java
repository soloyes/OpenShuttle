package com.shuttle.game.labelsticker;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.anomaly.AbstractAnomaly;
import com.shuttle.game.bot.anomaly.CorrosiveAnomaly;
import com.shuttle.game.bot.anomaly.CorrosiveAnomalyEmitter;
import com.shuttle.game.bot.anomaly.ElectricalAnomaly;
import com.shuttle.game.bot.anomaly.ElectricalAnomalyEmitter;
import com.shuttle.game.bot.anomaly.MagneticAnomaly;
import com.shuttle.game.bot.anomaly.MagneticAnomalyEmitter;
import com.shuttle.game.bot.anomaly.RadiationAnomaly;
import com.shuttle.game.bot.anomaly.RadiationAnomalyEmitter;
import com.shuttle.game.bot.consumable.AbstractConsumable;
import com.shuttle.game.bot.consumable.Crate;
import com.shuttle.game.bot.consumable.CrateEmitter;
import com.shuttle.game.bot.consumable.FuelEmitter;
import com.shuttle.game.bot.consumable.Storage;
import com.shuttle.game.bot.consumable.StorageEmitter;
import com.shuttle.game.bot.consumable.fuel.Fuel;
import com.shuttle.game.bot.rocket.RocketLauncher;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.effects.AsteroidLaunchEmitter;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.player.Standing;
import com.shuttle.game.player.navigation.Radar;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.ui.GameInfoSystem;
import com.shuttle.game.utils.Consumer;
import com.shuttle.game.utils.pool.AbstractPoolable;
import com.shuttle.game.utils.pool.ObjectPool;

import java.util.Arrays;
import java.util.List;

/**
 * @author Shuttle on 3/27/19.
 */

public enum Sticker {
    //Crate
    BOOST {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
            if (!isGolden) {
                float d = Rules.PLAYER_BOOST_ENGINE_POWER - Shuttle.velocity.len();
                Shuttle.setShadowsCoordinates();
                Shuttle.velocity.add(
                        d * (float) Math.cos(Math.toRadians(Shuttle.angle)),
                        d * (float) Math.sin(Math.toRadians(Shuttle.angle))
                );
            }
        }

        @Override
        public void consume(AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
            notify(consumable);
        }

        @Override
        public void notify(AbstractConsumable consumable) {
            print(consumable, GameInfoSystem.Preset.TURBO);
            sound();
        }

        @Override
        public void sound() {
            boomBox.playSound(MSConstants.GET_BOOST);
        }
    },
    REPAIR {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
            if (!isGolden) {
                standing.changeHP(Rules.CONSUMABLE_REPAIR_ADD);
            }
        }

        @Override
        public void consume(AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
            notify(consumable);
        }

        @Override
        public void notify(AbstractConsumable consumable) {
            print(consumable, GameInfoSystem.Preset.REPAIR);
            sound();
        }

        @Override
        public void sound() {
            boomBox.playSound(MSConstants.GET_REPAIR);
        }
    },
    SHIELD {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
            if (!isGolden) {
                CrateEmitter crateEmitter = (CrateEmitter) ObjectPool.EMITTERS.get(Crate.class);
                List<AbstractConsumable> list = crateEmitter.getActiveList();
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setShielded(true);
                }
                FuelEmitter fuelEmitter = (FuelEmitter) ObjectPool.EMITTERS.get(Fuel.class);
                list = fuelEmitter.getActiveList();
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setShielded(true);
                }
                StorageEmitter storageEmitter = (StorageEmitter) ObjectPool.EMITTERS.get(Storage.class);
                list = storageEmitter.getActiveList();
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setShielded(true);
                }
            }
        }

        @Override
        public void consume(AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
            notify(consumable);
        }

        @Override
        public void notify(AbstractConsumable consumable) {
            print(consumable, GameInfoSystem.Preset.SHIELD);
            sound();
        }

        @Override
        public void sound() {
            boomBox.playSound(MSConstants.GET_SHIELD);
        }
    },
    TRASH {
        int score;

        @Override
        public void consume(Vector2 position, boolean isGolden) {
            AsteroidLaunchEmitter asteroidLaunchEmitter = (AsteroidLaunchEmitter) ObjectPool.EMITTERS.get(AsteroidLaunchEmitter.Element.class);
            asteroidLaunchEmitter.addToList();
            score = Rules.CONSUMABLE_TRASH_BASE_SCORE *
                    (MathUtils.random(Rules.CONSUMABLE_TRASH_GOLDEN_SCORE_MIN, Rules.CONSUMABLE_TRASH_GOLDEN_SCORE_MAX) *
                            ((isGolden) ? Rules.CONSUMABLE_TRASH_GOLDEN_SCORE_FACTOR : 1)
                    );
            standing.changeScore(score);
            //todo: Побольше очков
            ScreenManager.getInstance().getAdsController().showInterstitialAd();
        }

        @Override
        public void consume(AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
            notify(consumable);
        }

        @Override
        public void notify(AbstractConsumable consumable) {
            GameInfoSystem.setup(
                    consumable.getPosition().x, consumable.getPosition().y,
                    "+" + score + (consumable.isGolden() ? "[GOLD]" : "[GREEN]") + GameInfoSystem.Preset.TRASH + "[]",
                    (consumable.isGolden() ? 7.0f : 3.0f),
                    true
            );
            sound();
        }

        @Override
        public void sound() {
            boomBox.playSound(MSConstants.GET_TRASH);
        }
    },
    SPLASH {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
            if (!isGolden) {
                CorrosiveAnomalyEmitter corrosiveAnomalyEmitter = (CorrosiveAnomalyEmitter) ObjectPool.EMITTERS.get(CorrosiveAnomaly.class);
                List<AbstractAnomaly> anomalies = corrosiveAnomalyEmitter.getActiveList();
                for (int i = 0; i < anomalies.size(); i++) {
                    if (anomalies.get(i).getPosition().dst(Shuttle.position) <= Rules.WORLD_WIDTH) {
                        if (!anomalies.get(i).isTaken()) {
                            anomalies.get(i).setMarkedToBoom(true);
                            standing.addToStatistics(anomalies.get(i).getType(), anomalies.get(i).getLevel());
                            standing.changeScore(Rules.ANOMALIES_BASE_SCORE_ADD);
                        }
                    }
                }
            }
        }

        @Override
        public void consume(AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
            notify(consumable);
        }

        @Override
        public void notify(AbstractConsumable consumable) {
            print(consumable, GameInfoSystem.Preset.SPLASH);
            sound();
        }

        @Override
        public void sound() {
            boomBox.playSound(MSConstants.GET_SPLASH);
        }
    },
    SCAN {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
            if (!isGolden) {
                CrateEmitter crateEmitter = (CrateEmitter) ObjectPool.EMITTERS.get(Crate.class);
                List<AbstractConsumable> list = crateEmitter.getActiveList();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isDanger()) {
                        list.get(i).setScanned(true);
                    }
                }
                StorageEmitter storageEmitter = (StorageEmitter) ObjectPool.EMITTERS.get(Storage.class);
                list = storageEmitter.getActiveList();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isDanger()) {
                        list.get(i).setScanned(true);
                    }
                }
            }
        }

        @Override
        public void consume(AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
            notify(consumable);
        }

        @Override
        public void notify(AbstractConsumable consumable) {
            print(consumable, GameInfoSystem.Preset.SCAN);
            sound();
        }

        @Override
        public void sound() {
            boomBox.playSound(MSConstants.GET_SCANNER);
        }
    },
    //Storage
    LASER {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
            Shuttle.isLaser = true;
            Standing.getInstance().setLaserChargeLevel(Standing.getInstance().getMaxLaserChargeLevel());
        }

        @Override
        public void consume(AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
            notify(consumable);
        }

        @Override
        public void notify(AbstractConsumable consumable) {
            print(consumable, GameInfoSystem.Preset.LASER);
            sound();
        }

        @Override
        public void sound() {
            boomBox.playSound(MSConstants.GET_LASER);
        }
    },
    RADAR {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
            Radar.isActive = true;
        }

        @Override
        public void consume(AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
            notify(consumable);
        }

        @Override
        public void notify(AbstractConsumable consumable) {
            print(consumable, GameInfoSystem.Preset.RADAR);
            sound();
        }

        @Override
        public void sound() {
            boomBox.playSound(MSConstants.GET_RADAR);
        }
    },
    ROCKETS {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
            if (!isGolden) {
                RocketLauncher.launch();
            }
        }

        @Override
        public void consume(AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
            notify(consumable);
        }

        @Override
        public void notify(AbstractConsumable consumable) {
            print(consumable, GameInfoSystem.Preset.ROCKETS);
            boomBox.playSound(MSConstants.GET_ROCKETS);
        }

        @Override
        public void sound() {
        }
    },
    //Fuel
    FUEL {
        float fuel;

        @Override
        public void consume(Vector2 position, boolean isGolden) {
            if (isGolden) {
                fuel = Standing.getInstance().getMaxFuel();
            } else {
                fuel = Rules.CONSUMABLE_FUEL_ADD_FACTOR *
                        MathUtils.random(
                                Rules.CONSUMABLE_FUEL_MIN_ADD,
                                Rules.CONSUMABLE_FUEL_MAX_ADD
                        );
            }
            Standing.getInstance().changeFuel(fuel);

        }

        @Override
        public void consume(AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
            notify(consumable);
        }

        @Override
        public void notify(AbstractConsumable consumable) {
            print(consumable, GameInfoSystem.Preset.FUEL);
            sound();
        }

        @Override
        public void sound() {
            boomBox.playSound(MSConstants.GET_FUEL);
        }
    },
    FUEL_UI {
        @Override
        public void consume(Vector2 position, boolean isGolden) {

        }

        @Override
        public void consume(AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
        }

        @Override
        public void notify(AbstractConsumable consumable) {

        }

        @Override
        public void sound() {

        }
    },
    //Surprise
    PATTERN {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
        }

        @Override
        public void consume(AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
        }

        @Override
        public void notify(AbstractConsumable consumable) {

        }

        @Override
        public void sound() {

        }
    },
    PATTERN_GOLD {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
        }

        @Override
        public void consume(AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
        }

        @Override
        public void notify(AbstractConsumable consumable) {

        }

        @Override
        public void sound() {

        }
    },
    SHABBY {
        @Override
        public void consume(Vector2 position, boolean isGolden) {

        }

        @Override
        public void consume(AbstractConsumable consumable) {
            Sticker.consumeShabby(consumable);
        }

        @Override
        public void notify(AbstractConsumable consumable) {

        }

        @Override
        public void sound() {

        }
    },
    SHABBY_GOLD {
        @Override
        public void consume(Vector2 position, boolean isGolden) {

        }

        @Override
        public void consume(AbstractConsumable consumable) {
            Sticker.consumeShabby(consumable);
        }

        @Override
        public void notify(AbstractConsumable consumable) {

        }

        @Override
        public void sound() {

        }
    },
    //Astronaut
    REGULAR {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
        }

        @Override
        public void consume(AbstractConsumable consumable) {
        }

        @Override
        public void notify(AbstractConsumable consumable) {

        }

        @Override
        public void sound() {

        }
    }, MECHANIC {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
        }

        @Override
        public void consume(AbstractConsumable consumable) {
        }

        @Override
        public void notify(AbstractConsumable consumable) {

        }

        @Override
        public void sound() {

        }
    }, MEDIC {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
        }

        @Override
        public void consume(AbstractConsumable consumable) {
        }

        @Override
        public void notify(AbstractConsumable consumable) {

        }

        @Override
        public void sound() {

        }
    }, MILITARY {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
        }

        @Override
        public void consume(AbstractConsumable consumable) {
        }

        @Override
        public void notify(AbstractConsumable consumable) {

        }

        @Override
        public void sound() {

        }
    }, SCIENTIST {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
        }

        @Override
        public void consume(AbstractConsumable consumable) {
        }

        @Override
        public void notify(AbstractConsumable consumable) {

        }

        @Override
        public void sound() {

        }
    };
    public static List<Sticker> SELECTABLE = Arrays.asList(BOOST, REPAIR, SHIELD, SPLASH, SCAN, ROCKETS);
    public static List<Sticker> ASTRONAUTS = Arrays.asList(REGULAR, MEDIC, SCIENTIST, MECHANIC, MILITARY);
    private static BoomBox boomBox;
    private static Standing standing;

    static {
        boomBox = new BoomBox();
        standing = Standing.getInstance();
    }

    public abstract void consume(Vector2 position, boolean isGolden);

    public abstract void consume(AbstractConsumable consumable);

    public abstract void notify(AbstractConsumable consumable);

    public abstract void sound();

    private static void consumeShabby(AbstractConsumable consumable) {
        if (MathUtils.random(0, 1) == 0) {
            getPositive(consumable);
        } else {
            getNegative(consumable);
        }
    }

    private static void getPositive(AbstractConsumable consumable) {
        // 0 - Kill related level corrosive anomaly
        // 1 - Add score
        // 2 - Add manual activated loot, or generate Crate/Storage
        // 3 - Kill Fuel/Radiation anomaly, or add HP/life
        int level = (consumable.isGolden() ? 1 : 0) + (consumable.getType() == AbstractPoolable.Type.CRATE ? 0 : 2);
        int r = MathUtils.random(0, 3);
        int score;
        switch (r) {
            case 0:
                CorrosiveAnomalyEmitter corrosiveAnomalyEmitter = (CorrosiveAnomalyEmitter) ObjectPool.EMITTERS.get(CorrosiveAnomaly.class);
                List<AbstractAnomaly> anomalies = corrosiveAnomalyEmitter.getActiveList();
                for (int i = 0; i < anomalies.size(); i++) {
                    AbstractAnomaly anomaly = anomalies.get(i);
                    if (anomaly.getLevel() == level && !anomaly.isTaken()) {
                        anomaly.setMarkedToBoom(true);
                        Standing.getInstance().changeScore(Rules.ANOMALIES_BASE_SCORE_ADD * (level + 1));
                    }
                }
                GameInfoSystem.setup(
                        consumable.getPosition().x, consumable.getPosition().y,
                        GameInfoSystem.Preset.ANOMALY_FLUSH.getValue(),
                        5.0f,
                        true
                );
                boomBox.playSound(MSConstants.ANOMALY_FLUSH);
                break;
            case 1:
                score = (level + 1) * Rules.SCORE_BASE_BONUS;
                Standing.getInstance().changeScore(score);
                GameInfoSystem.setup(
                        consumable.getPosition().x, consumable.getPosition().y,
                        GameInfoSystem.Preset.SCORE + "[GREEN] +" + score + "[]",
                        5.0f,
                        true
                );
                boomBox.playSound(MSConstants.ADD_SCORE);
                break;
            case 2:
                if (consumable.isGolden()) {
                    for (int i = 0; i < Sticker.SELECTABLE.size(); i++) {
                        for (int j = 0; j < Rules.SELECTOR_SHABBY_BASE_BONUS; j++) {
                            Consumer.consume(Sticker.SELECTABLE.get(i), consumable);
                        }
                    }
                    GameInfoSystem.setup(
                            consumable.getPosition().x, consumable.getPosition().y,
                            GameInfoSystem.Preset.BIG_BONUS.getValue(),
                            5.0f,
                            true
                    );
                    boomBox.playSound(MSConstants.ADD_SELECTABLE);
                } else {
                    switch (consumable.getType()) {
                        case CRATE:
                            GameInfoSystem.setup(
                                    consumable.getPosition().x, consumable.getPosition().y,
                                    GameInfoSystem.Preset.BONUS_CRATE.getValue(),
                                    5.0f,
                                    true
                            );
                            for (int i = 0; i < (level + 1) * Rules.CONSUMABLE_GENERATE_BASE_BONUS; i++) {
                                CrateEmitter crateEmitter = (CrateEmitter) ObjectPool.EMITTERS.get(Crate.class);
                                crateEmitter.createBonusCrate();
                            }
                            break;
                        case STORAGE:
                            StorageEmitter storageEmitter = (StorageEmitter) ObjectPool.EMITTERS.get(Storage.class);
                            GameInfoSystem.setup(
                                    consumable.getPosition().x, consumable.getPosition().y,
                                    GameInfoSystem.Preset.BONUS_STORAGE.getValue(),
                                    5.0f,
                                    true
                            );
                            for (int i = 0; i < (level + 1) * Rules.CONSUMABLE_GENERATE_BASE_BONUS; i++) {
                                storageEmitter.createBonusStorage();
                            }
                            break;
                    }
                    boomBox.playSound(MSConstants.GENERATE_CRATE_STORAGE);
                }
                break;
            case 3:
                if (consumable.isGolden()) {
                    switch (consumable.getType()) {
                        case CRATE:
                            Standing.getInstance().changeHP(Standing.getInstance().getMaxHp());
                            boomBox.playSound(MSConstants.ADD_HP);
                            break;
                        case STORAGE:
                            Standing.getInstance().changeHP(Standing.getInstance().getMaxHp());
                            Standing.getInstance().changeLives(1);
                            boomBox.playSound(MSConstants.UP);
                            break;
                    }
                } else {
                    switch (consumable.getType()) {
                        case CRATE:
                            List<AbstractAnomaly> list;
                            if (MathUtils.random(0, 1) == 0) {
                                MagneticAnomalyEmitter magneticAnomalyEmitter = (MagneticAnomalyEmitter) ObjectPool.EMITTERS.get(MagneticAnomaly.class);
                                list = magneticAnomalyEmitter.getActiveList();
                                score = Rules.ANOMALIES_MAGNETIC_BONUS_SCORE;
                            } else {
                                RadiationAnomalyEmitter radiationAnomalyEmitter = (RadiationAnomalyEmitter) ObjectPool.EMITTERS.get(RadiationAnomaly.class);
                                list = radiationAnomalyEmitter.getActiveList();
                                score = Rules.ANOMALIES_RADIATION_BONUS_SCORE;
                            }
                            for (int i = 0; i < list.size(); i++) {
                                AbstractAnomaly anomaly = list.get(i);
                                if (!anomaly.isTaken()) {
                                    list.get(i).destroyWithEffect();
                                    GameInfoSystem.setup(
                                            anomaly.getPosition().x, anomaly.getPosition().y,
                                            GameInfoSystem.Preset.SCORE + "[GREEN] +" + score + "[]",
                                            5.0f,
                                            true
                                    );
                                }
                            }
                            break;
                        case STORAGE:
                            score = Rules.ANOMALIES_MAGNETIC_BONUS_SCORE;
                            MagneticAnomalyEmitter magneticAnomalyEmitter = (MagneticAnomalyEmitter) ObjectPool.EMITTERS.get(MagneticAnomaly.class);
                            list = magneticAnomalyEmitter.getActiveList();
                            for (int i = 0; i < list.size(); i++) {
                                AbstractAnomaly anomaly = list.get(i);
                                if (!anomaly.isTaken()) {
                                    anomaly.destroyWithEffect();
                                    GameInfoSystem.setup(
                                            anomaly.getPosition().x, anomaly.getPosition().y,
                                            GameInfoSystem.Preset.SCORE + "[GREEN] +" + score + "[]",
                                            5.0f,
                                            true
                                    );
                                }
                            }
                            score = Rules.ANOMALIES_RADIATION_BONUS_SCORE;
                            RadiationAnomalyEmitter radiationAnomalyEmitter = (RadiationAnomalyEmitter) ObjectPool.EMITTERS.get(RadiationAnomaly.class);
                            list = radiationAnomalyEmitter.getActiveList();
                            for (int i = 0; i < list.size(); i++) {
                                AbstractAnomaly anomaly = list.get(i);
                                if (!anomaly.isTaken()) {
                                    anomaly.destroyWithEffect();
                                    GameInfoSystem.setup(
                                            anomaly.getPosition().x, anomaly.getPosition().y,
                                            GameInfoSystem.Preset.SCORE + "[GREEN] +" + score + "[]",
                                            5.0f,
                                            true
                                    );
                                }
                            }
                            break;
                    }
                    GameInfoSystem.setup(
                            consumable.getPosition().x, consumable.getPosition().y,
                            GameInfoSystem.Preset.ANOMALY_FLUSH.getValue(),
                            5.0f,
                            true
                    );
                    boomBox.playSound(MSConstants.KILL_MAGNETIC_RADIATION);
                    break;
                }
        }
    }

    private static void getNegative(AbstractConsumable consumable) {
        // 0 - Add related level corrosive anomaly
        // 1 - Del score
        // 2 - Disable Laser/Radar, or del Crate/Storage
        // 3 - Add Electrical/Corrosive/Radiation anomaly, or del HP/fuel/life
        int level = (consumable.isGolden() ? 1 : 0) + (consumable.getType() == AbstractPoolable.Type.CRATE ? 0 : 2);
        int r = MathUtils.random(0, 3);
        int score;
        switch (r) {
            case 0:
                CorrosiveAnomalyEmitter corrosiveAnomalyEmitter = (CorrosiveAnomalyEmitter) ObjectPool.EMITTERS.get(CorrosiveAnomaly.class);
                corrosiveAnomalyEmitter.createBonusAnomaly(consumable.getPosition(), level);
                for (int i = 0; i < (4 - level) * Rules.ANOMALY_GENERATE_BASE_BONUS; i++) {
                    corrosiveAnomalyEmitter.createBonusAnomaly(level);
                }
                GameInfoSystem.setup(
                        consumable.getPosition().x, consumable.getPosition().y,
                        GameInfoSystem.Preset.ANOMALY_ARISE.getValue(),
                        5.0f,
                        true
                );
                boomBox.playSound(MSConstants.ADD_CORROSIVE);
                break;
            case 1:
                score = (level + 1) * Rules.SCORE_BASE_BONUS;
                Standing.getInstance().changeScore(-score);
                GameInfoSystem.setup(
                        consumable.getPosition().x, consumable.getPosition().y,
                        GameInfoSystem.Preset.SCORE + "-[RED]" + score + "[]",
                        5.0f,
                        true
                );
                boomBox.playSound(MSConstants.DEL_SCORE);
                break;
            case 2:
                switch (consumable.getType()) {
                    case CRATE:
                        if (consumable.isGolden()) {
                            Radar.isActive = false;
                            GameInfoSystem.setup(
                                    consumable.getPosition().x, consumable.getPosition().y,
                                    "-[RED]" + GameInfoSystem.Preset.RADAR + "[]",
                                    5.0f,
                                    true
                            );
                        } else {
                            Shuttle.isLaser = false;
                            GameInfoSystem.setup(
                                    consumable.getPosition().x, consumable.getPosition().y,
                                    "-[RED]" + GameInfoSystem.Preset.LASER + "[]",
                                    5.0f,
                                    true
                            );
                        }
                        boomBox.playSound(MSConstants.DISABLE_LASER_RADAR);
                        break;
                    case STORAGE:
                        List<AbstractConsumable> list;
                        GameInfoSystem.Preset preset;
                        if (consumable.isGolden()) {
                            StorageEmitter storageEmitter = (StorageEmitter) ObjectPool.EMITTERS.get(Storage.class);
                            list = storageEmitter.getActiveList();
                            preset = GameInfoSystem.Preset.STORAGE_FLUSH;
                        } else {
                            CrateEmitter crateEmitter = (CrateEmitter) ObjectPool.EMITTERS.get(Crate.class);
                            list = crateEmitter.getActiveList();
                            preset = GameInfoSystem.Preset.CRATE_FLUSH;
                        }

                        for (int i = 0; i < list.size(); i++) {
                            AbstractConsumable c = list.get(i);
                            if (!c.isTaken()) {
                                c.destroyWithEffect();
                            }
                        }
                        GameInfoSystem.setup(
                                consumable.getPosition().x, consumable.getPosition().y,
                                preset.getValue(),
                                5.0f,
                                true
                        );
                        boomBox.playSound(MSConstants.DEL_CRATE_STORAGE);
                        break;
                }
                break;
            case 3:
                switch (consumable.getType()) {
                    case CRATE:
                        if (consumable.isGolden()) {
                            Standing.getInstance().changeLives(-1);
                            GameInfoSystem.setup(
                                    consumable.getPosition().x, consumable.getPosition().y,
                                    "-[RED]" + GameInfoSystem.Preset.UP + "[]",
                                    5.0f,
                                    true
                            );
                            boomBox.playSound(MSConstants.DOWN);
                        } else {
                            if (MathUtils.random(0, 1) == 0) {
                                Standing.getInstance().changeHP(-Standing.getInstance().getHp() + 1);
                                GameInfoSystem.setup(
                                        consumable.getPosition().x, consumable.getPosition().y,
                                        "-[RED]" + GameInfoSystem.Preset.HP + "[]",
                                        5.0f,
                                        true
                                );
                            } else {
                                Standing.getInstance().changeFuel(-Standing.getInstance().getFuel());
                                GameInfoSystem.setup(
                                        consumable.getPosition().x, consumable.getPosition().y,
                                        "-[RED]" + GameInfoSystem.Preset.FUEL + "[]",
                                        5.0f,
                                        true
                                );
                            }
                            boomBox.playSound(MSConstants.DEL_HP_FUEL);
                        }
                        break;
                    case STORAGE:
                        if (consumable.isGolden()) {
                            ElectricalAnomalyEmitter electricalAnomalyEmitter = (ElectricalAnomalyEmitter) ObjectPool.EMITTERS.get(ElectricalAnomaly.class);
                            electricalAnomalyEmitter.createBonusAnomaly(consumable.getPosition());
                        } else {
                            if (MathUtils.random(0, 1) == 0) {
                                RadiationAnomalyEmitter radiationAnomalyEmitter = (RadiationAnomalyEmitter) ObjectPool.EMITTERS.get(RadiationAnomaly.class);
                                radiationAnomalyEmitter.createBonusAnomaly(consumable.getPosition());
                            } else {
                                MagneticAnomalyEmitter magneticAnomalyEmitter = (MagneticAnomalyEmitter) ObjectPool.EMITTERS.get(MagneticAnomaly.class);
                                magneticAnomalyEmitter.createBonusAnomaly(consumable.getPosition());
                            }
                        }
                        GameInfoSystem.setup(
                                consumable.getPosition().x, consumable.getPosition().y,
                                GameInfoSystem.Preset.ANOMALY_ARISE.getValue(),
                                5.0f,
                                true
                        );
                        boomBox.playSound(MSConstants.ANOMALIES_BORN);
                        break;
                }
                break;
        }
    }

    private static void print(AbstractConsumable consumable, GameInfoSystem.Preset p) {
        GameInfoSystem.setup(
                consumable.getPosition().x, consumable.getPosition().y,
                (consumable.isGolden() ? "[GOLD]" : "[GREEN]") + p + "[]",
                (consumable.isGolden() ? 7.0f : 3.0f),
                true
        );
    }
}