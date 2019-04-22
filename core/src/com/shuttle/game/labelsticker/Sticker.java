package com.shuttle.game.labelsticker;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.anomaly.AbstractAnomaly;
import com.shuttle.game.bot.consumable.AbstractConsumable;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.player.Standing;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.ui.GameInfoSystem;
import com.shuttle.game.utils.BoundCheck;
import com.shuttle.game.utils.Consumer;

import java.util.Arrays;
import java.util.List;

/**
 * @author Shuttle on 3/27/19.
 */
//todo: gold loot sounds??
public enum Sticker {
    //Crate
    BOOST {
        @Override
        public void consume(GameScreen gs, Vector2 position, boolean isGolden) {
            if (!isGolden) {
                Shuttle player = gs.getPlayer();
                float d = Rules.PLAYER_BOOST_ENGINE_POWER - Shuttle.velocity.len();
                player.setShadowsCoordinates();
                Shuttle.velocity.add(
                        d * (float) Math.cos(Math.toRadians(Shuttle.angle)),
                        d * (float) Math.sin(Math.toRadians(Shuttle.angle))
                );
            }
            GameInfoSystem.setup(
                    position.x, position.y,
                    (isGolden ? "[GOLD]" : "[GREEN]") + GameInfoSystem.Preset.TURBO + "[]",
                    (isGolden ? 7.0f : 3.0f),
                    true
            );
            boomBox.playSound(MSConstants.GET_BOOST);
        }

        @Override
        public void consume(GameScreen gs, AbstractConsumable consumable) {
            consume(gs, consumable.getPosition(), consumable.isGolden());
        }
    },
    REPAIR {
        @Override
        public void consume(GameScreen gs, Vector2 position, boolean isGolden) {
            if (!isGolden) {
                standing.changeHP(Rules.CONSUMABLE_REPAIR_ADD);
            }
            GameInfoSystem.setup(
                    position.x, position.y,
                    (isGolden ? "[GOLD]" : "[GREEN]") + GameInfoSystem.Preset.REPAIR + "[]",
                    (isGolden ? 7.0f : 3.0f),
                    true
            );
            boomBox.playSound(MSConstants.GET_REPAIR);
        }

        @Override
        public void consume(GameScreen gs, AbstractConsumable consumable) {
            consume(gs, consumable.getPosition(), consumable.isGolden());
        }
    },
    SHIELD {
        @Override
        public void consume(GameScreen gs, Vector2 position, boolean isGolden) {
            if (!isGolden) {
                List<AbstractConsumable> list = gs.getCrateEmitter().getActiveList();
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setShielded(true);
                }
                list = gs.getFuelEmitter().getActiveList();
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setShielded(true);
                }
                list = gs.getStorageEmitter().getActiveList();
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setShielded(true);
                }
            }
            GameInfoSystem.setup(
                    position.x, position.y,
                    (isGolden ? "[GOLD]" : "[GREEN]") + GameInfoSystem.Preset.SHIELD + "[]",
                    (isGolden ? 7.0f : 3.0f),
                    true
            );
            boomBox.playSound(MSConstants.GET_SHIELD);
        }

        @Override
        public void consume(GameScreen gs, AbstractConsumable consumable) {
            consume(gs, consumable.getPosition(), consumable.isGolden());
        }
    },
    TRASH {
        @Override
        public void consume(GameScreen gs, Vector2 position, boolean isGolden) {
            int score = Rules.CONSUMABLE_TRASH_BASE_SCORE *
                    ((isGolden) ?
                            Rules.CONSUMABLE_TRASH_GOLDEN_SCORE_FACTOR * MathUtils.random(
                                    Rules.CONSUMABLE_TRASH_GOLDEN_SCORE_MIN,
                                    Rules.CONSUMABLE_TRASH_GOLDEN_SCORE_MAX)
                            : MathUtils.random(
                            Rules.CONSUMABLE_TRASH_GOLDEN_SCORE_MIN,
                            Rules.CONSUMABLE_TRASH_GOLDEN_SCORE_MAX)
                    );
            GameInfoSystem.setup(
                    position.x, position.y,
                    "+" + score + (isGolden ? "[GOLD]" : "[GREEN]") + GameInfoSystem.Preset.TRASH + "[]",
                    (isGolden ? 7.0f : 3.0f),
                    true
            );
            standing.changeScore(score);
            boomBox.playSound(MSConstants.GET_TRASH);
        }

        @Override
        public void consume(GameScreen gs, AbstractConsumable consumable) {
            consume(gs, consumable.getPosition(), consumable.isGolden());
        }
    },
    SPLASH {
        @Override
        public void consume(GameScreen gs, Vector2 position, boolean isGolden) {
            if (!isGolden) {
                List<AbstractAnomaly> anomalies = gs.getCorrosiveAnomalyEmitter().getActiveList();
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
            GameInfoSystem.setup(
                    position.x, position.y,
                    (isGolden ? "[GOLD]" : "[GREEN]") + GameInfoSystem.Preset.SPLASH + "[]",
                    (isGolden ? 7.0f : 3.0f),
                    true
            );
            boomBox.playSound(MSConstants.GET_SPLASH);
        }

        @Override
        public void consume(GameScreen gs, AbstractConsumable consumable) {
            consume(gs, consumable.getPosition(), consumable.isGolden());
        }
    },
    SCAN {
        @Override
        public void consume(GameScreen gs, Vector2 position, boolean isGolden) {
            if (!isGolden) {
                List<AbstractConsumable> list = gs.getCrateEmitter().getActiveList();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isDanger()) {
                        list.get(i).setScanned(true);
                    }
                }
                list = gs.getStorageEmitter().getActiveList();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isDanger()) {
                        list.get(i).setScanned(true);
                    }
                }
            }
            GameInfoSystem.setup(
                    position.x, position.y,
                    (isGolden ? "[GOLD]" : "[GREEN]") + GameInfoSystem.Preset.SCAN + "[]",
                    (isGolden ? 7.0f : 3.0f),
                    true
            );
            boomBox.playSound(MSConstants.GET_SCANNER);
        }

        @Override
        public void consume(GameScreen gs, AbstractConsumable consumable) {
            consume(gs, consumable.getPosition(), consumable.isGolden());
        }
    },
    //Storage
    LASER {
        @Override
        public void consume(GameScreen gs, Vector2 position, boolean isGolden) {
            GameInfoSystem.setup(
                    position.x, position.y,
                    "[GREEN]" + GameInfoSystem.Preset.LASER + "[]",
                    7.0f,
                    true
            );
            gs.getPlayer().setLaser(true);
            Standing.getInstance().setLaserChargeLevel(Standing.getInstance().getMaxLaserChargeLevel());
            boomBox.playSound(MSConstants.GET_LASER);
        }

        @Override
        public void consume(GameScreen gs, AbstractConsumable consumable) {
            consume(gs, consumable.getPosition(), consumable.isGolden());
        }
    },
    RADAR {
        @Override
        public void consume(GameScreen gs, Vector2 position, boolean isGolden) {
            GameInfoSystem.setup(
                    position.x, position.y,
                    "[GREEN]" + GameInfoSystem.Preset.RADAR + "[]",
                    7.0f,
                    true
            );
            boomBox.playSound(MSConstants.GET_RADAR);
            gs.getPlayer().getRadar().setActive(true);
        }

        @Override
        public void consume(GameScreen gs, AbstractConsumable consumable) {
            consume(gs, consumable.getPosition(), consumable.isGolden());
        }
    },
    ROCKETS {
        @Override
        public void consume(GameScreen gs, Vector2 position, boolean isGolden) {
            if (!isGolden) {
                gs.getRocketLauncher().launch();
            }
            GameInfoSystem.setup(
                    position.x, position.y,
                    (isGolden ? "[GOLD]" : "[GREEN]") + GameInfoSystem.Preset.ROCKETS + "[]",
                    (isGolden ? 7.0f : 3.0f),
                    true
            );
            boomBox.playSound(MSConstants.GET_ROCKETS);
        }

        @Override
        public void consume(GameScreen gs, AbstractConsumable consumable) {
            consume(gs, consumable.getPosition(), consumable.isGolden());
        }
    },
    //Fuel
    FUEL {
        @Override
        public void consume(GameScreen gs, Vector2 position, boolean isGolden) {
            float fuel;
            if (isGolden) {
                fuel = Standing.getInstance().getMaxFuel();
            } else {
                fuel = Rules.CONSUMABLE_FUEL_ADD_FACTOR *
                        MathUtils.random(
                                Rules.CONSUMABLE_FUEL_MIN_ADD,
                                Rules.CONSUMABLE_FUEL_MAX_ADD
                        );
            }
            GameInfoSystem.setup(
                    position.x, position.y,
                    GameInfoSystem.Preset.FUEL + (isGolden ? "[GOLD]" : "[GREEN]") + "+" + fuel + "[]",
                    (isGolden ? 7.0f : 3.0f),
                    true
            );
            Standing.getInstance().changeFuel(fuel);
            boomBox.playSound(MSConstants.GET_FUEL);
        }

        @Override
        public void consume(GameScreen gs, AbstractConsumable consumable) {
            consume(gs, consumable.getPosition(), consumable.isGolden());
        }
    },
    FUEL_UI {
        @Override
        public void consume(GameScreen gs, Vector2 position, boolean isGolden) {

        }

        @Override
        public void consume(GameScreen gs, AbstractConsumable consumable) {
            consume(gs, consumable.getPosition(), consumable.isGolden());
        }
    },
    //Surprise
    PATTERN {
        @Override
        public void consume(GameScreen gs, Vector2 position, boolean isGolden) {
        }

        @Override
        public void consume(GameScreen gs, AbstractConsumable consumable) {
            consume(gs, consumable.getPosition(), consumable.isGolden());
        }
    },
    PATTERN_GOLD {
        @Override
        public void consume(GameScreen gs, Vector2 position, boolean isGolden) {
        }

        @Override
        public void consume(GameScreen gs, AbstractConsumable consumable) {
            consume(gs, consumable.getPosition(), consumable.isGolden());
        }
    },
    SHABBY {
        @Override
        public void consume(GameScreen gs, Vector2 position, boolean isGolden) {

        }

        @Override
        public void consume(GameScreen gs, AbstractConsumable consumable) {
            Sticker.consumeShabby(gs, consumable);
        }
    },
    SHABBY_GOLD {
        @Override
        public void consume(GameScreen gs, Vector2 position, boolean isGolden) {

        }

        @Override
        public void consume(GameScreen gs, AbstractConsumable consumable) {
            Sticker.consumeShabby(gs, consumable);
        }
    };
    public static List<Sticker> SELECTABLE = Arrays.asList(BOOST, REPAIR, SHIELD, SPLASH, SCAN, ROCKETS);
    private static BoomBox boomBox;
    private static BoundCheck boundCheck;
    private static Standing standing;
    static {
        boomBox = new BoomBox();
        boundCheck = new BoundCheck();
        standing = Standing.getInstance();
    }

    public abstract void consume(GameScreen gs, Vector2 position, boolean isGolden);

    public abstract void consume(GameScreen gs, AbstractConsumable consumable);

    private static void consumeShabby(GameScreen gs, AbstractConsumable consumable) {
        int r = MathUtils.random(0, 1);
        switch (consumable.getType()) {
            case CRATE:
                if (consumable.isGolden()) {
                    if (r == 0) {
                        getPositive(gs, consumable, 1);
                    } else {
                        getNegative(gs, consumable, 1);
                    }
                } else {
                    if (r == 0) {
                        getPositive(gs, consumable, 0);
                    } else {
                        getNegative(gs, consumable, 0);
                    }
                }
                break;
            case STORAGE:
                if (consumable.isGolden()) {
                    if (r == 0) {
                        getPositive(gs, consumable, 3);
                    } else {
                        getNegative(gs, consumable, 3);
                    }
                } else {
                    if (r == 0) {
                        getPositive(gs, consumable, 2);
                    } else {
                        getNegative(gs, consumable, 2);
                    }
                }
                break;
        }
    }

    public static void getPositive(GameScreen gs, AbstractConsumable consumable, int level) {
        // 0 - Kill related level corrosive anomaly
        // 1 - Add score
        // 2 - Add manual activated loot, or generate Crate/Storage
        // 3 - Kill Fuel/Radiation anomaly, or add HP/life
        int r = MathUtils.random(0, 3);
        int score;
        switch (r) {
            case 0:
                List<AbstractAnomaly> anomalies = gs.getCorrosiveAnomalyEmitter().getActiveList();
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
                            Consumer.consume(gs, Sticker.SELECTABLE.get(i), consumable);
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
                                gs.getCrateEmitter().createBonusAnomaly();
                            }
                            break;
                        case STORAGE:
                            gs.getCrateEmitter().createBonusAnomaly();
                            GameInfoSystem.setup(
                                    consumable.getPosition().x, consumable.getPosition().y,
                                    GameInfoSystem.Preset.BONUS_STORAGE.getValue(),
                                    5.0f,
                                    true
                            );
                            for (int i = 0; i < (level + 1) * Rules.CONSUMABLE_GENERATE_BASE_BONUS; i++) {
                                gs.getStorageEmitter().createBonusAnomaly();
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
                                list = gs.getMagneticAnomalyEmitter().getActiveList();
                                score = Rules.ANOMALIES_MAGNETIC_BONUS_SCORE;
                            } else {
                                list = gs.getRadiationAnomalyEmitter().getActiveList();
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
                            for (int i = 0; i < gs.getMagneticAnomalyEmitter().getActiveList().size(); i++) {
                                AbstractAnomaly anomaly = gs.getMagneticAnomalyEmitter().getActiveList().get(i);
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
                            for (int i = 0; i < gs.getRadiationAnomalyEmitter().getActiveList().size(); i++) {
                                AbstractAnomaly anomaly = gs.getRadiationAnomalyEmitter().getActiveList().get(i);
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

    public static void getNegative(GameScreen gs, AbstractConsumable consumable, int level) {
        // 0 - Add related level corrosive anomaly
        // 1 - Del score
        // 2 - Disable Laser/Radar, or del Crate/Storage
        // 3 - Add Electrical/Corrosive/Radiation anomaly, or del HP/fuel/life
        int r = MathUtils.random(0, 3);
        int score;
        switch (r) {
            case 0:
                gs.getCorrosiveAnomalyEmitter().createBonusAnomaly(consumable.getPosition(), level);
                for (int i = 0; i < (4 - level) * Rules.ANOMALY_GENERATE_BASE_BONUS; i++) {
                    gs.getCorrosiveAnomalyEmitter().createBonusAnomaly(level);
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
                            gs.getPlayer().getRadar().setActive(false);
                            GameInfoSystem.setup(
                                    consumable.getPosition().x, consumable.getPosition().y,
                                    "-[RED]" + GameInfoSystem.Preset.RADAR + "[]",
                                    5.0f,
                                    true
                            );
                        } else {
                            gs.getPlayer().setLaser(false);
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
                            list = gs.getStorageEmitter().getActiveList();
                            preset = GameInfoSystem.Preset.STORAGE_FLUSH;
                        } else {
                            list = gs.getCrateEmitter().getActiveList();
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
                            gs.getElectricalAnomalyEmitter().createBonusAnomaly(consumable.getPosition());
                        } else {
                            if (MathUtils.random(0, 1) == 0) {
                                gs.getMagneticAnomalyEmitter().createBonusAnomaly(consumable.getPosition());
                            } else {
                                gs.getRadiationAnomalyEmitter().createBonusAnomaly(consumable.getPosition());
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
}
