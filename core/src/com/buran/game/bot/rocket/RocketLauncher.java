package com.buran.game.bot.rocket;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.buran.game.bot.anomaly.AbstractAnomaly;
import com.buran.game.conf.MSConstants;
import com.buran.game.conf.Rules;
import com.buran.game.effects.BoomBox;
import com.buran.game.effects.Shake;
import com.buran.game.player.Buran;
import com.buran.game.ui.GameInfoSystem;
import com.buran.game.utils.BoundCheck;
import com.buran.game.utils.ListOfListsJoiner;

import lombok.Getter;

/**
 * @author soloyes on 20/01/19.
 */

public class RocketLauncher {
    @Getter
    private static boolean launched;
    private BoomBox boomBox;
    private boolean charged;
    private float time;
    private static int amplitude;
    private ListOfListsJoiner<AbstractAnomaly> anomalyJoiner;
    private RocketEmitter rocketEmitter;

    public RocketLauncher(RocketEmitter rocketEmitter, ListOfListsJoiner<AbstractAnomaly> anomalyJoiner) {
        this.charged = false;
        launched = false;
        amplitude = 0;
        this.boomBox = new BoomBox();
        this.anomalyJoiner = anomalyJoiner;
        this.rocketEmitter = rocketEmitter;
    }

    public static void launch() {
        launched = true;
        amplitude++;
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

        if (charged && !Buran.isTaken && !Rules.GAME_OVER && BoundCheck.playerOnScreen) {
            int count = 0;
            for (int i = 0; i < anomalyJoiner.size(); i++) {
                AbstractAnomaly anomaly = anomalyJoiner.get(i);
                if (anomaly.isAtRocketPoint()) {
                    count++;
                    rocketEmitter.createRocket(Buran.position, anomaly);
                }
            }
            if (count > 0) {
                boomBox.playSound(MSConstants.LAUNCH_ROCKETS);
                Shake.getInstance().shakeCamera(1, 35, 10 + 5 * amplitude);
            }
            if (count >= 10) {
                GameInfoSystem.setup(
                        Buran.position.x,
                        Buran.position.y,
                        String.valueOf(GameInfoSystem.Preset.ADIEU.getValue()),
                        15.0f,
                        false
                );
            }
            charged = false;
            amplitude = 0;
        }
    }
}