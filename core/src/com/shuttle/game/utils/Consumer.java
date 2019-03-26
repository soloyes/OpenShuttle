package com.shuttle.game.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.bot.anomaly.AbstractAnomaly;
import com.shuttle.game.bot.consumable.AbstractConsumable;
import com.shuttle.game.bot.consumable.Label;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.player.Standing;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.ui.GameInfoSystem;

import java.util.List;

public class Consumer {
    public static void consume(GameScreen gs, AbstractConsumable consumable, Label label) {
        //todo: gold loot sounds??
        //todo: gold loot, gold Trash?
        //todo: avoid gold show some, while move to select
        Standing standing = Standing.getInstance();
        Shuttle player = gs.getPlayer();
        Vector2 position = consumable.getPosition();
        BoomBox boomBox = consumable.getBoomBox();
        BoundCheck boundCheck = consumable.getBoundCheck();

        if (label.getSticker() == Label.Sticker.REPAIR) {
            standing.changeHP(Rules.CONSUMABLE_REPAIR_ADD);
            GameInfoSystem.setup(
                    position.x, position.y,
                    GameInfoSystem.Preset.REPAIR,
                    3.0f,
                    true
            );
            boomBox.playSound(MSConstants.GET_REPAIR);
        }

        if (label.getSticker() == Label.Sticker.BOOST) {
            float d = Rules.PLAYER_BOOST_ENGINE_POWER - Shuttle.velocity.len();
            player.setShadowsCoordinates();
            Shuttle.velocity.add(
                    d * (float) Math.cos(Math.toRadians(player.getAngle())),
                    d * (float) Math.sin(Math.toRadians(player.getAngle()))
            );
            GameInfoSystem.setup(
                    position.x, position.y,
                    GameInfoSystem.Preset.TURBO,
                    5.0f,
                    true
            );
            boomBox.playSound(MSConstants.GET_BOOST);
        }

        if (label.getSticker() == Label.Sticker.SPLASH) {
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
            GameInfoSystem.setup(
                    position.x, position.y,
                    GameInfoSystem.Preset.SPLASH,
                    8.0f,
                    true
            );
            boomBox.playSound(MSConstants.GET_SPLASH);
        }

        if (label.getSticker() == Label.Sticker.TRASH) {
            int score = Rules.CONSUMABLE_TRASH_BASE_SCORE * MathUtils.random(0, 5);
            GameInfoSystem.setup(
                    position.x, position.y,
                    "+" + score + "[YELLOW]" + GameInfoSystem.Preset.TRASH + "[]",
                    2.0f,
                    true
            );
            standing.changeScore(score);
            boomBox.playSound(MSConstants.GET_TRASH);
        }

        if (label.getSticker() == Label.Sticker.SHIELD) {
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
            GameInfoSystem.setup(
                    position.x, position.y,
                    GameInfoSystem.Preset.SHIELD,
                    2.0f,
                    true
            );
            boomBox.playSound(MSConstants.GET_SHIELD);
        }

        if (label.getSticker() == Label.Sticker.SCAN) {
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
            GameInfoSystem.setup(
                    position.x, position.y,
                    GameInfoSystem.Preset.SCAN,
                    2.0f,
                    true
            );
            boomBox.playSound(MSConstants.GET_SCANNER);
        }

        if (label.getSticker() == Label.Sticker.RADAR) {
            GameInfoSystem.setup(
                    position.x, position.y,
                    "[GREEN]" + GameInfoSystem.Preset.RADAR + "[]",
                    3.0f,
                    true
            );
            boomBox.playSound(MSConstants.GET_RADAR);
            gs.getPlayer().getRadar().setActive(true);
        }

        if (label.getSticker() == Label.Sticker.LASER) {
            GameInfoSystem.setup(
                    position.x, position.y,
                    "[GREEN]" + GameInfoSystem.Preset.LASER + "[]",
                    3.0f,
                    true
            );
            gs.getPlayer().setLaser(true);
            Standing.getInstance().setLaserChargeLevel(Standing.getInstance().getMaxLaserChargeLevel());
            boomBox.playSound(MSConstants.GET_LASER);
        }

        if (label.getSticker() == Label.Sticker.ROCKETS) {
            GameInfoSystem.setup(
                    position.x, position.y,
                    GameInfoSystem.Preset.ROCKETS,
                    3.0f,
                    true
            );
            boomBox.playSound(MSConstants.GET_ROCKETS);
            gs.getRocketLauncher().launch();
        }

        if (label.getSticker() == Label.Sticker.FUEL) {
            float fuel;
            if (consumable.isGolden()) {
                fuel = Standing.getInstance().getMaxFuel();
                GameInfoSystem.setup(
                        position.x, position.y,
                        GameInfoSystem.Preset.FUEL + "[GOLD] +" + fuel + "[]",
                        5.0f,
                        true
                );
            } else {
                fuel = Rules.CONSUMABLE_FUEL_ADD_FACTOR * MathUtils.random(Rules.CONSUMABLE_FUEL_MIN_ADD, Rules.CONSUMABLE_FUEL_MAX_ADD);
                GameInfoSystem.setup(
                        position.x, position.y,
                        GameInfoSystem.Preset.FUEL + "[GREEN] +" + fuel + "[]",
                        3.0f,
                        true
                );
            }
            Standing.getInstance().changeFuel(fuel);
            boomBox.playSound(MSConstants.GET_FUEL);
        }

        if (label.getSticker() == Label.Sticker.SHABBY || label.getSticker() == Label.Sticker.SHABBY_GOLD) {
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
        addToUI(gs, consumable, label);
        addToSelector(gs, consumable, label);
    }

    private static void getPositive(GameScreen gs, AbstractConsumable consumable, int level) {
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
                        consumable.getPosition().x,
                        consumable.getPosition().y,
                        GameInfoSystem.Preset.ANOMALY_FLUSH.getValue(),
                        3.0f,
                        true
                );
                break;
            case 1:
                score = (level + 1) * Rules.SCORE_BASE_BONUS;
                Standing.getInstance().changeScore(score);
                GameInfoSystem.setup(
                        consumable.getPosition().x,
                        consumable.getPosition().y,
                        GameInfoSystem.Preset.SCORE + "[GREEN] +" + score + "[]",
                        3.0f,
                        true
                );
                break;
            case 2:
                if (consumable.isGolden()) {
                    //todo://Add manual activated loot
                } else {
                    switch (consumable.getType()) {
                        case CRATE:
                            GameInfoSystem.setup(
                                    consumable.getPosition().x,
                                    consumable.getPosition().y,
                                    GameInfoSystem.Preset.BONUS_CRATE.getValue(),
                                    3.0f,
                                    true
                            );
                            for (int i = 0; i < (level + 1) * Rules.CONSUMABLE_GENERATE_BASE_BONUS; i++) {
                                gs.getCrateEmitter().createBonusAnomaly();
                            }
                            break;
                        case STORAGE:
                            gs.getCrateEmitter().createBonusAnomaly();
                            GameInfoSystem.setup(
                                    consumable.getPosition().x,
                                    consumable.getPosition().y,
                                    GameInfoSystem.Preset.BONUS_STORAGE.getValue(),
                                    3.0f,
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
                                            anomaly.getPosition().x,
                                            anomaly.getPosition().y,
                                            GameInfoSystem.Preset.SCORE + "[GREEN] +" + score + "[]",
                                            3.0f,
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
                                            anomaly.getPosition().x,
                                            anomaly.getPosition().y,
                                            GameInfoSystem.Preset.SCORE + "[GREEN] +" + score + "[]",
                                            3.0f,
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
                                            anomaly.getPosition().x,
                                            anomaly.getPosition().y,
                                            GameInfoSystem.Preset.SCORE + "[GREEN] +" + score + "[]",
                                            3.0f,
                                            true
                                    );
                                }
                            }
                            break;
                    }
                    GameInfoSystem.setup(
                            consumable.getPosition().x,
                            consumable.getPosition().y,
                            GameInfoSystem.Preset.ANOMALY_FLUSH.getValue(),
                            3.0f,
                            true
                    );
                    break;
                }
        }
    }

    private static void getNegative(GameScreen gs, AbstractConsumable consumable, int level) {
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
                        consumable.getPosition().x,
                        consumable.getPosition().y,
                        GameInfoSystem.Preset.ANOMALY_ARISE.getValue(),
                        3.0f,
                        true
                );
                break;
            case 1:
                score = (level + 1) * Rules.SCORE_BASE_BONUS;
                Standing.getInstance().changeScore(-score);
                GameInfoSystem.setup(
                        consumable.getPosition().x,
                        consumable.getPosition().y,
                        GameInfoSystem.Preset.SCORE + "-[RED]" + score + "[]",
                        3.0f,
                        true
                );
                break;
            case 2:
                switch (consumable.getType()) {
                    case CRATE:
                        if (consumable.isGolden()) {
                            gs.getPlayer().getRadar().setActive(false);
                            GameInfoSystem.setup(
                                    consumable.getPosition().x,
                                    consumable.getPosition().y,
                                    "-[RED]" + GameInfoSystem.Preset.RADAR + "[]",
                                    3.0f,
                                    true
                            );
                        } else {
                            gs.getPlayer().setLaser(false);
                            GameInfoSystem.setup(
                                    consumable.getPosition().x,
                                    consumable.getPosition().y,
                                    "-[RED]" + GameInfoSystem.Preset.LASER + "[]",
                                    3.0f,
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
                                consumable.getPosition().x,
                                consumable.getPosition().y,
                                preset.getValue(),
                                3.0f,
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
                                    consumable.getPosition().x,
                                    consumable.getPosition().y,
                                    "-[RED]" + GameInfoSystem.Preset.UP + "[]",
                                    3.0f,
                                    true
                            );
                        } else {
                            if (MathUtils.random(0, 1) == 0) {
                                Standing.getInstance().changeHP(-Standing.getInstance().getHp() + 1);
                                GameInfoSystem.setup(
                                        consumable.getPosition().x,
                                        consumable.getPosition().y,
                                        "-[RED]" + GameInfoSystem.Preset.HP + "[]",
                                        3.0f,
                                        true
                                );
                            } else {
                                Standing.getInstance().changeFuel(-Standing.getInstance().getFuel());
                                GameInfoSystem.setup(
                                        consumable.getPosition().x,
                                        consumable.getPosition().y,
                                        "-[RED]" + GameInfoSystem.Preset.FUEL + "[]",
                                        3.0f,
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
                                consumable.getPosition().x,
                                consumable.getPosition().y,
                                GameInfoSystem.Preset.ANOMALY_ARISE.getValue(),
                                3.0f,
                                true
                        );
                        break;
                }
                break;
        }
    }

    private static void addToUI(GameScreen gs, AbstractConsumable consumable, Label label) {
        gs.getUiLatestPanel().addLabel(label.getSticker());
    }

    private static void addToSelector(GameScreen gs, AbstractConsumable consumable, Label label) {
        if (consumable.isGolden()) {
            if (Label.Sticker.SELECTABLE.contains(label.getSticker())) {
                gs.getSelectorGroup().addSelector(label);
            }
        }
    }
}
