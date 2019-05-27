package com.shuttle.game.bot.rocket;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shuttle.game.bot.anomaly.AbstractAnomaly;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.effects.Shake;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.utils.BoundCheck;
import com.shuttle.game.utils.ListOfListsJoiner;

import lombok.Getter;

/**
 * @author Shuttle on 20/01/19.
 */
public class RocketLauncher {
    private static RocketLauncher instance;

    public static RocketLauncher getInstance() {
        if (instance == null) {
            instance = new RocketLauncher();
        }
        return instance;
    }

    public void doRestart() {
        instance = null;
    }

    @Getter
    private boolean launched;

    private BoomBox boomBox;
    private boolean charged;
    private float time;
    private int amplitude;
    private ListOfListsJoiner<AbstractAnomaly> anomalyJoiner;

    private RocketLauncher() {
        this.charged = false;
        this.boomBox = new BoomBox();
        this.anomalyJoiner = ListOfListsJoiner.newAbstractAnomalyList();
    }

    public void launch() {
        launched = true;
        amplitude++;
    }

    public void render(SpriteBatch batch) {
        RocketEmitter.getInstance().render(batch);
    }

    public void update(float dt) {
        RocketEmitter.getInstance().update(dt);
        if (launched) {
            boomBox.playMusicEffectOnce(MSConstants.CHARGING_ROCKETS);
            time += dt;
            if (time >= Rules.LAUNCH_ROCKETS_TIME) {
                time = 0.0f;
                launched = false;
                charged = true;
            }
        }

        if (charged && !Shuttle.isTaken && !Rules.GAME_OVER && BoundCheck.playerOnScreen) {
            int count = 0;
            for (int i = 0; i < anomalyJoiner.size(); i++) {
                AbstractAnomaly anomaly = anomalyJoiner.get(i);
                if (anomaly.isAtRocketPoint()) {
                    count++;
                    RocketEmitter.getInstance().createRocket(Shuttle.position, anomaly);
                }
            }
            if (count > 0) {
                boomBox.playSound(MSConstants.LAUNCH_ROCKETS);
                Shake.getInstance().shakeCamera(1, 35, 10 + 5 * amplitude);
            }
            charged = false;
            amplitude = 0;
        }
    }
}