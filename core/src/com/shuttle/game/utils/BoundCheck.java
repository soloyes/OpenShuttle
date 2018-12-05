package com.shuttle.game.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.GameInfoSystem;
import com.shuttle.game.bot.pool.AbstractPoolable;
import com.shuttle.game.conf.MSConstants;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.conf.VConstants;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.utils.effects.BoomBox;
import com.shuttle.game.utils.effects.Vibrator;

/**
 * @author Shuttle on 8/26/18.
 */
public class BoundCheck {
    private float alertTimer;
    private float hpTimer;
    private float fuelTimer;
    private Rectangle rectangle;
    private BoomBox boomBox;

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
        if (!rectangle.contains(player.getPosition())) {
            checkCount(player, dt);
        } else {
            alertTimer = 0;
            fuelTimer = 0;
            hpTimer = 0;
            player.getStanding().setFuelAlertOutOfBounds(false);
            player.getStanding().setHpAlert(false);
        }
    }

    private void checkCount(Shuttle player, float dt) {
        alertTimer += dt;
        fuelTimer += dt;
        hpTimer += dt;
        //every second
        if (alertTimer >= 1.0f) {
            alert(player, alertTimer);
            alertTimer = 0.0f;
        }

        if (hpTimer >= Rules.PLAYER_TIMER_HP_OUT_OF_MAP) {
            player.takeDamage(Rules.PLAYER_HP_OUT_OF_MAP);
            player.getStanding().setHpAlert(true);
            hpTimer = 0.0f;
        }
        if (fuelTimer >= Rules.PLAYER_TIMER_FUEL_OUT_OF_MAP) {
            player.getStanding().setFuel(player.getStanding().getFuel() - Rules.PLAYER_FUEL_OUT_OF_MAP);
            player.getStanding().setFuelAlertOutOfBounds(true);
            fuelTimer = 0.0f;
        }
    }

    private void alert(Shuttle player, float duration) {
        if (!Rules.GAME_OVER) {
            player.getGs().getGameInfoSystem().setup(
                    player.getGs().getCamera().position.x,
                    player.getGs().getCamera().position.y,
                    GameInfoSystem.GameInfoSystemPreset.OUT,
                    3,
                    true
            );
            boomBox.playSound(MSConstants.PLAYER_OUT_OF_SCREEN);
            player.getGs().getShake().shake(duration, 35, 10);
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

    public boolean checkAndHandleBoundsInCamera(AbstractPoolable poolable, Camera camera) {
        rectangle.set(
                camera.position.x - Rules.WORLD_WIDTH / 2,
                camera.position.y - Rules.WORLD_HEIGHT / 2,
                Rules.WORLD_WIDTH, Rules.SPACE_HEIGHT
        );
        return rectangle.contains(poolable.getPosition());
    }
}