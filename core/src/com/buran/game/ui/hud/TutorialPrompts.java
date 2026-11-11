package com.buran.game.ui.hud;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.buran.game.campaign.Campaign;
import com.buran.game.campaign.SectorRun;
import com.buran.game.conf.Rules;
import com.buran.game.input.Gamepad;
import com.buran.game.player.Buran;
import com.buran.game.ui.groups.PauseGroup;
import com.buran.game.ui.groups.selector.Selector;
import com.buran.game.ui.groups.selector.SelectorGroup;
import com.buran.game.ui.menu.Hints;

/**
 * Button prompts that appear the first time a button is needed and leave once it has been
 * pressed (tips 4, 28): fly, use the item in the bay, switch items. World pass, under the ship.
 */
public class TutorialPrompts {
    private float time;
    private boolean steered;
    private final float[] shown = new float[4];

    /** Seconds left of the "don't ram, burn it" lesson after a rock hit the hull. */
    private static float ramLesson;
    private static int ramCount;

    /** A whole rock hit the ship. */
    /**
     * A rock hit: say what rocks are, in words that agree with the rest of the game. The ram
     * shield breaks anomalies, not rocks; the laser is for rocks.
     */
    public static void rammed() {
        ramCount++;
        if (ramCount > 3) {
            return;
        }
        String line;
        if (Buran.ramOn) {
            line = "THE RAM SHIELD SOFTENS ROCKS, IT DOES NOT BREAK THEM";
        } else if (Buran.isLaser) {
            line = "ROCKS HURT  -  LET THE LASER BURN THEM: KEEP THEM IN THE RED RING";
        } else {
            line = "ROCKS HURT  -  GO AROUND THEM";
        }
        Comms.say(line, 5f, Comms.TIP);
    }

    /** The EMP hint asks for the prompt slot (set every frame while an electric one is near). */
    public static float empRequest;

    public TutorialPrompts() {
        ramLesson = 0f;
        ramCount = 0;
        SelectorGroup.usedOnce = false;
        SelectorGroup.switchedOnce = false;
    }

    private static boolean ramTried;
    private String said;
    private float sayTime;

    private void say(String line) {
        Comms.say(line, 3f, Comms.TIP);
    }

    /** World y of the camera centre (prompts stay clear of the console). */
    public static float cameraY;

    public void update(float dt) {
        time += dt;
        ramLesson = Math.max(0f, ramLesson - dt);
        SectorRun run = Campaign.run();
        boolean live = !Rules.GAME_OVER && !PauseGroup.isPause() && (run == null || (!run.isBriefing() && !run.isClear()));
        if (Gamepad.get().isSteering()) {
            steered = true;
        }
        SelectorGroup sg = SelectorGroup.getInstance();
        Selector c = sg.getCenter();
        boolean fly = live && run != null && run.sector().number == 1 && !steered;
        boolean use = live && c != null && c.isActive() && !SelectorGroup.usedOnce;
        boolean sw = live && sg.getSelectorCount() >= 2 && SelectorGroup.usedOnce && !SelectorGroup.switchedOnce;
        // The ram shield: offered once there is charge for it and it has never been switched.
        boolean ram = live && !Buran.ramOn && !ramTried
                && com.buran.game.player.Standing.getInstance().getRamPower() >= 1f;
        if (Buran.ramSwitchAge < 0.05f && !Buran.ramDenied) {
            ramTried = true;
        }
        if (Buran.ramDenied && Buran.ramSwitchAge < 0.05f) {
            say("RAM SHIELD EMPTY  -  PULL IN METAL TO CHARGE IT");
        }
        if (Gamepad.get().takePulseDenied()) {
            new com.buran.game.effects.BoomBox().playSound(com.buran.game.conf.MSConstants.RAM_DENIED);
            say("EMP NEEDS SHIELD  -  WAIT FOR IT TO RECHARGE");
        }
        sayTime = Math.max(0f, sayTime - dt);
        boolean[] want = {fly, use, sw, ram};
        for (int i = 0; i < want.length; i++) {
            shown[i] = MathUtils.clamp(shown[i] + (want[i] ? dt * 4f : -dt * 4f), 0f, 1f);
        }
    }

    /** Whole rocks within this distance make the laser reach visible. */
    private static final float LASER_SHOW = 1100f;
    private static final com.badlogic.gdx.graphics.Color LASER = com.badlogic.gdx.graphics.Color.valueOf("ff6a5aff");
    private float laserRing;

    public void render(Batch batch, BitmapFont font, com.buran.game.world.ThreatTextures tx) {
        float pulse = 0.8f + 0.2f * MathUtils.sin(time * 5f);
        float x = Buran.position.x;
        float y = Buran.position.y - 280f;
        // The laser's reach around the ship while a whole rock is near: get it inside, don't touch.
        boolean rock = Buran.isLaser && com.buran.game.player.Standing.getInstance().getLaserChargeLevel() > 0f && rockNear();
        laserRing = MathUtils.clamp(laserRing + (rock ? 0.05f : -0.05f), 0f, 1f);
        if (laserRing > 0f && !Rules.GAME_OVER) {
            float r = Rules.PLAYER_LASER_TAKE_RANGE;
            batch.setColor(LASER.r, LASER.g, LASER.b, laserRing * (0.22f + 0.08f * MathUtils.sin(time * 3f)));
            batch.draw(tx.ring, x - r, y + 280f - r, r * 2f, r * 2f);
            batch.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        }
        // One button prompt at a time, the one that matters most right now; never over the console.
        float lowest = cameraY - Rules.VIEW_HEIGHT / 2f + Rules.VIEW_HEIGHT * 0.3f;
        y = Math.max(y, lowest);
        Selector c = SelectorGroup.getInstance().getCenter();
        if (empRequest > 0f) {
            Prompt.draw(batch, font, Hints.button("Y", "F"), "EMP", x, y, empRequest * pulse);
        } else if (shown[0] > 0f) {
            Prompt.draw(batch, font, Hints.button("LS", "ARROWS"), "FLY", x, y, shown[0] * pulse);
        } else if (shown[3] > 0f) {
            Prompt.draw(batch, font, Hints.button("A", "SPACE"), "RAM SHIELD", x, y, shown[3] * pulse);
        } else if (shown[1] > 0f && c != null) {
            Prompt.draw(batch, font, Hints.button("X", "X"), "USE " + c.getSticker().name(), x, y, shown[1] * pulse);
        } else if (shown[2] > 0f) {
            String[] keys = Hints.pad() ? new String[]{"LB", "RB"} : new String[]{"Q", "E"};
            Prompt.draw(batch, font, keys, "SWITCH ITEM", x, y, shown[2] * pulse);
        }
        empRequest = 0f;
    }

    private static boolean rockNear() {
        for (Class<?> k : new Class<?>[]{com.buran.game.bot.asteroids.MetalAsteroid.class, com.buran.game.bot.asteroids.MineralAsteroid.class}) {
            com.buran.game.utils.pool.ObjectPool<?> p = com.buran.game.utils.pool.ObjectPool.EMITTERS.get(k);
            if (p == null) {
                continue;
            }
            for (Object o : p.getActiveList()) {
                com.buran.game.bot.asteroids.AbstractAsteroid a = (com.buran.game.bot.asteroids.AbstractAsteroid) o;
                if (!a.isFragmented() && a.getPosition().dst2(Buran.position) < LASER_SHOW * LASER_SHOW) {
                    return true;
                }
            }
        }
        return false;
    }
}
