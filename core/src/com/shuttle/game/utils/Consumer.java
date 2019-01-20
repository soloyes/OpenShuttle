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
        Standing standing = gs.getPlayer().getStanding();
        Shuttle player = gs.getPlayer();
        Vector2 position = consumable.getPosition();
        BoomBox boomBox = consumable.getBoomBox();
        BoundCheck boundCheck = consumable.getBoundCheck();

        if (label.getSticker() == Label.Sticker.REPAIR) {
            standing.changeHP(Rules.CONSUMABLE_REPAIR_ADD);
            gs.getGameInfoSystem()
                    .setup(
                            position.x,
                            position.y,
                            GameInfoSystem.GameInfoSystemPreset.REPAIR,
                            3.0f,
                            true
                    );
            boomBox.playSound(MSConstants.GET_REPAIR);
        }

        if (label.getSticker() == Label.Sticker.BOOST) {
            float d = Rules.PLAYER_BOOST_ENGINE_POWER - player.getVelocity().len();
            player.setShadowsCoordinates();
            player.getVelocity().add(
                    d * (float) Math.cos(Math.toRadians(player.getAngle())),
                    d * (float) Math.sin(Math.toRadians(player.getAngle()))
            );
            gs.getGameInfoSystem()
                    .setup(
                            position.x,
                            position.y,
                            GameInfoSystem.GameInfoSystemPreset.TURBO,
                            5.0f,
                            true
                    );
            boomBox.playSound(MSConstants.GET_BOOST);
        }

        if (label.getSticker() == Label.Sticker.SPLASH) {
            List<AbstractAnomaly> anomalies = gs.getCorrosiveAnomalyEmitter().getActiveList();
            for (int i = 0; i < anomalies.size(); i++) {
                if (boundCheck.checkAndHandleBoundsInCamera(anomalies.get(i), gs.getCamera())) {
                    if (!anomalies.get(i).isTaken()) {
                        anomalies.get(i).setMarkedToBoom(true);
                        standing.addToStatistics(anomalies.get(i).getType(), anomalies.get(i).getLevel());
                        standing.changeScore(Rules.ANOMALIES_BASE_SCORE_ADD);
                    }
                }
            }
            gs.getGameInfoSystem()
                    .setup(
                            position.x,
                            position.y,
                            GameInfoSystem.GameInfoSystemPreset.SPLASH,
                            8.0f,
                            true
                    );
            boomBox.playSound(MSConstants.GET_SPLASH);
        }

        if (label.getSticker() == Label.Sticker.TRASH) {
            int score = Rules.CONSUMABLE_TRASH_BASE_SCORE * MathUtils.random(0, 5);
            gs.getGameInfoSystem()
                    .setup(
                            position.x,
                            position.y,
                            "+" + score + "[YELLOW]" + GameInfoSystem.GameInfoSystemPreset.TRASH + "[]",
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
            gs.getGameInfoSystem()
                    .setup(
                            position.x,
                            position.y,
                            GameInfoSystem.GameInfoSystemPreset.SHIELD,
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

            gs.getGameInfoSystem()
                    .setup(
                            position.x,
                            position.y,
                            GameInfoSystem.GameInfoSystemPreset.SCAN,
                            2.0f,
                            true
                    );
            boomBox.playSound(MSConstants.GET_SCANNER);
        }

        if (label.getSticker() == Label.Sticker.RADAR) {
            gs.getGameInfoSystem()
                    .setup(
                            position.x,
                            position.y,
                            "[GREEN]" + GameInfoSystem.GameInfoSystemPreset.RADAR + "[]",
                            3.0f,
                            true
                    );
            boomBox.playSound(MSConstants.GET_RADAR);
            gs.getPlayer().getRadar().setActive(true);
        }

        if (label.getSticker() == Label.Sticker.LASER) {
            gs.getGameInfoSystem()
                    .setup(
                            position.x,
                            position.y,
                            "[GREEN]" + GameInfoSystem.GameInfoSystemPreset.LASER + "[]",
                            3.0f,
                            true
                    );
            gs.getPlayer().setLaser(true);
            gs.getPlayer().setLaserChargeLevel(6);
            boomBox.playSound(MSConstants.GET_LASER);
        }

        if (label.getSticker() == Label.Sticker.ROCKET) {
            gs.getGameInfoSystem()
                    .setup(
                            position.x,
                            position.y,
                            GameInfoSystem.GameInfoSystemPreset.ROCKET,
                            3.0f,
                            true
                    );
            boomBox.playSound(MSConstants.GET_ROCKET);
        }

        if (label.getSticker() == Label.Sticker.FUEL) {
            float fuel;
            if (consumable.isGolden()) {
                fuel = gs.getPlayer().getStanding().getMaxFuel();
            } else {
                fuel = Rules.CONSUMABLE_FUEL_ADD_FACTOR * MathUtils.random(Rules.CONSUMABLE_FUEL_MIN_ADD, Rules.CONSUMABLE_FUEL_MAX_ADD);
            }

            gs.getPlayer().getStanding().changeFuel(fuel);

            if (consumable.isGolden()) {
                gs.getGameInfoSystem()
                        .setup(
                                position.x,
                                position.y,
                                GameInfoSystem.GameInfoSystemPreset.FUEL + "[GOLD] +" + fuel + "[]",
                                5.0f,
                                true
                        );
            } else {
                gs.getGameInfoSystem()
                        .setup(
                                position.x,
                                position.y,
                                GameInfoSystem.GameInfoSystemPreset.FUEL + "[GREEN] +" + fuel + "[]",
                                3.0f,
                                true
                        );
            }
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
    }

    private static void addToUI(GameScreen gs, AbstractConsumable consumable, Label label) {
        if (!consumable.isGolden()) {
            gs.getUiLatestPanel().addLabel(label.getSticker());
        } else {

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
                gs.getGameInfoSystem().setup(
                        consumable.getPosition().x,
                        consumable.getPosition().y,
                        GameInfoSystem.GameInfoSystemPreset.ANOMALY_ARISE,
                        3.0f,
                        true
                );
                break;
            case 1:
                score = (level + 1) * Rules.SCORE_BASE_BONUS;
                gs.getPlayer().getStanding().changeScore(-score);
                gs.getGameInfoSystem().setup(
                        consumable.getPosition().x,
                        consumable.getPosition().y,
                        GameInfoSystem.GameInfoSystemPreset.SCORE + "-[RED]" + score + "[]",
                        3.0f,
                        true
                );
                break;
            case 2:
                switch (consumable.getType()) {
                    case CRATE:
                        if (consumable.isGolden()) {
                            gs.getPlayer().getRadar().setActive(false);
                            gs.getGameInfoSystem().setup(
                                    consumable.getPosition().x,
                                    consumable.getPosition().y,
                                    "-[RED]" + GameInfoSystem.GameInfoSystemPreset.RADAR + "[]",
                                    3.0f,
                                    true
                            );
                        } else {
                            gs.getPlayer().setLaser(false);
                            gs.getGameInfoSystem().setup(
                                    consumable.getPosition().x,
                                    consumable.getPosition().y,
                                    "-[RED]" + GameInfoSystem.GameInfoSystemPreset.LASER + "[]",
                                    3.0f,
                                    true
                            );
                        }
                        break;
                    case STORAGE:
                        List<AbstractConsumable> list;
                        GameInfoSystem.GameInfoSystemPreset preset;
                        if (consumable.isGolden()) {
                            list = gs.getStorageEmitter().getActiveList();
                            preset = GameInfoSystem.GameInfoSystemPreset.STORAGE_FLUSH;
                        } else {
                            list = gs.getCrateEmitter().getActiveList();
                            preset = GameInfoSystem.GameInfoSystemPreset.CRATE_FLUSH;
                        }

                        for (int i = 0; i < list.size(); i++) {
                            AbstractConsumable c = list.get(i);
                            if (!c.isTaken()) {
                                c.destroyWithEffect();
                            }
                        }
                        gs.getGameInfoSystem().setup(
                                consumable.getPosition().x,
                                consumable.getPosition().y,
                                preset,
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
                            gs.getPlayer().getStanding().changeLives(-1);
                            gs.getGameInfoSystem().setup(
                                    consumable.getPosition().x,
                                    consumable.getPosition().y,
                                    "-[RED]" + GameInfoSystem.GameInfoSystemPreset.UP + "[]",
                                    3.0f,
                                    true
                            );
                        } else {
                            if (MathUtils.random(0, 1) == 0) {
                                gs.getPlayer().getStanding().changeHP(-gs.getPlayer().getStanding().getHp() + 1);
                                gs.getGameInfoSystem().setup(
                                        consumable.getPosition().x,
                                        consumable.getPosition().y,
                                        "-[RED]" + GameInfoSystem.GameInfoSystemPreset.HP + "[]",
                                        3.0f,
                                        true
                                );
                            } else {
                                gs.getPlayer().getStanding().changeFuel(-gs.getPlayer().getStanding().getFuel());
                                gs.getGameInfoSystem().setup(
                                        consumable.getPosition().x,
                                        consumable.getPosition().y,
                                        "-[RED]" + GameInfoSystem.GameInfoSystemPreset.FUEL + "[]",
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
                        gs.getGameInfoSystem().setup(
                                consumable.getPosition().x,
                                consumable.getPosition().y,
                                GameInfoSystem.GameInfoSystemPreset.ANOMALY_ARISE,
                                3.0f,
                                true
                        );
                        break;
                }
                break;
        }
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
                        gs.getPlayer().getStanding().changeScore(Rules.ANOMALIES_BASE_SCORE_ADD * (level + 1));
                    }
                }
                gs.getGameInfoSystem().setup(
                        consumable.getPosition().x,
                        consumable.getPosition().y,
                        GameInfoSystem.GameInfoSystemPreset.ANOMALY_FLUSH,
                        3.0f,
                        true
                );
                break;
            case 1:
                score = (level + 1) * Rules.SCORE_BASE_BONUS;
                gs.getPlayer().getStanding().changeScore(score);
                gs.getGameInfoSystem().setup(
                        consumable.getPosition().x,
                        consumable.getPosition().y,
                        GameInfoSystem.GameInfoSystemPreset.SCORE + "[GREEN] +" + score + "[]",
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
                            gs.getGameInfoSystem().setup(
                                    consumable.getPosition().x,
                                    consumable.getPosition().y,
                                    GameInfoSystem.GameInfoSystemPreset.BONUS_CRATE,
                                    3.0f,
                                    true
                            );
                            for (int i = 0; i < (level + 1) * Rules.CONSUMABLE_GENERATE_BASE_BONUS; i++) {
                                gs.getCrateEmitter().createBonusAnomaly();
                            }
                            break;
                        case STORAGE:
                            gs.getCrateEmitter().createBonusAnomaly();
                            gs.getGameInfoSystem().setup(
                                    consumable.getPosition().x,
                                    consumable.getPosition().y,
                                    GameInfoSystem.GameInfoSystemPreset.BONUS_STORAGE,
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
                            gs.getPlayer().getStanding().changeHP(gs.getPlayer().getStanding().getMaxHp());
                            break;
                        case STORAGE:
                            gs.getPlayer().getStanding().changeHP(gs.getPlayer().getStanding().getMaxHp());
                            gs.getPlayer().getStanding().changeLives(1);
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
                                    gs.getGameInfoSystem().setup(
                                            anomaly.getPosition().x,
                                            anomaly.getPosition().y,
                                            GameInfoSystem.GameInfoSystemPreset.SCORE + "[GREEN] +" + score + "[]",
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
                                    gs.getGameInfoSystem().setup(
                                            anomaly.getPosition().x,
                                            anomaly.getPosition().y,
                                            GameInfoSystem.GameInfoSystemPreset.SCORE + "[GREEN] +" + score + "[]",
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
                                    gs.getGameInfoSystem().setup(
                                            anomaly.getPosition().x,
                                            anomaly.getPosition().y,
                                            GameInfoSystem.GameInfoSystemPreset.SCORE + "[GREEN] +" + score + "[]",
                                            3.0f,
                                            true
                                    );
                                }
                            }
                            break;
                    }
                    gs.getGameInfoSystem().setup(
                            consumable.getPosition().x,
                            consumable.getPosition().y,
                            GameInfoSystem.GameInfoSystemPreset.ANOMALY_FLUSH,
                            3.0f,
                            true
                    );
                    break;
                }
        }
    }
}
