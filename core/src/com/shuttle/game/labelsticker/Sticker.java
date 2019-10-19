package com.shuttle.game.labelsticker;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.anomaly.AbstractAnomaly;
import com.shuttle.game.bot.anomaly.CorrosiveAnomalyEmitter;
import com.shuttle.game.bot.anomaly.ElectricalAnomalyEmitter;
import com.shuttle.game.bot.anomaly.MagneticAnomalyEmitter;
import com.shuttle.game.bot.anomaly.RadiationAnomalyEmitter;
import com.shuttle.game.bot.consumable.AbstractConsumable;
import com.shuttle.game.bot.consumable.CrateEmitter;
import com.shuttle.game.bot.consumable.FuelEmitter;
import com.shuttle.game.bot.consumable.StorageEmitter;
import com.shuttle.game.bot.rocket.RocketLauncher;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.player.Standing;
import com.shuttle.game.player.navigation.Radar;
import com.shuttle.game.ui.GameInfoSystem;
import com.shuttle.game.ui.groups.configs.RightGroup;
import com.shuttle.game.utils.BoundCheck;
import com.shuttle.game.utils.Consumer;
import com.shuttle.game.utils.pool.AbstractPoolable;

import java.util.Arrays;
import java.util.List;

/**
 * @author Shuttle on 3/27/19.
 */
