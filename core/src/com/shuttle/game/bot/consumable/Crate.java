package com.shuttle.game.bot.consumable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.shuttle.game.GameInfoSystem;
import com.shuttle.game.bot.anomaly.AbstractAnomaly;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.PaintConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.conf.VConstants;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.player.Standing;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.Assets;
import com.shuttle.game.utils.effects.MoveProcessor;
import com.shuttle.game.utils.effects.Vibrator;

import java.util.List;

/**
 * @author Shuttle on 10/11/18.
 */
public class Crate extends AbstractConsumable {
    public Crate(GameScreen gs) {
        super(gs);
        this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.CRATE_1_REGION);
        this.width = PaintConstants.CRATE_1_WIDTH;
        this.height = PaintConstants.CRATE_1_HEIGHT;
        this.type = Type.CRATE;
        this.moveProcessor = new MoveProcessor(gs);
        this.shield = new Shield(this);
        this.scan = new Scan(this);
    }

    @Override
    public void init(float x, float y) {
        this.position.set(x, y);
        this.angle = MathUtils.random(0, 360);
        this.rotationSpeed = MathUtils.random(-20, 20);
        this.active = true;
        this.scaleX = this.saveScaleX = 1.0f;
        this.scaleY = this.saveScaleY = 1.0f;
        this.taken = false;
        this.color = new Color(1.0f, 1.0f, 1.0f, 1.0f);
        this.spiralRadius = MathUtils.random(20, 60);
        this.atRayPoint = false;
        this.labeled = true;
        this.labels[0].setScaleFactor(3.5f);
        this.labels[1].setScaleFactor(3.5f);
        stickLabel();
        this.shielded = false;
        this.shield.setTime(Rules.CONSUMABLE_SHIELD_TIME);
        this.scanned = false;
    }

    private void stickLabel() {
        int r = MathUtils.random(0, 20);
        switch (r) {
            case 0:
                generateDanger();
                break;
            default:
                generateSafe();
        }
    }

    private void generateSafe() {
        int r = MathUtils.random(0, 6);
        switch (r) {
            case 0:
                labels[1].setSticker(Label.Sticker.BOOST);
                break;
            case 1:
                labels[1].setSticker(Label.Sticker.REPAIR);
                break;
            case 2:
                labels[1].setSticker(Label.Sticker.TRASH);
                break;
            case 3:
                labels[1].setSticker(Label.Sticker.SPLASH);
                break;
            case 5:
                labels[1].setSticker(Label.Sticker.SCAN);
                break;
            case 6:
                labels[1].setSticker(Label.Sticker.SHIELD);
        }
    }

    private void generateDanger() {
        int r = MathUtils.random(0, 5);
        switch (r) {
            case 1:
                labels[1].setSticker(Label.Sticker.PATTERN);
                break;
            default:
                labels[1].setSticker(Label.Sticker.SHABBY);
        }
    }

    @Override
    public void update(float dt) {
        basicUpdate(dt);
        moveProcessor.update(this, dt);
        boundCheck.checkAndHandleBoundsAndDestroy(this);
        for (int i = 0; i < labels.length; i++) {
            labels[i].update(dt);
        }
        shield.update(dt);
    }

    @Override
    public void consumed(GameScreen gs) {
        Standing standing = gs.getPlayer().getStanding();
        Shuttle player = gs.getPlayer();

        if (labels[1].getSticker() == Label.Sticker.REPAIR) {
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

        if (labels[1].getSticker() == Label.Sticker.BOOST) {
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

        if (labels[1].getSticker() == Label.Sticker.SPLASH) {
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

        if (labels[1].getSticker() == Label.Sticker.TRASH) {
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

        if (labels[1].getSticker() == Label.Sticker.SHIELD) {
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
            //todo: Shield music
            boomBox.playSound(MSConstants.GET_TRASH);
        }

        if (labels[1].getSticker() == Label.Sticker.SCAN) {
            List<AbstractConsumable> list = gs.getCrateEmitter().getActiveList();
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setScanned(true);
            }
            list = gs.getStorageEmitter().getActiveList();
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setScanned(true);
            }

            gs.getGameInfoSystem()
                    .setup(
                            position.x,
                            position.y,
                            GameInfoSystem.GameInfoSystemPreset.SCAN,
                            2.0f,
                            true
                    );
            //todo: Scan music
            boomBox.playSound(MSConstants.GET_TRASH);
        }
    }

    @Override
    public void badEffect(GameScreen gs) {
        gs.getGameInfoSystem().setup(
                position.x,
                position.y,
                GameInfoSystem.GameInfoSystemPreset.EASY,
                3.0f,
                false
        );
        boomBox.playSound(MSConstants.KILL_CRATE);
        Vibrator.vibrate(VConstants.PLAYER_CRATE_DESTROY);
    }

    @Override
    public void destroyWithEffect() {
        super.destroyWithEffect();
        showSprayCollision(gs.getParticleEmitter());
    }
}