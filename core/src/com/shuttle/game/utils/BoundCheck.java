package com.shuttle.game.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.conf.VConstants;
import com.shuttle.game.effects.BoomBox;
import com.shuttle.game.effects.Shake;
import com.shuttle.game.effects.Vibrator;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.player.Standing;
import com.shuttle.game.screens.ScreenManager;
import com.shuttle.game.ui.GameInfoSystem;
import com.shuttle.game.utils.pool.AbstractPoolable;

/**
 * @author Shuttle on 8/26/18.
 */
public class BoundCheck {
    private float alertTimer;
    private float hpTimer;
    private float fuelTimer;
    private Rectangle rectangle;
    private BoomBox boomBox;
    public static boolean playerOnScreen;

    public BoundCheck() {
        this.rectangle = new Rectangle();
        this.boomBox = new BoomBox();
    }

    public static void avoidPosition(Vector2 from, Vector2 to, int near, int x1, int x2, int y1, int y2) {
        Vector2 tmp = from.cpy();
        while (tmp.sub(to).len() <= near) {
            //Prevent generation near given position
            to.set(MathUtils.random(x1, x2), MathUtils.random(y1, y2));
        }
    }

    public void checkAndHandleBounds(Shuttle player, float dt) {
        rectangle.set(
                -player.getWidth() / 2, -player.getHeight() / 2,
                Rules.SPACE_WIDTH + player.getWidth(),
                Rules.SPACE_HEIGHT + player.getHeight()
        );
        if (!rectangle.contains(Shuttle.position)) {
            checkCount(dt);
            playerOnScreen = false;
        } else {
            alertTimer = 0;
            fuelTimer = 0;
            hpTimer = 0;
            Standing.getInstance().setFuelAlertOutOfBounds(false);
            Standing.getInstance().setHpAlert(false);
            playerOnScreen = true;
        }
    }

    private void checkCount(float dt) {
        alertTimer += dt;
        fuelTimer += dt;
        hpTimer += dt;
        //every second
        if (alertTimer >= 1.0f) {
            alert(alertTimer);
            alertTimer = 0.0f;
        }

        if (hpTimer >= Rules.PLAYER_TIMER_HP_OUT_OF_MAP) {
            Standing.getInstance().takeDamage(Rules.PLAYER_HP_OUT_OF_MAP);
            Standing.getInstance().setHpAlert(true);
            hpTimer = 0.0f;
        }
        if (fuelTimer >= Rules.PLAYER_TIMER_FUEL_OUT_OF_MAP) {
            Standing.getInstance().setFuel(Standing.getInstance().getFuel() - Rules.PLAYER_FUEL_OUT_OF_MAP);
            Standing.getInstance().setFuelAlertOutOfBounds(true);
            fuelTimer = 0.0f;
        }
    }

    private void alert(float duration) {
        if (!Rules.GAME_OVER) {
            GameInfoSystem.setup(
                    ScreenManager.getInstance().getCamera().position.x,
                    ScreenManager.getInstance().getCamera().position.y,
                    GameInfoSystem.Preset.OUT,
                    3,
                    true
            );
            boomBox.playSound(MSConstants.OUT_OF_SCREEN);
            Shake.getInstance().shakeCamera(duration, 35, 10);
            Vibrator.vibrate(VConstants.PLAYER_OUT_OF_SCREEN);
        }
    }

    public boolean checkAndHandleBounds(AbstractPoolable poolable) {
        rectangle.set(
                -poolable.getWidth() / 2, -poolable.getHeight() / 2,
                Rules.SPACE_WIDTH + poolable.getWidth(),
                Rules.SPACE_HEIGHT + poolable.getHeight()
        );
        return !rectangle.contains(poolable.getPosition());
    }

    public void checkAndHandleBoundsAndDestroy(AbstractPoolable poolable) {
        rectangle.set(
                -poolable.getWidth() / 2, -poolable.getHeight() / 2,
                Rules.SPACE_WIDTH + poolable.getWidth(),
                Rules.SPACE_HEIGHT + poolable.getHeight()
        );
        if (!rectangle.contains(poolable.getPosition()))
            poolable.destroy();
    }

    public boolean checkAndHandleBoundsInCamera(AbstractPoolable poolable) {
        Camera camera = ScreenManager.getInstance().getViewport().getCamera();
        rectangle.set(
                camera.position.x - Rules.WORLD_WIDTH / 2,
                camera.position.y - Rules.WORLD_HEIGHT / 2,
                Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT
        );
        return rectangle.contains(poolable.getPosition());
    }
}