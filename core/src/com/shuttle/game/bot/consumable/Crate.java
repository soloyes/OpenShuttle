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
        this.texture = Assets.getInstance().getAtlas().findRegion(PaintConstants.CRATE_2_REGION);
        this.width = PaintConstants.CRATE_2_WIDTH;
        this.height = PaintConstants.CRATE_2_HEIGHT;
        this.type = Type.CRATE;
        this.moveProcessor = new MoveProcessor(gs);
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
    }

    @Override
    public void update(float dt) {
        basicUpdate(dt);
        moveProcessor.update(this, dt);
        boundCheck.checkAndHandleBoundsAndDestroy(this);
    }

    @Override
    public void consumed(GameScreen gs) {
        Standing standing = gs.getPlayer().getStanding();
        Shuttle player = gs.getPlayer();
        int x = MathUtils.random(0, 3);
        /*Hack to pretty shadow effect. This effect should be finished firstly*/
        if (player.getVelocity().len() >= Rules.PLAYER_ENGINE_POWER) {
            x++;
        }
        /*Hack*/
        /*Hack to prevent useless hp additions*/
        if (x == 0 && standing.getHp() == standing.getMaxHp()) {
            x++;
        }
        /*Hack*/
        if (x == 0) {
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
        if (x == 1) {
            player.getVelocity().add(
                    Rules.PLAYER_TURBO_ENGINE_POWER * (float) Math.cos(Math.toRadians(player.getAngle())),
                    Rules.PLAYER_TURBO_ENGINE_POWER * (float) Math.sin(Math.toRadians(player.getAngle()))
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
        if (x == 2) {
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
                            GameInfoSystem.GameInfoSystemPreset.FLUSH,
                            8.0f,
                            true
                    );
            boomBox.playSound(MSConstants.ANOMALY_FLUSH);
        }
        if (x > 2) {
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
}
