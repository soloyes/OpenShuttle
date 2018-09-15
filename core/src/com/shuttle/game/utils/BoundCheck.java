package com.shuttle.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.GameInfoSystem;
import com.shuttle.game.player.Shuttle;
import com.shuttle.game.bot.pool.AbstractPoolable;
import com.shuttle.game.conf.Rules;
import com.shuttle.game.screens.ScreenManager;

/**
 * @author Shuttle on 8/26/18.
 */
public class BoundCheck {
    private float alertTimer;
    private float hpTimer;
    private float fuelTimer;
    private Vector2 tmp = new Vector2();

    public void checkAndHandleBounds(AbstractPoolable poolable) {
        if (poolable.getPosition().x + poolable.getWidth() / 2 < 0)
            poolable.destroy();
        if (poolable.getPosition().x - poolable.getWidth() / 2 > Rules.SPACE_WIDTH)
            poolable.destroy();
        if (poolable.getPosition().y + poolable.getHeight() / 2 < 0)
            poolable.destroy();
        if (poolable.getPosition().y - poolable.getHeight() / 2 > Rules.SPACE_HEIGHT)
            poolable.destroy();
    }

    public void checkAndHandleBounds(Shuttle player, float dt) {
        if (player.getPosition().x + player.getWidth() / 2 < 0)
            checkCount(player, dt);
        else if (player.getPosition().x - player.getWidth() / 2 > Rules.SPACE_WIDTH)
            checkCount(player, dt);
        else if (player.getPosition().y + player.getHeight() / 2 < 0)
            checkCount(player, dt);
        else if (player.getPosition().y - player.getHeight() / 2 > Rules.SPACE_HEIGHT)
            checkCount(player, dt);
        else {
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
            alert(player);
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

    private void alert(Shuttle player) {
        tmp.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        ScreenManager.getInstance().getViewport().unproject(tmp);
        player.getGs().getGameInfoSystem().setup(
                tmp.x - Rules.WORLD_WIDTH / 2,
                tmp.y + Rules.WORLD_HEIGHT / 2,
                GameInfoSystem.GameInfoSystemPreset.OUT,
                3,
                true
        );
    }
}