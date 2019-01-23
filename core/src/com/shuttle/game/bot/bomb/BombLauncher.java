package com.shuttle.game.bot.bomb;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.utils.BoundCheck;

import lombok.Getter;

/**
 * @author Shuttle on 20/01/19.
 */
public class BombLauncher {
    @Getter
    private BombEmitter bombEmitter;
    private GameScreen gs;
    private BoomBox boomBox;

    private boolean charged;
    private boolean launched;
    private int bank;
    private float time;

    public BombLauncher(GameScreen gs) {
        this.gs = gs;
        this.bombEmitter = new BombEmitter(gs);
        this.launched = false;
        this.charged = false;
        this.boomBox = new BoomBox();
    }

    public void dispose() {
        bombEmitter.dispose();
    }

    public void render(SpriteBatch batch) {
        bombEmitter.render(batch);
    }

    public void update(float dt) {
        bombEmitter.update(dt);
        if (launched) {
            boomBox.playMusicEffectOnce(MSConstants.CHARGING_BOMBS);
            time += dt;
            if (time >= Rules.LAUNCH_BOMBS_TIME) {
                time = 0.0f;
                launched = false;
                charged = true;
            }
        }

        if (charged && !gs.getPlayer().isTaken() && !Rules.GAME_OVER) {
            for (int i = 0; i <= bank * Rules.LAUNCH_BOMBS; i++) {
                bombEmitter.createBomb(gs.getPlayer().getPosition());
            }
            if (BoundCheck.playerOnScreen) {
                boomBox.playSound(MSConstants.LAUNCH_BOMBS);
                gs.getShake().shake(1, 35, 10 * bank);
            }
            charged = false;
            bank = 0;
        }
    }

    public void launch() {
        launched = true;
        bank += 1;
    }
}
