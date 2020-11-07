package com.buran.game.labelsticker;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.buran.game.bot.anomaly.AbstractAnomaly;
import com.buran.game.bot.anomaly.CorrosiveAnomaly;
import com.buran.game.bot.anomaly.CorrosiveAnomalyEmitter;
import com.buran.game.bot.consumable.AbstractConsumable;
import com.buran.game.bot.consumable.Crate;
import com.buran.game.bot.consumable.CrateEmitter;
import com.buran.game.bot.consumable.FuelEmitter;
import com.buran.game.bot.consumable.Storage;
import com.buran.game.bot.consumable.StorageEmitter;
import com.buran.game.bot.consumable.fuel.Fuel;
import com.buran.game.conf.MSConstants;
import com.buran.game.conf.Rules;
import com.buran.game.effects.AsteroidLaunchEmitter;
import com.buran.game.player.Buran;
import com.buran.game.ui.GameInfoSystem;
import com.buran.game.utils.pool.ObjectPool;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

/**
 * @author soloyes on 3/27/19.
 */

public enum Sticker {
    //Crate
    BOOST("[#FFFFFF]BOOST[]\n[#cc6e54]Acceleration - the rate of change of the velocity of the ship with respect to time. When you get this object, you able to activate it from the top storage menu. Danger! When velocity is high, you may get more damage. From the other hand, you able to kill more anomalies.[]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
            if (!isGolden) {
                float d = Rules.PLAYER_BOOST_ENGINE_POWER - Buran.velocity.len();
                Buran.setShadowsCoordinates();
                Buran.velocity.add(
                        d * (float) Math.cos(Math.toRadians(Buran.angle)),
                        d * (float) Math.sin(Math.toRadians(Buran.angle))
                );
            }
        }

        @Override
        public void consume(com.buran.game.bot.consumable.AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
            notify(consumable);
        }

        @Override
        public void notify(com.buran.game.bot.consumable.AbstractConsumable consumable) {
            print(consumable, com.buran.game.ui.GameInfoSystem.Preset.TURBO);
            sound();
        }

