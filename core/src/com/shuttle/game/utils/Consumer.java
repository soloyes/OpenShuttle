package com.shuttle.game.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.GameInfoSystem;
import com.shuttle.game.bot.anomaly.AbstractAnomaly;
import com.shuttle.game.bot.consumable.AbstractConsumable;
import com.shuttle.game.bot.consumable.Label;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.player.Standing;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.effects.BoomBox;

import java.util.List;

public class Consumer {
    public static void consume(GameScreen gs, AbstractConsumable consumable, Label label) {
        //todo: gold loot sounds??

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
            boomBox.playSound(MSConstants.PLAYER_REPAIR);
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
            boomBox.playSound(MSConstants.TURBO);
        }

        if (label.getSticker() == Label.Sticker.SPLASH) {
            List<AbstractAnomaly> anomalies = gs.getCorrosiveAnomalyEmitter().getActiveList();
            for (int i = 0; i < anomalies.size(); i++) {
                if (boundCheck.checkAndHandleBoundsInCamera(anomalies.get(i), gs.getCamera())) {
                    if (!anomalies.get(i).isTaken()) {
                        anomalies.get(i).setMarkedToBoom(true);
                        standing.addToStatistics(anomalies.get(i).getType(), anomalies.get(i).getLevel());
                        int score = Rules.ANOMALIES_BASE_SCORE_ADD;
                        gs.getGameInfoSystem().setup(
                                anomalies.get(i).getPosition().x,
                                anomalies.get(i).getPosition().y,
                                GameInfoSystem.GameInfoSystemPreset.SCORE + "[GREEN] +" + score + "[]",
                                3.0f,
                                true
                        );
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
            boomBox.playSound(MSConstants.ANOMALY_SPLASH);
        }

        if (label.getSticker() == Label.Sticker.TRASH) {
            gs.getGameInfoSystem()
                    .setup(
                            position.x,
                            position.y,
                            GameInfoSystem.GameInfoSystemPreset.TRASH,
                            2.0f,
                            true
                    );
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
                            GameInfoSystem.GameInfoSystemPreset.RADAR,
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
                            GameInfoSystem.GameInfoSystemPreset.LASER,
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
            switch (consumable.getType()) {
                case CRATE:
                    //todo: -1 life + boom, -all fuel, new green anomaly, disable laser, -score
                    //generate some crates, kill rad or fuel anomaly (+ score)
                    if (consumable.isGolden()) {
                        gs.getCorrosiveAnomalyEmitter().createBonusAnomaly(consumable.getPosition(), 1);
                        //todo: -1/2 life + boom, -1/2 fuel, new tan anomaly, disable radar, -score
                        //+several manual activated loot, add HP
                    } else {
                        gs.getCorrosiveAnomalyEmitter().createBonusAnomaly(consumable.getPosition(), 0);
                    }
                    break;
                case STORAGE:
                    if (consumable.isGolden()) {
                        //todo: -2 life + boom, -all fuel, new red anomaly, disable radar/laser, -score
                        //+generate storage, kill rad and fuel anomaly (+ score)
                        gs.getCorrosiveAnomalyEmitter().createBonusAnomaly(consumable.getPosition(), 3);
                    } else {
                        //todo: -1,5 life + boom, -all fuel, new yellow anomaly, disable radar and laser, -score
                        //+several manual activated loot, add HP
                        gs.getCorrosiveAnomalyEmitter().createBonusAnomaly(consumable.getPosition(), 2);
                    }
                    break;
            }
        }
    }

    private void getPositive(GameScreen gs, AbstractConsumable consumable, int level) {
        int r = MathUtils.random(0, 3);
        // 0 - Kill related level corrosive anomaly
        // 1 - Add score
        // 2 - Add manual activated loot, or generate Crate/Storage
        // 3 - Kill Fuel/Radiation anomaly, or add HP/life

        switch (r) {
            case 0:
                List<AbstractAnomaly> anomalies = gs.getCorrosiveAnomalyEmitter().getActiveList();
                for (int i = 0; i < anomalies.size(); i++) {
                    if (anomalies.get(i).getLevel() == level) {
                        //todo:// Добавить очки
                        anomalies.get(i).setMarkedToBoom(true);
                    }
                }
                break;
            case 1:
                gs.getPlayer().getStanding().changeScore((level + 1) * Rules.SCORE_BASE_BONUS);
                break;
            case 2:
                if (consumable.isGolden()) {
                    //todo://Add manual activated loot

                } else {
                    switch (consumable.getType()) {
                        case CRATE:
                            for (int i = 0; i < (level + 1) * Rules.CONSUMABLE_GENERATE_BASE_BONUS; i++) {
                                gs.getCrateEmitter().createBonusAnomaly();
                            }
                            break;
                        case STORAGE:
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
                            } else {
                                list = gs.getRadiationAnomalyEmitter().getActiveList();
                            }
                            for (int i = 0; i < list.size(); i++) {
                                list.get(i).destroy();
                            }
                            break;
                        case STORAGE:
                            break;
                    }
                    break;
                }
        }
    }
}
