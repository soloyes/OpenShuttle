package com.shuttle.game.utils;

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
import java.util.Map;

public class Consumer {
    public static void consume(GameScreen gs, AbstractConsumable consumable, Label label) {
        //todo: Add Gold boxes addition here (seems need to add one more parameter to signature, and if-else logic in every block)


        Standing standing = gs.getPlayer().getStanding();
        Shuttle player = gs.getPlayer();
        Map<Label.Layer, List<Label>> labels = consumable.getLabels();
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
    }
}