//todo: gold loot sounds??
//todo: рефакториг Sticker, и удалить level только в Corrosive (Results повлияет)
//todo: Для золотых коробок надо увеличить объем добавленного лута. Это основное отличие, помимо того, что в детсвом режмипе
//оно просто добавляется в инвентарь.
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
                List<AbstractConsumable> list = CrateEmitter.getInstance().getActiveList();
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setShielded(true);
                }
                list = FuelEmitter.getInstance().getActiveList();
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setShielded(true);
                }
                list = StorageEmitter.getInstance().getActiveList();
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
            score = Rules.CONSUMABLE_TRASH_BASE_SCORE *
                    ((isGolden) ?
                            Rules.CONSUMABLE_TRASH_GOLDEN_SCORE_FACTOR * MathUtils.random(
                                    Rules.CONSUMABLE_TRASH_GOLDEN_SCORE_MIN,
                                    Rules.CONSUMABLE_TRASH_GOLDEN_SCORE_MAX)
                            : MathUtils.random(
                            Rules.CONSUMABLE_TRASH_GOLDEN_SCORE_MIN,
                            Rules.CONSUMABLE_TRASH_GOLDEN_SCORE_MAX)
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
            boomBox.playSound(MSConstants.GET_TRASH);
        }
    },
    SPLASH {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
            if (!isGolden) {
                List<AbstractAnomaly> anomalies = CorrosiveAnomalyEmitter.getInstance().getActiveList();
                for (int i = 0; i < anomalies.size(); i++) {
                    if (boundCheck.checkAndHandleBoundsInCamera(anomalies.get(i))) {
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
                List<AbstractConsumable> list = CrateEmitter.getInstance().getActiveList();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isDanger()) {
                        list.get(i).setScanned(true);
                    }
                }
                list = StorageEmitter.getInstance().getActiveList();
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
                RocketLauncher.getInstance().launch();
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
            RightGroup.getInstance().update(this);
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
    }, MECHANIC {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
            RightGroup.getInstance().update(this);
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
    }, MEDIC {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
            RightGroup.getInstance().update(this);
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
    }, MILITARY {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
            RightGroup.getInstance().update(this);
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
    }, SCIENTIST {
        @Override
        public void consume(Vector2 position, boolean isGolden) {
            RightGroup.getInstance().update(this);
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
    };
    public static List<Sticker> SELECTABLE = Arrays.asList(BOOST, REPAIR, SHIELD, SPLASH, SCAN, ROCKETS);
    public static List<Sticker> ASTRONAUTS = Arrays.asList(REGULAR, MEDIC, SCIENTIST, MECHANIC, MILITARY);
    private static BoomBox boomBox;
    private static BoundCheck boundCheck;
    private static Standing standing;
    static {
        boomBox = new BoomBox();
        boundCheck = new BoundCheck();
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
        int level = Integer.parseInt(String.valueOf(consumable.isGolden() ? "1" : "0") + String.valueOf(consumable.getType() == AbstractPoolable.Type.CRATE ? "0" : "1"));
        int r = MathUtils.random(0, 3);
        int score;
        switch (r) {
            case 0:
                List<AbstractAnomaly> anomalies = CorrosiveAnomalyEmitter.getInstance().getActiveList();
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
                                CrateEmitter.getInstance().createBonusAnomaly();
                            }
                            break;
                        case STORAGE:
                            CrateEmitter.getInstance().createBonusAnomaly();
                            GameInfoSystem.setup(
                                    consumable.getPosition().x, consumable.getPosition().y,
                                    GameInfoSystem.Preset.BONUS_STORAGE.getValue(),
                                    5.0f,
                                    true
                            );
                            for (int i = 0; i < (level + 1) * Rules.CONSUMABLE_GENERATE_BASE_BONUS; i++) {
                                StorageEmitter.getInstance().createBonusAnomaly();
                            }
                            break;
                    }
                }
                break;
            case 3:
                if (consumable.isGolden()) {
                    switch (consumable.getType()) {
                        case CRATE:
                            Standing.getInstance().changeHP(Standing.getInstance().getMaxHp());
                            break;
                        case STORAGE:
                            Standing.getInstance().changeHP(Standing.getInstance().getMaxHp());
                            Standing.getInstance().changeLives(1);
                            break;
                    }
                } else {
                    switch (consumable.getType()) {
                        case CRATE:
                            List<AbstractAnomaly> list;
                            if (MathUtils.random(0, 1) == 0) {
                                list = MagneticAnomalyEmitter.getInstance().getActiveList();
                                score = Rules.ANOMALIES_MAGNETIC_BONUS_SCORE;
                            } else {
                                list = RadiationAnomalyEmitter.getInstance().getActiveList();
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
                            for (int i = 0; i < MagneticAnomalyEmitter.getInstance().getActiveList().size(); i++) {
                                AbstractAnomaly anomaly = MagneticAnomalyEmitter.getInstance().getActiveList().get(i);
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
                            for (int i = 0; i < RadiationAnomalyEmitter.getInstance().getActiveList().size(); i++) {
                                AbstractAnomaly anomaly = RadiationAnomalyEmitter.getInstance().getActiveList().get(i);
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
                    break;
                }
        }
    }

    private static void getNegative(AbstractConsumable consumable) {
        // 0 - Add related level corrosive anomaly
        // 1 - Del score
        // 2 - Disable Laser/Radar, or del Crate/Storage
        // 3 - Add Electrical/Corrosive/Radiation anomaly, or del HP/fuel/life
        int level = Integer.parseInt(String.valueOf(consumable.isGolden() ? "1" : "0") + String.valueOf(consumable.getType() == AbstractPoolable.Type.CRATE ? "0" : "1"));
        int r = MathUtils.random(0, 3);
        int score;
        switch (r) {
            case 0:
                CorrosiveAnomalyEmitter.getInstance().createBonusAnomaly(consumable.getPosition(), level);
                for (int i = 0; i < (4 - level) * Rules.ANOMALY_GENERATE_BASE_BONUS; i++) {
                    CorrosiveAnomalyEmitter.getInstance().createBonusAnomaly(level);
                }
                GameInfoSystem.setup(
                        consumable.getPosition().x, consumable.getPosition().y,
                        GameInfoSystem.Preset.ANOMALY_ARISE.getValue(),
                        5.0f,
                        true
                );
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
                        break;
                    case STORAGE:
                        List<AbstractConsumable> list;
                        GameInfoSystem.Preset preset;
                        if (consumable.isGolden()) {
                            list = StorageEmitter.getInstance().getActiveList();
                            preset = GameInfoSystem.Preset.STORAGE_FLUSH;
                        } else {
                            list = CrateEmitter.getInstance().getActiveList();
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
                        }
                        break;
                    case STORAGE:
                        if (consumable.isGolden()) {
                            ElectricalAnomalyEmitter.getInstance().createBonusAnomaly(consumable.getPosition());
                        } else {
                            if (MathUtils.random(0, 1) == 0) {
                                RadiationAnomalyEmitter.getInstance().createBonusAnomaly(consumable.getPosition());
                            } else {
                                MagneticAnomalyEmitter.getInstance().createBonusAnomaly(consumable.getPosition());
                            }
                        }
                        GameInfoSystem.setup(
                                consumable.getPosition().x, consumable.getPosition().y,
                                GameInfoSystem.Preset.ANOMALY_ARISE.getValue(),
                                5.0f,
                                true
                        );
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