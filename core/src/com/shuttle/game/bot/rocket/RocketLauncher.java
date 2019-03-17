package com.shuttle.game.bot.rocket;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shuttle.game.bot.anomaly.AbstractAnomaly;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.BoundCheck;

import lombok.Getter;

/**
 * @author Shuttle on 20/01/19.
 */
public class RocketLauncher {
    @Getter
    private RocketEmitter rocketEmitter;
    private GameScreen gs;
    private BoomBox boomBox;
    private boolean charged;
    public static boolean launched;
    private float time;
    private int amplitude;

    public RocketLauncher(GameScreen gs) {
        this.gs = gs;
        this.rocketEmitter = new RocketEmitter(gs);
        this.charged = false;
        this.boomBox = new BoomBox();
    }

    public void render(SpriteBatch batch) {
        rocketEmitter.render(batch);
    }

    public void update(float dt) {
        rocketEmitter.update(dt);
        if (launched) {
            boomBox.playMusicEffectOnce(MSConstants.CHARGING_ROCKETS);
            time += dt;
            if (time >= Rules.LAUNCH_ROCKETS_TIME) {
                time = 0.0f;
                launched = false;
                charged = true;
            }
        }

        if (charged && !gs.getPlayer().isTaken() && !Rules.GAME_OVER && BoundCheck.playerOnScreen) {
            int count = 0;
            for (int i = 0; i < gs.getAnomalyList().size(); i++) {
                for (int j = 0; j < gs.getAnomalyList().get(i).size(); j++) {
                    AbstractAnomaly anomaly = gs.getAnomalyList().get(i).get(j);
                    if (anomaly.isAtRocketPoint()) {
                        count++;
                        rocketEmitter.createRocket(Shuttle.position, anomaly);
                    }
                }
            }
            if (count > 0) {
                boomBox.playSound(MSConstants.LAUNCH_ROCKETS);
                gs.getShake().shake(1, 35, 10 + 5 * amplitude);
            }
            charged = false;
            amplitude = 0;
        }
    }

    public void launch() {
        launched = true;
        amplitude++;
    }
}
