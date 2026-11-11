package com.buran.game.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.buran.game.background.CloudSky;
import com.buran.game.conf.MSConstants;
import com.buran.game.conf.Rules;
import com.buran.game.conf.VConstants;
import com.buran.game.effects.BoomBox;
import com.buran.game.effects.Shake;
import com.buran.game.effects.Vibrator;
import com.buran.game.player.Buran;
import com.buran.game.player.Standing;
import com.buran.game.screens.ScreenManager;
import com.buran.game.ui.GameInfoSystem;
import com.buran.game.utils.pool.AbstractPoolable;

/**
 * @author soloyes on 8/26/18.
 */

public class BoundCheck {
    private float alertTimer = 1.0f;
    private float hpTimer = Rules.PLAYER_TIMER_HP_OUT_OF_MAP - 0.35f;
    private float fuelTimer;
    private Rectangle rectangle;
    private BoomBox boomBox;
    public static boolean playerOnScreen;

    public BoundCheck() {
        this.rectangle = new Rectangle();
        this.boomBox = new BoomBox();
    }

    public static void avoidPosition(Vector2 from, Vector2 to, int near, int x1, int x2, int y1, int y2) {
        //Prevent generation near given position
        while (from.dst(to) <= near) {
            to.set(MathUtils.random(x1, x2), MathUtils.random(y1, y2));
        }
    }

    public void checkAndHandleBounds(Buran player, float dt) {
        // The dashed line itself is the wall: crossing it hurts at once.
        rectangle.set(0f, 0f, Rules.SPACE_WIDTH, Rules.SPACE_HEIGHT);
        if (!rectangle.contains(Buran.position)) {
            // Training sectors: the edge only pushes back, it does not hurt.
            com.buran.game.campaign.Sector sec = com.buran.game.campaign.Campaign.current();
            boolean harm = sec == null || sec.borderDamage;
            if (harm) {
                checkCount(dt);
            }
            playerOnScreen = false;
            pushBack(dt);
            if (harm && Buran.position.x >= Rules.SPACE_WIDTH + 1.5f * Rules.VIEW_WIDTH ||
                    Buran.position.x <= -1.5f * Rules.VIEW_WIDTH ||
                    Buran.position.y >= Rules.SPACE_HEIGHT + 1.5f * Rules.VIEW_HEIGHT ||
                    Buran.position.y <= -1.5f * Rules.VIEW_HEIGHT) {
                Standing.getInstance().changeHP(-Standing.getInstance().getHp());
                CloudSky.bcDx = 0.0f;
                CloudSky.mcDx = 0.0f;
                CloudSky.scDx = 0.0f;
                CloudSky.bcDy = 0.0f;
                CloudSky.mcDy = 0.0f;
                CloudSky.scDy = 0.0f;
            }
        } else {
            // Primed so the next crossing alerts and bites immediately.
            alertTimer = 1.0f;
            fuelTimer = 0;
            hpTimer = Rules.PLAYER_TIMER_HP_OUT_OF_MAP - 0.35f;
            Standing.getInstance().setFuelAlertOutOfBounds(false);
            Standing.getInstance().setHpAlert(false);
            playerOnScreen = true;
        }
    }

    private final Vector2 overshoot = new Vector2();

    /**
     * Soft wall: past the border a spring pulls the Buran back and outward speed is damped,
     * so the ship cannot drift far out and the border always stays in view.
     */
    private void pushBack(float dt) {
        float cx = Math.max(0f, Math.min(Rules.SPACE_WIDTH, Buran.position.x));
        float cy = Math.max(0f, Math.min(Rules.SPACE_HEIGHT, Buran.position.y));
        overshoot.set(cx - Buran.position.x, cy - Buran.position.y);
        float dist = overshoot.len();
        if (dist <= 0f) {
            return;
        }
        Buran.velocity.mulAdd(overshoot, Rules.BORDER_SPRING * dt);
        overshoot.nor();
        float outward = -Buran.velocity.dot(overshoot);
        if (outward > 0f) {
            // Bleed off the speed that carries the ship further out.
            Buran.velocity.mulAdd(overshoot, outward * Math.min(1f, Rules.BORDER_DAMPING * dt));
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
                    "[RED]CRITICAL DAMAGE[]",
                    3,
                    true
            );
            boomBox.playSound(MSConstants.LOW_LIFE_WARNING);
            com.buran.game.effects.Juice.flash(0.18f, 1f, 0.15f, 0.1f);
            Shake.getInstance().shakeCamera(duration, 35, 10);
            Vibrator.vibrateGame(VConstants.PLAYER_OUT_OF_SCREEN);
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

    /**
     * Keeps a drifting object inside the sector: it bounces off the border instead of
     * vanishing behind it (the camera can see a bit past the edge).
     */
    public void keepInside(AbstractPoolable poolable) {
        keepInside(poolable, 0f);
    }

    /** Same, with an extra margin past the border (hunting anomalies follow the ship out). */
    public void keepInside(AbstractPoolable poolable, float margin) {
        Vector2 p = poolable.getPosition();
        Vector2 v = poolable.getVelocity();
        float rx = poolable.getWidth() / 2f - margin;
        float ry = poolable.getHeight() / 2f - margin;
        if (p.x < rx) {
            p.x = rx;
            if (v != null && v.x < 0) v.x = -v.x;
        } else if (p.x > Rules.SPACE_WIDTH - rx) {
            p.x = Rules.SPACE_WIDTH - rx;
            if (v != null && v.x > 0) v.x = -v.x;
        }
        if (p.y < ry) {
            p.y = ry;
            if (v != null && v.y < 0) v.y = -v.y;
        } else if (p.y > Rules.SPACE_HEIGHT - ry) {
            p.y = Rules.SPACE_HEIGHT - ry;
            if (v != null && v.y > 0) v.y = -v.y;
        }
    }

    /**
     * For objects that fly through the sector (asteroids): opacity fades from 1 at the border
     * to 0 half a size outside it, where the object is recycled. No popping in or out.
     *
     * @return the opacity to draw the object with
     */
    public float edgeFade(AbstractPoolable poolable) {
        Vector2 p = poolable.getPosition();
        float half = Math.max(poolable.getWidth(), poolable.getHeight()) / 2f;
        float out = Math.max(Math.max(-p.x, p.x - Rules.SPACE_WIDTH), Math.max(-p.y, p.y - Rules.SPACE_HEIGHT));
        if (out >= half) {
            poolable.destroy();
            return 0f;
        }
        return out <= 0f ? 1f : 1f - out / half;
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
                camera.position.x - Rules.VIEW_WIDTH / 2f,
                camera.position.y - Rules.VIEW_HEIGHT / 2f,
                Rules.VIEW_WIDTH, Rules.VIEW_HEIGHT
        );
        return rectangle.contains(poolable.getPosition());
    }
}