        @Override
        public void sound() {
            boomBox.playSound(MSConstants.GET_BOOST);
        }
    },
    REPAIR("[#FFFFFF]REPAIR[]\n[#cc6e54]Using to restore by replacing a part or putting together what is torn or broken. Your ship takes damage when collide with asteroids, black holes, and anomalies. Be aware! Carefully leave the space.[]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
            if (!isGolden) {
                standing.changeHP(Rules.CONSUMABLE_REPAIR_ADD);
            }
        }

        @Override
        public void consume(com.buran.game.bot.consumable.AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
            notify(consumable);
        }

        @Override
        public void notify(com.buran.game.bot.consumable.AbstractConsumable consumable) {
            print(consumable, com.buran.game.ui.GameInfoSystem.Preset.REPAIR);
            sound();
        }

        @Override
        public void sound() {
            boomBox.playSound(MSConstants.GET_REPAIR);
        }
    },
    SHIELD("[#FFFFFF]SHIELD[]\n[#cc6e54]A shield is round a piece of personal armour. Your shield may bite a related color anomaly. Shield should be charged, just do the harvesting and watch around.[]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
            if (!isGolden) {
                CrateEmitter crateEmitter = (CrateEmitter) ObjectPool.EMITTERS.get(Crate.class);
                List<com.buran.game.bot.consumable.AbstractConsumable> list = crateEmitter.getActiveList();
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
        public void consume(com.buran.game.bot.consumable.AbstractConsumable consumable) {
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
            boomBox.playSound(com.buran.game.conf.MSConstants.GET_SHIELD);
        }
    },
    TRASH("[#FFFFFF]TRASH[]\n[#cc6e54]A box like this can give you many interesting possibilities. The main thing is not to be afraid to take it and open it so that you have a chance to earn more points.[]") {
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
            boomBox.playSound(com.buran.game.conf.MSConstants.GET_TRASH);
        }
    },
    SPLASH("[#FFFFFF]SPLASH[]\n[#cc6e54]This item helps you clear the map of anomalies. Not every anomaly can withstand this possibility, so a few experiments will be required.[]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
            if (!isGolden) {
                CorrosiveAnomalyEmitter corrosiveAnomalyEmitter = (CorrosiveAnomalyEmitter) ObjectPool.EMITTERS.get(CorrosiveAnomaly.class);
                List<AbstractAnomaly> anomalies = corrosiveAnomalyEmitter.getActiveList();
                for (int i = 0; i < anomalies.size(); i++) {
                    if (anomalies.get(i).getPosition().dst(Buran.position) <= Rules.WORLD_WIDTH) {
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
    SCAN("[#FFFFFF]SCAN[]\n[#cc6e54]You may find boxes that are difficult to recognize. They can contain anything. Use the scanner to look inside.[]") {
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
    LASER("[#FFFFFF]LASER[]\n[#cc6e54]The ability to destroy asteroids at a distance appears only with a non-zero charge of the laser scale. The purple scale indicates the presence of a laser, and gives you the option of automatic fire.[]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
            Buran.isLaser = true;
            com.buran.game.player.Standing.getInstance().setLaserChargeLevel(com.buran.game.player.Standing.getInstance().getMaxLaserChargeLevel());
        }

        @Override
        public void consume(com.buran.game.bot.consumable.AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
            notify(consumable);
        }

        @Override
        public void notify(com.buran.game.bot.consumable.AbstractConsumable consumable) {
            print(consumable, com.buran.game.ui.GameInfoSystem.Preset.LASER);
            sound();
        }

        @Override
        public void sound() {
            boomBox.playSound(com.buran.game.conf.MSConstants.GET_LASER);
        }
    },
    RADAR("[#FFFFFF]RADAR[]\n[#cc6e54]Our space is a boundless two-dimensional extent in which objects and events have relative position and direction. But it's good when there is a device that shows the location of those objects.[]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
            com.buran.game.player.navigation.Radar.isActive = true;
        }

        @Override
        public void consume(com.buran.game.bot.consumable.AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
            notify(consumable);
        }

        @Override
        public void notify(com.buran.game.bot.consumable.AbstractConsumable consumable) {
            print(consumable, com.buran.game.ui.GameInfoSystem.Preset.RADAR);
            sound();
        }

        @Override
        public void sound() {
            boomBox.playSound(com.buran.game.conf.MSConstants.GET_RADAR);
        }
    },
    ROCKETS("[#FFFFFF]ROCKETS[]\n[#cc6e54]When there is no way out, you can smash everything around. True, powerful weapons take time to recharge. Find ammunition and activate it.[]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
            if (!isGolden) {
                com.buran.game.bot.rocket.RocketLauncher.launch();
            }
        }

        @Override
        public void consume(com.buran.game.bot.consumable.AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
            notify(consumable);
        }

        @Override
        public void notify(com.buran.game.bot.consumable.AbstractConsumable consumable) {
            print(consumable, com.buran.game.ui.GameInfoSystem.Preset.ROCKETS);
            boomBox.playSound(com.buran.game.conf.MSConstants.GET_ROCKETS);
        }

        @Override
        public void sound() {
        }
    },
    //Fuel
    FUEL("[#FFFFFF]FUEL[]\n[#cc6e54]When there is no fuel, the ship cannot fly. But he can still move. True, the speed may not be enough to get away from the attacking anomalies.[]") {
        float fuel;

        @Override
        public void consume(Vector2 position, boolean isGolden) {
            if (isGolden) {
                fuel = com.buran.game.player.Standing.getInstance().getMaxFuel();
            } else {
                fuel = com.buran.game.conf.Rules.CONSUMABLE_FUEL_ADD_FACTOR *
                        MathUtils.random(
                                com.buran.game.conf.Rules.CONSUMABLE_FUEL_MIN_ADD,
                                com.buran.game.conf.Rules.CONSUMABLE_FUEL_MAX_ADD
                        );
            }
            com.buran.game.player.Standing.getInstance().changeFuel(fuel);

        }

        @Override
        public void consume(com.buran.game.bot.consumable.AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
            notify(consumable);
        }

        @Override
        public void notify(com.buran.game.bot.consumable.AbstractConsumable consumable) {
            print(consumable, com.buran.game.ui.GameInfoSystem.Preset.FUEL);
            sound();
        }

        @Override
        public void sound() {
            boomBox.playSound(com.buran.game.conf.MSConstants.GET_FUEL);
        }
    },
    FUEL_UI("[#FFFFFF]FUEL[]\n[#cc6e54]When there is no fuel, the ship cannot fly. But he can still move. True, the speed may not be enough to get away from the attacking anomalies.[]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {

        }

        @Override
        public void consume(com.buran.game.bot.consumable.AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
        }

        @Override
        public void notify(com.buran.game.bot.consumable.AbstractConsumable consumable) {

        }

        @Override
        public void sound() {

        }
    },
    //Surprise
    PATTERN("[#FFFFFF]PATTERN[]\n[#cc6e54]Not all boxes can be labeled. This happens for a variety of reasons.[]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
        }

        @Override
        public void consume(com.buran.game.bot.consumable.AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
        }

        @Override
        public void notify(com.buran.game.bot.consumable.AbstractConsumable consumable) {

        }

        @Override
        public void sound() {

        }
    },
    PATTERN_GOLD("[#FFFFFF]GOLD PATTERN[]\n[#cc6e54]There is a theory that gold boxes have great potential. But this holds great risks for the ship. Like space, the space for the possibilities of the boxes is limitless.[]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
        }

        @Override
        public void consume(com.buran.game.bot.consumable.AbstractConsumable consumable) {
            consume(consumable.getPosition(), consumable.isGolden());
        }

        @Override
        public void notify(com.buran.game.bot.consumable.AbstractConsumable consumable) {

        }

        @Override
        public void sound() {

        }
    },
    SHABBY("[#FFFFFF]SHABBY[]\n[#cc6e54]Space debris, radiation, and other aggressive phenomena completely destroy the ability to scan such boxes. They shouldn't be dangerous, but no one has explored the possibilities of forgotten objects.[]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {

        }

        @Override
        public void consume(com.buran.game.bot.consumable.AbstractConsumable consumable) {
            Sticker.consumeShabby(consumable);
        }

        @Override
        public void notify(com.buran.game.bot.consumable.AbstractConsumable consumable) {

        }

        @Override
        public void sound() {

        }
    },
    SHABBY_GOLD("[#FFFFFF]GOLD SHABBY[]\n[#cc6e54]Our scientists cannot determine what it is.[]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {

        }

        @Override
        public void consume(com.buran.game.bot.consumable.AbstractConsumable consumable) {
            Sticker.consumeShabby(consumable);
        }

        @Override
        public void notify(com.buran.game.bot.consumable.AbstractConsumable consumable) {

        }

        @Override
        public void sound() {

        }
    },
    //Astronaut
    REGULAR("[#FFFFFF]REGULAR[]\n[#cc6e54][]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
        }

        @Override
        public void consume(com.buran.game.bot.consumable.AbstractConsumable consumable) {
        }

        @Override
        public void notify(com.buran.game.bot.consumable.AbstractConsumable consumable) {

        }

        @Override
        public void sound() {

        }
    },
    MECHANIC("[#FFFFFF]MECHANIC[]\n[#cc6e54][]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
        }

        @Override
        public void consume(com.buran.game.bot.consumable.AbstractConsumable consumable) {
        }

        @Override
        public void notify(com.buran.game.bot.consumable.AbstractConsumable consumable) {

        }

        @Override
        public void sound() {

        }
    },
    MEDIC("[#FFFFFF]MEDIC[]\n[#cc6e54][]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
        }

        @Override
        public void consume(com.buran.game.bot.consumable.AbstractConsumable consumable) {
        }

        @Override
        public void notify(com.buran.game.bot.consumable.AbstractConsumable consumable) {

        }

        @Override
        public void sound() {

        }
    },
    MILITARY("[#FFFFFF]MILITARY[]\n[#cc6e54][]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
        }

        @Override
        public void consume(com.buran.game.bot.consumable.AbstractConsumable consumable) {
        }

        @Override
        public void notify(com.buran.game.bot.consumable.AbstractConsumable consumable) {

        }

        @Override
        public void sound() {

        }
    },
    SCIENTIST("[#FFFFFF]SCIENTIST[]\n[#cc6e54][]") {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
        }

        @Override
        public void consume(com.buran.game.bot.consumable.AbstractConsumable consumable) {
        }

        @Override
        public void notify(com.buran.game.bot.consumable.AbstractConsumable consumable) {

        }

        @Override
        public void sound() {

        }
    };

    Sticker(String description) {
        this.description = description;
    }

    public static List<Sticker> SELECTABLE = Arrays.asList(BOOST, REPAIR, SHIELD, SPLASH, SCAN, ROCKETS);
    public static List<Sticker> ASTRONAUTS = Arrays.asList(REGULAR, MEDIC, SCIENTIST, MECHANIC, MILITARY);
    public static List<Sticker> ALL = Arrays.asList(Sticker.values());
    private static com.buran.game.effects.BoomBox boomBox;
    private static com.buran.game.player.Standing standing;
    @Getter
    private String description;

    static {
        boomBox = new com.buran.game.effects.BoomBox();
        standing = com.buran.game.player.Standing.getInstance();
    }

    public abstract void consume(Vector2 position, boolean isGolden);

    public abstract void consume(com.buran.game.bot.consumable.AbstractConsumable consumable);

    public abstract void notify(com.buran.game.bot.consumable.AbstractConsumable consumable);

    public abstract void sound();

    private static void consumeShabby(com.buran.game.bot.consumable.AbstractConsumable consumable) {
        if (MathUtils.random(0, 1) == 0) {
            getPositive(consumable);
        } else {
            getNegative(consumable);
        }
    }

    private static void getPositive(com.buran.game.bot.consumable.AbstractConsumable consumable) {
        // 0 - Kill related level corrosive anomaly
        // 1 - Add score
        // 2 - Add manual activated loot, or generate Crate/Storage
        // 3 - Kill Fuel/Radiation anomaly, or add HP/life
        int level = (consumable.isGolden() ? 1 : 0) + (consumable.getType() == com.buran.game.utils.pool.AbstractPoolable.Type.CRATE ? 0 : 2);
        int r = MathUtils.random(0, 3);
        int score;
        switch (r) {
            case 0:
                com.buran.game.bot.anomaly.CorrosiveAnomalyEmitter corrosiveAnomalyEmitter = (com.buran.game.bot.anomaly.CorrosiveAnomalyEmitter) com.buran.game.utils.pool.ObjectPool.EMITTERS.get(com.buran.game.bot.anomaly.CorrosiveAnomaly.class);
                List<com.buran.game.bot.anomaly.AbstractAnomaly> anomalies = corrosiveAnomalyEmitter.getActiveList();
                for (int i = 0; i < anomalies.size(); i++) {
                    com.buran.game.bot.anomaly.AbstractAnomaly anomaly = anomalies.get(i);
                    if (anomaly.getLevel() == level && !anomaly.isTaken()) {
                        anomaly.setMarkedToBoom(true);
                        com.buran.game.player.Standing.getInstance().changeScore(com.buran.game.conf.Rules.ANOMALIES_BASE_SCORE_ADD * (level + 1));
                    }
                }
                com.buran.game.ui.GameInfoSystem.setup(
                        consumable.getPosition().x, consumable.getPosition().y,
                        com.buran.game.ui.GameInfoSystem.Preset.ANOMALY_FLUSH.getValue(),
                        5.0f,
                        true
                );
                boomBox.playSound(com.buran.game.conf.MSConstants.ANOMALY_FLUSH);
                break;
            case 1:
                score = (level + 1) * com.buran.game.conf.Rules.SCORE_BASE_BONUS;
                com.buran.game.player.Standing.getInstance().changeScore(score);
                com.buran.game.ui.GameInfoSystem.setup(
                        consumable.getPosition().x, consumable.getPosition().y,
                        com.buran.game.ui.GameInfoSystem.Preset.SCORE + "[GREEN] +" + score + "[]",
                        5.0f,
                        true
                );
                boomBox.playSound(com.buran.game.conf.MSConstants.ADD_SCORE);
                break;
            case 2:
                if (consumable.isGolden()) {
                    for (int i = 0; i < Sticker.SELECTABLE.size(); i++) {
                        for (int j = 0; j < com.buran.game.conf.Rules.SELECTOR_SHABBY_BASE_BONUS; j++) {
                            com.buran.game.utils.Consumer.consume(Sticker.SELECTABLE.get(i), consumable);
                        }
                    }
                    com.buran.game.ui.GameInfoSystem.setup(
                            consumable.getPosition().x, consumable.getPosition().y,
                            com.buran.game.ui.GameInfoSystem.Preset.BIG_BONUS.getValue(),
                            5.0f,
                            true
                    );
                    boomBox.playSound(com.buran.game.conf.MSConstants.ADD_SELECTABLE);
                } else {
                    switch (consumable.getType()) {
                        case com.buran.game.utils.pool.AbstractPoolable.Type.CRATE:
                            com.buran.game.ui.GameInfoSystem.setup(
                                    consumable.getPosition().x, consumable.getPosition().y,
                                    com.buran.game.ui.GameInfoSystem.Preset.BONUS_CRATE.getValue(),
                                    5.0f,
                                    true
                            );
                            for (int i = 0; i < (level + 1) * com.buran.game.conf.Rules.CONSUMABLE_GENERATE_BASE_BONUS; i++) {
                                com.buran.game.bot.consumable.CrateEmitter crateEmitter = (com.buran.game.bot.consumable.CrateEmitter) com.buran.game.utils.pool.ObjectPool.EMITTERS.get(com.buran.game.bot.consumable.Crate.class);
                                crateEmitter.createBonusCrate();
                            }
                            break;
                        case com.buran.game.utils.pool.AbstractPoolable.Type.STORAGE:
                            com.buran.game.bot.consumable.StorageEmitter storageEmitter = (com.buran.game.bot.consumable.StorageEmitter) com.buran.game.utils.pool.ObjectPool.EMITTERS.get(com.buran.game.bot.consumable.Storage.class);
                            com.buran.game.ui.GameInfoSystem.setup(
                                    consumable.getPosition().x, consumable.getPosition().y,
                                    com.buran.game.ui.GameInfoSystem.Preset.BONUS_STORAGE.getValue(),
                                    5.0f,
                                    true
                            );
                            for (int i = 0; i < (level + 1) * com.buran.game.conf.Rules.CONSUMABLE_GENERATE_BASE_BONUS; i++) {
                                storageEmitter.createBonusStorage();
                            }
                            break;
                    }
                    boomBox.playSound(com.buran.game.conf.MSConstants.GENERATE_CRATE_STORAGE);
                }
                break;
            case 3:
                if (consumable.isGolden()) {
                    switch (consumable.getType()) {
                        case com.buran.game.utils.pool.AbstractPoolable.Type.CRATE:
                            com.buran.game.player.Standing.getInstance().changeHP(com.buran.game.player.Standing.getInstance().getMaxHp());
                            boomBox.playSound(com.buran.game.conf.MSConstants.ADD_HP);
                            break;
                        case com.buran.game.utils.pool.AbstractPoolable.Type.STORAGE:
                            com.buran.game.player.Standing.getInstance().changeHP(com.buran.game.player.Standing.getInstance().getMaxHp());
                            com.buran.game.player.Standing.getInstance().changeLives(1);
                            boomBox.playSound(com.buran.game.conf.MSConstants.UP);
                            break;
                    }
                } else {
                    switch (consumable.getType()) {
                        case com.buran.game.utils.pool.AbstractPoolable.Type.CRATE:
                            List<com.buran.game.bot.anomaly.AbstractAnomaly> list;
                            if (MathUtils.random(0, 1) == 0) {
                                com.buran.game.bot.anomaly.MagneticAnomalyEmitter magneticAnomalyEmitter = (com.buran.game.bot.anomaly.MagneticAnomalyEmitter) com.buran.game.utils.pool.ObjectPool.EMITTERS.get(com.buran.game.bot.anomaly.MagneticAnomaly.class);
                                list = magneticAnomalyEmitter.getActiveList();
                                score = com.buran.game.conf.Rules.ANOMALIES_MAGNETIC_BONUS_SCORE;
                            } else {
                                com.buran.game.bot.anomaly.RadiationAnomalyEmitter radiationAnomalyEmitter = (com.buran.game.bot.anomaly.RadiationAnomalyEmitter) com.buran.game.utils.pool.ObjectPool.EMITTERS.get(com.buran.game.bot.anomaly.RadiationAnomaly.class);
                                list = radiationAnomalyEmitter.getActiveList();
                                score = com.buran.game.conf.Rules.ANOMALIES_RADIATION_BONUS_SCORE;
                            }
                            for (int i = 0; i < list.size(); i++) {
                                com.buran.game.bot.anomaly.AbstractAnomaly anomaly = list.get(i);
                                if (!anomaly.isTaken()) {
                                    list.get(i).destroyWithEffect();
                                    com.buran.game.ui.GameInfoSystem.setup(
                                            anomaly.getPosition().x, anomaly.getPosition().y,
                                            com.buran.game.ui.GameInfoSystem.Preset.SCORE + "[GREEN] +" + score + "[]",
                                            5.0f,
                                            true
                                    );
                                }
                            }
                            break;
                        case com.buran.game.utils.pool.AbstractPoolable.Type.STORAGE:
                            score = com.buran.game.conf.Rules.ANOMALIES_MAGNETIC_BONUS_SCORE;
                            com.buran.game.bot.anomaly.MagneticAnomalyEmitter magneticAnomalyEmitter = (com.buran.game.bot.anomaly.MagneticAnomalyEmitter) com.buran.game.utils.pool.ObjectPool.EMITTERS.get(com.buran.game.bot.anomaly.MagneticAnomaly.class);
                            list = magneticAnomalyEmitter.getActiveList();
                            for (int i = 0; i < list.size(); i++) {
                                com.buran.game.bot.anomaly.AbstractAnomaly anomaly = list.get(i);
                                if (!anomaly.isTaken()) {
                                    anomaly.destroyWithEffect();
                                    com.buran.game.ui.GameInfoSystem.setup(
                                            anomaly.getPosition().x, anomaly.getPosition().y,
                                            com.buran.game.ui.GameInfoSystem.Preset.SCORE + "[GREEN] +" + score + "[]",
                                            5.0f,
                                            true
                                    );
                                }
                            }
                            score = com.buran.game.conf.Rules.ANOMALIES_RADIATION_BONUS_SCORE;
                            com.buran.game.bot.anomaly.RadiationAnomalyEmitter radiationAnomalyEmitter = (com.buran.game.bot.anomaly.RadiationAnomalyEmitter) com.buran.game.utils.pool.ObjectPool.EMITTERS.get(com.buran.game.bot.anomaly.RadiationAnomaly.class);
                            list = radiationAnomalyEmitter.getActiveList();
                            for (int i = 0; i < list.size(); i++) {
                                com.buran.game.bot.anomaly.AbstractAnomaly anomaly = list.get(i);
                                if (!anomaly.isTaken()) {
                                    anomaly.destroyWithEffect();
                                    com.buran.game.ui.GameInfoSystem.setup(
                                            anomaly.getPosition().x, anomaly.getPosition().y,
                                            com.buran.game.ui.GameInfoSystem.Preset.SCORE + "[GREEN] +" + score + "[]",
                                            5.0f,
                                            true
                                    );
                                }
                            }
                            break;
                    }
                    com.buran.game.ui.GameInfoSystem.setup(
                            consumable.getPosition().x, consumable.getPosition().y,
                            com.buran.game.ui.GameInfoSystem.Preset.ANOMALY_FLUSH.getValue(),
                            5.0f,
                            true
                    );
                    boomBox.playSound(com.buran.game.conf.MSConstants.KILL_MAGNETIC_RADIATION);
                    break;
                }
        }
    }

    private static void getNegative(com.buran.game.bot.consumable.AbstractConsumable consumable) {
        // 0 - Add related level corrosive anomaly
        // 1 - Del score
        // 2 - Disable Laser/Radar, or del Crate/Storage
        // 3 - Add Electrical/Corrosive/Radiation anomaly, or del HP/fuel/life
        int level = (consumable.isGolden() ? 1 : 0) + (consumable.getType() == com.buran.game.utils.pool.AbstractPoolable.Type.CRATE ? 0 : 2);
        int r = MathUtils.random(0, 3);
        int score;
        switch (r) {
            case 0:
                com.buran.game.bot.anomaly.CorrosiveAnomalyEmitter corrosiveAnomalyEmitter = (com.buran.game.bot.anomaly.CorrosiveAnomalyEmitter) com.buran.game.utils.pool.ObjectPool.EMITTERS.get(com.buran.game.bot.anomaly.CorrosiveAnomaly.class);
                corrosiveAnomalyEmitter.createBonusAnomaly(consumable.getPosition(), level);
                for (int i = 0; i < (4 - level) * com.buran.game.conf.Rules.ANOMALY_GENERATE_BASE_BONUS; i++) {
                    corrosiveAnomalyEmitter.createBonusAnomaly(level);
                }
                com.buran.game.ui.GameInfoSystem.setup(
                        consumable.getPosition().x, consumable.getPosition().y,
                        com.buran.game.ui.GameInfoSystem.Preset.ANOMALY_ARISE.getValue(),
                        5.0f,
                        true
                );
                boomBox.playSound(com.buran.game.conf.MSConstants.ADD_CORROSIVE);
                break;
            case 1:
                score = (level + 1) * com.buran.game.conf.Rules.SCORE_BASE_BONUS;
                com.buran.game.player.Standing.getInstance().changeScore(-score);
                com.buran.game.ui.GameInfoSystem.setup(
                        consumable.getPosition().x, consumable.getPosition().y,
                        com.buran.game.ui.GameInfoSystem.Preset.SCORE + "-[RED]" + score + "[]",
                        5.0f,
                        true
                );
                boomBox.playSound(com.buran.game.conf.MSConstants.DEL_SCORE);
                break;
            case 2:
                switch (consumable.getType()) {
                    case com.buran.game.utils.pool.AbstractPoolable.Type.CRATE:
                        if (consumable.isGolden()) {
                            com.buran.game.player.navigation.Radar.isActive = false;
                            com.buran.game.ui.GameInfoSystem.setup(
                                    consumable.getPosition().x, consumable.getPosition().y,
                                    "-[RED]" + com.buran.game.ui.GameInfoSystem.Preset.RADAR + "[]",
                                    5.0f,
                                    true
                            );
                        } else {
                            Buran.isLaser = false;
                            com.buran.game.ui.GameInfoSystem.setup(
                                    consumable.getPosition().x, consumable.getPosition().y,
                                    "-[RED]" + com.buran.game.ui.GameInfoSystem.Preset.LASER + "[]",
                                    5.0f,
                                    true
                            );
                        }
                        boomBox.playSound(com.buran.game.conf.MSConstants.DISABLE_LASER_RADAR);
                        break;
                    case com.buran.game.utils.pool.AbstractPoolable.Type.STORAGE:
                        List<com.buran.game.bot.consumable.AbstractConsumable> list;
                        com.buran.game.ui.GameInfoSystem.Preset preset;
                        if (consumable.isGolden()) {
                            com.buran.game.bot.consumable.StorageEmitter storageEmitter = (com.buran.game.bot.consumable.StorageEmitter) com.buran.game.utils.pool.ObjectPool.EMITTERS.get(com.buran.game.bot.consumable.Storage.class);
                            list = storageEmitter.getActiveList();
                            preset = com.buran.game.ui.GameInfoSystem.Preset.STORAGE_FLUSH;
                        } else {
                            com.buran.game.bot.consumable.CrateEmitter crateEmitter = (com.buran.game.bot.consumable.CrateEmitter) com.buran.game.utils.pool.ObjectPool.EMITTERS.get(com.buran.game.bot.consumable.Crate.class);
                            list = crateEmitter.getActiveList();
                            preset = com.buran.game.ui.GameInfoSystem.Preset.CRATE_FLUSH;
                        }

                        for (int i = 0; i < list.size(); i++) {
                            com.buran.game.bot.consumable.AbstractConsumable c = list.get(i);
                            if (!c.isTaken()) {
                                c.destroyWithEffect();
                            }
                        }
                        com.buran.game.ui.GameInfoSystem.setup(
                                consumable.getPosition().x, consumable.getPosition().y,
                                preset.getValue(),
                                5.0f,
                                true
                        );
                        boomBox.playSound(com.buran.game.conf.MSConstants.DEL_CRATE_STORAGE);
                        break;
                }
                break;
            case 3:
                switch (consumable.getType()) {
                    case com.buran.game.utils.pool.AbstractPoolable.Type.CRATE:
                        if (consumable.isGolden()) {
                            com.buran.game.player.Standing.getInstance().changeLives(-1);
                            com.buran.game.ui.GameInfoSystem.setup(
                                    consumable.getPosition().x, consumable.getPosition().y,
                                    "-[RED]" + com.buran.game.ui.GameInfoSystem.Preset.UP + "[]",
                                    5.0f,
                                    true
                            );
                            boomBox.playSound(com.buran.game.conf.MSConstants.DOWN);
                        } else {
                            if (MathUtils.random(0, 1) == 0) {
                                com.buran.game.player.Standing.getInstance().changeHP(-com.buran.game.player.Standing.getInstance().getHp() + 1);
                                com.buran.game.ui.GameInfoSystem.setup(
                                        consumable.getPosition().x, consumable.getPosition().y,
                                        "-[RED]" + com.buran.game.ui.GameInfoSystem.Preset.HP + "[]",
                                        5.0f,
                                        true
                                );
                            } else {
                                com.buran.game.player.Standing.getInstance().changeFuel(-com.buran.game.player.Standing.getInstance().getFuel());
                                com.buran.game.ui.GameInfoSystem.setup(
                                        consumable.getPosition().x, consumable.getPosition().y,
                                        "-[RED]" + com.buran.game.ui.GameInfoSystem.Preset.FUEL + "[]",
                                        5.0f,
                                        true
                                );
                            }
                            boomBox.playSound(com.buran.game.conf.MSConstants.DEL_HP_FUEL);
                        }
                        break;
                    case com.buran.game.utils.pool.AbstractPoolable.Type.STORAGE:
                        if (consumable.isGolden()) {
                            com.buran.game.bot.anomaly.ElectricalAnomalyEmitter electricalAnomalyEmitter = (com.buran.game.bot.anomaly.ElectricalAnomalyEmitter) com.buran.game.utils.pool.ObjectPool.EMITTERS.get(com.buran.game.bot.anomaly.ElectricalAnomaly.class);
                            electricalAnomalyEmitter.createBonusAnomaly(consumable.getPosition());
                        } else {
                            if (MathUtils.random(0, 1) == 0) {
                                com.buran.game.bot.anomaly.RadiationAnomalyEmitter radiationAnomalyEmitter = (com.buran.game.bot.anomaly.RadiationAnomalyEmitter) com.buran.game.utils.pool.ObjectPool.EMITTERS.get(com.buran.game.bot.anomaly.RadiationAnomaly.class);
                                radiationAnomalyEmitter.createBonusAnomaly(consumable.getPosition());
                            } else {
                                com.buran.game.bot.anomaly.MagneticAnomalyEmitter magneticAnomalyEmitter = (com.buran.game.bot.anomaly.MagneticAnomalyEmitter) com.buran.game.utils.pool.ObjectPool.EMITTERS.get(com.buran.game.bot.anomaly.MagneticAnomaly.class);
                                magneticAnomalyEmitter.createBonusAnomaly(consumable.getPosition());
                            }
                        }
                        com.buran.game.ui.GameInfoSystem.setup(
                                consumable.getPosition().x, consumable.getPosition().y,
                                com.buran.game.ui.GameInfoSystem.Preset.ANOMALY_ARISE.getValue(),
                                5.0f,
                                true
                        );
                        boomBox.playSound(com.buran.game.conf.MSConstants.ANOMALIES_BORN);
                        break;
                }
                break;
        }
    }

    private static void print(com.buran.game.bot.consumable.AbstractConsumable consumable, com.buran.game.ui.GameInfoSystem.Preset p) {
        com.buran.game.ui.GameInfoSystem.setup(
                consumable.getPosition().x, consumable.getPosition().y,
                (consumable.isGolden() ? "[GOLD]" : "[GREEN]") + p + "[]",
                (consumable.isGolden() ? 7.0f : 3.0f),
                true
        );
    }
}